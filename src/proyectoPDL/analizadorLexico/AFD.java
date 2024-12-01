package proyectoPDL.analizadorLexico;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AFD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hola");

	
		String est = "0";
		String carac  = ";";
		AFD afd = new  AFD();
		int x = afd.tabla_estados.get(est+carac);
		String y = afd.tabla_acciones.get(est+carac);
		
		System.out.println("Nos encontramos en  el  estado "+ est + " y hemos leido  el  caracter " + carac + " y nos corresponde irnos al estado "  + x + " y realizar  la  accion " + y);
		
		
		
	}

    public Map<String, Integer> tabla_estados = new HashMap<String, Integer>();
    public Map<String, String> tabla_acciones = new HashMap<String, String>();

	private static String del = "\n\t ";
	private static String l = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String c3 = "{}(),;+";
	private static String d = "0123456789";
	private static String c1 = d+"_";
	private static String c2 = d+"_"+l;
    
	private String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ{}(),;/|&\n\t\"-+=!\0_: ";
	
    public AFD()
    {
    	initTables();
    	
    }

	public  String accion (int estado, char caracter) throws IllegalStateException, IllegalArgumentException
	{	
		
		String key = String.valueOf(estado) +  String.valueOf(caracter);
		
		if(!esCaracterValido(caracter))
		{
			throw new  IllegalArgumentException();
		}
		if (!tabla_acciones.containsKey(key)) {
            throw new IllegalStateException();
        }
		return tabla_acciones.get( key );
			
	}
	
	public  int estado (int estado, char caracter) throws IllegalStateException, IllegalArgumentException
	{
		String key = String.valueOf(estado) +  String.valueOf(caracter);
		if(!esCaracterValido(caracter))
		{
			throw new  IllegalArgumentException();
		}
		
		if (!tabla_estados.containsKey(key)) {
            throw new IllegalStateException();
        }
		
		
		return tabla_estados.get( key );
	}
	
//	public int estado(int estado, char caracter) throws CaracterValidoNoEsperadoException, CaracterNoValidoException {
//	    String key = String.valueOf(estado) + String.valueOf(caracter);
//	    if (!tabla_estados.containsKey(key)) {
//	        throw new CaracterValidoNoEsperadoException(caracter);
//	    }
//	    if (!esCaracterValido(caracter)) {
//	        throw new CaracterNoValidoException(caracter);
//	    }
//
//	    return tabla_estados.get(key);
//	}

	
	

	
	private  void putExpTablaEstados(String val, String val2, int val3, boolean bol) {
		
		// val = estado actual             val2 = caracter leido         val3 = estado siguiente          bool= si NOT.
		
		// 3 :   
		
		if(bol) {
        for (char c : val2.toCharArray()) {
            tabla_estados.put(val + c, val3);
        }
        
        
		}
		
		else {
		
			for (char c : base.toCharArray()) {
				if( !val2.contains(String.valueOf(c)) )
	            tabla_estados.put(val + c, val3);		
	        }
			
			
		}
    }
	
	
	private  void putExpTablaAcciones(String val, String val2, String val3, boolean bol) {
		if(bol) {
	        for (char c : val2.toCharArray()) 
	        {
	            tabla_acciones.put(val + c, val3);
	        }
		}
		else 
		{
			for (char c : base.toCharArray()) 
			{
				if( !val2.contains(String.valueOf(c)) )
	            tabla_acciones.put(val + c, val3);
				
	        }
			
		}
    }
	
	
	public  boolean esEstadoFinal(int estado)
	{
		if ( estado >= 11 && estado != 12 )
			return true;
		
		
		else return false;
	}
	
	public boolean esCaracterValido( char c ) 
	{
		
		return base.contains(String.valueOf(c));
		
	}
	
	
//	"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ{}(),;/|&\n\t\"+-=!\0_"
	private void initTables() {
		
		// estado 0
		putExpTablaEstados("0",del, 0, true);
		putExpTablaAcciones("0", del, null, true);
		
		tabla_acciones.put("0/", null);
		tabla_estados.put("0/", 5);
		
		putExpTablaEstados("0",l+"_", 1, true);
		putExpTablaAcciones("0", l+"_", "C+Ilex", true);
		
		tabla_acciones.put("0!", null);
		tabla_estados.put("0!", 9);
		
		tabla_acciones.put("0\"", "Ilex");
		tabla_estados.put("0\"", 2);
		
		tabla_acciones.put("0|", null);
		tabla_estados.put("0|", 7);
		
		putExpTablaEstados("0",c3, 16, true);
		putExpTablaAcciones("0", c3, "G11", true);

		tabla_acciones.put("0=", null);
		tabla_estados.put("0=", 6);
		
		putExpTablaEstados("0",d, 10, true);
		putExpTablaAcciones("0", d, "Vini", true);
		
		tabla_acciones.put("0-", null);
		tabla_estados.put("0-", 3);
		
		tabla_acciones.put("0&", null);
		tabla_estados.put("0&", 8);
		
		//		tabla_acciones.put("0&", null);
		//		tabla_estados.put("0&", 8);   QUE HACE ESTO AQUI?
		
		// ESTADO  1
//		private static String del = "\n\t ";
//		private static String l = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		private static String c3 = "{}(),;+";
//		private static String d = "0123456789";
//		private static String c1 = d+"_";
//		private static String c2 = d+"_"+l;
		
		
		putExpTablaEstados("1",l, 1, true);
		putExpTablaAcciones("1", l, "C", true);
		
		putExpTablaEstados("1",c1, 4, true);
		putExpTablaAcciones("1", c1, "C", true);
		
		putExpTablaEstados("1",d+l+"_", 23, false);
		putExpTablaAcciones("1", d+l+"_", "G9", false);
		
		
		// ESTADO  2
		
						putExpTablaEstados("2","\"", 2, false);
						putExpTablaAcciones("2", "\"", "C", false);
		
		putExpTablaEstados("2","\"", 17, true);
		putExpTablaAcciones("2", "\"", "G3", true);
		
		
		// ESTADO  3
		
		tabla_acciones.put("3-", "G5");
		tabla_estados.put("3-", 11);
		
		putExpTablaEstados("3","-", 15, false);
		putExpTablaAcciones("3", "-", "G6", false);
		
		// ESTADO  4
		
		putExpTablaEstados("4",c2, 4, true);
		putExpTablaAcciones("4", c2, "C", true);
		
		putExpTablaEstados("4",c2, 13, false);
		putExpTablaAcciones("4", c2, "G9", false);
		
		
		// ESTADO 5
		// este esta pendiente de ponerle su error
		tabla_acciones.put("5/", null);
		tabla_estados.put("5/", 12);

		// ESTADO 6
		
		tabla_acciones.put("6=", "G7");
		tabla_estados.put("6=", 20);
		
		putExpTablaEstados("6","=", 21, false);
		putExpTablaAcciones("6", "=", "G8", false);
		
		// ESTADO 7
		// pendiente de tirar  erro si  no le el caracter 
		
		tabla_acciones.put("7|", "G2");
		tabla_estados.put("7|", 18);
		
		// ESTADO 8
		//error
		
		tabla_acciones.put("8&", "G1");
		tabla_estados.put("8&", 14);
		
		// ESTADO 9
		// error
		
		tabla_acciones.put("9=", "G4");
		tabla_estados.put("9=", 19);
		
		// ESTADO  10
		
		putExpTablaEstados("10", d, 10, true);
		putExpTablaAcciones("10", d, "V", true);
		
		putExpTablaEstados("10", d, 22, false);
		putExpTablaAcciones("10", d, "G10", false);
		
		
//		putExpTablaEstados("10", d+l+c3+"-&|=", 22, false);
//		putExpTablaAcciones("10", d+l+c3+"-&|=", "G10", false);
		
//		putExpTablaEstados("10", c3, 22, true);
//		putExpTablaAcciones("10", c3, "G10+G11", true);
//		
//		putExpTablaEstados("10", "-", 3, true);
//		putExpTablaAcciones("10", "-", "G10", true);
//		
//		putExpTablaEstados("10", "&", 8, true);
//		putExpTablaAcciones("10", "&", "G10", true);
//		
//		putExpTablaEstados("10", "|", 7, true);
//		putExpTablaAcciones("10", "|", "G10", true);
//		
//		putExpTablaEstados("10", "=", 6, true);
//		putExpTablaAcciones("10", "=", "G10", true);
		
		
		
//						putExpTablaEstados("10", d, 25, false);
//						putExpTablaAcciones("10", d, "G10", false);
		

		// ESTADO  11
		
		
		// ESTADO  11
		
		
		// ESTADO  12
		
		putExpTablaEstados("12", "\n", 0, true);
		putExpTablaAcciones("12", "\n", null, true);
		
		putExpTablaEstados("12", "\n", 12, false);
		putExpTablaAcciones("12", "\n", null, false);
		
		// ESTADO  13
		
		// ESTADO  14
		// ESTADO  15
		// ESTADO  16
		// ESTADO  17
		// ESTADO  18
		// ESTADO  19
		// ESTADO 20
		
		
		
	}

	
	
}
