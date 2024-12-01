package Principal;

import java.util.ArrayList;

import proyectoPDL.analizadorLexico.*;
import proyectoPDL.analizadorLexico.Entrada.E_Tipo;
import proyectoPDL.analizadorSintactico.Pair;

public class TablaDeSimbolos {

	private ArrayList<ArrayList<Entrada>> tablasDeSimbolos;
	int contador_id = 0;
	public boolean flag_en_Local = false;
	public boolean flag_let = false;
	public boolean flag_atributos_funcion = false;
	private int var_cont = 0;

	// public boolean flag_declarando = false;

	public ArrayList<Entrada> TablaActual() {
		if (flag_en_Local == false) {
			return tablasDeSimbolos.get(0);

		} else {
			return tablasDeSimbolos.get(tablasDeSimbolos.size() - 1);
			// return tablasDeSimbolos.getLast();
		}
	}

	public boolean flagLocal() {
		return flag_en_Local;
	}

	/* ## AVISO ## :: no lo estamos usando xd */
	public void insertarDatoTS(String modo, int idPos, Object contenido) {
		Pair<Integer, Integer> postablaYentrada = obtenerNumeroEntradaXidPos(idPos);
		int idTabla = postablaYentrada.getKey();
		int numEntrada = postablaYentrada.getValue();

		if (modo.toLowerCase().equals("tipo")) {
			Entrada ent;
			E_Tipo _contenido_ = (E_Tipo) contenido;
			ent = getEntradaTS(idTabla, numEntrada);
			ent.setTipo(_contenido_);
			calcularDesplazamiento(idTabla, numEntrada); /*
															 * este metodo se encarga de ir a la entread y darle un
															 * valor de desplazamiento ## ATENCION ## : posiblemente
															 * pueda haber errores aqui por que devuelve -1 o -2
															 */

		} else if (modo.toLowerCase().equals("tipoParam")) {
			E_Tipo _contenido_ = (E_Tipo) contenido;
			Entrada ent = getEntradaTS(idTabla, numEntrada);
			ent.setTipoParam(_contenido_);

		} else if (modo.toLowerCase().equals("tiporetorno")) {
			E_Tipo _contenido_ = (E_Tipo) contenido;
			Entrada ent = getEntradaTS(idTabla, numEntrada);
			ent.setTipoRetorno(_contenido_);

		} else if (modo.toLowerCase().equals("setnumparam")) {
			int _contenido_ = (int) contenido;
			Entrada ent = getEntradaTS(idTabla, numEntrada);
			ent.setNumParam(_contenido_);

		} else if (modo.toLowerCase().equals("setnumparam")) {
			int _contenido_ = (int) contenido;
			Entrada ent = getEntradaTS(idTabla, numEntrada);
			ent.setNumParam(_contenido_);

		}
	}

	public Pair<Integer, Integer> obtenerNumeroEntradaXidPos(int idPos) {
		idPos = idPos + 1; // porque empieza en 0
		Pair<Integer, Integer> par = new Pair<Integer, Integer>(null, null);
		int total_listas = tablasDeSimbolos.size();
		ArrayList<Entrada> se_encuentra_aqui = null;
		int idLista = 0;
		for (ArrayList<Entrada> lista : tablasDeSimbolos) {
			int tam = lista.size();
			idLista = idLista - tam;
			if (idLista < 0) {
				se_encuentra_aqui = lista;
				break;
			}
			idLista++;
		}

		int tam = se_encuentra_aqui.size();
		int pos = tam + idLista;

		return new Pair<Integer, Integer>(pos - 1, idLista);
	}

	public Entrada obtenerEntradaXidPos(int identificador) {
		Entrada entrada_encontrada = null;
		for (ArrayList<Entrada> lista : tablasDeSimbolos) {
			for (Entrada ent : lista) {
				if (ent.getIdentificador() == identificador) {
					entrada_encontrada = ent;
					break;
				}

			}
		}

		return entrada_encontrada;
	}

	public boolean calcularDesplazamiento(int idLista, int idPos) {
		if (tablasDeSimbolos.get(idLista).size() == 0) {
			Entrada ent = tablasDeSimbolos.get(idLista).get(idPos);
			int tam = obtenerTamanoTipo(ent);
			ent.setDesplazamiento(tam);
			return true;
		} else {
			Entrada ent = tablasDeSimbolos.get(idLista).get(idPos);
			Entrada ent_ant = tablasDeSimbolos.get(idLista).get(idPos - 1);
			int tam = obtenerTamanoTipo(ent);
			int desp = ent_ant.getDesplazamiento();
			ent.setDesplazamiento(desp + tam);
			return true;
		}

	}

	public void calcularDesplazamiento2(ArrayList<Entrada> lista, Entrada ent) {
		if (lista.size() == 0) {

		} else if (lista.size() == 1) {
			// Entrada ent = lista.get(idPos);
			// int tam = obtenerTamanoTipo(ent);
			ent.setDesplazamiento(0);
		} else {
			int idPos = lista.indexOf(ent);
			if (idPos == 0) {
				ent.setDesplazamiento(0);
			} else {
				Entrada ent_ant = lista.get(idPos - 1);
				int tam = obtenerTamanoTipo(ent_ant);
				int desp = ent_ant.getDesplazamiento();
				ent.setDesplazamiento(desp + tam);

			}
		}
	}

	public int obtenerTamanoTipo(Entrada ent) {
		E_Tipo tipo = ent.getTipo();
		if (tipo == null) {

			return -1; /* con este valor notificamos que esta entrada no tiene tipo definido */
		}

		if (tipo == E_Tipo.ent) {
			return 1;

		} else if (tipo == E_Tipo.logico) {
			return 1;
		} else if (tipo == E_Tipo.vacio) {
			return 0; /* ## AVISO ## : NO SE SI ESTE ES EL VALOR QUE DEBERIA DEVOLVER */
		} else if (tipo == E_Tipo.cadena) {
			return 64; /* ## AVISO ## : NO SE SI ESTE ES EL VALOR QUE DEBERIA DEVOLVER */
		}

		return -1; /* con este valor notificamos que tenemos una situacion inedita */
	}

	public int crearTablaLocal() {
		tablasDeSimbolos.add(new ArrayList<Entrada>());
		return tablasDeSimbolos.size() - 1;
	}

	public Entrada getEntradaTS(int idLista, int idEntrada) {
		return tablasDeSimbolos.get(idLista).get(idEntrada);
	}

	public TablaDeSimbolos() {
		tablasDeSimbolos = new ArrayList<ArrayList<Entrada>>();
		tablasDeSimbolos.add(new ArrayList<Entrada>());
	}

	public ArrayList<Entrada> getTablaDeSimbolosGeneral() {
		return tablasDeSimbolos.get(0);
	}

	public ArrayList<Entrada> getUltimaTablaDeSimbolosLocal() {
		return tablasDeSimbolos.get(tablasDeSimbolos.size() - 1);
		// return tablasDeSimbolos.getLast();
	}

	public ArrayList<ArrayList<Entrada>> getTablaDeSimbolos() {
		return tablasDeSimbolos;
	}

	public int agregarEntrada(int idLista, Entrada entrada) {
		/* SI NO EXISTE, AGREGA LA ENTRADA Y DEVUELVE SU INDICE */
		if (!contieneEntrada(idLista, entrada)) {

			tablasDeSimbolos.get(idLista).add(entrada);
			int idRetorno = contador_id;
			contador_id++;
			entrada.setIdentificador(idRetorno);
			return idRetorno;
		}
		/* SI EXISTE, NO AGREGA NADA, Y DEVUELVE SU INDICE */
		else {
			/*
			 * Buscamos el indice de la entrada que coincide, obtenemos esa entrada, y
			 * devolvemos su indice
			 */
			int idEntrada = tablasDeSimbolos.get(idLista).indexOf(entrada); // buscamos en la lista una entrada
			Entrada ent = tablasDeSimbolos.get(idLista).get(idEntrada);
			return ent.getIdentificador();
		}
	}

	public int agregarEntrada2(Entrada entrada) {

		if (flag_atributos_funcion == true) { /* ESTAMOS DENTRO DEL PARENTESIS DE UNA FUNCION */

			getUltimaTablaDeSimbolosLocal().add(entrada);
			int indice = getUltimaTablaDeSimbolosLocal().indexOf(entrada);
			Entrada ent = getUltimaTablaDeSimbolosLocal().get(indice);
			ent.setIdentificador(contador_id++);
			return ent.getIdentificador();
		}

		/* LOCAL */
		if (flag_en_Local == true) {
			if (getUltimaTablaDeSimbolosLocal().contains(entrada)) { /* YA EXISTE UNA ENTRADA LLAMADA IGUAL */
				int indexEnt = getUltimaTablaDeSimbolosLocal().indexOf(entrada);
				return getUltimaTablaDeSimbolosLocal().get(indexEnt).getIdentificador();
			} else { /* NO EXISTE NINGUNA ENTRADA */

				if (flag_let == true) { /* ESTAMOS DECLARANDO DENTRO DE LA LOCAL */
					/* LE CREAMOS UNA NUEVA ENTRADA */
					getUltimaTablaDeSimbolosLocal().add(entrada);
					entrada.setIdentificador(contador_id++);
					return entrada.getIdentificador();
				} else { /* ESTAMOS EN LOCAL, PERO NO EN SENTENCIA DECLARATIVA */

					/* BUSCAMOS SI EXISTE EN LA GLOBAL */
					if (getTablaDeSimbolosGeneral().contains(entrada)) { /* EXISTE EN LA GLOBAL */
						int indexEnt = getTablaDeSimbolosGeneral().indexOf(entrada);
						return getTablaDeSimbolosGeneral().get(indexEnt).getIdentificador();
					} else { /* NO EXISTE EN LA GLOBAL */
						/* ESTAMOS USANDO UNA VARIABLE QUE NO EXISTE EN NINGUNA PARTE */
						/* la declararemos como tipo int por defectpo */
						getTablaDeSimbolosGeneral().add(entrada);
						entrada.setIdentificador(contador_id++);
						entrada.setTipo(E_Tipo.ent);
						return entrada.getIdentificador();

					}
				}
			}
		}
		/* GLOBAL */
		else {
			/* COMPROBAMOS SI EXISTE O NO EN LA GLOBAL */
			if (getTablaDeSimbolosGeneral().contains(entrada)) { /* EXISTE EN LA GLOBAL */

				/* Obtenemos el id de la entrada que ya existe */

				int indexEnt = getTablaDeSimbolosGeneral().indexOf(entrada);
				return getTablaDeSimbolosGeneral().get(indexEnt).getIdentificador();

			} else { /* NO EXISTE EN LA GLOBAL */

				if (flag_let == true) { /* NO EXISTE Y LA ESTAMOS DECLARANDO */

					getTablaDeSimbolosGeneral().add(entrada);
					entrada.setIdentificador(contador_id);
					contador_id++;
					return entrada.getIdentificador();

				} else { /* NO EXISTE Y NO ESTAMOS DECLARANDO */

					getTablaDeSimbolosGeneral().add(entrada);
					entrada.setIdentificador(contador_id);
					entrada.setTipo(E_Tipo.ent);
					contador_id++;
					return entrada.getIdentificador();
				}

			}
		}
	}

	public boolean contieneEntrada(int idLista, Entrada entrada) {
		return tablasDeSimbolos.get(idLista).contains(entrada);

	}

	public int obtenerIdPos(int idLista, int pos) {
		int tam = 0;

		for (int i = 0; i < idLista; i++) {
			tam += tablasDeSimbolos.get(i).size();
		}

		tam += pos;
		return tam;
	}

	public int obtenerIndice(int idLista, Entrada entrada) {
		return tablasDeSimbolos.get(idLista).indexOf(entrada);
	}

	// public int ultimoOcupado() {
	// return listaGlobal.getValue().size()-1;
	// }
	public void recalcularDesplazamientoEnTodasLasListas() {
		for (ArrayList<Entrada> tabla : tablasDeSimbolos) {
			int ultimoDesp = 0;
			for (Entrada ent : tabla) {
				if (ent.getTipo() == E_Tipo.function) {
					continue;
				} else {
					if (tabla.size() == 1) {
						ent.setDesplazamiento(ultimoDesp);
						ultimoDesp += obtenerTamanoTipo(ent);
					} else {
						int indexEnt = tabla.indexOf(ent);
						if (indexEnt == 0) {
							ent.setDesplazamiento(ultimoDesp);
							ultimoDesp = ultimoDesp + obtenerTamanoTipo(ent);
						} else {
							ent.setDesplazamiento(ultimoDesp);
							ultimoDesp = ultimoDesp + obtenerTamanoTipo(ent);
						}
					}

				}

			}
		}

	}

	public String toString() {
		recalcularDesplazamientoEnTodasLasListas();
		String contenido = "";
		int cont = 1;
		for (ArrayList<Entrada> elemento : tablasDeSimbolos) {

			if (cont == 1) {
				contenido = contenido + "TABLA PRINCIPAL #" + cont + ":\n";

			} else {
				contenido = contenido + "TABLA LOCAL #" + cont + ":\n";
			}

			for (Entrada ent : elemento) {
				contenido = contenido + ent.toString();

			}
			cont++;
		}
		return contenido;
	}

}