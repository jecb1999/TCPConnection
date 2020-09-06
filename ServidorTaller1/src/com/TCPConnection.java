package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPConnection extends Thread{

	private static TCPConnection instance = null;
	
	private TCPConnection() {

	}
	
	public static synchronized TCPConnection getInstance() {
		if(instance == null) {
			instance = new TCPConnection();
		}
		return instance;
	}
	
	private ServerSocket server;
	private Socket socket;
	private int puerto;
	
	public OnMessageListener listener;
	
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	@Override
	public void run() {
		
		try {
			server = new ServerSocket(puerto);
			System.out.println("Esperando....");
			socket = server.accept();
			System.out.println("Conectado");
			
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			OutputStream os = socket.getOutputStream();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
			
			InetAddress myAddress = InetAddress.getLocalHost();
			NetworkInterface net = NetworkInterface.getByInetAddress(myAddress);
			
			while(true) {
				
				String msg = br.readLine();
				listener.onmessage(msg);

				
				if(msg.equalsIgnoreCase("remoteIpconfig")) {
					String answer = myAddress.getHostAddress();
					bw.write(answer+"\n");
					bw.flush();
				}else if(msg.equalsIgnoreCase("interface")) {
					String answer = net.getName();
					bw.write(answer+"\n");
					bw.flush();
				}else if(msg.equalsIgnoreCase("whatTimeIsIt")) {
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Date date = new Date();
					String answer = dateFormat.format(date);
					bw.write(answer+"\n");
					bw.flush();
				}else if(msg.length() == 1024){
					bw.write(msg+"\n");
					bw.flush();
				}else if(msg.length() == 8192) {
					bw.write(msg+"\n");
					bw.flush();
				}
				else {
					String answer = "comando no valido";
					bw.write(answer+"\n");
					bw.flush();
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setListener(OnMessageListener listener) {
		this.listener = listener;
	}
	
	public interface OnMessageListener{
		public void onmessage(String msg);
	}

}