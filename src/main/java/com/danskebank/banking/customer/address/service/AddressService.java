package com.danskebank.banking.customer.address.service;

import com.danskebank.banking.customer.address.model.Address;
import com.danskebank.banking.customer.address.rest.dto.create.AddressCreateRequest;
import com.danskebank.banking.customer.address.rest.dto.update.AddressUpdateRequest;
import com.danskebank.banking.customer.shared.model.Customer;

import java.util.List;
import java.util.Set;

public interface AddressService {

    void addAddresses(Customer customer, List<AddressCreateRequest> addressCreateRequests);

    void syncAddresses(Customer customer, List<AddressUpdateRequest> addressUpdates);

    Address updateExisting(Address existing, AddressUpdateRequest dto);

    Address createNew(AddressUpdateRequest dto);

    void removeDeleted(Customer customer, Set<Long> idsToDelete);
}