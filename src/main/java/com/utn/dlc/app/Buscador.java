package com.utn.dlc.app;



import com.utn.dlc.app.entity.Documento;
import com.utn.dlc.app.entity.Posteo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Buscador {
    private List<Documento> ld;
    private String q;
    private int N;
    private int R;
    private HashMap<String, ArrayList<Integer>> vocabulario;
    Vocabulario vocabulario2 = new Vocabulario();

    public Buscador(List<Documento> ld, String q) throws Exception {
        this.ld = ld;
        this.q = q;
        Vocabulario vocabulario = new Vocabulario();
        this.vocabulario = vocabulario.getDatosVocabulario();
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


    public void buscarPalabra(){
        Scanner sc = new Scanner(System.in);  //Se crea un objeto Scanner
        String q;
        System.out.print("Introduzca la consulta Q: ");
        this.setQ(sc.nextLine());  //leer un String

        while (sc.hasNext() == true){
            List<Double> arraylist = this.calculoIDF(this.q);
            arraylist.get(0);
            for (int i = 0; i < this.vocabulario2.getPosteoById.size(); i++) {
                if(i == R){
                    break;
                }

            }


        }
    }

    public List<Double> calculoIDF(String q){
        List<Double> arrayList= new ArrayList<>();
        String[] split = q.split(" ");
        for (int i = 0; i < q.length(); i++) {
            int nr = (this.vocabulario.get(split[i])).get(0);
            arrayList.add(Math.log10(this.N / nr));
        }
        Collections.reverse(arrayList);
        return arrayList;
    }

    public int getN() {
        return N;
    }

    public void setN() throws Exception {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://tpi-dlc.mysql.database.azure.com:3306/tpi?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
        Class.forName(driver).newInstance();
        Connection connection = DriverManager.getConnection(url, "Programa", "Programa123456789");
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
}
