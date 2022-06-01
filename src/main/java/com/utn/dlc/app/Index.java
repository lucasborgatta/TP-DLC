//package com.utn.dlc.app;
//
//import org.apache.http.Header;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import java.io.*;
//import java.util.*;
//
//public class Index {
//    private CloseableHttpClient httpClient = HttpClients.createDefault();
//
//    public void index() throws IOException {
//        File folder = new File("D:\\opt\\Facu\\DLC\\TP\\src\\main\\resources\\Prueba");
//        int contador = 0;
//        boolean flagPalabra = false;
//        ArrayList<String> stopWords = stopWords();
//        stopWords = stopWords();
//        for (File file : folder.listFiles()) {
//            this.sendPostDocumento(contador, file.getName());
//            String linea;
//            String palabra;
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            linea = br.readLine();
//            StringTokenizer st = new StringTokenizer(linea, " \n1234567890.,;:!?-_\"()[]{}¡¿#$%&/=*-+*|°¬@");
//            while (st.hasMoreTokens()) {
//                palabra = st.nextToken();
//                if (!stopWords.contains(palabra)) {
//                    sendPostPalabras(palabra, flagPalabra);
//                    flagPalabra = true;
//                }
//
//            }
//            contador++;
//            flagPalabra = false;
//        }
//    }
//
//    public ArrayList<String> stopWords() throws IOException {
//        File file = new File("D:\\opt\\Facu\\DLC\\TP\\src\\main\\resources\\stop_words_spanish.txt");
//        ArrayList<String> words = new ArrayList<String>();
//        BufferedReader br = new BufferedReader(new FileReader(file));
//
//        String line;
//        while ((line = br.readLine()) != null) {
//            words.add(line);
//        }
//        return words;
//    }
//
//}
//
