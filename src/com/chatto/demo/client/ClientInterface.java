package com.chatto.demo.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

public class ClientInterface extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtSearch;
	private ClientInter userClient;
	
	public ClientInterface(String username, Socket socket) {
		userClient = new ClientInter(socket, username);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("I've press the close key.");
				userClient.closeClient();
			}
		});
		
		setBackground(Color.CYAN);
		setResizable(false);
		
		setTitle("Chatto Client: " + username);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 500);
		appearLocation();
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsers = new JLabel("在线用户：");
		lblUsers.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblUsers.setForeground(Color.BLACK);
		lblUsers.setBounds(24, 8, 84, 28);
		contentPane.add(lblUsers);
		
		JList list = new JList();
		list.setBackground(Color.WHITE);
		list.setForeground(Color.DARK_GRAY);
		list.setBounds(16, 46, 265, 414);
		contentPane.add(list);
		
		txtSearch = new JTextField();
		txtSearch.setBackground(Color.DARK_GRAY);
		txtSearch.setForeground(Color.WHITE);
		txtSearch.setText("search");
		txtSearch.setBounds(131, 6, 145, 34);
		contentPane.add(txtSearch);
		txtSearch.setColumns(10);
		
		setVisible(true);
	}
	
	private void appearLocation() {
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    setLocation(screenWidth / 12, screenHeight / 8);
	}

	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ClientInterface frame = new ClientInterface("Test");
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}
}
