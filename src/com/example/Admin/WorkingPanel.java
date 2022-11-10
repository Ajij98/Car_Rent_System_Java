package com.example.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.View;

import com.example.menu.Booking_Car;
import com.example.menu.Car_Details;
import com.toedter.calendar.JDateChooser;

public class WorkingPanel extends JPanel{
	JPanel mainPanel=new JPanel();
	JLabel lblBackgroundImage=new JLabel(new ImageIcon(""));
	JLabel lblCenterImage=new JLabel(new ImageIcon("Images/back.jpg"));
	
	JPanel panelNorth=new JPanel();
	JPanel panelNorthCenter=new JPanel();
	JPanel panelNorthEast=new JPanel();
	JPanel panelCenter=new JPanel();
	JPanel panelWest=new JPanel();
	JPanel panelWestNorth=new JPanel();
	JPanel panelWestCenter=new JPanel();
	JPanel panelWestSouth=new JPanel();
	
	JButton btnCarDetails=new JButton("Car Details");
	JButton btnBookingCar=new JButton("Booking Car");
		
	JLabel lblOCRS=new JLabel("ONLINE CAR RENT SYSTEM");
	JLabel lblOCRSImage=new JLabel(new ImageIcon("Images/image.png"));
	
	JLabel lblUserIcon=new JLabel(new ImageIcon("icons/user1.png"));
	JLabel lblCurrentUser=new JLabel("current user :");
	JLabel lblUser=new JLabel("Admin");
	
	ImageIcon iconLogout=new ImageIcon("icons/logout.png");
	JButton btnLogout=new JButton("Logout",iconLogout);
	
	JLabel lblNewDate=new JLabel("Date:");
	JLabel lblDate=new JLabel();

	JLabel lblT=new JLabel("Time:");
	JLabel lblTime=new JLabel();
	
	Car_Details carDetails=new Car_Details();
	Booking_Car bookCar=new Booking_Car();
	
	JFrame frame;
	
	public WorkingPanel(JFrame frm){
		this.frame=frm;
		cmp();
		btnAction();
		showCurrentTime();
		showCurrentDate();
	}
	public void btnAction(){	
		btnCarDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allPanelTrueFalse();
				carDetails.setVisible(true);
				carDetails.autoId();
				carDetails.tableDataLoad();
			}
		});
		btnBookingCar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allPanelTrueFalse();
				bookCar.setVisible(true);
				bookCar.tableDataLoad();
				bookCar.cmbCarIdDataLoad();
				bookCar.cmbSearchDataLoad();
			}
		});
		
		btnCarDetails.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				btnCarDetails.setBackground(new Color(26, 50, 54));
			}
			public void mouseEntered(MouseEvent e) {
				btnCarDetails.setBackground(Color.decode("#D13F13"));
			}
		});
		btnBookingCar.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				btnBookingCar.setBackground(new Color(26, 50, 54));
			}
			public void mouseEntered(MouseEvent e) {
				btnBookingCar.setBackground(Color.decode("#D13F13"));
			}
		});
		
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkConfirmation("Sure to Logout?")){
					frame.setVisible(false);
					Login lg=new Login();
					lg.setVisible(true);
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
	public void cmp(){
		setBackground(Color.BLUE);
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		BorderLayout border=new BorderLayout();
		mainPanel.setLayout(border);
		border.setVgap(0);		
		mainPanel.add(lblBackgroundImage, BorderLayout.CENTER);
		lblBackgroundImage();
		addPanel();
	}
	public void lblBackgroundImage() {
		lblBackgroundImage.setLayout(new BorderLayout());
		lblBackgroundImage.add(panelNorth, BorderLayout.NORTH);
		panelNorth();
		lblBackgroundImage.add(panelCenter, BorderLayout.CENTER);
		panelCenter();
		lblBackgroundImage.add(panelWest, BorderLayout.WEST);
		panelWest();
	}
	public void panelNorth(){
		panelNorth.setPreferredSize(new Dimension(0, 40));
		FlowLayout flow=new FlowLayout();
		flow.setVgap(0);
		panelNorth.setLayout(flow);
		panelNorth.add(lblOCRSImage);
		
		lblOCRSImage.setLayout(new BorderLayout());
		lblOCRSImage.add(panelNorthCenter, BorderLayout.CENTER);
		panelNorthCenter();
		lblOCRSImage.add(panelNorthEast, BorderLayout.EAST);
		panelNorthEast();
	}
	public void panelNorthCenter(){
		panelNorthCenter.setOpaque(false);
		panelNorthCenter.setBackground(new Color(58, 168, 222));
		FlowLayout flow=new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT);
		flow.setHgap(10);
		flow.setVgap(8);
		panelNorthCenter.setLayout(flow);
		panelNorthCenter.add(lblOCRS);
		lblOCRS.setFont(new Font("Carlibri", Font.PLAIN, 18));
		lblOCRS.setForeground(Color.WHITE);
	}	
	public void showCurrentTime(){
		new Timer(0, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date date=new Date();
				SimpleDateFormat s=new SimpleDateFormat("hh:mm:ss a");
				lblTime.setText(s.format(date));
			}
		}).start();
	}
	public void showCurrentDate(){
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
		lblDate.setText(format.format(date));
	}
	public void panelNorthEast(){
		panelNorthEast.setOpaque(false);
		panelNorthEast.setBackground(new Color(58, 168, 222));
		panelNorthEast.setPreferredSize(new Dimension(410, 0));
		FlowLayout flow=new FlowLayout();
		panelNorthEast.setLayout(flow);
		flow.setHgap(10);
		flow.setVgap(3);
		
		panelNorthEast.add(lblNewDate);
		lblNewDate.setForeground(Color.WHITE);
		lblNewDate.setFont(new Font("carlibri", Font.BOLD, 14));
		
		panelNorthEast.add(lblDate);
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(new Font("carlibri", Font.BOLD, 14));
		
		panelNorthEast.add(lblT);
		lblT.setForeground(Color.WHITE);
		lblT.setFont(new Font("carlibri", Font.BOLD, 14));
		
		panelNorthEast.add(lblTime);
		lblTime.setForeground(Color.WHITE);
		lblTime.setFont(new Font("carlibri", Font.BOLD, 14));
		
		panelNorthEast.add(btnLogout);
		btnLogout.setPreferredSize(new Dimension(120, 32));
		btnLogout.setBackground(Color.decode("#B8CFE5"));
	}
	public void panelCenter(){
		FlowLayout flow=new FlowLayout();
		flow.setVgap(0);
		flow.setHgap(0);
		panelCenter.setLayout(flow);
		panelCenter.add(lblCenterImage);
	}
	public void panelWest(){
		panelWest.setBackground(Color.green);
		panelWest.setPreferredSize(new Dimension(230, 0));
		panelWest.setLayout(new BorderLayout());
		panelWest.add(panelWestNorth, BorderLayout.NORTH);
		panelWestNorth();
		panelWest.add(panelWestCenter, BorderLayout.CENTER);
		panelWestCenter();
		panelWest.add(panelWestSouth, BorderLayout.SOUTH);
		panelWestSouth();
	}
	public void panelWestNorth(){
		panelWestNorth.setPreferredSize(new Dimension(0, 60));
		panelWestNorth.setBackground(new Color(26, 50, 54));
		panelWestNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
		FlowLayout flow=new FlowLayout();
		panelWestNorth.setLayout(flow);
		flow.setAlignment(FlowLayout.LEFT);
		flow.setVgap(15);
		flow.setHgap(6);
		
		panelWestNorth.add(lblUserIcon);
		
		panelWestNorth.add(lblCurrentUser);
		lblCurrentUser.setForeground(Color.WHITE);
		lblCurrentUser.setFont(new Font("Carlibri", Font.PLAIN, 13));
		
		panelWestNorth.add(lblUser);
		lblUser.setForeground(Color.GREEN);
		lblUser.setFont(new Font("Century Gothic", Font.BOLD, 15));
	}
	public void panelWestCenter(){
		Font font=new Font("Carlibri", Font.PLAIN, 13);
		panelWestCenter.setBackground(new Color(26, 50, 54));
		GridLayout grid=new GridLayout(9, 1);
		panelWestCenter.setLayout(grid);
		grid.setVgap(10);	
		
		panelWestCenter.add(btnCarDetails);
		btnCarDetails.setBackground(new Color(26, 50, 54));
		btnCarDetails.setForeground(Color.WHITE);
		btnCarDetails.setFont(font);
		btnCarDetails.setBorderPainted(false);
		
		panelWestCenter.add(btnBookingCar);
		btnBookingCar.setBackground(new Color(26, 50, 54));
		btnBookingCar.setForeground(Color.WHITE);
		btnBookingCar.setFont(font);
		btnBookingCar.setBorderPainted(false);
	}
	public void panelWestSouth(){
		panelWestSouth.setPreferredSize(new Dimension(0, 160));
		panelWestSouth.setBackground(new Color(26, 50, 54));
	}
	public void addPanel(){
		FlowLayout flow=new FlowLayout();
		lblCenterImage.setLayout(flow);
		flow.setVgap(0);
		
		lblCenterImage.add(carDetails);
		lblCenterImage.add(bookCar);
		allPanelTrueFalse();
	}
	public void allPanelTrueFalse(){
		carDetails.setVisible(false);
		bookCar.setVisible(false);
	}
}

















