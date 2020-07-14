package Server.service;

import Server.model.Consumer;

import java.util.List;

public interface ConsumerService {
    void create(Consumer consumer);
    List<Consumer> readAll();
    Consumer read(int id);
    boolean update(Consumer consumer, int id);
    boolean delete(int id);
}
