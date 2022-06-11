package com.utn.dlc.app.controller;


import com.utn.dlc.app.entity.Posteo;
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
    void addNewPosteo(@RequestParam Long id_documento, String nombre_palabra) {

        Posteo posteo = new Posteo();
        posteo.setId_documento(id_documento);
        posteo.setNombre_palabra(nombre_palabra);

        posteoRepository.save(posteo);

        //return "Posteo Saved";
    }

    @GetMapping(path = "/all") // Todos
    public @ResponseBody
    Iterable<Posteo> getAllPosteos() {
        ResponseEntity.ok().build();
        return posteoRepository.findAll();
    }
}


