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
	private Thread run, manage, getInfo;
	
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
			openInResourcs();
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
	
	private String getAllOnlineUsers() {
		String users = "/u/";
		for (int i = 0; i < onlineUsers.size() ; i++) {
			users += onlineUsers.get(i).getUsername() + "/n/";
		}
		users += "/e/";
		return users;
	}

	public void openInResourcs() {
		try {
			System.out.println(clientSocket.getInetAddress() + " : \t" + clientSocket.getPort());
			in = clientSocket.getInputStream();
			inFromClient = new BufferedReader(new InputStreamReader(in));
			String str = inFromClient.readLine();
			System.out.println(str);
			if (str.startsWith("/c/")) {
				username = str.substring(3, str.length());
				password = inFromClient.readLine();
				System.out.println("username: " + username);
				System.out.println("password: " + password);
				loginResult = MySQLConnect.checkPassword(username, password);
				if (loginResult) {
					storeOnlineUsers();
					getInfo(username);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 等待从客户端发送信息，比如关闭窗口
	public void getInfo(String username) {
		getInfo = new Thread("GetInfo") {
			public void run() {
				
				Socket singleSocket = null;
				InputStream singleIn = null;
				OutputStream singleOut = null;
				BufferedReader singleInBR = null;
				PrintWriter singlePW = null;
				
				try {
					OnlineClient singleClient = getClient(username);
					singleSocket = singleClient.getSocket();
					singleIn = singleSocket.getInputStream();
					singleOut = singleSocket.getOutputStream();
					singleInBR = new BufferedReader(new InputStreamReader(singleIn));
					singlePW = new PrintWriter(singleOut);
					String message = "";
					while (singleClient.getConnection()) {
						String str = singleInBR.readLine();
						if (str.startsWith("/q/")) {
							System.out.println("Start to remove the closed client from list...");
							removeOnlineUsers(username);
							singleClient.setConnection(false);
							break;
						} else if (str.startsWith("/u/") && onlineUsers.size() >= 1) {
							String u = getAllOnlineUsers();
							singlePW.write(u + '\n');
							singlePW.flush();
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if(singlePW != null)
							singlePW.close();
						if(singleOut != null)
							singleOut.close();
						if(singleIn != null)
							singleIn.close();
						if(singleInBR != null)
							singleInBR.close();
						if(singleSocket != null)
							singleSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("The client resources have been closed...");
				}
			}
		};
		getInfo.start();
	}
	
	// 将OnlineClient从List中取出
	public OnlineClient getClient(String username) {
		OnlineClient result = null;
		for (int i = 0; i < onlineUsers.size(); i++) {
			if (onlineUsers.get(i).getUsername().equals(username)) {
				result = onlineUsers.get(i); 
				break;
			}
		}
		return result;
	}
	
	// 将在线用户存入list中
	public void storeOnlineUsers() {
		onlineUsers.add(new OnlineClient(username, clientSocket, clientSocket.getInetAddress(), clientSocket.getPort(), true));
	}
	
	// 客户端关闭后将在线用户从list中移出
	public void removeOnlineUsers(String username) {
		for(int i = 0; i < onlineUsers.size(); i++) {
			OnlineClient oc = onlineUsers.get(i);
			if (oc.getUsername().equals(username)) {
				onlineUsers.remove(i);
				break;
			}
		}
	}
	
	// 关闭clientSocket资源
//	public void closeClientResources(Socket socket) {
//		try {
//			if(pw != null)
//				pw.close();
//			if(out != null)
//				out.close();
//			if(in != null)
//				in.close();
//			if(inFromClient != null)
//				inFromClient.close();
//			if(clientSocket != null)
//				clientSocket.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//	}
	
	public void sendRequest() {
		try {
			out = clientSocket.getOutputStream();
			pw = new PrintWriter(out);
			pw.write(loginResult + "\n");
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
