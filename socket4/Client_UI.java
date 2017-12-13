package socket4;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class Client_UI {
	JFrame f = new JFrame("클라이언트");
	Button b1, b2;
	TextField ip, port, id, msg;
	static TextArea ta;
	
	BufferedReader br = null;
	PrintWriter pw = null;
	Socket socket = null;
	InputStream in = null;
	OutputStream out = null;
	Client_UI(){
		Panel p1= new Panel();
		p1.add(ip = new TextField("70.12.109.63"));
		p1.add(port = new TextField("8000"));
		p1.add(b1 = new Button("연결"));
		p1.add(b2 = new Button("종료"));
		f.add(p1, BorderLayout.NORTH);
		f.add(ta = new TextArea(25, 30));
		Panel p2 = new Panel();
		p2.add(id = new TextField());
		p2.add(msg = new TextField(35));
		f.add(p2, BorderLayout.SOUTH);
		f.setSize(500, 350);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		b1.addActionListener(new ActionListener() { // 연결
			public void actionPerformed(ActionEvent e) {
				//String name = id.getText();
				try {
					socket = new Socket(ip.getText(),9000);
					System.out.println("접속됨..");
					in = socket.getInputStream();
					br = new BufferedReader(new InputStreamReader(in));
					new receive(socket, br, ta).start();
				} catch (UnknownHostException e1) {
				} catch (IOException e1) {
				}
			}
		});
		msg.addActionListener(new ActionListener() { // message enter
			public void actionPerformed(ActionEvent e) {
				try {
					out = socket.getOutputStream();
					pw = new PrintWriter(new OutputStreamWriter(out));
					pw.println(id.getText() +": "+ msg.getText());
					pw.flush();
					msg.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		b2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						if(socket != null) socket.close();
						if(br != null) br.close();
						if(pw != null) pw.close();
						if(out != null) out.close();
						
					} catch (IOException e1) {
					}
				
			}
		});
	}
public static void main(String args[]) {

		Client_UI ui = new Client_UI();
}
class receive extends Thread {
	Socket socket;
	BufferedReader br;
	TextArea ta;
	String save;
	public receive(Socket socket, BufferedReader br, TextArea ta) {
		this.socket = socket;
		this.br = br;
		this.ta = ta;
	}

	public void run() {
		String msg = null;
		try {
			while ((msg = br.readLine()) != null) {
				//save += msg+"\n";
				ta.append(msg+"\n");
			}
		} catch (Exception e) {

		} finally {

		}
	}

}
}

