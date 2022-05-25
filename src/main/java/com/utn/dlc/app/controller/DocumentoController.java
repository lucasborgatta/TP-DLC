package com.utn.dlc.app.controller;

import com.utn.dlc.app.entity.Documento;
import com.utn.dlc.app.entity.Palabra;
import com.utn.dlc.app.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.Doc;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/documentos")
public class DocumentoController {


	@Autowired
	private DocumentoRepository documentoRepository;

	@PostMapping(path = "/add")
	public @ResponseBody void addNewDocument(@RequestParam Long id, String nombre){ // Los nombres que estan aca son los mismos que van en el postman, si no no funciona
		Documento doc = new Documento();
		doc.setNombre(nombre);
		doc.setId(id);
		documentoRepository.save(doc);
		//return "Saved";
	}

	@PostMapping(path = "/add-all")
	public ResponseEntity<List<Documento>> addAllDocument(List<Documento> documentos){
		return new ResponseEntity<>(documentoRepository.saveAll(documentos), HttpStatus.OK);
	}

//	@GetMapping(path = "/skere")
//	public ResponseEntity<List<Documento>> skere(Long id){
//		return new ResponseEntity<>(documentoRepository.getDocumentoById(id), HttpStatus.OK);
//	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Documento> getAllDocuments(){
		ResponseEntity.ok().build();
		return  documentoRepository.findAll();
	}

	@GetMapping(path = "/id")
	public @ResponseBody Optional<Documento> getDocumentoByID(@RequestParam Long id) {
		return documentoRepository.findById(id);
	}

	@DeleteMapping(path = "/delete")
	public @ResponseBody void deleteDocument(@RequestParam Long id){

		if (documentoRepository.existsById(id)) {
			documentoRepository.deleteById(id);
			//return "Deleted";
		}
		//else
			//return "404 Not Found";
	}

	@PutMapping(path = "/put")
	public @ResponseBody void putDocumento(@RequestParam Long id, String nombre){
		Documento doc = new Documento();
		doc.setId(id);
		doc.setNombre(nombre);
		documentoRepository.saveAndFlush(doc);
		//return "Saved";
	}


}