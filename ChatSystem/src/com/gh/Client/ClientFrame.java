package com.gh.Client;

import java.awt.Toolkit;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.gh.model.Info;
import com.gh.util.DateUtil;
import com.gh.util.EnumInfoType;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame {

	private JFrame Clientframe;
	private JTextField textField;
	private String username;
	private JTextArea textArea = null;
	private JList<String> list = null;
	private DefaultListModel<String> model = null;
	private UserService us;

	// ��ʼ���û��б�
	public void InitUserList() {
		model = new DefaultListModel<String>();
		model.addElement("������");
		list.setModel(model);
	}

	// ����û��������б�
	public void AddUserToList(String username) {
		// ���˶�ǽ����ǽ������֮ǰ��ӵ�Ҫȫɾ�ˣ������һ��
		model.removeAllElements();
		model.addElement("������");
		String[] str = username.split(",");
		for (String s : str) {
			model.addElement(s);
		}
		//list.setModel(model);
	}

	public void updateText(String text) {
		StringBuffer sb = new StringBuffer();
		sb.append(textArea.getText()).append(DateUtil.getTime() + "\n").append(text).append("\n");
		textArea.setText(sb.toString());
	}
	public void DelUser(String username){
		model.removeElement(username);
	}

	/**
	 * Create the application.
	 */
	public ClientFrame(String username,UserService us) {
		this.username = username;
		this.us=us;
		initialize();
		// ��ʼ���û��б�
		InitUserList();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Clientframe = new JFrame();
		Clientframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Info info=new Info();
				info.setFromUser(username);
				info.setInfoType(EnumInfoType.EXIT);
				us.send(info);
				us.setFlag(false);
				Clientframe.dispose();
			}
		});
		Clientframe.setVisible(true);
		Clientframe
				.setIconImage(Toolkit.getDefaultToolkit().getImage(ClientFrame.class.getResource("/com/gh/res/1.png")));
		Clientframe.setTitle("\u804A\u804A-\u5BA2\u6237\u7AEF");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int x = (int) (d.getWidth() - 534) / 2;
		int y = (int) (d.getHeight() - 383) / 2;
		Clientframe.setBounds(x, y, 534, 383);
		Clientframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Clientframe.getContentPane().setLayout(new BorderLayout(0, 0));

		JLabel label = new JLabel("��ǰ�û���:" + username);
		Clientframe.getContentPane().add(label, BorderLayout.NORTH);

		JPanel jpanel = new JPanel();
		Clientframe.getContentPane().add(jpanel, BorderLayout.EAST);
		jpanel.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("--\u5728\u7EBF\u7528\u6237\u5217\u8868--");
		jpanel.add(lblNewLabel, BorderLayout.NORTH);

		list = new JList<String>();
		jpanel.add(list, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		Clientframe.getContentPane().add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JPanel panel = new JPanel();
		Clientframe.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_1 = new JLabel("\u8BF7\u8F93\u5165\uFF1A");
		panel.add(lblNewLabel_1, BorderLayout.WEST);

		textField = new JTextField();
		panel.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);

		JButton button = new JButton("\u53D1\u9001");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��ȡ������Ϣ
				String sendContent = textField.getText().trim();
				// ��ȡ���Ͷ���
				Info info = new Info();
				info.setFromUser(username);
				info.setToUser((String) list.getSelectedValue());
				info.setContent(sendContent);
				info.setInfoType(EnumInfoType.SEND_INFO);
				System.out.println(info.getToUser());
				// �����жϷ��Ͷ����Ƿ�Ϊ��
				if ("".equals(info.getToUser()) || info.getToUser() == null) {
					JOptionPane.showMessageDialog(Clientframe, "��ѡ���Ͷ���");
					return;
				} else if (info.getToUser().equals(username)) {
					JOptionPane.showMessageDialog(Clientframe, "���ܶ��Լ�����");
					return;
				} else if (info.getContent().equals("") || info.getContent() == null) {
					JOptionPane.showMessageDialog(Clientframe, "���ݲ���Ϊ��");
					return;
				} else
					us.send(info);
				textField.setText("");
			}
		});
		panel.add(button, BorderLayout.EAST);
	}

}
