package es.upm.etsisi.qhcube.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CRUDAbstractService<E> {

    JpaRepository<E, Long> getRepository();

    default List<E> findAll() {
        return this.getRepository().findAll();
    }

    default List<E> findAll(Sort sort) {
        return this.getRepository().findAll(sort);
    }

    @Transactional
    default void save(E entity) {
        getRepository().save(entity);
    }

    @Transactional
    default void delete(Long id) {
        getRepository().deleteById(id);
    }

}
