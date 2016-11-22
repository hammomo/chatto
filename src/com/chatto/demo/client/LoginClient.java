package com.chatto.demo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.chatto.demo.info.GlobalData;

public class LoginClient {
	
	private String username;
	private String password;
	private String address;
	private int port;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private BufferedReader inFromServer;
	private PrintWriter pw;
	private boolean result;
	
	public LoginClient(String username, String password) {
		address = GlobalData.ADDRESS;
		port = GlobalData.LOGIN_PORT;
		this.username = username;
		this.password = password;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public boolean openConnection() {
		boolean openResult;
		try {
			socket = new Socket(address, port);
			openResult = true;
		} catch (UnknownHostException e) {
			openResult = false;
			e.printStackTrace();
		} catch (IOException e) {
			openResult = false;
			e.printStackTrace();
		}
		return openResult;
	}
	
	public void sendUserInfo() {
		try {
			out = socket.getOutputStream();
			pw = new PrintWriter(out);
			pw.write(username + '\n');
			pw.flush();
			pw.write(password + '\n');
			pw.flush();
//			socket.shutdownOutput();   // 暂时先不用关闭
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getServerRequest() {
		try {
			in = socket.getInputStream();
			inFromServer = new BufferedReader(new InputStreamReader(in));
			result = Boolean.parseBoolean(inFromServer.readLine());
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
			System.out.println("The socket isn't closed yet. It should be delivered to ClientInter.");
			System.out.println("IP address: " +  socket.getInetAddress() + "\tPort: " + socket.getPort() + "\tLocal port: " + socket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
