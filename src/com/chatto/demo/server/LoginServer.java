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
	private Thread run;
	
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
			try {
				clientSocket = loginServerSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			getClientInfo();
			sendRequest();
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
	
	public void getClientInfo() {
		try {
			System.out.println(clientSocket.getInetAddress() + " : \t" + clientSocket.getPort());
			in = clientSocket.getInputStream();
			inFromClient = new BufferedReader(new InputStreamReader(in));
			username = inFromClient.readLine();
			password = inFromClient.readLine();
			System.out.println("username: " + username);
			System.out.println("password: " + password);
			clientSocket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
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
