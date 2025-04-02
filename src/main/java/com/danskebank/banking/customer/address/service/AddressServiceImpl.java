package com.danskebank.banking.customer.address.service;

import com.danskebank.banking.customer.address.model.Address;
import com.danskebank.banking.customer.address.rest.dto.create.AddressCreateRequest;
import com.danskebank.banking.customer.address.rest.dto.update.AddressUpdateRequest;
import com.danskebank.banking.customer.shared.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    public void addAddresses(Customer customer, List<AddressCreateRequest> addressCreateRequests) {
        if (addressCreateRequests == null) return;

        List<Address> addresses = (List<Address>) addressCreateRequests.stream()
                .map(dto -> Address.builder()
                        .street(dto.getStreet())
                        .city(dto.getCity())
                        .country(dto.getCountry())
                        .build())
                .toList();

        customer.getAddresses().clear();
        customer.getAddresses().addAll(addresses);
    }

    public void syncAddresses(Customer customer, List<AddressUpdateRequest> addressUpdates) {
        if (addressUpdates == null) {
            return;
        }

        Map<Long, Address> existingById = customer.getAddresses().stream()
                .filter(a -> a.getId() != null)
                .collect(Collectors.toMap(Address::getId, Function.identity()));

        Set<Long> incomingIds = addressUpdates.stream()
                .map(AddressUpdateRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<Address> updated = addressUpdates.stream()
                .map(dto -> {
                    if (dto.getId() != null && existingById.containsKey(dto.getId())) {
                        return updateExisting(existingById.get(dto.getId()), dto);
                    }
                    return createNew(dto);
                })
                .toList();

        removeDeleted(customer, incomingIds);

        customer.getAddresses().clear();
        customer.getAddresses().addAll(updated);
    }

    public Address updateExisting(Address existing, AddressUpdateRequest dto) {
        existing.setStreet(dto.getStreet());
        existing.setCity(dto.getCity());
        existing.setCountry(dto.getCountry());
        existing.setState(dto.getState());
        existing.setZipCode(dto.getZipCode());

        return existing;
    }

    public Address createNew(AddressUpdateRequest dto) {
        return Address.builder()
                .street(dto.getStreet())
                .city(dto.getCity())
                .country(dto.getCountry())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .build();
    }

    public void removeDeleted(Customer customer, Set<Long> idsToDelete) {
        List<Address> toRemove = customer.getAddresses().stream()
                .filter(addr -> addr.getId() != null && !idsToDelete.contains(addr.getId()))
                .toList();
        customer.getAddresses().removeAll(toRemove);
    }
}
