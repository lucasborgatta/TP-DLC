package com.utn.dlc.app;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.sql.*;
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

            System.out.println(file.getName());

            String linea;
            String palabra;

            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((linea = br.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(linea, " \n1234567890.,;:!?-_\"()[]{}\\¡¿#$%&/=*-+*|°¬@<>;'�\u0015~`\f\u0007\u001A '\t''\u0003''\u0005''\u000E''\u001E''\u0014'");

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
            // Este metodo se usa para completar la hash maps de palabras por documento, para contar la cantidad de documentos en los que aparece una palabra
            completarHashMaps(hashMapPosteoPorDocumento);

            // Sube todas las palabras del documento leido, necesitamos tenerlas subidas para poder subir el posteo
            postearPalabras(connection, hashMapPalabrasdeTodosLosDocumentos);

            // Sube el documento a la base, tambien tiene que estar cargado antes para poder subir el posteo
            postearDocumentos(connection, contadorDocumentos, file.getName());

            // Ahora si subimos el posteo de ese documento
            postearPosteo(connection, contadorDocumentos, hashMapPosteoPorDocumento);

            contadorDocumentos++;

            // Limpiamos la hashmap para liberar memoria
            hashMapPosteoPorDocumento.clear();
            // Mirar si el posteo de posteos no se puede hacer cada 10 documentos si no el numero de llamadas se nos va mucho
        }
    }




    public void completarHashMaps(HashMap<String, Integer> hashMapPosteoPorDocumento) {

        Set<String> keyHashMapPosteos;
        keyHashMapPosteos = hashMapPosteoPorDocumento.keySet();

        Iterator iteratorKey = keyHashMapPosteos.iterator();

        // Actualizamos la tabla de palabras con los datos que leimos de 1 solo documento
        while (iteratorKey.hasNext()) {

            String palabraKey = String.valueOf(iteratorKey.next());

            // Esto se fija si el documento ya estaba en la hash map, si estaba le aumenta el contador de cantidad de documentos en los que aparece
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


    // La connection que se pasa por parametros es para poder hacer la subida a la base, tiene que estar en el tpu aplicattion si o si, no puede estar aca

    public void postearPalabras(Connection connection, HashMap<String, Integer> hashMapPalabrasdeTodosLosDocumentos ) throws SQLException {

        String insertPalabras = "INSERT INTO palabras (nombre, cant_documentos) values";

        // Esto es para recorrer los datos de la hashmap
        Set<String> keyHashMapPalabras = hashMapPalabrasdeTodosLosDocumentos.keySet();
        Collection<Integer> cantidadDocumentos = hashMapPalabrasdeTodosLosDocumentos.values();

        System.out.println("Cantidad de palabras " + keyHashMapPalabras.size());

        Iterator iteratorPalabra = keyHashMapPalabras.iterator();
        Iterator iteratorCantidadDocumentos = cantidadDocumentos.iterator();

        for (int j = 0; j < hashMapPalabrasdeTodosLosDocumentos.size(); j++) {

            String values = "";

            // Si esta en la ultima linea hace la query pero sin ponerle la coma al final
            if (j == hashMapPalabrasdeTodosLosDocumentos.size() - 1) {

                // El iterator Palabra trae la palabra y el iterator cantidad de documentos trae la cantidad de documentos de esa palabra para meter adentro de la query
                values = " ('" + iteratorPalabra.next() + "'," + iteratorCantidadDocumentos.next() + ") ON DUPLICATE KEY UPDATE cant_documentos = cant_documentos + 1"; // El duplicate key update es para que no salte el error por clave duplicada y actualice los contadores

            } else {

                values = " ('" + iteratorPalabra.next() + "'," + iteratorCantidadDocumentos.next() + "),";

            }
            // Se arma la query enorme
            insertPalabras = insertPalabras + values;
        }

        // Se hace la llamada a la base de datos
        PreparedStatement statement = connection.prepareStatement(insertPalabras);
        statement.execute();
        statement.clearParameters();
        statement.clearBatch();
    }

    public void postearDocumentos(Connection connection, Integer numeroDocumento, String nombreDocumento) throws SQLException {

        // El ignore es para que si salta algun error ignore ese insert y siga con los demas
        String insertDocumentos = "INSERT IGNORE INTO documentos values";

        // Arma la query para el documento
        insertDocumentos = insertDocumentos + "(" + numeroDocumento + ",'" + nombreDocumento + "')";

        // Lo sube a la base de datos
        PreparedStatement statement = connection.prepareStatement(insertDocumentos);
        statement.execute();
        statement.clearParameters();
        statement.clearBatch();
    }

    public int getCantidadDocumentos(Connection connection, String palabra) throws SQLException{
        String consulta = "select count(p.id_documento) from posteos p where p.nombre_palabra like";
        consulta = consulta + "\"" + palabra +"\"";
        Statement statement = connection.createStatement();
        ResultSet rs;
        rs = statement.executeQuery(consulta);
        while (rs.next()){
            int count = rs.getInt(1);
            return count;
        }
        return 0;
    }

    public ArrayList getDatosPeso(Connection connection, String termino) throws SQLException{
        ArrayList array = new ArrayList();
        List<List<Integer>> secondArray = new ArrayList<>();

        String consulta = "select p.frecuencia, p.id_documento,count(p.id_documento)  from posteos p where p.nombre_palabra like";
        consulta = consulta + "\"" + termino +"\"" + "group by p.frecuencia, p.id_documento";
        Statement statement = connection.createStatement();
        ResultSet rs;
        rs = statement.executeQuery(consulta);
        int cant_documentos = 0;
        while (rs.next()){
            List<Integer> thirdArray= new ArrayList<>();
            int frecuencia = rs.getInt(1);
            int idDoc = rs.getInt(2);
            thirdArray.add(frecuencia);
            thirdArray.add(idDoc);
            cant_documentos += rs.getInt(3);
            secondArray.add(thirdArray);
        }
        array.add(secondArray);
        array.add(cant_documentos);
        return array;
    }

    // Este funciona igual que el de arriba
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
    }
}
