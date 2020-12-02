package com.formacionbdi.microservicios.commons.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.formacionbdi.microservicios.commons.services.CommonService;

public class CommonController<E, S extends CommonService<E>> {

	@Autowired
	protected S commonService;
	
	/* METODOS GET */
	
	@GetMapping
	public ResponseEntity<?> listar(){		
		return ResponseEntity.ok().body(commonService.findAll());
	}
	
	@GetMapping("/pagina")
	public ResponseEntity<?> listar(Pageable pageable){		
		return ResponseEntity.ok().body(commonService.findAll(pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable Long id){
		Optional<E> o= commonService.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();				
		}
		return ResponseEntity.ok(o.get());
	}
	
	/* METODOS POST */
	
	@PostMapping
	public ResponseEntity<?> agregar(@Valid @RequestBody E entity, BindingResult result){
		if(result.hasErrors()) {
			return validar(result);
		}
		E entityDb = commonService.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(entityDb);
	}
	
	/* METODOS DELETE */
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {		
		commonService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	/* OTROS METODOS */

	protected ResponseEntity<?> validar(BindingResult result){
		Map<String, Object> errores = new HashMap<>();
		result.getFieldErrors().forEach(err ->{
			errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(errores);
	}
}