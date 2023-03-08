package c_userBanco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class UsersTCPClient {
	private static final Scanner SCANNER = new Scanner(System.in);

	public static final String SERVER = "8.tcp.ngrok.io";//EN ESTA VARIABLE SE GUARDARA LA IP DEL SERVIDOR 181.78.24.118
	public static final int PORT = 16670;// LE PASAMOS EL PUERTO AL CUAL NOS QUEREMOS CONECTAR // 12188

	//NOS PERMITE IMPRIMIR REPRESENTACIONES FORMATEADAS DE UNA SALIDA DE TXT
	private PrintWriter toNetwork;
	//NOS PROPORCIONA FUNCIONES UTILES, COM LINEAS, QUE NOS PROPORCIONA UN STREAM DE LIENAS
	//TAMBIEN LEE EL TXT DESDE UN FLUJO DE ENTRADA DE CARACTERES
	private BufferedReader fromNetwork;

	//DECLARAMOS UNA VARIABLE VACIA DE TIPO XTRING PARA LUEGO UTILIZARLA
	private String fromServer="";

	private Socket clientSideSocket; //SE CREA EL SOCKET

	public static void main(String[] args) throws Exception {
		UsersTCPClient ec = new UsersTCPClient();//INSTANCIAMOS LA CLASE TCPCLIENTE
		//TERMINOS DE COMO SE USA LA APLICACION BANCO

		//LLAMAMOS EL METODO DONDE SE VAN A EJECUTAR EL PROGRAMA
		ec.init();

	}

	public UsersTCPClient() {
		//PARA SABER SI ESTA CORRIENDO EL CLEINTE
		System.out.println("Echo TCP cliente is running... ");
		mostrarIndicaciones();

	}

	private void mostrarIndicaciones() {
		System.out.println("La Aplicación del banco tiene 4 opciones las cuales son:"+
				"\n Crear - Depositar - Consultar - Retirar \n");
		System.err.println("\n\nNOTA: sin importar que opcion que desee que se ejecute debe ser separado por comas y sin espacios despues de las comas\n");
		System.out.println("Para CREAR una cuenta se necesita ingresar la instruccion de la siguiente manera"
				+ "crear,nombreUsuario,idUsuario, PinUsuario");
		System.out.println("Para DEPOSITAR saldo en una cuenta se necesita ingresar la instruccion de la siguiente manera "
				+ "depositar,nombreUsuario,idUsuario,valorIngresar");
		System.out.println("Para DEPOSITAR saldo en una cuenta se necesita ingresar la instruccion de la siguiente manera "
				+ "depositar,nombreUsuario,idUsuario,valorIngresar");
		System.out.println("Para CONSULTAR saldo en una cuenta se necesita ingresar la instruccion de la siguiente manera "
				+ "depositar,nombreUsuario,pinUsuario");
		System.out.println("Para RETIRAR saldo en una cuenta se necesita ingresar la instruccion de la siguiente manera "
				+ "depositar,nombreUsuario,pinUsuario,valorRetiro");
		System.out.println("Para que se lea un txt y se ejecute line por liena des txt y se utiliza la palabra PROCESAR");
	}

	public void init() throws Exception{
		while(true){//MIENTRAS LA FUNCION SEA VERDADERA

			//LLAMOS AL METOCO PROTOCL Y LE PASAMOS POR PARAMETRO EL SOCJET DE CLIENTE
			protocol(clientSideSocket);

		}

	}


	/**
	 * SE LE PASA POR PARAMETRO EL SOCKET LO CUAL
	 * NOS AYUDA EN PUNTOS FINALES DE ENLACES
	 * DE COMUNICACIONES ENTRE PROCESOS
	 *
	 * @param socket
	 * @throws Exception
	 */
	public void protocol (Socket socket) throws Exception {
		//SE LE PIDE QUE PASE PARAMETRO PARA EJECUTAR
		System.out.print("Ingrese comando a ejecutar: ");
		String fromUser = SCANNER.nextLine();

		//SE GUARDA EL COMANDO A EJECUTAR Y SE KE ENVIA AL SERVIDOR
		//PARA QUE EJECUTE EL CODIGO DEL BANCO
		System.out.println("mandando "+fromUser);
		ingresarOpcion(fromUser);
	}

	/**
	 * ACÁ ES DONDE SE PRODESA LA INFORMACION
	 * Y DEPENDIENDO LA PALABRA CLABE QUE SE LE INGRESE
	 * POR CONSOLA SEPARADO POR COMAS, SE LE ENVIA AL SERVIDOR
	 * PARA QUE EJECUTE EL COMANDO CORRESPONDIENTE
	 * @param fromUser
	 * @throws IOException
	 */
	public void ingresarOpcion(String fromUser) throws IOException {
		//SE INICIALIZA UNA VARIABLE DONDE QUEDARA GUARDADA
		//LA INFORMACION PARA QUE EL SERVIDOOR LA PROCESE
		String fromServerAnterior = "";
		//SE PARTIRA EL TEXTO POR COMAS Y SE TOMARA LA POSISION 0
		//LA CUAL SERA LA PALABRA CLABE OSEA LA OPSION A REALIZAR
		String opcion = fromUser.split(",")[0];
		String message = fromUser;
		//SI LA PALABRA CLABE ES procesar, TOMARA UN ARCHIVO PANO.TXT
		//Y COMENZARA A EJECUTAR LINEA POR LINEA, LAS CUALES SON
		//COMANDOS DE LAS CUATRO OPCIONES DEL BANCO
		if(opcion.equalsIgnoreCase("procesar")) {

			//SE LE PASA LA RUTA DONDE ESTA EL TXT
			String nombre = "./src/resources/";
			//ACA PARATIRA LA CADE POR COMAS TOMANDO LA POSISION 1
			//PARA SABER QUE OPCION DEBE SEGUIR
			nombre += fromUser.split(",")[1];
			ArrayList<String> lista;
			try {
				lista = ArchivoUtil.leerArchivo(nombre);
				for(String s: lista) {
					clientSideSocket = new Socket(SERVER, PORT);
					//SE UTILIZA EL METODO CREARSTREAM EN ESTA FUNCION
					createStreams(clientSideSocket);
					message = s;
					toNetwork.println(message);//IMPRIMIMOS EL MENSJE DE VUELTA QUE DA EL SERVER
					fromServer = fromNetwork.readLine();//LEERMOS LA RESPUESTA DEL SERVER
					//SE IMPRIME EL RESULTADO DE LA ACCION QUE EJECUTO EL SERVER
					System.out.println("[Client] from server " + fromServer);
					clientSideSocket.close();// CERRAMOS LAS PERTICIONES
					Thread.sleep(3000);
				}
			} catch (Exception  e ) {//SE CAPTURAN LAS EXCEPCIONES
				//EN ESTE CASO SI NO FUNCIONA EL CODIGO DEL TRY SE VIENE AL CATCH PARA ATRAPAR EL ERROR
				// TODO Auto-generated catch block
				System.out.println("no existe el archivo txt");
			}


		}else {
			// SI LA OPCION ES DOFERENTE A PROCESAR, SIGNIFICA QUE LAS DEBEMOS
			//HACER A MANO Y UNA POR UNA
			clientSideSocket = new Socket(SERVER, PORT);
			try {
				createStreams(clientSideSocket);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			toNetwork.println(fromUser);
			fromServer = fromNetwork.readLine();
			System.out.println("[Client] from server: " + fromServer);
			clientSideSocket.close();
		}

	}

	/**
	 * ESTE METODO CREATESTREAMS ES PARA GENERAR
	 * UNA SECUENCA DE OBJETOS QUE ADMITE VARIOS METODOS
	 * QUE SE PUEDEN CANALIZAR PARA PRODUCIR EL RESULTADO DESEADO
	 * @param socket
	 * @throws Exception
	 */
	public void createStreams (Socket socket) throws Exception{
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

}
