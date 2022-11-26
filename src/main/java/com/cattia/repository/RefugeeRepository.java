package com.cattia.repository;

import com.cattia.entity.Refugee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefugeeRepository extends JpaRepository<Refugee, Long> {

    @Query("SELECT r FROM Refugee r WHERE r.active = 1")
    List<Refugee> findAllActiveRefugees();

    @Modifying
    @Query("UPDATE Refugee r SET identification_number=?2, first_name=?3, last_name=?4 WHERE r.id = ?1")
    int updateRefugee(Long id, String identificationNumber, String firstName, String lastName);

    List<Refugee> findByIdentificationNumber(String identificationNumber);

    @Modifying
    @Query("update Refugee r set r.active = ?2 where r.id = ?1")
    int updateRefugeeStatus(Long id, int refugeeStatus);
}