package com.utn.dlc.app.controller;


import com.utn.dlc.app.entity.Posteo;
import com.utn.dlc.app.entity.PosteoPKId;
import com.utn.dlc.app.repository.PosteoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posteos")
public class PosteoController {

    @Autowired
    private PosteoRepository posteoRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewPosteo(@RequestParam Long id_documento, String nombre_palabra) {

        Posteo posteo = new Posteo();
        posteo.setId_documento(id_documento);
        posteo.setNombre_palabra(nombre_palabra);
        posteoRepository.save(posteo);
        return "Saved";
    }

    @PostMapping(path = "/add-all")
    public ResponseEntity<List<Posteo>> addAllPosteos(List<Posteo> posteos){
        return new ResponseEntity<>(posteoRepository.saveAll(posteos), HttpStatus.OK);
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
        }
        else
        {
            return "404 Not Found";
        }

        posteoRepository.saveAndFlush(posteo);

        return "Updated";
    }


}



