package com.cattia.service;

import com.cattia.dto.UserAccountEditFormDto;
import com.cattia.dto.UserAccountFormDto;
import com.cattia.dto.UserAccountOverviewDto;
import com.cattia.dto.UserDto;
import com.cattia.mapper.UserAccountMapper;
import com.cattia.model.UserAccount;
import com.cattia.repository.UserAccountRepository;
import com.cattia.util.UserEntityToDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;

    public UserAccountFormDto getUserAccountById(Long userAccountId) {
        Optional<UserAccount> userAccount = userAccountRepository.findById(userAccountId);
        if (userAccount.isPresent()) {
            return userAccountMapper.mapUserAccountFormDto(userAccount.get());
        }
        return null;
    }

    public List<UserAccountOverviewDto> getAllUserAccounts() {
        List<UserAccount> userAccountList = userAccountRepository.findAll();
        return userAccountMapper.mapUserAccountOverviewDtoList(userAccountList);
    }

    public UserAccount saveNewUserAccount(UserAccountFormDto userAccount) {
        return userAccountRepository.save(userAccountMapper.mapUserAccount(userAccount));
    }

    public UserAccount getOperator(Long id) {//TODO Nu returna o entitate JPA in controller, foloseste un mapper si returneaza un UserAccountDto
        return userAccountRepository.findById(id).get();
    }

    public UserAccount findByUserAccount(String username) {
        Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);
        if (userAccount.isPresent()) {
            return userAccount.get();
        }
        return null;
    }

    public UserAccount findByEmail(String email) {
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(email);
        if (userAccount.isPresent()) {
            return userAccount.get();
        }
        return null;
    }

    @Transactional
    public int deactivateUserAccount(Long userAccountId) {
        return userAccountRepository.updateStatus(userAccountId, 0);
    }

    @Transactional
    public int activateUserAccount(Long userAccountId) {
        return userAccountRepository.updateStatus(userAccountId, 1);
    }

    @Transactional
    public int updateUserAccountPassword(UserAccountFormDto userAccount) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        var password = encoder.encode(userAccount.getPassword());
        return userAccountRepository.updatePassword(userAccount.getId(), password);
    }

    @Transactional
    public int updateUserAccount(UserAccountEditFormDto userAccount) {
        return userAccountRepository.updateUserAccount(userAccount.getId(), userAccount.getUsername(), userAccount.getEmail(), userAccount.getFirstName(), userAccount.getLastName(), userAccount.getRole().getId());
    }

    public boolean isAdmin(String username) {
        var userAccount = findByUserAccount(username);
        if ("ROLE_ADMINISTRATOR".equals(userAccount.getRole().getRole())){
            return true;
        }
        return false;
    }
}
