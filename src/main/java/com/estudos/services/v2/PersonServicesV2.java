package com.estudos.services.v2;

import com.estudos.data.mapper.DozerMapper;
import com.estudos.data.mapper.custom.PersonMapper;
import com.estudos.data.model.Person;
import com.estudos.data.v1.PersonVO;
import com.estudos.data.v2.PersonVOV2;
import com.estudos.repository.PersonRepository;
import com.estudos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServicesV2 {
    private Logger logger = Logger.getLogger(PersonServicesV2.class.getName());

    @Autowired
    PersonRepository repository;
    @Autowired
    PersonMapper mapper;

    public PersonVOV2 createV2(PersonVOV2 person){
        logger.info("Creating one person with V2!");

        var entity = mapper.convertVoToEntity(person);
        return mapper.convertEntityToVO(repository.save(entity));
    }

}
