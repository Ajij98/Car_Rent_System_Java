package com.example.menu;

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
import java.awt.event.MouseListener;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;
import com.example.Admin.SessionBean;
import com.example.Admin.SuggestText;
import com.example.Admin.dbConneciton;
import com.toedter.calendar.JDateChooser;

public class Car_Details extends JPanel{

	JPanel panelCenter=new JPanel();
	JPanel panelWest=new JPanel();
	JPanel panelWestCenter=new JPanel();
	JPanel panelWestSouth=new JPanel();

	JLabel lblCarId=new JLabel("Car Id:");
	JLabel lblModelName=new JLabel("Model Name:");
	JLabel lblBrand=new JLabel("Brand:");
	JLabel lblColor=new JLabel("Color:");
	JLabel lblPrice=new JLabel("Price:");

	JTextField txtCarId=new JTextField(20);
	JTextField txtModelName=new JTextField();
	SuggestText cmbBrand=new SuggestText();
	JTextField txtColor=new JTextField();
	JTextField txtPrice=new JTextField();

	String col[]={"CarId","Model Name","Brand","Color","Price"};
	Object row[][]={};
	DefaultTableModel model=new DefaultTableModel(row, col);
	JTable table=new JTable(model);
	JScrollPane scroll=new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	ImageIcon iconAdd=new ImageIcon("icons/add1.png");
	JButton btnSubmit=new JButton("Submit",iconAdd);
	ImageIcon iconEdit=new ImageIcon("icons/btnEdit.png");
	JButton btnUpdate=new JButton("Update",iconEdit);
	ImageIcon iconRefresh=new ImageIcon("icons/btnRefresh.png");
	JButton btnRefresh=new JButton("Refresh",iconRefresh);
	ImageIcon iconDelete=new ImageIcon("icons/cancel-icon.png");
	JButton btnCancle=new JButton("Delete",iconDelete);

	boolean isUpdate=false;

	public Car_Details(){
		cmp();
		btnAction();
	}
	public void refreshWork(){
		txtModelName.setText("");
		cmbBrand.txtSuggest.setText("");
		txtColor.setText("");
		txtPrice.setText("");
	}
	public void btnAction(){
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isUpdate){
					if(checkConfirmation("Are you sure you want to update?")){
						if(deleteData()){
							if(insertData()){
								JOptionPane.showMessageDialog(null, "data updated successfully");
								autoId();
								tableDataLoad();
								refreshWork();
							}
						}
					}
				}
				else{
					if(checkValidation()){
						if(checkConfirmation("do you want to insert?")){
							if(insertData()){
								JOptionPane.showMessageDialog(null, "data inserted successfully");
								autoId();
								tableDataLoad();
								refreshWork();
							}
						}
					}
				}

			}
		});
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isUpdate=true;
			}
		});
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				autoId();
				tableDataLoad();
				refreshWork();
			}
		});
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkValidation()){
					if(checkConfirmation("do you want to delete?")){
						if(deleteData()){
							JOptionPane.showMessageDialog(null, "data deleted successfuly");
							autoId();
							tableDataLoad();
							refreshWork();
						}
					}
				}
			}
		});
		table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
				String id=model.getValueAt(table.getSelectedRow(), 0).toString();
				searchDataLoad(id);
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseClicked(MouseEvent arg0) {
			}
		});
	}
	public void autoId(){
		try {
			String sql="select ifnull(max(cast(SUBSTRING(carId,LOCATE('-',carId)+1,"
					+ "LENGTH(carId)-LOCATE('-',carId)) as UNSIGNED)),0)+1 as id from"
					+ " tbcardetails ";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			if(rs.next()){
				txtCarId.setText("CarId-"+rs.getString("id"));
			}
			dbConneciton.con.close();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"autoId()");
		}
	}
	public boolean checkValidation(){
		if(!txtCarId.getText().trim().isEmpty()){
			if(!txtModelName.getText().trim().isEmpty()){
				if(!cmbBrand.txtSuggest.getText().trim().isEmpty()){
					if(!txtColor.getText().trim().isEmpty()){
						if(!txtPrice.getText().trim().isEmpty()){
							if(isExistModelName()){
								return true;
							}
						}
						else{
							JOptionPane.showMessageDialog(null, "insert price please");
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "insert color please");
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "select brand please");
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "insert model name please");
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "insert car id please");
		}
		return false;
	}
	public boolean isExistModelName(){
		try {
			String sql="select * from tbcardetails where model like "
					+ "'"+txtModelName.getText().trim().toString()+"'";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				JOptionPane.showMessageDialog(null, "The car in this model Alreay Exist!");
				return false;
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"isExistModelName()");
		}
		return true;
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
			String sql="insert into tbcardetails(carId,model,brand,color,price)"
					+ "values('"+txtCarId.getText().trim().toString()+"',"
					+ "'"+txtModelName.getText().trim().toString()+"',"
					+ "'"+cmbBrand.txtSuggest.getText().trim().toString()+"',"
					+ "'"+txtColor.getText().trim().toString()+"',"
					+ "'"+txtPrice.getText().trim().toString()+"')";
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
	public void tableDataLoad(){
		try {
			for(int i=model.getRowCount()-1; i>=0; i--){
				model.removeRow(i);
			}
			String sql="select carId,model,brand,color,price from tbcardetails";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				model.addRow(new Object[]{rs.getString("carId"),rs.getString("model"),
						rs.getString("brand"),rs.getString("color"),rs.getString("price")});
			}
			dbConneciton.con.close();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"tableDataLoad()");
		}
	}
	public void searchDataLoad(String carId){
		try {
			String sql="select carId,model,brand,color,price from tbcardetails "
					+ "where carId='"+carId+"'";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				txtCarId.setText(rs.getString("carId"));
				txtModelName.setText(rs.getString("model"));
				cmbBrand.txtSuggest.setText(rs.getString("brand"));
				txtColor.setText(rs.getString("color"));
				txtPrice.setText(rs.getString("price"));
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"searchDataLoad()");
		}
	}
	public boolean deleteData(){
		try {
			String sql="delete from tbcardetails where carId like "
					+ "'"+txtCarId.getText().trim().toString()+"'";
			dbConneciton.connection();
			dbConneciton.sta.executeUpdate(sql);
			dbConneciton.con.close();
			return true;
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"deleteData()");
		}
		return false;
	}
	public void cmp(){
		TitledBorder title=BorderFactory.createTitledBorder("Car Details");
		title.setTitleColor(Color.BLACK);
		title.setTitleFont(new Font("Carlibri", Font.BOLD, 20));
		title.setTitleJustification(TitledBorder.CENTER);
		setBorder(title);

		setOpaque(false);
		setPreferredSize(new Dimension(1130	,725));
		setLayout(new BorderLayout());
		add(panelCenter, BorderLayout.CENTER);
		panelCenter();
		add(panelWest, BorderLayout.WEST);
		panelWest();
	}
	public void panelCenter(){
		panelCenter.setOpaque(false);
		GridBagConstraints cn=new GridBagConstraints();
		GridBagLayout grid=new GridBagLayout();
		panelCenter.setLayout(grid);

		cn.gridx=0;
		cn.gridy=0;
		cn.fill=GridBagConstraints.BOTH;
		cn.insets=new Insets(5, 5, 5, 5);
		panelCenter.add(scroll,cn);
		scroll.setPreferredSize(new Dimension(530,630));
		table.getTableHeader().setReorderingAllowed(false);
	}
	public void panelWest(){
		panelWest.setOpaque(false);
		panelWest.setPreferredSize(new Dimension(565,0));
		panelWest.setLayout(new BorderLayout());
		panelWest.add(panelWestCenter, BorderLayout.CENTER);
		panelWestCenter();
		panelWest.add(panelWestSouth, BorderLayout.SOUTH);
		panelWestSouth();
	}
	public void panelWestCenter(){
		panelWestCenter.setOpaque(false);
		GridBagConstraints c=new GridBagConstraints();
		GridBagLayout grid=new GridBagLayout();
		panelWestCenter.setLayout(grid);
		c.fill=GridBagConstraints.BOTH;
		c.insets=new Insets(5, 5, 10, 5);

		c.gridx=0;
		c.gridy=0;
		panelWestCenter.add(lblCarId,c);
		lblCarId.setFont(new Font("", Font.BOLD, 14));
		lblCarId.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=0;
		panelWestCenter.add(txtCarId,c);

		c.gridx=0;
		c.gridy=1;
		panelWestCenter.add(lblModelName,c);
		lblModelName.setFont(new Font("", Font.BOLD, 14));
		lblModelName.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=1;
		panelWestCenter.add(txtModelName,c);

		c.gridx=0;
		c.gridy=2;
		panelWestCenter.add(lblBrand,c);
		lblBrand.setFont(new Font("", Font.BOLD, 14));
		lblBrand.setForeground(Color.BLACK);


		c.gridx=1;
		c.gridy=2;
		panelWestCenter.add(cmbBrand.cmbSuggest,c);
		cmbBrand.v.add("");
		cmbBrand.v.add("BMW");
		cmbBrand.v.add("Ferari");
		cmbBrand.v.add("Mercedes Benz");
		cmbBrand.v.add("Lamborghini");
		cmbBrand.v.add("Toyota");
		cmbBrand.v.add("Nissan X Trail");
		cmbBrand.v.add("Volvo");

		c.gridx=0;
		c.gridy=3;
		panelWestCenter.add(lblColor,c);
		lblColor.setFont(new Font("", Font.BOLD, 14));
		lblColor.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=3;
		panelWestCenter.add(txtColor,c);

		c.gridx=0;
		c.gridy=4;
		panelWestCenter.add(lblPrice,c);
		lblPrice.setFont(new Font("", Font.BOLD, 14));
		lblPrice.setForeground(Color.BLACK);

		c.gridx=1;
		c.gridy=4;
		panelWestCenter.add(txtPrice,c);
	}
	public void panelWestSouth(){
		panelWestSouth.setOpaque(false);
		panelWestSouth.setPreferredSize(new Dimension(0, 150));
		FlowLayout flow=new FlowLayout();
		panelWestSouth.setLayout(flow);
		flow.setHgap(10);
		flow.setVgap(0);

		panelWestSouth.add(btnSubmit);
		btnSubmit.setPreferredSize(new Dimension(100, 35));
		btnSubmit.setBackground(Color.decode("#46A049"));
		btnSubmit.setForeground(Color.BLACK);

		panelWestSouth.add(btnUpdate);
		btnUpdate.setPreferredSize(new Dimension(100, 35));
		btnUpdate.setBackground(Color.decode("#0B7DDA"));
		btnUpdate.setForeground(Color.BLACK);

		panelWestSouth.add(btnRefresh);
		btnRefresh.setPreferredSize(new Dimension(110, 35));
		btnRefresh.setBackground(Color.decode("#E68A00"));
		btnRefresh.setForeground(Color.BLACK);

		panelWestSouth.add(btnCancle);
		btnCancle.setPreferredSize(new Dimension(110, 35));
		btnCancle.setBackground(Color.decode("#DA190B"));
		btnCancle.setForeground(Color.BLACK);
	}
}
















