package com.example.ordering.ordering.domain;

import com.example.ordering.member.domain.Member;
import com.example.ordering.orderItem.domain.OrderItem;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ToString.Exclude
    @OneToMany(mappedBy = "ordering", cascade = CascadeType.PERSIST)
    private final List<OrderItem> orderItems = new ArrayList<>();


//    Funtcion
    public Ordering(Member member) {this.member = member;}
    public void cancleOrder(){this.orderStatus = OrderStatus.CANCELED;}


//    Time
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;

}
