package com.example.distributed_trx_mgt.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class InventoryEntity {

    @Id
    private String productId;
    private Long balance;
}
