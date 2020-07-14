package Server.service;

import Server.model.Consumer;
import Server.model.Product;
import Server.model.Purchase;

import java.util.List;

public interface PurchaseService {
    void create(Purchase purchase);
    List<Purchase> readAll();
   // List<Product> readProducts(int id);
   // List<Consumer> readConsumers(int id);
    Purchase read(int id);
    boolean update(Purchase purchase, int id);
    boolean addProducts(int id,  List<Product> products);
    boolean addConsumers(int id, List<Consumer> consumers);
    boolean delete(int id);
}
