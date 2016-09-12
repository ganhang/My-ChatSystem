package com.gh.control;

import java.net.SocketAddress;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.gh.model.AddInfo;
import com.gh.model.DelInfo;
import com.gh.model.LoginInfo;
import com.gh.model.Pepole;
import com.gh.model.SendInfo;
import com.gh.utils.Tools;
import com.gh.view.SeverFrame;

/**
 * ��������Ϣ�������
 * 
 * @author NIIT
 *
 */
public class ServerIOHandler extends IoHandlerAdapter {

	// �洢���еĿͻ���
	private List<IoSession> clients = new ArrayList<>();// ��ǰ����
	private HashMap<String, String> linksInfo = new HashMap<String, String>();// �������ַ
	private HashMap<String, IoSession> idAndSession = new HashMap<String, IoSession>();// �������ַ
	private ArrayList<Pepole> pepoles = new ArrayList<Pepole>();
	private ArrayList<String> onlinePepoles = new ArrayList<String>();// �����û�
	private LoginInfo lgi;
	private SeverFrame severFrame;

	public ServerIOHandler(SeverFrame severFrame) {
		this.severFrame = severFrame;
	}

	/**
	 * �пͻ������� ����
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// ���
		clients.add(session);
		System.out.println(session.getRemoteAddress() + "���ӷ�����");
		// �����Ự������ �ͻ��˵�ַ
	}

	/**
	 * �ͻ��˹رջỰ ����
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// �����Ự������ �ͻ��˵�ַ
		SocketAddress address = session.getRemoteAddress();
		System.out.println("��ServerIOHandler�Ự�˳���" + address);// �������
		// nowp.setOnline(false);//���õ�ǰ�û�״̬
		if (linksInfo.get(session.getRemoteAddress().toString()) != null) {// �ų��������Ӻ�ע����������ʱ��ʾ
			severFrame.addText(linksInfo.get(session.getRemoteAddress()
					.toString()) + "����");// �����ı�����Ϣ
			severFrame.delUser(linksInfo.get(session.getRemoteAddress()
					.toString()));// �ڷ���� �����û��б���ɾ��
			severFrame.delnotalk(linksInfo.get(session.getRemoteAddress()
					.toString()));//�ڽ����б�ɾһ��
			// ���͸����˸������ߺ���
			onlinePepoles.remove(linksInfo.get(session.getRemoteAddress()
					.toString()));
			// linksInfo.remove(session.getRemoteAddress().toString());//��ϵ�Ƴ�
			idAndSession.remove(linksInfo.get(session.getRemoteAddress()
					.toString()));// id��session��ϵ�Ƴ�
			sendMessageToAllClient("�û�����~"
					+ linksInfo.get(session.getRemoteAddress().toString()));
		}
		// ɾ������
		clients.remove(session);

	}

	/**
	 * �յ��ͻ�����Ϣ ����
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception	{
		// �ͻ��˵�ַ
		SocketAddress address = session.getRemoteAddress();
		System.err.println("�������յ���Ϣ:"+message);
		// ����ǵ�½��Ϣ
		if (message instanceof LoginInfo) {
			lgi = (LoginInfo) message;
			// ���ļ�������ݿ���Ϣ
			pepoles = Tools.readFile();
			boolean flag = true;// ��֤��½��ǣ�true��ʾ�������
			for (Pepole p : pepoles) {
				if (lgi.getId().equals(p.getId())
						&& lgi.getPwd().equals(p.getPassword())) {
					// �ж��ǲ����Ѿ���½
					boolean isOnline = false;
					for (String onlineId : onlinePepoles) {
						if (p.getId().equals(onlineId)) {
							isOnline = true;
						}
					}
					if (isOnline) {//�������
						session.write("���Ѿ�����");
						flag = false;
						break;
					} else {
						session.write("�����½~" + p.getName() + "~" + p.getId());// ����һ��ͨ���ı�Ǽ��û����������ڿͻ�����ʾ�û�����
						flag = false;// �����֤ͨ��
						onlinePepoles.add(p.getId());// ��ӵ�������
						linksInfo.put(address.toString(), p.getId());// ��ӵ�ַ��idӳ��
						idAndSession.put(p.getId(), session);// ���id��session��ӳ��
						severFrame.addText(p.getId() + "����");// ����������ʾ�û���¼
						severFrame.AddUserToList(p.getId());// �����������б���ʾid
						// ������Ϣ���������ߵ��û�
						sendMessageToAllClient("�û�����~" + p.getId());
						break;
					}
				}
			}
			if (flag) {
				session.write("�����½");// �������
			}
			// �������Ӻ�����Ϣ
		} else if (message instanceof AddInfo) {
			AddInfo addInfo = (AddInfo) message;
			// ���ı��в����ǲ��������id
			pepoles = Tools.readFile();
			// �Ƿ��Ѿ����Ϊ����
			boolean flag = true;
			for (Pepole p : pepoles) {
				if (p.getId().equals(addInfo.getFriendId())) {
					flag = false;
					if (Tools.writeFile(addInfo)) {
						session.write("��Ӻ��ѳɹ�");
						System.out.println("д��ɹ�");
					} else {
						session.write("��Ӻ���ʧ��");
						System.out.println("д��ʧ��");
					}
				}
			}
			if (flag) {
				session.write("��Ӻ���ʧ��");
				System.out.println("д��ʧ��");
			}
			// �����ע����Ϣ
		} else if (message instanceof Pepole) {
			Pepole p = (Pepole) message;
			if (Tools.writeFile(p)) {
				session.write("ע��ɹ�");
			} else {
				session.write("ע��ʧ��");
			}
			// ������ַ���
		} else if (message instanceof String) {
			String s = (String) message;
			if ("���Һ���".equals(s)) {
				System.out.println("�������յ���ѯ"
						+ linksInfo.get(session.getRemoteAddress().toString())
						+ "�ĺ���");
				ArrayList<Pepole> friends = Tools.readUseFriendFile(linksInfo
						.get(session.getRemoteAddress().toString()));
				String friendsName = "�����б�~";
				for (Pepole p : friends) {
					friendsName += p.getId() + "~";// ���ͺ��ѵ�id
				}
				friendsName = friendsName
						.substring(0, friendsName.length() - 1);// ȥ�����һ��/
				session.write(friendsName);
			} else if ("�������ߺ���".equals(s)) {
				String onlineFriends = "���ߺ���~";
				for (String op : onlinePepoles) {
					onlineFriends += op + "~";
				}
				onlineFriends = onlineFriends.substring(0,
						onlineFriends.length() - 1);
				session.write(onlineFriends);
			}else if(s.startsWith("������Ϣ")){
				s=s.substring(5);
				ArrayList<Pepole> use = Tools.readFile();
				for(Pepole p:use){
					if(p.getId().equals(s)){
						session.write(p);
					}
				}
			}
			
			
		} else if (message instanceof DelInfo) {
			DelInfo delInfo = (DelInfo) message;
			if (Tools.writeFile(delInfo)) {
				// true �����ļ�������ɾ���ɹ�
				// false �����ļ�û����id �� ��id����Ϊ�գ��ļ�������
				session.write("ɾ�����ѳɹ�");
				System.out.println("ɾ�����ѳɹ�");
			} else {
				session.write("ɾ������ʧ��");
				System.out.println("ɾ������ʧ��");
			}
		} else if (message instanceof SendInfo) {
			SendInfo sendInfo = (SendInfo) message;
			if (sendInfo.getDescId().equals("���������û�")) {// ���Ŀ��id��������
				System.err.println("������ת��" + sendInfo + "��������");
				severFrame.addText("������ת����Ϣ" + sendInfo + "��"+ sendInfo.getDescId());
				sendMessageToAllClient(sendInfo);
			} else {// ���͵�������
					// ����ӳ��id��sessionӳ���õ�session
				System.err.println("������ת����Ϣ" + sendInfo + "��"+ sendInfo.getDescId());
				
				severFrame.addText("������ת����Ϣ" + sendInfo + "��"+ sendInfo.getDescId());
				if (idAndSession.get(sendInfo.getDescId()) != null)// ���Ŀ��id����
					idAndSession.get(sendInfo.getDescId()).write(sendInfo);
				else {
					// Ŀ�겻����
					session.write("�û�������");
				}
			}
		}
	}

	/**
	 * ������Ϣ�����пͻ���
	 * 
	 * @param msg
	 */
	public void sendMessageToAllClient(Object msg) {
		for (IoSession session : clients) {
			session.write(msg);
		}
	}
	/**
	 * ����id��session
	 * @param id
	 * @return
	 */
	public IoSession getIdSession(String id) {
		return idAndSession.get(id);
	}
}
