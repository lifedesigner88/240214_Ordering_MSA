package com.example.ordering.ordering.service;

import com.example.ordering.ordering.domain.Ordering;
import com.example.ordering.ordering.dto.OrderReqDto;
import com.example.ordering.ordering.dto.OrderResDto;
import com.example.ordering.ordering.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderService {

    public final OrderRepository orderRepo;
    @Autowired
    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Ordering createOrder(List<OrderReqDto> dots){
//
//
//
//        Member member = memberRepo.findByEmail(email)
//                .orElseThrow(()-> new EntityNotFoundException("not Found email"));
//
//        Ordering ordering = new Ordering(member);
//
//        for(OrderReqDto dto : dots){
//            Item item = itemRepo.findById(dto.getItemId())
//                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));
//            OrderStatus.OrderItem orderItem = OrderStatus.OrderItem.builder()
//                    .item(item)
//                    .ordering(ordering)
//                    .quantity(dto.getQuantity())
//                    .build();
//
//            ordering.getOrderItems().add(orderItem);
//            int banlance = item.getStockQuantity() - dto.getQuantity();
//
//            if(banlance < 0) throw new IllegalArgumentException("재고 부족 합니다");
//            orderItem.getItem().updateStockQuantity(banlance);
//        }
//        return orderRepo.save(ordering);
        return null;
    }
    public Ordering cancelOrder(Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        Ordering ordering = orderRepo.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
//
//        if(!ordering.getMember().getEmail().equals(email)
//                && !authentication.getAuthorities()
//                .contains(
//                                new SimpleGrantedAuthority("ROLE_ADMIN")
//                        )
//        )
//            throw new AccessDeniedException("권한이 없습니다.");
//
//        if(ordering.getOrderStatus() == OrderStatus.CANCELED)
//            throw new IllegalStateException("이미 취소된 주문입니다.");
//
//        ordering.cancleOrder();
//        for(OrderStatus.OrderItem orderItem : ordering.getOrderItems())
//            orderItem.getItem().updateStockQuantity(
//                    orderItem.getQuantity() + orderItem.getItem().getStockQuantity());
//
//        return ordering;
        return null;
    }

    public List<OrderResDto> orderList() {
        List<Ordering> orderings = orderRepo.findAll();
        return orderings.stream()
                .map(OrderResDto::new)
                .collect(Collectors.toList());
    }


    public List<OrderResDto> findByMemberId(Long id) {
//        Member member = memberRepo.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        List<Ordering> orderings = orderRepo.findByMemberId(id);
        return orderings.stream()
                .map(OrderResDto::new)
                .collect(Collectors.toList());
    }


    public List<OrderResDto> findMyOrders(Long memberId) {

        List<Ordering> orderings = orderRepo.findByMemberId(memberId);

        return orderings.stream()
                .map(OrderResDto::new)
                .collect(Collectors.toList());
    }
}