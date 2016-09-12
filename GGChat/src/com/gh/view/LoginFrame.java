package com.gh.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.mina.core.session.IoSession;

import com.gh.control.Client;
import com.gh.utils.Tools;

import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.border.LineBorder;

/**
 * ͨ���̳�JFrameʵ��һ������
 * 
 * @author �¸���
 *
 */
public class LoginFrame extends JFrame {
	private JPanel panel;
	private JLabel lab_title;
	private JLabel lab_name;
	private JLabel lab_psw;
	private JTextField tf_id;
	private JPasswordField tf_password;
	private JButton ok;
	private JButton register;
	private JLabel lblNewLabel;
	private JButton button;
	// ȫ�ֵ�λ�ñ��������ڱ�ʾ����ڴ����ϵ�λ��
	static Point origin = new Point();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * ���췽��
	 */

	public LoginFrame() {
		setUndecorated(true);
		// ��ʼ������
		initialize();

	}

	/**
	 * �����϶��¼�
	 */
	public void initWindowMoveEvent() {
		addMouseListener(new MouseAdapter() {
			// ���£�mousePressed ���ǵ����������걻����û��̧��
			public void mousePressed(MouseEvent e) {
				// ����갴�µ�ʱ���ô��ڵ�ǰ��λ��
				origin.x = e.getX();
				origin.y = e.getY();

			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			// �϶���mouseDragged ָ�Ĳ�������ڴ������ƶ�������������϶�

			public void mouseDragged(MouseEvent e) {
				// ������϶�ʱ��ȡ���ڵ�ǰλ��
				Point FirstFrame = getLocation();
				// ���ô��ڵ�λ��
				// ���ڵ�ǰ��λ��+��굱ǰ�ڴ��ڵ�λ��-��갴�µ�ʱ���ڴ��ڵ�λ��
				setLocation(FirstFrame.x + e.getX() - origin.x, FirstFrame.y
						+ e.getY() - origin.y);
			}
		});
	}

	private void initialize() {
		setResizable(false);
		initWindowMoveEvent();//�϶��¼�
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				LoginFrame.class.getResource("/com/gh/res/1.png")));
		setTitle("GGTalk-\u767B\u9646");// ���ô���
		setSize(521, 280);
		setLocationRelativeTo(null);// ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ʵ�������
		panel = new JPanel();
		panel.setLayout(null);// null���֣������Ҫʹ��x,y,width,height����λ��
		lab_title = new JLabel("  Welcome login GGtalk");
		lab_title.setBounds(100, 30, 350, 50);

		// ������������
		panel.add(lab_title);

		// ��������
		lab_title.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		lab_title.setForeground(Color.WHITE);

		lab_name = new JLabel("\u767B\u5F55\u540D\uFF1A ");
		lab_name.setForeground(new Color(178, 34, 34));
		lab_name.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		lab_name.setHorizontalAlignment(SwingConstants.LEFT);
		lab_name.setBounds(100, 138, 100, 30);
		panel.add(lab_name);

		lab_psw = new JLabel("\u5BC6   \u7801 \uFF1A ");
		lab_psw.setForeground(new Color(178, 34, 34));
		lab_psw.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		lab_psw.setBounds(100, 188, 120, 30);
		panel.add(lab_psw);

		tf_id = new JTextField();
		tf_id.setBorder(new LineBorder(Color.BLACK, 1, true));
		tf_id.setForeground(new Color(204, 51, 51));
		tf_id.setOpaque(false);
		tf_id.setBounds(180, 138, 200, 30);
		panel.add(tf_id);

		tf_password = new JPasswordField();
		// ��enter����½
		tf_password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// ��½
					String username = tf_id.getText().trim();
					String password = new String(tf_password.getPassword());
					if (tf_id.getText().isEmpty()
							|| tf_password.getPassword().length == 0) {
						Tools.show(LoginFrame.this, "��½�������벻��Ϊ��");
						return;
					}
					// ��֤��½
					String readIpAndPort = Tools.readIpAndPort();
					String[] split = readIpAndPort.split("~");
					String ip=split[0];
					int port=Integer.parseInt(split[1]);
					Client client = new Client(LoginFrame.this);
					client.setIP(ip);
					client.setPort(port);
					client.login(username, password);
				}
			}
		});
		tf_password.setBorder(new LineBorder(Color.BLACK, 1, true));
		tf_password.setBounds(180, 188, 200, 30);
		tf_password.setOpaque(false);
		panel.add(tf_password);
		/**
		 * ��½
		 */
		ok = new JButton("\u767B\u5F55");
		ok.setForeground(new Color(178, 34, 34));
		ok.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		ok.setOpaque(false);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��½
				String username = tf_id.getText().trim();
				String password = new String(tf_password.getPassword());
				if (tf_id.getText().isEmpty()
						|| tf_password.getPassword().length == 0) {
					Tools.show(LoginFrame.this, "��½�������벻��Ϊ��");
					return;
				}
				// ��֤��½
				String readIpAndPort = Tools.readIpAndPort();
				String[] split = readIpAndPort.split("~");
				String ip=split[0];
				int port=Integer.parseInt(split[1]);
				Client client = new Client(LoginFrame.this);
				client.setIP(ip);
				client.setPort(port);
				client.login(username, password);
			}
		});
		ok.setBackground(Color.WHITE);
		ok.setBounds(180, 238, 80, 30);
		panel.add(ok);

		/**
		 * ע��
		 */
		register = new JButton("\u6CE8\u518C");
		register.setForeground(new Color(178, 34, 34));
		register.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		register.setOpaque(false);
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ע��
				new Thread(new Runnable() {
					@Override
					public void run() {
						String readIpAndPort = Tools.readIpAndPort();
						String[] split = readIpAndPort.split("~");
						String ip=split[0];
						int port=Integer.parseInt(split[1]);
						Client client = new Client(LoginFrame.this);
						client.setIP(ip);
						client.setPort(port);
						RegisterFrame registerFrame = new RegisterFrame(
								LoginFrame.this);
						client.setRegisterFrame(registerFrame);
						if (client.connect()) {
							registerFrame.setClientSession(client.getSession());
							registerFrame.setVisible(true);
							LoginFrame.this.setVisible(false);
						}
					}
				}).start();
			}
		});
		register.setBackground(Color.WHITE);
		register.setBounds(293, 238, 87, 30);
		panel.add(register);

		// ���ô����Ĭ�����
		setContentPane(panel);

		JButton btnX = new JButton("X");
		btnX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnX.setBorder(new LineBorder(Color.WHITE, 1, true));
		btnX.setBackground(new Color(178, 34, 34));
		btnX.setForeground(Color.WHITE);
		btnX.setBounds(473, 0, 39, 23);
		panel.add(btnX);

		button = new JButton("-");
		button.addActionListener(new ActionListener() {
			// ������С��
			public void actionPerformed(ActionEvent e) {
				setExtendedState(JFrame.ICONIFIED);
			}
		});
		button.setBorder(new LineBorder(Color.WHITE, 1, true));
		button.setBackground(new Color(178, 34, 34));
		button.setForeground(Color.WHITE);
		button.setBounds(434, 0, 39, 23);
		panel.add(button);
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginFrame.class
				.getResource("/com/gh/res/\u767B\u5F55.jpg")));
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setBounds(0, 0, 519, 278);
		panel.add(lblNewLabel);
		File file=new File("data/");
		if(!file.exists()){
			file.mkdirs();
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("data/IP.txt"));
				bw.write("192.168.48.2~9000");
				bw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
