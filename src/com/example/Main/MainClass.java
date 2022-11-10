package com.example.Main;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.example.Admin.Login;
import com.example.Admin.dbConneciton;

public class MainClass {

	public static void main(String args[]){
		try {
			dbConneciton.connection();
			dbConneciton.con.close();
			System.out.println("connected successfully");
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		Login lg=new Login();  
	}
}
