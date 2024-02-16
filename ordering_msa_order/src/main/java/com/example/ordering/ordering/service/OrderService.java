package com.example.ordering.ordering.service;

import com.example.ordering.ordering.domain.OrderStatus;
import com.example.ordering.ordering.domain.Ordering;
import com.example.ordering.ordering.dto.MemberDto;
import com.example.ordering.ordering.dto.OrderReqDto;
import com.example.ordering.ordering.dto.OrderResDto;
import com.example.ordering.ordering.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderService {

    private final String MEMBER_API = "http://member-service/";
    private final String ITEM_API = "http://item-service/";
    public final OrderRepository orderRepo;
    private final RestTemplate restTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepo, RestTemplate restTemplate) {
        this.orderRepo = orderRepo;
        this.restTemplate = restTemplate;
    }

    public Ordering createOrder(List<OrderReqDto> dots, String email){
        String url = MEMBER_API + "member/findByEmail?" + email;
        MemberDto members = restTemplate.getForObject(url, MemberDto.class);
        Ordering ordering = new Ordering(members.getId());

        orderRepo.save(ordering);

        for(OrderReqDto dto : dots){

            String url = MEMBER_API + "item/" + dot.;
            MemberDto members = restTemplate.getForObject(url, MemberDto.class);

            Item item = itemRepo.findById(dto.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));
            OrderStatus.OrderItem orderItem = OrderStatus.OrderItem.builder()
                    .item(item)
                    .ordering(ordering)
                    .quantity(dto.getQuantity())
                    .build();

            ordering.getOrderItems().add(orderItem);
            int banlance = item.getStockQuantity() - dto.getQuantity();

            if(banlance < 0) throw new IllegalArgumentException("재고 부족 합니다");
            orderItem.getItem().updateStockQuantity(banlance);
        }
        return orderRepo.save(ordering);



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