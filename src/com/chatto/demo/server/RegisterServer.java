package com.chatto.demo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.chatto.demo.info.GlobalData;
import com.chatto.demo.server.mysql.MySQLConnect;

public class RegisterServer implements Runnable {
	
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private InputStream in;
	private OutputStream out;
	private BufferedReader inFromClient;
	private Thread run;
	private String username;
	private String password;
	private int userID;
	private PrintWriter pw;
	private boolean running = false;
	private boolean exists = false;
	private String firstSign;
	
	public RegisterServer() {
			// 1.创建一个服务器端Socket，即ServerSocket，指定要绑定的端口，并监听此端口
			try {
				serverSocket = new ServerSocket(GlobalData.REGISTER_PORT);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			run = new Thread(this, "Register Server");
			run.start();
	}
	
	public void run() {
		running = true;
		System.out.println("Register Server started on port : " + GlobalData.REGISTER_PORT);
		while (running) {
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			getSign();
			
			if (firstSign.equals("/r/")) {
				getRegisterInfo();	
			} else if (firstSign.equals("/c/")) {
				getChecknameInfo();
			}
			sendServerRequest();
			// 关闭相关资源
			try {
				if(pw != null)
					pw.close();
				if(out != null)
					out.close();
				if(in != null)
					in.close();
				if(inFromClient != null)
					inFromClient.close();
				if(clientSocket != null)
					clientSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void getSign() {
		try {
			// 2.获取输入流，并读取客户端信息
			System.out.println(clientSocket.getInetAddress() + "\t" + clientSocket.getPort());
			in = clientSocket.getInputStream(); 	// 字节输入流
			inFromClient = new BufferedReader(new InputStreamReader(in)); 		// 为输入流添加缓冲
			firstSign = inFromClient.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getRegisterInfo() {
		try {
			username = inFromClient.readLine();
			password = inFromClient.readLine();
			userID = UniqueIdentifier.getIdentifier();
			System.out.println("Info From Client:");
			System.out.println("username: " + username);
			System.out.println("password: " + password);
			System.out.println("userID: " + userID);
			if (!MySQLConnect.checkUsernameExist(username)) {
				exists = false;
				MySQLConnect.createUser(username, password, userID);
			} else {
				exists = true;
			}
			clientSocket.shutdownInput(); 		// 关闭输入流
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	public void getChecknameInfo() {
		try {
			String checkname = inFromClient.readLine();
			System.out.println("The username should be checked: " + checkname);
			if (!MySQLConnect.checkUsernameExist(checkname)) {
				exists = false;
			} else {
				exists = true;
			}
			clientSocket.shutdownInput(); 		// 关闭输入流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendServerRequest() {
		// 3.获取输出流，响应客户端请求
		try {
			out = clientSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw = new PrintWriter(out);
		if (!exists) {
			pw.write("true\n");
			pw.flush();
		} else {
			pw.write("false\n");
			pw.flush();
		}
		
	}
	
	public static void main(String[] args) {
		new RegisterServer();
	}
}
