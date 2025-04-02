package com.danskebank.banking.customer.shared.service;

import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CustomerServiceResolver {

    private final Map<CustomerType, CustomerService> customerServiceMap;

    public CustomerServiceResolver(List<CustomerService> customerServiceList) {
        this.customerServiceMap = customerServiceList.stream()
                .collect(Collectors.toUnmodifiableMap(CustomerService::getSupportedType, Function.identity()));
    }

    public CustomerService resolveService(CustomerType customerType) {
        if (customerType == null) {
            throw new IllegalArgumentException("Customer type must not be null");
        }

        return Optional.ofNullable(customerServiceMap.get(customerType))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No customer service found for customer type: " + customerType));
    }
}
