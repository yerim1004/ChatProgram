//mainȭ���� ģ�� ����Ʈ ȭ��
import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
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
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;

public class HanoTalkClientMain extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public String IpAddress;
	public String PortNumber;
	
	public static JPanel mainPanel;
	public JPanel sidePanel;
	public static JPanel chatPanel; 
	
	public JButton friendBtn;
	public JButton chatBtn;
	public JButton myBtn;
	
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	private Socket socket; // �������
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private static ArrayList<String> userlist;//chatobject�κ��� �������Ʈ ���� ����
	private static ArrayList<JButton> fbtn;//ģ�� ��� ��ư����Ʈ
	
	public String UserName;
	public HanoTalkClientMain mainView;
	
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
		
		sidePanel = new JPanel();
		sidePanel.setBounds(0, 0, 60, 311);
		sidePanel.setBackground(new Color(255, 255, 255));
		contentPane.add(sidePanel);
		
		mainPanel = new JPanel();
		mainPanel.setBounds(60, 0, 199, 311);
		mainPanel.setBackground(new Color(240, 180, 190));
		contentPane.add(mainPanel);
		
		chatPanel = new JPanel();
		chatPanel.setBounds(60, 0, 199, 311);
		chatPanel.setBackground(new Color(240, 180, 190));
		chatPanel.setVisible(false);
		contentPane.add(chatPanel);
		
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
			
			ChatMsg obcm = new ChatMsg(UserName, "110", "Online");
			SendObject(obcm);
			
			ListenNetwork net = new ListenNetwork();
			net.start();
			
			
			
			myBtn = new JButton(UserName);
			myBtn.setPreferredSize(new Dimension(100, 20));
			mainPanel.add(myBtn);
			
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "connect error");
		}
	}
	public void addButtons(ArrayList<String> list) {
		ArrayList<JButton> addbtn = new ArrayList<>();
		//JOptionPane.showMessageDialog(null, list.size());
		for(int i=0; i<list.size(); i++) {
			addbtn.add(new JButton());
			addbtn.get(i).setText(list.get(i));
			addbtn.get(i).setBackground(new Color(255, 255, 255));
			addbtn.get(i).setPreferredSize(new Dimension(100, 20));
			addbtn.get(i).addActionListener(new openChatAction()); //��ư�� ������ ä�ù� ����
			addbtn.get(i).setVisible(true);
			
			mainPanel.add(addbtn.get(i));
		}
		revalidate();
		repaint();
	}
	public void addLabel(ArrayList<String> list) {
		JLabel[] addlabel = new JLabel[list.size()];// = new JLabel();
		for(int i=0; i<list.size(); i++) {
			addlabel[i] = new JLabel(list.get(i));
			addlabel[i].setBackground(new Color(255, 255, 255));
			addlabel[i].setPreferredSize(new Dimension(100, 20));
			addlabel[i].addMouseListener(null);
			addlabel[i].setVisible(true);
			
			mainPanel.add(addlabel[i]);
		}
		revalidate();
		repaint();
	}
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					userlist = new ArrayList<String>();
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
						for(int i=0; i<userlist.size(); i++) {
							String name = userlist.get(i);
							if(name.matches(UserName)) {//�ڱ��ڽ��� ��ư�� ������� ����
								//JOptionPane.showMessageDialog(null, name);
								userlist.remove(String.valueOf(name));
							}
						}
						addButtons(userlist);
						//addLabel(userlist);
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
	class resetBtnAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//���ΰ�ħ ��ư�� ������
	
			ChatMsg obcm = new ChatMsg(UserName, "120", "list ��û");
			SendObject(obcm);

		}
	}
	class chatBtnAction implements ActionListener {//ä�ù� �г�
		@Override
		public void actionPerformed(ActionEvent e) {
			mainPanel.setVisible(false);
			chatPanel.setVisible(true);
		}
	}
	class friendBtnAction implements ActionListener {//ģ�� �г�
		@Override
		public void actionPerformed(ActionEvent e) {
			mainPanel.setVisible(true);
			chatPanel.setVisible(false);
		}
	}
	class openChatAction implements  ActionListener{//��ư�� ������ �� ä�ù� ����
		@Override
		public void actionPerformed(ActionEvent e) {
			HanoTalkClientView chatview = new HanoTalkClientView(mainView);
		}
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
}