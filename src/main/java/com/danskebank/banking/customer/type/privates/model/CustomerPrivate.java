package com.danskebank.banking.customer.type.privates.model;

import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "CUSTOMER_PRIVATE")
@DiscriminatorValue("PRIVATE")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class CustomerPrivate extends Customer {

    @Column(name = "PRIVATE_REFERENCE_CODE")
    private String privateReferenceCode;

    @Override
    public CustomerType getCustomerType() {
        return CustomerType.PRIVATE;
    }
}

