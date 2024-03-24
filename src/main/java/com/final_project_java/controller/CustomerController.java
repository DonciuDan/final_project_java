package com.final_project_java.controller;

import com.final_project_java.exception.ResourceNotFoundException;
import com.final_project_java.model.Customer;
import com.final_project_java.service.CustomerService;
import com.final_project_java.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping //http://localhost:8081/api/customers
    public ResponseEntity<ApiResponse> getAllCustomers() {
        List<Customer> customersList = customerService.getAllCustomers();
//        if (customersList.isEmpty()) {
//            throw new ResourceNotFoundException("The DB doesn't contain any customers");
//        }
//        ApiResponse response = new ApiResponse.Builder()
//                .status(HttpStatus.OK.value())
//                .message("Customer list")
//                .data(customersList)
//                .build();
//        return new ResponseEntity<>(response, HttpStatus.OK);
        return ResponseEntity.ok(ApiResponse.success("Customer List",customersList));
    }

    @GetMapping("/customersById/{id}") //http://localhost:8081/api/customers/customersById/{id}
    public ResponseEntity<ApiResponse> getAllCustomerById(@PathVariable Long id) {
        Optional<Customer> customerById = customerService.getCustomerById(id);
        customerById.orElseThrow(() ->
                new ResourceNotFoundException("The customer with id : " + id + " doesn't exist in DB"));
        return ResponseEntity.ok(ApiResponse.success("Customer by ID",customerById.get()));
    }

    @GetMapping("/customersByName/{name}") //http://localhost:8081/api/customers/customersByName/{name}
    public ResponseEntity<ApiResponse> getAllCustomersByName(@PathVariable String name) {
        List<Customer> customersByName = customerService.getCustomersByName(name);
        if (customersByName.isEmpty()) {
            throw new ResourceNotFoundException("The client with name : " + name + " doesn't exist in DB");
        }
        return ResponseEntity.ok(ApiResponse.success("Customer by name",customersByName));
    }

    @PostMapping("/addNewCustomer") //http://localhost:8081/api/customers/addNewCustomer
    public ResponseEntity<ApiResponse> saveCustomer(@RequestBody Customer customer) {
        Customer customer1 = customerService.saveCustomer(customer);
        return ResponseEntity.ok(ApiResponse.success("Add customer",customer1));
    }

    @PutMapping("/updateCustomer") //http://localhost:8081/api/customers/updateCustomer
    public ResponseEntity<ApiResponse> updateCustomer(@RequestBody Customer customer) {
        if (customer.getId() == null){
            throw new ResourceNotFoundException("Item id is not valid");
        }
        Optional<Customer> customerOptional = customerService.getCustomerById(customer.getId());
        customerOptional.orElseThrow(()->
                new ResourceNotFoundException("The item id: " + customer.getId() + " doesn't exist in DB"));

        return ResponseEntity.ok(ApiResponse.success("Updated customer successful",customerService.updateCustomer(customer)));
    }

    @DeleteMapping("/deleteCustomerById/{id}") //http://localhost:8081/api/customers/deleteCustomerById/{id}
    public ResponseEntity<ApiResponse> deleteCustomerById(@PathVariable Long id) {
        Optional<Customer> customerOptional = customerService.getCustomerById(id);
        customerOptional.orElseThrow(() ->
                new ResourceNotFoundException("The customer with id : " + id + " doesn't exist in DB"));
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer with id: " + id + " deleted successfully",null));
    }


}
