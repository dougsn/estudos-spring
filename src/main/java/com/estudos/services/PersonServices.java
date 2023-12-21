package com.estudos.services;

import com.estudos.data.PersonVO;
import com.estudos.repository.PersonRepository;
import com.estudos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public List<PersonVO> findAll(){
        logger.info("Finding all person!");

        return repository.findAll();
    }

    public PersonVO findById(Long id){
        logger.info("Finding one person!");

        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + id + " not found."));
    }

    public PersonVO create(PersonVO person){
        logger.info("Creating one person!");

        return repository.save(person);
    }

    public PersonVO update(PersonVO person){
        logger.info("Updating one person!");

        PersonVO entity = repository.findById(person.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + person.getId() + " not found."));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(person);
    }

    public void delete(Long id){
        logger.info("Deleting one person!");
        PersonVO entity = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + id + " not found."));

        repository.delete(entity);

    }

}
