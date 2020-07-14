package Server.service;

import Server.model.Product;
import Server.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    // Хранилище клиентов
   // private static final Map<Integer, Product> PRODUCT_REPOSITORY_MAP = new HashMap<>();

    // Переменная для генерации ID клиента
   // private static final AtomicInteger PRODUCT_ID_HOLDER = new AtomicInteger();
    @Autowired
    private ProductRepository productRepository;
    @Override
    public void create(Product product) {
//        final int productId = PRODUCT_ID_HOLDER.incrementAndGet();
//        product.setId(productId);
//        PRODUCT_REPOSITORY_MAP.put(productId, product);
          productRepository.save(product);
    }

    @Override
    public List<Product> readAll() {
//        return new ArrayList<>(PRODUCT_REPOSITORY_MAP.values());
        return productRepository.findAll();
    }

    @Override
   public Product read(int id) {
//        return PRODUCT_REPOSITORY_MAP.get(id);
        if(productRepository.existsById(id)){
            return productRepository.getOne(id);
        }
        return null;
    }

    @Override
    public boolean update(Product product, int id) {
//        if (PRODUCT_REPOSITORY_MAP.containsKey(id)) {
//            product.setId(id);
//            PRODUCT_REPOSITORY_MAP.put(id, product);
//            return true;
//        }
//
//        return false;
        if(productRepository.existsById(id)){
            //product.setId(id);
            Product old = productRepository.getOne(id);
            old.setAmount(product.getAmount());
            old.setCost(product.getCost());
            old.setName(product.getName());
            productRepository.save(old);
            return true;
        }
        return false;
    }



    @Override
   public boolean delete(int id) {
//        return PRODUCT_REPOSITORY_MAP.remove(id) != null;
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        return false;
   }
}
