package first_socket;

import java.io.*;
import java.net.*; 
import java.util.*;

public class Server {

	public static final int PORT = 10000;
	
	public static void main(String[]args) {
		Server sm = new Server();
		try {
			ServerSocket ss = new ServerSocket(PORT);
			
			System.out.println("Waiting!");
			
			while(true) {
				try {
					
					Socket sc = ss.accept();
					System.out.println("Welcome!");
					
					ConnectToClient cc = new ConnectToClient(sc);
					cc.start();
				}catch(Exception ex) {
					ex.printStackTrace();
					break;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

class ConnectToClient extends Thread{
	private Socket sc;
	private BufferedReader br;
	private PrintWriter pw;
	
	public ConnectToClient(Socket s)
    {
        sc = s;
        
    }
	
	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sc.getOutputStream())));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				String str = br.readLine();
				System.out.print(str);
				Random rnd = new Random();
				RamdomStrings rs = new RamdomStrings();
				
				pw.println("Server : [" + str.charAt(str.length()-1) + rs.GetRandomString(rnd.nextInt(10)) + "] (^_^)!");
                //ここが重要！flushメソッドを呼ぶことでソケットを通じてデータを送信する
                pw.flush();
			}catch(Exception e) {
				try {
					br.close();
					pw.close();
					sc.close();
					System.out.println("Good Bye");
					break;
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}

class RamdomStrings{
	private final String stringchar = "aaa";
	private Random rnd = new Random();
	private StringBuffer sbf = new StringBuffer(15);
	
	public String GetRandomString(int cnt) {
		for(int i = 0; i<cnt; i++) {
			int val = rnd.nextInt(stringchar.length());
			sbf.append(stringchar.charAt(val));
		}
		return sbf.toString();
	}
}
