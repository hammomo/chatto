package com.chatto.demo.server;

public class ClientInfo {
	private String username;
	private String password;
	private int userID;
	
	public ClientInfo(String username, String password) {
		this.username = username;
		this.password = password;
		this.userID = UniqueIdentifier.getIdentifier();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getUserID() {
		return userID;
	}
}
