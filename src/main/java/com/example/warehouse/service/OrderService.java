package com.example.warehouse.service;

import com.example.warehouse.dao.CustomerRepository;
import com.example.warehouse.dao.OrderItemRepository;
import com.example.warehouse.dao.OrderRepository;
import com.example.warehouse.dao.ProductRepository;
import com.example.warehouse.dto.CreateOrderDto;
import com.example.warehouse.dto.OrderDto;
import com.example.warehouse.dto.OrderGetByIdDto;
import com.example.warehouse.dto.PatchOrderDto;
import com.example.warehouse.dto.ProductToGetOrderByIdDto;
import com.example.warehouse.dto.ProductToOrderDto;
import com.example.warehouse.entities.Customer;
import com.example.warehouse.entities.Order;
import com.example.warehouse.entities.OrderItem;
import com.example.warehouse.entities.OrderItemId;
import com.example.warehouse.entities.Product;
import com.example.warehouse.enums.OrderStatus;
import com.example.warehouse.exceptions.InvalidEntityDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    private static BigDecimal getTotalPrice(ProductToOrderDto p, Product product) {
        if (product.getQuantity() < p.getQuantity() || p.getQuantity() < 0) {
            throw new InvalidEntityDataException("Ошибшка: недостаточно товраов на складе", "INCORRECT_QUANTITY", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!product.isAvailable()) {
            throw new InvalidEntityDataException("Ошибка: товар недоступен к заказу", "INCORRECT_PRODUCT_STATUS", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        BigDecimal price = product.getPrice();
        return price.multiply(BigDecimal.valueOf(p.getQuantity()));
    }

    @Transactional
    public UUID create(CreateOrderDto createOrderDto, Long customerId) {

        if (createOrderDto.getDeliveryAddress() == null || createOrderDto.getDeliveryAddress().isBlank()) {
            throw new InvalidEntityDataException("Неккоректный адресс", "INCORRECT_ADDRESS", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        Order order = new Order();
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        Customer customer = customerOptional.orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

        order.setCustomer(customer);
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryAddress(createOrderDto.getDeliveryAddress());

        order = orderRepository.save(order);

        for (ProductToOrderDto productDto : createOrderDto.getProducts()) {
            Product product = productRepository.findById(productDto.getId()).orElseThrow();

            if (!product.isAvailable()) {
                throw new InvalidEntityDataException("Ошибка: товар недоступен к заказу", "INCORRECT_PRODUCT_STATUS", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            BigDecimal productTotalPrice = getTotalPrice(productDto, product);
            totalPrice = totalPrice.add(productTotalPrice);

            if (productDto.getQuantity() != 0) {
                product.setQuantity(product.getQuantity() - productDto.getQuantity());
                productRepository.save(product);
            }

            OrderItem orderItem = new OrderItem();
            OrderItemId orderItemId = new OrderItemId(order.getId(), product.getId());
            orderItem.setId(orderItemId);
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setPrice(productTotalPrice);
            orderItem.setQuantity(productDto.getQuantity());
            orderItemRepository.save(orderItem);

            orderItemRepository.save(orderItem);
        }

        return order.getId();
    }

    @Transactional
    public UUID patchOrder(PatchOrderDto patchDto, UUID orderId, Long customerId) {

        Order order = orderRepository.findById(orderId).orElseThrow();

        if (!Objects.equals(order.getCustomer().getId(), customerId)) {
            throw new InvalidEntityDataException("Ошибка: изменение заказа недоступно для пользователя " + customerId, "INCORRECT_CUSTOMER_ID", HttpStatus.FORBIDDEN);
        }

        if (!order.getStatus().equals(OrderStatus.CREATED)) {
            throw new InvalidEntityDataException("Ошибка: изменение заказа недоступно для заказа со статусом" + order.getStatus(), "INCORRECT_ORDER_STATUS", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        for (ProductToOrderDto p : patchDto.getPatchOrders()) {

            if (p.getQuantity() < 0) {
                throw new InvalidEntityDataException("Ошибка: количество товара не может быть отрицательным для продукта с ID " + p.getId(), "NEGATIVE_PRODUCT_QUANTITY", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Product product = productRepository.findById(p.getId()).orElseThrow(() -> new InvalidEntityDataException("Ошибка: переданного ID продукта" + p.getId() + " нет в заказе " + order.getId(), "INCORRECT_PRODUCT_ID", HttpStatus.UNPROCESSABLE_ENTITY));
            int availableQuantity = product.getQuantity();
            int requestedQuantity = p.getQuantity();

            if (availableQuantity < requestedQuantity) {
                throw new InvalidEntityDataException("Ошибка: недостаточное количество товара на складе для продукта с ID " + p.getId(), "INSUFFICIENT_PRODUCT_QUANTITY", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            OrderItem orderItem = orderItemRepository.findOrderItemByProductId(p.getId());

            if (orderItem == null) {
                orderItem = new OrderItem();
                OrderItemId orderItemId = new OrderItemId();
                orderItemId.setOrderId(order.getId());
                orderItemId.setProductId(p.getId());
                orderItem.setId(orderItemId);
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(p.getQuantity());
                orderItem.setPrice(product.getPrice());
            }

            orderItem.setQuantity(orderItem.getQuantity() + p.getQuantity());

            BigDecimal newProductTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            orderItem.setPrice(newProductTotalPrice);

            orderItemRepository.save(orderItem);
        }

        return orderId;
    }

    @Transactional
    public OrderGetByIdDto getById(Long customerId, UUID orderId) {
        OrderGetByIdDto getByIdDto = new OrderGetByIdDto();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new InvalidEntityDataException("Ошибка: заказа с ID " + orderId + " не существует ", "INCORRECT_ORDER_ID", HttpStatus.UNPROCESSABLE_ENTITY));

        if (!Objects.equals(order.getCustomer().getId(), customerId)) {
            throw new InvalidEntityDataException("Ошибка: изменение заказа недоступно для пользователя " + customerId, "INCORRECT_CUSTOMER_ID", HttpStatus.FORBIDDEN);
        }

        List<OrderItem> orderItems = orderItemRepository.findAllByOrderIdWithProducts(orderId);
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<ProductToGetOrderByIdDto> productToGetOrderDtos = new ArrayList<>();

        for (OrderItem o : orderItems) {
            BigDecimal itemTotalPrice = o.getPrice().multiply(BigDecimal.valueOf(o.getQuantity()));

            ProductToGetOrderByIdDto productDto = new ProductToGetOrderByIdDto();
            productDto.setProductId(o.getProduct().getId());
            productDto.setName(productRepository.findNameById(o.getProduct().getId()));
            productDto.setQuantity(o.getQuantity());
            productDto.setPrice(o.getPrice());

            productToGetOrderDtos.add(productDto);

            totalPrice = totalPrice.add(itemTotalPrice);
        }

        getByIdDto.setOrderId(orderId);
        getByIdDto.setProducts(productToGetOrderDtos);
        getByIdDto.setTotalPrice(totalPrice);

        return getByIdDto;
    }

    @Transactional
    public void deleteOrderById(UUID orderId, Long customerId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new InvalidEntityDataException("Ошибка: заказа с ID " + orderId, "INCORRECT_ORDER_ID", HttpStatus.UNPROCESSABLE_ENTITY));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new InvalidEntityDataException("Ошибка: изменение заказа недоступно для пользователя " + customerId, "INCORRECT_CUSTOMER_ID", HttpStatus.FORBIDDEN);
        }

        if (orderRepository.findById(orderId).orElseThrow().getStatus().equals(OrderStatus.CANCELLED)) {
            throw new InvalidEntityDataException("Ошибка: заказ уже отменен", "INCORRECT_ORDER_STATUS", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (orderRepository.findById(orderId).orElseThrow().getStatus().equals(OrderStatus.CREATED)) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);

            for (OrderItem orderItem : orderItems) {
                Product product = orderItem.getProduct();
                int quantity = orderItem.getQuantity();

                product.setQuantity(product.getQuantity() + quantity);
                productRepository.save(product);
        }
        }
    }

    @Transactional
    public void confirmOrder(UUID orderId, Long customerId) {
    //TODO: create method
    }

    @Transactional
    public OrderStatus updateStatus(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.DONE);
        orderRepository.save(order);

        return order.getStatus();
    }

}
