package com.danskebank.banking.customer.type.individuals.model;

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

import java.time.LocalDate;

@Entity
@Table(name = "CUSTOMER_INDIVIDUAL")
@DiscriminatorValue("INDIVIDUAL")
@Audited
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerIndividual extends Customer {
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "NATIONAL_ID")
    private String nationalId;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
}
