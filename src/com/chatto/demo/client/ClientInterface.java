package com.chatto.demo.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class ClientInterface extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	public ClientInterface(String username) {
		
		setTitle("Chatto Client: " + username);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 500);
		appearLocation();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	private void appearLocation() {
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    setLocation(screenWidth / 12, screenHeight / 8);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientInterface frame = new ClientInterface("Test");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	


}
