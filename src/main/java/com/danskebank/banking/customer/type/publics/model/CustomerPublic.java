package com.danskebank.banking.customer.type.publics.model;

import com.danskebank.banking.customer.shared.model.Customer;
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
@Table(name = "CUSTOMER_PUBLIC")
@DiscriminatorValue("PUBLIC")
@Audited
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPublic extends Customer {
    @Column(name = "REGISTRATION_NUMBER")
    private String registrationNumber;
}
