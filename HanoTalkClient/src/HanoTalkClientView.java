
// JavaObjClientView.java ObjecStram ��� Client
//�������� ä�� â
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ImageObserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import java.awt.Canvas;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class HanoTalkClientView extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private JButton btnsendemoji;
	private JButton btnEmoji;
	
	private JButton btnsmile;
	
	private String ip_addr;
	private String port_no;
	
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	private Socket socket; // �������
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JLabel lblUserName;
	private JTextPane textArea;

	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;

	JPanel panel;
	private Graphics gc;
	private int pen_size = 2; 
	private Image panelImage = null; 
	private Graphics gc2 = null;
	ImageIcon sendjpg = new ImageIcon("./Image_Folder/send.png");
	ImageIcon emojipng = new ImageIcon("./Image_Folder/EMOJI.png");
	ImageIcon smile = new ImageIcon("./Image_Folder/smile.jpg");
	
	public HanoTalkClientView(HanoTalkClientMain mainview)  {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 634);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 352, 471);
		contentPane.add(scrollPane);

		textArea = new JTextPane();
		textArea.setBackground(new Color(255, 192, 203));
		textArea.setEditable(true);
		textArea.setFont(new Font("����ü", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 10, 10);
		contentPane.add(panel_1);

		txtInput = new JTextField();
		txtInput.setBounds(74, 489, 209, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton(sendjpg);
		btnSend.setBounds(295, 489, 69, 40);
		contentPane.add(btnSend);

		lblUserName = new JLabel("Name");
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setFont(new Font("����", Font.BOLD, 14));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(12, 539, 62, 40);
		contentPane.add(lblUserName);
		setVisible(true);

		UserName = mainview.UserName;
		ip_addr = mainview.IpAddress;
		port_no = mainview.PortNumber;
		AppendText("User " + UserName + " connecting " + ip_addr + " " + port_no);
		
		lblUserName.setText(UserName);

		imgBtn = new JButton("+");
		imgBtn.setFont(new Font("����", Font.PLAIN, 16));
		imgBtn.setBounds(12, 489, 50, 40);
		contentPane.add(imgBtn);

		JButton btnNewButton = new JButton("�� ��");
		btnNewButton.setFont(new Font("����", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		btnNewButton.setBounds(295, 539, 69, 40);
		contentPane.add(btnNewButton);
		
		btnEmoji = new JButton(emojipng);
		btnEmoji.setBounds(214, 539, 69, 40);
		contentPane.add(btnEmoji);
		btnEmoji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Emojipanel();
				setVisible(true);
			}
		} );
		
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();
			TextSendAction action = new TextSendAction();
			btnSend.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			ImageSendAction action2 = new ImageSendAction();
			EmojiSendAction action3 = new EmojiSendAction();
			imgBtn.addActionListener(action2);
			btnsmile.addActionListener(action3);
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}
	class Emojipanel extends JFrame {
		public Emojipanel(){
		JPanel Emojipanel = new JPanel();
		setBounds(100, 100, 400, 334);
		
		
		btnsmile = new JButton(smile);
		btnsmile.setBounds(0,0, 73, 74);
		add(btnsmile);
		
		btnsendemoji = new JButton(sendjpg);
		btnsendemoji.setBounds(164, 255, 73, 40);
		add(btnsendemoji);
		
		add(Emojipanel);
        setVisible(true);
        
        
		}
	}
	// Server Message�� �����ؼ� ȭ�鿡 ǥ��
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {

					Object obcm = null;
					String msg = null;
					String msg2 = null;
					ChatMsg cm;
					Date date = new Date();
					SimpleDateFormat time = new SimpleDateFormat("a h:m");
					
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n %s [%s]", cm.UserName,cm.data, time.format(date));
						msg2 = String.format("[%s]\n [%s] %s", cm.UserName, time.format(date),cm.data);
					} else
						continue;
					switch (cm.code) {
					case "200": // chat message
						if (cm.UserName.equals(UserName)) {
							AppendTextR(msg2); // �� �޼����� ������
						}
						else
							AppendText(msg);
						break;
					case "300": // Image ÷��
						if (cm.UserName.equals(UserName))
							AppendTextR("[" + cm.UserName + "]");
						else
							AppendText("[" + cm.UserName + "]");
						AppendImage(cm.img);
						AppendText("["+time.format(date)+"]");
						break;
					case "301": // �̸�Ƽ�� Image ÷��
						if (cm.UserName.equals(UserName))
							AppendTextR("[" + cm.UserName + "]");
						else
							AppendText("[" + cm.UserName + "]");
						AppendImage(cm.img);
						AppendText("["+time.format(date)+"]");
						break;
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����

			}
		}
	}
	
	// keyboard enter key ġ�� ������ ����
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				txtInput.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				if (msg.contains("/exit")) // ���� ó��
					System.exit(0);
			}
		}
	}

	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// �׼� �̺�Ʈ�� sendBtn�϶� �Ǵ� textField ���� Enter key ġ��
			if (e.getSource() == imgBtn) {
				frame = new Frame("�̹���÷��");
				fd = new FileDialog(frame, "�̹��� ����", FileDialog.LOAD);
				fd.setVisible(true);
				
				if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
					ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
					ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
					obcm.img = img;
					SendObject(obcm);
				}
			}
		}
	}

	class EmojiSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg obcm = new ChatMsg(UserName, "301", "IMG");
			obcm.img = smile;
			SendObject(obcm);
		}
	}
	ImageIcon icon1 = new ImageIcon("src/icon.jpg");

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	public void AppendText(String msg) {
		/*Date date = new Date();
		SimpleDateFormat time = new SimpleDateFormat("a h:m");
		*/
		msg = msg.trim(); 
		
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
	    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
	    
		try {
			//doc.insertString(doc.getLength(), msg + " " +time.format(date) + "\n", left );
			doc.insertString(doc.getLength(), msg +"\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
	}
	// ȭ�� ������ ���
	public void AppendTextR(String msg) {
		Date date = new Date();
		SimpleDateFormat time = new SimpleDateFormat("a h:m");
		
		msg = msg.trim();
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.BLUE);	
	    doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(), msg+"\n", right );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);

	}
	
	public void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		Image ori_img = ori_icon.getImage();
		Image new_img;
		ImageIcon new_icon;
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		
		if (width > 200 || height > 200) {
			if (width > height) { 
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { 
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			new_icon = new ImageIcon(new_img);
			textArea.insertIcon(new_icon);
		} else {
			textArea.insertIcon(ori_icon);
			new_img = ori_img;
		}
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
	}

	// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server���� network���� ����
	public void SendMessage(String msg) {
		try {
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
			oos.reset();
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
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

	public void SendObject(Object ob) { // ������ �޼����� ������ �޼ҵ�
		try {
			oos.writeObject(ob);
			oos.reset();
		} catch (IOException e) {
			AppendText("SendObject Error");
		}
	}
}
