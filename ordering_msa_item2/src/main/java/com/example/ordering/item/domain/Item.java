package com.example.ordering.item.domain;

import com.example.ordering.item.dto.ItemReqDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;

    private int price;
    private int stockQuantity;

    @Setter
    private String imagePath;
    private String delYn = "N";

    public void delete() {this.delYn = "Y";}
    public void updateStockQuantity(int quantity) {
        this.stockQuantity += quantity;
    }

    public Item(ItemReqDto itemReqDto) {dtoToItem(itemReqDto);}
    public void update(ItemReqDto itemReqDto){dtoToItem(itemReqDto);}
    public void dtoToItem(ItemReqDto dto){
        this.name = dto.getName();
        this.category = dto.getCategory();
        this.price = dto.getPrice();
        this.stockQuantity = dto.getStockQuantity();
    }


    //    Time
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;
}
