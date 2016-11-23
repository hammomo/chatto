package com.chatto.demo.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClientInterface extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtSearch;
	private ClientInter userClient;
	private JList<String> list;
	private Thread update, run, select;
	private boolean updateFlag = false, selectFlag = false;
	private String selectedName;
//	private String[] test = {"red", "yellow", "blue"};
	
	public ClientInterface(String username, Socket socket) {
		userClient = new ClientInter(socket, username);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				selectFlag = false;
				updateFlag = false;
				userClient.setConnection(false);
				System.out.println("I've press the close key.");
				update.interrupt();
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
		
		list = new JList<String>();
//		list.setListData(test);
		list.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (list.getSelectedIndex() != -1 && e.getClickCount() == 2) {
					@SuppressWarnings("unchecked")
					JList<String> theList = (JList<String>) e.getSource();
					System.out.println(username + ": " +theList.getSelectedValue());
				}
				
			}
		});
		list.setBackground(Color.WHITE);
		list.setForeground(Color.DARK_GRAY);
		list.setBounds(16, 46, 265, 414);
		contentPane.add(list);
		list.setFont(new Font("Verdana", 0, 24));
		
		txtSearch = new JTextField();
		txtSearch.setBackground(Color.DARK_GRAY);
		txtSearch.setForeground(Color.WHITE);
		txtSearch.setText("search");
		txtSearch.setBounds(131, 6, 145, 34);
		contentPane.add(txtSearch);
		txtSearch.setColumns(10);
		
		setVisible(true);
		
		updateFlag = true;
		run = new Thread(this, "Running");
		run.start();
	}
	
	public void run() {
		update();
	}
	
	private void appearLocation() {
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    setLocation(screenWidth / 12, screenHeight / 8);
	}

	public void update() {
		update = new Thread("Update") {
			public void run() {
				String onlineUsers = "";
				while (updateFlag) {
					onlineUsers = userClient.getUsersUpdate();
					String[] u = onlineUsers.split("/u/|/n/|/e/");
					String[] users = Arrays.copyOfRange(u, 1, u.length);
					List<String> theList = new ArrayList<String>();
					theList.addAll(Arrays.asList(users));
					for (int i = 0; i < theList.size(); i++) {
						if(userClient.getUsername().equals(theList.get(i))) {
							theList.remove(i);
							break;
						}
					}
					users = theList.toArray(new String[1]);
					list.setListData(users);
				}
			}
		};
		update.start();
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
