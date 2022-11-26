package com.cattia.security;

import com.cattia.model.UserAccount;
import com.cattia.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount user = userAccountRepository.getUserByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("Could not find User");
		}
		return new UserAccountDetail(user);
	}

}
