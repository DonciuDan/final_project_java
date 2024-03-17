package com.final_project_java.controller;

import com.final_project_java.exception.ResourceNotFoundException;
import com.final_project_java.model.Item;
import com.final_project_java.model.Order;
import com.final_project_java.service.OrderService;
import com.final_project_java.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/orders")
public class OrderController {
    public final OrderService orderService;

    @GetMapping //http://localhost:8081/api/orders
    public ResponseEntity<ApiResponse> getAllOrders() {
        List<Order> ordersList = orderService.getAllOrders();
        if (ordersList.isEmpty()) {
            throw new ResourceNotFoundException("No orders found in DB");
        }
        return ResponseEntity.ok(ApiResponse.success("Order List",ordersList));
    }

    @GetMapping("/orderById/{id}") //http://localhost:8081/api/orders/orderById/{id}
    public ResponseEntity<ApiResponse> getAllOrdersById(@PathVariable Long id) {
        Optional<Order> orderOptional = orderService.getOrderById(id);
        orderOptional.orElseThrow(() ->
                new ResourceNotFoundException("The order with id: " + id + " doesn't exist in DB"));
        return ResponseEntity.ok(ApiResponse.success("Orders by id",orderOptional.get()));
    }

    @GetMapping("/ordersByDate/{date}") //http://localhost:8081/api/orders/ordersByDate/{date}
    public ResponseEntity<ApiResponse> getAllOrdersByDate(@PathVariable LocalDate date) {
        List<Order> ordersByDateList = orderService.getOrdersByDate(date);
        if (ordersByDateList.isEmpty()) {
            throw new ResourceNotFoundException("There are no orders made in " + date);
        }
        return ResponseEntity.ok(ApiResponse.success("Orders by date",ordersByDateList));
    }

    @PostMapping("/addOrder") //http://localhost:8081/api/orders/addOrder
    public ResponseEntity<ApiResponse> saveOrder(@RequestBody Order order) {
        return ResponseEntity.ok(ApiResponse.success("Order saved",orderService.saveOrder(order)));
    }

    @PutMapping("/updateOrder")  //http://localhost:8081/api/orders/updateOrder
    public ResponseEntity<ApiResponse> updateOrder(@RequestBody Order order) {
        if (order.getId() == null){
            throw new ResourceNotFoundException("Item id is not valid");
        }
        Optional<Order> orderOptional = orderService.getOrderById(order.getId());
        orderOptional.orElseThrow(()->
                new ResourceNotFoundException("The item id: " + order.getId() + " doesn't exist in DB"));

        return ResponseEntity.ok(ApiResponse.success("Update Order",orderService.updateOrder(order)));
    }

    @DeleteMapping("/deleteOrderById/{id}") //http://localhost:8081/api/orders/deleteOrderById/{id}
    public ResponseEntity<ApiResponse> deleteOrderById(@PathVariable Long id) {
        Optional<Order> orderOptional = orderService.getOrderById(id);
        orderOptional.orElseThrow(() ->
                new ResourceNotFoundException("The order with id: " + id + " doesn't exist in DB"));
        orderService.deleteOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order with id: " + id + " was deleted successfully",null));
    }

//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
}
