import java.util.*;
// No necesitamos regex para este enfoque simple por ahora

// Para representar la información de salida de la tabla
class TokenInfo {
    int id;
    String lexeme;
    String tokenTypeString; // Renombrado para claridad

    public TokenInfo(int id, String lexeme, String tokenTypeString) {
        this.id = id;
        this.lexeme = lexeme;
        this.tokenTypeString = tokenTypeString;
    }

    @Override
    public String toString() {
        return String.format("| %-15d | %-20s | %-20s |", id, lexeme, tokenTypeString);
    }
}

// Enum para los tipos de token internos (para lógica interna)
enum InternalTokenType {
    PALABRA_RESERVADA_OPERACION,
    PALABRA_RESERVADA_ENTRADA,
    PALABRA_RESERVADA_SALIDA,
    PALABRA_RESERVADA_SI,
    PALABRA_RESERVADA_ENTONCES,
    IDENTIFICADOR,
    NUMERO_ENTERO,
    CADENA_LITERAL,
    OP_SUMA, OP_RESTA, OP_MULT, OP_DIV,
    OP_MENOR, OP_MAYOR, OP_IGUAL_IGUAL,
    ASIGNACION,
    PAREN_IZQ, PAREN_DER,
    LLAVE_IZQ, LLAVE_DER,
    PUNTO,
    WHITESPACE, // No se mostrará en la tabla
    ERROR
}

public class SimpleCalcLexerSimulator {

    // Mapa para palabras reservadas a su tipo interno específico y su ID de la tabla 9.6
    private static final Map<String, InternalTokenType> PALABRAS_RESERVADAS_MAP = new LinkedHashMap<>(); // Linked para mantener orden si es necesario
    private static final Map<String, Integer> PALABRAS_RESERVADAS_IDS = new HashMap<>();
    private static final String PR_TYPE_STRING = "Palabra Reservada (PR)"; // String común para PR

    // Mapa para operadores y delimitadores a su tipo interno y su ID de la tabla 9.6
    private static final Map<String, InternalTokenType> OPS_DELIMS_MAP = new HashMap<>();
    private static final Map<String, Integer> OPS_DELIMS_IDS = new HashMap<>();
    private static final String OP_TYPE_STRING = "Operador"; // String común para Operador
    private static final String DELIM_TYPE_STRING = "Delimitador"; // String común para Delimitador

    static {
        // Palabras Reservadas
        PALABRAS_RESERVADAS_MAP.put("OPERACION", InternalTokenType.PALABRA_RESERVADA_OPERACION);
        PALABRAS_RESERVADAS_MAP.put("ENTRADA", InternalTokenType.PALABRA_RESERVADA_ENTRADA);
        PALABRAS_RESERVADAS_MAP.put("SALIDA", InternalTokenType.PALABRA_RESERVADA_SALIDA);
        PALABRAS_RESERVADAS_MAP.put("SI", InternalTokenType.PALABRA_RESERVADA_SI);
        PALABRAS_RESERVADAS_MAP.put("ENTONCES", InternalTokenType.PALABRA_RESERVADA_ENTONCES);

        PALABRAS_RESERVADAS_IDS.put("OPERACION", 1);
        PALABRAS_RESERVADAS_IDS.put("ENTRADA", 2);
        PALABRAS_RESERVADAS_IDS.put("SALIDA", 3);
        PALABRAS_RESERVADAS_IDS.put("SI", 4);
        PALABRAS_RESERVADAS_IDS.put("ENTONCES", 5);

        // Operadores y Delimitadores (basado en la tabla 9.6)
        // Operadores Aritméticos
        OPS_DELIMS_MAP.put("+", InternalTokenType.OP_SUMA);       OPS_DELIMS_IDS.put("+", 20);
        OPS_DELIMS_MAP.put("-", InternalTokenType.OP_RESTA);      OPS_DELIMS_IDS.put("-", 21);
        OPS_DELIMS_MAP.put("*", InternalTokenType.OP_MULT);       OPS_DELIMS_IDS.put("*", 22);
        OPS_DELIMS_MAP.put("/", InternalTokenType.OP_DIV);        OPS_DELIMS_IDS.put("/", 23);
        // Operadores Relacionales
        OPS_DELIMS_MAP.put("<", InternalTokenType.OP_MENOR);      OPS_DELIMS_IDS.put("<", 30);
        OPS_DELIMS_MAP.put(">", InternalTokenType.OP_MAYOR);      OPS_DELIMS_IDS.put(">", 31);
        OPS_DELIMS_MAP.put("==", InternalTokenType.OP_IGUAL_IGUAL);OPS_DELIMS_IDS.put("==", 32);
        // Operador Asignación
        OPS_DELIMS_MAP.put("=", InternalTokenType.ASIGNACION);    OPS_DELIMS_IDS.put("=", 40);
        // Delimitadores
        OPS_DELIMS_MAP.put("(", InternalTokenType.PAREN_IZQ);     OPS_DELIMS_IDS.put("(", 50);
        OPS_DELIMS_MAP.put(")", InternalTokenType.PAREN_DER);     OPS_DELIMS_IDS.put(")", 51);
        OPS_DELIMS_MAP.put("{", InternalTokenType.LLAVE_IZQ);     OPS_DELIMS_IDS.put("{", 52); // Corregido de tu comentario
        OPS_DELIMS_MAP.put("}", InternalTokenType.LLAVE_DER);     OPS_DELIMS_IDS.put("}", 53);
        OPS_DELIMS_MAP.put(".", InternalTokenType.PUNTO);         OPS_DELIMS_IDS.put(".", 54);
    }

    // IDs categóricos para los que no son específicos por lexema en la tabla 9.6
    private static final int ID_IDENTIFICADOR = 10;
    private static final String IDENTIFICADOR_TYPE_STRING = "Identificador";

    private static final int ID_NUMERO_ENTERO = 11;
    private static final String NUMERO_ENTERO_TYPE_STRING = "Numero Entero";

    private static final int ID_CADENA_LITERAL = 12;
    private static final String CADENA_LITERAL_TYPE_STRING = "Cadena Literal";

    private static final int ID_ERROR = 99;
    private static final String ERROR_TYPE_STRING = "Indefinido/Error";


    public static List<TokenInfo> analyze(String input) {
        List<TokenInfo> tokens = new ArrayList<>();
        int currentIndex = 0;
        // int currentLine = 1; // Para futuro reporte de errores
        // int currentCol = 1; // Para futuro reporte de errores

        while (currentIndex < input.length()) {
            char currentChar = input.charAt(currentIndex);
            String lexeme = "";
            int tokenId = ID_ERROR; // Default
            String tokenTypeString = ERROR_TYPE_STRING; // Default

            // 1. Ignorar Espacios en Blanco
            if (Character.isWhitespace(currentChar)) {
                currentIndex++;
                continue;
            }

            // 2. Identificadores y Palabras Reservadas ([A-Z][A-Z0-9]*)
            if (currentChar >= 'A' && currentChar <= 'Z') {
                StringBuilder sb = new StringBuilder();
                sb.append(currentChar);
                currentIndex++;
                while (currentIndex < input.length()) {
                    char nextChar = input.charAt(currentIndex);
                    if ((nextChar >= 'A' && nextChar <= 'Z') || (nextChar >= '0' && nextChar <= '9')) {
                        sb.append(nextChar);
                        currentIndex++;
                    } else {
                        break;
                    }
                }
                lexeme = sb.toString();
                if (PALABRAS_RESERVADAS_MAP.containsKey(lexeme)) {
                    tokenId = PALABRAS_RESERVADAS_IDS.get(lexeme);
                    tokenTypeString = PR_TYPE_STRING;
                } else {
                    tokenId = ID_IDENTIFICADOR;
                    tokenTypeString = IDENTIFICADOR_TYPE_STRING;
                }
            }
            // 3. Literales Numéricos Enteros ([0-9]+)
            else if (Character.isDigit(currentChar)) {
                StringBuilder sb = new StringBuilder();
                sb.append(currentChar);
                currentIndex++;
                while (currentIndex < input.length() && Character.isDigit(input.charAt(currentIndex))) {
                    sb.append(input.charAt(currentIndex));
                    currentIndex++;
                }
                lexeme = sb.toString();
                tokenId = ID_NUMERO_ENTERO;
                tokenTypeString = NUMERO_ENTERO_TYPE_STRING;
            }
            // 4. Literales de Cadena ("[^"\n\r]*")
            else if (currentChar == '"') {
                StringBuilder sb = new StringBuilder();
                currentIndex++; // Consumir la comilla inicial
                boolean closed = false;
                while (currentIndex < input.length()) {
                    char nextChar = input.charAt(currentIndex);
                    if (nextChar == '"') {
                        currentIndex++;
                        closed = true;
                        break;
                    } else if (nextChar == '\n' || nextChar == '\r') {
                        // Error: salto de línea en cadena, no se cierra
                        lexeme = sb.toString() + " (Cadena con salto de línea no permitido)";
                        // La cadena hasta aquí se considera errónea, tokenId y tokenTypeString quedan como ERROR
                        // Avanzamos currentIndex para no quedarnos en el salto de línea en un bucle infinito de error
                        // currentIndex++; // Opcional, depende de cómo quieras manejar el error y continuar
                        break;
                    }
                    sb.append(nextChar);
                    currentIndex++;
                }
                if (closed) {
                    lexeme = sb.toString();
                    tokenId = ID_CADENA_LITERAL;
                    tokenTypeString = CADENA_LITERAL_TYPE_STRING;
                } else if (tokenId == ID_ERROR) { // Si ya se marcó error por salto de línea
                    // lexeme ya está asignado con el mensaje de error
                }
                else { // Si no se cerró y no fue por salto de línea (EOF)
                    lexeme = sb.toString() + " (Cadena no terminada)";
                    // tokenId y tokenTypeString quedan como ERROR
                }
            }
            // 5. Operadores y Delimitadores
            else {
                // Intentar coincidencia de 2 caracteres primero (para ==)
                if (currentIndex + 1 < input.length()) {
                    String twoCharOp = input.substring(currentIndex, currentIndex + 2);
                    if (OPS_DELIMS_MAP.containsKey(twoCharOp)) {
                        lexeme = twoCharOp;
                        tokenId = OPS_DELIMS_IDS.get(lexeme);
                        // Determinar si es Operador o Delimitador para el string
                        InternalTokenType type = OPS_DELIMS_MAP.get(lexeme);
                        if (type == InternalTokenType.OP_MENOR || type == InternalTokenType.OP_MAYOR || type == InternalTokenType.OP_IGUAL_IGUAL ||
                                type == InternalTokenType.OP_SUMA || type == InternalTokenType.OP_RESTA || type == InternalTokenType.OP_MULT || type == InternalTokenType.OP_DIV ||
                                type == InternalTokenType.ASIGNACION) {
                            tokenTypeString = OP_TYPE_STRING;
                        } else {
                            tokenTypeString = DELIM_TYPE_STRING;
                        }
                        currentIndex += 2;
                    }
                }

                // Si no hubo coincidencia de 2 caracteres, o si no hay suficientes caracteres, intentar 1
                if (lexeme.isEmpty()) { // lexeme.isEmpty() indica que no se encontró un op de 2 chars
                    String oneCharOp = input.substring(currentIndex, currentIndex + 1);
                    if (OPS_DELIMS_MAP.containsKey(oneCharOp)) {
                        lexeme = oneCharOp;
                        tokenId = OPS_DELIMS_IDS.get(lexeme);
                        InternalTokenType type = OPS_DELIMS_MAP.get(lexeme);
                        if (type == InternalTokenType.OP_MENOR || type == InternalTokenType.OP_MAYOR || type == InternalTokenType.OP_IGUAL_IGUAL ||
                                type == InternalTokenType.OP_SUMA || type == InternalTokenType.OP_RESTA || type == InternalTokenType.OP_MULT || type == InternalTokenType.OP_DIV ||
                                type == InternalTokenType.ASIGNACION) {
                            tokenTypeString = OP_TYPE_STRING;
                        } else {
                            tokenTypeString = DELIM_TYPE_STRING;
                        }
                        currentIndex += 1;
                    } else {
                        // Carácter no reconocido
                        lexeme = oneCharOp;
                        tokenId = ID_ERROR;
                        tokenTypeString = ERROR_TYPE_STRING;
                        currentIndex += 1;
                    }
                }
            }
            tokens.add(new TokenInfo(tokenId, lexeme, tokenTypeString));
        }
        return tokens;
    }

    public static void printTokenTable(List<TokenInfo> tokens) {
        System.out.println("+-----------------+----------------------+---------------------------+");
        System.out.println("| # Identificador | Lexema               | Tipo de Token             |");
        System.out.println("+-----------------+----------------------+---------------------------+");
        for (TokenInfo token : tokens) {
            System.out.println(token);
        }
        System.out.println("+-----------------+----------------------+---------------------------+");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingresa el código SimpleCalc (o 'salir' para terminar):");

        while(true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("salir")) {
                break;
            }
            if (input.trim().isEmpty()) {
                continue;
            }
            List<TokenInfo> recognizedTokens = analyze(input);
            printTokenTable(recognizedTokens);
            System.out.println("\nIngresa más código o 'salir':");
        }
        scanner.close();
        System.out.println("Simulador terminado.");
    }
}