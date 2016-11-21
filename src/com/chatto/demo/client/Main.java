package com.chatto.demo.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;

	public Main() {
	    setTitle("CenteredFrame");
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
	    setLocation(screenWidth / 10, screenHeight / 4);
	  }

	  public static void main(String[] args) {
	    JFrame frame = new Main();
	    frame.setVisible(true);
	  }
}
