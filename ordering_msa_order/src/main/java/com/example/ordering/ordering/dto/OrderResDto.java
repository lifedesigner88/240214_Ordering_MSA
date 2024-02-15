package com.example.ordering.ordering.dto;

import com.example.ordering.orderItem.domain.OrderItem;
import com.example.ordering.ordering.domain.Ordering;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResDto {

    private Long orderId;
    private String memberEmail;
    private String orderStatus;
    private List<OrderResItemDto> orderItems;

    @Data
    public static class OrderResItemDto {
        private Long itemId;
        private String itemName;
        private int quantity;
    }


    public OrderResDto (Ordering ordering){
        this.setOrderId(ordering.getId());
        this.setMemberEmail(ordering.getMember().getEmail());
        this.setOrderStatus(ordering.getOrderStatus().toString());

        List<OrderResItemDto> orderItems = new ArrayList<>();

        for (OrderItem orderItem : ordering.getOrderItems()) {
            OrderResItemDto orderResItemDto = new OrderResItemDto();
            orderResItemDto.setItemId(orderItem.getId());
            orderResItemDto.setItemName(orderItem.getItem().getName());
            orderResItemDto.setQuantity(orderItem.getQuantity());
            orderItems.add(orderResItemDto);
        }
        this.setOrderItems(orderItems);
    }
}
