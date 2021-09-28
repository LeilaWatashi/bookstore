package com.watashi.bookstore.exception.helper;


import com.watashi.bookstore.dto.EntityDTO;
import com.watashi.bookstore.entity.Entity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.Type;

public class ModelMapperHelper {

    private static ModelMapper modelMapper = new ModelMapper();

    private ModelMapperHelper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    public static <T extends Entity, R extends EntityDTO> R fromEntityToDTO(T entity, Type type) {
        R mapped = modelMapper.map(entity, type);
        reloadMapper();

        return mapped;
    }

    private static void reloadMapper() {
        modelMapper = new ModelMapper();
    }

    public static <T extends EntityDTO, R extends Entity> R fromDTOToEntity(T dto, Type type) {
        R mapped = modelMapper.map(dto, type);
        reloadMapper();

        return mapped;
    }

    public static <S extends EntityDTO, D extends Entity> TypeMap<S, D> configureTypeMapWithDTOSource(Class<S> sourceType, Class<D> destinationType) {
        return modelMapper.typeMap(sourceType, destinationType);
    }

    public static <S extends Entity, D extends EntityDTO> TypeMap<S, D> configureTypeMapWithEntitySource(Class<S> sourceType, Class<D> destinationType) {
        return modelMapper.typeMap(sourceType, destinationType);
    }

    public static <S extends EntityDTO, D extends Entity > void addEntityFieldsToSkip(PropertyMap<S, D> skipEntityFieldsMap) {
        modelMapper.addMappings(skipEntityFieldsMap);
    }

    public static <S extends Entity, D extends EntityDTO> void addDTOFieldsToSkip(PropertyMap<S, D> skipDTOFieldsMap) {
        modelMapper.addMappings(skipDTOFieldsMap);
    }
}
