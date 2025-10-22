package com.ordermanagement.orderapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a Customer in the Order Management System
 * A Customer can place multiple orders.
 *
 * @author Luis Garcia
 * @version 0.1
 */
@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Access(AccessType.FIELD)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;
    @NotBlank(message = "Email is required")
    @Column(nullable = false)
    @Email(message = "Email must be valid")
    private String email;
    @NotNull(message = "Phone is required")
    @Column(nullable = false)
    private Long phone;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @Transient
    public Long getId() {
        return this.customerId;
    }

    public String getName() {
        return  this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return this.phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
