package com.ecommerce.project.controller;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProducts(@PathVariable Long categoryId,
                                                  @RequestBody ProductDTO productDTO){
        ProductDTO _productDTO = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(_productDTO,HttpStatus.CREATED);
    }
}
