package com.example.ordering.item.dto;

import lombok.Data;

@Data
public class ItemQuantityUpdateDto {
    private Long id;
    private int stockQuantity;
}
