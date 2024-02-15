package com.example.ordering.item.dto;


import com.example.ordering.item.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResDto {

    private Long id;

    private String name;
    private String category;

    private int price;
    private int stockQuantity;
    private String imagePath;

    public ItemResDto(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.stockQuantity = item.getStockQuantity();
        this.imagePath = item.getImagePath();
    }
}
