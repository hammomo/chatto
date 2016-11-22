package com.chatto.demo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.chatto.demo.info.GlobalData;
import com.chatto.demo.server.mysql.MySQLConnect;

public class LoginServer implements Runnable {
	
	private ServerSocket loginServerSocket;
	private Socket clientSocket;
	private InputStream in;
	private OutputStream out;
	private BufferedReader inFromClient;
	private PrintWriter pw;
	private String username;
	private String password;
	private boolean loginResult;
	private boolean running = false;
	private Thread run, manage;
	
	private List<OnlineClient> onlineUsers = new ArrayList<OnlineClient>();
	
	public LoginServer() {
		try {
			loginServerSocket = new ServerSocket(GlobalData.LOGIN_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		run = new Thread(this, "Login Server");
		run.start();
	}

	public void run() {
		running = true;
		System.out.println("Login server started on port: " + GlobalData.LOGIN_PORT);
		while (running) {
			manageClients();
			try {
				clientSocket = loginServerSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			getClientInfo();
			sendRequest();
			// 关闭相关资源
//			try {
//				if(pw != null)
//					pw.close();
//				if(out != null)
//					out.close();
//				if(in != null)
//					in.close();
//				if(inFromClient != null)
//					inFromClient.close();
//				if(clientSocket != null) {
////					removeOnlineUsers(clientSocket); 	// 关闭TCP连接
//					clientSocket.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} 
			
		}
	}
	
	public void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					System.out.println("Online Users:");
					for(int i = 0; i < onlineUsers.size(); i++) {
						OnlineClient oc = onlineUsers.get(i);
						System.out.println("username: " + oc.getUsername() 
						+ "\tip address: " + oc.getAddress() + "\tport: " + oc.getPort() );
						
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		manage.start();
	}
	
	public void getClientInfo() {
		try {
			System.out.println(clientSocket.getInetAddress() + " : \t" + clientSocket.getPort());
			in = clientSocket.getInputStream();
			inFromClient = new BufferedReader(new InputStreamReader(in));
			username = inFromClient.readLine();
			password = inFromClient.readLine();
			System.out.println("username: " + username);
			System.out.println("password: " + password);
			storeOnlineUsers();
			clientSocket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 将在线用户存入list中
	public void storeOnlineUsers() {
		onlineUsers.add(new OnlineClient(username, clientSocket.getInetAddress(), clientSocket.getPort()));
	}
	
	// 客户端关闭后将在线用户从list中移出
	public void removeOnlineUsers(Socket socket) {
		for(int i = 0; i < onlineUsers.size(); i++) {
			OnlineClient oc = onlineUsers.get(i);
			if (socket.getInetAddress().toString().equals(oc.getAddress().toString()) && socket.getPort() == oc.getPort()) {
				onlineUsers.remove(i);
				break;
			}
		}
	}
	
	public void sendRequest() {
		try {
			loginResult = MySQLConnect.checkPassword(username, password);
			out = clientSocket.getOutputStream();
			pw = new PrintWriter(out);
			pw.write(loginResult + "\n");
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
