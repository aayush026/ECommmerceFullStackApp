package com.ecomm.ayush.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomm.ayush.model.Address;

public interface AddressRepo extends JpaRepository<Address, Long> {

}
