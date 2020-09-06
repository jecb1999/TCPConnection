package app;

import com.Cliente;
import com.Cliente.OnListener;

public class Application implements OnListener{
	
	private Cliente cliente;
	
	public Application() {
		cliente = new Cliente();
		cliente.setListener(this);
	}
	
	public void init() {
		cliente.start();
	}

	@Override
	public void listen(String m) {
//		System.out.println("El mensaje enviado fue: "+" "+m);
		
	}

	@Override
	public void recibido(String m) {
		System.out.println(m);
		
	}
	

}
