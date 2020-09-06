package app;

import com.TCPConnection;
import com.TCPConnection.OnMessageListener;

public class Application implements OnMessageListener{
	
	private TCPConnection connection;
	
	public Application() {
		
		connection = TCPConnection.getInstance();
		connection.setListener(this);
		connection.setPuerto(5000);
	}
	
	public void init() {
		connection.start();
		while(true) {
			
		}
	}

	@Override
	public void onmessage(String msg) {
		
	}

}
