package proyectoPDL.analizadorSemantico;

import proyectoPDL.analizadorLexico.Entrada.E_Tipo;
import proyectoPDL.analizadorLexico.Entrada;

public class Atributo {

	public String nombre;
	public E_Tipo tipo;
	// E_Valor valor;
	public Entrada ent;

	public Atributo(String nombre, E_Tipo tipo, Entrada ent) {
		this.nombre = nombre;
		this.tipo = tipo;
		this.ent = ent;
	}

	public Entrada getEnt() {
		return this.ent;
	}

	public E_Tipo getTipo() {
		return this.tipo;
	}

	public void setTipo(E_Tipo tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String toString() {
		return "|" + nombre + ":" + getTipo() + "|";
	}

}
