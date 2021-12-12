package ModelBO;
import java.util.ArrayList;

import ModelBEAN.HoatDong;
import ModelBEAN.PhongBan;
import ModelDAU.DAO;

public class BO {
	DAO dao = new DAO();
	
	public void addRecordBO(String tk, String doSt, String obj) {
		dao.addRecordDAO(tk, doSt, obj);
	}
	
	public int checkLoginBO(String tk, String mk, String ip) {
		return dao.checkLoginDAO(tk, mk, ip);
	}

	public ArrayList<String> getListUSSharedBO(String tk) {
		return dao.getListUSSharedDAO(tk);
	}

	public ArrayList<String> getListUSSharedByMeBO(String tk) { // lấy danh sách những người mình chia sẻ dữ liệu ra
		return dao.getListUSSharedByMeDAO(tk);
	}

	public void AddShareBO(String tk1, String tk2) {
		dao.AddShareDAO(tk1, tk2);
	}

	public void DelShareBO(String tk1, String tk2) {
		dao.DelShareDAO(tk1, tk2);
	}

	public ArrayList<String> getListShareBO(String tk) { // lấy danh sách những người đã chia sẻ với mình
		return dao.getListShareDAO(tk);
	}
	
	public void addNewDataBO(String tk, String tenData, String pathData, String type) {
		dao.addNewDataDAO(tk, tenData, pathData, type);
	}
	
	public void delDataBO(String tk, String tenData) {
		dao.delDataDAO(tk, tenData);
	}
	
	public ArrayList<String> getListPathFolderDataBO(String tk) {
		return dao.getListPathFolderDataDAO(tk);
	}
	
	public ArrayList<String> getListPathFileDataBO(String tk) {
		return dao.getListPathFileDataDAO(tk);
	}
	
	public ArrayList<String> getListNamePathFolderDataBO(String tk) {
		// trả về tên thư mục của client gửi qua
		return dao.getListNamePathFolderDataDAO(tk);
	}
	
	public ArrayList<String> getListNamePathFileDataBO(String tk) {
		// trả về tên thư mục của client gửi qua
		return dao.getListNamePathFileDataDAO(tk);
	}

	public ArrayList<HoatDong> get100ActivityNewestBO() {
		// trả về 100 hoạt động gần nhất
		return dao.get100ActivityNewestDAO();
	}
	
	public ArrayList<HoatDong> getSelectedActivityBO(String tk, String hv, String staTime, String endTime) {
		// trả về 100 hoạt động theo form
		return dao.getSelectedActivityDAO(tk, hv, staTime, endTime);
	}
	
	public ArrayList<PhongBan> getUsersBO() {
		return dao.getUsersDAO();
	}
	
	public void UBanUserBO(String tk, Boolean stt) {
		dao.UBanUserDAO(tk, stt);
	}
	
	public Boolean addNewUserBO(String tk, String pass, String ip) {
		return dao.addNewUserDAO(tk, pass, ip);
	}
}
