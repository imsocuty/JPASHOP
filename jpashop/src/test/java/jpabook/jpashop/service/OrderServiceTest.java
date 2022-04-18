package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{

        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(),book.getId(),orderCount);

        Order getOrder =orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(Float.parseFloat("주문한 상품 종류 수가 정확해야 한다."),1, getOrder.getOrderItems().size());
        assertEquals(Float.parseFloat("주문 가격은 가격 * 수량이다."), 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals(Float.parseFloat("주문 수량만큼 재고가 줄어야 한다."),8, book.getStockQuantity());

    }

    @Test
    public void 주문취소() throws Exception{

    }


    @Test(expected= NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{




    }
}