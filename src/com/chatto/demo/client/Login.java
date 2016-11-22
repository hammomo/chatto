package com.chatto.demo.client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField pwdPassword;

	public Login() {
		setResizable(false);
		
		// 适应windows系统
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setVisible(true);
		setTitle("Chatto登录");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 480);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUsername = new JTextField();
		txtUsername.setText("username");
		txtUsername.setBounds(176, 83, 151, 26);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblUsername = new JLabel("用户名：");
		lblUsername.setBounds(72, 88, 61, 16);
		contentPane.add(lblUsername);
		
		pwdPassword = new JPasswordField();
		pwdPassword.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) loginAction();
			}
		});
		pwdPassword.setText("password");
		pwdPassword.setBounds(176, 175, 151, 26);
		contentPane.add(pwdPassword);
		
		JLabel lblPassword = new JLabel("密码：");
		lblPassword.setBounds(72, 180, 61, 16);
		contentPane.add(lblPassword);
		
		JButton btnLogin = new JButton("登录");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginAction();
			}
		});
		btnLogin.setBounds(135, 268, 130, 46);
		contentPane.add(btnLogin);
		
		JButton btnRegister = new JButton("注册");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Register();
			}
		});
		btnRegister.setBounds(135, 340, 130, 46);
		contentPane.add(btnRegister);
	}
	
	public void loginAction() {
		@SuppressWarnings("deprecation")
		LoginClient lc = new LoginClient(txtUsername.getText(), pwdPassword.getText());
		if (lc.openConnection()) {
			lc.sendUserInfo();
			lc.getServerRequest();
		} else {
			System.out.println("Connection failed.");
		}
		if (lc.getResult()) {
			dispose();
			System.out.println("Login succeed!");
//			lc.closeResources();
			new ClientInterface(getName(), lc.getSocket());
		} else {
			System.out.println("Login failed!");
			txtUsername.setText("");
			pwdPassword.setText("");
		}
	}
	
	public String getName() {
		return txtUsername.getText();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
