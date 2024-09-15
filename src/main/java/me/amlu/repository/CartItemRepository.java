package me.amlu.repository;

import lombok.NonNull;
import me.amlu.model.CartItem;

public interface CartItemRepository extends BaseRepository<CartItem, Long> {

    void deleteByCartCustomerIdAndCartDeletedAtIsNull(@NonNull Long customerId);
}
