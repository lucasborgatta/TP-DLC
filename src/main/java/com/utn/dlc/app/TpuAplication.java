//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import com.utn.dlc.app.entity.Palabra;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.sql.*;
import java.util.*;


@SpringBootApplication
@EnableJpaAuditing
public class TpuAplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TpuAplication.class, args);

		long startTime = System.currentTimeMillis();

		// Esto es para poder hacer la conexion a la base y tener una conexion que podamos manejar con codigo
		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://tpi-dlc.mysql.database.azure.com:3306/tpi?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";

		Class.forName(driver).newInstance();
		Connection connection = DriverManager.getConnection(url, "Programa", "Programa123456789");



		Index in = new Index();
//		in.index(connection);
		in.getFrecuenciaMaxPorPalabra(connection);

//		String consulta = "Select MAX(frecuencia), nombre_palabra from posteos group by nombre_palabra";
//		Statement statement = connection.createStatement();
//		ResultSet rs;
//		rs = statement.executeQuery(consulta);
//		Hashtable<String, Integer> frecuenciaMax = new Hashtable<String, Integer>();
//		while (rs.next()) {
//
//			int frecuencia = rs.getInt(1);
//			String palabra = rs.getString(2);
//			if (palabra.equals("addison")) {
//				frecuenciaMax.put(palabra, frecuencia);
//			}
//		}
//		System.out.println(frecuenciaMax);


//		long endtime = System.currentTimeMillis() - startTime;
//		System.out.println(endtime);

	}
}

