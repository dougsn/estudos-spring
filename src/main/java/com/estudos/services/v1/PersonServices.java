package com.estudos.services.v1;

import com.estudos.controller.v1.PersonController;
import com.estudos.data.mapper.DozerMapper;
import com.estudos.data.model.Person;
import com.estudos.data.v1.PersonVO;
import com.estudos.repository.PersonRepository;
import com.estudos.services.exceptions.BadRequestException;
import com.estudos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public List<PersonVO> findAll() {
        logger.info("Finding all person!");
        var persons = DozerMapper.parseListsObjects(repository.findAll(), PersonVO.class);
        persons.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        return persons;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person!");
        Person person = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + id + " not found."));

        // Colocando o link hateoas para si mesmo.
        var vo = DozerMapper.parseObject(person, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

        return vo;
    }

    public PersonVO create(PersonVO person) {
        logger.info("Creating one person!");
        if (person == null) throw new BadRequestException("O person não pode ser nulo");

        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updating one person!");
        if (person == null) throw new BadRequestException("O person não pode ser nulo");

        Person entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + person.getKey() + " not found."));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + id + " not found."));

        repository.delete(entity);

    }

}
