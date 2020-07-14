package Server.service;

import Server.model.Product;

import java.util.List;

public interface ProductService {
    void create(Product product);
    List<Product> readAll();
    Product read(int id);
    boolean update(Product product, int id);
    boolean delete(int id);
}
