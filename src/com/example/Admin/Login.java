package com.example.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.JobAttributes;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Login extends JFrame{

	Dimension dmn=Toolkit.getDefaultToolkit().getScreenSize();
	Dimension screeSize=Toolkit.getDefaultToolkit().getScreenSize();
	JPanel mainPanel=new JPanel();
	JPanel panelNorth=new JPanel();
	JPanel panelSouth=new JPanel();
	JPanel panelWest=new JPanel();
	JPanel panelEast=new JPanel();
	JPanel panelCenter=new JPanel();
	JPanel panelCenterNorth=new JPanel();
	JPanel panelCenterSouth=new JPanel();
	JPanel panelCenterCenter=new JPanel();
	
	JLabel lblBackgroundImage=new JLabel(new ImageIcon("images/carshowroom.jpg"));

	JLabel lblUserName=new JLabel("UserName");
	JLabel lblPass=new JLabel("Password");
	
	JTextField txtUserName=new JTextField(20);
	JPasswordField txtPassword=new JPasswordField();

	JLabel lblName=new JLabel("username here...");
	JLabel lblPassword=new JLabel("password here...");

	JLabel lblLogin=new JLabel("LogIn Here");
	JButton btnLogin=new JButton("LogIn");
	JButton btnRegistration=new JButton("Registration");
	
	Registration registration=new Registration(this);

	JLabel lblOCRS=new JLabel("ONLINE CAR RENT SYSTEM");
	
	SessionBean sessionBean=new SessionBean();

	public Login(){
		init();
		cmp();
		btnAction();
	}
	public void btnAction(){
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkValidation()){
					if(checkUserName()){
						if(checkPassword()){
							sessionBean.setUserName(txtUserName.getText().trim().toString());
							loginAction();
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "incorrect User Name","Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnRegistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrationWork();
				registration.autoId();
			}
		});
		txtUserName.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				char ch=e.getKeyChar();
				if(ch!=KeyEvent.VK_BACK_SPACE && ch!=KeyEvent.VK_DELETE && ch!=KeyEvent.VK_ENTER){
					lblName.setVisible(false);
				}
				else if(txtUserName.getText().trim().toString().isEmpty()){
					lblName.setVisible(true);
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
		txtPassword.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				char ch=e.getKeyChar();
				if(ch!=KeyEvent.VK_BACK_SPACE && ch!=KeyEvent.VK_DELETE && ch!=KeyEvent.VK_ENTER){
					lblPassword.setVisible(false);
				}
				else if(txtPassword.getText().trim().toString().isEmpty()){
					lblPassword.setVisible(true);
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(checkConfirmation("Sure to exit whole system?")){
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
				else{
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
	}
	public boolean checkConfirmation(String caption){
		int a=JOptionPane.showConfirmDialog(null, caption,"Confirmation", JOptionPane.YES_NO_OPTION);
		if(a==JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
	public void registrationWork(){
		mainPanel.setVisible(false);
		add(registration);
		registration.setVisible(true);
		setSize(600, 600);
		setTitle("Registration_Form");
		setLocationRelativeTo(null);
		setResizable(false);
	}
	public boolean checkValidation(){
		if(!txtUserName.getText().trim().isEmpty()){
			if(!txtPassword.getText().trim().isEmpty()){
				return true;
			}
			else{
				JOptionPane.showMessageDialog(null, "Please insert Password","Error",JOptionPane.WARNING_MESSAGE);
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Please insert User Name","Error",JOptionPane.WARNING_MESSAGE);
		}
		return false;
	}
	public boolean checkUserName(){
		try {
			String sql="select userName from tbregistration";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				if(txtUserName.getText().trim().toString().equals(rs.getString("userName"))){
					return true;
				}
			}
			dbConneciton.con.close();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return false;
	}
	public boolean checkPassword(){
		try {
			String sql="select password from tbregistration where "
					+ "userName='"+txtUserName.getText().trim().toString()+"'";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				if(txtPassword.getText().trim().toString().equals(rs.getString("password"))){
					return true;
				}
				else{
					JOptionPane.showMessageDialog(null, "incorrect password",
							"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
			dbConneciton.con.close();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return false;
	}
	public void loginAction(){
		mainPanel.setVisible(false);
		WorkingPanel wp=new WorkingPanel(this);
		add(wp);
		wp.setVisible(true);
		setSize(screeSize);
		setTitle("Working_Panel");
		setLocationRelativeTo(null);
		setResizable(false);
	}
	public void cmp(){
		BorderLayout border=new BorderLayout();
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(border);
		mainPanel.add(lblBackgroundImage, BorderLayout.CENTER);
		lblBackgroundImage.setLayout(new BorderLayout());
		lblBackgroundImage.add(panelNorth, BorderLayout.NORTH);
		panelNorth();
		lblBackgroundImage.add(panelSouth, BorderLayout.SOUTH);
		panelSouth();
		lblBackgroundImage.add(panelCenter, BorderLayout.CENTER);
		panelCenter();
		lblBackgroundImage.add(panelEast, BorderLayout.EAST);
		panelEast();
		lblBackgroundImage.add(panelWest, BorderLayout.WEST);
		panelWest();
	}
	public void panelNorth(){
		panelNorth.setPreferredSize(new Dimension(0, 140));
		panelNorth.setBackground(Color.BLACK);
		panelNorth.setOpaque(false);

		FlowLayout flow=new FlowLayout();
		panelNorth.setLayout(flow);
		flow.setAlignment(FlowLayout.CENTER);
		flow.setVgap(30);
		
		panelNorth.add(lblOCRS);
		lblOCRS.setForeground(Color.white);
		lblOCRS.setFont(new Font("Carlibri", Font.BOLD, 28));
	}
	public void panelSouth(){
		panelSouth.setPreferredSize(new Dimension(0, 140));
		panelSouth.setBackground(Color.BLACK);
		panelSouth.setOpaque(false);
	}
	public void panelCenter(){
		panelCenter.setBackground(new Color(37,42,64,120));
		BorderLayout border=new BorderLayout();
		panelCenter.setLayout(border);
		panelCenter.add(panelCenterNorth,BorderLayout.NORTH);
		panelCenterNorth();
		panelCenter.add(panelCenterSouth,BorderLayout.SOUTH);
		panelCenterSouth();
		panelCenter.add(panelCenterCenter,BorderLayout.CENTER);
		panelCenterCenter();
	}
	public void panelCenterNorth(){
		panelCenterNorth.setBackground(new Color(12,44,59,100));
		panelCenterNorth.setPreferredSize(new Dimension(0, 200));
		FlowLayout flow=new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT);
		flow.setVgap(30);
		flow.setHgap(30);
		panelCenterNorth.setLayout(flow);
		panelCenterNorth.add(lblLogin);
		lblLogin.setFont(new Font("Century Gothic", Font.BOLD, 23));
		lblLogin.setForeground(Color.WHITE);
	}
	public void panelCenterSouth(){
		panelCenterSouth.setPreferredSize(new Dimension(0, 70));
		panelCenterSouth.setBackground(Color.WHITE);
		FlowLayout flow=new FlowLayout();
		panelCenterSouth.setLayout(flow);
		flow.setVgap(0);
		flow.setHgap(45);
		flow.setAlignment(FlowLayout.LEFT);

		panelCenterSouth.add(btnLogin);
		btnLogin.setPreferredSize(new Dimension(100, 34));
		btnLogin.setBackground(new Color(12, 44, 59));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBorderPainted(false);
		btnLogin.setFont(new Font("Century Gothic", Font.BOLD, 14));
		
		panelCenterSouth.add(btnRegistration);
		btnRegistration.setPreferredSize(new Dimension(130, 34));
		btnRegistration.setBackground(Color.WHITE);
		btnRegistration.setForeground(new Color(12, 44, 59));
		btnRegistration.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnRegistration.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(12, 44, 59)));
		
	}
	public void panelCenterCenter(){
		Font font=new Font("Century Gothic", Font.BOLD, 14);
		panelCenterCenter.setBackground(Color.WHITE);
		FlowLayout flow=new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT);
		flow.setHgap(1);
		GridBagConstraints c=new GridBagConstraints();
		GridBagLayout grid=new GridBagLayout();
		panelCenterCenter.setLayout(grid);
		c.fill=GridBagConstraints.BOTH;
		c.insets=new Insets(2, 2, 5, 60);

		c.gridx=0;
		c.gridy=0;
		panelCenterCenter.add(lblUserName,c);
		lblUserName.setFont(font);

		c.gridx=0;
		c.gridy=1;
		panelCenterCenter.add(txtUserName,c);
		txtUserName.setPreferredSize(new Dimension(230, 25));
		txtUserName.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#FFA727")));
		txtUserName.setOpaque(false);
		txtUserName.setLayout(flow);
		txtUserName.add(lblName);
		lblName.setForeground(Color.LIGHT_GRAY);

		c.gridx=0;
		c.gridy=2;
		panelCenterCenter.add(lblPass,c);
		lblPass.setFont(font);

		c.gridx=0;
		c.gridy=3;
		panelCenterCenter.add(txtPassword,c);
		txtPassword.setPreferredSize(new Dimension(230, 25));
		txtPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#FFA727")));
		txtPassword.setOpaque(false);
		txtPassword.setLayout(flow);
		txtPassword.add(lblPassword);
		lblPassword.setForeground(Color.LIGHT_GRAY);
	}
	public void panelEast(){
		panelEast.setPreferredSize(new Dimension(490, 0));
		panelEast.setBackground(Color.BLUE);
		panelEast.setOpaque(false);
	}
	public void panelWest(){
		panelWest.setPreferredSize(new Dimension(490, 0));
		panelWest.setBackground(Color.BLUE);
		panelWest.setOpaque(false);
	}
	public void init(){
		setSize(dmn);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Login_Form");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}















