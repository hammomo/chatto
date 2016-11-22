package com.chatto.demo.server;

import java.net.InetAddress;
import java.net.Socket;

public class OnlineClient {
	
	private String username;
	private Socket socket;
	private InetAddress address;
	private int port;
	private boolean connection = false;
	
	public OnlineClient (String username, Socket socket, InetAddress address, int port, boolean connection) {
		this.username = username;
		this.socket = socket;
		this.address = address;
		this.port = port;
		this.connection = connection;
		
	}
	
	public void setConnection(boolean connection) {
		this.connection = connection;
	}
	
	public boolean getConnection() {
		return connection;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
}
