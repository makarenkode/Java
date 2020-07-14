package Server.service;

import Server.model.Consumer;
import Server.repository.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    //private static final Map<Integer, Consumer> CONSUMER_REPOSITORY_MAP = new HashMap<>();

    // Переменная для генерации ID клиента
    //private static final AtomicInteger CONSUMER_ID_HOLDER = new AtomicInteger();
    @Autowired
    private ConsumerRepository consumerRepository;
    @Override
    public void create(Consumer consumer) {
//        final int consumerId = CONSUMER_ID_HOLDER.incrementAndGet();
//        consumer.setId(consumerId);
//        CONSUMER_REPOSITORY_MAP.put(consumerId, consumer);
        consumerRepository.save(consumer);

    }

    @Override
    public List<Consumer> readAll() {
//        return new ArrayList<>(CONSUMER_REPOSITORY_MAP.values());
        return consumerRepository.findAll();
    }

    @Override
    public Consumer read(int id) {
        if(consumerRepository.existsById(id)) {
            return consumerRepository.getOne(id);
        }
        return null;
    }

    @Override
    public boolean update(Consumer consumer, int id) {
//        if (CONSUMER_REPOSITORY_MAP.containsKey(id)) {
//            consumer.setId(id);
//            CONSUMER_REPOSITORY_MAP.put(id, consumer);
//            return true;
//        }
//
//        return false;
        if(consumerRepository.existsById(id)){
            consumer.setId(id);
            consumerRepository.save(consumer);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        if(consumerRepository.existsById(id)){
            consumerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
