package com.cattia.config;

import com.cattia.repository.ProductRepository;
import com.cattia.repository.RefugeeRepository;
import com.cattia.repository.StockHistoryRepository;
import com.cattia.repository.UserAccountRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Run application with Envronment variables: SPRING_PROFILES_ACTIVE=devlocal
 */

@Configuration
@Profile("devlocal")
public class DevlocalRepositoryProvider {

    @Bean
    InitializingBean sendDatabase(RefugeeRepository refugeeRepository, ProductRepository productRepository, StockHistoryRepository stockHistoryRepository, UserAccountRepository userAccountRepository) {
        return () -> {
            var allProducts = RandomDataGenerator.getAllProducts();
            userAccountRepository.saveAll(RandomDataGenerator.getUserAccounts());
            refugeeRepository.saveAll(RandomDataGenerator.getAllRefugees());
            productRepository.saveAll(allProducts);
            stockHistoryRepository.saveAll(RandomDataGenerator.getAllStockHistory(allProducts));
        };
    }


}