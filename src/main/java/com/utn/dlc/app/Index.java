package com.utn.dlc.app;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.*;

public class Index {

    public void index() throws IOException {
        File folder = new File("C:\\» Universidad\\DLC\\TP\\src\\main\\resources\\Prueba");
        int contadorDocumentos = 0;
        ArrayList<String> stopWords = stopWords();
        for (File file : folder.listFiles()) {
            this.sendPostDocumento(contadorDocumentos, file.getName());
            String linea;
            String palabra;
            BufferedReader br = new BufferedReader(new FileReader(file));

            // while ((line = br.readLine()) != null) { Si no anda lo de abajo usar esto
            linea = br.readLine(); // PROBAR ESTO, SI NO CAMBIARLO PARA QUE LEA TODAS LAS LINEAS

            StringTokenizer st = new StringTokenizer(linea, " \n1234567890.,;:!?-_\"()[]{}¡¿#$%&/=*-+*|°¬@");
            while (st.hasMoreTokens()) {

                palabra = st.nextToken();

                if (!stopWords.contains(palabra)){ // Controlamos que la palabra no sea una stop word del diccionario

                    // Guarda la palabra en la base
                    sendPostPalabras(palabra, false);

                    // Guarda el documento en la base
                    sendPostPosteos(contadorDocumentos, palabra);
                }
            }

            contadorDocumentos++;
        }
    }

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

    public static void sendPostPalabras(String nombrePalabra, boolean flag) throws IOException {
        HttpPost post = new HttpPost("http://localhost:8080/palabras/add");
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("nombre", nombrePalabra));
        urlParameters.add(new BasicNameValuePair("flag", Boolean.toString(flag)));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }

    public static void sendPostPosteos(int contador, String nombreDoc) throws IOException {
        HttpPost post = new HttpPost("http://localhost:8080/posteos/add");
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("id", Integer.toString(contador)));
        urlParameters.add(new BasicNameValuePair("nombre", nombreDoc));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }

}
