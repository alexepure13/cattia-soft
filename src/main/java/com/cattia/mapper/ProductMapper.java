package com.cattia.mapper;

import com.cattia.dto.*;
import com.cattia.entity.Product;
import com.cattia.entity.StockHistory;
import com.cattia.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.cattia.model.UserAccount;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final Util util;

    public ProductFormDto mapProductDto(Product product) {
        return ProductFormDto.builder()
                .id(product.getId())
                .category(product.getCategory())
                .code(product.getCode())
                .name(product.getName())
                .unitOfMeasurement(product.getUnitOfMeasurement())
                .build();
    }

    public List<ProductOverviewDto> mapProductOverviewDtoList(List<Product> productList) {
        return productList.stream()
                .map(this::mapProductOverviewDto)
                .collect(Collectors.toList());
    }

    private ProductOverviewDto mapProductOverviewDto(Product product) {
        BigDecimal quantity = util.calculateStockQuantity(product);

        return ProductOverviewDto.builder()
                .id(product.getId())
                .status(util.getStatus(product.getActive()))
                .code(product.getCode())
                .category(product.getCategory())
                .name(product.getName())
                .unitOfMeasurement(product.getUnitOfMeasurement().getValue())
                .quantity(quantity)
                .build();
    }

    public Product mapProduct(ProductFormDto productDto, UserAccount userAccount) {
        return Product.builder()
                .id(productDto.getId())
                .active(StatusEnum.ACTIVE.getId())
                .createdOn(LocalDateTime.now())
                .userAccount(userAccount)
                .code(productDto.getCode())
                .name(productDto.getName())
                .unitOfMeasurement(productDto.getUnitOfMeasurement())
                .category(productDto.getCategory())
                .build();
    }

    public ProductStockDto mapProductStockDto(Product product) {
        BigDecimal quantity = util.calculateStockQuantity(product);
        return ProductStockDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .category(product.getCategory().getValue())
                .unitOfMeasurement(product.getUnitOfMeasurement().getValue())
                .quantity(new BigDecimal(0))
                .availableQuantity(quantity)
                .productStockHistoryList(mapToProductStockHistoryDto(product.getStockHistoryList()))
                .build();
    }

    private List<ProductStockHistoryDto> mapToProductStockHistoryDto(List<StockHistory> stockHistoryList) {
        return stockHistoryList.stream()
                .map(this::mapToProductStockHistoryDto)
                .sorted(Comparator.comparing(ProductStockHistoryDto::getCreatedOn).reversed())
                .collect(Collectors.toList());
    }

    private ProductStockHistoryDto mapToProductStockHistoryDto(StockHistory stockHistory) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ProductStockHistoryDto.builder()
                .id(stockHistory.getId())
                .createdOn(stockHistory.getCreatedOn().format(formatter))
                .createdBy(stockHistory.getUserAccount().getUsername())
                .action(stockHistory.getAction())
                .quantity(stockHistory.getQuantity())
                .build();
    }

    public Product mapProductToUpdate(Product product, ProductFormDto productDto) {
        product.setCode(productDto.getCode());
        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setUnitOfMeasurement(productDto.getUnitOfMeasurement());
        return product;
    }
}
