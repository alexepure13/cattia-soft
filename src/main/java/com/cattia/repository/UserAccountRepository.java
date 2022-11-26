package com.cattia.repository;

import com.cattia.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findByEmail(String email);

    @Query("SELECT u from UserAccount u Where u.username = :username")
    UserAccount getUserByUsername(@Param("username") String username);

    @Modifying
    @Query("UPDATE UserAccount u SET active=:status where u.id=:id")
    int updateStatus(Long id, int status);

    @Modifying
    @Query("UPDATE UserAccount u SET password=:password where u.id=:id")
    int updatePassword(Long id, String password);

    @Modifying
    @Query("UPDATE UserAccount u SET username=:username, email=:email, firstName=:firstName, lastName=:lastName, id_role=:roleId where u.id=:id")
    int updateUserAccount(Long id, String username, String email, String firstName, String lastName, Long roleId);
}


