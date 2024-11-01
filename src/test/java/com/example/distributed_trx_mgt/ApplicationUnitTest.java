package com.example.distributed_trx_mgt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import com.example.distributed_trx_mgt.inventory.InventoryEntity;
import com.example.distributed_trx_mgt.inventory.InventoryRepository;
import com.example.distributed_trx_mgt.order.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
public class ApplicationUnitTest {

    private static String productId = UUID.randomUUID().toString();

    @Autowired
    private Application application;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderRepository orderRepository;

/*    @Test
    @Transactional
    public void testPlaceOrderSuccess() throws Exception {
        int amount = 1;
        long initialBalance = getBalance(inventoryRepository, productId);
        application.placeOrder(productId, amount);
        long finalBalance = getBalance(inventoryRepository, productId);
        assertEquals(initialBalance - amount, finalBalance);
    }*/

    @Test
    @Transactional
    public void testPlaceOrderFailure() {
        int amount = 10;
        long initialBalance = getBalance(inventoryRepository, productId);
        try {
            application.placeOrder(productId, amount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        long finalBalance = getBalance(inventoryRepository, productId);
        assertEquals(initialBalance, finalBalance);
    }

/*    @BeforeEach
    public void setUp() {
        InventoryEntity inventory = new InventoryEntity();
        inventory.setProductId(productId);
        inventory.setBalance(10000L);
        inventoryRepository.save(inventory);

    }*/

    private static long getBalance(InventoryRepository inventoryRepository, String productId) {
        return inventoryRepository.getReferenceById(productId).getBalance();
    }

}
