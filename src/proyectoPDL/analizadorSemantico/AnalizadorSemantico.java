package proyectoPDL.analizadorSemantico;

import java.util.Stack;

import Principal.TablaDeSimbolos;
import proyectoPDL.analizadorLexico.Entrada;
import proyectoPDL.analizadorLexico.Entrada.E_Tipo;
import proyectoPDL.gestorErrores.ErrorSemantico;
import proyectoPDL.gestorErrores.GestorErrores;

public class AnalizadorSemantico {

	TablaDeSimbolos tabla;
	Stack<Atributo> pila;

	public Stack<Atributo> getPila() {
		return pila;
	}

	public AnalizadorSemantico(TablaDeSimbolos tabla) {
		this.tabla = tabla;
		this.pila = new Stack<Atributo>();
		pila.push(null);
	}
	/*
	 * ___ ___ ___ _ _ ___ ___ ___ __ __ _ _ _ _____ ___ ___ _ ___
	 * | _ \ __/ __| | /_\ / __| / __| __| \/ | /_\ | \| |_ _|_ _/ __| /_\ / __|
	 * | / _| (_ | |__ / _ \\__ \ \__ \ _|| |\/| |/ _ \| .` | | | | | (__ / _ \\__ \
	 * |_|_\___\___|____/_/ \_\___/ |___/___|_| |_/_/ \_\_|\_| |_| |___\___/_/
	 * \_\___/
	 */

	private Entrada entradaUltimaFuncion;

	public boolean ejecutarAccionSemantica(int regla, int contador) throws ErrorSemantico {
		regla++;
		// Stack<String> pilaASint = pila;
		Stack<Atributo> pilaASem = pila;

		if (regla == 1) {
			/* p’->P */
			Atributo P = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo P_ = new Atributo("P_", E_Tipo.tipo_ok, null);
			if (P.tipo == E_Tipo.tipo_ok) {
				vaciarPila(pilaASem, contador);

				pilaASem.push(P_);
				pilaASem.push(null);
			} else {
				// System.err.println("ERROR: tipo_error");
				// System.exit(0);
				throw new ErrorSemantico("tipo_error");
			}
		} else if (regla == 2) {
			/* P->BP */
			Atributo P = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo B = obtenerEnPilaAtributos(pilaASem, -1);

			if (P.tipo == E_Tipo.tipo_ok || B.tipo == E_Tipo.tipo_ok) {

				Atributo P_ = new Atributo("P", E_Tipo.tipo_ok, null);

				vaciarPila(pilaASem, contador);
				pilaASem.push(P_);
				pilaASem.push(null);

			} else {
				// System.err.println("ERROR: tipo_error");
				// System.exit(0);
				throw new ErrorSemantico("tipo_error");
			}

		} else if (regla == 3) {
			// P->FP
			Atributo F = obtenerEnPilaAtributos(pilaASem, -1);

			Atributo P = new Atributo("P", F.tipo, null);

			vaciarPila(pilaASem, contador);

			pilaASem.push(P);
			pilaASem.push(null);
		} else if (regla == 4) {
			/* P->λ */
			pilaASem.push(new Atributo("P", E_Tipo.tipo_ok, null));
			pilaASem.push(null);
		} else if (regla == 5) {
			// B->if (E) S
			Atributo S = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo E = obtenerEnPilaAtributos(pilaASem, -2);

			Atributo B;

			if (E.tipo == E_Tipo.logico && S.tipo == E_Tipo.tipo_ok) {

				B = new Atributo("B", E_Tipo.tipo_ok, null);
				vaciarPila(pilaASem, contador);
				pilaASem.push(B);
				pilaASem.push(null);

			} else {
				B = new Atributo("B", E_Tipo.tipo_error, null);
				System.err.println("sencencia if incorrecta.");
				// System.exit(0);
				if (E.tipo != E_Tipo.logico)
					throw new ErrorSemantico(
							"En la sentencia if, el valor de la hipotesis es '" + E.tipo + "' en lugar de 'logico'");
				if (S.tipo != E_Tipo.tipo_ok)
					throw new ErrorSemantico("En a sentencia if, el valor de la conclusion es '" + S.tipo
							+ "' en lugar de 'tipo_ok'. Es decir, la conclusión no es valida");

			}

		} else if (regla == 6) {
			/* B-> let id T; */

			Atributo T = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo B;

			if (T.getTipo() == null) {
				B = new Atributo("B", E_Tipo.tipo_error, null);
				// System.exit(0);
				throw new ErrorSemantico("El tipo declarado no es correcto.");
			}

			B = new Atributo("B", E_Tipo.tipo_ok, null);
			Atributo id = obtenerEnPilaAtributos(pilaASem, -2);
			// tabla.insertarDatoTS("tipo", id_atrib.getEnt().ge, T_atrib.getTipo());
			/* ## AVISO ## : hacer los cambios pertinentes en insertarDatoTS */

			if (id.getEnt().setTipo(T.getTipo()) == false) { /* ESTA VARIABLE YA HA SIDO DECLARADA */
				B = new Atributo("B", E_Tipo.tipo_error, null);
				// System.exit(0);
				throw new ErrorSemantico("La variable '" + id.getEnt().getLexema()
						+ "' ya ha sido declarada. Es decir, se esta volviendo a declarar por segunda vez en el mismo ámbito.");
			}
			vaciarPila(pilaASem, contador);
			pilaASem.push(B);
			pilaASem.push(null);
			// tabla.flag_declarando=false;
		} else if (regla == 7) {
			// B->S
			Atributo S = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo B = new Atributo("B", S.getTipo(), null);

			vaciarPila(pilaASem, contador);
			pilaASem.push(B);
			pilaASem.push(null);
		} else if (regla == 8) {
			// B -> while ( E ) { C }

			Atributo C = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo E = obtenerEnPilaAtributos(pilaASem, -4);

			if (E.tipo == E_Tipo.logico && C.tipo == E_Tipo.tipo_ok) {
				Atributo B = new Atributo("B", E_Tipo.tipo_ok, null);

				vaciarPila(pilaASem, contador);
				pilaASem.push(B);
				pilaASem.push(null);

			} else {
				Atributo B = new Atributo("B", E_Tipo.tipo_error, null);
				if (E.tipo != E_Tipo.logico) {
					throw new ErrorSemantico("El valor dentro del parentesis (hipótesis) es tipo '" + E.tipo
							+ "', cuando debería ser de tipo 'logico'.");
				}
				if (C.tipo != E_Tipo.tipo_ok) {
					throw new ErrorSemantico("Las conclusiones no son validas.");
				}

			}

		} else if (regla == 9) {
			// T-> int
			// String nombre, E_Tipo tipo, Entrada ent
			Atributo at1 = new Atributo("T", E_Tipo.ent, null);
			meterEnPilaAtributos(pilaASem, 0, at1, 1);
		} else if (regla == 10) {
			// T-> boolean
			Atributo at1 = new Atributo("T", E_Tipo.logico, null);
			meterEnPilaAtributos(pilaASem, 0, at1, 1);
		} else if (regla == 11) {
			// T-> string
			Atributo at1 = new Atributo("T", E_Tipo.cadena, null);
			meterEnPilaAtributos(pilaASem, 0, at1, 1);
		} else if (regla == 12) {
			// S->id = E;
			Atributo E = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo id = obtenerEnPilaAtributos(pilaASem, -3);
			Atributo S = new Atributo("S", null, null);
			if (E.tipo == id.tipo) {
				S.tipo = E_Tipo.tipo_ok;

				vaciarPila(pilaASem, contador);
				pilaASem.push(S);
				pilaASem.push(null);
			} else {

				S.setTipo(E_Tipo.tipo_error);
				throw new ErrorSemantico("Los tipos de las asignaciones no son equivalentes. '" + id.getTipo() + "' = '"
						+ E.tipo + "'.");
			}

		} else if (regla == 13) {
			// S-> id (L); // Este es de la funciones tipo function (a,b,c,d)
			Atributo L = obtenerEnPilaAtributos(pilaASem, -2);
			Atributo id = obtenerEnPilaAtributos(pilaASem, -4);
			Atributo S = new Atributo("S", null, null);

			if (id.tipo == E_Tipo.function && L.tipo == E_Tipo.tipo_ok) {
				S.setTipo(id.tipo); /* Es una llamada */
				vaciarPila(pilaASem, contador);
				pilaASem.push(S);
				pilaASem.push(null);
			} else {
				S.setTipo(E_Tipo.tipo_error);
				if (id.tipo != E_Tipo.function)
					throw new ErrorSemantico("El id no es tipo funcion");
				if (L.tipo != E_Tipo.tipo_ok)
					throw new ErrorSemantico("Los atributos no son correctos");

			}
		} else if (regla == 14) {
			// S-> put E;
			Atributo E = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo S = new Atributo("S", null, null);

			if (E.tipo == E_Tipo.cadena || E.tipo == E_Tipo.ent) {
				S.setTipo(E_Tipo.tipo_ok);
				vaciarPila(pilaASem, contador);
				pilaASem.push(S);
				pilaASem.push(null);
			} else {
				S.setTipo(E_Tipo.tipo_error);
				throw new ErrorSemantico(
						"Tipo no adecuado. Debería ser o 'cad' o 'ent', pero tenemos: put '" + E.tipo + "';.");

			}
		} else if (regla == 15) {
			// S->get id;
			Atributo id = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo S = new Atributo("S", null, null);

			if (id.tipo == E_Tipo.ent || id.tipo == E_Tipo.cadena) {
				S.tipo = E_Tipo.tipo_ok;
				vaciarPila(pilaASem, contador);
				pilaASem.push(S);
				pilaASem.push(null);

			} else {
				S.tipo = E_Tipo.tipo_ok;
				throw new ErrorSemantico(
						"Tipo no adecuado. Debería ser o 'cad' o 'ent', pero tenemos: get '" + id.tipo + "';.");

			}

		} else if (regla == 16) {
			// S-> return X;
			Atributo X = obtenerEnPilaAtributos(pilaASem, -1);

			if (entradaUltimaFuncion.TipoRetorno() != X.tipo) {
				throw new ErrorSemantico("El valor retorno de la funcion '" + entradaUltimaFuncion.getLexema()
						+ "' no es igual al tipo de la función. Se halló '" + X.tipo + "', y se esperaba '"
						+ entradaUltimaFuncion.TipoRetorno() + "'.");
				// System.exit(0);
			} else {
				Atributo S = new Atributo("S", E_Tipo.tipo_ok, null);
				vaciarPila(pilaASem, contador);
				pilaASem.push(S);
				pilaASem.push(null);
			}

		} else if (regla == 17) {
			// L->EQ
			Atributo E = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo Q = obtenerEnPilaAtributos(pilaASem, 0);

			if (Q.tipo != E_Tipo.tipo_error && E.tipo != E_Tipo.tipo_error) {
				Atributo L = new Atributo("L", E_Tipo.tipo_ok, null);
				vaciarPila(pilaASem, contador);
				pilaASem.push(L);
				pilaASem.push(null);
			} else {
				throw new ErrorSemantico("tipo_error en regla 17.");

			}
		} else if (regla == 18) {
			// L-> λ
			pilaASem.push(new Atributo("L", E_Tipo.tipo_ok, null));
			pilaASem.push(null);

		} else if (regla == 19) {
			// Q-> ,EQ
			Atributo E = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo Q = obtenerEnPilaAtributos(pilaASem, 0);
			if (Q.tipo != E_Tipo.tipo_error && E.tipo != E_Tipo.tipo_error) {
				Atributo Q_ = new Atributo("Q", E_Tipo.tipo_ok, null);
				vaciarPila(pilaASem, contador);
				pilaASem.push(Q_);
				pilaASem.push(null);
			} else {
				throw new ErrorSemantico("tipo_error en regla 19.");
				// System.exit(0);
			}
		} else if (regla == 20) {
			// Q-> λ
			pilaASem.push(new Atributo("Q", E_Tipo.tipo_ok, null));
			pilaASem.push(null);

		} else if (regla == 21) {
			// X-> E
			Atributo E = obtenerEnPilaAtributos(pilaASem, 0);

			Atributo X = new Atributo("X", E.tipo, null);

			vaciarPila(pilaASem, contador);

			pilaASem.push(X);
			pilaASem.push(null);
		} else if (regla == 22) {
			// X-> λ
			pilaASem.push(new Atributo("X", E_Tipo.tipo_ok, null));
			pilaASem.push(null);

		} else if (regla == 23) {
			// E -> E || R
			Atributo R = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo E = obtenerEnPilaAtributos(pilaASem, -2);
			Atributo E_ = new Atributo("E", null, null);
			if (E.tipo == E_Tipo.logico && R.tipo == E_Tipo.logico) {
				E.setTipo(E_Tipo.logico);
				vaciarPila(pilaASem, contador);
				pilaASem.push(E);
				pilaASem.push(null);
			} else {
				E_.setTipo(E_Tipo.tipo_error);
				throw new ErrorSemantico(
						"Operacion OR entre tipos incorrectos. Los 2 operandos deberian ser lógicos, sin embargo tenemos: '"
								+ E.tipo + "' || '" + R.tipo + "'.");
			}
		} else if (regla == 24) {
			// E-> R
			Atributo R = obtenerEnPilaAtributos(pilaASem, 0);

			Atributo E = new Atributo("E", R.tipo, null);

			vaciarPila(pilaASem, contador);

			pilaASem.push(E);
			pilaASem.push(null);
		} else if (regla == 25) {
			// R -> R&&U
			Atributo U = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo R = obtenerEnPilaAtributos(pilaASem, -2);
			Atributo R_ = new Atributo("R", null, null);
			if (U.tipo == E_Tipo.logico && R.tipo == E_Tipo.logico) {
				R_.setTipo(E_Tipo.logico);
				vaciarPila(pilaASem, contador);
				pilaASem.push(R_);
				pilaASem.push(null);
			} else {
				R_.setTipo(E_Tipo.tipo_error);
				throw new ErrorSemantico(
						"Operacion AND entre tipos incorrectos. Los 2 operandos deberian ser lógicos, sin embargo tenemos: '"
								+ R.tipo + "' && '" + U.tipo + "'.");

			}
		} else if (regla == 26) {
			// R-> U
			Atributo U = obtenerEnPilaAtributos(pilaASem, 0);

			Atributo R = new Atributo("R", U.tipo, null);

			vaciarPila(pilaASem, contador);

			pilaASem.push(R);
			pilaASem.push(null);
		} else if (regla == 27) {
			// U-> U = = V
			Atributo U = obtenerEnPilaAtributos(pilaASem, -2);
			Atributo V = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo U_ = new Atributo("U", null, null);
			if ((U.tipo == V.tipo) && (U.tipo == E_Tipo.ent)) {
				U_.tipo = E_Tipo.logico;
				vaciarPila(pilaASem, contador);
				pilaASem.push(U_);
				pilaASem.push(null);
			} else {
				U_.tipo = E_Tipo.tipo_error;
				throw new ErrorSemantico(
						"Operacion EQUALS entre tipos incorrectos. Los 2 operandos deberian ser 'ent', sin embargo tenemos: '"
								+ U.tipo + "' == '" + V.tipo + "'.");
			}

		} else if (regla == 28) {
			// U-> U != V
			Atributo U = obtenerEnPilaAtributos(pilaASem, -2);
			Atributo V = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo U_ = new Atributo("U", null, null);
			if ((U.tipo == V.tipo) && (U.tipo == E_Tipo.ent)) {
				U_.tipo = E_Tipo.logico;
			} else {
				U_.tipo = E_Tipo.tipo_error;
				throw new ErrorSemantico(
						"Operacion NO EQUALS entre tipos incorrectos. Los 2 operandos deberian ser 'ent', sin embargo tenemos: '"
								+ U.tipo + "' != '" + V.tipo + "'.");

			}

			vaciarPila(pilaASem, contador);
			pilaASem.push(U_);
			pilaASem.push(null);
		} else if (regla == 29) {
			// U-> V
			Atributo V = obtenerEnPilaAtributos(pilaASem, 0);

			Atributo U = new Atributo("U", V.tipo, null);

			vaciarPila(pilaASem, contador);

			pilaASem.push(U);
			pilaASem.push(null);

		} else if (regla == 30) {
			// V-> V + W
			Atributo V = obtenerEnPilaAtributos(pilaASem, -2);
			Atributo W = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo V_antecedente = new Atributo("V", null, null);
			if (V.tipo == E_Tipo.ent && W.tipo == E_Tipo.ent) {
				V_antecedente.tipo = E_Tipo.ent;
			} else {
				V_antecedente.tipo = E_Tipo.tipo_error;
				throw new ErrorSemantico(
						"Operacion SUMA entre tipos incorrectos. Los 2 operandos deberian ser 'ent', sin embargo tenemos: '"
								+ V.tipo + "' + '" + W.tipo + "'.");
			}

			vaciarPila(pilaASem, contador);
			pilaASem.push(V_antecedente);
			pilaASem.push(null);
		} else if (regla == 31) {
			// V->V-W
			Atributo V = obtenerEnPilaAtributos(pilaASem, -2);
			Atributo W = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo V_antecedente = new Atributo("V", null, null);
			if (V.tipo == E_Tipo.ent && W.tipo == E_Tipo.ent) {
				V_antecedente.tipo = E_Tipo.ent;
			} else {
				V_antecedente.tipo = E_Tipo.tipo_error;
				throw new ErrorSemantico(
						"Operacion RESTA entre tipos incorrectos. Los 2 operandos deberian ser 'ent', sin embargo tenemos: '"
								+ V.tipo + "' - '" + W.tipo + "'.");
			}

			vaciarPila(pilaASem, contador);
			pilaASem.push(V_antecedente);
			pilaASem.push(null);
		} else if (regla == 32) {
			// V->W
			Atributo W = obtenerEnPilaAtributos(pilaASem, 0);

			if (W.getTipo() == E_Tipo.cadena || W.getTipo() == E_Tipo.ent || W.getTipo() == E_Tipo.logico
					|| W.getTipo() == E_Tipo.function) {

				Atributo V = new Atributo("V", W.tipo, null);

				vaciarPila(pilaASem, contador);

				pilaASem.push(V);
				pilaASem.push(null);
			} else {

				throw new ErrorSemantico("tipo_error en regla 32");

			}
		} else if (regla == 33) {
			// W-> id
			Atributo id = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo W = new Atributo("W", id.tipo, null);
			vaciarPila(pilaASem, contador);

			pilaASem.push(W);
			pilaASem.push(null);
		} else if (regla == 34) {
			// W-> ( E ) // Esto es para las de tipo (a+b) --> ( E ) oo (a!=b) --> ( E )
			Atributo E = obtenerEnPilaAtributos(pilaASem, -1);

			if (E.tipo == E_Tipo.logico || E.tipo == E_Tipo.ent || E.tipo == E_Tipo.cadena) {

				Atributo W = new Atributo("W", E.tipo, null);
				vaciarPila(pilaASem, contador);

				pilaASem.push(W);
				pilaASem.push(null);

			} else {
				throw new ErrorSemantico(
						"El valor entre parentesis deberia ser o bien 'logico' o 'ent', sin embargo tenemos: ('"
								+ E.tipo + "')");
				// System.exit(0);
			}
		} else if (regla == 35) {
			// W-> id ( L ) ## esta es para tipos "function(id,id,num...)" ##

			Atributo L = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo id = obtenerEnPilaAtributos(pilaASem, -3);
			if (id.tipo == E_Tipo.function && L.tipo == E_Tipo.tipo_ok) {

				Atributo W = new Atributo("W", id.ent.TipoRetorno(), null);
				vaciarPila(pilaASem, contador);

				pilaASem.push(W);
				pilaASem.push(null);

			} else {

				if (id.tipo != E_Tipo.function)
					throw new ErrorSemantico("El identificador '" + id.getEnt().getLexema() + "' es de tipo '" + id.tipo
							+ "', cuando debería ser de tipo 'function'. (Es decir, no esta declarada)");
				if (L.tipo != E_Tipo.tipo_ok)
					throw new ErrorSemantico("Los argumentos dentre parentesis no son validos");

			}

		} else if (regla == 36) {
			// W-> ent
			Atributo W = new Atributo("W", E_Tipo.ent, null);

			vaciarPila(pilaASem, contador);
			pilaASem.push(W);
			pilaASem.push(null);

		} else if (regla == 37) {
			// W-> cad
			Atributo W = new Atributo("W", E_Tipo.cadena, null);
			vaciarPila(pilaASem, contador);
			pilaASem.push(W);
			pilaASem.push(null);
		} else if (regla == 38) {
			// W-> --id
			Atributo id = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo W_antecedente = new Atributo("W", null, null);
			if (id.tipo == E_Tipo.ent) {
				W_antecedente.tipo = E_Tipo.ent;
			} else {
				W_antecedente.tipo = E_Tipo.tipo_error;
				throw new ErrorSemantico("Se esta intentando PREDECREMENTAR un valor que no es entero. Tenemos: --'"
						+ id.getTipo() + "'.");
			}

			vaciarPila(pilaASem, contador);
			pilaASem.push(W_antecedente);
			pilaASem.push(null);
		} else if (regla == 39) {
			// F-> F1 { C }
			Atributo C = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo F1 = obtenerEnPilaAtributos(pilaASem, -3);
			tabla.flag_en_Local = false;
			entradaUltimaFuncion = null;

			if (C.tipo == F1.tipo && F1.tipo == E_Tipo.tipo_ok) {
				Atributo F = new Atributo("F", E_Tipo.tipo_ok, null);

				vaciarPila(pilaASem, contador);

				pilaASem.push(F);
				pilaASem.push(null);
			} else {

				if (F1.tipo != E_Tipo.tipo_ok)
					throw new ErrorSemantico("La funcion tiene un tipo incorrecto");
				if (C.tipo != E_Tipo.tipo_ok)
					throw new ErrorSemantico("El contenido de la funcion es incorrecto");

			}
		} else if (regla == 40) {
			// F1-> F2 ( A )
			tabla.flag_atributos_funcion = false;
			Atributo F2 = obtenerEnPilaAtributos(pilaASem, -3);
			Atributo A = obtenerEnPilaAtributos(pilaASem, -1);

			if (F2.tipo == E_Tipo.tipo_ok && (A.tipo == E_Tipo.tipo_ok || A.tipo == E_Tipo.vacio)) {

				Atributo F1 = new Atributo("F1", E_Tipo.tipo_ok, null);

				vaciarPila(pilaASem, contador);

				pilaASem.push(F1);
				pilaASem.push(null);

			} else {

				if (F2.tipo != E_Tipo.tipo_ok)
					throw new ErrorSemantico("La declaracion de la funcion es incorrecta");
				if (A.tipo != E_Tipo.tipo_ok)
					throw new ErrorSemantico("La declaracion de los argumetos de la funcion es incorrecta");

			}

		}

		else if (regla == 41) {
			// F2-> function id H
			tabla.crearTablaLocal();
			tabla.flag_en_Local = true;
			tabla.flag_atributos_funcion = true;
			/*
			 * crear la tabla
			 * EL A.Lex ya ha creado la variable id en la tabla global
			 * cogemos id y en su entrada le metemos su tipo de retorno
			 * obtener id.tipo := H.tipo
			 * 
			 * colapsamos la pila 3*(2) posiciones
			 */

			Atributo F2 = new Atributo("F2", null, null);
			Atributo H = obtenerEnPilaAtributos(pilaASem, 0);
			if (H.tipo == E_Tipo.vacio || H.tipo == E_Tipo.cadena || H.tipo == E_Tipo.ent || H.tipo == E_Tipo.logico) {
				Atributo id = obtenerEnPilaAtributos(pilaASem, -1);
				id.ent.setTipoRetorno(H.tipo);
				id.ent.setTipo(E_Tipo.function);
				F2.tipo = E_Tipo.tipo_ok;
				F2.ent = id.ent;

				entradaUltimaFuncion = id.ent;

			} else {

				throw new ErrorSemantico("La declaracion de la funcion es incorrecta");
			}

			vaciarPila(pilaASem, contador);

			pilaASem.push(F2);
			pilaASem.push(null);

		}

		else if (regla == 42) {
			// H->T
			Atributo T = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo H = new Atributo("H", null, null);
			H.tipo = T.tipo;

			vaciarPila(pilaASem, contador);

			pilaASem.push(H);
			pilaASem.push(null);
			// H_atrib.setTipo(T_atrib.getTipo());
		} else if (regla == 43) {
			// H-> void
			Atributo H = new Atributo("H", E_Tipo.vacio, null);
			vaciarPila(pilaASem, contador);
			pilaASem.push(H);
			pilaASem.push(null);
		} else if (regla == 44) {
			// A-> T id K
			Atributo T = obtenerEnPilaAtributos(pilaASem, -2);
			Atributo id = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo K = obtenerEnPilaAtributos(pilaASem, 0);

			if (T.tipo == E_Tipo.ent || T.tipo == E_Tipo.cadena || T.tipo == E_Tipo.logico) {

				id.ent.setTipo(T.tipo);
				entradaUltimaFuncion.setTipoParam(T.tipo);
				Atributo A = new Atributo("A", null, null);
				A.setTipo(E_Tipo.tipo_ok);

				/*
				 * EL ANALIZADOR LEXICO YA HA INTORODUCIDO
				 * EL VALOR de ID EN LA ENTRADA DONDE CORRESPONDE
				 */

				vaciarPila(pilaASem, contador);

				pilaASem.push(A);
				pilaASem.push(null);

			} else {
				throw new ErrorSemantico("El argumento '" + id.getEnt().getLexema()
						+ "' se esta intentando declara con un tipo incorrecto. Deberia ser logico, cadena o entero.");
			}

		} else if (regla == 45) {
			// A-> void
			Atributo A = new Atributo("A", E_Tipo.vacio, null);
			vaciarPila(pilaASem, contador);
			pilaASem.push(A);
			pilaASem.push(null);
		} else if (regla == 46) {
			// K -> , T id K
			Atributo K = obtenerEnPilaAtributos(pilaASem, 0);
			Atributo id = obtenerEnPilaAtributos(pilaASem, -1);

			Atributo T = obtenerEnPilaAtributos(pilaASem, -2);

			if ((T.tipo == E_Tipo.ent || T.tipo == E_Tipo.cadena || T.tipo == E_Tipo.logico)
					&& K.tipo == E_Tipo.tipo_ok) {
				/*
				 * EL ANALIZADOR LEXICO YA HA INTORODUCIDO
				 * EL VALOR EN LA ENTRADA DONDE CORRESPONDE
				 */
				id.ent.setTipo(T.tipo);
				entradaUltimaFuncion.setTipoParam(T.tipo);

				Atributo K_ = new Atributo("K", E_Tipo.tipo_ok, null);
				vaciarPila(pilaASem, contador);

				pilaASem.push(K_);
				pilaASem.push(null);

			} else {
				throw new ErrorSemantico("El argumento '" + id.getEnt().getLexema()
						+ "' se esta intentando declara con un tipo incorrecto. Deberia ser logico, cadena o entero.");
			}

		} else if (regla == 47) {
			// K-> λ
			pilaASem.push(new Atributo("K", E_Tipo.tipo_ok, null));
			pilaASem.push(null);
		} else if (regla == 48) {
			// C -> B C
			Atributo B = obtenerEnPilaAtributos(pilaASem, -1);
			Atributo C = obtenerEnPilaAtributos(pilaASem, 0);

			if (B.tipo == E_Tipo.tipo_ok) {
				Atributo C_ = new Atributo("C", B.tipo, null);
				vaciarPila(pilaASem, contador);

				pilaASem.push(C_);
				pilaASem.push(null);

			} else {
				throw new ErrorSemantico("tipo_error regla 48.");
			}

		} else if (regla == 49) {
			// C-> λ
			pilaASem.push(new Atributo("C", E_Tipo.tipo_ok, null));
			pilaASem.push(null);
		}

		return false;
	}

	public void vaciarPila(Stack<Atributo> pilila, int contador) {

		for (int i = 0; i < contador * 2; i++) {
			pilila.pop();
		}
	}

	/*
	 * Sustituimos
	 * 0 ---> en el tope de la pila
	 * -1 -> justo debajo del tope
	 * -2 ...
	 * 
	 */
	public void meterEnPilaAtributos(Stack<Atributo> pilila, int relPos, Atributo at, int modo) { /* modo 1 */
		if (modo == 1) {
			pilila.set(pilila.size() - 1 + relPos * 2 - 1, at);
		}
		/* modo 0 */
		else if (modo == 0) {
			pilila.set(pilila.size() - 1 + relPos, at);
		}
	}

	public Atributo obtenerEnPilaAtributos(Stack<Atributo> pilila, int relPos) {

		return pilila.get(pilila.size() - 1 + relPos * 2 - 1);
	}

}
