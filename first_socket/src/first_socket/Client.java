package first_socket;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements Runnable {

	
	public static final String HOST = "localhost";
	
	public static final int PORT = 10000;
	
	private JTextField tf;
	private JTextArea ta;
	private JScrollPane sp;
	private JPanel pn;
	private JButton bt;
	
	private Socket sc;
	private BufferedReader br;
	private PrintWriter pw;
	
	public static void main (String[]args) {
		Client cl = new Client();
	}
	
	public Client() {
		
		super("Client Filed");
		
		tf = new JTextField();
        ta = new JTextArea();
        sp = new JScrollPane(ta);
        pn = new JPanel();
        bt = new JButton("Send");
        
        pn.add(bt);
        add(tf, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(pn, BorderLayout.SOUTH);

        bt.addActionListener(new SampleActionListener());
        addWindowListener(new SampleWindowListener());

        setSize(400,300);
        setVisible(true);

        //Threadクラスのインスタンスを作成・実行
        //ここからソケット通信用のスレッドが作成され，通信が開始します．
        Thread th = new Thread(this);
        th.start();
	}
	
	public void run() {
		try {
			sc = new Socket(HOST,PORT);
			br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sc.getOutputStream())));
			
			while(true) {
				try {
					String str = br.readLine();
					ta.append(str + "\n");
				}catch(Exception e) {
					br.close();
					pw.close();
					sc.close();
					break;
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public class SampleActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				String str = tf.getText();
				pw.println(str);
				ta.append(str + "\n");
				pw.flush();
				tf.setText("");
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	class SampleWindowListener extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}
