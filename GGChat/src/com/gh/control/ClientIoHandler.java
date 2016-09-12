 package com.gh.control;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.gh.model.Pepole;
import com.gh.model.SendInfo;
import com.gh.utils.Tools;
import com.gh.view.LoginFrame;
import com.gh.view.RegisterFrame;
import com.gh.view.TalkFrame;

/**
 * �ͻ�����Ϣ�������
 * 
 * @author NIIT
 *
 */
public class ClientIoHandler extends IoHandlerAdapter {
	private RegisterFrame registerFrame;
	private LoginFrame loginFrame;
	private NioSocketConnector client;
	private boolean isPwd = true;
	private TalkFrame talkFrame;
	private String[] sses;



	public ClientIoHandler(LoginFrame loginFrame, NioSocketConnector client) {
		this.loginFrame = loginFrame;
		this.client = client;
	}

	/**
	 * ���ӵ��������� ����
	 */
	@Override
	public void sessionCreated(IoSession session)  throws Exception{
		System.out.println("��ClientIoHandler�Ự������");
	}

	/**
	 * �������ر�ʱ����
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception{

		System.out.println("��ClientIoHandler�Ự����");
		if (isPwd) {
			client.dispose();
			Tools.show(loginFrame, "�������رգ����ӶϿ�");
			talkFrame.dispose();
			System.exit(0);
		}
	}

	/**
	 * �յ���������Ϣ����
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception	 {
		if(message instanceof Pepole){
			Pepole p=(Pepole) message;
			System.out.println("�յ�pepole��Ϣ"+message);
			talkFrame.newInfoFrame(p);
		}
		else if (message instanceof SendInfo) {
			SendInfo sendInfo = (SendInfo) message;
			System.out.println("���������ҷ���:" + message);
			String srcId=sendInfo.getSrcId();
			String descId=sendInfo.getDescId();
			String content=sendInfo.getContent();
			/**
			 * ���յ���Ϣֻ���������
			 * 1 �Ǳ���Ⱥ������ Ҳ����srcId�Ǳ���Ҳ�������ң� descId��������
			 * 2 ��srcId�Ǳ��� descId����
			 * �������˺þ�
			 */
			if(descId.equals("���������û�")){
				//descId��������
				if(srcId.equals(talkFrame.getMyId())){
					//������ҷ�����
					/**
					 * �ҷ��������Լ���talkFrame����ʾ���ˣ�����ע����
					 */
//					talkFrame.addText("���������û�", "���㡿�ԡ����������û���˵��\n"+content);
				}else{
					//�����ҷ���
					talkFrame.addText("���������û�", "��"+srcId+"���ԡ����������û���˵��\n"+content);
				}
			}else{
				//descId����
				talkFrame.addText(srcId, "��"+srcId+"���ԡ��㡿˵��\n"+content);
			}
			
		} else if (message instanceof String) {
			System.out.println("�յ�������String��Ϣ" + message);
			String s = (String) message;
			if (s.startsWith("�����½")) {// �жϱ��
				loginFrame.dispose();
				sses = s.split("~");
				talkFrame = new TalkFrame(sses[1], sses[2], session);// ����������
				talkFrame.setVisible(true);

			} else if ("���Ѿ�����".equals(s)) {
				// �������
				client.dispose();
				Tools.show(loginFrame, "���Ѿ�����");
			}else if ("�����½".equals(s)) {
				client.dispose();
				if (isPwd) {// �������Ͽ����Ӻͷ������Ͽ��������ֿ�
					isPwd = false;
					Tools.show(loginFrame, "��½��������������û���˺���ע�ᣡ");
				}

			} else if ("��Ӻ��ѳɹ�".equals(s)) {
				// �������
				Tools.show(talkFrame, "��Ӻ��ѳɹ�");
				talkFrame.addfiend(talkFrame.getOnlineId());

			} else if ("��Ӻ���ʧ��".equals(s)) {
				Tools.show(talkFrame, "��Ӻ���ʧ�ܣ����Ѳ����ڻ��Ѿ����ڣ�");

			} else if ("ע��ɹ�".equals(s)) {
				//�ر�ע�����
				Tools.show(registerFrame, "ע��ɹ����뷵�ص�½");
				registerFrame.dispose();
				//��ʾ��½����
				loginFrame.setVisible(true);

			} else if ("ע��ʧ��".equals(s)) {
				Tools.show(registerFrame, "ע��ʧ�ܣ���id�Ѵ��ڣ��뻻��id��");

			}else if (s.startsWith("�����б�")) {
				System.out.println("���յ����������ĺ����б�");
				s = s.substring(5);
				String[] split = s.split("~");
				talkFrame.UpdataList(split);// ���º����б�

			} else if (s.startsWith("���ߺ���")) {
				System.out.println("���յ��������������ߺ���");
				s = s.substring(5);
				String[] split = s.split("~");
				talkFrame.UpdataOnlineList(split);// ���������б�

			} else if (s.startsWith("�û�����")) {
				s = s.substring(5);
				talkFrame.delOnlineList(s);// ɾ�������б�
				// �ı���֪ͨ
				talkFrame.addText("���������û�","���������û�" + s + "����");
				
			} else if (s.equals("ɾ�����ѳɹ�")) {
				// ����ɾ��
				Tools.show(talkFrame, "ɾ�����ѳɹ�");
				talkFrame.DelUser(talkFrame.getFriendId());
				
			} else if (s.equals("ɾ������ʧ��")) {
				Tools.show(talkFrame, "Ҫɾ���ĺ��Ѳ�����");
			} else if (s.startsWith("�û�����")) {
				s = s.substring(5);
				talkFrame.addOnlineList(s);
				// �ı���֪ͨ
				if (!s.equals(talkFrame.getMyId()))// �Լ����߲���ʾ
					
					talkFrame.addText("���������û�","���������û�" + s + "����");
			}
//			else if (s.startsWith("�û���Ϣ")) {
//				s = s.substring(5);
//				String[] split = s.split("/");
//				// �����ı���
//				if (!s.equals(talkFrame.getMyId()))
//					talkFrame.addText("�û���" + split[0] + ":\n" + split[1]);
//			}
			else if(s.equals("�û�������")){
				
				talkFrame.addText(talkFrame.getFriendId(),"���������û�������");
			}
			else if(s.equals("�㱻����")){
				System.err.println("�յ�����");
				
				talkFrame.setTalk(false);//����༭
			}else if(s.equals("�㱻T����")){
				System.err.println("�յ���T");
				talkFrame.dispose();//�������
				Tools.show(talkFrame, "�㱻������T��������ϵ����������Ա��");
				System.exit(0);//�̹߳���
			}else if(s.equals("�������")){
				System.err.println("�յ��������");
				talkFrame.setTalk(true);//����༭
			}
		}

	}// ����ĩ
	public void setRegisterFrame(RegisterFrame registerFrame) {
		this.registerFrame = registerFrame;
	}

}
