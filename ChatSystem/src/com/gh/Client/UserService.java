package com.gh.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.gh.model.Info;
import com.gh.model.LoginInfo;

public class UserService {
	private LoginInfo logininfo = null;
	private ClientFrame clientframe = null;
	private boolean flag = true;
	private Info getInfo = null;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	private ObjectOutputStream bw;
	private ObjectInputStream br;
	public void login(final String username, final String password, final JFrame frame) {
		//������Ϣ�߳�
		Thread t=new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Socket sk = new Socket("192.168.1.102", 8000);
					//���ﱻ���˺þã�����Ҫ�ȴ���bw�ٴ���br,��Ϊscoket����ͻ��˺ͷ����ͬʱ�������������߻����Scoketͨ��
					bw =new ObjectOutputStream(sk.getOutputStream());
					br = new ObjectInputStream(sk.getInputStream());
					logininfo = new LoginInfo(username, password);
					bw.writeObject(logininfo);//����������͵�¼��Ϣ
					bw.flush();
					//�ӷ�����յ�¼��Ϣ
					LoginInfo objflag = (LoginInfo) br.readObject();
					if (objflag.isFlag()) {// ������Ե�¼
						// �����������
						clientframe = new ClientFrame(logininfo.getName(),UserService.this);
						// ���������ʾ��ӭ��Ϣ
						clientframe.updateText("����������ӭ��¼��");
						frame.dispose();
						// �ȴ�������Ϣ 
						while (isFlag()) {
							getInfo = new Info();
							getInfo = (Info) br.readObject();
							switch (getInfo.getInfoType()) {
							case SEND_INFO:
								clientframe.updateText("�û���"+getInfo.getFromUser()+"˵��"+getInfo.getContent());
								break;
							case ADD_USER:
								clientframe.AddUserToList(getInfo.getContent());
								break;
							case EXIT:
								clientframe.DelUser(getInfo.getFromUser());
								clientframe.updateText("���������û�"+getInfo.getFromUser()+"������");
								break;
							default:
								break;
							}

						}
					} else {
						JOptionPane.showMessageDialog(frame, "�û������������");
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, "�����������쳣");
					 //e.printStackTrace();
				}
			}
		});
		t.start();
	}
	//������Ϣ�߳�
	public void send(Info info){
		try {
			bw.writeObject(info);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
