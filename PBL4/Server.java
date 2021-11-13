/*
 * Xong mở chia sẻ
 * chưa mở được thư mục chia sẻ của người khác -- đã rồi
 * chưa đồng bộ
 * 
 */

package PBL4;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(56789);

		JFrame jFrame;
		JButton jbSignUp, jbManageUser, jbManageActivate;
		JLabel jlTitle, jlNotify, jlInfo;
		JPanel jpShowNotify, jpButton;
		JScrollPane jScrollPane;

		jFrame = new JFrame("Server GUI");
		jFrame.setSize(750, 900);
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
		jlNotify.setFont(new Font("Arial", Font.BOLD, 20));
		jlNotify.setBorder(new EmptyBorder(20, 0, 0, 0));
		jlNotify.setAlignmentX(Component.CENTER_ALIGNMENT);

		jpShowNotify = new JPanel();
		jpShowNotify.setBorder(new EmptyBorder(10, 0, 10, 0));
		jpShowNotify.setLayout(new BoxLayout(jpShowNotify, BoxLayout.Y_AXIS));
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

		while (true) {
			try {
				Socket socket = serverSocket.accept();
				Thread mainThread = new Thread(new XuLyClientServer(socket));
				mainThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
				dos.writeInt(bo.checkLogin(tk, mk, socket.getLocalAddress().toString()));
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
					String tk = dis.readUTF();
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
					bo.addNewData(tk, nameFile, pathFile, "File");
				} catch (Exception e) {
					e.printStackTrace();
				}
				dis.close();
				socket.close();
				break;
			}
			case "SendFolder": {
				try {
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
					bo.addNewData(tk, nameFolder, pathFolder, "Folder");
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
				}
				dis.close();
				socket.close();
				break;
			}
			case "Delete": { // tạm
				String tk = dis.readUTF();
				String name_ = dis.readUTF();
				BO bo = new BO();
				bo.delData(tk, name_);
				File fileDele = new File(getPathFileByName(name_, tk));
				if (fileDele.isFile()) {
					fileDele.delete();
					System.out.println(tk + " xóa tệp tin: " + name_);
				} else {
					deleteFolder(fileDele);
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
				dis.close();
				socket.close();
				break;
			}

			case "CloseShare": {
				String tk1 = dis.readUTF();
				String tk2 = dis.readUTF();
				System.out.println(tk1 + " đóng chia sẻ " + tk2);
				BO bo = new BO();
				bo.DelShare(tk1, tk2);
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
				
				copyFolder(fileCopy, fileCopynew);
				dis.close();
				socket.close();
				break;
			}
			
			case "Synchronize": {
				String tk = dis.readUTF(); // tên người request
				System.out.println(tk + " đồng bộ dữ liệu");
				BO bo = new BO();
				ArrayList<String> listNamePath = bo.getListNamePathFolderData(tk);
				ArrayList<String> listPath = bo.getListPathFolderData(tk);
				try {
					System.out.println("ĐB");
					ArrayList<String> temp3 = new ArrayList<>(); // chứa link folder của client gửi dề, để check cái nào không tồn tại nữa thì xóa
					ArrayList<String> temp4 = new ArrayList<>(); // chứa link file của client gửi dề, để check cái nào không tồn tại nữa thì xóa
					ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
					objectOutput.writeObject(listPath); // gửi qua path các thư mục đã tải lên
					
					Boolean check = dis.readBoolean();
					
					if(check) { // nếu các mục gốc còn giữ được
						for (int i = 0; i < listPath.size(); i++) {
							temp3.add(listNamePath.get(i));
							// nhận lại full path cho từng path lớn... tạo thêm nếu có mới
							File newfolder = new File(pathRootServer + "\\" + tk + "\\" + listNamePath.get(i));
							ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
							Object objectReceive = objectInput.readObject(); // chứa list path folder của clt
							ArrayList<String> NameFolderList = (ArrayList<String>) objectReceive;
							Object objectReceive2 = objectInput.readObject(); // chứa list path file của clt
							ArrayList<String> NameFileList = (ArrayList<String>) objectReceive2;
							for (int j = 0; j < NameFolderList.size(); j++) {
								File new_folder = new File(newfolder + "\\" + NameFolderList.get(j));
								temp3.add(listNamePath.get(i) + "\\" + NameFolderList.get(j));
								temp4.add(listNamePath.get(i) + "\\" + NameFileList.get(j));
								if (!new_folder.exists()) {
									new_folder.mkdirs();
								}
							}
							
							for (int j = 0; j < NameFileList.size(); j++) {
								temp4.add(listNamePath.get(i) + "\\" + NameFileList.get(j));
							}
						}

						File folderTK = new File(pathRootServer + "\\" + tk);
						ArrayList<String> temp1 = new ArrayList<>(); // chứa link file ở sv
						ArrayList<String> temp2 = new ArrayList<>(); // chứa link folder ở sv
						
						getChild(folderTK.listFiles(), 0, "", temp1, temp2);
						
						for (int i = 0; i < temp2.size(); i++) {
							if (temp3.contains(temp2.get(i))) {
								System.out.print(".");
							} else {
								System.out.println("\nTên fol này không có ở clt: " + folderTK + "\\" + temp2.get(i));
							}
						}
						
						for (int i = 0; i < temp1.size(); i++) {
							if (temp4.contains(temp1.get(i))) {
								System.out.print(".");
							} else {
								System.out.println("\nTên file này không có ở clt: " + folderTK + "\\" + temp1.get(i));
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
			if (file.isFile()) {
				trave.add("Tệp: " + file.getName());
			}
			if (file.isDirectory()) {
				trave.add("Thư mục: " + file.getName());
			}
		}
		return trave;
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
}


class BO { ///// SQLLLLLLL
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
}