package com.cattia.repository;

import com.cattia.entity.RefugeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefugeeOrderRepository extends JpaRepository<RefugeeOrder, Long> {

    @Modifying
    @Query("delete from RefugeeOrder ro where ro.id = :orderId")
    void deleteOrderById(@Param("orderId") Long orderId);

}
