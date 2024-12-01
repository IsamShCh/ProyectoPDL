package proyectoPDL.gestorErrores;

import Principal.TablaDeSimbolos;

public class GestorErrores {

	static int numero_de_linea_lexico;
	static int numero_de_linea_SemSint;
	static TablaDeSimbolos tablaSimbolos;

	public GestorErrores(TablaDeSimbolos tabla) {

		numero_de_linea_lexico = 1;
		numero_de_linea_SemSint = 1;
		tablaSimbolos = tabla;
	}

	public static void sumarLineaLexico() {

		numero_de_linea_lexico++;

	}

	public static void sumarLineaSemSint() {

		numero_de_linea_SemSint++;

	}

	public static int getLineaLexico() {
		return numero_de_linea_lexico;

	}

	public static int getLineaSemSint() {
		return numero_de_linea_SemSint;

	}

	public static TablaDeSimbolos getTablaSimbolos() {
		return tablaSimbolos;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
