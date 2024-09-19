package com.github.supercoding.web.controller;

import com.github.supercoding.repository.ElectronicStoreItemRepository;
import com.github.supercoding.repository.ItemEntity;
import com.github.supercoding.web.dto.Item;
import com.github.supercoding.web.dto.ItemBody;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ElectronicStoreController {

    private ElectronicStoreItemRepository electronicStoreItemRepository;

    public ElectronicStoreController(ElectronicStoreItemRepository electronicStoreItemRepository) {
        this.electronicStoreItemRepository = electronicStoreItemRepository;
    }

    private static int serialItemId = 1;
    private List<Item> items = Arrays.asList(
            new Item(String.valueOf(serialItemId++), "Apple iPhone 12 Pro Max", "Smartphone", 1490000, "A14 Bionic", "512GB"),
            new Item(String.valueOf(serialItemId++), "Samsung Galaxy S21 Ultra", "Smartphone", 1690000, "Exynos 2100", "256GB"),
            new Item(String.valueOf(serialItemId++), "Google Pixel 6 Pro", "Smartphone", 1290000, "Google Tensor", "128GB"),
            new Item(String.valueOf(serialItemId++), "Dell XPS 15", "Laptop", 2290000, "Intel Core i9", "1TB SSD"),
            new Item(String.valueOf(serialItemId++), "Sony Alpha 7 III", "Mirrorless Camera", 2590000, "BIONZ X", "No internal storage"),
            new Item(String.valueOf(serialItemId++), "Microsoft Xbox Series X", "Gaming Console", 499000, "Custom AMD Zen 2", "1TB SSD"));

    @GetMapping("/items")
    public List<Item> findAllItem(){
        List<ItemEntity> itemEntities = electronicStoreItemRepository.findAllItems();
        return itemEntities.stream().map(Item::new).collect(Collectors.toList());
    }

    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody){
        ItemEntity itemEntity = new ItemEntity(null, itemBody.getName(), itemBody.getType(),
                itemBody.getPrice(), itemBody.getSpec().getCpu(), itemBody.getSpec().getCapacity());

        Integer itemId = electronicStoreItemRepository.saveItem(itemEntity);
        return "ID: " + itemId;
    }

    @GetMapping("/items/{id}")
    public Item findItemByPathId(@PathVariable String id){
        Item itemFounded = items.stream()
                                .filter((item -> item.getId().equals(id)))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException());
        return itemFounded;
    }

    @GetMapping("/items-query")
    public Item findItemByQueryId(@RequestParam("id") String id){
        Item itemFounded = items.stream()
                .filter((item -> item.getId().equals(id)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
        return itemFounded;
    }

    @GetMapping("/items-queries")
    public List<Item> findItemByQueryIds(@RequestParam("id") List<String> ids){

        Set<String> idSet = ids.stream().collect(Collectors.toSet());

        List<Item> itemsFound = items.stream()
                                    .filter((item -> idSet.contains(item.getId())))
                                    .collect(Collectors.toList());
        return itemsFound;
    }

    @DeleteMapping("/items/{id}")
    public String deleteItemById(@PathVariable String id){
        items = items.stream()
                .filter((item) -> !item.getId().equals(id))
                .collect(Collectors.toList());
        return "Object with id = " + id + " has been deleted";
    }

    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable String id, @RequestBody ItemBody itemBody){

        Integer idInt = Integer.valueOf(id);
        ItemEntity itemEntity = new ItemEntity(idInt, itemBody.getName(), itemBody.getType(),
                itemBody.getPrice(), itemBody.getSpec().getCpu(), itemBody.getSpec().getCapacity());

        ItemEntity itemEntityUpdated = electronicStoreItemRepository.updateItemEntity(idInt, itemEntity);

        Item itemUpdated = new Item(itemEntityUpdated);
        return itemUpdated;
    }
}