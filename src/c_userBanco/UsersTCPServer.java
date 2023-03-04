package c_userBanco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersTCPServer {
	
	public static final int PORT = 3400;
	private ServerSocket listener;
	private Socket serverSideSocket;
	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;
	
	public HashMap<String, Cliente> mapaUsuarios = new HashMap<String,Cliente>();
	private String nombre;
	private String answer;
	
	public static void main(String[] args) throws Exception {
		UsersTCPServer es = new UsersTCPServer();
		while(true) {
			es.init(); 
		}
		
	}
	
	
	public UsersTCPServer() {
		System.out.println("Echo TCP server is runnig on port: " + PORT);
	}

	private void init() throws Exception {
		listener = new ServerSocket(PORT);
		
		while(true) {
			serverSideSocket = listener.accept();
			createStreams(serverSideSocket);
			protocol(serverSideSocket);
		}
		
	}

	
	private void protocol(Socket socket) throws Exception{
		String message = fromNetwork.readLine();
		
		System.out.println("Recibido "+message);
		System.out.println("[Server] from cleint: " + message);
		opcionBanco(message);
		
		
	}

	private void opcionBanco(String message) throws IOException {
		String nombre;
		int id, pin;
		
		Boolean repetido = false;
		
		String opcion = message.split(",")[0];
		
		if(opcion.equalsIgnoreCase("crear")) {
			crearCuenta(message);	
		}else if(opcion.equalsIgnoreCase("Ingresar")) {
			agregarSalgo(message);
		}else if(opcion.equalsIgnoreCase("Consultar")) {
			consultarCuenta(message);
		}else if(opcion.equalsIgnoreCase("retirar")) {
			retirarSaldo(message);
		}
		
		toNetwork.println(answer);
	}


	private void createStreams(Socket socket) throws Exception{
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
	}
	
	private void crearCuenta(String message) {
		nombre = message.split(",")[1];
		Cliente caux = new Cliente(nombre);
		int id = Integer.parseInt(message.split(",")[2]);
		int pin = Integer.parseInt(message.split(",")[3]);
		
		boolean repetido = verificarDuplicado(nombre);

		if(!repetido) {
			caux.setCuentaBancaria("cuenta - "+caux.getNombre());
			caux.setId(id);
			caux.setPin(pin);
			caux.setSaldo(0);
			mapaUsuarios.put(caux.getNombre(), caux);
			answer = "cuenta creada mi papa";
		}else {
			answer = "cuenta ya existente con ese nombre";
		}
	}
	
	private void consultarCuenta(String message) {
		String nombre = message.split(",")[1];
		int pin = Integer.parseInt(message.split(",")[2]);
		boolean repetido = verificarDuplicado(nombre);
		Cliente caux = new Cliente(nombre);
		Cliente aux2 = null;
		if(repetido) {
			caux.setPin(pin);
			for(Map.Entry m: mapaUsuarios.entrySet() ) {
				String nombreAux = (String) m.getKey();
				aux2 = (Cliente) m.getValue();
				if(nombreAux.equalsIgnoreCase(caux.getNombre()) && pin ==aux2.getPin() ) {	
					answer = "su saldo es de: "+aux2.getSaldo();
					break;
				}
			}
		}
		else {
			answer = "No existe la cuenta";
		}
	}
	
	private void retirarSaldo(String message) {
		String nombre = message.split(",")[1];
		int pin = Integer.parseInt(message.split(",")[2]);
		float retiro = Float.parseFloat(message.split(",")[3]);
		
		boolean repetido = verificarDuplicado(nombre);
		Cliente caux = new Cliente(nombre);
		Cliente aux2 = null;
		if(repetido) {
			caux.setPin(pin);
			for(Map.Entry m: mapaUsuarios.entrySet() ) {
				String nombreAux = (String) m.getKey();
				aux2 = (Cliente) m.getValue();
				if(nombreAux.equalsIgnoreCase(caux.getNombre()) && pin ==aux2.getPin() ) {	
					aux2.setSaldo(aux2.getSaldo()-retiro);
					mapaUsuarios.put(aux2.getNombre(), aux2);
					answer = "se ha realizado el retiro mi perro";
					break;
				}
			}
		}
		else {
			answer = "No existe la cuenta";
		}	
	}
	
	private void agregarSalgo(String message) {
		String nombre = message.split(",")[1];
		int pin = Integer.parseInt(message.split(",")[2]);
		float saldo = Integer.parseInt(message.split(",")[3]);
		
		Cliente caux = new Cliente(nombre);
		
		boolean repetido = verificarDuplicado(nombre);
		
		if(repetido) {
			caux.setSaldo(saldo);
			Cliente aux2 = null;
			for(Map.Entry m: mapaUsuarios.entrySet() ) {
				String nombreAux = (String) m.getKey();
				aux2 = (Cliente) m.getValue();
				if(nombreAux.equalsIgnoreCase(caux.getNombre()) && pin ==aux2.getPin() ) {	
					aux2.setSaldo(saldo);
					mapaUsuarios.put(aux2.getNombre(), aux2);
					break;
				}
			}
			answer = "se ha ingesado la platica mi pes";
		}else {
			answer = "no existe cuenta";
		}
	}
	
	private boolean verificarDuplicado(String nombre) {
		boolean repetido = false;
		Cliente caux = new Cliente(nombre);
		
		for(Map.Entry m: mapaUsuarios.entrySet() ) {
			String nombreAux = (String) m.getKey();
			if(nombreAux.equalsIgnoreCase(caux.getNombre())) {	
				repetido = true;
				break;
			}
		}

		return repetido;
	}

}
