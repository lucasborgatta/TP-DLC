//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


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
		ArrayList arrayList = new ArrayList();
		arrayList = in.getDatosPeso(connection, "aaron");
		ArrayList ar2 = (ArrayList) arrayList.get(0);
		int cantDocs = (int) arrayList.get(1);
		for (int i = 0; i < ar2.size(); i++) {
			ArrayList ar3 = (ArrayList) ar2.get(i);
			System.out.println("Frecuencia: "+ ar3.get(0)+ " Id Documento: " + ar3.get(1));
		}
		System.out.println("La cantidad de documentos dodne aparece aaron es: " + cantDocs);

	}
}

