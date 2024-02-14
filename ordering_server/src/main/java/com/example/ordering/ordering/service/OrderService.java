package com.example.ordering.ordering.service;

import com.example.ordering.item.domain.Item;
import com.example.ordering.item.repository.ItemRepository;
import com.example.ordering.member.domain.Member;
import com.example.ordering.member.repository.MemberRepository;
import com.example.ordering.orderItem.domain.OrderItem;
import com.example.ordering.ordering.domain.OrderStatus;
import com.example.ordering.ordering.domain.Ordering;
import com.example.ordering.ordering.dto.OrderReqDto;
import com.example.ordering.ordering.dto.OrderResDto;
import com.example.ordering.ordering.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderService {

    public final OrderRepository orderRepo;
    public final MemberRepository memberRepo;
    public final ItemRepository itemRepo;
    @Autowired
    public OrderService(OrderRepository orderRepo,
                        MemberRepository memberRepo,
                        ItemRepository itemRepo) {
        this.orderRepo = orderRepo;
        this.memberRepo = memberRepo;
        this.itemRepo = itemRepo;
    }

    public Ordering createOrder(List<OrderReqDto> dots){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getName();

        log.info("1. 이메일 출력" + email);


        Member member = memberRepo.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("not Found email"));
        log.info("2. 맴버 출력" + member.toString());

        Ordering ordering = new Ordering(member);
        log.info("3. Order 출력" + ordering.toString());

        for(OrderReqDto dto : dots){
            Item item = itemRepo.findById(dto.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));
            log.info("4. ITEM 출력" + item);

            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .ordering(ordering)
                    .quantity(dto.getQuantity())
                    .build();
            log.info("5. OrderItem 출력" + orderItem);

            ordering.getOrderItems().add(orderItem);

            int banlance = item.getStockQuantity() - dto.getQuantity();
            log.info("6. 재고 출력" + banlance);

            if(banlance < 0) throw new IllegalArgumentException("재고 부족 합니다");
            orderItem.getItem().updateStockQuantity(banlance);
        }

        log.info("7. 포문탈출" );
        return orderRepo.save(ordering);
    }
    public Ordering cancelOrder(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Ordering ordering = orderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if(!ordering.getMember().getEmail().equals(email)
                && !authentication.getAuthorities()
                .contains(
                                new SimpleGrantedAuthority("ROLE_ADMIN")
                        )
        )
            throw new AccessDeniedException("권한이 없습니다.");

        if(ordering.getOrderStatus() == OrderStatus.CANCELED)
            throw new IllegalStateException("이미 취소된 주문입니다.");

        ordering.cancleOrder();
        for(OrderItem orderItem : ordering.getOrderItems())
            orderItem.getItem().updateStockQuantity(
                    orderItem.getQuantity() + orderItem.getItem().getStockQuantity());

        return ordering;
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
    public List<OrderResDto> findMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return member.getOrderings().stream()
                .map(OrderResDto::new)
                .collect(Collectors.toList());
    }
}