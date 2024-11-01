package com.example.distributed_trx_mgt.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
@Entity
public class OrderEntity {
    @Id
    private String orderId;
    private String productId;
    @Max(5)
    private Long amount;
}
