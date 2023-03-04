package c_userBanco;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public  class ArchivoUtil {

	private static final String ruta  = "./src/resources/historial.txt";

	public static void guardarArchivo(String contenido) throws IOException {

		FileWriter fw = new FileWriter(ruta,true);
		BufferedWriter bfw = new BufferedWriter(fw);
		bfw.write(contenido+"\n");
		bfw.close();
		fw.close();
	}

	/**
	 * ESte metodo retorna el contendio del archivo ubicado en una ruta,con la lista de cadenas.
	 * @param ruta
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> leerArchivo(String ruta) throws IOException {

		ArrayList<String>  contenido = new ArrayList<String>();
		FileReader fr=new FileReader(ruta);
		BufferedReader bfr=new BufferedReader(fr);
		String linea="";
		while((linea = bfr.readLine())!=null)
		{
			contenido.add(linea);
		}
		bfr.close();
		fr.close();
		return contenido;
	}


	

	
}

