package proyectoPDL.analizadorLexico;

import java.util.ArrayList;

import Principal.TablaDeSimbolos;
import proyectoPDL.analizadorLexico.Token.Tipo;

public class AccionesSemanticas {

	public String lex;
	int num;
	ArrayList<Token> tokens = new ArrayList<>(); // nos se si esto deberia ir en esta clase
	// public int contador_ID = 1;
	TablaDeSimbolos tabla;

	public AccionesSemanticas(TablaDeSimbolos tabla) {
		this.tabla = tabla;

	}

	public int accionSemantica(String accion, char c) {
		if (accion == null) {
			return 1;
		}

		else if (accion.equals("Ilex"))
			return Ilex();
		else if (accion.equals("G3"))
			return G3();
		else if (accion.equals("C"))
			return C(c);
		else if (accion.equals("C+Ilex")) {
			Ilex();
			return C(c);
		} else if (accion.equals("G1"))
			return G1();
		else if (accion.equals("G2"))
			return G2();
		else if (accion.equals("G4"))
			return G4();
		else if (accion.equals("G5"))
			return G5();
		else if (accion.equals("G6"))
			return G6();
		else if (accion.equals("G7"))
			return G7();
		else if (accion.equals("G8"))
			return G8();
		else if (accion.equals("G9"))
			return G9();
		else if (accion.equals("G10"))
			return G10();
		else if (accion.equals("G11"))
			return G11(c);
		else if (accion.equals("Vini"))
			return Vini(c);
		else if (accion.equals("V"))
			return V(c);
		else if (accion.equals("G10+G11")) {
			G10();
			return G11(c);
		}

		return -1;

	}

	public int Ilex() {
		lex = "";
		return 1;
	}

	public int C(char c) {
		lex = lex + c;
		return 1;
	}

	public int V(char c) {
		num = num * 10 + Character.getNumericValue(c);
		return 1;
	}

	public int Vini(char c) {
		num = Character.getNumericValue(c);
		return 1;
	}

	public int G1() {
		tokens.add(new Token(Tipo.AND));
		return 1;
	}

	public int G2() {
		tokens.add(new Token(Tipo.OR));
		return 1;
	}

	public int G3() {
		if (lex.length() <= 64) {
			tokens.add(new Token(Tipo.CAD, lex));
			return 1;
		} else {
			try {
				throw new Exception("El lexema \"" + lex + "\" tiene más de 64 caracteres");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

		}
		return -1;
	}

	public int G4() {
		tokens.add(new Token(Tipo.NEQUAL));
		return 1;
	}

	public int G5() {
		tokens.add(new Token(Tipo.AUTODEC));
		return 1;

	}

	public int G6() {

		tokens.add(new Token(Tipo.RESTA));
		return 0;

	}

	public int G7() {
		tokens.add(new Token(Tipo.EQUAL));
		return 1;

	}

	public int G8() {
		tokens.add(new Token(Tipo.IGUAL));
		return 0;

	}

	public int G9() {
		if (lex.equals("boolean"))
			tokens.add(new Token(Tipo.BOOLEAN));
		else if (lex.equals("if"))
			tokens.add(new Token(Tipo.IF));
		else if (lex.equals("function"))
			tokens.add(new Token(Tipo.FUNCTION));
		else if (lex.equals("get"))
			tokens.add(new Token(Tipo.GET));
		else if (lex.equals("int"))
			tokens.add(new Token(Tipo.INT));
		else if (lex.equals("let")) {
			tokens.add(new Token(Tipo.LET));
			tabla.flag_let = true;
		} else if (lex.equals("put"))
			tokens.add(new Token(Tipo.PUT));
		else if (lex.equals("return"))
			tokens.add(new Token(Tipo.RETURN));
		else if (lex.equals("string"))
			tokens.add(new Token(Tipo.STRING));
		else if (lex.equals("void"))
			tokens.add(new Token(Tipo.VOID));
		else if (lex.equals("while"))
			tokens.add(new Token(Tipo.WHILE));
		else if (lex.equals("get"))
			tokens.add(new Token(Tipo.GET));
		else {
			Entrada ent = new Entrada(lex);

			int posicion = tabla.agregarEntrada2(ent);
			tokens.add(new Token(Tipo.ID, posicion));
			tabla.flag_let = false;

			return 0;

		}
		return 0;

	}

	public int G10() {
		if (num > 32767) {
			try {
				throw new Exception("El entero \"" + num + "\" tiene tiene un valor superior a 32767");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

		} else {
			tokens.add(new Token(Tipo.ENT, num));
			return 0;

		}

		return 0;

	}

	public int G11(char c) {
		if (c == '}')
			tokens.add(new Token(Tipo.LLAVC));
		else if (c == '{')
			tokens.add(new Token(Tipo.LLAVA));
		else if (c == '(')
			tokens.add(new Token(Tipo.PARA));
		else if (c == ')')
			tokens.add(new Token(Tipo.PARC));
		else if (c == ',')
			tokens.add(new Token(Tipo.COMA));
		else if (c == ';')
			tokens.add(new Token(Tipo.PYC));
		else if (c == '+')
			tokens.add(new Token(Tipo.SUM));
		return 1;

	}

}
