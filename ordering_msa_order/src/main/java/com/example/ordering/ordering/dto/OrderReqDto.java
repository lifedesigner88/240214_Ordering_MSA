package com.example.ordering.ordering.dto;

import lombok.Data;

@Data
public class OrderReqDto {
        private Long itemId;
        private int quantity;
}



//
//@Data
//public class OrderReqDto {
//
//    private List<OrderReqItemDto> orderReqItemDtoList;
//    @Data
//    public static class OrderReqItemDto {
//        private Long itemId;
//        private int quantity;
//    }
//}