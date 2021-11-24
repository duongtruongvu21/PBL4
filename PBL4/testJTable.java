package PBL4;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class testJTable {
	static JTable jpShowNotify;
	static JFrame jFrame;
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(56789);
		
		JButton jbSignUp, jbManageUser, jbManageActivate;
		JLabel jlTitle, jlNotify, jlInfo;
		JPanel jpButton;
		JScrollPane jScrollPane;

		jFrame = new JFrame("Server GUI");
		jFrame.setSize(1000, 900);
		jFrame.setLocationRelativeTo(null);
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jlTitle = new JLabel("Server - Đồng bộ dữ liệu");
		jlTitle.setFont(new Font("Arial", Font.BOLD, 40));
		jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
		jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		//jlInfo = new JLabel(InetAddress.getLocalHost().getHostAddress() + "   " + serverSocket.getLocalSocketAddress());
		jlInfo = new JLabel("IP: " + InetAddress.getLocalHost().getHostAddress() + "   Port: " + serverSocket.getLocalPort());
		jlInfo.setFont(new Font("Arial", Font.ITALIC, 20));
		jlInfo.setBorder(new EmptyBorder(5, 0, 10, 0));
		jlInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		jpButton = new JPanel();
		jpButton.setBorder(new EmptyBorder(10, 0, 10, 0));
		
		jbSignUp = new JButton("Thêm người dùng");
		jbSignUp.setPreferredSize(new Dimension(225, 40));
		jbSignUp.setFont(new Font("Arial", Font.BOLD, 20));
		
		jbManageUser = new JButton("Người dùng");
		jbManageUser.setPreferredSize(new Dimension(225, 40));
		jbManageUser.setFont(new Font("Arial", Font.BOLD, 20));
		
		jbManageActivate = new JButton("Hoạt động");
		jbManageActivate.setPreferredSize(new Dimension(225, 40));
		jbManageActivate.setFont(new Font("Arial", Font.BOLD, 20));

		jlNotify = new JLabel("Thông báo");
		jlNotify.setFont(new Font("Arial", Font.BOLD, 32));
		jlNotify.setBorder(new EmptyBorder(20, 0, 0, 0));
		jlNotify.setAlignmentX(Component.CENTER_ALIGNMENT);

		
		DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Thời Gian");
        model.addColumn("Tài Khoản");
        model.addColumn("Hành Động");
        model.addColumn("Đối Tượng");
		jpShowNotify = new JTable(model);
		jpShowNotify.setBorder(new EmptyBorder(10, 0, 10, 0));
		jpShowNotify.setLayout(new BoxLayout(jpShowNotify, BoxLayout.Y_AXIS));
		jpShowNotify.setRowHeight(32);
		jpShowNotify.setFont(new Font("Roboto", Font.ITALIC, 20));
		jpShowNotify.setEnabled(false);
		
		jpShowNotify.getColumnModel().getColumn(0).setPreferredWidth(132);
		jpShowNotify.getColumnModel().getColumn(1).setPreferredWidth(50);
		jpShowNotify.getColumnModel().getColumn(2).setPreferredWidth(50);
		jpShowNotify.getColumnModel().getColumn(3).setPreferredWidth(320);
		
		jScrollPane = new JScrollPane(jpShowNotify);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setPreferredSize(new Dimension(10, 625));
		
		jpButton.add(jbSignUp);
		jpButton.add(jbManageUser);
		jpButton.add(jbManageActivate);
		
		jFrame.add(jlTitle);
		jFrame.add(jlInfo);
		jFrame.add(jpButton);
		jFrame.add(jlNotify);
		jFrame.add(jScrollPane);
		jFrame.setVisible(true);

		jbSignUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LoadContentMainForm();
			}
		});
		
		jbManageUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel dtm = (DefaultTableModel) jpShowNotify.getModel();
				dtm.setRowCount(0);
			}
		});
		
		jbManageActivate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		LoadContentMainForm();
	}
	
	
	public static void LoadContentMainForm() {
		BO bo = new BO();
		ArrayList<HoatDong> listContent = bo.get100ActivityNewest();
		for(int i = 0; i < listContent.size(); i++) {
			DefaultTableModel model = (DefaultTableModel) jpShowNotify.getModel();
			String tg = listContent.get(i).getThoiGian();
			String tk = listContent.get(i).getTaiKhoan();
			String hv = listContent.get(i).getHanhVi();
			String dt = listContent.get(i).getDoiTuong();
	        model.addRow(new Object[]{tg, tk, hv, dt});
		}
		jFrame.validate();
	}
}
