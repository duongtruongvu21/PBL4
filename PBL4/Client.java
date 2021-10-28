package PBL4;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Client {
	static JFrame jFrame;
	static JPanel jpConnect, jpButton, jpShowFileServer, jpListUser;
	static JTextField jtHost, jtPort, jtTK, jtMK;
	static JLabel jlTitle, jlFileName, jlDataServer;
	static JComboBox jcShare;
	static String host = ""; static int port = 0;
	static String pathRootClient = System.getProperty("user.home") + "\\Downloads\\PBL4_Client";
	static File folderRootClient = new File(pathRootClient);
	
	public static void main(String[] args) {
		
		if (!folderRootClient.exists()){
			folderRootClient.mkdirs();
        }
		
		final File[] fileToSend = new File[1];
		final File[] folderToSend = new File[1];
		JButton jbConnect, jbChooseFile, jbChooseFolder, jbSendFile, jbSendFolder, jbShare, jbSynch;
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

		jFrame.add(jlTitle);
		jFrame.add(jpConnect);
		jFrame.add(jlFileName);
		jFrame.add(jpButton);
		jFrame.add(jlDataServer);
		jFrame.add(jScrollPane);
		jFrame.setVisible(true);

		jbConnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(jtHost.getText().equals("") || jtPort.getText().equals("") || jtTK.getText().equals("") || jtMK.getText().equals("")) {
					jlDataServer.setText("Nhập vào đủ thông tin!!!");
				} else {
					jpShowFileServer.removeAll();
					jpShowFileServer.revalidate();
					jpShowFileServer.repaint();
					int status_ = isServerAlive();
					if (status_ == 1) {
						LoadDataCBB();
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
				}
			}
		});

		jbChooseFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				jlFileName.setText("Đang gửi đi, có thể mất vài phút nếu đường truyền bận!");
				if (isServerAlive() == 1) {
					try {
						Socket socket = new Socket(host, port);
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						dos.writeUTF("SendFile");
						if(jcShare.getSelectedIndex()==0) {
							dos.writeUTF(jtTK.getText());
						} else {
							dos.writeUTF("public");
						}
						dos.writeUTF(fileToSend[0].getName());
						FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
						byte[] fileBytes = new byte[(int) fileToSend[0].length()];
						fileInputStream.read(fileBytes);
						dos.writeInt(fileBytes.length);
						dos.write(fileBytes);
						dos.close();
						socket.close();
						jbSendFile.setEnabled(false);
						jlFileName.setText("Đã gửi tệp: '" + fileToSend[0].getName() + "' thành công!");
						LoadData();
						fileToSend[0] = null;
					} catch (Exception er) {
						// TODO: handle exception
					}
				}
			}
		});

		jbChooseFolder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				jlFileName.setText("Đang gửi đi, có thể mất vài phút nếu đường truyền bận!");
				if (isServerAlive() == 1) {
					try {
						Socket socket = new Socket(host, port);
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						dos.writeUTF("SendFolder");
						if(jcShare.getSelectedIndex()==0) {
							dos.writeUTF(jtTK.getText());
						} else {
							dos.writeUTF("public");
						}
						dos.writeUTF(folderToSend[0].getName());
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
						LoadData();
						folderToSend[0] = null;
					} catch (Exception er) {
						// TODO: handle exception
					}
				}
			}
		});
		
		jbShare.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JDialog shareFrame = createFrameShare();
				shareFrame.setVisible(true);
			}
		});
		
		jcShare.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				LoadData();
				if(jcShare.getSelectedIndex()<2) {
					jbChooseFile.setEnabled(true);
					jbChooseFolder.setEnabled(true);
				} else {
					jbChooseFile.setEnabled(false);
					jbChooseFolder.setEnabled(false);
				}
			}
		});
		
		
		// end main
	}

	static int isServerAlive() {
		int trave = -1; // -1 là không kết nối được, 0 là sai tk mk, 1 là thành công
		try {
			Socket socket = new Socket(jtHost.getText(), Integer.parseInt(jtPort.getText()));
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("checkConnect");
			dos.writeUTF(jtTK.getText());
			dos.writeUTF(jtMK.getText());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			if(dis.readBoolean()) {
				trave = 1;
			} else {
				trave = 0;
			}
			host = jtHost.getText();
			port = Integer.parseInt(jtPort.getText());
			dos.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return trave;
	}

	static MouseListener getMouseListener(String todo) {
		return new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (todo.equals("Click")) {
					//System.out.println(todo + ": " + ((JPanel) e.getSource()).getName());
				} else {
					//System.out.println(todo + ": " + ((JButton) e.getSource()).getName());
					switch (todo) {
					case "Dele": {
						try {
							int index = Integer.parseInt(((JButton) e.getSource()).getName());
							Socket socket = new Socket(host, port);
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							dos.writeUTF("Delete");
							if(jcShare.getSelectedIndex()==0) {
								dos.writeUTF(jtTK.getText());
							} else {
								if(jcShare.getSelectedIndex()==1) {
									dos.writeUTF("public");
								} else {
									dos.writeUTF(jcShare.getSelectedItem().toString());
								}
							}
							dos.writeInt(index);
							dos.close();
							socket.close();
							jcShare.setSelectedIndex(0); // reset lại đối với folder thì bị lỗi nên về 0 cho chắc
						} catch (Exception er) {
							// TODO: handle exception
						}
						break;
					}
					case "Down": {
						try {
							int index = Integer.parseInt(((JButton) e.getSource()).getName());
							Socket socket = new Socket(host, port);
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			                DataInputStream dis = new DataInputStream(socket.getInputStream());
							dos.writeUTF("Download");
							if(jcShare.getSelectedIndex()==0) {
								dos.writeUTF(jtTK.getText());
							} else {
								if(jcShare.getSelectedIndex()==1) {
									dos.writeUTF("public");
								} else {
									dos.writeUTF(jcShare.getSelectedItem().toString());
								}
							}
							dos.writeInt(index);
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
								break;
							}
							default:
								break;
							}
							jlFileName.setText("Đã tải " + namestr + " thành công!");
							dos.close();
							socket.close();
							LoadData();
						} catch (Exception er) {
							// TODO: handle exception
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
							// TODO: handle exception
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
							// TODO: handle exception
						}
						
						//System.out.println("Đóng Chia sẻ: " + ((JButton) e.getSource()).getName());
				        break;
					}
					
					case "Copy":{
						try {
							Socket socket = new Socket(host, port);
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							int index = Integer.parseInt(((JButton) e.getSource()).getName());
							dos.writeUTF("Copy");
							dos.writeUTF(jtTK.getText()); // gửi định danh người request
							if(jcShare.getSelectedIndex() == 1) { //gửi địa chỉ muốn copy
								dos.writeUTF("public");
							}
							if(jcShare.getSelectedIndex() > 1) {
								dos.writeUTF(jcShare.getSelectedItem().toString());
							}
							dos.writeInt(index); // gửi index file muốn copy
							dos.close();  // gửi xong, đóng kết nối reload lại dữ liệu
							socket.close();
							LoadData();
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
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
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
				jpFileRow.setName("" + i);

				JButton jbDown = new JButton("Tải");
				jbDown.setPreferredSize(new Dimension(100, 24));
				jbDown.setFont(new Font("Arial", Font.BOLD, 16));
				jbDown.setName("" + i);

				JButton jbDele = new JButton("Xóa");
				jbDele.setPreferredSize(new Dimension(100, 24));
				jbDele.setFont(new Font("Arial", Font.BOLD, 16));
				jbDele.setName("" + i);
				
				JButton jbCopy = new JButton("Sao");
				jbCopy.setPreferredSize(new Dimension(100, 24));
				jbCopy.setFont(new Font("Arial", Font.BOLD, 16));
				jbCopy.setName("" + i);
				
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
			// TODO: handle exception
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
		if (status_ == 1) {
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
			er.printStackTrace();
		}
	}
	
}
