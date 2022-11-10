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
import javax.swing.JCheckBox;
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
import com.example.Admin.SuggestText;
import com.example.Admin.dbConneciton;
import com.toedter.calendar.JDateChooser;

public class Booking_Car extends JPanel{

	JPanel panelCenter=new JPanel();
	JPanel panelWest=new JPanel();
	JPanel panelWestCenter=new JPanel();
	JPanel panelWestSouth=new JPanel();

	JLabel lblSearch=new JLabel("Search: ");
	SuggestText cmbSearch=new SuggestText();
	JLabel lblSearchIcon=new JLabel(new ImageIcon("icons/CenterImage.png"));

	JLabel lblCarId=new JLabel("Car Id: ");
	JLabel lblModelName=new JLabel("Model Name: ");
	JLabel lblPrice=new JLabel("Price: ");
	JLabel lblBookingDate=new JLabel("Booking Date: ");
	JLabel lblNoPleces=new JLabel("No Places: ");

	SuggestText cmbCarId=new SuggestText();
	JTextField txtModelName=new JTextField(20);
	JTextField txtPrice=new JTextField();
	JDateChooser dateBookingDate=new JDateChooser();
	JTextField txtNoOfPleces=new JTextField();

	ImageIcon iconAdd=new ImageIcon("icons/add1.png");
	JButton btnSubmit=new JButton("Submit",iconAdd);
	ImageIcon iconEdit=new ImageIcon("icons/btnEdit.png");
	JButton btnUpdate=new JButton("Update",iconEdit);
	ImageIcon iconRefresh=new ImageIcon("icons/btnRefresh.png");
	JButton btnRefresh=new JButton("Refresh",iconRefresh);
	ImageIcon iconDelete=new ImageIcon("icons/cancel-icon.png");
	JButton btnCancle=new JButton("Delete",iconDelete);

	String col[]={"CarId","Brand","Model Name","Price","Booking Date","No Pleces"};
	Object row[][]={};
	DefaultTableModel model=new DefaultTableModel(row, col);
	JTable table=new JTable(model);
	JScrollPane scroll=new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	boolean isUpdate=false;

	public Booking_Car(){
		cmp();
		btnAction();
	}
	public void refreshWork(){
		cmbCarId.txtSuggest.setText("");
		txtModelName.setText("");
		dateBookingDate.setDate(new Date());
		txtPrice.setText("");
		txtNoOfPleces.setText("");
		cmbSearch.txtSuggest.setText("");
	}
	public void btnAction(){
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isUpdate){
					if(checkConfirmation("Are you sure you want to update?")){
						if(deleteData()){
							if(insertData()){
								JOptionPane.showMessageDialog(null, "data updated successfully");
								tableDataLoad();
								cmbSearchDataLoad();
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
								tableDataLoad();
								cmbSearchDataLoad();
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
				cmbSearchDataLoad();
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
							cmbSearchDataLoad();
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
				String id=model.getValueAt(table.getSelectedRow(), 2).toString();
				searchDataLoad(id);
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		cmbSearch.cmbSuggest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!cmbSearch.txtSuggest.getText().trim().isEmpty()){
					StringTokenizer token=new StringTokenizer(cmbSearch.txtSuggest.getText().trim(), "#");
					String id=token.nextToken();
					String model=token.nextToken();
					searchDataLoad(model);
				}
				else{
					cmbCarId.txtSuggest.setText("");
					cmbCarIdDataLoad();
					cmbSearchDataLoad();
					tableDataLoad();
					refreshWork();
				}
			}
		});
	}
	public boolean checkValidation(){
		if(!cmbCarId.txtSuggest.getText().trim().isEmpty()){
			if(!txtModelName.getText().trim().isEmpty()){
				if(!txtPrice.getText().trim().isEmpty()){
					if(!txtNoOfPleces.getText().trim().isEmpty()){
						if(isExistModelName()){
							return true;
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "insert no of places please");
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "insert price please");
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
			String sql="select * from tbbookingcar where modelName like "
					+ "'"+txtModelName.getText().trim().toString()+"'";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				JOptionPane.showMessageDialog(null, "The car in this model Alreay bocked!");
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
	public void cmbCarIdDataLoad(){
		try {
			cmbCarId.v.clear();
			cmbCarId.v.add("");
			String sql="select carId,brand from tbcardetails";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				cmbCarId.v.add(rs.getString("carId")+"#"+rs.getString("brand"));
			}
			dbConneciton.con.close();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"cmbCarIdDataLoad()");
		}
	}
	public boolean insertData(){
		try {
			StringTokenizer token=new StringTokenizer(cmbCarId.txtSuggest.getText().trim().toString(), "#");
			String carid=token.nextToken();
			String brand=token.nextToken();
			String date=new SimpleDateFormat("yyyy-MM-dd").format(dateBookingDate.getDate());

			String sql="insert into tbbookingcar(carId,brand,modelName,price,bookingDate,noPlaces)"
					+ "values('"+carid+"',"
					+ "'"+brand+"',"
					+ "'"+txtModelName.getText().trim().toString()+"',"
					+ "'"+txtPrice.getText().trim().toString()+"',"
					+ "'"+date+"',"
					+ "'"+txtNoOfPleces.getText().trim().toString()+"')";
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
			String sql="select carId,brand,modelName,price,bookingDate,noPlaces from tbbookingcar";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				model.addRow(new Object[]{rs.getString("carId"),rs.getString("brand"),
						rs.getString("modelName"),rs.getString("price"),rs.getString("bookingDate"),
						rs.getString("noPlaces")});
			}
			dbConneciton.con.close();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"tableDataLoad()");
		}
	}
	public void cmbSearchDataLoad(){
		try {
			cmbSearch.v.clear();
			cmbSearch.v.add("");
			String sql="select carId,modelName from tbbookingcar";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				cmbSearch.v.add(rs.getString("carId")+"#"+rs.getString("modelName"));
			}
			dbConneciton.con.close();
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"cmbSearchDataLoad()");
		}
	}
	public void searchDataLoad(String carId){
		try {
			String sql="select carId,brand,modelName,price,bookingDate,noPlaces from tbbookingcar "
					+ "where modelName='"+carId+"'";
			dbConneciton.connection();
			ResultSet rs=dbConneciton.sta.executeQuery(sql);
			while(rs.next()){
				cmbCarId.txtSuggest.setText(rs.getString("carId")+"#"+rs.getString("brand"));
				txtModelName.setText(rs.getString("modelName"));
				txtPrice.setText(rs.getString("price"));
				dateBookingDate.setDate(rs.getDate("bookingDate"));
				txtNoOfPleces.setText(rs.getString("noPlaces"));
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e+"searchDataLoad()");
		}
	}
	public boolean deleteData(){
		try {
			String sql="delete from tbbookingcar where modelName like "
					+ "'"+txtModelName.getText().trim().toString()+"'";
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
		TitledBorder title=BorderFactory.createTitledBorder("Booking Car");
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

		c.gridx=0;
		c.gridy=0;
		c.insets=new Insets(5, 5, 50, 5);
		panelWestCenter.add(lblSearch,c);
		lblSearch.setForeground(Color.GREEN);
		lblSearch.setFont(new Font("", Font.BOLD, 14));

		c.gridx=1;
		c.gridy=0;
		c.insets=new Insets(5, 5, 50, 5);
		panelWestCenter.add(cmbSearch.cmbSuggest,c);

		c.gridx=2;
		c.gridy=0;
		c.insets=new Insets(5, 5, 50, 5);
		panelWestCenter.add(lblSearchIcon,c);

		c.gridx=0;
		c.gridy=1;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(lblCarId,c);
		lblCarId.setForeground(Color.BLACK);
		lblCarId.setFont(new Font("", Font.BOLD, 14));

		c.gridx=1;
		c.gridy=1;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(cmbCarId.cmbSuggest,c);

		c.gridx=0;
		c.gridy=2;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(lblModelName,c);
		lblModelName.setForeground(Color.BLACK);
		lblModelName.setFont(new Font("", Font.BOLD, 14));

		c.gridx=1;
		c.gridy=2;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(txtModelName,c);

		c.gridx=0;
		c.gridy=3;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(lblModelName,c);
		lblModelName.setForeground(Color.BLACK);
		lblModelName.setFont(new Font("", Font.BOLD, 14));

		c.gridx=1;
		c.gridy=3;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(txtModelName,c);

		c.gridx=0;
		c.gridy=4;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(lblPrice,c);
		lblPrice.setForeground(Color.BLACK);
		lblPrice.setFont(new Font("", Font.BOLD, 14));

		c.gridx=1;
		c.gridy=4;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(txtPrice,c);

		c.gridx=0;
		c.gridy=5;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(lblBookingDate,c);
		lblBookingDate.setForeground(Color.BLACK);
		lblBookingDate.setFont(new Font("", Font.BOLD, 14));

		c.gridx=1;
		c.gridy=5;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(dateBookingDate,c);
		dateBookingDate.setDate(new Date());
		dateBookingDate.setDateFormatString("dd-MM-yyyy");

		c.gridx=0;
		c.gridy=6;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(lblNoPleces,c);
		lblNoPleces.setForeground(Color.BLACK);
		lblNoPleces.setFont(new Font("", Font.BOLD, 14));

		c.gridx=1;
		c.gridy=6;
		c.insets=new Insets(5, 5, 5, 5);
		panelWestCenter.add(txtNoOfPleces,c);
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
