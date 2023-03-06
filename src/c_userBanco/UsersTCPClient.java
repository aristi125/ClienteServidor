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

	public static final String SERVER = "localhost";//ESTA ES LA IP DE MI PORTATIL
	public static final int PORT = 3400;

	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;
	private String fromServer="";

	private Socket clientSideSocket;

	public static void main(String[] args) throws Exception {
		UsersTCPClient ec = new UsersTCPClient();

		ec.init();


	}

	public UsersTCPClient() {
		System.out.println("Echo TCP cleinte is running... ");
	}

	public void init() throws Exception{
		while(true){

			//createStreams(clientSideSocket);
			protocol(clientSideSocket);

		}

	}



	public void protocol (Socket socket) throws Exception {
		System.out.print("Ingrese su usuario: ");
		String fromUser = SCANNER.nextLine();

		System.out.println("mandando "+fromUser);
		ingresarOpcion(fromUser);
		//fromServer = fromNetwork.readLine();
		//System.out.println("[Client] from server LOGIN: " + fromServer);
	}

	public void ingresarOpcion(String fromUser) throws IOException {
		String fromServerAnterior = "";
		String opcion = fromUser.split(",")[0];
		String message = fromUser;
		if(opcion.equalsIgnoreCase("procesar")) {

			String nombre = "./src/resources/";
			nombre += fromUser.split(",")[1];
			ArrayList<String> lista;
			try {
				lista = ArchivoUtil.leerArchivo(nombre);
				for(String s: lista) {
					clientSideSocket = new Socket(SERVER, PORT);
					createStreams(clientSideSocket);
					message = s;
					toNetwork.println(message);
					fromServer = fromNetwork.readLine();
					System.out.println("[Client] from server LOGIN: " + fromServer);
					clientSideSocket.close();
					Thread.sleep(5000);
				}
			} catch (Exception  e ) {
				// TODO Auto-generated catch block
				System.out.println("no existe el archivo mi perro");
			}


		}else {
			clientSideSocket = new Socket(SERVER, PORT);
			try {
				createStreams(clientSideSocket);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			toNetwork.println(fromUser);
			fromServer = fromNetwork.readLine();
			System.out.println("[Client] from server LOGIN: " + fromServer);
			clientSideSocket.close();
		}

	}


	public void createStreams (Socket socket) throws Exception{
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

}
