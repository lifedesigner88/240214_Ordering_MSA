package com.example.ordering.ordering.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    private String name;
    private String category;

    private int price;
    private int stockQuantity;
    private String imagePath;

}
