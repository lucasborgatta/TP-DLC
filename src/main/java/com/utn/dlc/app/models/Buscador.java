package com.utn.dlc.app.models;

import com.utn.dlc.app.entity.Documento;
import com.utn.dlc.app.models.Vocabulario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Buscador {
    private HashMap<String, Double> ld;
    private List<Documento> documentos;
    private String q;
    private int N;
    private HashMap<String, ArrayList<Integer>> vocabulario;
    private ArrayList<String> pk;
    private Connection connection;


    public Buscador(String q, Connection conexion) throws Exception {
        ld = new HashMap<>();
        this.q = q;
        Vocabulario vocabulario = new Vocabulario();
        this.vocabulario = vocabulario.getDatosVocabulario(conexion);
        this.connection = conexion;
    }

    /**
     * 1. Sea LD = [ ] (lista vacía).
     * 2. Mientras queden términos en q sin procesar:
     *      a. En q tomar el término tk que tenga menor longitud de Pk (es decir, el que tenga el mayor idf).
     *      b. En la lista Pk de tk, repetir R veces:
     *          i. Si el documento actual di no está en LD agregar en LD el documento actual di, junto con su índice ir de relevancia (valor inicial ir = 0). Mantener LD ordenada por ir.
     *          ii. Sumar en el ir del documento di el valor tfk * idf (la frecuencia del término tk en el documento di por la frecuencia inversa del término tk).
     *      c. Tanto si se recuperaron R documentos en el paso anterior, como si no, volver al ciclo del paso 2, y continuar con el siguiente término.
     * 3. Mostrar los primeros R documentos de LD (incluyendo el índice ir de cada uno si se trata de un prototipo).
     */


    public void buscarPalabra(int R) throws Exception{
        Scanner sc = new Scanner(System.in);  //Se crea un objeto Scanner
        String q;
        System.out.print("Introduzca la consulta Q: ");
        this.setQ(sc.nextLine());  //leer un String
        this.setPk();
        this.setN();
        HashMap<String, Double> terminosIdf = this.calculoIDF(this.q);

        Collection<Double> collection = terminosIdf.values();
        Iterator iterator = collection.iterator();
        ArrayList<Double> arrayList = new ArrayList<>();

        while (iterator.hasNext()) {
            arrayList.add((Double) iterator.next());
        }

        Collections.reverse(arrayList);
        System.out.println("Array " + arrayList);
        int j = 0;

        ArrayList documentos = new ArrayList<>();
        //consulta = Quijote de la mancha
        double mayoridf;

        StringTokenizer stringTokenizer = new StringTokenizer(this.q);

        while (stringTokenizer.hasMoreTokens() && (ld.size() < R)) {
            System.out.println("hasnext");
            mayoridf = arrayList.get(j);
            System.out.println("Mayor idf " + mayoridf);
            j++;
            documentos = buscarD(stringTokenizer.nextToken());
            ArrayList<String> nombresDocs = (ArrayList<String>) documentos.get(0);
            ArrayList<Integer> frecuenciaDocs = (ArrayList<Integer>) documentos.get(1);

            System.out.println("Documentos " + nombresDocs);
            System.out.println("frecuenciaDocs " + frecuenciaDocs);

            for (int i = 0; i < nombresDocs.size(); i++) {
                double ir;
                String di = nombresDocs.get(i);
                System.out.println("Numero documento " + di);
                if (ld.isEmpty()){
                    ir = (double) mayoridf * (double) frecuenciaDocs.get(i);
                    System.out.println("ir cuando es vacia " + ir);
                    this.ld.put(di, ir);
                }
                else
                {
                    if (!ld.containsKey(di) && ld.size() < R) {
                        ir = mayoridf * frecuenciaDocs.get(i);
                        System.out.println("ir cuando no es vacia " + ir);
                        this.ld.put(di, ir);
                    }
                }
            }
        }
        this.ld = sortByValue(this.ld); //todo ESTA AL REVES, HAY Q MOSTRARLA EL VERRE
        System.out.println("Tabla ld " + ld);
    }

    public HashMap<String, Double> calculoIDF(String q){
        HashMap<String, Double> hashMap = new HashMap<>();
        String[] split = q.split(" ");

        System.out.println("Documentos " + this.N);
        System.out.println("Lenght " + q.length());
        System.out.println(q);
        System.out.println(split.length);

        for (int i = 0; i < split.length; i++) {
            int nr = (this.vocabulario.get(split[i])).get(0);
            hashMap.put(split[i], Math.log10(this.N / nr)); //todo Controlar
        }
        hashMap = sortByValue(hashMap);
        System.out.println("Hashmap " + hashMap);
        return hashMap;
    }

    public int getN() {
        return N;
    }

    public void setN() throws Exception {
        String consulta = "select count(*) from documentos";
        Statement statement = connection.createStatement();
        ResultSet rs;
        rs = statement.executeQuery(consulta);
        while (rs.next()){
            this.N = rs.getInt(1);
        }
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public HashMap<String, ArrayList<Integer>> getVocabulario() {
        return vocabulario;
    }

    public void setVocabulario(HashMap<String, ArrayList<Integer>> vocabulario) {
        this.vocabulario = vocabulario;
    }
    public ArrayList<String> setPk() throws Exception{
        ArrayList<String> arrayList = new ArrayList<>();
        String consulta = "select distinct p.nombre_palabra from posteos p";
        Statement statement = connection.createStatement();
        ResultSet rs;
        rs = statement.executeQuery(consulta);
        while (rs.next()){
            arrayList.add(rs.getString(1));
        }
        return arrayList;
    }
    public ArrayList buscarD(String tk) throws Exception{
        ArrayList arrayList= new ArrayList<>();
        ArrayList<String> arrayNombres= new ArrayList<>();
        ArrayList<Integer> arrayFrecuencias= new ArrayList<>();
        String consulta = "select p.id_documento, p.frecuencia from posteos p where p.nombre_palabra like ";
        consulta = consulta + "'" + tk + "'" + " order by p.frecuencia desc";
        Statement statement = connection.createStatement();
        ResultSet rs;
        rs = statement.executeQuery(consulta);
        while (rs.next()){
            arrayNombres.add(rs.getString(1));
            arrayFrecuencias.add(rs.getInt(2));
        }
        arrayList.add(arrayNombres);
        arrayList.add(arrayFrecuencias);
        return arrayList;
    }

    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm)
    {
        // Creating a list from elements of HashMap
        List<Map.Entry<String, Double> > list
                = new LinkedList<>(
                hm.entrySet());

        // Sorting the list using Collections.sort() method
        // using Comparator
        Collections.sort(
                list,
                Comparator.comparing(Map.Entry::getValue));

        // putting the  data from sorted list back to hashmap
        HashMap<String, Double> result
                = new LinkedHashMap<>();
        for (Map.Entry<String, Double> me : list) {
            result.put(me.getKey(), me.getValue());
        }

        // returning the sorted HashMap
        return result;
    }
}