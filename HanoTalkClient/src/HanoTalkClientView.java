
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
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
   private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
   private JButton btnSend;
   private JButton btnsendemoji;
   private JButton btnEmoji;
   private JButton btnsmile;
   private JButton btnwink;
   private JButton btnangry;
   private JButton btndiggy;
   private JButton btnhappy;
   private JButton btnmerong;
   private JButton btncool;
   private String ip_addr;
   private String port_no;
   private HanoTalkClientMain mainview;
   public String RoomNumber;
   
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
   ImageIcon wink = new ImageIcon("./Image_Folder/wink.png");
   ImageIcon angry = new ImageIcon("./Image_Folder/angry.jpg");
   ImageIcon cool = new ImageIcon("./Image_Folder/cool.jpg");
   ImageIcon diggy = new ImageIcon("./Image_Folder/diggy.jpg");
   ImageIcon happy = new ImageIcon("./Image_Folder/happy.png");
   ImageIcon merong = new ImageIcon("./Image_Folder/merong.jpg");
   
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
      textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
      scrollPane.setViewportView(textArea);
      
      /*JPanel panel_1 = new JPanel();
      panel_1.setBounds(0, 0, 10, 10);
      contentPane.add(panel_1);
*/
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
      lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
      lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
      lblUserName.setBounds(12, 539, 62, 40);
      contentPane.add(lblUserName);
      setVisible(true);
      
      AppendText("User " + UserName + " connecting " + ip_addr + " " + port_no);
      lblUserName.setText(UserName);

      imgBtn = new JButton("+");
      imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
      imgBtn.setBounds(12, 489, 50, 40);
      contentPane.add(imgBtn);

      JButton btnNewButton = new JButton("종 료");
      btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
      btnNewButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
            msg.room.roomid = RoomNumber;
            mainview.SendObject(msg);
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
         /*socket = new Socket(ip_addr, Integer.parseInt(port_no));
         oos = new ObjectOutputStream(socket.getOutputStream());
         oos.flush();
         ois = new ObjectInputStream(socket.getInputStream());
*/
         ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
         obcm.room.roomid = RoomNumber;
         mainview.SendObject(obcm);

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
      } catch (NumberFormatException e) {
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
      btnwink = new JButton(wink);
      btnwink.setBounds(73,0, 73, 74);
      add(btnwink);
      btnangry = new JButton(angry);
      btnangry.setBounds(146,0, 73, 74);
      add(btnangry);
      btncool = new JButton(cool);
      btncool.setBounds(0,74, 73, 74);
      add(btncool);
      btndiggy = new JButton(diggy);
      btndiggy.setBounds(73,74, 73, 74);
      add(btndiggy);
      btnhappy = new JButton(happy);
      btnsmile.setBounds(146,74, 73, 74);
      add(btnhappy);
      //btnmerong = new JButton(merong);
      //btnmerong.setBounds(219,, 73, 74);
      add(btnmerong);
      btnsendemoji = new JButton(sendjpg);
      btnsendemoji.setBounds(164, 255, 73, 40);
      add(btnsendemoji);
      
      add(Emojipanel);
        setVisible(true);
        
        
      }
   }
   
   
   // Server Message를 수신해서 화면에 표시
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
                     AppendTextR(msg2); // 내 메세지는 우측에
                  }
                  else
                     AppendText(msg);
                  break;
               case "300": // Image 첨부
                  if (cm.UserName.equals(UserName)) {
                     AppendTextR("[" + cm.UserName + "]");
                     AppendImage(cm.img);
                     AppendTextR("["+time.format(date)+"]");
                  }
                  else {
                     AppendText("[" + cm.UserName + "]");
                     AppendImage(cm.img);
                     AppendText("["+time.format(date)+"]");
                  }
                  break;
               case "301": // 이모티콘 Image 첨부
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
            
            } // 바깥 catch문끝

         }
      }
   }
   
   // keyboard enter key 치면 서버로 전송
   class TextSendAction implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         // Send button을 누르거나 메시지 입력하고 Enter key 치면
         if (e.getSource() == btnSend || e.getSource() == txtInput) {
            String msg = null;
            // msg = String.format("[%s] %s\n", UserName, txtInput.getText());
            msg = txtInput.getText();
            mainview.SendMessage(msg, RoomNumber);
            txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
            txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
            if (msg.contains("/exit")) // 종료 처리
               System.exit(0);
         }
      }
   }

   class ImageSendAction implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         // 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
         if (e.getSource() == imgBtn) {
            frame = new Frame("이미지첨부");
            fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
            fd.setVisible(true);
            
            if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
               ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
               ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
               obcm.img = img;
               obcm.room.roomid = RoomNumber;
               mainview.SendObject(obcm);
            }
         }
      }
   }

   class EmojiSendAction implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if(e.getSource()==Emojipanel) {
         ChatMsg obcm = new ChatMsg(UserName, "301", "IMG");
         obcm.img = smile;
         obcm.room.roomid = RoomNumber;
         mainview.SendObject(obcm);
         }
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
   // 화면 우측에 출력
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

   // Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
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
}