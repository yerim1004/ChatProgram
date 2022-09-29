// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:로그인, 101:로그인 성공, 200:채팅메시지, 300:Image, 301:이모티콘
	public String UserName;
	public String data;
	public ImageIcon img;
	
	public ArrayList<String> friendlist;//친구 목록
	public ArrayList<String> chatlist = null; //채팅방 참가자 목록
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