package org.vovgoo.orderservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vovgoo.orderservice.entity.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order,UUID> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.payment")
    Page<Order> findAllWithItemsAndPayment(Pageable pageable);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.payment WHERE o.userId = :userId")
    Page<Order> findByUserIdWithItemsAndPayment(@Param("userId") UUID userId, Pageable pageable);
}
