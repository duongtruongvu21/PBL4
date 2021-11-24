/*
 * Xong mở chia sẻ
 * chưa mở được thư mục chia sẻ của người khác -- đã rồi
 * chưa đồng bộ
 * 
 */


//SELECT * FROM `hoatdong` ORDER BY MSHD DESC LIMIT 30

package PBL4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class Server {
	static JTable jTShowNotify;
	static JPanel jpListUser;
	static JFrame jFrame;
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(56789);
		
		JButton jbSignUp, jbManageUser, jbManageActivate;
		JLabel jlTitle, jlNotify, jlInfo;
		JPanel jpButton;
		JScrollPane jScrollPane;

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
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
        jTShowNotify = new JTable(model);
        jTShowNotify.setBorder(new EmptyBorder(10, 10, 10, 10));
        jTShowNotify.setLayout(new BoxLayout(jTShowNotify, BoxLayout.Y_AXIS));
        jTShowNotify.setRowHeight(32);
        jTShowNotify.setFont(new Font("Roboto", Font.ITALIC, 20));
        jTShowNotify.setEnabled(false);
		
        jTShowNotify.getColumnModel().getColumn(0).setPreferredWidth(132);
        jTShowNotify.getColumnModel().getColumn(1).setPreferredWidth(50);
        jTShowNotify.getColumnModel().getColumn(2).setPreferredWidth(50);
        jTShowNotify.getColumnModel().getColumn(3).setPreferredWidth(320);
		
		jScrollPane = new JScrollPane(jTShowNotify);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setPreferredSize(new Dimension(10, 625));
		//jScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		jbSignUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog shareFrame = createFrameSignUp();
				shareFrame.setVisible(true);
			}
		});
		
		jbManageUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				JDialog shareFrame = createFrameShare();
//				shareFrame.setVisible(true);
			}
		});
		
		jbManageActivate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JDialog AtvtFrame = createFrameActivity();
				AtvtFrame.setVisible(true);
			}
		});
		
		jpButton.add(jbSignUp);
		jpButton.add(jbManageUser);
		jpButton.add(jbManageActivate);
		
		jPanel.add(jlTitle);
		jPanel.add(jlInfo);
		jPanel.add(jpButton);
		jPanel.add(jlNotify);
		jPanel.add(jScrollPane);
		jFrame.add(jPanel);
		jFrame.setVisible(true);


		LoadContentMainForm();
		int flagg = 0;
		
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				if((flagg++) % 2 == 0) LoadContentMainForm();
				Thread mainThread = new Thread(new XuLyClientServer(socket));
				mainThread.start();
			} catch (IOException e) {
				System.out.println("ERROR0001: " + e);
			}
		}
	}
	
	
	public static JDialog createFrameSignUp() { // sử dụng jdialog để có phương thức setModal, đè lên ưu tiên
		JDialog jFrame = new JDialog();
		jFrame.setTitle("Đăng Ký");
        jFrame.setSize(525, 345);
		jFrame.setLocationRelativeTo(null);
		jFrame.setModal(true);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        JLabel jlTitle = new JLabel("Thêm Tài Khoản Phòng Ban");
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));

        JPanel jpForm = new JPanel();
        jpForm.setBorder(new EmptyBorder(10, 0, 10, 0));
        jpForm.setLayout(new BoxLayout(jpForm, BoxLayout.Y_AXIS));
        
        JPanel jpUser = new JPanel();
        jpUser.setBorder(new EmptyBorder(5, 0, 5, 0));
        jpUser.setLayout(new BoxLayout(jpUser, BoxLayout.X_AXIS));
        
        JLabel jlUser = new JLabel("Nhập tài khoản: ");
        jlUser.setFont(new Font("Arial", Font.BOLD, 20));
        jlUser.setPreferredSize(new Dimension(240, 40));
        
        JTextField jtUser = new JTextField();
        jtUser.setFont(new Font("Arial", Font.PLAIN, 20));
        jtUser.setPreferredSize(new Dimension(240, 40));
        
        jpUser.add(jlUser);
        jpUser.add(jtUser);
        /////////////
        JPanel jpPass1 = new JPanel();
        jpPass1.setBorder(new EmptyBorder(5, 0, 5, 0));
        jpPass1.setLayout(new BoxLayout(jpPass1, BoxLayout.X_AXIS));
        
        JLabel jlPass1 = new JLabel("Nhập mật khẩu: ");
        jlPass1.setFont(new Font("Arial", Font.BOLD, 20));
        jlPass1.setPreferredSize(new Dimension(240, 40));
        
        JTextField jtPass1 = new JTextField();
        jtPass1.setFont(new Font("Arial", Font.PLAIN, 20));
        jtPass1.setPreferredSize(new Dimension(240, 40));
        
        jpPass1.add(jlPass1);
        jpPass1.add(jtPass1);
        
        /////////////
	    JPanel jpPass2 = new JPanel();
	    jpPass2.setBorder(new EmptyBorder(5, 0, 5, 0));
	    jpPass2.setLayout(new BoxLayout(jpPass2, BoxLayout.X_AXIS));
	    
	    JLabel jlPass2 = new JLabel("Xác nhận mật khẩu: ");
	    jlPass2.setFont(new Font("Arial", Font.BOLD, 20));
	    jlPass2.setPreferredSize(new Dimension(240, 40));
	    
	    JTextField jtPass2 = new JTextField();
	    jtPass2.setFont(new Font("Arial", Font.PLAIN, 20));
	    jtPass2.setPreferredSize(new Dimension(240, 40));
	    
	    jpPass2.add(jlPass2);
	    jpPass2.add(jtPass2);
	    
        /////////////
	    JPanel jpIP = new JPanel();
	    jpIP.setBorder(new EmptyBorder(5, 0, 5, 0));
	    jpIP.setLayout(new BoxLayout(jpIP, BoxLayout.X_AXIS));
	    
	    JLabel jlIP = new JLabel("Nhập IP máy truy cập: ");
	    jlIP.setFont(new Font("Arial", Font.BOLD, 20));
	    jlIP.setPreferredSize(new Dimension(240, 40));
	    
	    JTextField jtIP = new JTextField();
	    jtIP.setFont(new Font("Arial", Font.PLAIN, 20));
	    jtIP.setPreferredSize(new Dimension(240, 40));
	    
	    jpIP.add(jlIP);
	    jpIP.add(jtIP);
        
        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(10, 0, 10, 0));
        jpButton.setLayout(new BoxLayout(jpButton, BoxLayout.X_AXIS));
        
        JButton jbOK = new JButton("   Thêm Mới   ");
        jbOK.setPreferredSize(new Dimension(200, 40));
        jbOK.setFont(new Font("Arial", Font.BOLD, 20));
        
        jpButton.add(jbOK);
        
        jpForm.add(jpUser);
        jpForm.add(jpPass1);
        jpForm.add(jpPass2);
        jpForm.add(jpIP);
        
        jPanel.add(jlTitle);
        jPanel.add(jpForm);
        jPanel.add(jpButton);
        jFrame.add(jPanel);
        return jFrame;
    }
	
	public static JDialog createFrameActivity() { // sử dụng jdialog để có phương thức setModal, đè lên ưu tiên
		JDialog jFrame = new JDialog();
		jFrame.setTitle("Kiểm Tra Hoạt Động");
        jFrame.setSize(800, 700);
		jFrame.setLocationRelativeTo(null);
		jFrame.setModal(true);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
        JPanel jpSetDate = new JPanel();
        jpSetDate.setLayout(new BoxLayout(jpSetDate, BoxLayout.X_AXIS));
        jpSetDate.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JDateChooser jdateChooser1 = new JDateChooser();
        jdateChooser1.setPreferredSize(new Dimension(100, 40));
        jdateChooser1.setFont(new Font("Arial", Font.BOLD, 20));
        jdateChooser1.setBorder(new EmptyBorder(0, 5, 0, 5));
        JDateChooser jdateChooser2 = new JDateChooser();
        jdateChooser2.setPreferredSize(new Dimension(100, 40));
        jdateChooser2.setFont(new Font("Arial", Font.BOLD, 20));
        jdateChooser2.setBorder(new EmptyBorder(0, 5, 0, 10));
        
        JButton jbSearch = new JButton("  Lọc  ");
        jbSearch.setPreferredSize(new Dimension(200, 40));
		jbSearch.setFont(new Font("Arial", Font.BOLD, 20));
		jbSearch.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        try { // cài mặc định ngày trước và tắt không cho sửa text trong jdate
        	jdateChooser1.setDate(new Date("01 Jan 2001"));
        	jdateChooser2.setDate(java.util.Calendar.getInstance().getTime());
            JTextFieldDateEditor editor = (JTextFieldDateEditor) jdateChooser1.getDateEditor();
            editor.setEditable(false);
            JTextFieldDateEditor editor2 = (JTextFieldDateEditor) jdateChooser2.getDateEditor();
            editor2.setEditable(false);
		} catch (Exception e) {
			System.out.println("ERRRRR");
		}
        
        JPanel jpsetInfo = new JPanel();
        jpsetInfo.setLayout(new BoxLayout(jpsetInfo, BoxLayout.X_AXIS));
        jpsetInfo.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JTextField jtUserName = new JTextField("PhongBan00");
        jtUserName.setPreferredSize(new Dimension(200, 40));
        jtUserName.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JComboBox jcDoSth = new JComboBox();
        jcDoSth.setPreferredSize(new Dimension(200, 40));
        jcDoSth.setFont(new Font("Arial", Font.BOLD, 20));
        jcDoSth.addItem("Tất Cả");
        jcDoSth.addItem("Tải Về");
        
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Thời Gian");
        model.addColumn("Tài Khoản");
        model.addColumn("Hành Động");
        model.addColumn("Đối Tượng");
        
        JTable jTContent = new JTable(model);
        jTContent.setBorder(new EmptyBorder(10, 0, 10, 0));
        jTContent.setLayout(new BoxLayout(jTContent, BoxLayout.Y_AXIS));
        jTContent.setRowHeight(32);
        jTContent.setFont(new Font("Roboto", Font.ITALIC, 20));
        jTContent.setEnabled(false);
		
        jTContent.getColumnModel().getColumn(0).setPreferredWidth(132);
        jTContent.getColumnModel().getColumn(1).setPreferredWidth(50);
        jTContent.getColumnModel().getColumn(2).setPreferredWidth(50);
        jTContent.getColumnModel().getColumn(3).setPreferredWidth(320);
        JScrollPane jsContent = new JScrollPane(jTContent);
        jsContent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsContent.setPreferredSize(new Dimension(10, 640));
        
        
        
        jpSetDate.add(jdateChooser1);
        jpSetDate.add(jdateChooser2);
        jpSetDate.add(jbSearch);
        
        jpsetInfo.add(jtUserName);
        jpsetInfo.add(jcDoSth);

        jPanel.add(jpsetInfo);
        jPanel.add(jpSetDate);
        jPanel.add(jsContent);
        jFrame.add(jPanel);
        
        //DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //DateFormat dateFormatMDY = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        
        jbSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				DefaultTableModel dtm = (DefaultTableModel) jTContent.getModel();
		        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
		        String day1 = dateFormatYMD.format(jdateChooser1.getDate());
		        String day2 = dateFormatYMD.format(jdateChooser2.getDate());
				dtm.setRowCount(0);
				BO bo = new BO();
				ArrayList<HoatDong> listContent = bo.getSelectedActivity(jtUserName.getText(), "", day1, day2);
				for(int i = 0; i < listContent.size(); i++) {
					//DefaultTableModel model = (DefaultTableModel) jTShowNotify.getModel();
					String tg = listContent.get(i).getThoiGian();
					String tk = listContent.get(i).getTaiKhoan();
					String hv = listContent.get(i).getHanhVi();
					String dt = listContent.get(i).getDoiTuong();
					dtm.addRow(new Object[]{tg, tk, hv, dt});
				}
				jFrame.validate();
			}
		});
        
        return jFrame;
    }
	
//	public static JDialog createFrameShare() {
//		JDialog jFrame = new JDialog();
//        jFrame.setSize(525, 525);
//		jFrame.setLocationRelativeTo(null);
//		jFrame.setModal(true);
//        JPanel jPanel = new JPanel();
//        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
//        JLabel jlTitle = new JLabel("Quản Lý Người Dùng:");
//        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
//        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
//        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
//
//        jpListUser = new JPanel();
//        jpListUser.setBorder(new EmptyBorder(10, 0, 10, 0));
//        jpListUser.setLayout(new BoxLayout(jpListUser, BoxLayout.Y_AXIS));
//		JScrollPane js = new JScrollPane(jpListUser);
//		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		js.setPreferredSize(new Dimension(10, 400));
//        
//        jPanel.add(jlTitle);
//        jPanel.add(js);
//        jFrame.add(jPanel);
//        
//        
//        jpListUser.removeAll();
//        jpListUser.revalidate();
//        jpListUser.repaint();
//		LoadListUS();
//        return jFrame;
//    }
//	
//	static void LoadListUS() {
//		try {
//			ArrayList<String> ListUSShare = (ArrayList<String>) objectReceive;
//			
//			for (int i = 0; i < ListUSShare.size(); i++) {
//				JPanel jpFileRow = new JPanel();
//				jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));
//
//				JLabel jlFileName = new JLabel("        " + ListUSShare.get(i) + "                  ");
//				jlFileName.setFont(new Font("Roboto", Font.ITALIC, 21));
//				jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));
//				jpFileRow.setAlignmentX(Component.LEFT_ALIGNMENT);
//				jpFileRow.setName("" + ListUSShare.get(i));
//
//				JButton jbOpen = new JButton("Mở CS");
//				jbOpen.setPreferredSize(new Dimension(100, 24));
//				jbOpen.setFont(new Font("Arial", Font.BOLD, 16));
//				jbOpen.setName("" + ListUSShare.get(i));
//
//				JButton jbClose = new JButton("Đóng CS");
//				jbClose.setPreferredSize(new Dimension(100, 24));
//				jbClose.setFont(new Font("Arial", Font.BOLD, 16));
//				jbClose.setName("" + ListUSShare.get(i));
//				jpFileRow.addMouseListener(getMouseListener("Click"));
//				jbOpen.addMouseListener(getMouseListener("OpenShare"));
//				jbClose.addMouseListener(getMouseListener("CloseShare"));
//				// Add everything.
//				
//				jpFileRow.add(jlFileName);
//				jpFileRow.add(jbOpen);
//				jpFileRow.add(jbClose);
//				
//				if(ListCheck.get(i)) {
//					jbOpen.setEnabled(false);
//					jbClose.setEnabled(true);
//				} else {
//					jbOpen.setEnabled(true);
//					jbClose.setEnabled(false);
//				}
//				
//				if(!jtTK.getText().equals(ListUSShare.get(i))) {
//					jpListUser.add(jpFileRow);
//				}
//			}
//			dos.close();
//			socket.close();
//		} catch (Exception er) {
//			System.out.println("ERROR1221: " + er);
//		}
//	}
	
	public static void LoadContentMainForm() {
		DefaultTableModel dtm = (DefaultTableModel) jTShowNotify.getModel();
		dtm.setRowCount(0);
		BO bo = new BO();
		ArrayList<HoatDong> listContent = bo.get100ActivityNewest();
		for(int i = 0; i < listContent.size(); i++) {
			DefaultTableModel model = (DefaultTableModel) jTShowNotify.getModel();
			String tg = listContent.get(i).getThoiGian();
			String tk = listContent.get(i).getTaiKhoan();
			String hv = listContent.get(i).getHanhVi();
			String dt = listContent.get(i).getDoiTuong();
	        model.addRow(new Object[]{tg, tk, hv, dt});
		}
		jFrame.validate();
	}
}

class XuLyClientServer implements Runnable {
	static String pathRootServer = System.getProperty("user.home") + "\\Downloads\\PBL4_Server";
	static String pathRootServer1 = System.getProperty("user.home") + "\\Downloads\\PBL4_Server\\public";
	static File folderRootServer = new File(pathRootServer);
	static File folderRootServer1 = new File(pathRootServer1);
	Socket socket;

	public XuLyClientServer(Socket socket) { // alt shift S O
		super();
		this.socket = socket;
	}

	public void run() {
		if (!folderRootServer.exists()) {
			folderRootServer.mkdirs();
		}
		if (!folderRootServer1.exists()) {
			folderRootServer1.mkdirs();
		}
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			String request = dis.readUTF();

			switch (request) {
			case "checkConnect": {
				//System.out.println("Client is checking the connect");
				//System.out.println(socket.getLocalAddress().toString());
				String tk = dis.readUTF();
				String mk = dis.readUTF();
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				BO bo = new BO();
				dos.writeInt(bo.checkLogin(tk, mk, getHostsubPort(socket.getRemoteSocketAddress().toString())));
				//bo.addRecord(tk, mk, getHostsubPort(socket.getRemoteSocketAddress().toString()));
				dos.close();
				dis.close();
				socket.close();
				break;
			}
			case "Connect": {
				String tk = dis.readUTF();
				System.out.println(tk + " đã đăng nhập...");
				BO bo = new BO();
				ArrayList<String> listSharePP = bo.getListShare(tk); // lấy danh sách người có chia sẻ dữ liệu với mình
				try {
					ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
					objectOutput.writeObject(listSharePP);
					objectOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				dis.close();
				socket.close();
				break;
			}
			case "GetData": {
				//System.out.println("Client is connected");
				String tk = dis.readUTF();
				ArrayList<String> listFileInRoot = getNameFileAndFolderInRoot(tk);
				try {
					ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
					objectOutput.writeObject(listFileInRoot);
					objectOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				dis.close();
				socket.close();
				System.out.println("gdt");
				break;
			}
			case "SendFile": {
				try {
					String tk_temp = dis.readUTF(); // thằng gửi lên
					String tk = dis.readUTF(); // lưu vào thư mục của thằng (do có thể gửi vào public)
					String nameFile = dis.readUTF();
					System.out.println(tk + " gửi tệp tin: " + nameFile);
					String pathFile = dis.readUTF(); // xử lý lưu vào db để biết vị trí đồng bộ
					int sizeFile = dis.readInt();
					byte[] fileContentBytes = new byte[sizeFile];
					dis.readFully(fileContentBytes, 0, fileContentBytes.length);
					File fileToDownload = new File(pathRootServer + "\\" + tk + "\\" + nameFile);
					FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
					fileOutputStream.write(fileContentBytes);
					fileOutputStream.close();
					BO bo = new BO();
					if(!tk.equals("public")) {
						bo.addNewData(tk, nameFile, pathFile, "File");
					}
					bo.addRecord(tk_temp, "Gửi lên", tk + ", File: " + fileToDownload.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				dis.close();
				socket.close();
				break;
			}
			case "SendFolder": {
				try {
					String tk_temp = dis.readUTF();
					String tk = dis.readUTF();
					String nameFolder = dis.readUTF();
					System.out.println(tk + " gửi thư mục: " + nameFolder);
					String pathFolder = dis.readUTF(); // xử lý lưu vào db để biết vị trí đồng bộ
					File newfolder = new File(pathRootServer + "\\" + tk + "\\" + nameFolder);
					if (!newfolder.exists()) {
						newfolder.mkdirs();
					}
					ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
					Object objectReceive = objectInput.readObject();
					ArrayList<String> NameFolderList = (ArrayList<String>) objectReceive;
					int amountFileReceive = dis.readInt();
					//System.out.println("sẽ nhận thêm: " + amountFileReceive);
					for (int i = 0; i < NameFolderList.size(); i++) {
						File new_folder = new File(newfolder + "\\" + NameFolderList.get(i));
						if (!new_folder.exists()) {
							new_folder.mkdirs();
						}
					}

					for (int i = 0; i < amountFileReceive; i++) {
						String nameFile = dis.readUTF();
						int sizeFile = dis.readInt();
						byte[] fileContentBytes = new byte[sizeFile];
						dis.readFully(fileContentBytes, 0, fileContentBytes.length);
						File fileToDownload = new File(newfolder + "\\" + nameFile);
						FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
						fileOutputStream.write(fileContentBytes);
						fileOutputStream.close();
					}
					BO bo = new BO();
					if(!tk.equals("public")) {
						bo.addNewData(tk, nameFolder, pathFolder, "Folder");
					}
					bo.addRecord(tk_temp, "Gửi lên", tk + ", Folder: " + newfolder.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				dis.close();
				socket.close();
				break;
			}
			case "Download": { // tạm
				String tk0 = dis.readUTF();
				String tk = dis.readUTF();
				//int indexDown = dis.readInt();
				String name_ = dis.readUTF();
				//System.out.println("Nhận tên: " + tk);
				File fileDown = new File(getPathFileByName(name_, tk));
				if (fileDown.isFile()) {
					System.out.println(tk0 + " tải tệp tin: " + name_);
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					dos.writeUTF("SendFile");
					dos.writeUTF(fileDown.getName());
					FileInputStream fileInputStream = new FileInputStream(fileDown.getAbsolutePath());
					byte[] fileBytes = new byte[(int) fileDown.length()];
					fileInputStream.read(fileBytes);
					dos.writeInt(fileBytes.length);
					dos.write(fileBytes);
					dos.writeUTF("Success");
					dos.close();
					BO bo = new BO();
					bo.addRecord(tk0, "Tải Xuống", "(" + tk + ") File: " + fileDown.getName());
				} else {
					System.out.println(tk0 + " tải thư mục: " + name_);
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					dos.writeUTF("SendFolder");
					dos.writeUTF(fileDown.getName());
					File arr[] = fileDown.listFiles();
					ArrayList<String> nameFile = new ArrayList<>();
					ArrayList<String> nameDire = new ArrayList<>();
					getChild(arr, 0, "", nameFile, nameDire);

					ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
					objectOutput.writeObject(nameDire);

					dos.writeInt(nameFile.size());

					for (int i = 0; i < nameFile.size(); i++) { // send path, name, data.
						File sendFile = new File(fileDown.getPath() + "\\" + nameFile.get(i));
						dos.writeUTF(nameFile.get(i)); // gửi tên + path con trong thư mục chọn gửi
						FileInputStream fileInputStream = new FileInputStream(sendFile.getAbsolutePath());
						byte[] fileBytes = new byte[(int) sendFile.length()];
						fileInputStream.read(fileBytes);
						dos.writeInt(fileBytes.length);
						dos.write(fileBytes);
					}
					dos.writeUTF("Success");
					objectOutput.close();
					dos.close();
					BO bo = new BO();
					bo.addRecord(tk0, "Tải Xuống", "(" + tk + ") Folder: " + fileDown.getName());
				}
				
				dis.close();
				socket.close();
				break;
			}
			case "Delete": { // tạm
				String tk_temp = dis.readUTF();
				String tk = dis.readUTF();
				String name_ = dis.readUTF();
				BO bo = new BO();
				if(!tk.equals("public")) {
					bo.delData(tk, name_);
				}
				File fileDele = new File(getPathFileByName(name_, tk));
				if (fileDele.isFile()) {
					fileDele.delete();
					bo.addRecord(tk_temp, "Xóa", "(" + tk + ") File: " + fileDele.getName());
					System.out.println(tk + " xóa tệp tin: " + name_);
				} else {
					deleteFolder(fileDele);
					bo.addRecord(tk_temp, "Xóa", "(" + tk + ") Folder: " + fileDele.getName());
					System.out.println(tk + " xóa thư mục: " + name_);
				}
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeUTF("Success");
				dos.close();
				dis.close();
				socket.close();
				break;
			}

			case "ListUSShare": {
				// System.out.println("Get List User Share");
				String tk = dis.readUTF();
				BO bo = new BO();
				ArrayList<String> listUS = bo.getListUSShared(tk); // danh sachs all taif khaorn
				ArrayList<String> listUSS = bo.getListUSSharedByMe(tk); // danh sach tk cho pheps
				ArrayList<Boolean> temp = new ArrayList<>();
				for (int i = 0; i < listUS.size(); i++) {
					if (listUSS.contains(listUS.get(i))) {
						temp.add(true);
					} else {
						temp.add(false);
					}
				}
				try {
					ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
					objectOutput.writeObject(listUS);
					objectOutput.writeObject(temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			
			case "OpenShare": {
				String tk1 = dis.readUTF();
				String tk2 = dis.readUTF();
				System.out.println(tk1 + " mở chia sẻ " + tk2);
				BO bo = new BO();
				bo.AddShare(tk1, tk2);
				bo.addRecord(tk1, "Mở Sẻ Chia", tk2);
				socket.close();
				break;
			}

			case "CloseShare": {
				String tk1 = dis.readUTF();
				String tk2 = dis.readUTF();
				System.out.println(tk1 + " đóng chia sẻ " + tk2);
				BO bo = new BO();
				bo.DelShare(tk1, tk2);
				bo.addRecord(tk1, "Đóng Sẻ Chia", tk2);
				dis.close();
				socket.close();
				break;
			}

			case "Copy": {
				String tk1 = dis.readUTF(); // tên người request (copy bỏ vào thư mục này)
				String tk2 = dis.readUTF(); // copy từ dữ liệu người này
				//int index = dis.readInt(); 
				String name_ = dis.readUTF();
				System.out.println(tk1 + " copy: " + tk2 + "_._" + name_);
				File fileCopy = new File(getPathFileByName(name_, tk2));
				File fileCopynew = new File(pathRootServer + "\\" + tk1 + "\\" + fileCopy.getName());

				BO bo = new BO();
				bo.addRecord(tk1, "Sao Chép", "Của: " + tk2 + ": " + fileCopy.getName());
				
				copyFolder(fileCopy, fileCopynew);
				dis.close();
				socket.close();
				break;
			}
			
			case "Synchronize": {
				String tk = dis.readUTF(); // tên người request
				System.out.println(tk + " đồng bộ dữ liệu");
				
				BO bo = new BO();
				
				bo.addRecord(tk, "Đồng Bộ", tk);
				
				ArrayList<String> listNamePath = bo.getListNamePathFolderData(tk);
				ArrayList<String> listPath = bo.getListPathFolderData(tk);
				ArrayList<String> listNamePath2 = bo.getListNamePathFileData(tk);
				ArrayList<String> listPath2 = bo.getListPathFileData(tk);
				try {
					System.out.println("ĐB");
					System.out.println("11189");
					ArrayList<String> temp3 = new ArrayList<>(); // chứa link folder của client gửi dề, để check cái nào không tồn tại nữa thì xóa
					ArrayList<String> temp4 = new ArrayList<>(); // chứa link file của client gửi dề, để check cái nào không tồn tại nữa thì xóa
					ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
					System.out.println("1145");
					objectOutput.writeObject(listPath); // gửi qua path các thư mục đã tải lên
					objectOutput.writeObject(listPath2); // gửi qua path các file đã tải lên
					Boolean check = dis.readBoolean();
					
					if(check) { // nếu các mục gốc còn giữ được
						System.out.println("11");
						for (int i = 0; i < listPath.size(); i++) {
							System.out.println("1110");
							temp3.add(listNamePath.get(i));
							// nhận lại full path cho từng path lớn... tạo thêm nếu có mới
							File newfolder = new File(pathRootServer + "\\" + tk + "\\" + listNamePath.get(i));
							ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

							System.out.println("1115");
							Object objectReceive = objectInput.readObject(); // chứa list path folder của clt
							ArrayList<String> NameFolderList = (ArrayList<String>) objectReceive;
							Object objectReceive2 = objectInput.readObject(); // chứa list path file của clt
							ArrayList<String> NameFileList = (ArrayList<String>) objectReceive2;
							System.out.println("1116");
							for (int j = 0; j < NameFolderList.size(); j++) {
								System.out.println("111a");
								File new_folder = new File(newfolder + "\\" + NameFolderList.get(j));
								System.out.println("111f");
								temp3.add(listNamePath.get(i) + "\\" + NameFolderList.get(j));
								System.out.println("111b");
								if (!new_folder.exists()) {
									new_folder.mkdirs();
								}
							}
							System.out.println("111");
							for (int j = 0; j < NameFileList.size(); j++) {
								temp4.add(listNamePath.get(i) + "\\" + NameFileList.get(j));
							}
							System.out.println("112");
						}
						
						if(listNamePath2.size() > 0) {
							for (int j = 0; j < listNamePath2.size(); j++) {
								temp4.add(listNamePath2.get(j));
							}
						}
						
						File folderTK = new File(pathRootServer + "\\" + tk);
						ArrayList<String> temp1 = new ArrayList<>(); // chứa link file ở sv
						ArrayList<String> temp2 = new ArrayList<>(); // chứa link folder ở sv
						
						getChild(folderTK.listFiles(), 0, "", temp1, temp2);
						

						for (int i = 0; i < temp1.size(); i++) { // so sánh list tên file, cái nào không còn thì xóa
							if (temp4.contains(temp1.get(i))) {
								System.out.print(".");
							} else {
								File fileDele = new File(folderTK + "\\" + temp1.get(i));
								if(fileDele.exists()) {
									fileDele.delete();
								}
								System.out.println("\nTên file này không có ở clt: " + folderTK + "\\" + temp1.get(i));
							}
						}
						System.out.print("\n");
						
						for (int i = 0; i < temp2.size(); i++) { // so sánh list tên folder, cái nào không còn thì xóa
							if (temp3.contains(temp2.get(i))) {
								System.out.print(".");
							} else {
								File fileDele = new File(folderTK + "\\" + temp2.get(i));
								if(fileDele.exists()) {
									deleteFolder(fileDele);
								}
								System.out.println("\nTên fol này không có ở clt: " + folderTK + "\\" + temp2.get(i));
							}
						}
						
						// lấy hết mã MD5 của các file, gửi qua client so sánh, nếu khác nhận lại 
						// file của client về khởi tạo.
						File arr[] = folderTK.listFiles();
						ArrayList<String> listPathFile1 = new ArrayList<>();
						ArrayList<String> listPathFolder1 = new ArrayList<>();
						getChild(arr, 0, folderTK + "\\", listPathFile1, listPathFolder1);
//						for(int i = 0; i < listPathFile1.size(); i++) {
//							System.out.println(i + ": " + listPathFile1.get(i));
//						}
						ArrayList<String> MD5Files = getListMD5byPathFile(listPathFile1);
						objectOutput.writeObject(MD5Files);
						
						
						int soLuongDongBo = dis.readInt();
						
						for(int i = 0; i < soLuongDongBo; i++) {
							String path_temp = dis.readUTF();
							System.out.println(folderTK + "\\" + path_temp);
							
							File fileDele = new File(folderTK + "\\" + path_temp);
							if (fileDele.exists()) {
								fileDele.delete();
							}
							
							int sizeFile = dis.readInt();
							byte[] fileContentBytes = new byte[sizeFile];
							dis.readFully(fileContentBytes, 0, fileContentBytes.length);
							File fileToDownload = new File(folderTK + "\\" + path_temp);
							FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
							fileOutputStream.write(fileContentBytes);
							fileOutputStream.close();
						}
					}
					
					
				} catch (Exception e) {
					System.out.println("ERROR345: " + e);
				}
				dis.close();
				socket.close();
				break;
			}
			
			default:
				System.out.println("end");
				dis.close();
				socket.close();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// gửi lại path
	// chưa fix - đã fix - tạm
	public static ArrayList<String> getNameFileAndFolderInRoot(String tk) {
		String pathForTK = pathRootServer + "\\" + tk;
		File folderForTK = new File(pathForTK);
		if (!folderForTK.exists()) {
			folderForTK.mkdirs();
		}
		ArrayList<String> trave = new ArrayList<String>();
		File[] files = folderForTK.listFiles();
		for (File file : files) {
			long sizze = getDirectorySizeLegacy(file);
			sizze = sizze / 1024 + 1;
			String sizee = "";
			if(sizze > 1024 * 1024) {
				int si = (int)sizze * 100 / (1024*1024);
				sizee = (si * 1.0 / 100) + "Gb";
			} else {
				if(sizze > 1024) {
					int si = (int)sizze * 100 / (1024);
					sizee = (si * 1.0 / 100) + "Mb";
				} else {
					sizee = sizze + "Kb";
				}
			}
			if (file.isFile()) {
				trave.add("(" + sizee + ") Tệp: " + file.getName());
			}
			if (file.isDirectory()) {
				trave.add("(" + sizee + ") Thư mục: " + file.getName());
			}
		}
		return trave;
	}
	
	public static long getDirectorySizeLegacy(File dir) {
	      long length = 0;
	      File[] files = dir.listFiles();
	      if (files != null) {
	          for (File file : files) {
	              if (file.isFile())
	                  length += file.length();
	              else
	                  length += getDirectorySizeLegacy(file);
	          }
	      }
	      return length;
	  }
	
	public static String getPathFileByName(String name, String tk) {
		String pathForTK = pathRootServer + "\\" + tk;
		File folderForTK = new File(pathForTK);
		File[] files = folderForTK.listFiles();
		for(File i : files) {
			if(i.getName().equals(name)) {
				System.out.println(i.getPath());
				return i.getPath();
			}
		}
		return "none";
	}

	// gửi lại path - tạm
	// chưa fix - folder vừa gửi thì không xóa được ?
	static void deleteFolder(File file) {
		// System.out.println("Xóa thư mục " + file.getPath());
		File[] contents = file.listFiles();
		for (int i = 0; i < contents.length; i++) {
			contents[i].delete();
		}
		File[] contentss = file.listFiles();
		if (contentss != null) {
			for (int i = 0; i < contentss.length; i++) {
				if (contentss[i].isFile()) {
					contentss[i].delete();
				} else {
					deleteFolder(contentss[i]);
				}
			}
		}
		file.delete();
	}

	static void getChild(File[] arr, int index, String path, ArrayList<String> nameFile, ArrayList<String> nameDire) {
		if (index == arr.length)
			return;

		if (arr[index].isFile())
			nameFile.add(path + arr[index].getName());

		else if (arr[index].isDirectory()) {
			nameDire.add(path + arr[index].getName());
			getChild(arr[index].listFiles(), 0, path + arr[index].getName() + "\\", nameFile, nameDire);
		}

		getChild(arr, ++index, path, nameFile, nameDire);
	}

	
	static void copyFolder(File sourceFolder, File targetFolder) {
		if (sourceFolder.isDirectory()) {
            // Xac nhan neu targetFolder chua ton tai thi tao moi
            if (!targetFolder.exists()) {
                targetFolder.mkdir();
                //System.out.println("Thu muc da duoc tao " + targetFolder);
            }
            // Liet ke tat ca cac file va thu muc trong sourceFolder
            String files[] = sourceFolder.list();
            for (String file : files) {
                File srcFile = new File(sourceFolder, file);
                File tarFile = new File(targetFolder, file);
                // goi lai phuong thuc copyFolder
                copyFolder(srcFile, tarFile);
            }
        } else {
        	try {
        		FileInputStream fileInputStream = new FileInputStream(sourceFolder.getAbsolutePath());
    			FileOutputStream fileOutputStream = new FileOutputStream(targetFolder);
    			
    			byte[] fileBytes = new byte[(int) sourceFolder.length()];
    			fileInputStream.read(fileBytes);
    			fileOutputStream.write(fileBytes);
    			fileInputStream.close();
    			fileOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	static String getMD5(File file) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);
			byte[] dataBytes = new byte[1024];
			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			byte[] byteData = md.digest();
			fis.close();
			return convertByteToHex(byteData);
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	static String convertByteToHex(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
	public ArrayList<String> getListMD5byPathFile(ArrayList<String> listPath) {
		ArrayList<String> trave = new ArrayList<String>();
		for(int i = 0; i < listPath.size(); i++) {
			File temp1 = new File(listPath.get(i));
			trave.add(getMD5(temp1));
		}
		return trave;
	}
	
	static String getHostsubPort(String s) { // input "File: hello.cpp" -> output "hello.cpp"
		int index = 0;
		for(int i = 0; i < s.length() - 1; i++) {
			if(s.charAt(i) == ':') {
				index = i;
				break;
			}
		}
		return s.substring(0, index);
	}
}


class BO { ///// SQLLLLLLL
	
	public void addRecord(String tk, String doSt, String obj) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("insert into HoatDong(TaiKhoan, HanhVi, DoiTuong) VALUES (?, ?, ?);");
			ps.setString(1, tk);
			ps.setString(2, doSt);
			ps.setString(3, obj);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR21: " + e);
		}
	}
	
	public int checkLogin(String tk, String mk, String ip) {
		int trave = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from phongban");
			while (rs.next()) {
				String tkk = rs.getString("TaiKhoan");
				String mkk = rs.getString("MatKhau");
				String ipp = rs.getString("IP");
				if (tk.equals(tkk) && mk.equals(mkk)) {
					if (tk.equals(tkk) && ip.equals(ipp)) {
						trave = 10;
					} else {
						trave = 1;
					}
				}
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println("ERROR1: " + e);
		}
		return trave;
	}

	public ArrayList<String> getListUSShared(String tk) {
		ArrayList<String> trave = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from phongban");
			while (rs.next()) {
				String name = rs.getString("TaiKhoan");
				trave.add(name);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println("ERROR2: " + e);
		}
		return trave;
	}

	public ArrayList<String> getListUSSharedByMe(String tk) { // lấy danh sách những người mình chia sẻ dữ liệu ra
		ArrayList<String> trave = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");

			PreparedStatement ps;
			ps = con.prepareStatement("select * from chiase where TaiKhoanChiaSe = ?;");
			ps.setString(1, tk);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String name = rs.getString("TaiKhoanNhanChiaSe");
				trave.add(name);
			}
			rs.close();
			ps.close();
			
		} catch (Exception e) {
			System.out.println("ERROR3: " + e);
		}
		return trave;
	}

	public void AddShare(String tk1, String tk2) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("insert into ChiaSe(TaiKhoanChiaSe, TaiKhoanNhanChiaSe) VALUES (?, ?);");
			ps.setString(1, tk1);
			ps.setString(2, tk2);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR4: " + e);
		}
	}

	public void DelShare(String tk1, String tk2) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("delete from ChiaSe where TaiKhoanChiaSe = ? && TaiKhoanNhanChiaSe = ?;");
			ps.setString(1, tk1);
			ps.setString(2, tk2);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR5: " + e);
		}
	}

	public ArrayList<String> getListShare(String tk) { // lấy danh sách những người đã chia sẻ với mình
		ArrayList<String> trave = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("select * from chiase where TaiKhoanNhanChiaSe = ?;");
			ps.setString(1, tk);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String name = rs.getString("TaiKhoanChiaSe");
				trave.add(name);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR6: " + e);
		}
		return trave;
	}
	
	public void addNewData(String tk, String tenData, String pathData, String type) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("INSERT INTO dulieu(TaiKhoan, TenDL, PathDL, TypeDL) values (?, ?, ?, ?);");
			ps.setString(1, tk);
			ps.setString(2, tenData);
			ps.setString(3, pathData);
			ps.setString(4, type);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR7: " + e);
		}
	}
	
	public void delData(String tk, String tenData) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("delete from dulieu where TaiKhoan = ? && TenDL = ?;");
			ps.setString(1, tk);
			ps.setString(2, tenData);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR8: " + e);
		}
	}
	
	public ArrayList<String> getListPathFolderData(String tk) {
		// trả về list path thư mục của client gửi qua
		ArrayList<String> trave = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("select * from dulieu where TaiKhoan = ? and TypeDL = 'Folder';");
			ps.setString(1, tk);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String path = rs.getString("PathDL");
				trave.add(path);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR9: " + e);
		}
		return trave;
	}
	
	public ArrayList<String> getListPathFileData(String tk) {
		// trả về list path thư mục của client gửi qua
		ArrayList<String> trave = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("select * from dulieu where TaiKhoan = ? and TypeDL = 'File';");
			ps.setString(1, tk);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String path = rs.getString("PathDL");
				trave.add(path);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR1245: " + e);
		}
		return trave;
	}
	
	public ArrayList<String> getListNamePathFolderData(String tk) {
		// trả về tên thư mục của client gửi qua
		ArrayList<String> trave = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("select * from dulieu where TaiKhoan = ? and TypeDL = 'Folder';");
			ps.setString(1, tk);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String path = rs.getString("TenDL");
				trave.add(path);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR10: " + e);
		}
		return trave;
	}
	
	public ArrayList<String> getListNamePathFileData(String tk) {
		// trả về tên thư mục của client gửi qua
		ArrayList<String> trave = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("select * from dulieu where TaiKhoan = ? and TypeDL = 'File';");
			ps.setString(1, tk);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String path = rs.getString("TenDL");
				trave.add(path);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR17: " + e);
		}
		return trave;
	}
	
//	public ArrayList<String> get100ActivityNewest() {
//		// trả về 100 hoạt động gần nhất
//		ArrayList<String> trave = new ArrayList<String>();
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			String url = "jdbc:mysql://localhost:3306/pbl4";
//			Connection con = DriverManager.getConnection(url, "root", "");
//			PreparedStatement ps;
//			ps = con.prepareStatement("SELECT * FROM `hoatdong` ORDER BY MSHD DESC LIMIT 100");
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				String atvt = "";
//				atvt += "   " + rs.getString("ThoiGian");
//				atvt +=	"   " + rs.getString("TaiKhoan");
//				atvt += "   " + rs.getString("HanhVi");
//				atvt += "   " + rs.getString("DoiTuong");
//				
//				if(atvt.length() > 92) {
//					atvt = atvt.substring(0, 90) + "...";
//				}
//				
//				trave.add(atvt);
//			}
//			rs.close();
//			ps.close();
//		} catch (Exception e) {
//			System.out.println("ERROR172: " + e);
//		}
//		return trave;
//	}
	
	public ArrayList<HoatDong> get100ActivityNewest() {
		// trả về 100 hoạt động gần nhất
		ArrayList<HoatDong> trave = new ArrayList<HoatDong>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("SELECT * FROM `hoatdong` ORDER BY MSHD DESC LIMIT 100");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				HoatDong temp = new HoatDong();
				temp.setThoiGian(rs.getString("ThoiGian"));
				temp.setTaiKhoan(rs.getString("TaiKhoan"));
				temp.setHanhVi(rs.getString("HanhVi"));
				temp.setDoiTuong(rs.getString("DoiTuong"));
				
				trave.add(temp);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR172: " + e);
		}
		return trave;
	}
	
	public ArrayList<HoatDong> getSelectedActivity(String tk, String hv, String staTime, String endTime) {
		// trả về 100 hoạt động gần nhất
		ArrayList<HoatDong> trave = new ArrayList<HoatDong>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("SELECT * FROM `hoatdong` WHERE TaiKhoan LIKE ? and HanhVi LIKE ?"
					+ " AND ThoiGian BETWEEN ? AND ? + INTERVAL 1 day ORDER BY MSHD DESC LIMIT 100");
			ps.setString(1, "%" + tk + "%");
			ps.setString(2, "%" + hv + "%");
			ps.setString(3, staTime);
			ps.setString(4, endTime);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				HoatDong temp = new HoatDong();
				temp.setThoiGian(rs.getString("ThoiGian"));
				temp.setTaiKhoan(rs.getString("TaiKhoan"));
				temp.setHanhVi(rs.getString("HanhVi"));
				temp.setDoiTuong(rs.getString("DoiTuong"));
				
				trave.add(temp);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR172: " + e);
		}
		return trave;
	}
}