package com.cattia.mapper;

import com.cattia.dto.OrderDto;
import com.cattia.dto.OrderItemDto;
import com.cattia.dto.RefugeeOrderConfirmationDto;
import com.cattia.dto.RefugeeOrderDto;
import com.cattia.entity.OrderItem;
import com.cattia.entity.OrderStatusEnum;
import com.cattia.entity.Refugee;
import com.cattia.entity.RefugeeOrder;
import com.cattia.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RefugeeOrderMapper {

    private final Util util;

    public RefugeeOrderDto mapRefugeeOrderDto(Refugee refugee) {
        List<RefugeeOrder> refugeeOrderList = refugee.getOrderList();
        OrderDto openOrder = refugeeOrderList.stream()
                .filter(order -> OrderStatusEnum.OPEN.equals(order.getStatus()))
                .map(order -> mapToOrderDto(order))
                .findFirst()
                .orElseGet(()-> OrderDto.builder().id(0L).build());

        List<OrderDto> closedOrders = refugeeOrderList.stream()
                .filter(order -> OrderStatusEnum.OPEN.equals(order.getStatus()))
                .map(order -> mapToOrderDto(order))
                .collect(Collectors.toList());

        return RefugeeOrderDto.builder()
                .id(openOrder.getId())
                .refugeeId(refugee.getId())
                .identificationNumber(refugee.getIdentificationNumber())
                .firstName(refugee.getFirstName())
                .lastName(refugee.getLastName())
                .openOrder(openOrder)
                .closedProductOrderList(closedOrders)
                .build();
    }

    private OrderDto mapToOrderDto(RefugeeOrder refugeeOrder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return OrderDto.builder()
                .id(refugeeOrder.getId())
                .orderNumber(refugeeOrder.getOrderNumber())
                .createdOn(refugeeOrder.getCreatedOn().format(formatter))
                .createdBy(refugeeOrder.getUserAccount().getUsername())
                .status(refugeeOrder.getStatus().getValue())
                .orderedItems(mapOrderedItemDtoList(refugeeOrder.getOrderItemList()))
                .build();
    }

    private List<OrderItemDto> mapOrderedItemDtoList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::mapOrderedItem)
                .collect(Collectors.toList());
    }

    private OrderItemDto mapOrderedItem(OrderItem orderItem) {
        var product = orderItem.getProduct();
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getRefugeeOrder().getId())
                .productId(orderItem.getProduct().getId())
                .code(product.getCode())
                .name(product.getName())
                .unitOfMeasurement(product.getUnitOfMeasurement().getValue())
                .category(orderItem.getProduct().getCategory().getValue())
                .quantity(orderItem.getQuantity())
                .availableQuantity(util.calculateStockQuantity(orderItem.getProduct()))
                .build();
    }

    public RefugeeOrderDto mapRefugeeOrderDto(RefugeeOrder refugeeOrder){
        Refugee refugee = refugeeOrder.getRefugee();
        return RefugeeOrderDto.builder()
                .id(refugeeOrder.getId())
                .refugeeId(refugee.getId())
                .identificationNumber(refugee.getIdentificationNumber())
                .firstName(refugee.getFirstName())
                .lastName(refugee.getLastName())
                .openOrder(mapToOrderDto(refugeeOrder))
                .closedProductOrderList(null) //TODO To be filled with historic data, but how?
                .build();
    }

    public RefugeeOrderConfirmationDto mapRefugeeOrderConfirmationDto(RefugeeOrder refugeeOrder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<OrderItemDto> orderedItems = mapOrderedItemDtoList(refugeeOrder.getOrderItemList());

        OrderDto openOrder = OrderDto.builder()
                .id(refugeeOrder.getId())
                .orderNumber(refugeeOrder.getOrderNumber())
                .createdOn(refugeeOrder.getCreatedOn().format(formatter))
                .createdBy(refugeeOrder.getUserAccount().getUsername())
                .status(refugeeOrder.getStatus().getValue())
                .orderedItems(orderedItems)
                .build();

        return RefugeeOrderConfirmationDto.builder()
                .id(refugeeOrder.getRefugee().getId())
                .identificationNumber(refugeeOrder.getRefugee().getIdentificationNumber())
                .firstName(refugeeOrder.getRefugee().getFirstName())
                .lastName(refugeeOrder.getRefugee().getLastName())
                .completedOrder(openOrder)
                .build();

    }
}
