package com.example.ordering.ordering.service;

import com.example.ordering.common.CommonResponse;
import com.example.ordering.ordering.domain.OrderItem;
import com.example.ordering.ordering.domain.OrderStatus;
import com.example.ordering.ordering.domain.Ordering;
import com.example.ordering.ordering.dto.*;
import com.example.ordering.ordering.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        String url = MEMBER_API + "member/findByEmail?email=" + email;
        MemberDto member = restTemplate.getForObject(url, MemberDto.class);
        Ordering ordering = new Ordering(member.getId());
        orderRepo.save(ordering);

        for(OrderReqDto dto : dots){
            String itemUrl = ITEM_API + "item/" + dto.getItemId();
            ResponseEntity<ItemDto> itemDto = restTemplate.getForEntity(itemUrl, ItemDto.class);

            OrderItem orderItem = OrderItem.builder()
                    .itemId(dto.getItemId())
                    .quantity(dto.getQuantity())
                    .ordering(ordering)
                    .build();
            ordering.getOrderItems().add(orderItem);

            System.out.println(itemDto);
            System.out.println(ordering);

            int newQuantity = itemDto.getBody().getStockQuantity() - dto.getQuantity();
            System.out.println(newQuantity);
            if(newQuantity < 0) throw new IllegalArgumentException("재고 부족 합니다");

            String itemPathUrl = ITEM_API + "item/updateQuantity";
            ItemQuantityUpdateDto updateDto = new ItemQuantityUpdateDto();
            updateDto.setId(dto.getItemId());
            updateDto.setStockQuantity(newQuantity);
            HttpEntity<ItemQuantityUpdateDto> entity = new HttpEntity<>(updateDto);
            ResponseEntity<CommonResponse> response =
                    restTemplate.exchange(
                            itemPathUrl,
                            HttpMethod.POST,
                            entity,
                            CommonResponse.class
                    );
            System.out.println(response.getBody().getMessage());

        }
//        orderRepository.save를 먼저 함으로서 위 코드에서 에러 발생시 item서비스 호출하지 않으므로, 트랜잭션 문제 발생하지 않음.
//        지금은 저장할때 오류나면 롤백 불가능.


//        이후에 추가적인 로직이 존재할 경우 트랜잭션 이슈는 여전히 발생 가능함.
//        해결책으로 에러 발생할 가능성이 있는 코드 전체를
//        try catch로 예외처리 이후에 catch에서 updateRollbakQuantitiy 호출

        return orderRepo.save(ordering);
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