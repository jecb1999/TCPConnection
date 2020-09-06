package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente extends Thread{
	
	private OnListener listener;
	
	public Cliente() {
		this.listener =  listener;
	}
	
	public void setListener(OnListener ol) {
		this.listener = ol;
	}
	
	
	public void run() {
		try {
			System.out.println("Conectando.....");
			Socket socket = new Socket("127.0.0.1",5000);
			System.out.println("Conectado");
			
			OutputStream out = socket.getOutputStream();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			InputStream is = socket.getInputStream();
			InputStreamReader isr= new InputStreamReader(is);
			BufferedReader breadre = new BufferedReader(isr);
			
			Scanner scanner = new Scanner(System.in);//poner en app
			
			while(true) {
				
				String line = scanner.nextLine()+"\n";
				if(line.equalsIgnoreCase("RTT"+"\n") ) {
					byte[] rtt = new byte[1024];
					String msgrtt = new String(rtt);
					long t1 = System.nanoTime();;
					bw.write(msgrtt+"\n");
					bw.flush();
					String read = breadre.readLine();
					long t2 = System.nanoTime();//poner nanos
					if(read.length()==1024) {
						long resultado = t2-t1;
						String mensaje = "El tiempo fue"+": "+resultado;
						listener.recibido(mensaje);
					}else {
						String mensaje = "Error no llego el mismo mensaje";
						listener.recibido(mensaje);
					}
					
				}else if(line.equalsIgnoreCase("speed"+"\n")){
					byte[] speed = new byte[8192];
					String mspeed= new String(speed);
					long t1 = System.currentTimeMillis();//nanos
					bw.write(mspeed+"\n");
					bw.flush();
					String read = breadre.readLine();
					long t2 = System.currentTimeMillis();
					if(read.length()==mspeed.length()) {
						long resultado = (t2-t1)/1000;
						double KB = (mspeed.length()*2);
						double velocidad = (KB/resultado);
						if(velocidad == Double.POSITIVE_INFINITY) {
							String mensaje = "Valor incalculable";
							listener.recibido(mensaje);
						}else {
						String mensaje = "La velocidad fue"+": "+velocidad+"KB/s";
						listener.recibido(mensaje);
						}
					}else {
						listener.recibido("Error no llego el mismo mensaje");
					}
					
				}else {
				bw.write(line);
				bw.flush();
				listener.listen(line);
				
				String read = breadre.readLine();
				listener.recibido(read);
				}
				
			}
			
			
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		

	}
	public interface OnListener {
		public void listen(String m);
		public void recibido(String m);
	}
}



