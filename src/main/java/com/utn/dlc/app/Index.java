package com.utn.dlc.app;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

import java.io.*;
import java.util.*;

public class Index {
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    public void index() throws IOException {
        File folder = new File("D:\\opt\\Facu\\DLC\\TP\\src\\main\\resources\\Prueba");
        int contador = 0;
        boolean flagPalabra = false;
        ArrayList<String> stopWords = stopWords();
        stopWords = stopWords();
        for (File file : folder.listFiles()) {
            this.sendPostDocumento(contador, file.getName());
            String linea;
            String palabra;
            BufferedReader br = new BufferedReader(new FileReader(file));
            linea = br.readLine();
            StringTokenizer st = new StringTokenizer(linea, " \n1234567890.,;:!?-_\"()[]{}¡¿#$%&/=*-+*|°¬@");
            while (st.hasMoreTokens()) {
                palabra = st.nextToken();
                if (!stopWords.contains(palabra)) {
                    sendPostPalabras(palabra, flagPalabra);
                    flagPalabra = true;
                }

            }
            contador++;
            flagPalabra = false;
        }
    }

    public ArrayList<String> stopWords() throws IOException {
        File file = new File("D:\\opt\\Facu\\DLC\\TP\\src\\main\\resources\\stop_words_spanish.txt");
        ArrayList<String> words = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            words.add(line);
        }
        return words;
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

    public void sendPostPalabras(String nombrePalabra, boolean flag) throws IOException {
        HttpPost post = new HttpPost("http://localhost:8080/palabras/add");
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        ArrayList<String> getPalabra;

        if (Objects.nonNull(getPalabraById(nombrePalabra))) {
            getPalabra = getPalabraById(nombrePalabra);
            if (flag == false) {
                int contadorPalabra = Integer.parseInt(getPalabra.get(1));
                sendPutPalabra(nombrePalabra, contadorPalabra + 1);
            }

        } else {
            urlParameters.add(new BasicNameValuePair("nombre", nombrePalabra));
            urlParameters.add(new BasicNameValuePair("cant_Documentos", "1"));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }

    public ArrayList<String> getPalabraById(String value) {
        HttpClient client = HttpClientBuilder.create().build();
        String url = "http://localhost:8080/palabras/id?nombre=" + value;
        HttpGet get = new HttpGet(url);
        ArrayList<String> getResponse = new ArrayList<String>();
        //List<BasicNameValuePair> urlParameters = new ArrayList<>();
        //urlParameters.add(new BasicNameValuePair("nombre", "bancamos"));
        //request.setHeader("nombre", "bancamos");

        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream stream = entity.getContent()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    String palabra;
                    while ((line = reader.readLine()) != null) {
                        StringTokenizer st = new StringTokenizer(line, " \n:,\"{}");
                        while (st.hasMoreTokens()) {
                            palabra = st.nextToken();
                            if (!(palabra.equals("nombre") || palabra.equals("cantDocumentos"))) {
                                System.out.println(palabra);
                                getResponse.add(palabra);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        HttpGet request = new HttpGet("http://localhost:8080/palabras/id");
//        request.addHeader("nombre", "bancamos");
//        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
//
//        try (CloseableHttpResponse response = httpClient.execute(request)){
//            System.out.println(response.getStatusLine().toString());
//
//            HttpEntity entity = response.getEntity();
//            Header headers = entity.getContentType();
//            System.out.println(headers);
//
//            if (entity != null){
//                String result = EntityUtils.toString(entity);
//                InputStream stream = entity.getContent();
//                InputStreamReader reader = new InputStreamReader(stream);
//                System.out.println(reader.toString());
//            }
//        }
        return getResponse;
    }

    public void sendPutPalabra(String nombrePalabra, int cant_documentos) throws IOException {
        HttpPut put = new HttpPut("http://localhost:8080/palabras/put");
        List<BasicNameValuePair> urlParameters = new ArrayList<>();

        urlParameters.add(new BasicNameValuePair("nombre", nombrePalabra));
        urlParameters.add(new BasicNameValuePair("cant_documentos", String.valueOf(cant_documentos)));
        put.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(put)) {
            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }
}

