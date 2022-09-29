
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import static javax.swing.JOptionPane.*;



public class HanoTalkClientLogin extends JFrame{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUserName;//username==id
	private JTextField txtPassWord;
	private String IpAddress = "127.0.0.1";
	private String PortNumber = "30000";
	
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	
	ImageIcon icon = new ImageIcon("./Image_Folder/login.png");
	Image img = icon.getImage();
	Image img1 = img.getScaledInstance(150,50,Image.SCALE_SMOOTH);
	ImageIcon login_icon = new ImageIcon(img1);
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HanoTalkClientLogin frame = new HanoTalkClientLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public HanoTalkClientLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 275, 350);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 182, 193));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setBounds(25, 75, 82, 33);
		contentPane.add(lblNewLabel);
		
		txtUserName = new JTextField();
		txtUserName.setHorizontalAlignment(SwingConstants.CENTER);
		txtUserName.setBounds(110, 75, 116, 33);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		//JLabel lblPassWord = new JLabel("Password");
		//lblPassWord.setBounds(25, 140, 82, 33);
		//contentPane.add(lblPassWord);
		
		//txtPassWord = new JTextField();
		//txtPassWord.setHorizontalAlignment(SwingConstants.CENTER);
		//txtPassWord.setText("127.0.0.1");
		//txtPassWord.setColumns(10);
		//txtPassWord.setBounds(110, 140, 116, 33);
		//contentPane.add(txtPassWord);
		
		/*JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setBounds(20, 163, 82, 33);
		contentPane.add(lblPortNumber);
		
		txtPortNumber = new JTextField();
		txtPortNumber.setText("30000");
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setColumns(10);
		txtPortNumber.setBounds(101, 163, 116, 33);
		contentPane.add(txtPortNumber);*/
		
		
		JButton btnConnect = new JButton(login_icon);
		btnConnect.setBounds(70, 230, 130, 35);
		btnConnect.setBorderPainted(false);
		btnConnect.setPreferredSize(new Dimension(50,50));
		contentPane.add(btnConnect);
		
		
		Myaction action = new Myaction();
		btnConnect.addActionListener(action);
		txtUserName.addActionListener(action);
		//txtPassWord.addActionListener(action);
		//txtPortNumber.addActionListener(action);
		
	}
	class Myaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String id = txtUserName.getText().trim();
			//String pw = txtPassWord.getText().trim();
			//SendMessage(id, pw);
			
			//String login = readMessage();//�ڵ� �޾ƿ���
			
			/*if(login.matches("101")){//���� ȸ���̸� 
				HanoTalkClientMain maIn = new HanoTalkClientMain();
			}
			else if(login.matches("102")) {//pw�� Ʋ���� ���
				
			}
			else if(login.matches("103")) {//���� ȸ���� �ƴϸ� ȸ������ ����
				
				
			}*/
			
			
			//String port_no = txtPortNumber.getText().trim();
			//JavaGameClientView view = new JavaGameClientView(m.getId(), IpAddress, PortNumber);
			HanoTalkClientMain main = new HanoTalkClientMain(id, IpAddress, PortNumber);
			setVisible(false);
		}
	}
	
}
