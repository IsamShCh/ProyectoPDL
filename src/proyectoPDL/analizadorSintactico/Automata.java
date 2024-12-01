package proyectoPDL.analizadorSintactico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Automata {
	LR1 lr1;
	List<Estado> listaEstados;
	Map<Estado, Map<String, Estado>> listaTransiciones;
	Boolean prints = false;
	public Map<String, Pair<Character, Integer>> Accion;
	public Map<String, Integer> GOTO;

	public Automata(LR1 lr1) {
		this.lr1 = lr1;
		this.listaEstados = new ArrayList<>();
		this.listaTransiciones = new HashMap<>();
		this.Accion = new HashMap<String, Pair<Character, Integer>>();
		this.GOTO = new HashMap<String, Integer>();
	}

	// N,P,S,*,(,),
	public void construct() {
		// Crear el estado inicial
		ArrayList<String> temp = lr1.gram.getCharacters();
		Set<Item> init = new HashSet<>();
		Pair<String, String> primera_regla = lr1.gram.getProduccionesNumero(0);
		Item inicio = new Item(primera_regla.getKey(), primera_regla.getValue(), 0);
		// init.add(new Item("Y", "E", 0));
		init.add(inicio);
		Estado initialState = new Estado(0, lr1.cierre(init));
		listaEstados.add(initialState);

		// Crear los demás estados y transiciones
		int i = 0;
		while (i < listaEstados.size()) {
			Estado state = listaEstados.get(i);
			if (prints)
				System.out.println("Obtenemos el estado numero " + i + " que contiene:\n" + state + "\n----------\n");
			Map<String, Estado> stateTransitions = new HashMap<>();

			// for (String symbol : lr1.getGrammar().getProductions().keySet()) {

			for (String symbol : temp) {
				if (prints)
					System.out.println("Transicion " + symbol);
				if (prints)
					System.out.println("Hacemos GOTO desde el estado I" + i);
				Set<Item> gotoItems = lr1.GOTO(state.items, symbol);
				if (prints)
					System.out.println(gotoItems.toString());

				if (!gotoItems.isEmpty()) {
					// busca en todos los estados existentes si existe una lista de items que sea
					// igual a la que hemos obtenido antes
					// si existe, se devuelve ese estado
					// si no existe, se crea ese estado
					Estado gotoState = buscaOCrea(gotoItems);
					stateTransitions.put(symbol, gotoState);
				}
			}

			listaTransiciones.put(state, stateTransitions);
			i++;
		}
	}

	public void constructTabla() {
		for (Estado estadoActual : listaEstados) {
			int numeroEstadoActual = estadoActual.id;

			for (Item it : estadoActual.items) {
				String simboloDespuesPunto = it.getSimboloPunto(); // obtenemos el simbolo despues del punto
				// A-> alfa . a beta
				if (simboloDespuesPunto != null) // no esta al final
				{
					if (!lr1.gram.producciones.containsKey(simboloDespuesPunto)) {
						String key = numeroEstadoActual + simboloDespuesPunto;
						// lr1.GOTO(new Set<Item>(it), puntoD);
						Estado estadoSiguiente = listaTransiciones.get(estadoActual).get(simboloDespuesPunto); // en que
																												// estado
																												// terminamos
																												// patiendo
																												// del
																												// estado
																												// actual
																												// y
																												// transicionando
																												// con
																												// el
																												// elemento
																												// que
																												// haya
																												// despues
																												// del
																												// punto
						if (estadoSiguiente != null) // devolverá null si no existe esa transicion.
						{
							int numeroEstadoSiguente = estadoSiguiente.id;
							if (Accion.containsKey(key)) {
								if (!Accion.get(key).equals(new Pair<Character, Integer>('d', numeroEstadoSiguente)))
									System.out.println("ERROR\n");
							}
							Accion.put(key, new Pair<Character, Integer>('d', numeroEstadoSiguente));
							// GOTO.put(key, null)
						}
					}

				} else // esta al final
				{
					// ITEM:
					// A -> alfa .
					// A!=Y
					String A = it.izquierda;
					if (!A.equals("Y")) {

						// Accion[numero estado actual, x] = reducir + posicion de A en la gramatica
						// F -> xA | Bb
						// items de un estado F -> xA.

						// for(String prod : lr1.gram.producciones.get(it.izquierda) ) //obtenemos todas
						// las producciones de A :: A -> alfa .
						// {
						// x = FOLLOW(lo que haya a la derecha de la A en la gramatica)
						// Set<String> listaFollow = lr1.gram.follow(String.valueOf(prod.charAt(0))); //
						// este es nuestra x
						Set<String> listaFollow = lr1.gram.follow(A);
						for (String x : listaFollow) {
							String key = numeroEstadoActual + x;

							// L -> lambda L lambda --> numero x L null ->numero
							String consecuente = it.derecha;
							if (consecuente.equals("")) {
								consecuente = "λ";
							}
							int posicionGram = lr1.gram.ordenProducciones.get(A).get(consecuente);

							if (Accion.containsKey(key)) {
								if (!Accion.get(key).equals(new Pair<Character, Integer>('r', posicionGram)))
									System.out.println("ERROR\n");
							}

							Accion.put(key, new Pair<Character, Integer>('r', posicionGram));
						}
						// }
						// lr1.gram.follow(simboloDespuesPunto)

					}
					// ITEM:
					// Y -> S.
					else {
						// Accion[estado actual,$] = aceptar
						String key = numeroEstadoActual + "$";
						// int posicionGram = lr1.gram.ordenProducciones.get(A).get(it.derecha);

						if (Accion.containsKey(key)) {
							if (!Accion.get(key).equals(new Pair<Character, Integer>('a', null)))
								System.out.println("ERROR\n");
						}

						Accion.put(key, new Pair<Character, Integer>('a', null));
					}
				}
			}

			// para todos los no terminales de A que cumplan GOTO
			for (Map.Entry<String, Estado> entrada : listaTransiciones.get(estadoActual).entrySet()) // recorremos todas
																										// las
																										// transiciones
																										// posibles del
																										// estado X
			{
				String non_terminal = entrada.getKey();
				if (lr1.gram.producciones.containsKey(non_terminal)) // si la transicion es de un simbolo no terminal,
																		// entonces:
				{
					String key = estadoActual.id + non_terminal;

					if (GOTO.containsKey(key)) {
						if (!GOTO.get(key).equals(entrada.getValue().id))
							System.out.println("ERROR\n");
					}

					GOTO.put(key, entrada.getValue().id);
				}
			}
		}

	}

	private Estado buscaOCrea(Set<Item> items) {
		for (Estado state : listaEstados) {
			if (state.items.equals(items)) {
				return state;
			}
		}

		Estado nuevoEstado = new Estado(listaEstados.size(), items);
		listaEstados.add(nuevoEstado);
		return nuevoEstado;
	}

	public void print() {
		for (Estado state : listaEstados) {
			System.out.println("Estado " + state.id + ":");
			for (Item item : state.items) {
				System.out.println("  " + item);
			}
			System.out.println("---TRANSISICIONES del estado " + state.id + " ---");
			Map<String, Estado> estadoTransiciones = listaTransiciones.get(state);
			if (estadoTransiciones != null) {
				for (Map.Entry<String, Estado> entrada : estadoTransiciones.entrySet()) {
					System.out.println("  " + entrada.getKey() + " -> Estado " + entrada.getValue().id);
				}
			}
			System.out.println();
		}
	}

	// Accion[estado, a]
	public Pair<Character, Integer> Accion(String estado, String transicion) {
		return Accion.get(estado + transicion);
	}

	public Integer GOTO(String estado, String transicion) {
		return GOTO.get(estado + transicion);
	}

	public void printTabla() {
		for (Estado state : listaEstados) {
			System.out.println("Estado " + state.id + ":");
			for (Item item : state.items) {
				System.out.println("  " + item);
			}
			System.out.println("---TRANSISICIONES del estado " + state.id + " ---");
			Map<String, Estado> estadoTransiciones = listaTransiciones.get(state);
			if (estadoTransiciones != null) {
				for (Map.Entry<String, Estado> entrada : estadoTransiciones.entrySet()) {
					System.out.println("  " + entrada.getKey() + " -> Estado " + entrada.getValue().id);
				}
			}

		}
	}

	public String toDot() {
		StringBuilder dot = new StringBuilder("digraph Automata {\n");

		for (Estado state : listaEstados) {
			dot.append("    ").append(state.id).append(" [label=\"Estado ").append(state.id).append("\"];\n");
		}

		for (Map.Entry<Estado, Map<String, Estado>> entry : listaTransiciones.entrySet()) {
			Estado fromState = entry.getKey();
			for (Map.Entry<String, Estado> transition : entry.getValue().entrySet()) {
				String symbol = transition.getKey();
				Estado toState = transition.getValue();
				dot.append("    ").append(fromState.id).append(" -> ").append(toState.id).append(" [label=\"")
						.append(symbol).append("\"];\n");
			}
		}

		dot.append("}\n");
		return dot.toString();
	}

	public static String sustitucionSimbolos(String input) {
		input = input.replace("α", " if ");
		input = input.replace("β", " id ");
		input = input.replace("γ", " let ");
		input = input.replace("δ", " while ");
		input = input.replace("ε", " boolean ");
		input = input.replace("ζ", " put ");
		input = input.replace("η", " return ");
		input = input.replace("θ", " get ");
		input = input.replace("ι", " || ");
		input = input.replace("κ", " && ");
		input = input.replace("ω", " == ");
		input = input.replace("μ", " F1 ");
		input = input.replace("ν", " F2 ");
		input = input.replace("ξ", " cad ");
		input = input.replace("ο", " ent ");
		input = input.replace("π", " function ");
		input = input.replace("ρ", " void ");
		input = input.replace("σ", " int ");
		input = input.replace("τ", " string ");
		input = input.replace("ψ", " -- ");
		input = input.replace("υ", " - ");
		input = input.replace("φ", " != ");

		return input;
	}

}
