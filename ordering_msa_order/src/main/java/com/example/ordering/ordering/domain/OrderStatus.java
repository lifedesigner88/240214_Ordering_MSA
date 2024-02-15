package com.example.ordering.ordering.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

public enum OrderStatus {ORDERED, CANCELED;

    @Entity
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class OrderItem {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(nullable = false)
        private Ordering ordering;

        @JoinColumn(nullable = false)
        private Long item;

        private int quantity;



    }
}
