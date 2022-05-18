package com.utn.dlc.app.controller;


import com.utn.dlc.app.entity.Posteo;
import com.utn.dlc.app.entity.PosteoPKId;
import com.utn.dlc.app.repository.PosteoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/posteos")
public class PosteoController {

    @Autowired
    private PosteoRepository posteoRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewPosteo(@RequestParam Long id_documento, String nombre_palabra) {

        System.out.println("Posteo paso 2");

        Posteo posteo = new Posteo();
        posteo.setId_documento(id_documento);
        posteo.setNombre_palabra(nombre_palabra);

        posteoRepository.save(posteo);

        System.out.println("Posteo paso 3");

        return "Posteo Saved";
    }

    @GetMapping(path = "/all") // Todos
    public @ResponseBody Iterable<Posteo> getAllPosteos() {
        ResponseEntity.ok().build();
        return posteoRepository.findAll();
    }

    @GetMapping(path = "/id") // Busca por pk compuesta
    public @ResponseBody Optional<Posteo> getPosteoByID(@RequestParam Long id_documento, String nombre_palabra) {

        PosteoPKId posteoPKId = new PosteoPKId();
        posteoPKId.setId(id_documento);
        posteoPKId.setNombrePalabra(nombre_palabra);
        Optional<Posteo> posteo = posteoRepository.findById(posteoPKId);

        return posteo;
    }

    @GetMapping(path = "/cantDoc")
    public @ResponseBody Integer getCantidadDocumentosById(@RequestParam String nombre_palabra, int cantidadDocumentosTotales) {

        int suma = 0;

        PosteoPKId posteoPKId = new PosteoPKId();
        posteoPKId.setNombre_palabra(nombre_palabra);

        for (int i = 1; i < cantidadDocumentosTotales; i++) {
            posteoPKId.setId((long) i);
            if (posteoRepository.existsById(posteoPKId)) {
                suma ++;
            }
        }
        return suma;
    }

    @DeleteMapping(path = "/delete") // Borra por pk compuesta
    public String delete(@RequestParam Long id_documento, String nombre_palabra) {

        PosteoPKId posteoPKId = new PosteoPKId();
        posteoPKId.setId(id_documento);
        posteoPKId.setNombrePalabra(nombre_palabra);

        if (posteoRepository.existsById(posteoPKId)) {
            posteoRepository.deleteById(posteoPKId);
            return "Deleted";
        }
        else
            return "404 Not Found";

    }

    @PutMapping(path = "/update")
    public @ResponseBody
    String addPesoPosteo(@RequestParam Long id_documento, String nombre_palabra, Double peso) {

        Posteo posteo = new Posteo();
        posteo.setId_documento(id_documento);
        posteo.setNombre_palabra(nombre_palabra);

        // Creamos una instancia de posteoPKId
        PosteoPKId posteoPKId = new PosteoPKId();
        posteoPKId.setId(id_documento);
        posteoPKId.setNombrePalabra(nombre_palabra);

        if (posteoRepository.existsById(posteoPKId)) { // Si este posteo ya existe en la tabla lo unico que tenemos que hacer es incrementar en 1 su frecuencia
            posteo.setPeso(peso);
        }
        else
        {
            return "404 Not Found";
        }

        posteoRepository.save(posteo);

        return "Updated";
    }

    @PutMapping(path = "/updateFrecuencia")
    public @ResponseBody String addFrecuenciaPosteo(@RequestParam Long id_documento, String nombre_palabra, Long frecuencia){
        Posteo posteo = new Posteo();
        posteo.setId_documento(id_documento);
        posteo.setNombre_palabra(nombre_palabra);

        // Creamos una instancia de posteoPKId para verificar si existe
        PosteoPKId posteoPKId = new PosteoPKId();
        posteoPKId.setId(id_documento);
        posteoPKId.setNombrePalabra(nombre_palabra);

        if (posteoRepository.existsById(posteoPKId)) { // Si este posteo ya existe en la tabla lo unico que tenemos que hacer es incrementar en 1 su frecuencia
            posteo.setFrecuencia(frecuencia);
        }
        else
        {
            return "404 Not Found";
        }

        posteoRepository.saveAndFlush(posteo);

        return "Updated";
    }

    @PutMapping(path = "/updatePeso")
    public @ResponseBody String addPesoPosteo(@RequestParam Long id_documento, String nombre_palabra, Long frecuencia, Double peso) {

        Posteo posteo = new Posteo();
        posteo.setId_documento(id_documento);
        posteo.setNombre_palabra(nombre_palabra);
        posteo.setFrecuencia(frecuencia);

        // Creamos una instancia de posteoPKId para verificar si existe
        PosteoPKId posteoPKId = new PosteoPKId();
        posteoPKId.setId(id_documento);
        posteoPKId.setNombrePalabra(nombre_palabra);

        if (posteoRepository.existsById(posteoPKId)) { // Si este posteo ya existe en la tabla lo unico que tenemos que hacer es incrementar en 1 su frecuencia
            posteo.setPeso(peso);
        }
        else
        {
            return "404 Not Found";
        }

        posteoRepository.saveAndFlush(posteo);

        return "Updated";
    }
}



