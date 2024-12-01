package proyectoPDL.analizadorLexico;

public class Token {
	
	public enum Tipo {
        BOOLEAN,
        FUNCTION,
        GET,
        IF,
        INT,
        LET,
        PUT,
        RETURN,
        STRING,
        VOID,
        WHILE,
        AUTODEC,
        ENT,
        CAD,
        ID,
        IGUAL,
        COMA,
        PYC,
        PARA,
        PARC,
        LLAVA,
        LLAVC,
        SUM,
        RESTA,
        AND,
        OR,
        NEQUAL,
        EQUAL,
      //  SUMA
    }

	
//	
//    // Enumeración para los tipos de tokens permitidos
//	
//	public static Token BOOLEAN;
//	public static Token FUNCTION;
//	public static Token GET;
//	public static Token IF;
//	public static Token INT;
//	public static Token LET;
//	public static Token PUT;
//	public static Token RETURN;
//	public static Token STRING;
//	public static Token  VOID;
//	public static Token WHILE;
//	public static Token AUTODEC;
//	public static Token  ENT;
//	public static Token CAD;
//	public static Token ID;
//	public static Token IGUAL;
//	public static Token COMA;
//	public static Token PYC;
//	public static Token PARA;
//	public static Token PARC;
//	public static Token LLAVA;
//	public static Token LLAVC;
//	public static Token SUM;
//	public static Token RESTA;
//	public static Token AND;
//	public static Token OR;
	

    // Atributos de la clase Token
    private Tipo tipo;
    private String valor_str;
    private Integer valor_int;
    // Constructor de la clase Token
    public Token(Tipo tipo) {
        if( tipo != Tipo.ENT && tipo != Tipo.CAD &&  tipo != Tipo.ID )
        {
    	this.tipo = tipo;
        }
        else
        {
        	this.tipo =tipo;
        }
        //else System.out.println("UN ERROR");
        
    }
    public Token(Tipo tipo, int valor) {
        if( tipo == Tipo.ENT || tipo == Tipo.ID )
        {
    	this.tipo = tipo;
    	this.valor_int = valor;
        }
        else System.err.println("UN ERROR JEJE");
        
    }
    
    public Token(Tipo tipo, String valor) {
        if( tipo == Tipo.CAD )
        {
    	this.tipo = tipo;
    	this.valor_str = valor;
        }
        else System.out.println("UN ERROR JEJE");
        
    }

    // Métodos getter y setter para los atributos tipo y valor
    public Tipo getTipo() {
        return tipo;
    }

    public Object getValor() {
    	 if( tipo == Tipo.ENT || tipo == Tipo.ID || tipo == Tipo.INT )
    	 {
    		 return valor_int;

    	 }
    	 else if ( tipo == Tipo.CAD )
    	 {
    		 return valor_str;
    	 }
    	 
    	 else 
    		 return null;
    }

    public String getRepresentacionString() {
    	
        switch (this.tipo) {
            case IF:
                return "α";
            case ID:
                return "β";
            case LET:
                return "γ";
                
            case WHILE:
                return "δ";
            case BOOLEAN:
                return "ε";
                
            case PUT:
                return "ζ";
                
            case RETURN:
                return "η";
            case GET:
                return "θ";
                
            case OR:
                return "ι";
            case AND:
                return "κ";
                
            case RESTA:
                return "υ";
            case AUTODEC:
                return "ψ";
                
            case FUNCTION:
                return "π";
                
            case VOID:
                return "ρ";
                
            case INT:
                return "σ";
                
            case STRING:
                return "τ";
                
            case EQUAL:
                return "ω";
                
            case NEQUAL:
                return "φ";
                
            case CAD:
                return "ξ";
            case ENT:
                return "ο";
            case IGUAL:
            	return "=";       
            case COMA:
            	return ",";
            case PYC:
            	return ";";
            case PARA:
            	return "(";
            case PARC:
            	return ")";
            case LLAVA:
            	return "{";
            case LLAVC:
            	return "}";
            case SUM:
            	return "+";
            default:
                return null;
        }
    }
    
    
   public String getRepresentacionStringInv(String simbolo) {
    	
	   switch (simbolo) {
	    case "α":
	        return "if";
	    case "β":
	        return "id";
	    case "γ":
	        return "let";
	    case "δ":
	        return "while";
	    case "ε":
	        return "boolean";
	    case "ζ":
	        return "put";
	    case "η":
	        return "return";
	    case "θ":
	        return "get";
	    case "ι":
	        return "or";
	    case "κ":
	        return "and";
	    case "υ":
	        return "-";
	    case "ψ":
	        return "--";
	    case "π":
	        return "function";
	    case "ρ":
	        return "void";
	    case "σ":
	        return "int";
	    case "τ":
	        return "string";
	    case "ω":
	        return "==";
	    case "φ":
	        return "!=";
	    case "ξ":
	        return "cadena";
	    case "ο":
	        return "entero";
	    case "=":
	        return "=";
	    case ",":
	        return ",";
	    case ";":
	        return ";";
	    case "(":
	        return "(";
	    case ")":
	        return ")";
	    case "{":
	        return "{";
	    case "}":
	        return "}";
	    case "+":
	        return "+";
	    default:
	        return null;
	} 
	   
    }

    
    
    public String toString()
    {
    	if (getValor() == null)
    	{
    		
    		return "< "+   getTipo() +" , "+ " "  + " >";
    	}
    	
    	else if( getTipo() == Tipo.CAD)
    	{
    		return "< "+   getTipo() +" , \""+ getValor()  + "\" >";
    	}
    	else return "< "+   getTipo() +" , "+ getValor()  + " >";
    }
}
