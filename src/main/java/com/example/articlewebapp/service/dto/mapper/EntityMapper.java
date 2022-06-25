package com.example.articlewebapp.service.dto.mapper;


import java.util.List;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 *
 * @author Youssef Agagg
 */

public interface EntityMapper<D, E> {
    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntities(List<D> dtoList);

    List<D> toDTOs(List<E> entityList);


}