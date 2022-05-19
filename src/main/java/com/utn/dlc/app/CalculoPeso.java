package com.utn.dlc.app;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CalculoPeso {

    public void calculoPeso(){
        File folder = new File("C:\\Users\\nahue\\OneDrive\\Documentos\\GitHub\\TP-DLC\\src\\main\\resources\\Prueba");
        HashMap<Integer, String> palabras = getAllPalabras();

        for (File file : folder.listFiles()) {


        }
    }

    public ArrayList<String> getPalabraById(String value) {
        HttpClient client = HttpClientBuilder.create().build();
        String url = "http://localhost:8080/palabras/id?nombre=" + value;
        HttpGet get = new HttpGet(url);
        ArrayList<String> getResponse = new ArrayList<String>();

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
        return getResponse;
    }

    public ArrayList<String> getPosteoById(Long id_documento, String nombre_palabra) {
        HttpClient client = HttpClientBuilder.create().build();
        String url = "http://localhost:8080/posteos/id?id_documento=" + id_documento + "&nombre_palabra=" + nombre_palabra;
        HttpGet get = new HttpGet(url);
        ArrayList<String> getResponse = new ArrayList<String>();

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
                            if (!(palabra.equals("id_documento") || palabra.equals("nombre_palabra") || palabra.equals("frecuencia") || palabra.equals("peso"))) {
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
        return getResponse;
    }

    public HashMap<Integer, String> getAllPalabras() {
        HttpClient client = HttpClientBuilder.create().build();
        String url = "http://localhost:8080/palabras/all";
        HttpGet get = new HttpGet(url);
        HashMap<Integer, String> getResponse = new HashMap<>();
        int key = 0;

        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream stream = entity.getContent()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    String palabra;
                    while ((line = reader.readLine()) != null) {
                        StringTokenizer st = new StringTokenizer(line, " \n1234567890:,\"{}");
                        while (st.hasMoreTokens()) {
                            palabra = st.nextToken();
                            if (!(palabra.equals("nombre") || palabra.equals("cantDocumentos"))) {
                                System.out.println(palabra);
                                getResponse.put(key, palabra);
                                key++;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResponse;
    }

}
