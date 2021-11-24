/*
 * ******** SPEED TEST *********
 * ***** 192.168.1.12 - local other: sidebyside: 10-11MB, 10m: 1.5MB
 * *****************************
 */

package PBL4;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Client {
	static JFrame jFrame;
	static JPanel jpConnect, jpButton, jpShowFileServer, jpListUser;
	static JTextField jtHost, jtPort, jtTK, jtMK;
	static JLabel jlTitle, jlFileName, jlDataServer;
	static JComboBox jcShare;
	static JButton jbConnect;
	static String host = ""; static int port = 0;
	static String pathRootClient = System.getProperty("user.home") + "\\Downloads\\PBL4_Client";
	static File folderRootClient = new File(pathRootClient);
	
	public static void main(String[] args) {
		
		if (!folderRootClient.exists()){
			folderRootClient.mkdirs();
        }
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		final File[] fileToSend = new File[1];
		final File[] folderToSend = new File[1];
		JButton jbChooseFile, jbChooseFolder, jbSendFile, jbSendFolder, jbShare, jbSynch;
		JLabel jlhost, jlport, jlTK, jlMK;
		JScrollPane jScrollPane;

		jFrame = new JFrame("Client GUI");
		jFrame.setSize(750, 1024);
		jFrame.setLocationRelativeTo(null);
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jlTitle = new JLabel("Client - Đồng bộ dữ liệu");
		jlTitle.setFont(new Font("Arial", Font.BOLD, 40));
		jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
		jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		jlFileName = new JLabel("Chọn tệp hoặc thư mục cần gửi đi");
		jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
		jlFileName.setBorder(new EmptyBorder(0, 0, 0, 0));
		jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

		jlDataServer = new JLabel("Hãy kết nối trước!!!");
		jlDataServer.setFont(new Font("Arial", Font.BOLD, 20));
		jlDataServer.setBorder(new EmptyBorder(20, 0, 0, 0));
		jlDataServer.setAlignmentX(Component.CENTER_ALIGNMENT);

		jpConnect = new JPanel();
		jpConnect.setBorder(new EmptyBorder(10, 0, 0, 0));

		jpButton = new JPanel();
		jpButton.setBorder(new EmptyBorder(10, 0, 10, 0));

		jpShowFileServer = new JPanel();
		jpShowFileServer.setBorder(new EmptyBorder(10, 0, 10, 0));
		jpShowFileServer.setLayout(new BoxLayout(jpShowFileServer, BoxLayout.Y_AXIS));
		jScrollPane = new JScrollPane(jpShowFileServer);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setPreferredSize(new Dimension(10, 400));

		jtHost = new JTextField("localhost");
		jtHost.setPreferredSize(new Dimension(240, 40));
		jtHost.setFont(new Font("Arial", Font.PLAIN, 20));
		jtPort = new JTextField("56789");
		jtPort.setPreferredSize(new Dimension(100, 40));
		jtPort.setFont(new Font("Arial", Font.PLAIN, 20));
		jtTK = new JTextField("phongban01");
		jtTK.setPreferredSize(new Dimension(250, 40));
		jtTK.setFont(new Font("Arial", Font.PLAIN, 20));
		jtMK = new JTextField("1");
		jtMK.setPreferredSize(new Dimension(250, 40));
		jtMK.setFont(new Font("Arial", Font.PLAIN, 20));
		
		jlhost = new JLabel("Host: ");
		jlhost.setFont(new Font("Arial", Font.BOLD, 20));
		jlhost.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		jlport = new JLabel("Port: ");
		jlport.setFont(new Font("Arial", Font.BOLD, 20));
		jlport.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		jlTK = new JLabel("User: ");
		jlTK.setFont(new Font("Arial", Font.BOLD, 20));
		jlTK.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		jlMK = new JLabel("Pass: ");
		jlMK.setFont(new Font("Arial", Font.BOLD, 20));
		jlMK.setBorder(new EmptyBorder(20, 0, 0, 0));
		
		jbConnect = new JButton("Kết nối");
		jbConnect.setPreferredSize(new Dimension(200, 40));
		jbConnect.setFont(new Font("Arial", Font.BOLD, 20));

		jbChooseFile = new JButton("Chọn tệp để gửi");
		jbChooseFile.setPreferredSize(new Dimension(275, 40));
		jbChooseFile.setFont(new Font("Arial", Font.BOLD, 20));
		jbChooseFile.setEnabled(false);

		jbChooseFolder = new JButton("Chọn thư mục để gửi");
		jbChooseFolder.setPreferredSize(new Dimension(275, 40));
		jbChooseFolder.setFont(new Font("Arial", Font.BOLD, 20));
		jbChooseFolder.setEnabled(false);

		jbSendFile = new JButton("Gửi tệp đã chọn");
		jbSendFile.setPreferredSize(new Dimension(275, 40));
		jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));
		jbSendFile.setEnabled(false);

		jbSendFolder = new JButton("Gửi thư mục đã chọn");
		jbSendFolder.setPreferredSize(new Dimension(275, 40));
		jbSendFolder.setFont(new Font("Arial", Font.BOLD, 20));
		jbSendFolder.setEnabled(false);
		
		jcShare = new JComboBox();
		jcShare.setPreferredSize(new Dimension(200, 40));
		jcShare.setFont(new Font("Arial", Font.BOLD, 20));
		jcShare.setEnabled(false);
		
		jbShare = new JButton("Chia sẻ");
		jbShare.setPreferredSize(new Dimension(200, 40));
		jbShare.setFont(new Font("Arial", Font.BOLD, 20));
		jbShare.setEnabled(false);
		
		jbSynch = new JButton("Đồng bộ");
		jbSynch.setPreferredSize(new Dimension(200, 40));
		jbSynch.setFont(new Font("Arial", Font.BOLD, 20));
		jbSynch.setEnabled(false);

		jpConnect.add(jlhost);
		jpConnect.add(jtHost);
		jpConnect.add(jlport);
		jpConnect.add(jtPort);
		jpConnect.add(jbConnect);
		jpConnect.add(jlTK);
		jpConnect.add(jtTK);
		jpConnect.add(jlMK);
		jpConnect.add(jtMK);
		jpButton.add(jbChooseFile);
		jpButton.add(jbSendFile);
		jpButton.add(jbChooseFolder);
		jpButton.add(jbSendFolder);
		jpButton.add(jbShare);
		jpButton.add(jbSynch);
		jpButton.add(jcShare);

		// jpShowFileServer.add(jpShowFileServer);

		jPanel.add(jlTitle);
		jPanel.add(jpConnect);
		jPanel.add(jlFileName);
		jPanel.add(jpButton);
		jPanel.add(jlDataServer);
		jPanel.add(jScrollPane);
		jFrame.add(jPanel);
		jFrame.setVisible(true);

		jbConnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!jbConnect.getText().equals("Kết nối")) {
					jbSynch.setEnabled(false);
					jbChooseFile.setEnabled(false);
					jbChooseFolder.setEnabled(false);
					jbShare.setEnabled(false);
					jlDataServer.setText("Vui Lòng kết nối");
					jtHost.setEditable(true);
					jtPort.setEditable(true);
					jtTK.setEditable(true);
					jtMK.setEditable(true);
					
					fileToSend[0] = null;
					folderToSend[0] = null;
					
					jbConnect.setText("Kết nối");

					jcShare.removeAllItems();
					jcShare.revalidate();
					jcShare.repaint();
					jcShare.setEnabled(false);

					jpShowFileServer.removeAll();
					jpShowFileServer.revalidate();
					jpShowFileServer.repaint();
					

				} else {
					if(jtHost.getText().equals("") || jtPort.getText().equals("") || jtTK.getText().equals("") || jtMK.getText().equals("")) {
						jlDataServer.setText("Nhập vào đủ thông tin!!!");
					} else {
						jpShowFileServer.removeAll();
						jpShowFileServer.revalidate();
						jpShowFileServer.repaint();
						int status_ = isServerAlive();
						if (status_ == 10) {
							jbConnect.setText("Ngắt kết nối");
							LoadDataCBB();
							jbSynch.setEnabled(true);
							jbChooseFile.setEnabled(true);
							jbChooseFolder.setEnabled(true);
							jbShare.setEnabled(true);
							jcShare.setEnabled(true);
							jlDataServer.setText("Đã kết nối đến Server! Dữ liệu của bạn trên Server ở đây:");
							jtHost.setEditable(false);
							jtPort.setEditable(false);
							jtTK.setEditable(false);
							jtMK.setEditable(false);
						} 
						if (status_ == 0) {
							jlDataServer.setText("Tài khoản truy cập không đúng!! Vui lòng kiểm tra lại!");
						}
						
						if(status_ == -1) {
							jlDataServer.setText("Xãy ra sự cố khi kết nối đến Server!! Vui lòng kiểm tra lại!");
						}
						
						if(status_ == 1) {
							jlDataServer.setText("Đơn vị truy cập không đúng!");
						}
					}
				}
			}
		});

		jbChooseFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setDialogTitle("Chọn tệp để gửi đi: ");
				if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileToSend[0] = jFileChooser.getSelectedFile();
					jlFileName.setText("Bạn muốn gửi tệp: '" + fileToSend[0].getName() + "' ?");
					jbSendFile.setEnabled(true);
				}
			}
		});

		jbSendFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jlFileName.setText("Đang gửi đi, có thể mất vài phút nếu đường truyền bận!");
				if (isServerAlive() == 10) {
					try {
						Socket socket = new Socket(host, port);
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						dos.writeUTF("SendFile");
						dos.writeUTF(jtTK.getText());
						if(jcShare.getSelectedIndex()==0) {
							dos.writeUTF(jtTK.getText());
						} else {
							dos.writeUTF("public");
						}
						dos.writeUTF(fileToSend[0].getName()); // gửi tên file
						//System.out.println(fileToSend[0].getName() + " ...//... " + fileToSend[0].getPath());
						dos.writeUTF(fileToSend[0].getPath()); // gửi path file trong máy client
						FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
						byte[] fileBytes = new byte[(int) fileToSend[0].length()];
						fileInputStream.read(fileBytes);
						dos.writeInt(fileBytes.length);
						dos.write(fileBytes);
						dos.close();
						socket.close();
						jbSendFile.setEnabled(false);
						jlFileName.setText("Đã gửi tệp: '" + fileToSend[0].getName() + "' thành công!");
						LoadData(); // gửi file xong load lại
						fileToSend[0] = null;
					} catch (Exception er) {
						System.out.println("ERROR1: " + e);
					}
				}
			}
		});

		jbChooseFolder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jFileChooser.setDialogTitle("Chọn thư mục để gửi đi: ");
				if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					folderToSend[0] = jFileChooser.getSelectedFile();
					jlFileName.setText("Bạn muốn gửi thư mục: '" + folderToSend[0].getName() + "' ?");
					jbSendFolder.setEnabled(true);
				}
			}
		});

		jbSendFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jlFileName.setText("Đang gửi đi, có thể mất vài phút nếu đường truyền bận!");
				if (isServerAlive() == 10) {
					try {
						Socket socket = new Socket(host, port);
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						dos.writeUTF("SendFolder");
						dos.writeUTF(jtTK.getText());
						if(jcShare.getSelectedIndex()==0) {
							dos.writeUTF(jtTK.getText());
						} else {
							dos.writeUTF("public");
						}
						dos.writeUTF(folderToSend[0].getName());
						dos.writeUTF(folderToSend[0].getPath()); // gửi path file trong máy client
						File arr[] = folderToSend[0].listFiles();
						ArrayList<String> nameFile = new ArrayList<>();
						ArrayList<String> nameDire = new ArrayList<>();
						getChild(arr, 0, "", nameFile, nameDire);
						
						ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		                objectOutput.writeObject(nameDire);
		                
		                dos.writeInt(nameFile.size());
		                
						for (int i = 0; i < nameFile.size(); i++) { // send path, name, data.
							File sendFile = new File(folderToSend[0].getPath() + "\\" + nameFile.get(i));
							dos.writeUTF(nameFile.get(i)); // gửi tên + path con trong thư mục chọn gửi
							FileInputStream fileInputStream = new FileInputStream(sendFile.getAbsolutePath());
							byte[] fileBytes = new byte[(int) sendFile.length()];
							fileInputStream.read(fileBytes);
							dos.writeInt(fileBytes.length);
							dos.write(fileBytes);
						}
						objectOutput.close();
						dos.close();
						socket.close();
						jbSendFolder.setEnabled(false);
						jlFileName.setText("Đã gửi thư mục: '" + folderToSend[0].getName() + "' thành công!");
						LoadData(); // gửi thư mục xong load lại
						folderToSend[0] = null;
					} catch (Exception er) {
						System.out.println("ERROR2: " + er);
					}
				}
			}
		});
		
		jbShare.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog shareFrame = createFrameShare();
				shareFrame.setVisible(true);
			}
		});
		
//		jcShare.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//LoadData(); // chọn cbb rồi load lại
//				if(jcShare.getSelectedIndex()<2) {
//					jbChooseFile.setEnabled(true);
//					jbChooseFolder.setEnabled(true);
//				} else {
//					jbChooseFile.setEnabled(false);
//					jbChooseFolder.setEnabled(false);
//				}
//			}
//		});
		
		jcShare.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				LoadData(); // chọn cbb rồi load lại
				if(jcShare.getSelectedIndex()<2) {
					jbChooseFile.setEnabled(true);
					jbChooseFolder.setEnabled(true);
				} else {
					jbChooseFile.setEnabled(false);
					jbChooseFolder.setEnabled(false);
				}
			}
		});
		
		
		jbSynch.addActionListener(new ActionListener() {
			/* 
			 ************** ĐỒNG BỘ DỮ LIỆU ****************
			 *** client gửi yêu cầu đồng bộ lên server + tên tk yêu cầu
			 *** server truy vấn CSDL để lấy path các thư mục đã up lên của client bên phía client
			 *** client get all path đó, rồi get all path con trong các path đã get lưu vào mảng -
			 * - rồi gửi lại cho server == để server duyệt qua xem có thư mục nào mới để add vào
			 *** server duyệt qua các file đang có, chuyển sang mã MD5 rồi gửi về cho client theo list string
			 *** client nhận được list string, duyệt lại tất cả file của mình theo MD5, cái nào trùng -
			 * - thì bỏ qua, không trùng -> gửi dữ liệu tên + data file cho server
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isServerAlive() == 10) {
					try {
						System.out.print("ĐB");
						Socket socket = new Socket(host, port);
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						dos.writeUTF("Synchronize"); // gửi yêu cầu đồng bộ
						dos.writeUTF(jtTK.getText()); // tài khoản đồng bộ
						Boolean checkBoolean = true; // kiểm tra các mục gốc còn không
													 // còn thì được đồng bộ, không thì nghỉ

						ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
						Object objectReceive = objectInput.readObject();
						ArrayList<String> listPath = (ArrayList<String>)objectReceive;
						
						Object objectReceive278 = objectInput.readObject();
						ArrayList<String> listPath2 = (ArrayList<String>)objectReceive278;
						
						for (int j = 0; j < listPath.size(); j++) {
							try {
								File new_folder = new File(listPath.get(j));
								if (!new_folder.exists()) {
									checkBoolean = false;
								}
							} catch (Exception e2) {
								// TODO: handle exception
							}
						}
						
						for (int j = 0; j < listPath2.size(); j++) {
							try {
								File new_folder = new File(listPath2.get(j));
								if (!new_folder.exists()) {
									checkBoolean = false;
								}
							} catch (Exception e2) {
								// TODO: handle exception
							}
						}
						
						dos.writeBoolean(checkBoolean);
						if(checkBoolean) { // nếu đủ thư mục gốc thì gửi đi
							//System.out.println("ĐB" + listPath.size());
							ArrayList<String> ListnameFile1 = new ArrayList<>();
							ArrayList<String> ListnameFile2 = new ArrayList<>();
							ArrayList<String> ListnameFile3 = new ArrayList<>();
							
							for (int j = 0; j < listPath2.size(); j++) {
								try {
									File new_folder = new File(listPath2.get(j));
									ListnameFile3.add(new_folder.getName());
									ListnameFile1.add(new_folder.getName());
									ListnameFile2.add(new_folder.getPath());
								} catch (Exception e2) {
									// TODO: handle exception
								}
							}
							
							for(int i = 0; i < listPath.size(); i++) {
								File tempFile = new File(listPath.get(i));
								File arr[] = tempFile.listFiles();
								ArrayList<String> nameDire = new ArrayList<>();
								ArrayList<String> nameDire21 = new ArrayList<>(); // phụ để làm tham số thui
								getChild(arr, 0, "", ListnameFile3, nameDire); // lấy path all thư mục
								getChild(arr, 0, tempFile.getName() + "\\", ListnameFile1, nameDire21); // lấy path con file
								getChild(arr, 0, listPath.get(i) + "\\", ListnameFile2, nameDire21); // lấy full path file
								//System.out.println(nameDire.size());
								ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
				                objectOutput.writeObject(nameDire); // gửi list path folder wa cho server so sánh
				                objectOutput.writeObject(ListnameFile3); // gửi list path file wa sv ss
							}
							
							// nhận MD5 từ client
							Object objectReceive2 = objectInput.readObject();
							ArrayList<String> listMD5_1 = (ArrayList<String>)objectReceive2;
//							for(int i = 0; i < ListnameFile1.size(); i++) {
//								System.out.println(i + ": " + ListnameFile1.get(i));
//								System.out.println(ListnameFile2.get(i));
//							}
							
							ArrayList<String> FullPath1 = new ArrayList<>();
							ArrayList<String> SubPath1 = new ArrayList<>();
							for(int i = 0; i < ListnameFile1.size(); i++) {
								String temp_check = getMD5(new File(ListnameFile2.get(i)));
								if(!listMD5_1.contains(temp_check)) {
									System.out.println("!!!!!!!!!!" + i);
									System.out.println(ListnameFile1.get(i)); // in ra path con sai nội dung
									System.out.println(ListnameFile2.get(i)); // full path sai nội dung
									FullPath1.add(ListnameFile2.get(i));
									SubPath1.add(ListnameFile1.get(i));
								}
							}
							
							dos.writeInt(FullPath1.size()); // gửi số lượng file sẽ đồng bộ mới 
							
							for(int i = 0; i < FullPath1.size(); i++) {
								dos.writeUTF(SubPath1.get(i)); // gửi tên file
								File file_temp = new File(FullPath1.get(i));
								FileInputStream fileInputStream = new FileInputStream(file_temp.getAbsolutePath());
								byte[] fileBytes = new byte[(int) file_temp.length()];
								fileInputStream.read(fileBytes);
								dos.writeInt(fileBytes.length);
								dos.write(fileBytes);
							}
						} else {
							System.out.println("Không đồng bộ do thiếu gốc!");
						}
						
					} catch (Exception er) {
						System.out.println("ERROR24: " + er);
					}
				}
			}
		});
		
		// end main
	}

	static int isServerAlive() {
		int trave = -1; // -1 là không kết nối được, 0 là sai tk mk, 1 là sai máy, 10 là thành công
		try {
			Socket socket = new Socket(jtHost.getText(), Integer.parseInt(jtPort.getText()));
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("checkConnect");
			dos.writeUTF(jtTK.getText());
			dos.writeUTF(jtMK.getText());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			trave = dis.readInt();
			host = jtHost.getText();
			port = Integer.parseInt(jtPort.getText());
			dos.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("ERROR3: " + e);
		}
		return trave;
	}

	static MouseListener getMouseListener(String todo) {
		return new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (todo.equals("Click")) {
					//System.out.println(todo + ": " + ((JPanel) e.getSource()).getName());
				} else {
					//System.out.println(todo + ": " + ((JButton) e.getSource()).getName());
					switch (todo) {
					case "Dele": {
						try {
							String name_ = ((JButton) e.getSource()).getName();
							System.out.println(name_);
							//int index = Integer.parseInt(((JButton) e.getSource()).getName());
							Socket socket = new Socket(host, port);
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							dos.writeUTF("Delete");
							dos.writeUTF(jtTK.getText());
							if(jcShare.getSelectedIndex()==0) {
								dos.writeUTF(jtTK.getText());
							} else {
								if(jcShare.getSelectedIndex()==1) {
									dos.writeUTF("public");
								} else {
									dos.writeUTF(jcShare.getSelectedItem().toString());
								}
							}
							dos.writeUTF(name_);
							DataInputStream dis = new DataInputStream(socket.getInputStream());
							System.out.println(dis.readUTF());
							LoadData(); // xóa xong load lại để mất
							dos.close();
							socket.close();
							jcShare.setSelectedIndex(0); // reset lại đối với folder thì bị lỗi nên về 0 cho chắc
						} catch (Exception er) {
							System.out.println("ERROR6 delete: " + er);
						}
						break;
					}
					case "Down": {
						try {
							String name_ = ((JButton) e.getSource()).getName();
							Socket socket = new Socket(host, port);
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			                DataInputStream dis = new DataInputStream(socket.getInputStream());
							dos.writeUTF("Download");
							dos.writeUTF(jtTK.getText());
							if(jcShare.getSelectedIndex()==0) {
								dos.writeUTF(jtTK.getText());
							} else {
								if(jcShare.getSelectedIndex()==1) {
									dos.writeUTF("public");
								} else {
									dos.writeUTF(jcShare.getSelectedItem().toString());
								}
							}
							dos.writeUTF(name_);
							//dos.writeInt(index);
							String namestr = "";
							String request = dis.readUTF();
							switch (request) {
							case "SendFile": {
								String nameFile = dis.readUTF();
								namestr = "tệp " + nameFile;
								int sizeFile = dis.readInt();
								byte[] fileContentBytes = new byte[sizeFile];
								dis.readFully(fileContentBytes, 0, fileContentBytes.length);
								if(jcShare.getSelectedIndex() == 1) {
									nameFile = "public_" + nameFile;
								}
								if(jcShare.getSelectedIndex() > 1) {
									nameFile = jcShare.getSelectedItem().toString() + "_" + nameFile;
								}
								File fileToDownload = new File(pathRootClient + "\\" + nameFile);
								FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
			                    fileOutputStream.write(fileContentBytes);
			                    fileOutputStream.close();
			                    System.out.println(dis.readUTF());
								break;
							}
							case "SendFolder": {
								String nameFolder = dis.readUTF();
								namestr = "thư mục " + nameFolder;
								if(jcShare.getSelectedIndex() == 1) {
									nameFolder = "public_" + nameFolder;
								}
								if(jcShare.getSelectedIndex() > 1) {
									nameFolder = jcShare.getSelectedItem().toString() + "_" + nameFolder;
								}
								File newfolder = new File(pathRootClient + "\\" + nameFolder);
								if (!newfolder.exists()){
									newfolder.mkdirs();
						        }
								ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
								Object objectReceive = objectInput.readObject();
								ArrayList<String> NameFolderList = (ArrayList<String>)objectReceive;
								int amountFileReceive = dis.readInt();
								//System.out.println("sẽ nhận thêm: " + amountFileReceive);
								for(int i = 0; i < NameFolderList.size(); i++) {
									File new_folder = new File(newfolder + "\\" + NameFolderList.get(i));
									if (!new_folder.exists()){
										new_folder.mkdirs();
							        }
								}
								
								for(int i = 0; i < amountFileReceive; i++) {
									String nameFile = dis.readUTF();
									int sizeFile = dis.readInt();
									byte[] fileContentBytes = new byte[sizeFile];
									dis.readFully(fileContentBytes, 0, fileContentBytes.length);
									File fileToDownload = new File(newfolder + "\\" + nameFile);
									FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
				                    fileOutputStream.write(fileContentBytes);
				                    fileOutputStream.close();
								}
								System.out.println(dis.readUTF());
								break;
							}
							default:
								break;
							}
							jlFileName.setText("Đã tải " + namestr + " thành công!");
							dos.close();
							socket.close();
						} catch (Exception er) {
							System.out.println("ERROR7: " + er);
						}
						break;
					}
					
					case "OpenShare":{
						try {
							String name = ((JButton) e.getSource()).getName();
							Socket socket = new Socket(host, port);
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							dos.writeUTF("OpenShare");
							dos.writeUTF(jtTK.getText()); // gửi định danh người share
							dos.writeUTF(name);           // gửi định danh người được nhận dữ liệu
							dos.close();
							socket.close();
							LoadListUS();
						} catch (Exception er) {
							System.out.println("ERROR8: " + er);
						}
						
						//System.out.println("Mở chia sẻ: " + ((JButton) e.getSource()).getName());
				        break;
					}
					
					case "CloseShare":{
						try {
							String name = ((JButton) e.getSource()).getName();
							Socket socket = new Socket(host, port);
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							dos.writeUTF("CloseShare");
							dos.writeUTF(jtTK.getText()); // gửi định danh người ngắt share
							dos.writeUTF(name);           // gửi định danh người bị ngắt dữ liệu
							dos.close();
							socket.close();
							LoadListUS();
						} catch (Exception er) {
							System.out.println("ERROR9: " + er);
						}
						
						//System.out.println("Đóng Chia sẻ: " + ((JButton) e.getSource()).getName());
				        break;
					}
					
					case "Copy":{
						try {
							Socket socket = new Socket(host, port);
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							//int index = Integer.parseInt(((JButton) e.getSource()).getName());
							String name_ = ((JButton) e.getSource()).getName();
							dos.writeUTF("Copy");
							dos.writeUTF(jtTK.getText()); // gửi định danh người request
							if(jcShare.getSelectedIndex() == 1) { //gửi địa chỉ muốn copy
								dos.writeUTF("public");
							}
							if(jcShare.getSelectedIndex() > 1) {
								dos.writeUTF(jcShare.getSelectedItem().toString());
							}
							dos.writeUTF(name_);
							dos.close();  // gửi xong, đóng kết nối reload lại dữ liệu
							socket.close();
							LoadData(); // copy xong gửi lại
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						break;
					}
					
					default:
						throw new IllegalArgumentException("Unexpected value: " + todo);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {}
		};
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
	
	static void LoadData() {
		try {
			Socket socket = new Socket(host, port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("GetData");
			if(jcShare.getSelectedIndex()==0) {
				dos.writeUTF(jtTK.getText());
			} else {
				if(jcShare.getSelectedIndex()==1) {
					dos.writeUTF("public");
				} else {
					dos.writeUTF(jcShare.getSelectedItem().toString());
				}
			}
			ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
			Object objectReceive = objectInput.readObject();
			ArrayList<String> NameFileList = (ArrayList<String>) objectReceive;

			jpShowFileServer.removeAll();
			jpShowFileServer.revalidate();
			jpShowFileServer.repaint(); // xóa và cập nhật lại céi mới trong danh sách
			
			
			for (int i = 0; i < NameFileList.size(); i++) {
				//System.out.println(getNameOnly(NameFileList.get(i)));
				JPanel jpFileRow = new JPanel();
				jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));
				String nameRow = "";
				if (NameFileList.get(i).length() < 44) {
					nameRow = NameFileList.get(i) + "    ";
				} else {
					nameRow = NameFileList.get(i).substring(0, 41) + "...    ";
				}

				JLabel jlFileName = new JLabel("   " + nameRow);
				jlFileName.setFont(new Font("Roboto", Font.ITALIC, 21));
				jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));
				jpFileRow.setAlignmentX(Component.LEFT_ALIGNMENT);
				jpFileRow.setName(getNameOnly(NameFileList.get(i)));

				JButton jbDown = new JButton("Tải");
				jbDown.setPreferredSize(new Dimension(100, 24));
				jbDown.setFont(new Font("Arial", Font.BOLD, 16));
				jbDown.setName(getNameOnly(NameFileList.get(i)));

				JButton jbDele = new JButton("Xóa");
				jbDele.setPreferredSize(new Dimension(100, 24));
				jbDele.setFont(new Font("Arial", Font.BOLD, 16));
				jbDele.setName(getNameOnly(NameFileList.get(i)));
				
				JButton jbCopy = new JButton("Sao");
				jbCopy.setPreferredSize(new Dimension(100, 24));
				jbCopy.setFont(new Font("Arial", Font.BOLD, 16));
				jbCopy.setName(getNameOnly(NameFileList.get(i)));
				
				jpFileRow.addMouseListener(getMouseListener("Click"));
				jbDown.addMouseListener(getMouseListener("Down"));
				jbDele.addMouseListener(getMouseListener("Dele"));
				jbCopy.addMouseListener(getMouseListener("Copy"));
				// Add everything.
				jpFileRow.add(jlFileName);
				if(jcShare.getSelectedIndex()==0) { // của bản thân thì mở tải về - xóa
					jpFileRow.add(jbDown);
					jpFileRow.add(jbDele);
				} else {
					if(jcShare.getSelectedIndex()==1) { // của chung thì tải về - xóa - sao chép về bản thân
						jpFileRow.add(jbCopy);
						jpFileRow.add(jbDown);
						jpFileRow.add(jbDele);
					} else { // của người khác thì tải hoặc sao thôi - không xóa được
						jpFileRow.add(jbCopy);
						jpFileRow.add(jbDown);
					}
				}
				jpShowFileServer.add(jpFileRow);
				// System.out.println(NameFileList.get(i));
			}
			
			jFrame.validate();
			dos.close();
			socket.close();
		} catch (Exception er) {
			System.out.println("ERROR10: " + er);
		}
	}
	
	static void LoadDataCBB() {
		try {
			Socket socket = new Socket(host, port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("Connect");
			dos.writeUTF(jtTK.getText());
			ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
			Object objectReceive = objectInput.readObject();
			ArrayList<String> NameUserShare = (ArrayList<String>) objectReceive;

			jpShowFileServer.removeAll();
			jpShowFileServer.revalidate();
			jpShowFileServer.repaint(); // xóa và cập nhật lại céi mới trong danh sách
			

			jcShare.removeAllItems();
			jcShare.revalidate();
			jcShare.repaint();
			
			jcShare.addItem("Mặc định");
			jcShare.addItem("Công Cộng");
			for(int i = 0; i < NameUserShare.size(); i++) {
				jcShare.addItem(NameUserShare.get(i));
			}
			
			jFrame.validate();
			dos.close();
			socket.close();
		} catch (Exception er) {
			er.printStackTrace();
		}
	}
	
	public static JDialog createFrameShare() {
		JDialog jFrame = new JDialog();
        jFrame.setSize(525, 525);
		jFrame.setLocationRelativeTo(null);
		jFrame.setModal(true);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        JLabel jlTitle = new JLabel("Chia sẻ dữ liệu với người khác:");
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));

        jpListUser = new JPanel();
        jpListUser.setBorder(new EmptyBorder(10, 0, 10, 0));
        jpListUser.setLayout(new BoxLayout(jpListUser, BoxLayout.Y_AXIS));
		JScrollPane js = new JScrollPane(jpListUser);
		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		js.setPreferredSize(new Dimension(10, 400));
        
        jPanel.add(jlTitle);
        jPanel.add(js);
        jFrame.add(jPanel);
        
        
        jpListUser.removeAll();
        jpListUser.revalidate();
        jpListUser.repaint();
		int status_ = isServerAlive();
		if (status_ == 10) {
			LoadListUS();
		}
        return jFrame;
    }
	
	
	static void LoadListUS() {
		try {
			Socket socket = new Socket(host, port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("ListUSShare");
			dos.writeUTF(jtTK.getText());
			ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
			Object objectReceive = objectInput.readObject();
			Object objectReceive2 = objectInput.readObject();
			ArrayList<String> ListUSShare = (ArrayList<String>) objectReceive;
			ArrayList<Boolean> ListCheck = (ArrayList<Boolean>) objectReceive2;

			jpListUser.removeAll();
			jpListUser.revalidate();
			jpListUser.repaint(); // xóa và cập nhật lại céi mới
			
			for (int i = 0; i < ListUSShare.size(); i++) {
				JPanel jpFileRow = new JPanel();
				jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));

				JLabel jlFileName = new JLabel("        " + ListUSShare.get(i) + "                  ");
				jlFileName.setFont(new Font("Roboto", Font.ITALIC, 21));
				jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));
				jpFileRow.setAlignmentX(Component.LEFT_ALIGNMENT);
				jpFileRow.setName("" + ListUSShare.get(i));

				JButton jbOpen = new JButton("Mở CS");
				jbOpen.setPreferredSize(new Dimension(100, 24));
				jbOpen.setFont(new Font("Arial", Font.BOLD, 16));
				jbOpen.setName("" + ListUSShare.get(i));

				JButton jbClose = new JButton("Đóng CS");
				jbClose.setPreferredSize(new Dimension(100, 24));
				jbClose.setFont(new Font("Arial", Font.BOLD, 16));
				jbClose.setName("" + ListUSShare.get(i));
				jpFileRow.addMouseListener(getMouseListener("Click"));
				jbOpen.addMouseListener(getMouseListener("OpenShare"));
				jbClose.addMouseListener(getMouseListener("CloseShare"));
				// Add everything.
				
				jpFileRow.add(jlFileName);
				jpFileRow.add(jbOpen);
				jpFileRow.add(jbClose);
				
				if(ListCheck.get(i)) {
					jbOpen.setEnabled(false);
					jbClose.setEnabled(true);
				} else {
					jbOpen.setEnabled(true);
					jbClose.setEnabled(false);
				}
				
				if(!jtTK.getText().equals(ListUSShare.get(i))) {
					jpListUser.add(jpFileRow);
				}
			}
			dos.close();
			socket.close();
		} catch (Exception er) {
			System.out.println("ERROR1221: " + er);
		}
	}
	

	static String getNameOnly(String s) { // input "File: hello.cpp" -> output "hello.cpp"
		int index = 0;
		for(int i = 0; i < s.length() - 1; i++) {
			if(s.charAt(i) == ':' && s.charAt(i+1) == ' ') {
				index = i;
				break;
			}
		}
		return s.substring(index+2);
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
}
