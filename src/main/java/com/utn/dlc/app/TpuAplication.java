//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class TpuAplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TpuAplication.class, args);

		Vocabulario cp = new Vocabulario();
		cp.setVocabulario();
	}

}
