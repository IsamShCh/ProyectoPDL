package proyectoPDL.analizadorSintacticoSemantico;

import java.io.IOException;
import java.util.*;

import Principal.TablaDeSimbolos;
import proyectoPDL.analizadorLexico.Entrada;
import proyectoPDL.analizadorLexico.Token;
import proyectoPDL.analizadorLexico.Token.Tipo;
import proyectoPDL.analizadorSemantico.AnalizadorSemantico;
import proyectoPDL.analizadorSintactico.AnalizadorSintactico;
import proyectoPDL.analizadorSintactico.Automata;
import proyectoPDL.analizadorSintactico.Gramatica;
import proyectoPDL.analizadorSintactico.LR1;
import proyectoPDL.gestorErrores.ErrorSemantico;
import proyectoPDL.gestorErrores.ErrorSintactico;
import proyectoPDL.gestorErrores.GestorErrores;

public class MainASintSem{

	private Gramatica grammar;
	private LR1 lr1;
	private Automata aut;
	AnalizadorSintactico analizadorSintactico;

	
	
	public MainASintSem(AnalizadorSemantico ASem, TablaDeSimbolos tabla)
	{
		this.grammar = new Gramatica();
		initGrammar();
		this.lr1 = new LR1(grammar);
		this.setAut(new Automata(lr1));    
		getAut().construct();
		getAut().constructTabla();
		this.analizadorSintactico = new AnalizadorSintactico(getAut(), ASem, tabla);
		analizadorSintactico.getPila().push("0");
	}
	
	public boolean validarTokenASint(Token tok)
	{
		String token;
		if(tok != null)
		{
		token = tok.getRepresentacionString();
		}
		else
		{
			token = "$";
		}
		
		boolean verifica = true;
			
			try {
			analizadorSintactico.MetodoTabular(token, tok);
			}
			catch (Exception ex)
			{
				String nombre_token = obtenerLexemaToken(tok, token);
				if(ex.getClass() == ErrorSintactico.class)
				{
					System.err.println("Error Sintactico: " + "'" +nombre_token + "' no es valido en la sentencia. " + ex.getMessage() + "\nHallado en la linea: " + GestorErrores.getLineaLexico());
		            System.exit(1);
				}
				else if(ex.getClass() == ErrorSemantico.class) {
					System.err.println("Error Semantico: "+ex.getMessage()+"\nHallado en la linea: " + GestorErrores.getLineaSemSint());
					System.exit(1);
				}
			}

		return verifica;
	}
	
	
	private String obtenerLexemaToken(Token tok, String token)
	{
		String var ="";
		if(tok == null)
		{
			return "EOF";
		}
		else if(tok.getTipo() == Tipo.ID)
		{
			int idPos = (int)tok.getValor();
			Entrada ent = GestorErrores.getTablaSimbolos().obtenerEntradaXidPos(idPos);
			var = ent.getLexema();
			
		}
		else if(tok.getTipo() == Tipo.ENT || tok.getTipo() == Tipo.INT )
		{
			var = var + (int) tok.getValor();
		}
		else
		{
			var = tok.getRepresentacionStringInv(token);
		}
		return var;
	}
	private void initGrammar()
	{
		grammar.addProduccion("Y",Arrays.asList("P"));
		grammar.addProduccion("P",Arrays.asList("BP","FP","λ"));
		/* ## CAMBIOS ## */
		grammar.addProduccion("B",Arrays.asList("α(E)S","γβT;","S","δ(E){C}"));
		grammar.addProduccion("T",Arrays.asList("σ","ε","τ"));
		grammar.addProduccion("S",Arrays.asList("β=E;","β(L);","ζE;","θβ;","ηX;"));
		grammar.addProduccion("L",Arrays.asList("EQ","λ"));
		grammar.addProduccion("Q",Arrays.asList(",EQ","λ"));
		grammar.addProduccion("X",Arrays.asList("E","λ"));
		grammar.addProduccion("E",Arrays.asList("EιR","R"));
		grammar.addProduccion("R",Arrays.asList("RκU","U"));
		grammar.addProduccion("U",Arrays.asList("UωV","UφV","V"));
		grammar.addProduccion("V",Arrays.asList("V+W","VυW","W"));
		grammar.addProduccion("W",Arrays.asList("β","(E)","β(L)","ο","ξ","ψβ"));
		grammar.addProduccion("F",Arrays.asList("μ{C}"));
		grammar.addProduccion("μ",Arrays.asList("ν(A)"));
		grammar.addProduccion("ν",Arrays.asList("πβH"));
		grammar.addProduccion("H",Arrays.asList("T","ρ"));
		grammar.addProduccion("A",Arrays.asList("TβK","ρ"));
		grammar.addProduccion("K",Arrays.asList(",TβK","λ"));
		grammar.addProduccion("C",Arrays.asList("BC","λ"));
		
	}
	
	
	public String getParse()
	{
		return analizadorSintactico.formatoFicheroParse();
	}

	public Automata getAut() {
		return aut;
	}

	public void setAut(Automata aut) {
		this.aut = aut;
	}



}

