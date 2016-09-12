package com.gh.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.gh.control.Server;
import com.gh.utils.Tools;

import java.awt.Insets;
import java.io.File;
/**
 * 
 * @author ganhang
 *
 */
public class SeverFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;
	private DefaultListModel<String> onlinemodel=null;
	private DefaultListModel<String> nottalkemodel=null;
	private JList<String> list_online;
	private static Server server;
	private JList<String> list_nottalk;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SeverFrame frame = new SeverFrame();
					frame.setVisible(true);
					server = new Server(frame);
					int readPort = Tools.readPort();
					server.setPort(readPort);
					server.startServer();
					frame.addText("�������Ѿ������������ڼ���"+readPort+"�˿ڣ�");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public SeverFrame() {
		//��ʼ������
		initialize();
		
		InitUserList();//��ʼ�������б�
	}
	//�����ı���
	public void addText(String text){
		String time=Tools.getTime();
		String content=textArea.getText();
		content+=time+"\n"+text+"\n";
		textArea.setText(content);
		textArea.setCaretPosition(textArea.getText().length());
	}
	//��ʼ���û��б�
	public void InitUserList(){
		onlinemodel=new DefaultListModel<String>();
		nottalkemodel=new DefaultListModel<String>();
		onlinemodel.addElement("������");
		nottalkemodel.addElement("�����б�");
		list_online.setModel(onlinemodel);
		list_nottalk.setModel(nottalkemodel);
	}
	//����û��������б�
	public void AddUserToList(String username){
		onlinemodel.addElement(username);
		list_online.setModel(onlinemodel);
	}
	//�Ƴ��û�
	public void delUser(String username){
		onlinemodel.removeElement(username);
	}
	//�Ƴ������б�
	public void delnotalk(String name){
		nottalkemodel.removeElement(name);
	}
	private void initialize(){
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(SeverFrame.class.getResource("/com/gh/res/1.png")));
		setTitle("GGTalk-\u670D\u52A1\u5668");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 602, 414);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIam = new JLabel("I AM MACHINE");
		lblIam.setFont(new Font("΢���ź�", Font.BOLD, 16));
		lblIam.setBounds(224, 0, 138, 22);
		contentPane.add(lblIam);
		
		JScrollPane scrollPane_info = new JScrollPane();
		scrollPane_info.setBounds(463, 20, 113, 295);
		contentPane.add(scrollPane_info);
		
		 list_online = new JList<String>();
		scrollPane_info.setViewportView(list_online);
		
		JScrollPane scrollPane_list = new JScrollPane();
		scrollPane_list.setBounds(10, 20, 335, 295);
		contentPane.add(scrollPane_list);
		
		textArea = new JTextArea();
		scrollPane_list.setViewportView(textArea);
		
		JLabel lblNewLabel = new JLabel("\u4FE1\u606F");
		lblNewLabel.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 5, 54, 15);
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel("\u5728\u7EBF\u5217\u8868");
		label.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		label.setBounds(522, 5, 54, 15);
		contentPane.add(label);
		
		JButton btnT = new JButton("\u8E22Ta\u4E0B\u7EBF\uFF01");
		btnT.setMargin(new Insets(2, 10, 2, 10));
		btnT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//T��
				String selectId=list_online.getSelectedValue();
				if(selectId==null){
					Tools.show(SeverFrame.this, "��ѡ��ҪT���ˣ�");
				}else{
					IoSession idSession = server.getIdSession(selectId);
					idSession.write("�㱻T����");
					onlinemodel.removeElement(selectId);
				}
			}
		});
		btnT.setBackground(Color.WHITE);
		btnT.setForeground(Color.BLACK);
		btnT.setIcon(null);
		btnT.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		btnT.setBounds(483, 325, 93, 23);
		contentPane.add(btnT);
		
		JButton btn_nottalk = new JButton("\u7981\u8A00");
		btn_nottalk.setBackground(Color.WHITE);
		btn_nottalk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//����
				String selectId=list_online.getSelectedValue();
				if(selectId==null||"������".equals(selectId)){
					Tools.show(SeverFrame.this, "��ѡ��Ҫ���Ե��ˣ�");
				}else{
					IoSession idSession = server.getIdSession(selectId);
					idSession.write("�㱻����");
					nottalkemodel.addElement(selectId);
				}
			}
		});
		btn_nottalk.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		btn_nottalk.setBounds(483, 353, 93, 23);
		contentPane.add(btn_nottalk);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(355, 20, 99, 296);
		contentPane.add(scrollPane);
		
		list_nottalk = new JList<String>();
		scrollPane.setViewportView(list_nottalk);
		
		JButton btn_cancel = new JButton("\u89E3\u9664\u7981\u8A00");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//�������
				String selectId=list_nottalk.getSelectedValue();
				if(selectId==null||"�����б�".equals(selectId)){
					Tools.show(SeverFrame.this, "��ѡ��Ҫ������Ե��ˣ�");
				}else{
					IoSession idSession = server.getIdSession(selectId);
					idSession.write("�������");
					nottalkemodel.removeElement(selectId);
				}
			}
		});
		btn_cancel.setBackground(Color.WHITE);
		btn_cancel.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		btn_cancel.setBounds(355, 325, 93, 23);
		contentPane.add(btn_cancel);
		File file=new File("ServerData/");
		if(!file.exists()){
			file.mkdirs();
		}
	}
}
