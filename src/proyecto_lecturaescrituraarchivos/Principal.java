package proyecto_lecturaescrituraarchivos;

import java.io.*;
import java.nio.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;
import java.util.*;

/**
 * Ejemplos de lectura y escritura con E/S. Basado en los ejemplos de la
 * documentación oficial de Oracle:
 * https://docs.oracle.com/javase/tutorial/essential/io/index.html
 *
 * @author Angonoa, Franco
 */
public class Principal {

    /**
     * Al ejecutar el programa, se creará una carpeta dentro del proyecto
     * llamada archivosEjemplos que contendrá unos archivos .txt de acuerdo al
     * método utilizado de lectura y escritura.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            //Se crea la carpeta.
            Path directorio = Paths.get(".\\archivosEjemplos");
            if(!Files.isDirectory(directorio)) Files.createDirectory(directorio);

            //Se llama los métodos. Ir a cada uno para leer su documentación.
            escribirBytes();
            leerBytes();
            escribirBuffer();
            leerBuffer();
            escribirFlujo();
            leerFlujo();
            escribirCanales();
            leerCanales();
            
        } catch (FileAlreadyExistsException faee) {
            System.err.printf("El directorio ya existe: %s%n", faee);
        } catch (IOException io) {
            System.err.printf("Error de tipo IO en los archivos %s%n", io);
        }

    } //Fin del metodo main()

    
    /**
     * Método que recibe todas las llamadas Path para crear el archivo.
     *
     * @param tipo si es con flujo bytes, con buffered, etc.
     * @return la ruta del archivo que se va a crear.
     */
    private static Path generarPath(String tipo) {
        Path file = Paths.get(".\\archivosEjemplos\\escritura" + tipo + ".txt");
        return file;
    }

    /**
     * TODOS LOS BYTES O LÍNEAS DE UN ARCHIVO. Los métodos readAllBytes() o
     * readAllLines() *leen* el contenido completo de un archivo en una sola
     * transferencia.
     *
     * Utilice métodos write() para *escribir* bytes o líneas en un archivo.
     *
     * Referencia teórica: Página 29 Proydesa.
     *
     * @throws java.io.IOException el metodo main lo va a manejar.
     */
    public static void leerBytes() throws IOException {
        Path file = generarPath("Bytes");
        byte[] fileArray;

        System.out.println("\nComienza la lectura en bytes...");
        fileArray = Files.readAllBytes(file);

        for (byte ele : fileArray) {
            System.out.print(ele + ", ");
        }

        System.out.println("\nFIN de lectura en Bytes.");

    } //Fin del metodo

    public static void escribirBytes() throws IOException {
        Path file = generarPath("Bytes");

        //Escribimos un mensaje con bytes. En escribirFlujo() se ve como pasar del String a byte.
        byte[] buf = {72, 111, 108, 97, 33, 32, 69, 115, 116, 111, 32, 101, 115, 32, 117, 110, 32, 109, 101, 110, 115, 97, 106, 101, 32, 112, 97, 114, 97, 32, 101, 108, 32, 99, 117, 114, 115, 111, 32, 100, 101, 32, 74, 97, 118, 97, 32, 73, 110, 116, 101, 114, 109, 101, 100, 105, 111, 33, 32, 58, 68};
        Files.write(file, buf);

        System.out.println("\t==ARCHIVO ESCRITO EN BYTES==");

    } //Fin del metodo

    /**
     * MÉTODOS DE E/S EN BUFFER PARA ARCHIVOS DE TEXTO.
     *
     * El método newBufferedReader() abre un archivo para su *lectura*.
     *
     * El método newBufferedWriter() *escribe* en un archivo a través de un
     * objeto BufferedWriter.
     *
     * Referencia teórica: Página 32 Proydesa.
     *
     * @throws java.io.IOException que será manejado en el main
     */
    public static void leerBuffer() throws IOException {
        Path file = generarPath("Buffer");
        Charset charset = Charset.forName("UTF-8");

        //Utilizamos el try with resource ya que BufferedReader es una clase que implanta AutoCloseable
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line;
            System.out.println("\nComienza la lectura con BufferedReader...");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("\nFIN de la leectura con BuferedReader.");
        }
    } //Fin del metodo

    public static void escribirBuffer() throws IOException {
        Path file = generarPath("Buffer");
        Charset charset = Charset.forName("UTF-8");
        String s = "Esta es otra cadena para el uso de ejemplos en Java Intermedio";

        //Utilizamos el try with resource ya que BufferedWriter es una clase que implanta AutoCloseable
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(s, 0, s.length());
            System.out.println("\n\n\t==ARCHIVO ESCRITO EN BUFFERED==");
        }
    } //Fin del metodo

    /**
     * MÉTODOS DE FLUJOS DE BYTES.
     *
     * NIO.2 soporta también métodos para abrir flujos de bytes. Para crear,
     * agregar a un archivo o escribir en él, utilice el método
     * newOutputStream().
     *
     * Referencia teórica: Página 33 Proydesa.
     *
     * @throws java.io.IOException que será manejada en main.
     */
    public static void leerFlujo() throws IOException {
        Path file = generarPath("FlujoBytes");

        //Utilizamos el try-with-resource ya que tanto la clase InputStream y BufferedReader implantan AutoCloseable
        try (InputStream in = Files.newInputStream(file);
                BufferedReader reader
                = new BufferedReader(new InputStreamReader(in))) {
            System.out.println("\nComienza la lectura en Flujo de bytes...");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("\nFIN de la lectura en Flujo de bytes.");
        }
    } //Fin del metodo

    public static void escribirFlujo() throws IOException {
        //Convertimos un String en un arreglo de bytes
        String s = "En este ejemplo primero escribimos en una "
                + "cadena y luego lo convertimos a bytes.";
        byte data[] = s.getBytes();
        Path p = generarPath("FlujoBytes");

        //Utilizamos el try-with-resource ya que la clase OutputStream implanta AutoCloseable
        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
            System.out.println("\n\n\t==ARCHIVO ESCRITO EN FLUJO==");
        }
    } //Fin del metodo

    /**
     * MÉTODOS DE CANALES Y BYTEBUFFERS. La E/S de flujo lee un carácter cada
     * vez, mientras que la E/S de canal lee un buffer cada vez.
     *
     * La interfaz ByteChannel proporciona una funcionalidad de lectura y
     * escritura básica.
     *
     * Un objeto SeekableByteChannel es un objeto ByteChannel que tiene la
     * capacidad de mantener una posición en el canal y cambiarla.
     *
     * El método para escribir y leer E/S de canal es newByteChannel()
     *
     * Referencia teórica: Página 30 Proydesa.
     *
     * @throws java.io.IOException que será manejado en el main.
     */
    public static void leerCanales() throws IOException {
        Path file = generarPath("Canal");

        //Utilizamos el try-with-resource ya que la clase SeekableByteChannel implanta AutoCloseable
        try (SeekableByteChannel sbc = Files.newByteChannel(file)) {
            //En cada vuelta lee la cantidad de caracteres que le pases. 
            //En este caso en la primer vuelta va a leer "Esto es un",
            //en la segunda vuelta "a cadena q" y así...
            ByteBuffer buf = ByteBuffer.allocate(10);

            // Se debe leer los bytes con la codificación adecuada para 
            // esta plataforma. Si se saltea este paso, es posible que
            // se vea algo parecido a caracteres chinos cuando uno 
            // espera caracteres de estilo latino.
            System.out.println("\nComienza la lectura en Canales...");
            String encoding = System.getProperty("file.encoding");
            while (sbc.read(buf) > 0) {
                buf.rewind();
                System.out.print(Charset.forName(encoding).decode(buf));
                buf.flip();
            }
            System.out.println("\nFIN de la lectura en Canales.");
        }
    } //Fin del metodo

    public static void escribirCanales() throws IOException {

        // Crea un Set de opciones para añadir al archivo.
        Set<OpenOption> options = new HashSet<>();
        options.add(APPEND);
        options.add(CREATE);

        // Convertir de String a ByteBuffer.
        String s = "Esto es una cadena que representa al metodo Canales y ByteBuffers";
        byte data[] = s.getBytes();
        ByteBuffer bb = ByteBuffer.wrap(data);

        Path file = generarPath("Canal");

        //Utilizamos el try-with-resource ya que la clase SeekableByteChannel implanta AutoCloseable
        try (SeekableByteChannel sbc
                = Files.newByteChannel(file, options)) {
            sbc.write(bb);
            System.out.println("\n\n\t==ARCHIVO ESCRITO EN CANAL==");
        }
    } //Fin del metodo

} //Fin de la clase
