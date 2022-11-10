package com.example.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;

import com.example.Admin.SuggestText;
import com.toedter.calendar.JDateChooser;

public class Registration extends JPanel{

	JPanel panelCenter=new JPanel();
	JPanel panelSouth=new JPanel();

	JLabel lblImage=new JLabel(new ImageIcon("Images/back - Copy.jpg"));

	JLabel lblUserId=new JLabel("User Id:");
	JLabel lblUserName=new JLabel("User Name:");
	JLabel lblAddress=new JLabel("Address:");
	JLabel lblMobileNumber=new JLabel("Contact No:");
	JLabel lblEmail=new JLabel("Email:");
	JLabel lblPassword=new JLabel("Password: ");
	JLabel lblConfirmPassword=new JLabel("Confirm Password: ");

	JTextField txtUserId=new JTextField(20);
	JTextField txtUserName=new JTextField();
	JTextArea txtAddress=new JTextArea(5, 20);
	JTextField txtMobileNumber=new JTextField();
	JTextField txtEmail=new JTextField();
	JPasswordField txtPassword=new JPasswordField();
	JPasswordField txtConfirmPassword=new JPasswordField();
	JScrollPane scrollAddress=new JScrollPane(txtAddress, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	ImageIcon iconAdd=new ImageIcon("icons/add1.png");
	JButton btnSubmit=new JButton("Submit",iconAdd);
	ImageIcon iconDelete=new ImageIcon("icons/cancel-icon.png");
	JButton btnCancle=new JButton("Cancle",iconDelete);

	JFrame frame;

	public Registration(JFrame frm){
		this.frame=frm;
		cmp();
		btnAction();
	}
	public void txtClear(){
		txtUserName.setText("");
		txtAddress.setText("");
		txtMobileNumber.setText("");
		txtEmail.setText("");
		txtPassword.setText("");
		txtConfirmPassword.setText("");
	}
	public void refreshWork(){
		autoId();
		txtClear();
	}
	public void btnAction(){
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkValidation()){
					if(checkConfirmPassword()){
						if(checkConfirmation("sure to insert?")){
							if(insertData()){
								JOptionPane.showMessageDialog(null, "All data inserted successfully.");
								refreshWork();
								Login lg=new Login();
								lg.setVisible(true);
								frame.setVisible(false);
							}
						}
					}
				}
			}
		});
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login lg=new Login();
				lg.setVisible(true);
				frame.setVisible(false);
			}
		});
	}
	public void autoId(){
		try {
			String sql="select ifnull(max(cast(substring(userId,locate('-',userId)+1,"
					+ "length(userId)-locate('-',userId)) as UNSIGNED)),0)+1 as id "
					+ "from tbregistration";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			if(rs.next()){
				txtUserId.setText("UserId-"+rs.getString("id"));
			}
			dbConneciton.con.close();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"autoId()");
		}
	}
	public boolean checkValidation(){
		if(!txtUserId.getText().trim().toString().isEmpty()){
			if(!txtUserName.getText().trim().toString().isEmpty()){
				if(!txtAddress.getText().trim().toString().isEmpty()){
					if(!txtMobileNumber.getText().trim().toString().isEmpty()){
						if(!txtEmail.getText().trim().toString().isEmpty()){
							if(!txtPassword.getText().trim().toString().isEmpty()){
								if(!txtConfirmPassword.getText().trim().toString().isEmpty()){
									return true;
								}
								else{
									JOptionPane.showMessageDialog(null, "insert confirm password please.");
								}
							}
							else{
								JOptionPane.showMessageDialog(null, "insert password please.");
							}
						}
						else{
							JOptionPane.showMessageDialog(null, "insert email please.");
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "insert contact number please.");
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "insert address please.");
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "insert user name please.");
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "insert user id please.");
		}
		return false;
	}
	public boolean isExistUserName(){
		try {
			String sql="select * from tbregistration where "
					+ "userId='"+txtUserId.getText().trim().toString()+"'";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				JOptionPane.showMessageDialog(null, "This user name already exist!","Warning...",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
			dbConneciton.con.close();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"isExistUserName()");
		}
		return true;
	}
	public boolean checkConfirmPassword(){
		if(txtPassword.getText().trim().toString().equals(txtConfirmPassword.getText().trim().toString())){
			return true;
		}
		else{
			JOptionPane.showMessageDialog(null, "Confirm password not matched with Password.","Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		return false;
	}
	public boolean checkConfirmation(String caption){
		int a=JOptionPane.showConfirmDialog(null, caption,"confirmation",JOptionPane.YES_NO_OPTION);
		if(a==JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
	public boolean insertData(){
		try {
			String sql="insert into tbregistration(userId,userName,address,contactNo,email,password,"
					+ "confirmPassword)"
					+ "values('"+txtUserId.getText().trim().toString()+"',"
					+ "'"+txtUserName.getText().trim().toString()+"',"
					+ "'"+txtAddress.getText().trim().toString()+"',"
					+ "'"+txtMobileNumber.getText().trim().toString()+"',"
					+ "'"+txtEmail.getText().trim().toString()+"',"
					+ "'"+txtPassword.getText().trim().toString()+"',"
					+ "'"+txtConfirmPassword.getText().trim().toString()+"')";
			dbConneciton.connection();
			dbConneciton.sta.executeUpdate(sql);
			dbConneciton.con.close();
			return true;
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"insertData()");
		}
		return false;
	}
	public void cmp(){
		TitledBorder title=BorderFactory.createTitledBorder("User Registration Form");
		title.setTitleColor(new Color(12, 44, 59));
		title.setTitleFont(new Font("Carlibri", Font.BOLD, 16));
		title.setTitleJustification(TitledBorder.CENTER);
		setBorder(title);

		setOpaque(false);
		setPreferredSize(new Dimension(1130	,725));
		FlowLayout flow=new FlowLayout();
		setLayout(new BorderLayout());
		flow.setVgap(0);
		add(lblImage, BorderLayout.CENTER);

		lblImage.setLayout(new BorderLayout());
		lblImage.add(panelCenter, BorderLayout.CENTER);
		panelCenter();
		lblImage.add(panelSouth, BorderLayout.SOUTH);
		panelSouth();
	}
	private void panelCenter() {
		panelCenter.setOpaque(false);
		GridBagConstraints c=new GridBagConstraints();
		GridBagLayout grid=new GridBagLayout();
		panelCenter.setLayout(grid);
		c.fill=GridBagConstraints.BOTH;
		c.insets=new Insets(5, 5, 5, 5);

		c.gridx=0;
		c.gridy=0;
		panelCenter.add(lblUserId,c);
		lblUserId.setFont(new Font("", Font.BOLD, 14));
		lblUserId.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=0;
		panelCenter.add(txtUserId,c);
		txtUserId.setEditable(false);

		c.gridx=0;
		c.gridy=1;
		panelCenter.add(lblUserName,c);
		lblUserName.setFont(new Font("", Font.BOLD, 14));
		lblUserName.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=1;
		panelCenter.add(txtUserName,c);

		c.gridx=0;
		c.gridy=2;
		panelCenter.add(lblAddress,c);
		lblAddress.setFont(new Font("", Font.BOLD, 14));
		lblAddress.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=2;
		panelCenter.add(scrollAddress,c);

		c.gridx=0;
		c.gridy=3;
		panelCenter.add(lblMobileNumber,c);
		lblMobileNumber.setFont(new Font("", Font.BOLD, 14));
		lblMobileNumber.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=3;
		panelCenter.add(txtMobileNumber,c);

		c.gridx=0;
		c.gridy=4;
		panelCenter.add(lblEmail,c);
		lblEmail.setFont(new Font("", Font.BOLD, 14));
		lblEmail.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=4;
		panelCenter.add(txtEmail,c);

		c.gridx=0;
		c.gridy=5;
		panelCenter.add(lblPassword,c);
		lblPassword.setFont(new Font("", Font.BOLD, 14));
		lblPassword.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=5;
		panelCenter.add(txtPassword,c);

		c.gridx=0;
		c.gridy=6;
		panelCenter.add(lblConfirmPassword,c);
		lblConfirmPassword.setFont(new Font("", Font.BOLD, 14));
		lblConfirmPassword.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=6;
		panelCenter.add(txtConfirmPassword,c);
	}
	private void panelSouth() {
		panelSouth.setOpaque(false);
		panelSouth.setPreferredSize(new Dimension(0, 150));
		FlowLayout flow=new FlowLayout();
		panelSouth.setLayout(flow);
		flow.setHgap(15);
		flow.setVgap(10);

		panelSouth.add(btnSubmit);
		btnSubmit.setPreferredSize(new Dimension(100, 35));
		btnSubmit.setBackground(Color.decode("#46A049"));
		btnSubmit.setForeground(Color.BLACK);

		panelSouth.add(btnCancle);
		btnCancle.setPreferredSize(new Dimension(110, 35));
		btnCancle.setBackground(Color.decode("#E68A00"));
		btnCancle.setForeground(Color.BLACK);
	}
}
