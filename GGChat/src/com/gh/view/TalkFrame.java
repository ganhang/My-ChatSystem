package com.gh.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.mina.core.session.IoSession;

import com.gh.model.AddInfo;
import com.gh.model.DelInfo;
import com.gh.model.Pepole;
import com.gh.model.SendInfo;
import com.gh.utils.Tools;

import java.awt.Toolkit;
import javax.swing.border.LineBorder;

/**
 * 
 * @author �����ԣ�ʢ����
 *
 */
public class TalkFrame extends JFrame {
	private DefaultListModel<String> useModel;
	private DefaultListModel<String> onlineModel;
	private Hashtable<String, String> idAndContent = new Hashtable<String, String>();// ÿ������id��Ӧһ����������
	private Hashtable<String, Integer> idAndcount = new Hashtable<String, Integer>();// ÿ������id��Ӧһ������
	private JList<String> list_b;
	private JList<String> list;
	private JLabel onlineNum;
	private int onlineCount = 0;
	private JTextArea ta_show;
	private String myId;// ��������id
	private JTextArea ta_input;
	private String onlineId;// ���������id
	private String friendId;// �����friendid
	private JLabel title;

	public String getMyId() {
		return myId;
	}

	// �õ�ѡ��ĺ���id
	public String getFriendId() {
		return friendId;
	}

	// �õ�ѡ�������id

	public String getOnlineId() {
		return onlineId;
	}

	/**
	 * Create the application.
	 */
	public TalkFrame(String myName, String myId, IoSession clientSession) {

		this.myId = myId;
		initialize(myName, myId, clientSession);// ��ʼ������

		InitUserList(myId, clientSession);// ��ʼ���û��б�
		idAndContent.put("���������û�", "");

		onlineId = "���������û�";
		new Thread(new Runnable() {// ��һ���߳�ˢ���������
					private String clikContent = idAndContent.get("���������û�");

					@Override
					public void run() {
						while (true) {
							try {
								Thread.sleep(100);
								clikContent = idAndContent.get(onlineId);
								ta_show.setText(clikContent);
								ta_show.setCaretPosition(ta_show.getText().length());// �������Զ����������
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();

	}

	// �����ı�����
	/**
	 * 
	 * @param id ��˭˵
	 * @param text ˵������
	 */
	public void addText(String id, String text) {
		
		String time = Tools.getTime();//�õ�ʱ��
		String oldContent="";
		if(idAndContent.get(id)==null){
			oldContent="";
		}else{
			oldContent=idAndContent.get(id);//�õ��ɵ�����
		}
		String newContent = oldContent+time + "\n" + text + "\n";//������
		idAndContent.put(id, newContent);//���Ǿ�����
		ta_show.setCaretPosition(ta_show.getText().length());// �������Զ����������
		//�б���ʾ��Ϣ
		
		int talkNum=idAndcount.get(id)==null? 0:idAndcount.get(id);//id����
		idAndcount.put(id, talkNum+1);//��ԭ�������ϼ�һ
		talkNum=idAndcount.get(id);//�õ�����id����
		Object[] array =  onlineModel.toArray();//�õ��б�
		for(int i=0;i<array.length;i++){
			String sid=(String) array[i];
			if(sid.startsWith(id)){
				//�������Ҫ�ӵ����ݵ�id��ʼ���ı�����
				onlineModel.set(i, id+"-"+talkNum);
				break;
			}
		}
		
	}
	
	public void setTalk(boolean b) {
		if (b) {
			Tools.show(this, "��ϲ���㱻������ԣ��Ժ�ú�����,oh���ú�˵����");
		} else {
			Tools.show(this, "GG�㱻����,��ú�˵����");
		}
		ta_input.setEditable(b);
	}

	// ��ʼ���û��б������״̬�б�
	public void InitUserList(String myId, IoSession clientSession) {
		useModel = new DefaultListModel<String>();
		onlineModel = new DefaultListModel<String>();
		onlineModel.addElement("���������û�");
		list.setModel(useModel);
		list_b.setModel(onlineModel);
		// �ҷ�����Ҫ�����б�
		clientSession.write("���Һ���");
		clientSession.write("�������ߺ���");

	}

	// �ӷ��������º����б�
	public void UpdataList(String[] friends) {
		// ���˶�ǽ����ǽ������֮ǰ��ӵ�Ҫȫɾ�ˣ������һ��
		System.out.println("��ʼ���º����б�");
		useModel.removeAllElements();
		for (String f : friends) {
			useModel.addElement(f);
		}
	}

	// �ӷ������������ߺ����б�
	public void UpdataOnlineList(String[] onliefriends) {

		System.err.println("��ʼ�������ߺ����б�:");
		onlineModel.removeAllElements();
		onlineCount = 0;// �Ƴ����й�0
		onlineModel.addElement("���������û�");
		for (String f : onliefriends) {
			onlineCount++;
			onlineModel.addElement(f);
		}
		onlineNum.setText(onlineCount + "");// ������ʾ

	}
	//������ߺ���
	public void addOnlineList(String id){
		onlineCount++;
		onlineModel.addElement(id);
		onlineNum.setText(onlineCount + "");// ������ʾ
		
	}
	// �Ƴ������û�
	public void delOnlineList(String id){
		onlineCount--;
		Object[] array =  onlineModel.toArray();
		for(Object s:array){
			String str=(String) s;
			if(str.startsWith(id)){
				id=str;
				break;
			}
		}
		onlineModel.removeElement(id);
		onlineNum.setText(onlineCount + "");// ������ʾ
	}

	// ��Ӻ���
	public void addfiend(String friendId) {
		useModel.addElement(friendId);
	}

	// �Ƴ�����
	public void DelUser(String friendId) {
		useModel.removeElement(friendId);
		System.err.println("ɾ��" + friendId);
	}
	//��ʾ��Ϣ����
	public void newInfoFrame(Pepole p){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new UseInfoFrame(p.getId(), p.getName(),p.getSex(),p.getLikes(), p.getEmail(), p.getInfo())
					.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
//	// �Ƴ������û�
//	public void DelOnlineUser(String id) {
//		
//		list_b.clearSelection();
//		onlineCount--;// ��ǰ������
//		onlineModel.removeElement(id);
//		// list_b.setModel(onlineModel);
//		onlineNum.setText(onlineCount + "");// ������ʾ
//	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String myname, String myid, IoSession clientSession) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				LoginFrame.class.getResource("/com/gh/res/1.png")));
		setResizable(false);
		setTitle("GGtalk_��ǰID:" + myid);
		setBounds(100, 100, 720, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new LineBorder(Color.WHITE, 1, true));
		scrollPane_1.setBounds(10, 50, 103, 296);
		getContentPane().add(scrollPane_1);
		// �����б�
		list = new JList<String>();
		list.setBorder(new LineBorder(Color.WHITE, 1, true));
		list.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		list.setForeground(Color.WHITE);
		list.setBackground(new Color(0,0,0,0));//ʵ��͸��Ч��.
		list.setOpaque(false);
		scrollPane_1.setOpaque(false);
		scrollPane_1.getViewport().setOpaque(false);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount()==2){
					System.err.println("�����list����");
					friendId=list.getSelectedValue();
					if(friendId==null){
						Tools.show(TalkFrame.this, "��ѡ��id��");
					}else{
						clientSession.write("������Ϣ~"+friendId);
					}
				}
			}
		});
		scrollPane_1.setViewportView(list);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(new LineBorder(Color.WHITE, 1, true));
		scrollPane_2.setBounds(123, 50, 101, 296);
		getContentPane().add(scrollPane_2);
		// �����б�
		list_b = new JList<String>();
		list_b.setBorder(new LineBorder(Color.WHITE, 1, true));
		list_b.setForeground(Color.WHITE);
		list_b.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		list_b.setBackground(new Color(0,0,0,0));//ʵ��͸��Ч��.
		list_b.setOpaque(false);
		scrollPane_2.setOpaque(false);
		scrollPane_2.getViewport().setOpaque(false);
		list_b.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ta_show.setCaretPosition(ta_show.getText().length());// �������Զ����������
				 String[] split = list_b.getSelectedValue().split("-");
				onlineId = split[0];
				System.err.println("�㵥����list_b��ѡ����" + onlineId);
				title.setText("��" + myname + "����ӭʹ��GGtalk,�����ں͡�" + onlineId
						+ "������");// ����title
				ta_show.setText(idAndContent.get(onlineId));// ���������¼
				idAndcount.put(onlineId, 0);
				Object[] array =  onlineModel.toArray();//�õ��б�
				for(int i=0;i<array.length;i++){
					String sid=(String) array[i];
					if(sid.startsWith(onlineId)){
						//�������Ҫ�ӵ����ݵ�id��ʼ���ı�����
						onlineModel.set(i, onlineId);
						break;
					}
				}
			}
		});
		scrollPane_2.setViewportView(list_b);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(231, 33, 483, 362);
		
		getContentPane().add(scrollPane);

		ta_show = new JTextArea();
		
		ta_show.setEditable(false);
		ta_show.setFocusable(false);
		scrollPane.setViewportView(ta_show);
		// ��Ӻ���
		JButton btn_add = new JButton("\u6DFB\u52A0\u597D\u53CB");
		btn_add.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		btn_add.setBackground(Color.WHITE);
		btn_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onlineId = list_b.getSelectedValue();
				if (onlineId == null) {// ��Ϊ��
					Tools.show(TalkFrame.this, "��ѡ��id������id��");
					return;
				} else {
					if (onlineId.equals(myid)) {// ����Ϊ�Լ�
						Tools.show(TalkFrame.this, "��������Լ�");
						return;
					} else {
						clientSession.write(new AddInfo(myid, onlineId));
						System.out.println("������Ӻ�������");
					}
				}
			}
		});
		btn_add.setBounds(121, 377, 103, 31);
		getContentPane().add(btn_add);
		// ɾ������
		JButton btn_delete = new JButton("\u5220\u9664\u597D\u53CB");
		btn_delete.setBackground(Color.WHITE);
		btn_delete.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				friendId = list.getSelectedValue();
				if (friendId == null) {// ��Ϊ��
					Tools.show(TalkFrame.this, "��ѡ��id������id��");
					return;
				} else {
					if (friendId.equals(myid)) {// ����Ϊ�Լ�
						Tools.show(TalkFrame.this, "����ɾ���Լ�");
						return;
					} else {
						clientSession.write(new DelInfo(myid, friendId));
						System.out.println("����ɾ����������");
					}
				}

			}
		});

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(231, 398, 353, 72);
		getContentPane().add(scrollPane_3);
		// ���������
		ta_input = new JTextArea();
		ta_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				boolean eb = false;
				boolean ec = false;
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					eb = true;
				}
				if (e.isControlDown()) {
					ec = true;
				}
				if (eb && ec) {
					// ��ȡѡ�����
					onlineId=list_b.getSelectedValue();
					System.out.println("���ð�ť������Ϣ����" + onlineId);
					if (onlineId == null) {
						onlineId = "���������û�";
					}
					if (onlineId.equals(myid)) {
						Tools.show(TalkFrame.this, "���ܶ��Լ�����");
						return;
					} // �ж�����
					String sendContent = ta_input.getText().trim();
					if (sendContent.equals("") || sendContent == null) {
						Tools.show(TalkFrame.this, "���ݲ���Ϊ��");
						return;
					} else {
						String[] split = onlineId.split("-");
						onlineId = split[0];
						SendInfo sendInfo = new SendInfo(myId, onlineId,
								sendContent);
						clientSession.write(sendInfo);
						
						addText(onlineId, "���㡿��"+"��"+onlineId+"��˵:\n"+sendContent);
						idAndcount.put(onlineId, 0);
						Object[] array =  onlineModel.toArray();//�õ��б�
						for(int i=0;i<array.length;i++){
							String sid=(String) array[i];
							if(sid.startsWith(onlineId)){
								//�������Ҫ�ӵ����ݵ�id��ʼ���ı�����
								onlineModel.set(i, onlineId);
								break;
							}
						}
						ta_input.setText("");
//						ta_show.setCaretPosition(ta_show.getText().length());// �������Զ����������
					}
				}
			}
		});
		scrollPane_3.setViewportView(ta_input);
		btn_delete.setBounds(10, 377, 103, 31);
		getContentPane().add(btn_delete);

		JLabel label = new JLabel("\u6211\u7684\u597D\u53CB");
		label.setForeground(Color.WHITE);
		label.setBackground(Color.WHITE);
		label.setFont(new Font("���Ŀ���", Font.BOLD, 13));
		label.setBounds(10, 33, 68, 15);
		getContentPane().add(label);

		JLabel onlineZxNum = new JLabel("��ǰ�����û�����:");
		onlineZxNum.setForeground(Color.WHITE);
		onlineZxNum.setFont(new Font("���Ŀ���", Font.BOLD, 14));
		onlineZxNum.setBounds(0, 0, 131, 31);
		getContentPane().add(onlineZxNum);

		title = new JLabel("��" + myname + "����ӭʹ��GGtalk,�����ں͡������ˡ�����");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("���Ŀ���", Font.BOLD, 14));
		title.setBounds(241, 0, 448, 31);
		getContentPane().add(title);
		// ���Ͱ�ť
		JButton Enter = new JButton("����(Ctrl+Enter)");
		Enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��ȡѡ�����
				onlineId=list_b.getSelectedValue();
				System.out.println("���ð�ť������Ϣ����" + onlineId);
				if (onlineId == null) {
					onlineId = "���������û�";
				}
				if (onlineId.equals(myid)) {
					Tools.show(TalkFrame.this, "���ܶ��Լ�����");
					return;
				} // �ж�����
				String sendContent = ta_input.getText().trim();
				if (sendContent.equals("") || sendContent == null) {
					Tools.show(TalkFrame.this, "���ݲ���Ϊ��");
					return;
				} else {
					String[] split = onlineId.split("-");
					onlineId = split[0];
					SendInfo sendInfo = new SendInfo(myId, onlineId,
							sendContent);
					clientSession.write(sendInfo);
					addText(onlineId, "���㡿��"+"��"+onlineId+"��˵:\n"+sendContent);
					idAndcount.put(onlineId, 0);
					Object[] array =  onlineModel.toArray();//�õ��б�
					for(int i=0;i<array.length;i++){
						String sid=(String) array[i];
						if(sid.startsWith(onlineId)){
							//�������Ҫ�ӵ����ݵ�id��ʼ���ı�����
							onlineModel.set(i, onlineId);
							break;
						}
					}
					ta_input.setText("");
//					ta_show.setCaretPosition(ta_show.getText().length());// �������Զ����������
				}
			}
		});

		JButton button = new JButton(
				"\u6E05\u7A7A\u5E76\u4FDD\u5B58\u804A\u5929\u8BB0\u5F55");
		button.setMargin(new Insets(2, 10, 2, 10));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��������¼
				String talk = ta_show.getText();
				onlineId=list_b.getSelectedValue();
				if (onlineId == null) {
					onlineId = "���������û�";
				}
				String[] split = onlineId.split("-");
				onlineId = split[0];
				idAndContent.put(onlineId, "");
				Tools.writefile(myid, onlineId, talk);
				ta_show.setText("");
			}
		});
		button.setBackground(Color.WHITE);
		button.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		button.setBounds(583, 439, 135, 31);
		getContentPane().add(button);
		Enter.setBackground(Color.WHITE);
		Enter.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		Enter.setBounds(583, 398, 131, 43);
		getContentPane().add(Enter);
		
		JLabel label_1 = new JLabel("\u53CC\u51FB\u597D\u53CB\u663E\u793A\u597D\u53CB\u4FE1\u606F");
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("���Ŀ���", Font.BOLD, 13));
		label_1.setBounds(10, 356, 162, 15);
		getContentPane().add(label_1);

		onlineNum = new JLabel("0");
		onlineNum.setForeground(Color.WHITE);
		onlineNum.setIcon(null);
		onlineNum.setFont(new Font("���Ŀ���", Font.BOLD, 16));
		onlineNum.setBounds(136, 0, 36, 31);
		getContentPane().add(onlineNum);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(TalkFrame.class.getResource("/com/gh/res/\u804A\u5929.jpg")));
		lblNewLabel.setBounds(0, 0, 714, 472);
		getContentPane().add(lblNewLabel);

	}
}
