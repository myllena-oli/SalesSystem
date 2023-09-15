package org.example.controller;

import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<Object> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("A lista de clientes está vazia.");
        }

        return ResponseEntity.ok(customers);
    }


    @PostMapping("/countByEmailContaining")
    public ResponseEntity<String> countCustomersByEmailContaining(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        if (email != null && !email.isEmpty()) {
            long count = customerRepository.countByEmailContaining(email);

            if (count > 0) {
                return ResponseEntity.ok("Número de clientes com email contendo '" + email + "': " + count);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum cliente encontrado com email contendo '" + email + "'.");
    }


    @PostMapping
    public ResponseEntity<Object> createCustomer(@RequestBody Customer customer) {
        customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente criado.");
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente com ID " + id + " não encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cliente com ID " + id + " foi deletado com sucesso.");

    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(updatedCustomer.getName());
            customer.setEmail(updatedCustomer.getEmail());
            customer.setCpf(updatedCustomer.getCpf());
            customer.setAddress(updatedCustomer.getAddress());
            return customerRepository.save(customer);
        }).orElse(null);
    }
}

