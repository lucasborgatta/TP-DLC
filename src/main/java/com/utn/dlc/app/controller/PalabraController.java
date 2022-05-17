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
    public @ResponseBody String addNewPalabra(@RequestParam String nombre, boolean flag){

        if (palabraRepository.existsById(nombre)) {
            int cantidadDocumentos = pal.getCantDocumentos();
            System.out.println("Actualizando el contador de la palabra " + nombre);
            pal.setCantDocumentos(cantidadDocumentos + 1);
            System.out.println("Contador actualizado " + cantidadDocumentos + 1);
            palabraRepository.save(pal);
        }
        else
        {
            System.out.println("Contador de la palabra creado " + nombre);
            pal.setCantDocumentos(1);
            palabraRepository.save(pal);
        }

        palabraRepository.save(pal);
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
