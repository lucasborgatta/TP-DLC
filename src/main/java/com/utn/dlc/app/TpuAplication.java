//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import com.utn.dlc.app.controller.DocumentoController;
import com.utn.dlc.app.entity.Documento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@EnableJpaAuditing
public class TpuAplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(TpuAplication.class, args);
	}
}
