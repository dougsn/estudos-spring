package com.estudos.controller.v2;

import com.estudos.data.v2.PersonVOV2;
import com.estudos.services.v1.PersonServices;
import com.estudos.services.v2.PersonServicesV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person/v2")
public class PersonControllerV2 {
    @Autowired
    private PersonServicesV2 services;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonVOV2> createV2(@RequestBody PersonVOV2 person) {
        return ResponseEntity.status(HttpStatus.CREATED).body(services.createV2(person));
    }



}
