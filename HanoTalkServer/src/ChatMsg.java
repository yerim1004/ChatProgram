
// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;


class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:�α���, 400:�α׾ƿ�, 200:ä�ø޽���, 300:Image, 500: Mouse Event
	public String UserName;
	public String data;
	public ImageIcon img;
	public MouseEvent mouse_e;
	public int pen_size; // pen size

	public int start = 10;
	public ArrayList<ChatMember> friendlist = null;
	public ArrayList<ChatMember> chatlist = null; //ä�ù� ������ ���
	public Room room = null;
	
	public ChatMsg(String UserName, String code, String msg) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
}