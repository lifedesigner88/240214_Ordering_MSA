package com.example.ordering.item.controller;


import com.example.ordering.common.CommonResponse;
import com.example.ordering.item.domain.Item;
import com.example.ordering.item.dto.ItemQuantityUpdateDto;
import com.example.ordering.item.dto.ItemReqDto;
import com.example.ordering.item.dto.ItemResDto;
import com.example.ordering.item.dto.ItemSearchDto;
import com.example.ordering.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {

    private final ItemService service;
    public ItemController(@Autowired ItemService service) {
        this.service = service;
    }

//    Create
    @PostMapping("/item/create")
    public ResponseEntity<CommonResponse> itemCreate(ItemReqDto itemReqDto)  {
        return new ResponseEntity<>(
                new CommonResponse(
                        HttpStatus.CREATED,
                        "Item Successfully Created",
                        service.createItem(itemReqDto)
                ),
                HttpStatus.CREATED
        );
    }

//    Read
    @GetMapping("/items")
    public List<ItemResDto> items(ItemSearchDto SerchDto, Pageable pageable){
        return service.SerchItems(SerchDto, pageable);
    }


    @GetMapping("/item/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable
                                             Long id){
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(service.getImage(id));

    }


//    Update
    @PostMapping("/item/{id}/update")
    public ResponseEntity<CommonResponse> itemUpdate(@PathVariable Long id,
                                                     ItemReqDto itemReqDto){

        return ResponseEntity.ok()
                .body(
                        new CommonResponse(
                                HttpStatus.OK,
                                "Item Successfully Created",
                                service.updateItem(id, itemReqDto)
                        )
                );

    }


    @PatchMapping("/item/updateQuantity")
    public ResponseEntity<CommonResponse> findById (Long id){
        ItemResDto item = service.findById(id);

        return ResponseEntity.ok()
                .body(
                        new CommonResponse(
                                HttpStatus.OK,
                                "Item Successfully Created",
                                item
                        )
                );

    }





    @PatchMapping("/item/updateQuantity")
    public ResponseEntity<CommonResponse> itemUpdateQuantitiy (ItemQuantityUpdateDto dto){
        Item item = service.updateQuantitiy(dto);

        return ResponseEntity.ok()
                .body(
                        new CommonResponse(
                                HttpStatus.OK,
                                "Item Successfully Created",
                                item
                        )
                );

    }

//    Delete
    @DeleteMapping("/item/{id}/delete")
    public ResponseEntity<CommonResponse> Delete(@PathVariable
                                                 Long id) {

        return new ResponseEntity<>(
                new CommonResponse(
                        HttpStatus.OK,
                        "Item successfully deleted",
                        service.delete(id)
                ),
                HttpStatus.OK
        );
    }


}
