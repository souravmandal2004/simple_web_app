package com.example.simple_web_app.simple_web_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.simple_web_app.simple_web_app.model.Product;
import com.example.simple_web_app.simple_web_app.service.ProductService;


@RestController
@CrossOrigin
@RequestMapping ("/api")
public class ProductController {

    @Autowired
    ProductService productService;
    

    @GetMapping ("/products")
    public ResponseEntity <List<Product>> getAllProducts () {
        return new ResponseEntity <> (productService.getAllProducts (), HttpStatus.OK);
    }
    
    @GetMapping ("/product/{id}")
    public ResponseEntity <?> getProduct (@PathVariable int id) {
        // check if the product is present or not
        Product product = productService.getProductById(id);

        if (product != null) 
            return new ResponseEntity <> (product, HttpStatus.OK);
        else
            return new ResponseEntity <> ("Product is not present with id: " + id,HttpStatus.NOT_FOUND);
    }


    @PostMapping ("/product")
    public ResponseEntity<?> addProduct (@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        try {
            Product addedProduct = productService.addProduct (product, imageFile);
            return new ResponseEntity <> (addedProduct, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity <> (e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping ("/product/{id}/image")
    public ResponseEntity <byte[]> getImageByProductId (@PathVariable int id) {
        Product product = productService.getProductById(id);

        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
    }

    @PutMapping ("/product/{id}")
    public ResponseEntity<?> updateProduct (@PathVariable int id, @RequestPart Product product, @RequestPart MultipartFile imageFile) {
        Product existingProduct;
        try {
            existingProduct = productService.updateProduct (id, product, imageFile);
        } catch (IOException e) {
            return new ResponseEntity <> ("Failed to update Product", HttpStatus.BAD_REQUEST);
        }

        if (existingProduct != null) 
            return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        else
            return new ResponseEntity <> ("Failed to update Product", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping ("/product/{id}")
    public ResponseEntity<?> deleteProduct (@PathVariable int id) {
        Product existingProduct = productService.getProductById(id);

        if (existingProduct != null) {
            productService.deleteProduct (id);
            return new ResponseEntity <> ("Product deleted successfully!", HttpStatus.OK);
        }
        else {
            return new ResponseEntity <> ("Product not found!", HttpStatus.NOT_FOUND);
        }
    }
    
}
