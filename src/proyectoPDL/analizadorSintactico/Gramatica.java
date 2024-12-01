package proyectoPDL.analizadorSintactico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Gramatica { 
	Map<String, List<String>> producciones;
	Map<String, Map<String, Integer>> ordenProducciones;
	private int contador_de_reglas=0;

	public Gramatica() {
		//this.producciones = producciones;
		this.producciones = new HashMap<String, List<String>>();
		this.ordenProducciones = new HashMap<String, Map<String, Integer>>();
		//this.ordenProducciones = ordenProducciones;
	}
	
	

	
    public  Map<String, List<String>> convertirALL1() {
        Map<String, List<String>> nuevasProducciones = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : producciones.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            if (key.equals("S")) {
                List<String> newValues = new ArrayList<>();
                for (String value : values) {
                    newValues.add(value + key + "'");
                }
                nuevasProducciones.put(key, newValues);
            } else {
                List<String> alpha = new ArrayList<>();
                List<String> beta = new ArrayList<>();

                for (String value : values) {
                    if (value.startsWith(key)) {
                        alpha.add(value.substring(1));
                    } else {
                        beta.add(value);
                    }
                }

                if (!alpha.isEmpty()) {
                    List<String> newValues = new ArrayList<>();
                    for (String b : beta) {
                        newValues.add(b + key + "'");
                    }
                    nuevasProducciones.put(key, newValues);

                    newValues = new ArrayList<>();
                    for (String a : alpha) {
                        newValues.add(a + key + "'");
                    }
                    newValues.add("λ");
                    nuevasProducciones.put(key + "'", newValues);
                } else {
                    nuevasProducciones.put(key, values);
                }
            }
        }

        return  nuevasProducciones;
    }

	
	public  void addProduccion(String antecedente, List<String> consecuentes) {
		producciones.put(antecedente, consecuentes);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(String produccion: consecuentes)
		{
			map.put(produccion, contador_de_reglas);
			contador_de_reglas++;
		}
		ordenProducciones.put(antecedente, map);
	}
	
	public int getNumeroDeReglas() {
		return contador_de_reglas;
		
	}
	
	

	public Map<String, List<String>> getProducciones() {
		return producciones;
	}
	
	
	public Pair<String, String> getProduccionesNumero(int numero) {
		
		for (Map.Entry<String, Map<String, Integer>> entry : ordenProducciones.entrySet())
		{
			// imaginate hacer esta puta locura para solo obtener el orden en el que esta en lista jajajajajaj XDXDXDXD
						if (entry.getValue().containsValue(numero))
						{
							//return new Pair<String,String>(  entry.getKey()  , entry.getValue().keySet()  );
							for(String x: entry.getValue().keySet()) 
							{
								if (entry.getValue().get(x).equals(numero))
								{
									return new Pair<String,String>(  entry.getKey()  , x  );
									
								}
								
							}
						}
		}
		
		return null;
	}
	
	

	public void setProducciones(Map<String, List<String>> producciones) {
		this.producciones = producciones;
	}


	public Set<String> first(String C) {

		Set<String> lista_para_listos = new HashSet<String>();



		return first_rec(C, lista_para_listos);
	}


	public Set<String> first_rec(String C, Set<String> lista_para_listos) {


		//public Map<String, List<String>> getProducciones() 
//		System.out.println(C);
		List<String> produccionesC = getProducciones().get(C);
//		System.out.println(produccionesC);

		if(produccionesC == null) 
		{
			lista_para_listos.add(C);
			return lista_para_listos;
		}

		for(String prod: produccionesC)
		{
			boolean parar;
			int i = 0;
			do {
				parar = true;
				String symbolo = String.valueOf(prod.charAt(i));

				if(symbolo.equals("λ")) 
				{
					lista_para_listos.add(symbolo);
				}
				else if(!getProducciones().containsKey(symbolo)) 
				{
					lista_para_listos.add(symbolo);
				}
				else 
				{
					if(!symbolo.equals(C))
					{
						first_rec(symbolo,lista_para_listos);
//						System.out.println(lista_para_listos);
						if(lista_para_listos.contains("λ") && i+1 < prod.length())
						{
							lista_para_listos.remove("λ");
							i++; 
							parar=false;
						}
					}
					else if(symbolo.equals(C) && produccionesC.contains("λ") )
					{	
						i++;
						parar=false;

					}
				}
			} while (!parar);
		}
		return lista_para_listos;
	}
	
	
	
	
	
	// FIRST ALTERNATIVO
	
													//	public Set<String> first(String C){
													//	    Set<String> lista_para_listos = new HashSet<String>();
													//	    return first_rec(C, lista_para_listos, new HashSet<String>());
													//	}
													//
													//	public Set<String> first_rec(String C, Set<String> lista_para_listos, Set<String> visited){
													//	    if (visited.contains(C)) {
													//	        return lista_para_listos;
													//	    }
													//	    visited.add(C);
													//
													//	    List<String> produccionesC = getProducciones().get(C);
													//
													//	    if(produccionesC == null) {
													//	        lista_para_listos.add(C);
													//	        return lista_para_listos;
													//	    }
													//
													//	    for(String prod: produccionesC){
													//	        int i = 0;
													//	        boolean parar;
													//	        do {
													//	            parar = true;
													//	            String symbolo = String.valueOf(prod.charAt(i));
													//
													//	            if(symbolo.equals("λ")) {
													//	                lista_para_listos.add(symbolo);
													//	            } else if(!getProducciones().containsKey(symbolo)) {
													//	                lista_para_listos.add(symbolo);
													//	            } else {
													//	                if(!symbolo.equals(C)){
													//	                    Set<String> first_symbolo = first_rec(symbolo, new HashSet<String>(), visited);
													//	                    lista_para_listos.addAll(first_symbolo);
													//	                    if(first_symbolo.contains("λ") && i+1 < prod.length()){
													//	                        lista_para_listos.remove("λ");
													//	                        i++; 
													//	                        parar=false;
													//	                    }
													//	                } else if(symbolo.equals(C) && produccionesC.contains("λ") ) {    
													//	                    i++;
													//	                    parar=false;
													//	                }
													//	            }
													//	        } while (!parar);
													//	    }
													//	    return lista_para_listos;
													//	
	
	
// FOLLOW VIEJO


//																									public Set<String> follow(String C){
//																								
//																										Set<String> lista_follow = new HashSet<String>();
//																								
//																								
//																										return follow_rec(C, lista_follow);
//																									}
//																								
//																									public Set<String> follow_rec(String C, Set<String> lista_follow){
//																								
//																										Set<Map.Entry<String, List<String>>> entC = new HashSet<Map.Entry<String, List<String>>>();
//																										//Set<Map.Entry<String, List<String>>> entC = new HashSet<Map.Entry<String, List<String>>>();
//																										//producciones             S -> T | X | Y        X -> zT ....
//																								
//																										
//																										for (Map.Entry<String, List<String>> entry : producciones.entrySet())
//																										{
//																											for(String produccion: entry.getValue())
//																											{
//																												if(produccion.contains(C))
//																													entC.add(entry);
//																											}
//																								
//																										}
//																								
//																										System.out.println(entC);
//																										for (  Map.Entry<String, List<String>> entrada : entC)
//																										{
//																											if(entrada.getKey().equals("Y"))
//																											{
//																												lista_follow.add("$");
//																											}
//																								
//																								
//																											for(String produccion : entrada.getValue() ) 
//																											{
//																												int i = 0;
//																								
//																												while( i != produccion.length()-1 && !String.valueOf(produccion.charAt(i)).equals(C))
//																												{
//																													i++;
//																												}
//																								
//																												//System.out.println(produccion.charAt(i));
//																												if( String.valueOf(produccion.charAt(i)).equals(C))
//																												{
//																													if(i == produccion.length()-1) //ultima posicion
//																													{
//																														//lista_follow.add("$");	
//																														String antecedente = entrada.getKey();
//																														if(!antecedente.equals(C)) 
//																														{
//																															Set<String> lista_follow_antecedente = follow(antecedente);
//																															lista_follow.addAll(lista_follow_antecedente);
//																														}
//																													}
//																													else 
//																													{
//																														Set<String> first_sig_C = first(String.valueOf(produccion.charAt(i+1)));
//																								
//																														System.out.println(first_sig_C);
//																														if(first_sig_C.contains("λ"))
//																														{
//																															first_sig_C.remove("λ");
//																															lista_follow.addAll(first_sig_C);
//																															//Set<String> follow_B = follow(String.valueOf(produccion.charAt(i+1)));
//																															String antecedente = entrada.getKey();
//																															if(antecedente.equals(C)) {
//																																Set<String> follow_B = follow(antecedente);
//																																lista_follow.addAll(follow_B);		
//																															}
//																														}	
//																														else {
//																								
//																															lista_follow.addAll(first_sig_C);
//																														}
//																													}
//																												}
//																											}
//																								
//																										}
//																										return lista_follow;
//																									}

	
	
	public Set<String> follow(String C){
	    Set<String> lista_follow = new HashSet<String>();
	    return follow_rec(C, lista_follow, new HashSet<String>());
	}

	public Set<String> follow_rec(String C, Set<String> lista_follow, Set<String> visited){
	    if (visited.contains(C)) {
	        return lista_follow;
	    }
	    visited.add(C);

	    for (Map.Entry<String, List<String>> entry : producciones.entrySet()){
	        for(String produccion: entry.getValue()){
	            if(produccion.contains(C)){
	                if(entry.getKey().equals("Y")){
	                    lista_follow.add("$");
	                }

	                for(String prod : entry.getValue() ) {
	                    int i = prod.indexOf(C);

	                    if(i != -1){
	                        if(i == prod.length()-1) { // última posición
	                            String antecedente = entry.getKey();
	                            if(!antecedente.equals(C)) {
	                                Set<String> lista_follow_antecedente = follow_rec(antecedente, new HashSet<String>(), visited);
	                                lista_follow.addAll(lista_follow_antecedente);
	                            }
	                        } else {
	                            Set<String> first_sig_C = first(String.valueOf(prod.charAt(i+1)));

	                            if(first_sig_C.contains("λ")){
	                                first_sig_C.remove("λ");
	                                lista_follow.addAll(first_sig_C);
	                                String antecedente = entry.getKey();
	                                if(!antecedente.equals(C)) {
	                                    Set<String> follow_B = follow_rec(antecedente, new HashSet<String>(), visited);
	                                    lista_follow.addAll(follow_B);        
	                                }
	                            } else {
	                                lista_follow.addAll(first_sig_C);
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
	    return lista_follow;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	


	public Set<String> getNoTerminales(){
		return producciones.keySet();
	}
	
	public Set<String> getTerminales(){
	    // terminales = todos - noTerminales
	    Set<String> todos = new HashSet<String>(getCharacters());
	    Set<String> noTerminales = getNoTerminales();
	    // Resta noTerminales de todos
	    todos.removeAll(noTerminales);
	    return todos;
	}

	
	public ArrayList<String> getCharacters(){
	ArrayList<String> characters = new ArrayList<>();

	for (List<String> productionList : getProducciones().values()) {
		for (String production : productionList) {
			for (char c : production.toCharArray()) {
				if(c!='λ') characters.add(String.valueOf(c));
			}
		}
	}
	return characters;
	}
	
	


}
