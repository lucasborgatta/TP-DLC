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
import java.util.*;

public class Index {

    public Set<String> index() throws IOException {

        long startTime = System.currentTimeMillis();

        File folder = new File("C:\\» Universidad\\DLC\\TP\\src\\main\\resources\\Prueba");
        int contadorDocumentos = 0;
        ArrayList<String> stopWords = stopWords();

        HashMap<String, Integer> arrayList = new HashMap<String, Integer>();

        /*
        HashMap<String, Integer> hashMapPalabrasdeTodosLosDocumentos = new HashMap<>();
        HashMap<String, Integer> hashMapPosteoPorDocumento = new HashMap<String, Integer>();

        Set<String> keyHashMapPosteos;
        Collection<Integer> frecuencias;

        Set<String> keyHashMapPalabras;
        Collection<Integer> cantidadDocumentos;

         */

        for (File file : folder.listFiles()) {

            //this.sendPostDocumento(contadorDocumentos, file.getName());
            System.out.println("POSTEO DOCUMENTO");

            System.out.println(file.getName());

            String linea;
            String palabra;

            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((linea = br.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(linea, " \n1234567890.,;:!?-_\"()[]{}¡¿#$%&/=*-+*|°¬@<>;'�");

                while (st.hasMoreTokens()) {

                    palabra = st.nextToken().toLowerCase();

                    if (!stopWords.contains(palabra)) { // Controlamos que la palabra no sea una stop word del diccionario

                        Palabra palabra1 = new Palabra();
                        palabra1.setNombre(palabra);

                        arrayList.put(palabra, 0);
                        }
                    }
                }
            }
        Set<String> keySet = arrayList.keySet();
        return keySet;
    }

                        /*
                        // Guardamos en la hash map
                        // Si la hash map ya tenia la palabra, osea que aparecio en el documento entonces aumentamos en 1 el contador de frecuencias
                        if (hashMapPosteoPorDocumento.containsKey(palabra)) {
                            int contadorAux = hashMapPosteoPorDocumento.get(palabra);
                            contadorAux ++;
                            hashMapPosteoPorDocumento.put(palabra, contadorAux);
                        }
                        else
                        {
                            // Si la hash map no tenia la palabra, la agregamos y ponemos el contador en 1
                            hashMapPosteoPorDocumento.put(palabra, 1);
                        }

                        // Guarda la palabra en la base
                        //sendPostPalabras(palabra);

                        // Guarda el documento en la base
                        sendPostPosteos(contadorDocumentos, palabra);
                        System.out.println("POSTEO DE POSTEOS");
                    }
                }
            }

            // Aca es cuando se termina de leer todo el documento, entonces aca tenemos que actualizar en las tablas de la base lo que nos dio la hash map

            keyHashMapPosteos = hashMapPosteoPorDocumento.keySet();
            frecuencias = hashMapPosteoPorDocumento.values();

            Iterator iteratorKey = keyHashMapPosteos.iterator();
            Iterator iteratorValue = frecuencias.iterator();

            // Actualizamos la tabla de posteos con los datos que leimos del documento
            while (iteratorKey.hasNext()) {

                String palabraKey = String.valueOf(iteratorKey.next());
                int frecuenciasValue = Integer.parseInt(String.valueOf(iteratorValue.next()));

                sendPutPosteosFrecuencia((long) contadorDocumentos, palabraKey, frecuenciasValue);

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

            // Borramos la HashMap de posteos
            keyHashMapPosteos.clear();
            frecuencias.clear();

            contadorDocumentos++;
            hashMapPosteoPorDocumento.clear();
        }

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
        return getResponse;
    }

                         */


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


    /**
     * StringTokenizer st = new StringTokenizer(archivo, delimitidador);
     * <p>
     * while (st.hasMoreTokens()) {  System.out.println(st.nextToken());
     */


    public static void sendGetDocumento() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet("http://localhost:8080/documentos/all");

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

            //System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }

    public static void sendPostPalabras(String nombrePalabra) throws IOException {
        HttpPost post = new HttpPost("http://localhost:8080/palabras/add");
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("nombre", nombrePalabra));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            //System.out.println(EntityUtils.toString(response.getEntity()));
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

            //System.out.println(EntityUtils.toString(response.getEntity()));
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

            //System.out.println(EntityUtils.toString(response.getEntity()));
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

            //System.out.println(EntityUtils.toString(response.getEntity()));

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
