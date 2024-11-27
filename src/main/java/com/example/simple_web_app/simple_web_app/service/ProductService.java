package com.example.simple_web_app.simple_web_app.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.simple_web_app.simple_web_app.model.Product;
import com.example.simple_web_app.simple_web_app.repository.ProductRepository;


@Service
public class ProductService {
    
    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProducts () {
        return productRepository.findAll ();
    }

    public Product getProductById (int id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product addProduct (Product product, MultipartFile imagFile) throws IOException {
        product.setImageName(imagFile.getOriginalFilename());
        product.setImageType(imagFile.getContentType());
        product.setImageData(imagFile.getBytes());

        return productRepository.save (product);
    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {

        product.setImageData(imageFile.getBytes());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        

        return productRepository.save(product);
        
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
