//Esta es la clase que arranca cunado iniciamos la aplicacion
package com.utn.dlc.app;

import com.utn.dlc.app.controller.DocumentoController;
import com.utn.dlc.app.entity.Documento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;


@SpringBootApplication
@EnableJpaAuditing
public class TpuAplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(TpuAplication.class, args);
        DocumentoController dc = new DocumentoController();

    }

}
