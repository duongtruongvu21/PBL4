package ModelDAU;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import ModelBEAN.HoatDong;
import ModelBEAN.PhongBan;

public class BO {
	
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
				Boolean tt = rs.getBoolean("TrangThai");
				if (tk.equals(tkk) && mk.equals(mkk)) {
					if (tk.equals(tkk) && ip.equals(ipp)) {
						if(tt) {
							trave = 10;
						} else {
							trave = 3;
						}
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
	
	public ArrayList<PhongBan> getUsers() {
		ArrayList<PhongBan> trave = new ArrayList<PhongBan>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("SELECT * FROM `phongban`");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PhongBan temp = new PhongBan();
				temp.setTaiKhoan(rs.getString("TaiKhoan"));
				temp.setMatKhau(rs.getString("MatKhau"));
				temp.setIP(rs.getString("IP"));
				temp.setTrangThai(rs.getBoolean("TrangThai"));
				
				trave.add(temp);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR175: " + e);
		}
		return trave;
	}
	
	public void UBanUser(String tk, Boolean stt) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("update phongban set TrangThai = ? WHERE TaiKhoan = ?");
			ps.setBoolean(1, stt);
			ps.setString(2, tk);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("ERROR910: " + e);
		}
	}
	
	public Boolean addNewUser(String tk, String pass, String ip) {
		Boolean trave = false;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/pbl4";
			Connection con = DriverManager.getConnection(url, "root", "");
			PreparedStatement ps;
			ps = con.prepareStatement("INSERT INTO PhongBan(TaiKhoan, MatKhau, IP) VALUES (?, ?, ?);");
			ps.setString(1, tk);
			ps.setString(2, pass);
			ps.setString(3, ip);
			ps.executeUpdate();
			ps.close();
			trave = true;
		} catch (Exception e) {
			System.out.println("ERROR7: " + e);
		}
		return trave;
	}
}