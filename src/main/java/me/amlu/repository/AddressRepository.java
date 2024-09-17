package me.amlu.repository;

import me.amlu.model.Address;

import java.util.Optional;

public interface AddressRepository extends BaseRepository<Address, Long> {

    Optional<Address> findMatchingAddress( Address deliveryAddress); // <1>
}
