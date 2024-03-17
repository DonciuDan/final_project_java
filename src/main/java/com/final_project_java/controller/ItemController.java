package com.final_project_java.controller;

import com.final_project_java.exception.ResourceNotFoundException;
import com.final_project_java.model.Item;
import com.final_project_java.service.ItemService;
import com.final_project_java.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController //@RestController VS @Controller -> Spring rest api
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/items")
public class ItemController {
    //@RestController = @Controller + @ResponseBody
    //@RestController ofera suport pentru RestApi
    //Controller-ul e responsabil pentru a gestiona requesturile de HTTP care vin de la client(Aplicatia web) si de a returna un raspuns HTTP inapoi catre client
    //comunicarea intre frontend si backend e realizata prin protocolul HTTP de tipul request | response

    //dependency injection using annotation @RequiredArgsConstructor(DI through constructor)
    private final ItemService itemService;

    //Implement HTTP rest apis(HTTP verbs):GET(@GetMapping) , POST(@PostMapping) , PUT(@PutMapping) , DELETE(@DeleteMapping)


    //GET endpoint -> http://localhost:8081/api/items
    @GetMapping
    public ResponseEntity<ApiResponse> getAllItems() {
        List<Item> itemsList = itemService.readAllItems();
        if (itemsList.isEmpty()) {
            throw new ResourceNotFoundException("No items found in DB");
        }
        return ResponseEntity.ok(ApiResponse.success("All items list",itemsList));
    }

    @GetMapping("/itemById/{id}") //http://localhost:8081/api/items/itemById/id
    public ResponseEntity<ApiResponse> getItemById(@PathVariable Long id) {
        Optional<Item> itemById = itemService.getItemById(id);
        itemById.orElseThrow(() ->
                new ResourceNotFoundException("Item with id: " + id + " doesn't exist in DB"));
        return ResponseEntity.ok(ApiResponse.success("Item by id",itemById.get())); //pui get cand e optional
    }

    @GetMapping("/itemsByName/{name}")
    public ResponseEntity<ApiResponse> getAllItemsByName(@PathVariable String name) {
        List<Item> items = itemService.getAllItemsByName(name);
        if (items.isEmpty()) {
            throw new ResourceNotFoundException("No items found width: " + name + " in DB");
        }

        return ResponseEntity.ok(ApiResponse.success("All items by name",items));
    }

    @GetMapping("/itemsByCategory/{category}")
    public ResponseEntity<ApiResponse> getAllItemsByCategory(@PathVariable String category) {
        List<Item> itemsByCategory = itemService.getAllItemsByCategory(category);
        if (itemsByCategory.isEmpty()) {
            throw new ResourceNotFoundException("No items found width: " + category + " in DB");
        }
        return ResponseEntity.ok(ApiResponse.success("All items by Category",itemsByCategory));
    }


    //POST -> CREATE
    @PostMapping("/addNewItem") //http://localhost:8081/api/items/addNewitem
    public ResponseEntity<ApiResponse> saveItem(@RequestBody Item item) {
        Item newItem = itemService.saveItem(item);  //varianta 2
        return ResponseEntity.ok(ApiResponse.success("Item saved",newItem));
    }

    @PutMapping("/updateItem")  //http://localhost:8081/api/items/updateItem
    public ResponseEntity<ApiResponse> updateItem(@RequestBody Item item) {
        if (item.getId() == null){
            throw new ResourceNotFoundException("Item id is not valid");
        }
        Optional<Item> itemOptional = itemService.getItemById(item.getId());
        itemOptional.orElseThrow(()->
                new ResourceNotFoundException("The item id: " + item.getId() + " doesn't exist in DB"));

        return ResponseEntity.ok(ApiResponse.success("Item updated",itemService.updateItem(item)));
    }

    @DeleteMapping("/deleteItem/{id}")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable Long id) {
        Optional<Item> itemOptional = itemService.getItemById(id);
        itemOptional.orElseThrow(() ->
                new ResourceNotFoundException("Item with id: " + id + " doesn't exist in DB"));
//        return ResponseEntity.status(HttpStatus.OK)
//                .body("Item with id: " + id + " deleted successfully");  //Varianta 1
        itemService.deleteItemById(id);
        return ResponseEntity.ok(ApiResponse.success("Item deleted",null)); // varianta 2

    }
//    @ExceptionHandler(ResourceNotFoundException.class) //se foloseste pentru exceptii care sunt aruncate de controlere (trigger)
//    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
}
