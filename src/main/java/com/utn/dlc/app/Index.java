package com.utn.dlc.app;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Index {

    HashMap<String, Integer> hashMapPosteoPorDocumento = new HashMap<String, Integer>();
    HashSet<String> hashSet = new HashSet<>();

    public void index(Connection connection) throws IOException, SQLException {

        long startTime = System.currentTimeMillis();

        File folder = new File("C:\\» Universidad\\DLC\\TP\\src\\main\\resources\\static\\Files");
        int contadorDocumentos = 0;
        ArrayList<String> stopWords = stopWords();

        for (File file : folder.listFiles()) {

            System.out.println(file.getName());

            String linea;
            String palabra;



            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((linea = br.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(linea, " \n1234567890.,;:!?-_\"()[]{}\\¡¿#$%&/=*-+*|°¬@<>;'�\u0015~`\f\u0007\u001A\u001B\t\u0003\u0005\u000E\u001E\u0014^« ");

                while (st.hasMoreTokens()) {

                    palabra = st.nextToken().toLowerCase(); // Pasamos todas las palabras a minuscula

                    if (!(palabra.length() == 1) && !palabra.equals(" ") && !palabra.equals("    ")) {
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
            }

            // Sube el documento a la base, tambien tiene que estar cargado antes para poder subir el posteo
            postearDocumentos(connection, contadorDocumentos, file.getName());

            // Ahora si subimos el posteo de ese documento
            postearPosteo(connection, contadorDocumentos, hashMapPosteoPorDocumento);

            contadorDocumentos++;

            // Limpiamos la hashmap para liberar memoria
            hashMapPosteoPorDocumento.clear();
            // Mirar si el posteo de posteos no se puede hacer cada 10 documentos si no el numero de llamadas se nos va mucho
            System.out.println(1);
        }
     }


    // La connection que se pasa por parametros es para poder hacer la subida a la base, tiene que estar en el tpu aplicattion si o si, no puede estar aca

    public void postearDocumentos(Connection connection, Integer numeroDocumento, String nombreDocumento) throws SQLException {

        /*
        System.out.println(1);

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE INTO documentos values (?,?)");

        preparedStatement.setInt(1, numeroDocumento);
        preparedStatement.setString(2, nombreDocumento);
        preparedStatement.execute();
        preparedStatement.clearParameters();

         */


        StringBuilder stringBuilder = new StringBuilder("INSERT IGNORE INTO documentos values");

        String insert = "(" + numeroDocumento + ",'" + nombreDocumento + "')";

        stringBuilder.append(insert);

        PreparedStatement statement = connection.prepareStatement(String.valueOf(stringBuilder));
        statement.execute();
        statement.clearParameters();


    }

    // Este funciona igual que el de arriba
    public void postearPosteo(Connection connection, Integer numeroDocumento, HashMap<String, Integer> hashMapPosteoPorDocumento) throws SQLException {

        /*
        System.out.println(2);

        PreparedStatement preparedStatement = connection.prepareStatement("REPLACE INTO posteos values (?,?,?)");

        Set<String> keyHashMapPosteos = hashMapPosteoPorDocumento.keySet();
        Collection<Integer> frecuencias = hashMapPosteoPorDocumento.values();

        Iterator iteratorPalabra = keyHashMapPosteos.iterator();
        Iterator iteratorFrecuencia = frecuencias.iterator();

        for (int i = 0; i < hashMapPosteoPorDocumento.size(); i++) {

            preparedStatement.setInt(1, numeroDocumento);
            preparedStatement.setString(2, (String) iteratorPalabra.next());
            preparedStatement.setInt(3, (Integer) iteratorFrecuencia.next());
            preparedStatement.addBatch();

            if (i % 30000 == 0) {
                System.out.println(3);
                preparedStatement.executeBatch();
                preparedStatement.clearParameters();
            }
        }

        preparedStatement.executeBatch();
        preparedStatement.clearParameters();
         */





        StringBuilder stringBuilder = new StringBuilder("REPLACE INTO posteos values");

        // "ON DUPLICATE KEY UPDATE frecuencia = frecuencia + 1" Esto lo dejo aca comentado porque nos puede llegar a servir

        Set<String> keyHashMapPosteos = hashMapPosteoPorDocumento.keySet();
        Collection<Integer> frecuencias = hashMapPosteoPorDocumento.values();

        Iterator iteratorPalabra = keyHashMapPosteos.iterator();
        Iterator iteratorFrecuencia = frecuencias.iterator();


        for (int i = 0; i < hashMapPosteoPorDocumento.size(); i++) {

            if (i == hashMapPosteoPorDocumento.size() - 1) {

                stringBuilder.append("(" + numeroDocumento + ",'" + iteratorPalabra.next() + "'," + iteratorFrecuencia.next() + ")");
            }
            else
            {
                stringBuilder.append("(" + numeroDocumento + ",'" + iteratorPalabra.next() + "'," + iteratorFrecuencia.next() + "),");
            }
        }

        PreparedStatement statement = connection.prepareStatement(String.valueOf(stringBuilder));
        statement.execute();
        statement.clearParameters();



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
