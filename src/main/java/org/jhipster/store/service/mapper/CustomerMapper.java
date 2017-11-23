package org.jhipster.store.service.mapper;

import org.jhipster.store.domain.*;
import org.jhipster.store.service.dto.CustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {RestaurantMapper.class})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    CustomerDTO toDto(Customer customer); 

    @Mapping(source = "restaurantId", target = "restaurant")
    Customer toEntity(CustomerDTO customerDTO);

    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
