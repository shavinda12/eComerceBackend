package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.ProductDto;
import com.ecommercebackend.store.entities.Product;
import com.ecommercebackend.store.mappers.ProductMapper;
import com.ecommercebackend.store.mappers.UserMapper;
import com.ecommercebackend.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public Iterable<ProductDto>getProducts(@RequestParam(required = false,defaultValue = "",name = "sort") String sort,@RequestParam(required = false,name = "cat_id") Byte cat_id){
        List<Product> products;
        if(!Set.of("id","name").contains(sort)){
            sort="id";
        }
        if(cat_id!=null){
            products= productRepository.findAllByCategoryId(cat_id).stream().toList();
        }else{
            products= productRepository.findAllWithCategory().stream().toList();
        }
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable  Long id){
        var product= productRepository.findById(id).orElse(null);
        if(product==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));

    }
}
