package proyectoPDL.analizadorLexico;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;

import Principal.TablaDeSimbolos;
import proyectoPDL.gestorErrores.GestorErrores;

public class MainALexico {

    private PushbackReader pr;
    private AccionesSemanticas aS;
    private AFD afd;
    private int estado_inicial = 0;

    public MainALexico(String nombreArchivo, TablaDeSimbolos tabla) throws IOException {
        // Inicialización de variables y objetos
        // TablaDeSimbolos tabla = new TablaDeSimbolos(); // Creación de una nueva tabla
        // de símbolos
        this.aS = new AccionesSemanticas(tabla); // Creación de un nuevo objeto de acciones semánticas
        this.afd = new AFD(); // Creación de un nuevo objeto de AFD
        // Lectura del archivo de entrada
        FileReader fr = new FileReader(nombreArchivo);
        this.pr = new PushbackReader(fr);
    }

    public String getTabla() {

        return aS.tabla.toString();
    }

    public Token siguienteToken() throws IOException {
        int estado_actual = estado_inicial;
        int c;

        // Generación de tokens hasta que se alcanza un estado final
        boolean seguir = true;
        while (seguir) {
            if ((c = pr.read()) == -1) {
                seguir = false;
                c = ' ';
            }
            if (afd.esEstadoFinal(estado_actual))
                estado_actual = estado_inicial; // Si alcanzamos el estado final, reiniciamos el automata al estado
                                                // inicial 0.

            char ch = (char) c; // Convertimos el entero a carácter
            if (ch == '\r') {
                // gestorErrores.sumarLinea();
                ch = ' ';
            }
            if (ch == '\n') {
                GestorErrores.sumarLineaLexico();

            }

            if (estado_actual == 2 && ch != '"') {
                String accion = "C";
                aS.accionSemantica(accion, ch);
                continue;
            } else if (estado_actual == 12 && ch != '\n') {
                continue;
            }

            String accion = "";
            int estado_siguiente = -1;
            try {
                accion = afd.accion(estado_actual, ch);
                estado_siguiente = afd.estado(estado_actual, ch);
            } catch (IllegalArgumentException e) {
                System.err.println("Se encontró un caracter '" + ch + "' no es válido en la línea "
                        + GestorErrores.getLineaLexico() + ".");
                System.exit(1);
            } catch (IllegalStateException e) {
                System.err.println("El carácter '" + ch + "' es válido pero no se esperaba en la línea "
                        + GestorErrores.getLineaLexico() + ".");
                System.exit(1);
            }

            int ret = aS.accionSemantica(accion, ch);

            if (ret != 1) {
                // if(ch != '\n')
                pr.unread(c); // Devolvemos el carácter al flujo si ret != 1
            }
            estado_actual = estado_siguiente;

            if (!aS.tokens.isEmpty()) {
                return aS.tokens.remove(0); // con este añadimos y borramos
                // return aS.tokens.getLast();
            }

        }

        return null; // Fin del archivo
    }

}
