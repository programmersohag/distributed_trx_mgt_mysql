package com.example.distributed_trx_mgt;

import com.example.distributed_trx_mgt.inventory.InventoryEntity;
import com.example.distributed_trx_mgt.inventory.InventoryRepository;
import com.example.distributed_trx_mgt.order.OrderEntity;
import com.example.distributed_trx_mgt.order.OrderRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Component
public class Application {

    private final InventoryRepository inventoryRepository;

    private final OrderRepository orderRepository;

    public Application(InventoryRepository inventoryRepository, OrderRepository orderRepository) {
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(String productId, int amount) throws Exception {
        String orderId = UUID.randomUUID().toString();
        InventoryEntity inventory = inventoryRepository.getReferenceById(productId);
        inventory.setBalance(inventory.getBalance() - amount);
        inventoryRepository.save(inventory);
        OrderEntity order = new OrderEntity();
        order.setOrderId(orderId);
        order.setProductId(productId);
        order.setAmount((long) amount);
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<OrderEntity>> violations = validator.validate(order);
        if (!violations.isEmpty())
            throw new Exception("Invalid instance of an order.");
        orderRepository.save(order);

    }
}
