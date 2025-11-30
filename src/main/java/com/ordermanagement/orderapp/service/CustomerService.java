package com.ordermanagement.orderapp.service;


import com.ordermanagement.orderapp.entity.Customer;
import com.ordermanagement.orderapp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + customer.getEmail());
        }
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(()  -> new IllegalArgumentException("Customer not found with id: " + id));
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);

        if (!customer.getEmail().equals(customerDetails.getEmail()) &&
                customerRepository.existsByEmail(customerDetails.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + customerDetails.getEmail());
        }

        customer.setName(customerDetails.getName());
        customer.setPhone(customerDetails.getPhone());
        customer.setEmail(customerDetails.getEmail());

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
