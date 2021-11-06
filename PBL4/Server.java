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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Server {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(56789);

		JFrame jFrame;
		JLabel jlTitle, jlNotify, jlInfo;
		JPanel jpShowNotify;
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

		jlInfo = new JLabel(InetAddress.getLocalHost().getHostAddress() + "   " + serverSocket.getLocalSocketAddress());
		jlInfo.setFont(new Font("Arial", Font.ITALIC, 20));
		jlInfo.setBorder(new EmptyBorder(20, 0, 10, 0));
		jlInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

		jlNotify = new JLabel("Thông báo");
		jlNotify.setFont(new Font("Arial", Font.BOLD, 20));
		jlNotify.setBorder(new EmptyBorder(20, 0, 0, 0));
		jlNotify.setAlignmentX(Component.CENTER_ALIGNMENT);

		jpShowNotify = new JPanel();
		jpShowNotify.setBorder(new EmptyBorder(10, 0, 10, 0));
		jpShowNotify.setLayout(new BoxLayout(jpShowNotify, BoxLayout.Y_AXIS));
		jScrollPane = new JScrollPane(jpShowNotify);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// jpShowFileServer.add(jpShowFileServer);

		jFrame.add(jlTitle);
		jFrame.add(jlInfo);
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
				dos.writeInt(checkLogin(tk, mk, socket.getLocalAddress().toString()));
				dos.close();
				dis.close();
				socket.close();
				break;
			}
			case "Connect": {
				//System.out.println("Client is connected");
				String tk = dis.readUTF();
				ArrayList<String> listSharePP = getListShare(tk); // lấy danh sách người có chia sẻ dữ liệu với mình
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
					int sizeFile = dis.readInt();
					byte[] fileContentBytes = new byte[sizeFile];
					dis.readFully(fileContentBytes, 0, fileContentBytes.length);
					File fileToDownload = new File(pathRootServer + "\\" + tk + "\\" + nameFile);
					FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
					fileOutputStream.write(fileContentBytes);
					fileOutputStream.close();
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
				} catch (Exception e) {
					e.printStackTrace();
				}
				dis.close();
				socket.close();
				break;
			}
			case "Download": { // tạm
				String tk = dis.readUTF();
				//int indexDown = dis.readInt();
				String name_ = dis.readUTF();
				//System.out.println("Nhận tên: " + tk);
				File fileDown = new File(getPathFileByName(name_, tk));
				if (fileDown.isFile()) {
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
				File fileDele = new File(getPathFileByName(name_, tk));
				if (fileDele.isFile()) {
					fileDele.delete();
				} else {
					deleteFolder(fileDele);
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
				ArrayList<String> listUS = getListUSShared(tk); // danh sachs all taif khaorn
				ArrayList<String> listUSS = getListUSSharedByMe(tk); // danh sach tk cho pheps
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
				// System.out.println(tk1 + " " + tk2);
				AddShare(tk1, tk2);
				dis.close();
				socket.close();
				break;
			}

			case "CloseShare": {
				String tk1 = dis.readUTF();
				String tk2 = dis.readUTF();
				// System.out.println(tk1 + " " + tk2);
				DelShare(tk1, tk2);
				dis.close();
				socket.close();
				break;
			}

			case "Copy": {
				String tk1 = dis.readUTF(); // tên người request (copy bỏ vào thư mục này)
				String tk2 = dis.readUTF(); // copy từ dữ liệu người này
				//int index = dis.readInt(); 
				String name_ = dis.readUTF();
				File fileCopy = new File(getPathFileByName(name_, tk2));
				File fileCopynew = new File(pathRootServer + "\\" + tk1 + "\\" + fileCopy.getName());
				
				copyFolder(fileCopy, fileCopynew);
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
	
	///// SQLLLLLLL

	static int checkLogin(String tk, String mk, String ip) {
		int trave = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4_test";
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
			System.out.println("ERROR: " + e);
		}
		return trave;
	}

	public static ArrayList<String> getListUSShared(String tk) {
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
			System.out.println("ERROR: " + e);
		}
		return trave;
	}

	public static ArrayList<String> getListUSSharedByMe(String tk) { // lấy danh sách những người mình chia sẻ dữ liệu ra
		ArrayList<String> trave = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from chiase where TaiKhoanChiaSe = '" + tk + "';");
			while (rs.next()) {
				String name = rs.getString("TaiKhoanNhanChiaSe");
				trave.add(name);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
		return trave;
	}

	public static void AddShare(String tk1, String tk2) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			Statement stmt = con.createStatement();
			String qr = "INSERT INTO ChiaSe(TaiKhoanChiaSe, TaiKhoanNhanChiaSe) VALUES ('" + tk1 + "', '" + tk2 + "');";
			stmt.executeUpdate(qr);
			stmt.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}

	public static void DelShare(String tk1, String tk2) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			Statement stmt = con.createStatement();
			String qr = "delete from ChiaSe where TaiKhoanChiaSe = '" + tk1 + "' && TaiKhoanNhanChiaSe = '" + tk2
					+ "';";
			stmt.executeUpdate(qr);
			stmt.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}

	public static ArrayList<String> getListShare(String tk) { // lấy danh sách những người đã chia sẻ với mình
		ArrayList<String> trave = new ArrayList<String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from chiase where TaiKhoanNhanChiaSe = '" + tk + "';");
			while (rs.next()) {
				String name = rs.getString("TaiKhoanChiaSe");
				trave.add(name);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
		return trave;
	}
}