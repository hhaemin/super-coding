package com.github.supercoding.web.controller;

import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.items.BuyOrder;
import com.github.supercoding.web.dto.items.Item;
import com.github.supercoding.web.dto.items.ItemBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ElectronicStoreController {

    // @Slf4j를 하면 다음 코드를 적어줄 필요가 없다
    // 하지만 선언할 때 logger 가 아닌 log로
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ElectronicStoreItemService electronicStoreItemService;

    @Operation(summary = "모든 Item 검색 ")
    @GetMapping("/items")
    public List<Item> findAllItem(){
        log.info("GET / items 요청이 들어왔습니다.");
        List<Item> items =  electronicStoreItemService.findAllItem();
        log.info("GET / items 응답: " + items);
        return items;
    }

    @Operation(summary = "단일 Item 등록 ")
    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody){
        Integer itemId = electronicStoreItemService.saveItem(itemBody);
        return "ID: " + itemId;
    }

    @Operation(summary = "단일 Item id로 검색 ")
    @GetMapping("/items/{id}")
    public Item findItemByPathId(
            @Parameter(name="id", description="item ID", example = "1")
            @PathVariable String id){
        return electronicStoreItemService.findItemByInt(id);
    }

    @Operation(summary = "단일 Item id로 검색 (쿼리문)")
    @GetMapping("/items-query")
    public Item findItemByQueryId(
            @Parameter(name="id", description="item ID", example = "1")
            @RequestParam("id") String id){
        return electronicStoreItemService.findItemByInt(id);
    }

    @Operation(summary = "여러 Item ids로 검색 (쿼리문)")
    @GetMapping("/items-queries")
    public List<Item> findItemByQueryIds(
            @Parameter(name="ids", description="item IDs", example = "1,2,3")
            @RequestParam("id") List<String> ids){
        log.info("/items-queries 요청 ids: " + ids);
        List<Item> items = electronicStoreItemService.findItemsByIds(ids);
        log.info("/items-queries 응답 : " + items);
        return items;
    }

    @Operation(summary = "단일 Item id로 삭제 ")
    @DeleteMapping("/items/{id}")
    public String deleteItemByPathId(@PathVariable String id){
        electronicStoreItemService.deleteItem(id);
        return "Object with id = " + id + " has been deleted";
    }

    @Operation(summary = "단일 Item id로 수정 ")
    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable String id, @RequestBody ItemBody itemBody){
        return electronicStoreItemService.updateItem(id,itemBody);
    }

    @Operation(summary = "단일 Item 구매 ")
    @PostMapping("/items/buy")
    public String buyItem(@RequestBody BuyOrder buyOrder){

        Integer orderItemNums = electronicStoreItemService.buyItems(buyOrder);
        return "요청하신 Item 중 " + orderItemNums + "개를 구매하였습니다.";
    }

    @Operation(summary = "여러 Item types 검색 (쿼리문)")
    @GetMapping("/items-types")
    public List<Item> findItemByTypes(
            @Parameter(name="ids", description="item IDs", example = "1,2,3")
            @RequestParam("type") List<String> types){
        log.info("/items-types 요청 ids: " + types);
        List<Item> items = electronicStoreItemService.findItemsByTypes(types);
        log.info("/items-queries 응답 : " + items);
        return items;
    }

    @Operation(summary = "단일 Item id로 검색 (쿼리문)")
    @GetMapping("/items-prices")
    public List<Item> findItemByPrice(
            @RequestParam("max") Integer maxValue){
        return electronicStoreItemService.findItemsOrderByPrices(maxValue);
    }

    @Operation(summary = "pagination 지원")
    @GetMapping("/items-page")
    public Page<Item> findItemsPagination(Pageable pageable){
        return electronicStoreItemService.findAllWithPagable(pageable);
    }

    @Operation(summary = "pagination 지원 2")
    @GetMapping("/items-types-page")
    public Page<Item> findItemPagination(@RequestParam("type") List<String> types, Pageable pageable){
        return electronicStoreItemService.findAllWithPagable(types, pageable);
    }

//    @Operation("전체 Stores 정보 검색")
//    @GetMapping("/stores")
//    public List<StoreInfo> findAllStores(){
//        return electronicStoreItemService.findAllStoreInfo();
//    }

}
