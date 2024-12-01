
## Autores 

|Autores|
|---|
|Isam, Saha Chaham|
|Diego, Mendaña Álvarez|


# Proyecto PDL: Analizador Léxico, Sintáctico y Semántico

El lenguaje definido en este proyecto es un subconjunto muy básico de un lenguaje imperativo, similar en algunos aspectos a JavaScript o C, pero con una funcionalidad extremadamente limitada.  No es un lenguaje estándar ni tiene un nombre específico.  Es un lenguaje *ad hoc* creado para este proyecto académico, con el propósito de demostrar los principios de análisis léxico, sintáctico y semántico.
Este proyecto implementa un analizador léxico, sintáctico y semántico para un lenguaje de programación simple (PDL). El analizador procesa un archivo de entrada, genera una lista de tokens, construye una tabla de símbolos y realiza un análisis sintáctico ascendente para verificar la corrección del código fuente.

Resumen de las características del lenguaje:

* **Tipos de datos:** `int`, `boolean`, `string` y `void`.
* **Sentencias:**
    * `if (condición) sentencia`
    * `while (condición) { bloque de sentencias }`
    * `let identificador tipo;` (declaración de variables)
    * `identificador = expresión;` (asignación)
    * `put expresión;` (salida - similar a `console.log` o `printf`)
    * `get identificador;` (entrada)
    * `return expresión;` (retorno de función)
* **Expresiones:**
    * Operadores aritméticos: `+`, `-`
    * Operadores lógicos: `&&` (AND), `||` (OR), `==` (igualdad), `!=` (desigualdad)
    * Predecremento: `--identificador`
    * Literales: enteros, cadenas
    * Identificadores (variables)
    * Llamadas a funciones: `identificador(lista de argumentos)`
* **Funciones:**
    * Declaración: `function identificador(lista de parámetros) tipo { bloque de sentencias }` o `function identificador(lista de parámetros) void { bloque de sentencias }`
    * Los parámetros se declaran con tipo e identificador.
* **Limitaciones:** No hay soporte para:
    * Otros tipos de datos (flotantes, arrays, etc.)
    * Otros operadores (multiplicación, división, módulo, etc.)
    * Estructuras de control más complejas (else, for, switch, etc.)
    * Clases, objetos, etc.
    * Comentarios


Es un lenguaje muy simple diseñado para fines educativos.  No está pensado para ser utilizado en un contexto real, sino para aprender cómo funcionan los analizadores.

## Estructura del Proyecto

El proyecto está organizado en las siguientes carpetas y archivos principales:

### Paquete `Principal`

* **`Principal.java`**: Clase principal que inicia el análisis. Lee el archivo fuente, inicializa los analizadores y gestiona la salida.
* **`TablaDeSimbolos.java`**: Implementa la tabla de símbolos, que almacena información sobre las variables y funciones declaradas en el código.
* **`tableador.java`**:  *Utilidad auxiliar (probablemente para depuración o pruebas), no forma parte del núcleo del analizador.*

### Paquete `proyectoPDL.analizadorLexico`

* **`AccionesSemanticas.java`**: Define las acciones semánticas asociadas a las transiciones del AFD. Construye los tokens y los inserta en la tabla de símbolos.
* **`AFD.java`**: Implementa el Autómata Finito Determinista (AFD) que realiza el análisis léxico.
* **`Entrada.java`**: Representa una entrada en la tabla de símbolos. Almacena información como el lexema, tipo, desplazamiento, etc.
* **`MainALexico.java`**: Clase principal del analizador léxico. Lee el archivo fuente carácter por carácter y genera los tokens.
* **`Token.java`**: Representa un token, la unidad básica del análisis léxico. Almacena el tipo y valor del token.

### Paquete `proyectoPDL.analizadorSemantico`

* **`AnalizadorSemantico.java`**: Implementa el análisis semántico. Verifica la compatibilidad de tipos y otras reglas semánticas.
* **`Atributo.java`**: Representa un atributo utilizado en el análisis semántico.

### Paquete `proyectoPDL.analizadorSintactico`

* **`AnalizadorSintactico.java`**: Implementa el análisis sintáctico ascendente utilizando un autómata LR(1).
* **`Automata.java`**: Implementa el autómata LR(1) utilizado en el análisis sintáctico.
* **`Gramatica.java`**: Define la gramática del lenguaje PDL.
* **`Item.java`**: Representa un item LR(1).
* **`LR1.java`**: Implementa el algoritmo LR(1) para construir el autómata.
* **`Pair.java`**: Clase auxiliar para representar pares de valores.

### Paquete `proyectoPDL.analizadorSintacticoSemantico`

* **`MainASintSem.java`**: Coordina el análisis sintáctico y semántico.

### Paquete `proyectoPDL.gestorErrores`

* **`ErrorSemantico.java`**:  Excepción para errores semánticos.
* **`ErrorSintactico.java`**: Excepción para errores sintácticos.
* **`GestorErrores.java`**: Gestiona el número de línea para el reporte de errores.

## Ejecución

El programa se ejecuta desde la clase `Principal.java`. Busca un archivo llamado `fichero_entrada.txt` en el directorio actual, que debe contener el código fuente PDL a analizar. La salida del analizador (tokens, tabla de símbolos y parse) se guarda en archivos separados en el mismo directorio. Los errores se reportan a la consola y (potencialmente, según el código) a un archivo `errores.txt`.

`javac -d bin -sourcepath src src/Principal/Principal.java`
`java -cp bin Principal.Principal`

## Gramática

La gramática del lenguaje PDL está definida en la clase `Gramatica.java`.  Se utiliza una representación compacta con símbolos griegos para los tokens.  Es importante consultar la función `sustitucionSimbolos` en `Automata.java` y los métodos `getRepresentacionString` e `getRepresentacionStringInv` en `Token.java` para la correspondencia entre estos símbolos y los tokens reales.

## Mejoras para el futuro

* **Manejo de Errores:** Mejorar el reporte de errores, incluyendo información más detallada sobre la ubicación y tipo de error.
* **Más Características del Lenguaje:** Extender la gramática y el analizador para soportar más características del lenguaje, como diferentes tipos de datos, operadores, estructuras de control, etc.
* **Generación de Código:** Añadir una fase de generación de código para traducir el código PDL a un lenguaje intermedio o ensamblador.
