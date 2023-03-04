package intento2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {

    private static final String IP = "127.0.0.1";
    private static final int PUERTO = 4444;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Socket socket;
        BufferedReader entrada;
        PrintWriter salida;

        try {
            socket = new Socket(IP, PUERTO);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Conexión establecida con el servidor...");

            String opcion = "";

            while (!opcion.equals("salir")) {
                System.out.println("Ingrese una opción: ");
                opcion = sc.nextLine();
                String[] parametros = opcion.split(",");
                String comando = parametros[0];
                String nombre = "./src/resources/";
                nombre += parametros[1];
                if (comando.equals("procesar")) {

                    ArrayList<String> lineas = ArchivoUtil.leerArchivo(nombre);
                    for (String linea : lineas) {

                        String[] instruccion = linea.split(",");
                        salida.println(linea);
                        String respuesta = entrada.readLine();
                        System.out.println(respuesta);
                    }
                } else {
                    salida.println(opcion);
                    String respuesta = entrada.readLine();
                    System.out.println(respuesta);
                }
            }

            socket.close();
            entrada.close();
            salida.close();
            sc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
