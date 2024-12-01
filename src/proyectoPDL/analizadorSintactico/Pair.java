package proyectoPDL.analizadorSintactico;

import java.util.Objects;

public class Pair<K, V> {

	private final K key;
	private final V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Pair<K,V> pair = (Pair<K,V>) obj;
		return 
				Objects.equals(key, pair.key) &&
				Objects.equals(value, pair.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	@Override
	public String toString() {
		return "< "+key+", "+value+" >";
	}





}
