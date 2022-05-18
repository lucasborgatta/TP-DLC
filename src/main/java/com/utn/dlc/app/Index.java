package com.utn.dlc.app;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.imageio.IIOException;
import java.io.*;
import java.util.*;

public class Index {

    public void index() throws IOException {
        File folder = new File("C:\\» Universidad\\DLC\\TP\\src\\main\\resources\\Prueba");
        int contadorDocumentos = 0;
        ArrayList<String> stopWords = stopWords();

        HashMap<String, Integer> aparicionDocumentos = new HashMap<>();


        for (File file : folder.listFiles()) {

            HashMap<String, Integer> palabraPorDoc = new HashMap<String, Integer>();

            this.sendPostDocumento(contadorDocumentos, file.getName());

            String linea;
            String palabra;

            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((linea = br.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(linea, " \n1234567890.,;:!?-_\"()[]{}¡¿#$%&/=*-+*|°¬@");

                while (st.hasMoreTokens()) {

                    palabra = st.nextToken();

                    if (!stopWords.contains(palabra)){ // Controlamos que la palabra no sea una stop word del diccionario

                        // Guardamos en la hash map
                        // Si la hash map ya tenia la palabra, osea que aparecio en el documento entonces aumentamos en 1 el contador de frecuencias
                        if (palabraPorDoc.containsKey(palabra)) {
                            int contadorAux = palabraPorDoc.get(palabra);
                            contadorAux ++;
                            palabraPorDoc.put(palabra, contadorAux);
                        }
                        else
                        {
                            // Si la hash map no tenia la palabra, la agregamos y ponemos el contador en 1
                            palabraPorDoc.put(palabra, 1);
                        }

                        // Guarda la palabra en la base
                        sendPostPalabras(palabra);

                        // Guarda el documento en la base
                        sendPostPosteos(contadorDocumentos, palabra);
                    }
                }
            }

            // Aca es cuando se termina de leer todo el documento, entonces aca tenemos que actualizar en las tablas de la base lo que nos dio la hash map

            Set<String> keyHashMap = palabraPorDoc.keySet();
            Collection<Integer> values = palabraPorDoc.values();

            Iterator iteratorKey = keyHashMap.iterator();
            Iterator iteratorValue = values.iterator();

            while (iteratorKey.hasNext()) {

                String key = String.valueOf(iteratorKey.next());
                int value = Integer.parseInt(String.valueOf(iteratorValue.next()));

                sendPutPosteosFrecuencia((long) contadorDocumentos, key, value);

                if (aparicionDocumentos.containsKey(key)) {
                    int contadorAux = aparicionDocumentos.get(key);
                    contadorAux++;
                    aparicionDocumentos.put(key, contadorAux);
                }
                else
                {
                    aparicionDocumentos.put(key, 1);
                }


            }

            /*
            while (keyHashMap.iterator().hasNext()) {
                System.out.println("Keys");
                System.out.println(keyHashMap.iterator().next());
            }

             */


            contadorDocumentos++;
        }

        Set<String> keyHashMap = aparicionDocumentos.keySet();
        Collection<Integer> values = aparicionDocumentos.values();

        Iterator iteradorPalabras = keyHashMap.iterator();
        Iterator iteradorValues = values.iterator();

        while (iteradorPalabras.hasNext()) {

            String key = String.valueOf(iteradorPalabras.next());
            int value = Integer.parseInt(String.valueOf(iteradorValues.next()));

            sendPutPalabras(key, value);
        }

    }

    /*
    public void index() throws IOException {
        File folder = new File("D:\\opt\\Facu\\DLC\\TP\\src\\main\\resources\\Prueba");
        int contador = 0;
        boolean flagPalabra = false;
        HashMap<String, Integer> palabraPorDoc = new HashMap<String, Integer>();
        ArrayList<String> stopWords = stopWords();
        stopWords = stopWords();


        for (File file : folder.listFiles()) {
            this.sendPostDocumento(contador, file.getName());
            String linea;
            String palabra;
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((linea = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linea, " \n1234567890.,;:!?-_\"()[]{}¡¿#$%&/=*-+*|°¬@");
                while (st.hasMoreTokens()) {
                    palabra = st.nextToken();
                    if (!stopWords.contains(palabra)) {
                        sendPostPalabras(palabra, flagPalabra);
                        flagPalabra = true;
                    }
                contador++;
                flagPalabra = false;
            }
        }
    }
     */


    public ArrayList<String> stopWords() throws IOException {
        File file = new File("C:\\» Universidad\\DLC\\TP\\src\\main\\resources\\stop_words_spanish.txt");
        ArrayList<String> words = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            words.add(line);
        }
        return words;
//        System.out.println(words);
    }


    /**
     * StringTokenizer st = new StringTokenizer(archivo, delimitidador);
     * <p>
     * while (st.hasMoreTokens()) {  System.out.println(st.nextToken());
     */


    public static void sendGetDocumento() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet("http://localhost:8080/documentos/all");


        // Esto se agrega cunado necesitamos pasar los parametros (los del postman)
        //request.addHeader("id", "444");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpGet request2 = new HttpGet("http://localhost:8080/documentos/all");

            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        }

    }

    public void sendPostDocumento(int contador, String nombreDoc) throws IOException {
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        HttpPost post = new HttpPost("http://localhost:8080/documentos/add");

        // Esto se agrega cunado necesitamos pasar los parametros (los del postman)
        urlParameters.add(new BasicNameValuePair("nombre", nombreDoc));
        urlParameters.add(new BasicNameValuePair("id", Integer.toString(contador)));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }

    public static void sendPostPalabras(String nombrePalabra) throws IOException {
        HttpPost post = new HttpPost("http://localhost:8080/palabras/add");
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("nombre", nombrePalabra));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }

    public static void sendPutPalabras(String nombrePalabra, int cantidadDocumentos) throws IOException {
        HttpPut put = new HttpPut("http://localhost:8080/palabras/put");
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("nombre", nombrePalabra));
        urlParameters.add(new BasicNameValuePair("cantidadDocumentos", Integer.toString(cantidadDocumentos)));
        put.setEntity(new UrlEncodedFormEntity(urlParameters));

        try {
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse response = closeableHttpClient.execute(put);

            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static void sendPostPosteos(int contador, String nombreDoc) throws IOException {
        HttpPost post = new HttpPost("http://localhost:8080/posteos/add");
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("id_documento", Integer.toString(contador)));
        urlParameters.add(new BasicNameValuePair("nombre_palabra", nombreDoc));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }

    public static void sendPutPosteosFrecuencia(Long id_Documento, String nombrePalabra, int frecuencia) throws IIOException, UnsupportedEncodingException {
        HttpPut put = new HttpPut("http://localhost:8080/posteos/updateFrecuencia");
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("id_documento", Long.toString(id_Documento)));
        urlParameters.add(new BasicNameValuePair("nombre_palabra", nombrePalabra));
        urlParameters.add(new BasicNameValuePair("frecuencia", Integer.toString(frecuencia)));
        put.setEntity(new UrlEncodedFormEntity(urlParameters));

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(put);

            System.out.println(EntityUtils.toString(response.getEntity()));

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
