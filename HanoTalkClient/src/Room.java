import java.util.Vector;

//ctrl+w �ϸ� ����ȭ��
//�� ���ǿ� ������ ���� Ŭ����
public class Room {
		private String roomid;
		private Vector<String> list;
		
		public Room(String id, Vector list) {
			this.roomid = id;
			this.list = list;
		}
	}