package com.cattia.util;

import com.cattia.dto.StatusEnum;
import com.cattia.entity.Product;
import com.cattia.entity.StockHistory;
import com.cattia.entity.StockHistoryActionEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Util {

    public int getRandomIntInRage(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public BigDecimal calculateStockQuantity(Product product) {
        BigDecimal quantity = new BigDecimal(0);
        for (StockHistory stockHistory : product.getStockHistoryList()) {
            if (StockHistoryActionEnum.ADD.equals(stockHistory.getAction())) {
                quantity = quantity.add(stockHistory.getQuantity());
            } else if (StockHistoryActionEnum.REMOVED.equals(stockHistory.getAction()) || StockHistoryActionEnum.ORDERED.equals(stockHistory.getAction())) {
                quantity = quantity.subtract(stockHistory.getQuantity());
            } else {
                throw new IllegalArgumentException("Unknown StockHistoryEnum.");
            }
        }
        return quantity;
    }

    public StatusEnum getStatus(int status) {
        switch (status) {
            case 0:
                return StatusEnum.INACTIVE;
            case 1:
                return StatusEnum.ACTIVE;
            case 2:
                return StatusEnum.RESIGNED;
            default:
                throw new IllegalArgumentException("Unknown StatusEnum");
        }
    }
}
