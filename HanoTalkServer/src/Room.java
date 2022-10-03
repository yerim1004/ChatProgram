import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

//ctrl+w 하면 동기화됨
//룸 정의와 관리를 위한 클래스
public class Room implements Serializable{
		
	private static final long serialVersionUID = 1L;
		public String roomid;
		public ArrayList<String> list;
		
		public Room(String id, ArrayList list) {
			this.roomid = id;
			this.list = list;
		}
}
