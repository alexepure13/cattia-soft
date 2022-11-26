package com.cattia.service;

import com.cattia.entity.Product;
import com.cattia.entity.StockHistory;
import com.cattia.entity.StockHistoryActionEnum;
import com.cattia.model.UserAccount;
import com.cattia.repository.StockHistoryRepository;
import com.cattia.repository.UserAccountRepository;
import com.cattia.security.UserAccountDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class StockHistoryService {

    private final StockHistoryRepository stockHistoryRepository;
    private final UserAccountService userAccountService;

    public Long saveProductHistory(Long productId, String username, StockHistoryActionEnum action, BigDecimal quantity) {
        UserAccount userAccount = userAccountService.findByUserAccount(username);
        StockHistory stockHistory = StockHistory.builder()
                .createdOn(LocalDateTime.now())
                .userAccount(userAccount)
                .action(action)
                .quantity(quantity.abs())
                .product(Product.builder().id(productId).build())
                .build();
        return stockHistoryRepository.save(stockHistory).getId();
    }

}
