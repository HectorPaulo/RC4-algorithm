
/**
 * Esta libreria proporciona un conjunto de codificaciones estándar de
 * caracteres, como UTF-8, que se utiliza para convertir cadenas (String) en
 * arrays de bytes (byte[]).
 */
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main3 {

    // Definimos la constante N que representa el tamaño del array S (256 bytes)
    private static final int N = 256;

    // Método que intercambia dos elementos en el array S
    // Parámetros: S es el array, a y b son los índices de los elementos a
    // intercambiar
    private static void swap(int[] S, int a, int b) {
        // Guardamos temporalmente el valor de S[a]
        int temp = S[a];
        // Asignamos a S[a] el valor de S[b]
        S[a] = S[b];
        // Asignamos a S[b] el valor temporal (el antiguo valor de S[a])
        S[b] = temp;
    }

    // Implementación del algoritmo KSA (Key Scheduling Algorithm)
    // Este algoritmo inicializa y mezcla el array S usando la clave proporcionada
    private static void KSA(byte[] key, int[] S) {
        // Longitud de la clave
        int len = key.length;
        // Variable auxiliar j que se utiliza para mezclar los valores de S
        int j = 0;

        // Inicializamos el array S con los valores de 0 a 255
        for (int i = 0; i < N; i++) {
            S[i] = i;
        }

        // Realizamos la permutación del array S utilizando la clave
        for (int i = 0; i < N; i++) {
            // Calculamos el nuevo valor de j basado en el valor actual de S[i] y la clave
            j = (j + S[i] + key[i % len]) & 0xFF; // Usamos & 0xFF para asegurarnos de que j esté en el rango [0, 255]
            // Intercambiamos los valores de S[i] y S[j]
            swap(S, i, j);
        }
    }

    // Implementación del algoritmo PRGA (Pseudo-Random Generation Algorithm)
    // Este algoritmo genera la secuencia cifrante y realiza el cifrado/descifrado
    // XOR
    private static void PRGA(int[] S, byte[] input, byte[] output) {
        // Variables auxiliares i y j
        int i = 0, j = 0;

        // Recorremos cada byte del input (texto de entrada)
        for (int n = 0; n < input.length; n++) {
            // Incrementamos i y j basándonos en los valores actuales de S
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;

            // Intercambiamos los valores de S[i] y S[j]
            swap(S, i, j);

            // Generamos el byte pseudoaleatorio utilizando S[i] y S[j]
            int rnd = S[(S[i] + S[j]) & 0xFF];

            // Aplicamos XOR entre el byte del input y el byte pseudoaleatorio
            output[n] = (byte) (rnd ^ input[n]);
        }
    }

    // Función que realiza el cifrado RC4 completo
    // Toma la clave, el texto de entrada y genera el texto de salida (cifrado o
    // descifrado)
    public static void RC4Cipher(byte[] key, byte[] input, byte[] output) {
        // Creamos el array S de 256 bytes
        int[] S = new int[N];

        // Inicializamos y mezclamos S usando la clave mediante el KSA
        KSA(key, S);

        // Ciframos o desciframos el texto de entrada usando el PRGA
        PRGA(S, input, output);
    }

    public static void main(String[] args) {
        System.out.println("Ingresa la clave: ");
        Scanner scanner = new Scanner(System.in);
        String keyString = scanner.nextLine();

        System.out.println("Ingresa el mensaje: ");
        String plaintext = scanner.nextLine();

        // Convertimos la clave (keyString) a un array de bytes usando UTF-8
        byte[] key = keyString.getBytes(StandardCharsets.UTF_8);
        System.out.print("Clave en decimal: ");
        for (byte b : key) {
            System.out.print((b & 0xFF) + " ");
        }
        System.out.println();
        // Convertimos el texto plano (plaintext) a un array de bytes usando UTF-8
        byte[] input = plaintext.getBytes(StandardCharsets.UTF_8);
        // Creamos un array de bytes para almacenar el texto cifrado (de igual tamaño
        // que el input)
        byte[] ciphertext = new byte[input.length];
        // Creamos un array de bytes para almacenar el texto descifrado (de igual tamaño
        // que el input)
        byte[] decrypted = new byte[input.length];

        // Llamamos a la función RC4Cipher para cifrar el texto usando la clave
        RC4Cipher(key, input, ciphertext);

        // Imprimimos el texto cifrado en formato hexadecimal
        System.out.print("Ciphertext: ");
        for (byte b : ciphertext) {
            System.out.print((b & 0xFF) + " "); // Mostrar cada byte en decimal
        }
        System.out.println();

        // Llamamos a la función RC4Cipher nuevamente para descifrar el texto cifrado
        RC4Cipher(key, ciphertext, decrypted);

        // Imprimimos el texto descifrado, convirtiendo el array de bytes a una cadena
        // UTF-8
        System.out.print("Decrypted: ");
        System.out.println(new String(decrypted, StandardCharsets.UTF_8));

        scanner.close();
    }
}