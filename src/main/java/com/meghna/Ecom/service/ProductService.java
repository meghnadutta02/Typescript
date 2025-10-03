package com.meghna.Ecom.service;

import com.meghna.Ecom.model.Product;
import com.meghna.Ecom.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    @Autowired
    ProductRepo repo;
    public List<Product> getAllProducts(){
        return repo.findAll();
    }
    public Product addProduct(Product p, MultipartFile image) throws IOException {
        p.setImageName(image.getOriginalFilename());
        p.setImageType(image.getContentType());
        p.setImageData(image.getBytes());
        return repo.save(p);
    }
    public Product getProduct(int id){
        return repo.findById(id).orElse(null);
    }
    public Product getProduct1(int id){
        Optional<Product> pro=repo.findById(id);
        if(pro.isEmpty())
            throw new IllegalStateException("Product not found");
        return pro.get();
    }
    public Product updateProduct(Product p, int id,MultipartFile image) throws IOException {
        Product p1=repo.findById(id).orElse(null);
        if(p1!=null) {
            p1.setName(p.getName());
            p1.setPrice(p.getPrice());
            p1.setBrand(p.getBrand());
            p1.setCategory(p.getCategory());
            p1.setDescription(p.getDescription());
            p1.setAvailable(p.isAvailable());
            p1.setReleaseDate(p.getReleaseDate());
            p1.setQuantity(p.getQuantity());
            p1.setImageType(image.getContentType());
            p1.setImageName(image.getOriginalFilename());
            p1.setImageData(image.getBytes());
            repo.save(p1);
        };
        return p1;
    }
    public boolean deleteProduct(int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
