package socket4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient {
	public static void main(String args[]) {

		
		BufferedReader br = null;
		PrintWriter pw = null;
		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			String name = "[" + args[0] + "] ";

			socket = new Socket("127.0.0.1", 9001);
			System.out.println("Á¢¼ÓµÊ..");
			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

			in = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			out = socket.getOutputStream();
			pw = new PrintWriter(new OutputStreamWriter(out));
			new receive(socket, br).start();
			
			String message = null;
			while ((message = keyboard.readLine()) != null) {
				if (message.equals("quit"))
					break;	
				pw.println(name + message);
				pw.flush();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(br != null) br.close();
				if(pw != null) pw.close();
				if(socket != null) socket.close();
				if(in != null) in.close();
				if(out != null) out.close();
			} catch (IOException e) {
				
			}

		}

	}
	static class receive extends Thread {
		Socket socket;
		BufferedReader br;

		public receive(Socket socket, BufferedReader br) {
			this.socket = socket;
			this.br = br;
		}

		public void run() {
			String msg = null;
			try {
				while ((msg = br.readLine()) != null) {
					System.out.println(msg);
				}
			} catch (Exception e) {

			} finally {

			}
		}

	}
}


