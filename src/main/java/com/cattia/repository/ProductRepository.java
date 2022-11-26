package com.cattia.repository;

import com.cattia.entity.Product;
import com.cattia.entity.ProductCategoryEnum;
import com.cattia.entity.UnitOfMeasurementEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.active = 1")
    List<Product> findAllActiveProducts();

    List<Product> findByCode(String code);

    @Modifying
    @Query("update Product p set p.active = ?2 where p.id = ?1")
    int updateProductStatus(Long productId, int active);

    @Modifying
    @Query("UPDATE Product p SET code=:code, name=:name, category=:category, unit_of_measurement=:unitOfMeasurement WHERE p.id=:id")
    int updateProduct(Long id, String code, String name, @Param("category") ProductCategoryEnum category, @Param("unitOfMeasurement") UnitOfMeasurementEnum unitOfMeasurement);
}
