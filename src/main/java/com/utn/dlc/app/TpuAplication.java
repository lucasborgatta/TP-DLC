//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;


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

		/* INDEXACION
		Index in = new Index();
		in.index(connection);
		 */

		long endtime = System.currentTimeMillis() - startTime;
		System.out.println(endtime);

		/*
		Scanner scanner = new Scanner(System.in);

		System.out.println("Introduzca la consulta");
		scanner.nextLine();
		System.out.println(scanner.next());

		 */




	}
}

