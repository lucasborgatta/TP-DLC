package com.utn.dlc.app.controller;
import com.utn.dlc.app.entity.Palabra;
import com.utn.dlc.app.repository.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping(path = "/palabras")

public class PalabraController {

    @Autowired
    private PalabraRepository palabraRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewPalabra(@RequestParam String nombre){

        Palabra palabra = new Palabra();
        palabra.setNombre(nombre);

        palabraRepository.save(palabra);
        return "Saved";
    }

    @GetMapping(path= "/all")
    public @ResponseBody Iterable<Palabra> getAllPalabras(){
        ResponseEntity.ok().build();
        return palabraRepository.findAll();
    }

    @GetMapping(path = "/id")
    public @ResponseBody Optional<Palabra> getPalabraByID(@RequestParam String nombre) {
        return palabraRepository.findById(nombre);
    }

    @DeleteMapping(path = "/delete")
    public @ResponseBody String deleteByID(@RequestParam String nombre){

        if (palabraRepository.existsById(nombre)) {
            palabraRepository.deleteById(nombre);
            return "Deleted";
        }
        else
            return "404 Not Found";
    }
}
