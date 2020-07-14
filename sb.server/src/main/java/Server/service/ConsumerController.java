package Server.service;

import Server.model.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConsumerController {
    private final ConsumerService consumerService;

    @Autowired
    public ConsumerController(ConsumerService consumerService){
        this.consumerService = consumerService;
    }

    @PostMapping(value = "/consumers")
    public ResponseEntity<?> create (@RequestBody Consumer consumer){
        consumerService.create(consumer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/consumers")
    public ResponseEntity<List<Consumer>> read() {
        final List<Consumer> consumers = consumerService.readAll();

        return consumers != null && !consumers.isEmpty()
                ? new ResponseEntity<>(consumers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/consumers/{id}")
    public ResponseEntity<Consumer> read(@PathVariable(name = "id")int id){
        Consumer consumer = consumerService.read(id);
        return  consumer != null
                ? new ResponseEntity<>(consumer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping(value = "/consumers/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id")int id, @RequestBody Consumer consumer){
        final boolean updated = consumerService.update(consumer, id);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    @DeleteMapping(value = "/consumers/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id")int id){
        final boolean deleted = consumerService.delete(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    
}
