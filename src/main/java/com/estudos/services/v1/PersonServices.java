package com.estudos.services.v1;

import com.estudos.data.v1.PersonVO;
import com.estudos.data.mapper.DozerMapper;
import com.estudos.data.model.Person;
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
        return DozerMapper.parseListsObjects(repository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id){
        logger.info("Finding one person!");
        Person person = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + id + " not found."));

        return DozerMapper.parseObject(person, PersonVO.class);
    }

    public PersonVO create(PersonVO person){
        logger.info("Creating one person!");

        var entity = DozerMapper.parseObject(person, Person.class);
        return DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    }

    public PersonVO update(PersonVO person){
        logger.info("Updating one person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + person.getId() + " not found."));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    }

    public void delete(Long id){
        logger.info("Deleting one person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Person id: " + id + " not found."));

        repository.delete(entity);

    }

}
