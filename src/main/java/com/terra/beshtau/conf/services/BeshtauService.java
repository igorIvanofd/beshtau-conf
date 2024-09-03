package com.terra.beshtau.conf.services;

import com.terra.beshtau.conf.data.BeshtauEntity;
import com.terra.beshtau.conf.data.BeshtauRepository;
import com.terra.beshtau.conf.data.SamplePersonRepository;
import com.terra.beshtau.conf.data.SamplePerson;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BeshtauService {

    private final BeshtauRepository repository;

    public BeshtauService(BeshtauRepository repository) {
        this.repository = repository;
    }

    public Optional<BeshtauEntity> get(Long id) {
        return repository.findById(id);
    }

    public BeshtauEntity update(BeshtauEntity entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<BeshtauEntity> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<BeshtauEntity> list(Pageable pageable, Specification<BeshtauEntity> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
