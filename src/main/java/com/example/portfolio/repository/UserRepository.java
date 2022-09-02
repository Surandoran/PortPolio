package com.example.portfolio.repository;

import com.example.portfolio.dto.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {

    UserAccount save(UserAccount userAccount);
    Optional<UserAccount> findById(Long id);
    Optional<UserAccount> findByEmail(String email);
    List<UserAccount> findAll();

}
