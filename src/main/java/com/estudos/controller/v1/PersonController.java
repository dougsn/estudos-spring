package com.estudos.controller.v1;

import com.estudos.data.v1.PersonVO;
import com.estudos.services.v1.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {
    @Autowired
    private PersonServices services;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<PersonVO> findAll() {
        return services.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonVO findById(@PathVariable Long id) {
        return services.findById(id);
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PersonVO> create(@RequestBody PersonVO person) {
        return ResponseEntity.status(HttpStatus.CREATED).body(services.create(person));
    }

    @PutMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PersonVO> update(@RequestBody PersonVO person) {
        PersonVO newPerson = services.update(person);
        return ResponseEntity.status(HttpStatus.OK).body(newPerson);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }


}