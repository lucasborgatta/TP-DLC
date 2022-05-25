//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@SpringBootApplication
@EnableJpaAuditing
public class TpuAplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TpuAplication.class, args);

		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://tpi-dlc.mysql.database.azure.com:3306/tpi?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";

		Class.forName(driver).newInstance();
		Connection connection = DriverManager.getConnection(url, "Programa", "Programa123456789");

		Index in = new Index();
		in.index();

		String consulta = "Select MAX(frecuencia), nombre_palabra from posteos group by nombre_palabra";
		Statement statement = connection.createStatement();
		ResultSet rs;
		rs = statement.executeQuery(consulta);
		System.out.println(rs);
	}

}
