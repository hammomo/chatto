package com.chatto.demo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class ClientInter {
	
	private Socket socket;
	private String username;
	private InetAddress address;
	private int port;
	private InputStream in;
	private OutputStream out;
	private BufferedReader inFromServer;
	private PrintWriter pw;
	private boolean connection;
	private Thread getUsersUpdate;
	
	// 用于传递参数的constructor
	public ClientInter (Socket socket, String username) { 
		this.socket = socket; 	// 传递登录时所用的socket
		this.username = username; 	// 传递登录时的用户名
		address = socket.getInetAddress();
		port = socket.getPort();
		System.out.println("LOL!!! I'm back... The client is still running: ");
		System.out.println("Username: " + username + "\tIP address: " + socket.getInetAddress() + "\tPort: " + socket.getLocalPort() + "\tServer port: " + socket.getPort());
		openResources();
	}
	
	public void openResources() {
		try {
			out = socket.getOutputStream();
			pw = new PrintWriter(out);
			in = socket.getInputStream();
			inFromServer = new BufferedReader(new InputStreamReader(in));
			System.out.println("Resources have been opened again...");
			connection = true;
		} catch (IOException e) {
			System.out.println("I'm dead. I can only ruin everything.");
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public void sendCloseInfo() {
		pw.write("/q/" + username +"\n");
		pw.flush();
	}
	
	public void getUsersUpdate() {
		getUsersUpdate = new Thread("Update") {
			public void run() {
				String str = "";
				while (connection) {
					try {
						pw.write("/u/" + username + '\n');
						pw.flush();
						str = inFromServer.readLine();
						System.out.println(username + " : ");
						String[] u = str.split("/u/|/n/|/e/");
						String[] users = Arrays.copyOfRange(u, 1, u.length);
						for (int i = 0; i < users.length; i++) {
							System.out.println(users[i]);
						}
						Thread.sleep(10000);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		getUsersUpdate.start();
	}
	
	public void closeClient() {
		sendCloseInfo();
		System.out.println("I've send close information to server in order to tell it to close this socket.");
		try {
			inFromServer.close();
			in.close();
			pw.close();
			out.close();
			socket.close();
			System.out.println("Now I close myself.");
		} catch (IOException e) {
			System.out.println("OMG! Something wrong happened! And I have no idea how to handle it... That's aweful.");
			e.printStackTrace();
		}
	}
	
	
}
