package com.example.ordering.ordering.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@NoArgsConstructor
public class Ordering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDERED;


//    Relation
    @JoinColumn(nullable = false)
    private Long memberId;

    @ToString.Exclude
    @OneToMany(mappedBy = "ordering", cascade = CascadeType.PERSIST)
    private final List<OrderStatus.OrderItem> orderItems = new ArrayList<>();


//    Funtcion
    public Ordering(Long memberId) {this.memberId = memberId;}
    public void cancleOrder(){this.orderStatus = OrderStatus.CANCELED;}


//    Time
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;

}
