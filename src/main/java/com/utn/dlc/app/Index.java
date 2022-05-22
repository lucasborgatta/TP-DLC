package com.utn.dlc.app;

import com.utn.dlc.app.entity.Palabra;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.imageio.IIOException;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Index {

    private String update = "UPDATE";

    HashMap<String, Integer> hashMapPalabrasdeTodosLosDocumentos = new HashMap<>();
    HashMap<String, Integer> hashMapPosteoPorDocumento = new HashMap<String, Integer>();

    public void index(Connection connection) throws IOException, SQLException {

        long startTime = System.currentTimeMillis();

        File folder = new File("C:\\» Universidad\\DLC\\TP\\src\\main\\resources\\static\\Files");
        int contadorDocumentos = 0;
        ArrayList<String> stopWords = stopWords();

        for (File file : folder.listFiles()) {

            System.out.println("POSTEO DOCUMENTO");

            System.out.println(file.getName());

            String linea;
            String palabra;

            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((linea = br.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(linea, " \n1234567890.,;:!?-_\"()[]{}\\¡¿#$%&/=*-+*|°¬@<>;'�\u0015~`\f\u0007\u001A");

                while (st.hasMoreTokens()) {

                    palabra = st.nextToken().toLowerCase(); // Pasamos todas las palabras a minuscula

                    if (!stopWords.contains(palabra)) { // Controlamos que la palabra no sea una stop word del diccionario

                        // Guardamos en la hash map de los posteos
                        // Si la hash map ya tenia la palabra, osea que aparecio en el documento entonces aumentamos en 1 el contador de frecuencias
                        if (hashMapPosteoPorDocumento.containsKey(palabra)) {
                            int contadorAux = hashMapPosteoPorDocumento.get(palabra);
                            contadorAux++;
                            hashMapPosteoPorDocumento.put(palabra, contadorAux);
                        } else {
                            // Si la hash map no tenia la palabra, la agregamos y ponemos el contador en 1
                            hashMapPosteoPorDocumento.put(palabra, 1);
                        }
                    }
                }
            }

            // Con este metodo nos fijamos que palabras aparecieron en el documento que leimos entonces le podemos aumentar 1 en su contador
            completarHashMaps(hashMapPosteoPorDocumento);
            // Cuando terminamos de leer 1 documento guardamos primero todas sus palabras y despues todos sus posteos
            postearPalabras(connection, hashMapPalabrasdeTodosLosDocumentos);

            postearDocumentos(connection, contadorDocumentos, file.getName());

            postearPosteo(connection, contadorDocumentos, hashMapPosteoPorDocumento);

            contadorDocumentos++;

            hashMapPosteoPorDocumento.clear();
            // Mirar si el posteo de posteos no se puede hacer cada 10 documentos si no el numero de llamadas se nos va mucho
        }

            // Aca es cuando se termina de leer 1 documento, entonces tenemos que actualizar la hashmap que tiene todas las palabras en base a la hashmap del posteo





            /*

            ESTO NO ME ACUERDO BIEN LO QUE HACIA ASI QUE POR AHORA QUEDA COMENTADO

        keyHashMapPalabras = hashMapPalabrasdeTodosLosDocumentos.keySet();
        cantidadDocumentos = hashMapPalabrasdeTodosLosDocumentos.values();

        Iterator iteradorPalabras = keyHashMapPalabras.iterator();
        Iterator iteradorValues = cantidadDocumentos.iterator();

        // Actualizamos la tabla palabras con los datos que leimos de todos los documentos
        while (iteradorPalabras.hasNext()) {

            String palabrasKey = String.valueOf(iteradorPalabras.next());
            int cantidadDocumentoValue = Integer.parseInt(String.valueOf(iteradorValues.next()));
            sendPostPalabras(palabrasKey);
            System.out.println("POSTEO DE PALABRAS");

            sendPutPalabras(palabrasKey, cantidadDocumentoValue);
        }

        // Borramos la HashMap de palabras
        keyHashMapPalabras.clear();
        cantidadDocumentos.clear();

        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("Tiempo de ejecucion " + endTime);

             */

     }




    public void completarHashMaps(HashMap<String, Integer> hashMapPosteoPorDocumento) {

        Set<String> keyHashMapPosteos;
        keyHashMapPosteos = hashMapPosteoPorDocumento.keySet();

        Iterator iteratorKey = keyHashMapPosteos.iterator();

        // Actualizamos la tabla de posteos con los datos que leimos del documento
        while (iteratorKey.hasNext()) {

            String palabraKey = String.valueOf(iteratorKey.next());

            if (hashMapPalabrasdeTodosLosDocumentos.containsKey(palabraKey)) {
                int contadorAux = hashMapPalabrasdeTodosLosDocumentos.get(palabraKey);
                contadorAux++;
                hashMapPalabrasdeTodosLosDocumentos.put(palabraKey, contadorAux);
            }
            else
            {
                hashMapPalabrasdeTodosLosDocumentos.put(palabraKey, 1);
            }
        }
    }




    public void postearPalabras(Connection connection, HashMap<String, Integer> hashMapPalabrasdeTodosLosDocumentos ) throws SQLException {

        String insertPalabras = "INSERT INTO palabras (nombre, cant_documentos) values";

        Set<String> keyHashMapPalabras = hashMapPalabrasdeTodosLosDocumentos.keySet();
        Collection<Integer> cantidadDocumentos = hashMapPalabrasdeTodosLosDocumentos.values();

        System.out.println("Cantidad de palabras " + keyHashMapPalabras.size());

        Iterator iteratorPalabra = keyHashMapPalabras.iterator();
        Iterator iteratorFrecuencia = cantidadDocumentos.iterator();

        for (int j = 0; j < hashMapPalabrasdeTodosLosDocumentos.size(); j++) {

            String values = "";

            if (j == hashMapPalabrasdeTodosLosDocumentos.size() - 1) {

                values = " ('" + iteratorPalabra.next() + "'," + iteratorFrecuencia.next() + ") ON DUPLICATE KEY UPDATE cant_documentos = cant_documentos + 1";

            } else {

                values = " ('" + iteratorPalabra.next() + "'," + iteratorFrecuencia.next() + "),";

            }
            insertPalabras = insertPalabras + values;
        }


        PreparedStatement statement = connection.prepareStatement(insertPalabras);
        statement.execute();
        statement.clearParameters();
        statement.clearBatch();
    }

    public void postearDocumentos(Connection connection, Integer numeroDocumento, String nombreDocumento) throws SQLException {

        String insertDocumentos = "INSERT IGNORE INTO documentos values";

        insertDocumentos = insertDocumentos + "(" + numeroDocumento + ",'" + nombreDocumento + "')";

        PreparedStatement statement = connection.prepareStatement(insertDocumentos);
        statement.execute();
        statement.clearParameters();
        statement.clearBatch();
    }

    public void postearPosteo(Connection connection, Integer numeroDocumento, HashMap<String, Integer> hashMapPosteoPorDocumento) throws SQLException {

        String insertPosteos = "REPLACE INTO posteos values";

        Set<String> keyHashMapPosteos = hashMapPosteoPorDocumento.keySet();
        Collection<Integer> frecuencias = hashMapPosteoPorDocumento.values();

        Iterator iteratorPalabra = keyHashMapPosteos.iterator();
        Iterator iteratorFrecuencia = frecuencias.iterator();

        for (int j = 0; j < hashMapPosteoPorDocumento.size(); j++) {

            String values = "";

            if (j == hashMapPosteoPorDocumento.size() - 1) {

                values = "(" + numeroDocumento + ",'" + iteratorPalabra.next() + "'," + iteratorFrecuencia.next() + "," + 0 + ")";

            } else {

                values = "(" + numeroDocumento + ",'" + iteratorPalabra.next() + "'," + iteratorFrecuencia.next() + "," + 0 + "),";

            }
            insertPosteos = insertPosteos + values;
        }


        PreparedStatement statement = connection.prepareStatement(insertPosteos);
        statement.execute();
        statement.clearParameters();
        statement.clearBatch();
    }


    public ArrayList<String> stopWords() throws IOException {
        File file = new File("C:\\» Universidad\\DLC\\TP\\src\\main\\resources\\stop_words_english.txt");
        ArrayList<String> words = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            words.add(line);
        }
        return words;
//        System.out.println(words);
    }
}
