package com.final_project_java.repository;

import com.final_project_java.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //reprezinta o interfata care gestioneaza entitatea(item) pentru a stoca si manipula date prin CRUD in DB
//o clasa extinde o alta clasa cu extends dar o clasa poate sa implementeze mai multe interfete cu implements
//o interfata poate extinde una sau mai multe interfete cu extends
//Spring data la nivel de repo ne permite sa definim modalitati prin care putem sa executam un qurey de SQL:
// 1. Folosind sintaxa de JPQL prin utilizarea adnotarii @Query
// 2. Query native prin utilizarea adnotarii @Query -> diferenta fiind la Query narive prin setarea atributului boolean "nativeQuery" la valoarea True
public interface ItemRepository extends JpaRepository<Item, Long> { //<Entitstea pe care se executa CRUD , tipul cheii primare>
    // putem sa ne definim operatii CRUD custom
    //find all items by author


    List<Item> searchItemsByName(String name);
    List<Item> searchItemsByCategory(String category);

}
