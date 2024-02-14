package com.example.ordering.ordering.repository;

import com.example.ordering.ordering.domain.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Ordering, Long> {

    List<Ordering> findByMemberId(Long member_id);
}

