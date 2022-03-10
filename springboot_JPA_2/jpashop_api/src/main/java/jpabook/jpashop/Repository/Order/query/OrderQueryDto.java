package jpabook.jpashop.Repository.Order.query;

import jpabook.jpashop.Domain.Address;
import jpabook.jpashop.Domain.Enum.OrderStatus;
import jpabook.jpashop.Domain.Order;
import jpabook.jpashop.api.OrderApiController;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate,  OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

}
