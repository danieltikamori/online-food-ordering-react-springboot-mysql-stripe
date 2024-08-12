package me.amlu.request;

import lombok.Data;
import me.amlu.model.Address;

@Data
public class OrderRequest {

    private Long restaurantId;
    private Address deliveryAddress;

}
