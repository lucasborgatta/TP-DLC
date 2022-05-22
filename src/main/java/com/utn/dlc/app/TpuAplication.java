//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import com.utn.dlc.app.entity.Palabra;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


@SpringBootApplication
@EnableJpaAuditing
public class TpuAplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TpuAplication.class, args);

		long startTime = System.currentTimeMillis();

		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://tpi-dlc.mysql.database.azure.com:3306/tpi?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";

		Class.forName(driver).newInstance();
		Connection connection = DriverManager.getConnection(url, "Programa", "Programa123456789");

		Index in = new Index();
		in.index(connection);

		long endtime = System.currentTimeMillis() - startTime;
		System.out.println(endtime);

	}







		/*
		for (int i = 0; i < palabras.size(); i++) {


			Palabra palabra = palabras.get(i);
			System.out.println(palabra.getNombre());


			statement.setString(1, palabras.get(i));
			statement.setInt(2, 0);
			statement.addBatch();
		}

		statement.executeBatch();
		System.out.println("BATCH");


		 */
	}

