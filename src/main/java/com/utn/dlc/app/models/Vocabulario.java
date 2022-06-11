package com.utn.dlc.app.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class Vocabulario {

    /*
    public void setVocabulario() throws Exception {
        HashMap<String, ArrayList<Integer>> vocabulario;
        vocabulario = getDatosVocabulario();
    }
     */

    public HashMap<String, ArrayList<Integer>> getDatosVocabulario(Connection connection) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        HashMap<String, ArrayList<Integer>> hash = new HashMap<>();
        String consulta = "select p.nombre_palabra, count(p.id_documento), max(p.frecuencia) from posteos p group by p.nombre_palabra";
        Statement statement = connection.createStatement();
        ResultSet rs;
        rs = statement.executeQuery(consulta);
        while (rs.next()){
            ArrayList<Integer> arrayList= new ArrayList<>();
            int frecuenciaMax = rs.getInt(3);
            int cantidadDocs = rs.getInt(2);
            String palabra = rs.getString(1);
            arrayList.add(cantidadDocs);
            arrayList.add(frecuenciaMax);
            hash.put(palabra, arrayList);
        }
        return hash;
    }

}
