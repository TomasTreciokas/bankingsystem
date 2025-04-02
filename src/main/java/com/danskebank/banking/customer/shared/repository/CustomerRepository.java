package com.danskebank.banking.customer.shared.repository;

import com.danskebank.banking.customer.shared.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    @Query(value = """
                SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
                FROM customers c
                LEFT JOIN customer_individual ci ON c.id = ci.id
                LEFT JOIN customer_private cp ON c.id = cp.id
                LEFT JOIN customer_public cb ON c.id = cb.id
                WHERE c.name = :name
                  AND c.phone_number = :phone
                  AND c.email = :email
                  AND (
                       (c.customer_discriminator = 'INDIVIDUAL' AND ci.last_name = :lastName AND ci.national_id = :nationalId)
                    OR (c.customer_discriminator = 'PRIVATE' AND cp.private_reference_code = :privateRefCode)
                    OR (c.customer_discriminator = 'PUBLIC' AND cb.registration_number = :registrationNumber)
                  )
            """, nativeQuery = true)
    boolean existsByFullIdentity(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("lastName") String lastName,
            @Param("nationalId") String nationalId,
            @Param("privateRefCode") String privateRefCode,
            @Param("registrationNumber") String registrationNumber
    );
} 