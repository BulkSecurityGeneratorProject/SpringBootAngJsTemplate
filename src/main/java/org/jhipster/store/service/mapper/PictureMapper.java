package org.jhipster.store.service.mapper;

import org.jhipster.store.domain.*;
import org.jhipster.store.service.dto.PictureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Picture and its DTO PictureDTO.
 */
@Mapper(componentModel = "spring", uses = {RestaurantMapper.class})
public interface PictureMapper extends EntityMapper<PictureDTO, Picture> {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    PictureDTO toDto(Picture picture); 

    @Mapping(source = "restaurantId", target = "restaurant")
    Picture toEntity(PictureDTO pictureDTO);

    default Picture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Picture picture = new Picture();
        picture.setId(id);
        return picture;
    }
}
