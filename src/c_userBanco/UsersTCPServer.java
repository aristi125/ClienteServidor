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
	
	public static final int PORT = 3400;//LE DESIMOS QU EPUERTO SE VA USAR AL SERVER
	private ServerSocket listener;//PARA ESUCHCAR LAS PETICIONES
	private Socket serverSideSocket;//SE CREA EL SOCKET DEL SERVER
	private PrintWriter toNetwork;//NOS PERMITE IMPRIMIR REPRESENTACIONES FORMATEADAS DE UNA SALIDA DE TXT
	//NOS PROPORCIONA FUNCIONES UTILES, COM LINEAS, QUE NOS PROPORCIONA UN STREAM DE LIENAS
	//TAMBIEN LEE EL TXT DESDE UN FLUJO DE ENTRADA DE CARACTERES
	private BufferedReader fromNetwork;

	//UTILIZAREMOS UNS HASMAP PARA GUARDAR YENCONTAR LAS INFORMACIONS QUE SE GUARDARA
	public HashMap<String, Cliente> mapaUsuarios = new HashMap<String,Cliente>();
	private String nombre;
	private String answer;//PARA GUARDAR LA RESPUESTA QUE VA DAR EK SERVER
	
	public static void main(String[] args) throws Exception {
		//INSTANCIMAOS LA VARIABLE
		UsersTCPServer es = new UsersTCPServer();
		while(true) {
			//LLAMAMOS EL METODO DONDE SE VAN A EJECUTAR EL PROGRAMA
			es.init(); 
		}
		
	}
	
	//PARA SAVER QUE EL SERVER ESTE CORRIENDO Y EN QUE PUERTO
	public UsersTCPServer() {
		System.out.println("Echo TCP server is runnig on port: " + PORT);
	}

	private void init() throws Exception {
		//ESCUHAMOS LO QUE NOS QUIERE DECIR EL CLIENTE QUE ESTE
		// CONECTADO POR EL MISMO PUERTO
		listener = new ServerSocket(PORT);
		
		while(true) {
			//MISNTRAS EÑ SERVIDOR ESTE ESCUCHANDO ACEPTE LAS PETICIONES
			serverSideSocket = listener.accept();
			//SE LLAMA LA FUNCION CREARSTREAM
			createStreams(serverSideSocket);
			//SE LLAMA LA FUNCION PROTOCOL DONDE ES LA ENCARGADA DE HACER LAS OPERACIONES
			protocol(serverSideSocket);
		}
		
	}

	/**
	 * A ESTE METODO SE LE PASA EL SOCKET, EL CUAL NOS
	 * AYUDA A RECIBIR EL MENSAJE ENVIADO POR EL CLIENTE
	 * Y DESPUES DE EJECUTAR LA OPCION DESEADA ENVIA UN MENSAJE DE RESPUESTA
	 * @param socket
	 * @throws Exception
	 */
	private void protocol(Socket socket) throws Exception{
		//SE RESIVE Y SE LEE EL MENSAJE QUE ENVIO EL CLIENTE
		String message = fromNetwork.readLine();
		
		System.out.println("Recibido "+message);
		System.out.println("[Server] from cleint: " + message);
		//CON ESTE METODO NOS PERMITE  EJECUTAR EL COMANDO
		//DEPENDIENDO DE LA OPCION QUE INGRESE
		opcionBanco(message);
		
		
	}

	private void opcionBanco(String message) throws IOException {
		String nombre;
		int id, pin;
		
		Boolean repetido = false;
		//SE PARTE LA CADENA POR COMAS Y TOMA LA POSICION 0
		//DONDE ESTA EL OPCION A SEGUIR PARA EJECUTAR EL COMANDO
		String opcion = message.split(",")[0];
		
		if(opcion.equalsIgnoreCase("crear")) {//SI LA OPCION ES CREA
			//UTILIZAMOS LA FUNCION CREAR CUENTA DEL BANCO PASANDOLE EL MENSAJE
			crearCuenta(message);
		}else if(opcion.equalsIgnoreCase("depositar")) {//SI LA OPCION ES INGRESAR
			//UTILIZAMOS LA FUNCION CREAR AGREGARSALDO DEL BANCO PASANDOLE EL MENSAJE
			agregarSalgo(message);
		}else if(opcion.equalsIgnoreCase("Consultar")) {//SI LA OPCION ES CONSULTAR
			//UTILIZAMOS LA FUNCION CONSULTAR CUENTA DEL BANCO PASANDOLE EL MENSAJE
			consultarCuenta(message);
		}else if(opcion.equalsIgnoreCase("retirar")) {//SI LA OPCION ES RETIRAR
			//UTILIZAMOS LA FUNCION RETIRARSALDO CUENTA DEL BANCO PASANDOLE EL MENSAJE
			retirarSaldo(message);
		}

		//Y YA PARA FINALIZAR LE ENVIAMOS UNA RESPUETA AL CLIENTE
		//PARA QUE SEPA QUE EL METODO SE EJECUTO
		toNetwork.println(answer);
	}

	/**
	 * UNA SECUENCA DE OBJETOS QUE ADMITE VARIOS METODOS
	 * QUE SE PUEDEN CANALIZAR PARA PRODUCIR EL RESULTADO DESEADO
	 * @param socket
	 * @throws Exception
	 */
	private void createStreams(Socket socket) throws Exception{
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
	}

	/**
	 * ESE METODO NOS AYUDA A CREAR LAS CUENTA DEL BANCO
	 * LO CUAL EL MENSAJE QUE SE LE PASA POR PARAMETRO SERA
	 * SEPARADO POR CADA COMA LO CUAL EQUIVALE A UNA POSICION DE UNA RREGLO
	 * QUE CADA POSICION CONTIENE UNA INFOMACION PARA CREAR LA CUENTA
	 * @param message
	 */
	private void crearCuenta(String message) {
		nombre = message.split(",")[1];
		Cliente caux = new Cliente(nombre);
		int id = Integer.parseInt(message.split(",")[2]);
		int pin = Integer.parseInt(message.split(",")[3]);
		
		boolean repetido = verificarDuplicado(nombre);

		//SI EL USUARIO QUE VA A INGRESAR NO EXISTE LO AGREGA
		if(!repetido) {
			caux.setCuentaBancaria("cuenta - "+caux.getNombre());
			caux.setId(id);
			caux.setPin(pin);
			caux.setSaldo(0);
			mapaUsuarios.put(caux.getNombre(), caux);
			answer = "cuenta creada Exitosamente";
		}else {//SI EL USUARIO QUE VA A INGRESAR YA EXISTE NO LO AGREGA
			answer = "cuenta ya existente con ese nombre";
		}
	}

	/**
	 * 	ESE METODO NOS AYUDA A CONSULTAR LAS CUENTA DEL BANCO
	 * 	LO CUAL EL MENSAJE QUE SE LE PASA POR PARAMETRO SERA
	 * 	SEPARADO POR CADA COMA LO CUAL EQUIVALE A UNA POSICION DE UNA RREGLO
	 *  QUE CADA POSICION CONTIENE UNA INFOMACION DE LA CUENTA
	 * @param message
	 */
	private void consultarCuenta(String message) {
		String nombre = message.split(",")[1];
		int pin = Integer.parseInt(message.split(",")[2]);
		boolean repetido = verificarDuplicado(nombre);
		Cliente caux = new Cliente(nombre);
		Cliente aux2 = null;

		if(repetido) {//SI EL USUARIO QUE VA A INGRESAR YA EXISTE NO LO AGREGA
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
			answer = "No existe la cuenta por ese Nombre";
		}
	}

	/**
	 * ESE METODO NOS AYUDA A RETIRAR EL SALDO LAS CUENTA DEL BANCO
	 * LO CUAL EL MENSAJE QUE SE LE PASA POR PARAMETRO SERA
	 * SEPARADO POR CADA COMA LO CUAL EQUIVALE A UNA POSICION DE UNA RREGLO
	 * QUE CADA POSICION CONTIENE UNA INFOMACION PARA RETIRAR EL SALDO LA CUENTA
	 * @param message
	 */
	private void retirarSaldo(String message) {
		String nombre = message.split(",")[1];
		int pin = Integer.parseInt(message.split(",")[2]);
		float retiro = Float.parseFloat(message.split(",")[3]);
		
		boolean repetido = verificarDuplicado(nombre);
		Cliente caux = new Cliente(nombre);
		Cliente aux2 = null;
		if(repetido) {//SI EL USUARIO QUE VA A INGRESAR YA EXISTE NO LO AGREGA
			caux.setPin(pin);
			for(Map.Entry m: mapaUsuarios.entrySet() ) {
				String nombreAux = (String) m.getKey();
				aux2 = (Cliente) m.getValue();
				if(nombreAux.equalsIgnoreCase(caux.getNombre()) && pin ==aux2.getPin() ) {	
					aux2.setSaldo(aux2.getSaldo()-retiro);
					mapaUsuarios.put(aux2.getNombre(), aux2);
					answer = "se ha realizado el retiro Correctametne";
					break;
				}
			}
		}
		else {
			answer = "No existe la cuenta para hacer el retiro";
		}	
	}

	/**
	 * ESE METODO NOS AYUDA A METER SALDO LAS CUENTA DEL BANCO
	 * LO CUAL EL MENSAJE QUE SE LE PASA POR PARAMETRO SERA
	 * SEPARADO POR CADA COMA LO CUAL EQUIVALE A UNA POSICION DE UNA RREGLO
	 * QUE CADA POSICION CONTIENE UNA INFOMACION PARA INTRODUCIR SALDO LA CUENTA
	 * @param message
	 */
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
			answer = "se ha ingesado la el saldo deseado";
		}else {
			answer = "no existe cuenta con ese saldo";
		}
	}

	/**
	 * VERIFICAMOS QUE NO EXISTA UN DATO DUPLICADO EN EL
	 * HASHMAP POR MEDIO DEL PARAMETRO QUE SE LE PASA
	 * QUE EN ESTRE CASO ES EL NOMBRE
	 * @param nombre
	 * @return
	 */
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
