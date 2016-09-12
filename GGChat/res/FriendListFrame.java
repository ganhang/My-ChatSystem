package com.gh.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.mina.core.session.IoSession;

import com.gh.control.Server;
import com.gh.model.AddInfo;
import com.gh.model.Pepole;
import com.gh.utils.Tools;

/**
 * 
 * @author ʢ����
 *
 */
public class FriendListFrame extends JFrame {

	private JPanel contentPane;
	private JTextField tf_add;
	private JLabel lblNewLabel;
	private JList<String> list;
	private JLabel lblNewLabel_1;
	private JButton btn_delete;
	private JButton btn_send;
	private DefaultListModel<String> useModel;
	private DefaultListModel<String> onlineModel;
	private JList<String> list_b;
	private String friendId;
	//�õ�Ҫ��ӵ�friend id
	public String getFriendId() {
		return friendId;
	}
	/**
	 * Create the frame.
	 * 
	 * @param client
	 */
	public FriendListFrame(String myName, String myId, IoSession clientSession) {

		initialize(myName, myId, clientSession);// ��ʼ������

		InitUserList(myId, clientSession);// ��ʼ���û��б�
	}

	// ��ʼ���û��б������״̬�б�
	public void InitUserList(String myId, IoSession clientSession) {
		useModel = new DefaultListModel<String>();
		onlineModel = new DefaultListModel<String>();
		useModel.addElement("������");
		onlineModel.addElement("����״̬");
		// �ҷ�����Ҫ�����б�
		clientSession.write("���Һ���");
		clientSession.write("�������ߺ���");

	}

	// �ӷ��������º����б�
	public void UpdataList(String[] friends) {
		// ���˶�ǽ����ǽ������֮ǰ��ӵ�Ҫȫɾ�ˣ������һ��
		System.out.println("��ʼ���º����б�");
		useModel.removeAllElements();
		useModel.addElement("������");
		for (String f : friends) {
			useModel.addElement(f);
		}
		list.setModel(useModel);
	}

	// �ӷ������������ߺ����б�
	public void UpdataOnlineList(String[] onliefriends) {
		System.out.println("��ʼ�������ߺ����б�");
		onlineModel.removeAllElements();
		onlineModel.addElement("���������������û�");
		for (String f : onliefriends) {
			onlineModel.addElement(f);
		}
		list_b.setModel(onlineModel);
	}
	public void addfiend(String friendId){
		useModel.addElement(friendId);
	}
	// �Ƴ�����
	public void DelUser(String username) {
		
	}

	// �Ƴ������û�
	public void DelOnlineUser(String username) {
		onlineModel.removeElement(username);
	}

	private void initialize(String myname, String myid, IoSession clientSession) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				"E:\\WorkProject\\GGChat\\res\\1.png"));
		setTitle("GGTalk-" + myname + "-" + myid);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 310, 495);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tf_add = new JTextField();
		tf_add.setBounds(36, 355, 121, 21);
		contentPane.add(tf_add);
		tf_add.setColumns(10);

		JButton btn_add = new JButton("��Ӻ���");
		btn_add.setFont(new Font("���Ŀ���", Font.PLAIN, 14));
		btn_add.setBounds(171, 354, 93, 23);
		btn_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				friendId = tf_add.getText();
				tf_add.setText("");
				if (friendId.equals(myid)) {
					Tools.show(FriendListFrame.this, "��������Լ�Ϊ����");
				} else if (friendId.isEmpty()) {
					Tools.show(FriendListFrame.this, "�������Ϊ�գ�");
				} else {
					clientSession.write(new AddInfo(myid, friendId));
					System.out.println("FriendListFrame����д������");
				}
			}
		});
		contentPane.add(btn_add);

		btn_delete = new JButton("ɾ������");
		btn_delete.setFont(new Font("���Ŀ���", Font.PLAIN, 14));
		btn_delete.setBounds(46, 386, 93, 23);
		contentPane.add(btn_delete);

		btn_send = new JButton("����");
		btn_send.setFont(new Font("���Ŀ���", Font.PLAIN, 14));
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btn_send.setBounds(171, 387, 93, 23);
		contentPane.add(btn_send);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(17, 39, 126, 306);
		contentPane.add(scrollPane);

		list = new JList<String>();
		list.setBorder(null);// �ޱ߿�
		// list.setOpaque(false);// ͸��
		scrollPane.setViewportView(list);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(160, 39, 126, 306);
		contentPane.add(scrollPane_1);

		list_b = new JList<String>();
		list_b.setBorder(null);// �ޱ߿�
		scrollPane_1.setViewportView(list_b);
		// scrollPane.setOpaque(false);//͸��
		// scrollPane.getViewport().setOpaque(false);//͸��

		lblNewLabel = new JLabel("\u597D\u53CB\u5217\u8868");
		lblNewLabel.setFont(new Font("���Ŀ���", Font.PLAIN, 18));
		lblNewLabel.setBounds(101, 10, 93, 19);
		contentPane.add(lblNewLabel);

		lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(
				"E:\\WorkProject\\GGChat\\res\\5.jpg"));
		lblNewLabel_1.setBounds(0, -23, 304, 490);
		contentPane.add(lblNewLabel_1);
	}
}
