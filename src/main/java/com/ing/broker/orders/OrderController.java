package com.ing.broker.orders;

import com.sun.net.httpserver.Authenticator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        orderService.create(orderDTO);
        return new ResponseEntity<Authenticator.Success>(HttpStatus.CREATED);
    }

    @PostMapping(
            value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Order>> search(@RequestBody OrderSearchDTO orderSearchDTO) {
        List<Order> orders = orderService.search(orderSearchDTO);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.delete(id));
    }

    @GetMapping(
            value = "/all-pending",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Order>> getAllPendingOrders() {
        return ResponseEntity.ok(this.orderService.getAllPendingOrders());
    }

    @PostMapping(
            value = "/match",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Order>> match(@RequestBody List<OrderMatchDTO> matchList) {
        return ResponseEntity.ok(this.orderService.match(matchList));
    }
}
