package com.ing.broker;

import com.sun.net.httpserver.Authenticator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(
            value = "/new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@Valid @RequestBody OrderDTO orderDTO) {
        Order order = orderService.create(orderDTO);
        // TODO add global exception and advice controller
        return order != null ?
                new ResponseEntity<Authenticator.Success>(HttpStatus.CREATED) :
                new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        boolean deleted = orderService.delete(id);
        return ResponseEntity.ok(deleted);
    }
}
