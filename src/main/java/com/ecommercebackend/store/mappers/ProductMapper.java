package com.ecommercebackend.store.mappers;

import com.ecommercebackend.store.dtos.ProductDto;
import com.ecommercebackend.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target="categoryId",source="category.id")
    ProductDto toDto(Product product);
}
