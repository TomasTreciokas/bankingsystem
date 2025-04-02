package com.danskebank.banking.customer.shared.service;

import com.danskebank.banking.customer.address.model.Address;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customer> build(CustomerSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.searchCriteria() != null && !request.searchCriteria().isBlank()) {
                String term = "%" + request.searchCriteria().toLowerCase() + "%";
                Join<Customer, Address> addressJoin = root.join("addresses", JoinType.LEFT);

                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), term),
                        cb.like(cb.lower(root.get("lastName")), term),
                        cb.like(cb.lower(addressJoin.get("city")), term),
                        cb.like(cb.lower(addressJoin.get("street")), term)
                ));
            }

            if (request.customerType() != null) {
                predicates.add(cb.equal(root.get("customerType"), request.customerType()));
            }

            if (request.nationalId() != null) {
                predicates.add(cb.and(
                        cb.equal(root.get("customerType"), CustomerType.INDIVIDUAL),
                        cb.equal(root.get("nationalId"), request.nationalId())
                ));
            }
            if (request.dateOfBirth() != null) {
                predicates.add(cb.and(
                        cb.equal(root.get("customerType"), CustomerType.INDIVIDUAL),
                        cb.equal(root.get("dateOfBirth"), request.dateOfBirth())
                ));
            }
            if (request.registrationNumber() != null) {
                predicates.add(cb.and(
                        cb.equal(root.get("customerType"), CustomerType.PUBLIC),
                        cb.equal(root.get("registrationNumber"), request.registrationNumber())
                ));
            }
            if (request.privateReferenceCode() != null) {
                predicates.add(cb.and(
                        cb.equal(root.get("customerType"), CustomerType.PRIVATE),
                        cb.equal(root.get("privateReferenceCode"), request.privateReferenceCode())
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
