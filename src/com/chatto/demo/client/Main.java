package com.chatto.demo.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;

	public Main(String selectedName) {
	    setTitle("与" + selectedName + "聊天中");
	    addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    setSize(screenWidth / 2, screenHeight / 2);
	    setLocation(screenWidth / 3, screenHeight / 4);
	    setVisible(true);
	  }

	  public static void main(String[] args) {
	    JFrame frame = new Main("TEST");
	    frame.setVisible(true);
	  }
}
