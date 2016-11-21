package com.chatto.demo.client;

public class UserInfo {
	
	private String username;
	private String password;
	
	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void test() {
		System.out.println("New user created!\n");
		System.out.println("Username: " + getUsername() + "\nPassword: " + getPassword());
	}
}
