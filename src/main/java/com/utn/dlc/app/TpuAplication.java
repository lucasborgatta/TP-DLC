//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;


@SpringBootApplication
@EnableJpaAuditing
public class TpuAplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(TpuAplication.class, args);
	}
}
