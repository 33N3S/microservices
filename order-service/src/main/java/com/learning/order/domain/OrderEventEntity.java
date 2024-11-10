package com.learning.order.domain;

import com.learning.order.domain.models.OrderEventType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_event_id_seq")
    private Long id;

    @Column(nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String eventId;

    @Enumerated(EnumType.STRING)
    private OrderEventType eventType;

    @Column(nullable = false)
    private String payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
