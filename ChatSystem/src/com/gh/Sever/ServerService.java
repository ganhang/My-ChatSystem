package com.gh.Sever;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gh.model.Info;
import com.gh.model.LoginInfo;
import com.gh.util.EnumInfoType;

/**
 * �������ͻ��˼���
 * 
 * @author ganhang
 */
public class ServerService {
	private Info currInfo = null;
	private ServerFrame serFrame;
	private boolean flag;
	private UserServerThread ust;
	ExecutorService es = Executors.newScheduledThreadPool(1);
	// �������������û������߳�
	private Vector<UserServerThread> userThreads;
	public ServerService(ServerFrame serFrame) {
		this.serFrame = serFrame;
	}
	public void startSever() {
		flag = true;
		try {
			ServerSocket server = new ServerSocket(8000);
			userThreads = new Vector<UserServerThread>();
			while (flag) {
				Socket client = server.accept();
				System.out.println("Server�����û�����");
				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
				LoginInfo logininfo = (LoginInfo) ois.readObject();// ��ȡ�û��ʺ�����
				System.out.println(logininfo);
				// ��¼��֤
				if (UserLogin.islogin(logininfo.getName(), logininfo.getPwd())) {
					logininfo.setFlag(true);
					oos.writeObject(logininfo);// ����ȷ�ϵ�¼��Ϣ
					oos.flush();
					// ���浱ǰ�û���Ϣ
					currInfo = new Info();
					currInfo.setFromUser(logininfo.getName());
					// �ڷ���������ʾ�û���¼��Ϣ
					serFrame.updateText("�û�" + logininfo.getName() + "--�����ߣ�");
					// �����û������߳�
					UserServerThread ust = new UserServerThread(currInfo, ois, oos);// �����û��������
					userThreads.add(ust);// ��ӵ����̳�
					es.execute(ust);// �����߳�
				} else
					oos.writeObject(logininfo);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �û������߳�
	 * 
	 * @author ganhang
	 */
	class UserServerThread implements Runnable {
		private Info info = null;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;

		public UserServerThread(Info info, ObjectInputStream ois, ObjectOutputStream oos) {
			this.info = info;
			this.oos = oos;
			this.ois = ois;
		}

		@Override
		public void run() {
			// ���·������������û��б�
			serFrame.AddUserToList(currInfo.getFromUser());
			// �Ȼ�������û����б�
			StringBuffer sb = new StringBuffer();
			for (UserServerThread u : userThreads) {
				sb.append(u.info.getFromUser()).append(",");
			}
			System.out.println(sb.toString());
			// �������̳߳���ÿ���û�����һ���Լ��������û��б�
			for (UserServerThread usts : userThreads) {
				try {
					currInfo.setInfoType(EnumInfoType.ADD_USER);
					currInfo.setContent(sb.toString());
					usts.oos.writeObject(currInfo);// ע��Ҫд�Ǹ��̵߳������
					usts.oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// �ȴ������û���Ϣ
			System.out.println(flag);
			new Thread(new Wait(ois, oos)).start();

		}
		public void send(Info sendinfo) throws IOException {
			oos.writeObject(sendinfo);
			oos.flush();
		}
	}

	// ������Ϣ
	class Wait implements Runnable {
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private boolean wait = true;

		public Wait(ObjectInputStream ois, ObjectOutputStream oos) {
			this.oos = oos;
			this.ois = ois;
		}

		@Override
		public void run() {
			while (wait) {
				try {
					// ������Ϣ�����ؿ��߳���Ϊ��ѵ�ǰ�߳�����
					Info info = (Info) ois.readObject();
					System.out.println(Thread.currentThread().getName() + "�յ�һ��send��Ϣ");
					System.out.println(info);
					switch (info.getInfoType()) {
					case SEND_INFO:
						if (info.getToUser().equals("������")) {
							for (UserServerThread user : userThreads) {
								//�������׷����ˣ�ע��ÿ���û�����һ�������̣߳��ĸ��û�Ҫ������Ϣ�������ķ����߳�ȥ�������������û��ķ����̷߳����ˡ�
								if (!info.getFromUser().equals(user.info.getFromUser()))
									user.send(info);
								System.out.print(user.info.getFromUser() + "/");
							}
							System.out.println("��Ϣ��������");
						} else {
							for (UserServerThread user : userThreads) {
								if (info.getToUser().equals(user.info.getFromUser())) {
									user.send(info);
								}
							}
						}
						break;
					case EXIT:
						serFrame.DelUser(info.getFromUser());
						serFrame.updateText("�û�" + info.getFromUser() + "������");
						for (UserServerThread user : userThreads) {
							if (!info.getFromUser().equals(user.info.getFromUser())) {
								user.send(info);
							} 
							//����ust�����������¼���û����߳���Ϊһֱ�ڱ����ǣ�
							//��Ҫ����д�뵱ǰҪɾ��
							else ust = user;
							System.out.print(user.info.getFromUser() + ",");
						}
						System.out.println("�˳���������");
						// ɾ���������ϵĸ������û�
						wait = false;
						System.out.println("�Ƴ����û���" + ust.info.getFromUser());
						//��������ɾ���û��߳�
						userThreads.remove(ust);
						break;
					default:
						break;
					}
				} catch (ClassNotFoundException | IOException e) {
					 e.printStackTrace();
				}

			}
		}
	}

}
