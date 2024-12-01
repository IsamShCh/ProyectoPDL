package proyectoPDL.analizadorSintactico;

import java.util.HashSet;
import java.util.Set;

public class LR1 {

	Gramatica gram;

	public LR1(Gramatica gram) {
		this.gram = gram;
	}

	public Gramatica getGramatica() {
		return gram;
	}

	public void setGramatica(Gramatica gram) {
		this.gram = gram;
	}

	// SET{ N->.S , N->.X, J -> .id }

	public Set<Item> cierre(Set<Item> items) {
		Set<Item> conjuntoCerrado = new HashSet<>(items);
		boolean hayCambio;

		do {
			hayCambio = false;
			Set<Item> nuevosItems = new HashSet<>();

			for (Item item : conjuntoCerrado) {
				// F -> (.E)
				String SimboloPostPunto = item.getSimboloPunto();
				// SpP = E
				// N -> id | A=B
				// GRAMMAR Hash<String, List<String>
				//
				if (SimboloPostPunto != null && gram.getProducciones().containsKey(SimboloPostPunto)) {
					// lista --> E -> E+T T λ
					for (String production : gram.getProducciones().get(SimboloPostPunto)) {

						Item nuevoItem = new Item(SimboloPostPunto, production, 0);

						if (!conjuntoCerrado.contains(nuevoItem) && !nuevosItems.contains(nuevoItem)) {
							nuevosItems.add(nuevoItem);
							hayCambio = true;
						}
					}
				}
			}
			conjuntoCerrado.addAll(nuevosItems);
		} while (hayCambio);

		return conjuntoCerrado;
	}

	public Set<Item> GOTO(Set<Item> I, String X) {
		Set<Item> gotoSet = new HashSet<>();

		for (Item item : I) {

			if (X.equals(item.getSimboloPunto())) {
				gotoSet.add(new Item(item.getIzquierda(), item.getDerecha(), item.getDotPos() + 1));
			}
		}

		return cierre(gotoSet);
	}

}

class Estado {
	int id;
	Set<Item> items;

	public Estado(int id, Set<Item> items) {
		this.id = id;
		this.items = items;
	}

	public String toString() {

		return id + ": " + items.toString();
	}
}
