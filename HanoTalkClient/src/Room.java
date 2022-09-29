import java.util.Vector;

//ctrl+w 하면 동기화됨
//룸 정의와 관리를 위한 클래스
public class Room {
		private String roomid;
		private Vector<String> list;
		
		public Room(String id, Vector list) {
			this.roomid = id;
			this.list = list;
		}
	}