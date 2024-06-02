package com.ecomm.ayush.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

	public AuthResponse() {
		// TODO Auto-generated constructor stub
	}
	private String jwt;
	private String message;
}
