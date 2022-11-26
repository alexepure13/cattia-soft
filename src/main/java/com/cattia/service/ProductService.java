package com.cattia.service;

import com.cattia.dto.*;
import com.cattia.entity.Product;
import com.cattia.mapper.ProductMapper;
import com.cattia.repository.ProductRepository;
import com.cattia.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import com.cattia.model.UserAccount;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Util util;
    private final UserAccountService userAccountService;

    public List<ProductOverviewDto> getAllProducts() {
        return productMapper.mapProductOverviewDtoList(productRepository.findAll());
    }

    public List<ProductOverviewDto> getAllActiveAndAvailableProducts(RefugeeOrderDto refugeeOrder) {
        var activeAndAvailableProductList = productRepository.findAllActiveProducts().stream()
                .filter(this::filterProductsWithPositiveStock)
                .filter(product -> !getExcludedProducts(refugeeOrder).contains(product.getId()))
                .collect(Collectors.toList());
        return productMapper.mapProductOverviewDtoList(activeAndAvailableProductList);
    }

    private List<Long> getExcludedProducts(RefugeeOrderDto refugeeOrder) {
        if (refugeeOrder.getOpenOrder() != null && refugeeOrder.getOpenOrder().getOrderedItems() != null && !refugeeOrder.getOpenOrder().getOrderedItems().isEmpty()) {
            return refugeeOrder.getOpenOrder().getOrderedItems().stream()
                    .map(OrderItemDto::getProductId)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private boolean filterProductsWithPositiveStock(Product product) {
        BigDecimal productStock = util.calculateStockQuantity(product);
        return productStock.compareTo(BigDecimal.ZERO) > 0;
    }


    public ProductFormDto getProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return productMapper.mapProductDto(product.get());
        }
        return null;
    }

    public ProductStockDto getProductStockById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return productMapper.mapProductStockDto(product.get());
        }
        return null;
    }

    public Product saveProduct(ProductFormDto productDto, String username) {
        UserAccount userAccount = userAccountService.findByUserAccount(username);
        Product product = productMapper.mapProduct(productDto, userAccount);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(ProductFormDto productDto) {
        Optional<Product> existingProduct = productRepository.findById(productDto.getId());
        if (existingProduct.isPresent()){
            return productRepository.save(productMapper.mapProductToUpdate(existingProduct.get(), productDto));
        }
        return null;
    }

    public BigDecimal getCurrentStock(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return util.calculateStockQuantity(product.get());
        }
        return new BigDecimal(0);
    }


    public Map<Long, String> findByCode(String code) {
        List<Product> productList = productRepository.findByCode(code);
        return productList.stream()
                .collect(Collectors.toMap(Product::getId, Product::getCode));
    }

    @Transactional
    public int deactivateProduct(Long productId) {
        return productRepository.updateProductStatus(productId, 0);
    }

    @Transactional
    public int activateProduct(Long productId) {
        return productRepository.updateProductStatus(productId, 1);
    }
}
