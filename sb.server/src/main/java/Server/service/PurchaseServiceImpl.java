package Server.service;

import Server.model.Consumer;
import Server.model.Product;
import Server.model.Purchase;
import Server.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PurchaseServiceImpl implements  PurchaseService {
//    private static final Map<Integer, Purchase> PURCHASE_REPOSITORY_MAP = new HashMap<>();
//
//    // Переменная для генерации ID клиента
//    private static final AtomicInteger PURCHASE_ID_HOLDER = new AtomicInteger();

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public void create(Purchase purchase) {
//        final int purchaseId = PURCHASE_ID_HOLDER.incrementAndGet();
//        purchase.setId(purchaseId);
//        PURCHASE_REPOSITORY_MAP.put(purchaseId, purchase);
        if(purchase.getProducts() != null) {
            for (Iterator<Product> productIterator = purchase.getProducts().iterator(); productIterator.hasNext(); ) {
                productIterator.next().setPurchase(purchase);
            }
        }
        if(purchase.getConsumers() != null) {
            for (Iterator<Consumer> consumerIterator = purchase.getConsumers().iterator(); consumerIterator.hasNext(); ) {
                consumerIterator.next().setPurchase(purchase);
            }
        }
        purchaseRepository.save(purchase);
        //System.out.println(purchase.getProducts());

    }

    @Override
   public List<Purchase> readAll() {
//        return new ArrayList<>(PURCHASE_REPOSITORY_MAP.values());
      return   purchaseRepository.findAll();
   }

    @Override
    public Purchase read(int id) {
 //       return PURCHASE_REPOSITORY_MAP.get(id);
        if (purchaseRepository.existsById(id)){
      return   purchaseRepository.getOne(id);}
        return  null;
    }

    @Override
    public boolean update(Purchase purchase, int id) {
//        if (PURCHASE_REPOSITORY_MAP.containsKey(id)) {
//            purchase.setId(id);
//            PURCHASE_REPOSITORY_MAP.put(id, purchase);
//            return true;
//        }
        if(purchaseRepository.existsById(id)){
            purchase.setId(id);
            purchaseRepository.save(purchase);
            return true;
        }

        return false;
    }
    public boolean addProducts( int id,  List<Product> products){
//        if (PURCHASE_REPOSITORY_MAP.containsKey(id)) {
//            Purchase purchase;
//            purchase = PURCHASE_REPOSITORY_MAP.get(id);
//            purchase.setProducts(products);
//           // purchase.setId(id);
//           // purchase.setProducts(products);
//            PURCHASE_REPOSITORY_MAP.put(id, purchase);
//            return true;
//        }
        if(purchaseRepository.existsById(id)){
            Purchase purchase;
            purchase = purchaseRepository.getOne(id);
            //System.out.println(purchase.getName());
            purchase.getProducts().clear();
            purchase.getProducts().addAll(products);
            for (Iterator<Product>productIterator = purchase.getProducts().iterator(); productIterator.hasNext();){
                productIterator.next().setPurchase(purchase);
            }
            //purchase.setProducts(products);
            purchaseRepository.save(purchase);
            return true;
        }

        return false;
    }
    public boolean addConsumers( int id, List<Consumer> consumers){
//        if (PURCHASE_REPOSITORY_MAP.containsKey(id)) {
//            Purchase purchase;
//            purchase = PURCHASE_REPOSITORY_MAP.get(id);
//            //purchase.setId(id);
//            purchase.setConsumers(consumers);
//            PURCHASE_REPOSITORY_MAP.put(id, purchase);
//            return true;
//        }
        if (purchaseRepository.existsById(id)){
            Purchase purchase;
            purchase = purchaseRepository.getOne(id);
            //purchase.setConsumers(consumers);
            purchase.getConsumers().clear();
            purchase.getConsumers().addAll(consumers);
            for (Iterator<Consumer>consumerIterator = purchase.getConsumers().iterator(); consumerIterator.hasNext();){
                consumerIterator.next().setPurchase(purchase);
            }
            purchaseRepository.save(purchase);
            return true;
        }

        return false;
    }



    @Override
    public boolean delete(int id) {
      //  return PURCHASE_REPOSITORY_MAP.remove(id) != null;
        if (purchaseRepository.existsById(id)){
            purchaseRepository.deleteById(id);
            return true;
        }
        return  false;
    }
}
