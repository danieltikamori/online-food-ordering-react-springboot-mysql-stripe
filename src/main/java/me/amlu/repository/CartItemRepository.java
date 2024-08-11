package me.amlu.repository;

import me.amlu.model.Cart;
import me.amlu.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
