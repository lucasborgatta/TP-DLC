//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;


@SpringBootApplication
@EnableJpaAuditing
public class TpuAplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TpuAplication.class, args);

		Index in = new Index();
		in.index();

	}

}
