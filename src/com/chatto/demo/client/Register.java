package com.chatto.demo.client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Register extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtUsername;
	
	private String username;
	private String password;
	private String passwordConfirmation;
	private JPasswordField pwdPassword;
	private JPasswordField pwdConfirmation;
	
	public Register() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		setVisible(true);
		setTitle("Chatto注册");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 480);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUsername = new JTextField();
		txtUsername.setText("username");
		txtUsername.setBounds(129, 70, 200, 26);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblUsername = new JLabel("用户名：");
		lblUsername.setBounds(26, 64, 91, 39);
		contentPane.add(lblUsername);
		
		JButton btnCheck = new JButton("检查用户名是否可用？");
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String usernameCheck = txtUsername.getText();
				RegisterClient rc = new RegisterClient();
				if (rc.openConnection()) {
					rc.sendCheckUsername(usernameCheck);
					rc.getServerRequest();
				} else {
					System.out.println("Connection failed!");
				}
				boolean check = rc.getResult();
				if (check) {
					JOptionPane.showMessageDialog(null, "用户名可用！", "提示", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "用户名已占用！", "错误提示", JOptionPane.ERROR_MESSAGE);
					txtUsername.setText("");
				}
				rc.closeResources();
			}
		});
		btnCheck.setBounds(129, 107, 161, 29);
		contentPane.add(btnCheck);
		
		pwdPassword = new JPasswordField();
		pwdPassword.setText("password");
		pwdPassword.setBounds(129, 164, 200, 26);
		contentPane.add(pwdPassword);
		
		JLabel lblPassword = new JLabel("密码：");
		lblPassword.setBounds(26, 169, 91, 16);
		contentPane.add(lblPassword);
		
		pwdConfirmation = new JPasswordField();
		pwdConfirmation.setText("confirmation");
		pwdConfirmation.setBounds(129, 240, 200, 26);
		contentPane.add(pwdConfirmation);
		
		JLabel lblConfirm = new JLabel("确认密码：");
		lblConfirm.setBounds(26, 245, 91, 16);
		contentPane.add(lblConfirm);
		
		JButton btnRegister = new JButton("注册");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean info = registerInfo();
				if (info) {
					RegisterClient rc = new RegisterClient(username, password);
					if (rc.openConnection()) {
						rc.sendUserInfo();
						rc.getServerRequest();
					} else {
						System.out.println("Connection failed!");
					}
					System.out.println("The register action result is " + rc.getResult());
					if (rc.getResult()) {
						rc.closeResources();
						dispose();
						new Login();
					} else {
						txtUsername.setText("");
					}
				} else {
					JOptionPane.showMessageDialog(null, "密码与确认密码不一致", "错误提示", JOptionPane.ERROR_MESSAGE);					
				}
			}
		});
		btnRegister.setBounds(129, 329, 117, 39);
		contentPane.add(btnRegister);
		
		
	}
	
	@SuppressWarnings("deprecation")
	private boolean registerInfo() {
		username = txtUsername.getText();
		password = pwdPassword.getText();
		passwordConfirmation = pwdConfirmation.getText();
		
		if (password.equals(passwordConfirmation)) return true;
		else return false;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
