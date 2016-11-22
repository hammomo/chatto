package com.chatto.demo.server;

import java.net.InetAddress;

public class OnlineClient {
	
	private String username;
	private InetAddress address;
	private int port;
	
	public OnlineClient (String username, InetAddress address, int port) {
		this.username = username;
		this.address = address;
		this.port = port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
}
