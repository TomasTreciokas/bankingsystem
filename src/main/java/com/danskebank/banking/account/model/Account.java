package com.danskebank.banking.account.model;

import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.general.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ACCOUNTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Audited
public class Account extends BaseEntity {

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "ACCOUNT_CUSTOMER",
            joinColumns = @JoinColumn(name = "ACCOUNT_ID"),
            inverseJoinColumns = @JoinColumn(name = "CUSTOMER_ID")
    )
    @Builder.Default
    private Set<Customer> owners = new HashSet<>();
} 