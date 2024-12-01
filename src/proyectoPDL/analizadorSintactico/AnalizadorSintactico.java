package proyectoPDL.analizadorSintactico;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import Principal.TablaDeSimbolos;
import proyectoPDL.analizadorLexico.Entrada;
import proyectoPDL.analizadorLexico.Token.Tipo;
import proyectoPDL.analizadorLexico.Token;
import proyectoPDL.analizadorSemantico.AnalizadorSemantico;
import proyectoPDL.analizadorSemantico.Atributo;
import proyectoPDL.gestorErrores.ErrorSintactico;
import proyectoPDL.gestorErrores.GestorErrores;

public class AnalizadorSintactico {

	private Automata automata;
	private Stack<String> pila;
	ArrayList<Integer> parse;
	AnalizadorSemantico ASem;
	TablaDeSimbolos tabla;


	public AnalizadorSintactico(Automata automata, AnalizadorSemantico ASem, TablaDeSimbolos tabla) {
		this.setAutomata(automata);
		this.setPila(new Stack<String>());
		this.parse = new ArrayList<>();
		this.ASem = ASem;
		this.tabla = tabla;
	}
	private int estado_anterior;
/* Realizamos la  evaluacion del token  mediante el metodo tabular. */
	public void MetodoTabular(String token, Token tok) throws Exception { // tenemos que ponerle de quien va a solicitar los
		// tokens;

		Stack<String> pilaASint = pila;
		Stack<Atributo> pilaASem = ASem.getPila();
		
		// tenemos que poner que convierta los tokens en simbolos
		String s = getPila().pop(); // estado
		Pair<Character, Integer> siguiente_accion = getAutomata().Accion(s, token);
		
		System.out.print("");


		if (siguiente_accion == null) {
			String esperados = "";
			
			Estado est  = getAutomata().listaEstados.get(estado_anterior);
			Set<String> terminales = getAutomata().lr1.gram.getTerminales();
			Set<String> hola = getAutomata().listaTransiciones.get(est).keySet();
			for(String transiciones : getAutomata().listaTransiciones.get(est).keySet())
			{
				//transiciones = transiciones;
				if(terminales.contains(transiciones)) esperados+="'"+transiciones+"' | ";
				
			}
			String mensaje ="";
			if(esperados.length()>1)
			{
				esperados  =  Automata.sustitucionSimbolos(esperados);
				esperados = esperados.substring(0, esperados.length()-3);
				mensaje   = "Se esperaba: "  + esperados + "." ;
			}
			else {
				
				mensaje = "";
			}

			throw new ErrorSintactico(mensaje);
		}

		
		else if (siguiente_accion.getKey() == 'd') // CASO 1 ACCION [s,a] = desp k
		{
			estado_anterior = siguiente_accion.getValue();
			Entrada ent = null;
			Atributo at = null;
			if (tok.getTipo() == Tipo.ID) {
				//ent = tabla.obtenerEntradaXidPos((int) tok.getValor());
				ent = tabla.obtenerEntradaXidPos((int) tok.getValor());
				at = new Atributo(token, ent.getTipo(), ent);
			}

			getPila().push(s);

			getPila().push(token);
			pilaASem.push(at);

			getPila().push(String.valueOf(siguiente_accion.getValue())); // pedimos nuevo token. No hacemos nada
			pilaASem.push(null);
			
			if(GestorErrores.getLineaLexico() > GestorErrores.getLineaSemSint())
			{
				GestorErrores.sumarLineaSemSint();
			}
			
			
		}

		else if (siguiente_accion.getKey() == 'r') // CASO 2 ACCION[s,a] = red X X. A-->B
		{
			estado_anterior = siguiente_accion.getValue();
			getPila().push(s);

			/*
			 * _____ ___ _ _ _ _ __ __ ___ ___
			 * | __\ \ / /_\ | | | | | |/_\ | \/ |/ _ \/ __|
			 * | _| \ V / _ \| |_| |_| / _ \| |\/| | (_) \__ \
			 * |___| \_/_/_\_\____\___/_/ \_\_|__|_|\___/|___/
			 * /_\ / __/ __|_ _/ _ \| \| | / __| __| \/ |
			 * / _ \ (_| (__ | | (_) | .` | \__ \ _|| |\/| |
			 * /_/ \_\___\___|___\___/|_|\_| |___/___|_| |_|
			 * 
			 */
			/* obtenemos cual sera la siguiente accion a realizar */
			int numeroDeRegla = siguiente_accion.getValue();
			/* buscamos la regla que vamos a aplicar en la lista de reglas */
			Pair<String, String> regla_a_aplicar = getAutomata().lr1.gram.getProduccionesNumero(numeroDeRegla);

			int contador;
			/* esta es la excepcion lamda */
			if (regla_a_aplicar.getValue().equals("λ")) {
				contador = 0;
			} else {
				contador = regla_a_aplicar.getValue().length(); // numero de caracteres que tiene el consecuente.
			}

			/* Ejecutamos la accion semantica */
			ASem.ejecutarAccionSemantica(numeroDeRegla, contador);

			/*
 __   ___   ___ ___   _  __    __  ___  ___
 \ \ / /_\ / __|_ _| /_\ | \  /  |/ _ \/ __|
  \ V / _ \ (__ | | / _ \| |\/   | | (_) \__ \
   \_/_/ \_\___|___/_/_\_\_|   |_|\___/|___/
   | |    /_\   | _ \_ _| |    /_\
   | |__ / _ \  | _/| | | |__ / _ \
   |____/_/ \_\ |_| |___|____/_/ \_\
			 * 
			 */


			for (int i = 0; i < contador * 2; i++) // sacamos elementos de la pila
			{
				getPila().pop();
			}

			String s_ = getPila().pop();
			getPila().push(s_);
			String antecedente = regla_a_aplicar.getKey();
			Integer valGOTO = getAutomata().GOTO(s_, antecedente); // obtenemos GOTO[s' , A] y lo metemos en la pila ::->
			// GOTO[estado s_, A antecedente de la regla a aplicar]

			getPila().push(antecedente);
			getPila().push(String.valueOf(valGOTO));
			parse.add(numeroDeRegla);
			// volvemos al principio, pero no pedimos token.
			MetodoTabular(token, tok);

		} else if (siguiente_accion.getKey() == 'a') // CASO 3 ACCION[s,a] = aceptar
		{
			getPila().push(s);
		} else {
			System.err.println("HAY UN ERROR INESPERADO");
			//System.exit(0);
			throw new ErrorSintactico("");
		}

	}



	public String formatoFicheroGramatica() {
		String buffer = "";

		buffer = buffer + "Terminales = { ";
		// Terminales = { a b c }
		for (String x : getAutomata().lr1.gram.getTerminales()) {
			buffer = buffer + x + " ";
		}
		buffer = buffer + "}\n";
		// NoTerminales = { nterm1 nterm2 nterm3 ....... ntermM }
		buffer = buffer + "NoTerminales = { ";
		for (String x : getAutomata().lr1.gram.getNoTerminales()) {
			buffer = buffer + x + " ";
		}
		buffer = buffer + "}\n";

		// Axioma = ntermX

		buffer = buffer + "Axioma = " + getAutomata().lr1.gram.getProduccionesNumero(0).getKey() + "\n"; // obtenemos el
		// antecedente de la
		// regla numero 0.
		// E.D el axioma

		buffer = buffer + "Producciones = { \n";
		for (int i = 0; i < getAutomata().lr1.gram.getNumeroDeReglas(); i++) {
			Pair<String, String> regla = getAutomata().lr1.gram.getProduccionesNumero(i);
			String value = regla.getValue();
			if (value.equals("λ")) {
				value = "lambda";
			}

			buffer = buffer + regla.getKey() + " -> " + value + "\n";
		}
		buffer = buffer + "}\n";

		return buffer;
	}

	public String formatoFicheroParse() {
		String buffer = "Ascendente ";

		for (int x : parse) {
			buffer = buffer + x + " ";
		}

		return buffer + "\n";
	}
	public Stack<String> getPila() {
		return pila;
	}
	public void setPila(Stack<String> pila) {
		this.pila = pila;
	}
	public Automata getAutomata() {
		return automata;
	}
	public void setAutomata(Automata automata) {
		this.automata = automata;
	}

}