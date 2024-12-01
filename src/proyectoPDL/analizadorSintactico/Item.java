package proyectoPDL.analizadorSintactico;

import java.util.Objects;

class Item {
	String izquierda;
	String derecha;
	int puntoPosicion;

	public Item(String izquierda, String derecha, int puntoPosicion) {
		this.izquierda = izquierda;
		if (derecha.equals("λ"))
			derecha = "";
		this.derecha = derecha;
		this.puntoPosicion = puntoPosicion;
	}

	public String getIzquierda() {
		return izquierda;
	}

	public void setIzquierda(String izquierda) {
		this.izquierda = izquierda;
	}

	public String getDerecha() {
		return derecha;
	}

	public void setDerecha(String derecha) {
		this.derecha = derecha;
	}

	public int getDotPos() {
		return puntoPosicion;
	}

	public void setPuntoPosicion(int puntoPosicion) {
		this.puntoPosicion = puntoPosicion;
	}

	public String getSimboloPunto() {
		if (puntoPosicion < derecha.length()) {
			return String.valueOf(derecha.charAt(puntoPosicion));
		} else {
			return null;
		}
	}

	public String toString() {
		String derecha = getDerecha();
		if (derecha.isEmpty()) {
			return getIzquierda() + "->.";
		} else {
			return getIzquierda() + "->" + derecha.substring(0, puntoPosicion) + "."
					+ derecha.substring(puntoPosicion, derecha.length());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Item item = (Item) obj;
		return puntoPosicion == item.puntoPosicion &&
				Objects.equals(izquierda, item.izquierda) &&
				Objects.equals(derecha, item.derecha);
	}

	@Override
	public int hashCode() {
		return Objects.hash(izquierda, derecha, puntoPosicion);
	}

}