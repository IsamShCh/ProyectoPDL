package proyectoPDL.analizadorLexico;

import java.util.ArrayList;

public class Entrada {

	public enum E_Tipo {
		ent, logico, cadena, vacio, tipo_ok, tipo_error, function
	}
	private int identificador;
	private String lexema = "";
	private E_Tipo tipo;
	private int desplazamiento = -1;
	private E_Tipo tipoRetorno;
	private int numParam = 0;
	private ArrayList<E_Tipo> tipoParamXX;
	private String etiqFuncion = "";
	
	private boolean declarada = false;
	
	public Entrada(String lexema) {
		this.lexema = lexema;
		this.tipoParamXX = new ArrayList<E_Tipo>();
	}
	
	public void setIdentificador(int identificador)
	{
		this.identificador = identificador;
	}
	public int getIdentificador()
	{
		return this.identificador;
	}

	public String getEtiqFuncion()
	{
		return etiqFuncion;
	}
	
	public ArrayList<E_Tipo> getTipoParamXX()
	{
		return tipoParamXX;
	}
	
	
	public int getNumParam()
	{
		return numParam;
	}
	
	public E_Tipo TipoRetorno()
	{
		return tipoRetorno;
	}
	
	public int getDesplazamiento()
	{
		return desplazamiento;
	}
	
	

	
	public String getLexema()
	{
		return lexema;
	}
	
	public E_Tipo getTipo()
	{
		return tipo;
	}
	
	
	/* Este metodo es especial, porque nos servira para declarar solo en una ocasion */
	public boolean setTipo(E_Tipo e_Tipo)
	{
		if(e_Tipo == E_Tipo.function)
		{
			this.tipo = e_Tipo;
			return true;
		}
		
		
		if ( this.tipo == null ) { /* LA VARIABLE NO HA SIDO DECLARA TODAVÍA */
			this.tipo = e_Tipo;
			return true;
		} else { /* LA VARIABLE YA HA SIDO DECLARADA */
			return false;	
		}
	}

	
	public void setDesplazamiento(int desp)
	{
		this.desplazamiento = desp;
	}
	
	public void setTipoRetorno(E_Tipo tipo)
	{
		this.tipoRetorno=tipo;
	}
	
	
	public void setNumParam(int numParam)
	{
		this.numParam = numParam;
	}
	
	
	public void setTipoParamXX(ArrayList<E_Tipo> tipoParamXX)
	{
		this.tipoParamXX = tipoParamXX;
	}
	
	public void setTipoParam(E_Tipo tipo)
	{
		tipoParamXX.add(tipo);
		numParam++;
	}
	
	
	
	public void setEtiqFuncion(String etiqFuncion)
	{
		this.etiqFuncion = etiqFuncion;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public boolean equals(Object otra_entrada) {

		Entrada otra_entrada1 = (Entrada) otra_entrada;
		// tipo = ID <ID, 4>  lex= variable_1
		// tipo2 = ID <ID, 5> lex= variable_2
		if (otra_entrada1 == null)
		{
			return false;
		}
		
		
		if (otra_entrada1.lexema.equals(this.lexema)) {return true;}

		return false;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public String toString()
	{
		String contenido = "";
		String elemento = lexema;
		contenido = contenido + " * LEXEMA : "+ "'" + elemento + "'\n";
		
		String _tipo_="";
		if(tipo != null) {_tipo_ = tipo.toString();}
		contenido = contenido + "\t + tipo : " + "'" + _tipo_ + "'\n";
		
		if(desplazamiento>=0) {
		String _desp_ = String.valueOf(desplazamiento);
		contenido = contenido + "\t + despl : " + "'" + _desp_ + "'" + "\n";
		}
		
		if(tipoRetorno != null)
		{
			
		contenido = contenido + "\t + TipoRetorno : " + "'" + tipoRetorno.toString() + "'" + "\n";
			
		}
		
		if(numParam >= 0 && tipo == E_Tipo.function)
		{
			contenido = contenido + "\t\t + numParam : " + "'" + numParam + "'" + "\n";
			int cont = 1;
			for(E_Tipo t: tipoParamXX)
			{
				contenido = contenido + "\t\t\t + TipoParam" + cont +   " : " + "'" + t + "'" + "\n";
				cont++;
			}
		}
		
		//if (!etiqFuncion.equals(""))
		if(tipo == E_Tipo.function)
		{
			String etiqueta ="Etiq"+ lexema + "01";
			contenido = contenido + "\t\t + EtiqFuncion : " + "'" + etiqueta + "'" + "\n";
		}
		
		contenido = contenido + " ---------------    ----------------- \n" ;
		
		return contenido;
	}

	
	
	
}
