package Principal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import proyectoPDL.analizadorLexico.*;
import proyectoPDL.analizadorLexico.Token.Tipo;
import proyectoPDL.analizadorSemantico.AnalizadorSemantico;
import proyectoPDL.analizadorSintactico.*;
import proyectoPDL.analizadorSintacticoSemantico.MainASintSem;
import proyectoPDL.gestorErrores.GestorErrores;

public class Principal {

	public static void main(String[] args) throws IOException {
		String dirActual = System.getProperty("user.dir");
		System.out.println("Estamos trabajando en el directorio: " + dirActual);

		String nombreArchivo = dirActual + "\\fichero_entrada.txt";
		String ficheroParse = dirActual + "\\fichero_formato_Parse_Ascendente.txt";
		String ficheroTok = dirActual + "\\fichero_salida_tokens.txt";
		String ficheroTabla = dirActual + "\\fichero_salida_tabla.txt";

		System.out.println("Se leerá el contenido del fichero fuente " + nombreArchivo + "\n");

		PrintStream salidaError = new PrintStream(dirActual + "\\errores.txt");

		System.out.println("Si hay errores, se notificarán en el fichero: " + dirActual + "\\errores.txt");

		File file = new File(nombreArchivo);
		if (!file.exists()) {
			System.err.println(
					"No se ha encontrado un fichero fuente llamado 'fichero_entrada.txt' que contenga el código a analizar");
			System.exit(0);
		}

		TablaDeSimbolos tabla = new TablaDeSimbolos();
		GestorErrores gestorE = new GestorErrores(tabla);
		MainALexico AL = new MainALexico(nombreArchivo, tabla);
		AnalizadorSemantico ASem = new AnalizadorSemantico(tabla);
		MainASintSem AS = new MainASintSem(ASem, tabla);
		Token tok;

		boolean valido = false;
		boolean parar = false;

		FileWriter escritor_tok = new FileWriter(ficheroTok);
		BufferedWriter buffer_tok = new BufferedWriter(escritor_tok);

		FileWriter escritor_Parse = new FileWriter(ficheroParse);
		BufferedWriter buffer_Parse = new BufferedWriter(escritor_Parse);

		FileWriter escritor_Tabla = new FileWriter(ficheroTabla);
		BufferedWriter buffer_Tabla = new BufferedWriter(escritor_Tabla);

		while (!parar) {
			tok = AL.siguienteToken(); /* LOS ERRORES SE GESTIONAN DENTRO DEL ANALIZADOR LEXICO */
			if (tok == null) {
				parar = true;
			} else {
				buffer_tok.write(tok.toString() + " ");
			}
			// System.out.println(tok);

			if (parar == true) // estamos en el caso $
			{
				valido = AS.validarTokenASint(null); /* LOS ERRORES SE TRATAN DENTRO DEL ANALIZADOR SINTACTICO */
			} else {
				valido = AS.validarTokenASint(tok); /* PA */
			}
		}

		buffer_Tabla.write(AL.getTabla());
		buffer_Parse.write(AS.getParse());
		buffer_tok.close();
		buffer_Tabla.close();
		buffer_Parse.close();

		System.out.println("----------------------------------.");
		System.err.println("Ejecucion sin errores.");
		System.out.println("Puedes revisar la salida de tokens en: " + ficheroTok);
		System.out.println("Puedes revisar la Tabla de Simbolos en: " + ficheroTabla);
		System.out.println("Puedes revisar el Parse Ascendente en: " + ficheroParse);

	}

}
