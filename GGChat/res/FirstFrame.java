package com.gh.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import  java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.naming.event.EventContext;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

import java.awt.SystemColor;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;



/**
 * 
 * ͨ���̳�JFrameʵ��һ������
 * ***/


public class FirstFrame extends JFrame{
	  private JPanel panel;
	  
	  private JLabel lab_title;
	  private JLabel lab_name;
	  private JLabel lab_psw;
	  private JLabel Image;
	 
	  private JTextField  user;
	  private JPasswordField  pas;
	  
	  private JButton  login;
	  private JButton close;
	  private JButton register;
	  private JButton small;
	  
	  private EventClass even =null;
	  
	  //ȫ�ֵ�λ�ñ��������ڱ�ʾ����ڴ����ϵ�λ��
	  static Point origin=new Point();
	  /*
	   * ���췽��
	   */
	  public FirstFrame(){
		  setTitle("user login");//���ô���
		  setSize(521,296);
		  setLocationRelativeTo(null);//����
		  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  even =new EventClass();
		  
		  //ʵ�������
		  panel=new JPanel();
		  panel.setBorder(BorderFactory.createLineBorder(Color.white, 10, true));
		 // panel.setOpaque(false);
		//  panel.setBorder(BorderFactory.createLineBorder(Color.magenta, 5, true));
		  panel.setLayout(null);//null���֣������Ҫʹ��x,y,width,height����λ��
		  lab_title=new JLabel("  Welcome login GGtalk");
		  lab_title.setBounds(110,30,300,50);
		  
		  //������������
		  panel.add(lab_title);
		  setUndecorated(true);
		  //�����϶����÷���
		  initWindowMoveEvent();
		  
		  //��������
		  lab_title.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		  lab_title.setForeground(new Color(255, 255, 255));
		 // this.getContentPane().setBackground(Color.magenta);
		  
		  
		  lab_name=new JLabel("\u767B\u5F55\u540D\uFF1A ");
		  lab_name.setBackground(Color.WHITE);
		  lab_name.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		  lab_name.setForeground(new Color(182, 69, 25));
		  lab_name.setHorizontalAlignment(SwingConstants.LEFT);
		  lab_name.setBounds(110,136,100,30);
		  panel.add(lab_name);
		  
		  lab_psw=new JLabel("\u5BC6   \u7801 \uFF1A ");
		  lab_psw.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		  lab_psw.setForeground(new Color(182, 69, 25));
		  lab_psw.setBounds(110,176,120,30);
		  panel.add(lab_psw);
		  
		  user = new JTextField();
		  user.setOpaque(false);
		  user.setBackground(SystemColor.textHighlightText);
		  user.setForeground(new Color(255, 102, 0));
		  user.setBounds(190, 137, 200, 30);
		  panel.add(user);
		  
		  pas=new JPasswordField();
		  pas.setOpaque(false);
		  pas.setForeground(new Color(182, 69, 25));
		  pas.setBounds(190,177,200,30);
		  panel.add(pas);
		  
		  login=new JButton("��¼");
		  login.setOpaque(false);
		  login.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		 // login.setBorder(BorderFactory.createLineBorder(Color.white, 2, true));
		  login.setForeground(new Color(182, 69, 25));
		  login.setBackground(Color.WHITE);
		  login.setBounds(190, 228, 80, 30);
	      panel.add( login);
		  
		  //��Ӽ�������
	      login.addActionListener(even);
		  
		  register=new JButton("ע��");
		  register.setOpaque(false);
		  register.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		 // register.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
		  register.setForeground(new Color(182, 69, 25));
		  register.setBackground(Color.WHITE);
		  register.setBounds(303, 228, 87, 30);
		   panel.add(register);
		   //��Ӽ�������
		   register.addActionListener(even);
	
		   close = new JButton("x");
			  close.setBackground(new Color(182, 69, 25));
			// close.setIcon(null);
			  close.setForeground(Color.WHITE);
			//  close.setFont(new Font("΢���ź�", Font.PLAIN, 14));
			  close.setBounds(475, 10, 36, 21);
			 // close.setBorderPainted(false);ʵ�ְ�ť�ޱ߿�
			//  close.setMargin(new Insets(0, 0, 0, 0));// ��ť������߿����
			  close.setBorder(BorderFactory.createLineBorder(Color.white, 1, true));
			  panel.add(close);
			  
			  close.addActionListener(even);//���ü���
		   
			  small = new JButton("-");
			  small.setBackground(new Color(182, 69, 25));
			//  small.setIcon(null);
			  small.setForeground(Color.WHITE);
			//  small.setFont(new Font("΢���ź�", Font.PLAIN, 14));
			  small.setBounds(435, 10, 36, 21);
			//  small.setMargin(new Insets(0, 0, 0, 0));// ��ť������߿����
			  small.setBorder(BorderFactory.createLineBorder(Color.white, 1, true));
			  panel.add(small);
			  
		   
		 
		  //���ô����Ĭ�����
		  setContentPane(panel);
		  
		  Image = new JLabel("");
		  Image.setFont(new Font("΢���ź�", Font.PLAIN, 15));
		  Image.setIcon(new ImageIcon(FirstFrame.class.getResource("/com/gh/res/\u767B\u5F55.jpg")));
		  Image.setBackground(new Color(0, 102, 153));
		  Image.setBounds(1, 1, 520, 295);
		  panel.add(Image);
		  
		 
		 // setUndecorated(true);//ȥ���߿�
		 // close.setOpaque(false);
		  

		  //���ô�����С��
		  small.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	                setExtendedState(JFrame.ICONIFIED);
	            }
	        });
		  
		  
		//  small.addActionListener(even);//���ü���
	  }
	  
	  
	  /**
	   * �����϶��¼�
	   */
	  public void initWindowMoveEvent(){
		  addMouseListener(new MouseAdapter(){
			  //���£�mousePressed ���ǵ����������걻����û��̧��
			  public void  mousePressed(MouseEvent e){
			  //����갴�µ�ʱ���ô��ڵ�ǰ��λ��
			  origin.x=e.getX();
			  origin.y=e.getY();
		  
		  }
		  });
		  
		  addMouseMotionListener(new MouseMotionAdapter(){
			  //�϶���mouseDragged ָ�Ĳ�������ڴ������ƶ�������������϶�
			  
			  public void mouseDragged(MouseEvent e){
				  //������϶�ʱ��ȡ���ڵ�ǰλ��
				  Point FirstFrame=getLocation();
				  //���ô��ڵ�λ��
				  //���ڵ�ǰ��λ��+��굱ǰ�ڴ��ڵ�λ��-��갴�µ�ʱ���ڴ��ڵ�λ��
				  setLocation(FirstFrame.x+e.getX()-origin.x,FirstFrame.y+e.getY()-origin.y);
			  }
			  });
		  }
	  
	  /**
	   * �¼�������
       **/
	  public class EventClass implements ActionListener{
		  @Override
		  public void actionPerformed(ActionEvent e){
			  //�õ��¼�Դ
			  JButton btu=(JButton)e.getSource();
			  if(btu== login){
				  //ȡ���ı����������ֵ
				  String name=user.getText();
				  String pwd=new String(pas.getPassword());
				  
				  //�жϵ�¼�Ƿ�ɹ� 
				  if("chen".equals(name)){
					  dispose();
					  
					//  JOptionPane.showMessageDialog(FirstFrame.this,"Congratulations to login successfully ");
					//  JOptionPane.showMessageDialog(FirstFrame.this, " ", "welcome to GGtalk", JOptionPane.INFORMATION_MESSAGE);
				  }else{
					  pas.setText("");
					  JOptionPane.showMessageDialog(FirstFrame.this,"Password error, login failed��");
				  }
				 
			  }else if(btu==register){
				  JOptionPane.showMessageDialog(FirstFrame.this,"welcome register");
				  
			  }else if(btu==close){
				  setVisible(false);//���ش���
				  System.exit(0); //�˳�����
			  } 
			  
			 
		  }
	  }
	  
	  /**
	   * �������
	   * @param args
	   * */
	  public static void main(String[] args){
		  EventQueue.invokeLater(new Runnable(){
			  public void run(){
				  try{
					  FirstFrame window=new FirstFrame();
					  //��ʾ����
					  window.setVisible(true);
				  }catch( Exception e){
					  e.printStackTrace();
				  }
			  }
		  });
		  new FirstFrame();
	  }
}
