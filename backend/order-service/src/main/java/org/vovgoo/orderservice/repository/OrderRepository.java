package org.vovgoo.orderservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vovgoo.orderservice.entity.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order,UUID> {

    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    Page<Order> findAllWithItemsAndPayment(Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.orderDate DESC")
    Page<Order> findByUserIdWithItemsAndPayment(@Param("userId") UUID userId, Pageable pageable);
}
