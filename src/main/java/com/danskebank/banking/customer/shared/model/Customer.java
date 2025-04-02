package com.danskebank.banking.customer.shared.model;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.customer.address.model.Address;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import com.danskebank.banking.general.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CUSTOMERS")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "CUSTOMER_DISCRIMINATOR")
@Audited
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {

    @Column(name = "NAME")
    protected String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "CUSTOMER_TYPE")
    protected CustomerType customerType;

    @Column(name = "PHONE_NUMBER")
    protected String phoneNumber;

    @Column(name = "EMAIL")
    protected String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    protected Set<Address> addresses = new HashSet<>();

    @ManyToMany(mappedBy = "owners")
    @Builder.Default
    protected Set<Account> accounts = new HashSet<>();
} 