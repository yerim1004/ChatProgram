import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

//ctrl+w �ϸ� ����ȭ��
//�� ���ǿ� ������ ���� Ŭ����
public class Room implements Serializable{
		
	private static final long serialVersionUID = 1L;
		public String roomid;
		public ArrayList<String> list;
		
		public Room(String id, ArrayList list) {
			this.roomid = id;
			this.list = list;
		}
}
