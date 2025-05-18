# SimpleCalc Lexer Simulator

![Java Version](https://img.shields.io/badge/Java-11+-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](#) <!-- Reemplazar con tu badge de build real -->
[![Coverage Status](https://img.shields.io/badge/Coverage-N/A-lightgrey.svg)](#) <!-- Reemplazar con tu badge de coverage real -->

Simulador de Analizador Léxico para el lenguaje de programación didáctico "SimpleCalc". Este proyecto toma una cadena de entrada que representa código en SimpleCalc y la descompone en una secuencia de tokens, mostrando su identificación y tipo.

## Descripción del Lenguaje SimpleCalc

SimpleCalc es un lenguaje de programación didáctico enfocado en:
*   Operaciones aritméticas básicas (suma, resta, multiplicación, división) con números enteros.
*   Declaración implícita de variables.
*   Asignación de valores.
*   Entrada de datos por el usuario y salida de resultados en pantalla.
*   Control de flujo mediante sentencias condicionales simples (SI-ENTONCES).
*   Sintaxis clara, sencilla y estructurada.

(Puedes añadir un enlace al documento completo de diseño del lenguaje si lo tienes en el repositorio).

## Características del Simulador

*   **Entrada de Código:** Permite al usuario ingresar cadenas de código SimpleCalc de forma interactiva.
*   **Análisis Léxico:** Identifica lexemas basados en las reglas definidas para SimpleCalc.
*   **Clasificación de Tokens:** Determina el tipo de cada lexema (Palabra Reservada, Identificador, Número Entero, Operador, Delimitador, Cadena Literal).
*   **Identificación Numérica:** Asigna un número identificador a cada token según la especificación del lenguaje.
*   **Salida Tabulada:** Presenta los resultados del análisis en una tabla clara y legible.
*   **Manejo Básico de Errores:** Identifica caracteres o secuencias no válidas como errores léxicos.

## Alfabeto y Tokens de SimpleCalc (Resumen)

*   **Alfabeto:** Letras mayúsculas (A-Z), Dígitos (0-9), Operadores (+, -, \*, /), Relacionales (<, >, ==), Asignación (=), Delimitadores ((), {}, ., "), Espacios en blanco.
*   **Palabras Reservadas:** `OPERACION`, `ENTRADA`, `SALIDA`, `SI`, `ENTONCES`.
*   **Otros Tokens:** Identificadores, Números Enteros, Cadenas Literales, Operadores (Aritméticos, Relacionales, Asignación), Delimitadores.

## Tabla de Identificación de Tokens (Referencia de la Salida)

El simulador produce una tabla con las siguientes columnas:

| # Identificador | Lexema               | Tipo de Token             |
|-----------------|----------------------|---------------------------|
| (Ej: 1)         | (Ej: OPERACION)      | (Ej: Palabra Reservada (PR))|
| (Ej: 10)        | (Ej: VAR1)           | (Ej: Identificador)       |
| (Ej: 11)        | (Ej: 123)            | (Ej: Numero Entero)       |
| (Ej: 12)        | (Ej: "Hola")         | (Ej: Cadena Literal)      |
| (Ej: 20)        | (Ej: +)              | (Ej: Operador)            |
| (Ej: 50)        | (Ej: ()              | (Ej: Delimitador)         |
| (Ej: 99)        | (Ej: @)              | (Ej: Indefinido/Error)    |

*(Esta es una representación simplificada. Consultar la especificación completa del lenguaje para todos los IDs)*

## Cómo Usar

1.  **Clonar el Repositorio:**
    ```bash
    git clone https://github.com/TU_USUARIO/TU_REPOSITORIO.git
    cd TU_REPOSITORIO
    ```
2.  **Compilar el Código:**
    Asegúrate de tener un JDK (Java Development Kit) versión 11 o superior instalado.
    ```bash
    javac SimpleCalcLexerSimulator.java
    ```
3.  **Ejecutar el Simulador:**
    ```bash
    java SimpleCalcLexerSimulator
    ```
4.  **Ingresar Código:**
    El programa te pedirá que ingreses código SimpleCalc. Escribe una línea de código y presiona Enter.
    ```
    Ingresa el código SimpleCalc (o 'salir' para terminar):
    > OPERACION { VAR = 10 + VALOR . }
    ```
5.  **Ver Resultados:**
    Se mostrará la tabla de tokens identificados.
6.  **Salir:**
    Escribe `salir` y presiona Enter para terminar el programa.

## Ejemplo de Entrada y Salida

**Entrada:**
VAR = 100 + OTRO .

**Salida Esperada (Formato):**

## Contribuir

Las contribuciones son bienvenidas. Si deseas contribuir, por favor:
1.  Haz un Fork del repositorio.
2.  Crea una nueva rama (`git checkout -b feature/nueva-funcionalidad`).
3.  Realiza tus cambios y haz commit (`git commit -am 'Añade nueva funcionalidad'`).
4.  Haz Push a la rama (`git push origin feature/nueva-funcionalidad`).
5.  Abre un Pull Request.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles (si añades un archivo LICENSE). Si no, puedes simplemente declarar:
Este proyecto está bajo la Licencia MIT.
