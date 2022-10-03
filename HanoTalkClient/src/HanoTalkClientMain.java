//mainȭ���� ģ�� ����Ʈ ȭ��
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import static javax.swing.JOptionPane.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class HanoTalkClientMain extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel checkPane;
	public String IpAddress;
	public String PortNumber;
	
	private static JPanel myPanel;
	private JPanel sidePanel;
	private static JPanel menuPanel;
	private static JScrollPane mainPane;
	private static JScrollPane chatPane;
	
	public JButton friendBtn;
	public JButton chatBtn;
	public JButton myBtn;
	public JButton makeBtn;
	
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	private Socket socket; // �������
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private static ArrayList<ChatMember> userlist;//chatobject�κ��� �������Ʈ ���� ����
	private static ArrayList<String> stlist;
	private static ArrayList<JButton> fbtn = new ArrayList<>();//ģ�� ��� �����ص� ����Ʈ
	private static ArrayList<JLabel> fname = new ArrayList<>();
	private static ArrayList<JLabel> fstat = new ArrayList<>();
	
	public HanoTalkClientMain mainView;
	public String UserName;
	
	private ImageIcon greenicon = new ImageIcon("Image_Folder/green.JPG");
	private ImageIcon grayicon = new ImageIcon("Image_Folder/gray.jpg");
	private Vector<HanoTalkClientView> RoomList = new Vector<>();//�� ���� ����Ʈ
	private HanoTalkClientView room;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HanoTalkClientMain frame = new HanoTalkClientMain(String ip_addr, String port_no);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public HanoTalkClientMain(String username, String ip_addr, String port_no) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 275, 350);
		contentPane = new JPanel();
		//contentPane.setBackground(new Color(255, 182, 193));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		menuPanel = new JPanel();
		menuPanel.setBounds(0, 0, 259, 35);
		menuPanel.setLayout(null);
		contentPane.add(menuPanel);
		
		sidePanel = new JPanel();
		sidePanel.setBounds(0, 35, 60, 275);
		sidePanel.setBackground(new Color(255, 255, 255));
		contentPane.add(sidePanel);
		
		mainPane = new JScrollPane();
		mainPane.setLayout(null);
		mainPane.setBounds(59, 85, 201, 227);
		mainPane.setBackground(Color.WHITE);
		contentPane.add(mainPane);
		
		myPanel = new JPanel();
		myPanel.setLayout(null);
		myPanel.setBounds(60, 35, 199, 50);
		myPanel.setBackground(new Color(240, 180, 190));
		contentPane.add(myPanel);
		
		chatPane = new JScrollPane();
		chatPane.setBounds(60, 0, 199, 311);
		chatPane.setBackground(new Color(240, 180, 190));
		chatPane.setVisible(false);
		contentPane.add(chatPane);
		
		myBtn = new JButton(greenicon);
		myBtn.setBounds(5, 5, 40, 40);
		myPanel.add(myBtn);
		
		makeBtn = new JButton("����");
		makeBtn.setBounds(220, 2, 30, 30);
		makeBtn.addActionListener(new openCheckListAction());
		menuPanel.add(makeBtn);
		
		JButton friendBtn = new JButton("Ī��");
		friendBtn.setPreferredSize(new Dimension(100, 30));
		friendBtn.addActionListener(new friendBtnAction());
		sidePanel.add(friendBtn);
		
		JButton chatBtn = new JButton("ä�ù�");
		chatBtn.setPreferredSize(new Dimension(100, 30));
		chatBtn.addActionListener(new chatBtnAction());
		sidePanel.add(chatBtn);
		
		
		setVisible(true);
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			
			mainView = this;
			UserName = username;
			IpAddress = ip_addr;
			PortNumber = port_no;
			
			JLabel myName = new JLabel(UserName);
			myName.setBounds(50, 10, 60, 25);
			myPanel.add(myName);
			
			ChatMsg obcm = new ChatMsg(UserName, "110", "Online");
			SendObject(obcm);
			
			ListenNetwork net = new ListenNetwork();
			net.start();
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "connect error");
		}
	}
	
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					userlist = new ArrayList<ChatMember>();
					//stlist = new ArrayList<>();
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
						
					}else
						continue;
					switch(cm.code) {
					case "111"://��������Ʈ��ŭ ��ư ����, ChatObject
						userlist = cm.friendlist;
						//stlist = cm.chatlist;
						for(int i=0; i<userlist.size(); i++) {
							String name = userlist.get(i).mName.getText();
							System.out.println(name);
							if(name.matches(UserName)) {//�ڱ��ڽ��� ��ư�� ������� ����
								//JOptionPane.showMessageDialog(null, name);
								userlist.remove(String.valueOf(name));
							}
						}//���� ������ �̹����� �迭�� �޾Ƽ� �ѱ��______________________________________________
						/*for(int i=0; i<stlist.size(); i++) {
							String name = stlist.get(i);
							if(name.matches(UserName)) {//�ڱ��ڽ��� ��ư�� ������� ����
								//JOptionPane.showMessageDialog(null, name);
								userlist.remove(String.valueOf(name));
							}
						}*/
						addButtons(userlist);
						//addLabel(userlist);
						break;
					case "151"://�� ���� ���� �ڵ�, �������Ʈ �ޱ�
						openChat(cm.room.roomid);
						break;
					case "200": // chat message
						room = SearchRoom(cm.room.roomid);
						if (cm.UserName.equals(UserName))
							room.AppendTextR(msg); // �� �޼����� ������
						else
							room.AppendText(msg);
						break;
					case "300": // Image ÷��
						if (cm.UserName.equals(UserName))
							//AppendTextR("[" + cm.UserName + "]");
						//else
							//AppendText("[" + cm.UserName + "]");
						//AppendImage(cm.img);
						break;
					case "500": // Mouse Event ����
						//DoMouseEvent(cm);
						break;
					}
					
				} catch (IOException e) {
					
					try {
//					
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
				repaint();
			}
		}
	}
	public HanoTalkClientView SearchRoom(String roomid) {
		HanoTalkClientView cur_room = null;
		for(int i = 0; i<RoomList.size(); i++) {
			cur_room = (HanoTalkClientView) RoomList.elementAt(i);
			if(roomid.matches(cur_room.RoomNumber)) {
				break;
			}
		}
		return cur_room;
	}
//	class ChatMember {
	//	public JButton profile = new JButton();
		//public JLabel mName = new JLabel();
		//public JLabel stat = new JLabel();
	//}
	public void addButtons(ArrayList<ChatMember> list){//ArrayList<ChatMsg.ChatMember> list) {
		//ChatMsg.ChatMember b = new ChatMsg.ChatMember();
		JButton addbtn = new JButton();
		JLabel addname = new JLabel();
		JLabel addstat= new JLabel();
		JPanel[] addpanel = new JPanel[list.size()];
		greenicon = imageSetSize(greenicon, 30, 30);
		grayicon = imageSetSize(grayicon, 40, 40);
		//JOptionPane.showMessageDialog(null, list.size());
		mainPane.removeAll();
		
		//��ư ����Ʈ�� �������� ����Ʈ�� �̸��� ������ ��������
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(fname.size() == 0)
					break;
				if(list.get(j).mName.getText().matches((fname.get(i)).getText())) {//����Ʈ�� �ְ� ��ư����Ʈ�� �����ϸ�
					fstat.get(i).setIcon(greenicon);
					break;
				}else if(!list.get(i).mName.getText().matches((fname.get(j)).getText()) 
						&& j == list.size() - 1){//����Ʈ�� �ִ� �̸��� ��ư����Ʈ�� �������� ������
					fstat.get(i).setIcon(greenicon);
				}
			}
		}
		//����Ʈ�� �������� ��ư����Ʈ�� ������ ���� �����
		if(fname.size() == 0) {
			for(int i = 0; i < 3; i++) {
					//System.out.println(list.get(i).mName.getText());
					addstat.setIcon(greenicon);
					addname.setText(list.get(i).mName.getText());
					addbtn.setIcon(grayicon);
					addbtn.setBorderPainted(false);
					addbtn.setContentAreaFilled(false);
					addbtn.setBounds(10, 5 + i * 50, 40, 40);
					addname.setBounds(60, 5 + i * 50, 40, 40);
					addstat.setBounds(180, 20 + i * 50, 10, 10);
					addbtn.setVisible(true);
					addname.setVisible(true);
					addstat.setVisible(true);
					
					fbtn.add(addbtn);
					fname.add(addname);
					fstat.add(addstat);
						
					//System.out.println("��ư" + fname.get(i).getText());
					
				}
			}else if(fname.size() != 0){
				for(int i = 0; i < 3; i++) {
					for(int j = 0; j < 3; j++) {
						
						if(list.get(i).mName.getText().matches((fname.get(j)).getText())) {//����Ʈ�� �ְ� ��ư����Ʈ�� �����ϸ�
							break;
						}else if(!list.get(i).mName.getText().matches((fname.get(j)).getText()) 
								&& j == fname.size() - 1){//����Ʈ�� �ִ� �̸��� ��ư����Ʈ�� �������� ������
							addstat.setIcon(greenicon);
							addname.setText(list.get(i).mName.getText());
							addbtn.setBorderPainted(false);
							addbtn.setContentAreaFilled(false);
							addbtn.setBounds(10, 5 + i * 50, 40, 40);
							addname.setBounds(60, 5 + i * 50, 40, 40);
							addstat.setBounds(180, 20 + i * 50, 10, 10);
							addbtn.setVisible(true);
							addname.setVisible(true);
							addstat.setVisible(true);
							
							fbtn.add(addbtn);
							fname.add(addname);
							fstat.add(addstat);
							
							System.out.println("��ư" + fbtn.size());
						}
					}
				}	
		}
		System.out.println(fname.get(0).getText());
		System.out.println(fname.get(1).getText());
		System.out.println(fname.get(2).getText());
		
		//��ư �׸���
		for(int i=0; i < list.size(); i++) {
			//addpanel[i].setPreferredSize(new Dimension(199, 50));
			addpanel[i] = new JPanel();
			addpanel[i].setLayout(null);
			addpanel[i].setBounds(0, 5 + i * 50, 198, 50);
			addpanel[i].setBackground(Color.PINK);
			addpanel[i].add(fbtn.get(i));
			addpanel[i].add(fname.get(i));
			addpanel[i].add(fstat.get(i));
			addpanel[i].setVisible(true);
		
			//mainPane.setViewportView(addpanel[i]);
			mainPane.add(addpanel[i]);
		}
		//mainPane.revalidate();
		mainPane.revalidate();
		mainPane.repaint();
	}
	public void addLabel(ArrayList<String> list) {
		JCheckBox[] addlabel = new JCheckBox[list.size()];
		greenicon = imageSetSize(greenicon, 30, 30);
		
		for(int i=0; i<list.size(); i++) {
			addlabel[i] = new JCheckBox(list.get(i));
			addlabel[i].setBackground(new Color(255, 255, 255));
			addlabel[i].setBounds(0, 5 + i * 50, 198, 50);
			addlabel[i].addMouseListener(null);
			addlabel[i].setVisible(true);
			
			//mainPane.setViewportView(addlabel[i]);
			mainPane.add(addlabel[i]);
		}
		mainPane.revalidate();
		mainPane.repaint();
	}
	
	public class chatBtnAction implements ActionListener {//ä�ù� �г�
		@Override
		public void actionPerformed(ActionEvent e) {
			mainPane.setVisible(false);
			chatPane.setVisible(true);
		}
	}
	class friendBtnAction implements ActionListener {//ģ�� �г�
		@Override
		public void actionPerformed(ActionEvent e) {
			mainPane.setVisible(true);
			chatPane.setVisible(false);
		}
	}
	class makeChatAction implements ActionListener{//��ư�� ������ �� ä�ù� ���� 1:1
		@Override
		public void actionPerformed(ActionEvent e) {
			//��ư �ؽ�Ʈ ��������, ���������̶� ���ļ� ������ ������
			ArrayList<ChatMember> list = new ArrayList<>();
			ChatMember u = new ChatMember();
			
			for(int i=0; i<fname.size(); i++) {
				if(UserName.matches(fname.get(i).getText())) {
					u.mName.setText(UserName);
					u.profile = fbtn.get(i);
					u.stat = fstat.get(i);
					list.add(u);
					//u.stat
				}else if(fname.get(i).getText().matches(((JButton) e.getSource()).getText())) {
					u.mName.setText(((JButton) e.getSource()).getText());
					u.profile = fbtn.get(i);
					u.stat = fstat.get(i);
					list.add(u);
				}
			}
		
			ChatMsg obcm = new ChatMsg(UserName, "150", "chatlist");
			obcm.chatlist = list;
			SendObject(obcm);
		}
	}
	class CheckBoxList extends JFrame{
		CheckBoxList(){
			setTitle("");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(260, 280);
			
			checkPane = new JPanel();
			checkPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(checkPane);
			checkPane.setLayout(null);
			
			setVisible(true);
		}
	}
	class openCheckListAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			CheckBoxList check = new CheckBoxList();
		}
	}
	class makeGroupAction implements ActionListener{//��ư�� ������ �� ä�ù� ���� 1:n
		@Override
		public void actionPerformed(ActionEvent e) {
			//��ư �ؽ�Ʈ ��������, ���������̶� ���ļ� ������ ������
			//Vector<u.stat = fstat.get(i);
			///list.add(u);> list = new Vector<>();
			//list.add(UserName);
			//list.add(((JButton) e.getSource()).getText());
			//ChatMsg obcm = new ChatMsg(UserName, "150", "chatlist");
			//obcm.chatlist = list;
			//SendObject(obcm);
		}
	}
	public void openChat(String n) {
		HanoTalkClientView view = new HanoTalkClientView(mainView);
		view.RoomNumber = n;
		RoomList.add(view);
		view.AppendText("ä�ù��� �����Ǿ����ϴ�.");
	}
	ImageIcon imageSetSize(ImageIcon icon, int i, int j) {
		Image ximg = icon.getImage();
		Image yimg = ximg.getScaledInstance(i, j, java.awt.Image.SCALE_SMOOTH);
		ImageIcon xyimg = new ImageIcon(yimg);
		return xyimg;
	}
	
	class MyMouseEvent implements MouseListener, MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			

		}

		@Override
		public void mouseExited(MouseEvent e) {
			
			

		}

		@Override
		public void mousePressed(MouseEvent e) {
			

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}
	}
	
	public void SendObject(Object ob) { // ������ �޼����� ������ �޼ҵ�
		try {
			oos.writeObject(ob);
			oos.reset();
		} catch (IOException e) {
			//���� �˾� �޽��� ����
			JOptionPane.showMessageDialog(null, "SendObject Error");
		}
	}
	// Server���� network���� ����
	public void SendMessage(String msg, String id) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			obcm.room.roomid = id;
			oos.writeObject(obcm);
			oos.reset();
		} catch (IOException e) {
			//���� �˾� �޽���
			JOptionPane.showMessageDialog(null, "oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}
}