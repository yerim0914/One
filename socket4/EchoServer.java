//Broadcast
package socket4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class EchoServer {
	public static void main(String args[]){
		Socket socket = null;
		ServerSocket server = null;
		List<Socket> list = new ArrayList<>();
		try {
			server = new ServerSocket(8000);
			System.out.println("클라이언트의 접속을 대기중");
			
			while(true){
			socket = server.accept();
			list.add(socket);
			new EchoThread(socket, list).start();
			}
			
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (Exception e){
			System.out.println(e.getMessage());
		} finally{
		
			try {
				if(socket != null) socket.close();
				if(server != null) server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
class EchoThread extends Thread{
	Socket socket;
	List<Socket> list;
	int count;
	public EchoThread() {
		super();
	}
	public EchoThread(Socket socket, List<Socket> list) {
		super();
		this.socket = socket;
		this.list   = list;
	}
	@Override
	public void run() {
		
		InetAddress address = socket.getInetAddress();
		System.out.println(address.getHostAddress()+" 로부터 접속했습니다.");
		System.out.println("현재 접속자는 " + list.size() + "명");
		
		try{
		InputStream in = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		
		String message = null;
		while((message = br.readLine())!= null){
			broadcast(message);
		}
		br.close();
		} catch(IOException e){
			
		} finally{
			try {
				if(socket != null) socket.close();
				System.out.println("!접속해제!");
				list.remove(socket);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
	}
	public synchronized void broadcast(String msg) throws IOException{
		for(Socket socket : list){
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			pw.println(msg);
			pw.flush();
		}
	}
}

