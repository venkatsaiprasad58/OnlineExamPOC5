package com.ojas.service;

import java.util.Arrays;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ojas.model.Role;
import com.ojas.model.User;
import com.ojas.repository.RoleRepository;
import com.ojas.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User findUserByPhoneNumber(Long phoneNumber) {
		return userRepository.findByPhoneNumber(phoneNumber);
	}

	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(true);
		Role userRole = roleRepository.findByRole("USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
	}
	
	

}
