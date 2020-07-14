package Server.service;

import Server.model.Consumer;
import Server.model.Product;
import Server.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping(value = "/purchases")
    public ResponseEntity<?> create(@RequestBody Purchase purchase) {
        purchaseService.create(purchase);
        System.out.println(purchase.getId());
        return new ResponseEntity<>(purchase.getId(),HttpStatus.CREATED);
    }

    @GetMapping(value = "/purchases")
    public ResponseEntity<List<Purchase>> read() {
        final List<Purchase> purchases = purchaseService.readAll();

        return purchases != null && !purchases.isEmpty()
                ? new ResponseEntity<>(purchases, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/purchases/{id}")
    public ResponseEntity<Purchase> read(@PathVariable(name = "id") int id) {
        Purchase purchase = purchaseService.read(id);
        return purchase != null
                ? new ResponseEntity<>(purchase, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/purchases/{id}/{consumers}")
    public ResponseEntity<List<?>> read(@PathVariable(name = "id") int id, @PathVariable(name = "consumers") String consumer) {
        Purchase purchase = purchaseService.read(id);
        if (consumer.equals("consumers")) {
            List<Consumer> consumers = purchase.getConsumers();
            return consumers != null
                    ? new ResponseEntity<>(consumers, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if (consumer.equals("products")) {
                List<Product> products = purchase.getProducts();
                return products != null
                        ? new ResponseEntity<>(products, HttpStatus.OK)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

    }


    @PutMapping(value = "/purchases/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody Purchase purchase) {
        final boolean updated = purchaseService.update(purchase, id);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping(value = "/purchases/{id}/addProducts")
    public ResponseEntity<?> addProducts(@PathVariable(name = "id") int id, @RequestBody List<Product> products) {
        final boolean updated = purchaseService.addProducts(id, products);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping(value = "/purchases/{id}/addConsumers")
    public ResponseEntity<?> addConsumers(@PathVariable(name = "id") int id, @RequestBody List<Consumer> consumers) {
        final boolean updated = purchaseService.addConsumers(id, consumers);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/purchases/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = purchaseService.delete(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/purchases/{id}/{delete}/{id2}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id, @PathVariable(name = "delete") String del, @PathVariable(name = "id2") int id2) {
        Purchase purchase = purchaseService.read(id);
        ResponseEntity<?> deleted = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(id2 < 1 || id < 1){
            return deleted;
        }
        if (del.equals("deleteConsumer")) {
            List<Consumer> consumers = purchase.getConsumers();
            for(Iterator<Consumer>iter = consumers.iterator(); iter.hasNext();){
                if((int)iter.next().getId() == id2){
                    iter.remove();
                    purchase.setConsumers(consumers);
                    purchaseService.update(purchase, purchase.getId());
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }

//            for(Iterator<Consumer>iter = consumers.iterator(); iter.hasNext();){
//                if((int)iter.next().getId() == id2){
//                    iter.remove();
//                    return new ResponseEntity<>(HttpStatus.OK);
//                }
            //}
            deleted = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if (del.equals("deleteProduct")) {
                List<Product> products = purchase.getProducts();
                for(Iterator<Product>iter = products.iterator(); iter.hasNext();){
                    if((int)iter.next().getId() == id2){
                        iter.remove();
                        purchase.setProducts(products);
                        purchaseService.update(purchase, purchase.getId());
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                }
                deleted = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return  deleted;
    }
}
