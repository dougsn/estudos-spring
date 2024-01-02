package com.estudos.services.v1;

import com.estudos.controller.v1.PersonController;
import com.estudos.data.mapper.DozerMapper;
import com.estudos.data.model.Person;
import com.estudos.data.v1.PersonVO;
import com.estudos.repository.PersonRepository;
import com.estudos.services.exceptions.BadRequestException;
import com.estudos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;
    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        logger.info("Finding all person!");

        var personPage = repository.findAll(pageable);

        var personVOSPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
        personVOSPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(personVOSPage, link);
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

    @Transactional
    public PersonVO disablePerson(Long id) {
        logger.info("Disabling one person!");
        repository.disablePerson(id);

        Person person = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + id + " not found."));

        // Colocando o link hateoas para si mesmo.
        var vo = DozerMapper.parseObject(person, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

        return vo;
    }

    public PersonVO create(PersonVO person) {
        logger.info("Creating one person!");
        if (person == null) throw new BadRequestException("The person cannot be null.");

        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updating one person!");
        if (person == null) throw new BadRequestException("The person cannot be null.");

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
