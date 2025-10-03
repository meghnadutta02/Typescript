package com.meghna.Ecom.controller;

import java.util.Date;
import java.util.List;
import com.meghna.Ecom.model.Product;
import com.meghna.Ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @RequestMapping("/")
    public String greet() {
        return "Hello world!";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping(value = "/products")
    public ResponseEntity<?> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("brand") String brand,
            @RequestParam("price") double price,
            @RequestParam("category") String category,
            @RequestParam("releaseDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date releaseDate,
            @RequestParam("available") boolean available,
            @RequestParam("quantity") int quantity,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {

        try {
            Product p = new Product(name,description,brand,price,category,releaseDate,available,quantity);
            service.addProduct(p, imageFile);

            return new ResponseEntity<>("Product created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get image by id
    @GetMapping("/products/image/{id}")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int id){
        Product p=service.getProduct(id);
        byte[] image=p.getImageData();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(p.getImageType()))
                .body(image);
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product p = service.getProduct(id);
        if (p != null) {
            return new ResponseEntity<>(p, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@RequestParam("name") String name,
                                           @RequestParam("description") String description,
                                           @RequestParam("brand") String brand,
                                           @RequestParam("price") double price,
                                           @RequestParam("category") String category,
                                           @RequestParam("releaseDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date releaseDate,
                                           @RequestParam("available") boolean available,
                                           @RequestParam("quantity") int quantity,
                                           @RequestPart("imageFile") MultipartFile imageFile,@PathVariable int id) {
        try {
            Product p = new Product(name,description,brand,price,category,releaseDate,available,quantity);
            Product updated = service.updateProduct(p, id,imageFile);
            if (updated != null) {
                return new ResponseEntity<>("Product updated successfully",HttpStatus.OK);

            }
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        boolean deleted = service.deleteProduct(id);
        return deleted ?
                new ResponseEntity<>("Product deleted successfully",HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products=service.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
