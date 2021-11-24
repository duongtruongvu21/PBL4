package PBL4;


public class HoatDong {
	private String ThoiGian;
	private String TaiKhoan;
	private String HanhVi;
	private String DoiTuong;
	
	public String getThoiGian() {
		return ThoiGian;
	}
	public void setThoiGian(String thoiGian) {
		ThoiGian = thoiGian;
	}
	public String getTaiKhoan() {
		return TaiKhoan;
	}
	public void setTaiKhoan(String taiKhoan) {
		TaiKhoan = taiKhoan;
	}
	public String getHanhVi() {
		return HanhVi;
	}
	public void setHanhVi(String hanhVi) {
		HanhVi = hanhVi;
	}
	public String getDoiTuong() {
		return DoiTuong;
	}
	public void setDoiTuong(String doiTuong) {
		DoiTuong = doiTuong;
	}
	
	public String getString() {
		return "   " + ThoiGian + "   " + TaiKhoan + "   " + HanhVi + "  \t\t  " + DoiTuong;
	}
}