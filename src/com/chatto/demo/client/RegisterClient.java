package com.chatto.demo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

// for test import
import java.util.Scanner;

import com.chatto.demo.info.GlobalData;

public class RegisterClient {
	
	private String address;
	private int port;
	private String username;
	private String password;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private BufferedReader inFromServer;
	private PrintWriter pw;
	private boolean result;
	
	public RegisterClient(String username, String password) {
		address = GlobalData.ADDRESS;
		port = GlobalData.REGISTER_PORT;
		this.username = username;
		this.password = password;
	}
	
	public RegisterClient() {
		address = GlobalData.ADDRESS;
		port = GlobalData.REGISTER_PORT;
	}
	
	public boolean openConnection() {
		boolean openResult = false;
		try {
			socket = new Socket(address, port); // 1.创建客户端socket，指定服务器地址和端口
			openResult = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return openResult;
	}
	
	public void sendCheckUsername(String username) {
		try {
			out = socket.getOutputStream();
			pw = new PrintWriter(out);
			pw.write("/c/\n");
			pw.flush();
			pw.write(username);
			pw.flush();
			socket.shutdownOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendUserInfo() {
		try {
			// 2.获取输出流，向服务器端发送信息
			out = socket.getOutputStream(); 	// 字节输出流
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pw = new PrintWriter(out); 		// 将输出流包装为打印流
		pw.write("/r/\n");
		pw.flush();
		pw.write(username + '\n'); 	// 写入用户名
		pw.flush(); 	// 刷新缓存
		pw.write(password + '\n'); 	// 写入密码
		pw.flush(); 	// 刷新缓存
		try {
			socket.shutdownOutput(); 	// 关闭输出流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getServerRequest() {
		try {
			// 3.获取输入流，并读取服务器端的相应信息
			in = socket.getInputStream(); 		// 字节输入流
			inFromServer = new BufferedReader(new InputStreamReader(in)); 		// 封装成字符缓冲输入流
			result = Boolean.parseBoolean(inFromServer.readLine());
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getResult() {
		return result;
	}
	
	public void closeResources() {
		try {
			inFromServer.close();
			in.close();
			pw.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String test_username = input.next();
		String test_password = input.next();
		RegisterClient rc = new RegisterClient(test_username, test_password);
		if (rc.openConnection()) {
			rc.sendUserInfo();
			rc.getServerRequest();
		} else {
			System.out.println("Connection failed!");
		}
		System.out.println("The final result is " + rc.result);
		input.close();
	}
}
