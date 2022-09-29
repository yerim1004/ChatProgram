// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:�α���, 101:�α��� ����, 200:ä�ø޽���, 300:Image, 301:�̸�Ƽ��
	public String UserName;
	public String data;
	public ImageIcon img;
	
	public ArrayList<String> friendlist;//ģ�� ���
	public ArrayList<String> chatlist = null; //ä�ù� ������ ���
	public Room room = null;

	public ChatMsg(String UserName, String code, String msg) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String UserName) {
		this.UserName = UserName;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
	public void setRoom(Room r) {
		this.room = r;
	}
}