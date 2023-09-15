package org.example.controller;

import org.example.controller.CustomerController;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John", "john@example.com", "1234567890", "123 Main St"));

        when(customerRepository.findAll()).thenReturn(customers);

        ResponseEntity<Object> response = customerController.getAllCustomers();

        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody() instanceof List);

        verify(customerRepository, times(1)).findAll();
    }
    @Test
    void testGetAllCustomersEmptyList() {
        List<Customer> emptyCustomersList = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(emptyCustomersList);

        ResponseEntity<Object> response = customerController.getAllCustomers();

        assert(response.getStatusCode() == HttpStatus.NOT_FOUND);
        assert(response.getBody().equals("A lista de clientes está vazia."));

        verify(customerRepository, times(1)).findAll();
    }


    @Test
    void testCountCustomersByEmailContaining() {
        String email = "example@example.com";
        long count = 5;

        when(customerRepository.countByEmailContaining(email)).thenReturn(count);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);

        ResponseEntity<String> response = customerController.countCustomersByEmailContaining(requestBody);

        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody().equals("Número de clientes com email contendo '" + email + "': " + count));

        verify(customerRepository, times(1)).countByEmailContaining(email);
    }
    @Test
    void testCountCustomersByEmailContainingNoCustomerFound() {
        String email = "nonexistent@example.com";

        when(customerRepository.countByEmailContaining(email)).thenReturn(0L);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);

        ResponseEntity<String> response = customerController.countCustomersByEmailContaining(requestBody);

        assert(response.getStatusCode() == HttpStatus.NOT_FOUND);
        assert(response.getBody().equals("Nenhum cliente encontrado com email contendo '" + email + "'."));

        verify(customerRepository, times(1)).countByEmailContaining(email);
    }


    @Test
    void testCreateCustomer() {
        Customer customer = new Customer(1L, "John", "john@example.com", "1234567890", "123 Main St");

        ResponseEntity<Object> response = customerController.createCustomer(customer);

        assert(response.getStatusCode() == HttpStatus.CREATED);
        assert(response.getBody().equals("Cliente criado."));

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testGetCustomerById() {
        Long id = 1L;
        Customer customer = new Customer(id, "John", "john@example.com", "1234567890", "123 Main St");

        when(customerRepository.findById(id)).thenReturn(java.util.Optional.of(customer));

        ResponseEntity<Object> response = customerController.getCustomerById(id);

        assert(response.getStatusCode() == HttpStatus.OK);
        assert(response.getBody().equals(customer));

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        Long id = 1L;

        when(customerRepository.findById(id)).thenReturn(java.util.Optional.empty());

        ResponseEntity<Object> response = customerController.getCustomerById(id);

        assert(response.getStatusCode() == HttpStatus.NOT_FOUND);
        assert(response.getBody().equals("Cliente com ID " + id + " não encontrado."));

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteCustomer() {
        Long id = 1L;

        ResponseEntity<String> response = customerController.deleteCustomer(id);

        assert(response.getStatusCode() == HttpStatus.NO_CONTENT);
        assert(response.getBody().equals("Cliente com ID " + id + " foi deletado com sucesso."));

        verify(customerRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdateCustomer() {
        Long id = 1L;
        Customer updatedCustomer = new Customer(id, "Updated John", "updated@example.com", "9876543210", "456 Second St");

        when(customerRepository.findById(id)).thenReturn(Optional.of(updatedCustomer));
        when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);

        ResponseEntity<Object> response = customerController.updateCustomer(id, updatedCustomer);
        assert(response.getStatusCode() == HttpStatus.OK);

        // Verifique se o corpo da resposta está vazio
        assert(response.getBody() == null);

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).save(updatedCustomer);
    }


    @Test
    void testUpdateCustomerNotFound() {
        Long id = 1L;
        Customer updatedCustomer = new Customer(id, "Updated John", "updated@example.com", "9876543210", "456 Second St");

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = customerController.updateCustomer(id, updatedCustomer);

        assert(response.getStatusCode() == HttpStatus.NOT_FOUND);
        assert(response.getBody() == null);

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, never()).save(updatedCustomer);
    }
}
