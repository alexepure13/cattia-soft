package com.cattia.repository;

import com.cattia.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Modifying
    @Query("delete from OrderItem oi where oi.id = :orderItemId")
    void deleteByOrderId(@Param("orderItemId") Long orderItemId);

}
