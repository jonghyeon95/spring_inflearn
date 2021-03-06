package jpabook.jpashop.api;

import jpabook.jpashop.Domain.Address;
import jpabook.jpashop.Domain.Enum.OrderStatus;
import jpabook.jpashop.Domain.Order;
import jpabook.jpashop.Domain.OrderItem;
import jpabook.jpashop.Repository.Order.query.OrderFlatDto;
import jpabook.jpashop.Repository.Order.query.OrderItemQueryDto;
import jpabook.jpashop.Repository.Order.query.OrderQueryDto;
import jpabook.jpashop.Dto.OrderSearch;
import jpabook.jpashop.Repository.Order.query.OrderQueryRepository;
import jpabook.jpashop.Repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * xToMany 조회
 * Order -> OrderItems 1:N
 */

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    //================주문조회 v1 (entity직접 노출)=================//    #entity 직접 노출은 좋지않음
    @GetMapping("/api/v1/orders")
    public Result ordersV1(){
        List<Order> all = orderRepository.findAllBySearch(new OrderSearch());
        for (Order order : all) {
            order.getMember();
            order.getDelivery().getAddress();
//            for (OrderItem orderItem : order.getOrderItems()) {
//                orderItem.getItem().getName();
//            }
            order.getOrderItems().stream().forEach(o->o.getItem().getName());
        }
        return new Result(all);
    }

    //================주문조회 v2 (Dto반환, OrderItem에 대한 Dto 생성)=================// #N+1 문제
    @GetMapping("/api/v2/orders")
    public Result ordersV2(){
        List<Order> all = orderRepository.findAllBySearch(new OrderSearch());
        List<OrderDto> collect = all.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return new Result(collect);
    }

    //================주문조회 v3 (fetch join, distinct)=================// #페이징문제
    @GetMapping("/api/v3/orders")
    public Result ordersV3(){
        List<Order> all = orderRepository.findAllWithItem();
        List<OrderDto> collect = all.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return new Result(collect);
    }

    //================주문조회 v3.1 (page를 위한 batchsize)=================//
    @GetMapping("/api/v3.1/orders")
    public Result ordersV3_page(@RequestParam(required = false, defaultValue = "0") int offset,
                                @RequestParam(required = false, defaultValue = "100") int limit){
        List<Order> all = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> collect = all.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return new Result(collect);
    }

    //================주문조회 v4 (dto 직접 조회)=================//컬렉션 조회때 N+1문제
    @GetMapping("/api/v4/orders")
    public Result ordersV4() {
        List<OrderQueryDto> orders = orderQueryRepository.findOrderQueryDtos();

        return new Result(orders);
    }

    //================주문조회 v5 (where 조건에 IN 을 이용해 쿼리2번으로 변경)=================//
    @GetMapping("/api/v5/orders")
    public Result ordersV5() {
        List<OrderQueryDto> orders = orderQueryRepository.findAllByDto_optimization();

        return new Result(orders);
    }

    //================주문조회 v6 (뻥튀긴된 데이터 그냥 가져와서 OrderItem을 List화 시킴)=================//추가작업이 큼, 페이징 불가
    @GetMapping("/api/v6/orders")
    public Result ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        List<OrderQueryDto> collect = flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());

        return new Result(collect);
    }


    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //Lazy 초기화
            orderItems = order.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem)).collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;    //상품명
        private int orderPrice;
        private int count;  //주문수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }


}
