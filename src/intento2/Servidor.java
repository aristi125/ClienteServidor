package intento2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Servidor {

    private static final int PUERTO = 4444;

    private static HashMap<String, clienteDatos> clientes = new HashMap<String, clienteDatos>();

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;

        try {
            serverSocket = new ServerSocket(PUERTO);

            System.out.println("Servidor iniciado...");

            while (true) {
                socket = serverSocket.accept();

                ThreadCliente threadCliente = new ThreadCliente(socket);
                threadCliente.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ThreadCliente extends Thread {
        private Socket socket;
        private BufferedReader entrada;
        private PrintWriter salida;
        private clienteDatos cliente;

        public ThreadCliente(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                salida = new PrintWriter(socket.getOutputStream(), true);

                String comando = "";
                while ((comando = entrada.readLine()) != null) {
                    String[] parametros = comando.split(",");
                    String accion = parametros[0];

                    if (accion.equals("registrar")) {
                        cliente = new clienteDatos(parametros[1]);
                        cliente.setId(Integer.parseInt(parametros[2]));
                        cliente.setCuentaBancaria(parametros[3]);
                        cliente.setPin(Integer.parseInt(parametros[4]));

                        clientes.put(parametros[1], cliente);

                        salida.println("Cliente registrado correctamente.");

                    } else if (accion.equals("consultar")) {
                        String nombre = parametros[1];
                        String cuentaBancaria = parametros[2];
                        int pin = Integer.parseInt(parametros[3]);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
