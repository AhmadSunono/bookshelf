package com.sonouno.bookshelf.controller;

import com.sonouno.bookshelf.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

	@GetMapping("/validate")
	public ResponseEntity<User.UserDto> validateUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) authentication.getPrincipal();
		return new ResponseEntity<>(userDetails.toDto(), HttpStatus.OK);
	}

}
