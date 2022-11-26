package com.cattia.service;

import com.cattia.dto.RefugeeOrderConfirmationDto;
import com.cattia.dto.RefugeeOrderDto;
import com.cattia.dto.StockErrorMessageDto;
import com.cattia.dto.StockErrorReportDto;
import com.cattia.entity.*;
import com.cattia.mapper.RefugeeOrderMapper;
import com.cattia.repository.*;
import com.cattia.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cattia.model.UserAccount;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefugeeOrderService {

    private final RefugeeRepository refugeeRepository;
    private final ProductRepository productRepository;
    private final RefugeeOrderRepository refugeeOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RefugeeOrderMapper refugeeOrderMapper;
    private final StockHistoryRepository stockHistoryRepository;
    private final Util util;
    private final UserAccountService userAccountService;

    @Transactional
    public RefugeeOrderDto processOrder(Long refugeeId) {
        Optional<Refugee> refugee = refugeeRepository.findById(refugeeId);
        if (refugee.isPresent()) {
            return refugeeOrderMapper.mapRefugeeOrderDto(refugee.get());
        }
        return null;
    }

    public StockErrorReportDto validateStockQuantity(Long refugeeOrderId) {
        StockErrorReportDto stockErrorReport = new StockErrorReportDto();
        List<StockErrorMessageDto> stockErrorMessageList = new ArrayList<>();
        var refugeeOrder = refugeeOrderRepository.findById(refugeeOrderId);
        if (refugeeOrder.isPresent()) {
            stockErrorReport.setRefugeeId(refugeeOrder.get().getRefugee().getId());
            var openOrderItemList = refugeeOrder.get().getOrderItemList();

            openOrderItemList.stream()
                    .forEach(openOrderItem -> {
                        BigDecimal availableQuantity = util.calculateStockQuantity(openOrderItem.getProduct());
                        BigDecimal orderedQuantity = openOrderItem.getQuantity();
                        if (orderedQuantity.compareTo(availableQuantity) > 0) {
                            var stockErrorMessage = StockErrorMessageDto.builder()
                                    .code(openOrderItem.getProduct().getCode())
                                    .name(openOrderItem.getProduct().getName())
                                    .orderedQuantity(String.valueOf(orderedQuantity))
                                    .availableQuantity(String.valueOf(availableQuantity))
                                    .build();
                            stockErrorMessageList.add(stockErrorMessage);
                        }
                    });
        }
        stockErrorReport.setStockErrorMessageList(stockErrorMessageList);
        return stockErrorReport;
    }

    public RefugeeOrderConfirmationDto getRefugeeOrderConfirmationById(Long refugeeId) {
        var refugeeOrder = refugeeOrderRepository.findById(refugeeId);
        if (refugeeOrder.isPresent()) {
            var stockHistoryList = prepareOrderToComplete(refugeeOrder.get());
            stockHistoryRepository.saveAll(stockHistoryList);
            return refugeeOrderMapper.mapRefugeeOrderConfirmationDto(refugeeOrder.get());
        }
        return null;
    }

    private List<StockHistory> prepareOrderToComplete(RefugeeOrder refugeeOrder) {
        UserAccount userAccount = refugeeOrder.getUserAccount();
        refugeeOrder.setStatus(OrderStatusEnum.COMPLETED);
        return refugeeOrder.getOrderItemList().stream()
                .map(order -> mapToStockHistory(order, userAccount))
                .collect(Collectors.toList());
    }

    private StockHistory mapToStockHistory(OrderItem orderItem, UserAccount userAccount) {
        return StockHistory.builder()
                .createdOn(LocalDateTime.now())
                .userAccount(userAccount)
                .action(StockHistoryActionEnum.ORDERED)
                .quantity(orderItem.getQuantity())
                .product(orderItem.getProduct())
                .build();
    }

    @Transactional
    public Long saveOrderItem(Long orderId, Long refugeeId, Long productId, String username) {
        var product = productRepository.getOne(productId);
        var refugee = refugeeRepository.getOne(refugeeId);
        var userAccount = userAccountService.findByUserAccount(username);
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(new BigDecimal(1))
                .build();

        if (orderId == 0) { // No order exists, create a new one
            RefugeeOrder refugeeOrder = RefugeeOrder.builder()
                    .orderNumber(generateRandomOrderNumber())
                    .createdOn(LocalDateTime.now())
                    .userAccount(userAccount)
                    .status(OrderStatusEnum.OPEN)
                    .orderItemList(List.of(orderItem))
                    .refugee(refugee)
                    .build();
            orderItem.setRefugeeOrder(refugeeOrder);
            var savedOrderItem = orderItemRepository.save(orderItem);
            return savedOrderItem.getRefugeeOrder().getRefugee().getId();
        } else { // Order already exists, read it from the database
            Optional<RefugeeOrder> refugeeOrder = refugeeOrderRepository.findById(orderId);
            if (refugeeOrder.isPresent()) {
                RefugeeOrder existingRefugeeOrder = refugeeOrder.get();
                existingRefugeeOrder.getOrderItemList().add(orderItem);
                orderItem.setRefugeeOrder(existingRefugeeOrder);
                var savedOrderItem = orderItemRepository.save(orderItem);
                return savedOrderItem.getRefugeeOrder().getRefugee().getId();
            }
        }
        return 0L;
    }

    private String generateRandomOrderNumber() {
        return String.valueOf(util.getRandomIntInRage(10000000, 99999999));
    }

    public Long getOrderById(Long refugeeOrderId) {
        Optional<RefugeeOrder> refugeeOrder = refugeeOrderRepository.findById(refugeeOrderId);
        if (refugeeOrder.isPresent()) {
            return refugeeOrderMapper.mapRefugeeOrderDto(refugeeOrder.get()).getRefugeeId();
        }
        return 0L;
    }

    public Long updateOrderItemQuantity(Long orderItemId, BigDecimal quantity) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);
        if (orderItem.isPresent()) {
            orderItem.get().setQuantity(quantity);
            return orderItemRepository.save(orderItem.get()).getRefugeeOrder().getRefugee().getId();
        }
        return 0L;
    }

    @Transactional
    public Long removeOrderItem(Long orderItemId) {
        try {
            RefugeeOrder refugeeOrder = orderItemRepository.getOne(orderItemId).getRefugeeOrder();
            orderItemRepository.deleteByOrderId(orderItemId);
            deleteEmptyOrder(refugeeOrder.getId());
            return refugeeOrder.getRefugee().getId();
        } catch (Exception e) {
            return 0L;
        }
    }

    private void deleteEmptyOrder(Long orderId) {
        Optional<RefugeeOrder> refugeeOrder = refugeeOrderRepository.findById(orderId);
        if (refugeeOrder.isPresent() && refugeeOrder.get().getOrderItemList().isEmpty()) {
            refugeeOrderRepository.deleteOrderById(orderId);
        }
    }

}
