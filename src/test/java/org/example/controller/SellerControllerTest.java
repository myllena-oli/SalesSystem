package org.example.controller;

import org.example.controller.SellerController;
import org.example.model.Seller;
import org.example.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SellerControllerTest {

    @InjectMocks
    private SellerController sellerController;
    @Mock
    private SellerRepository sellerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetAllSellersEmptyList() {
        when(sellerRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<Object> response = sellerController.getAllSellers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("A lista de vendedores está vazia.", response.getBody());
    }

    @Test
    public void testGetAllSellersNonEmptyList() {
        List<Seller> sellers = new ArrayList<>();
        sellers.add(new Seller(1L, "John", "john@example.com", "1234567890", 5000.0));

        when(sellerRepository.findAll()).thenReturn(sellers);

        ResponseEntity<Object> response = sellerController.getAllSellers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sellers, response.getBody());
    }

    @Test
    public void testCreateSeller() {
        Seller newSeller = new Seller(1L, "Alice", "alice@example.com", "9876543210", 6000.0);

        ResponseEntity<Object> response = sellerController.createSeller(newSeller);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Vendedor criado.", response.getBody());
        verify(sellerRepository, times(1)).save(newSeller);
    }

    @Test
    public void testGetSellerSalariesEmptyList() {
        when(sellerRepository.findAllSalariesOrderBySalaryDesc()).thenReturn(new ArrayList<>());

        ResponseEntity<Object> response = sellerController.getSellerSalaries();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Não há salários para exibir.", response.getBody());
    }

    @Test
    public void testGetSellerSalariesNonEmptyList() {
        List<Double> salaries = new ArrayList<>();
        salaries.add(5000.0);
        salaries.add(6000.0);

        when(sellerRepository.findAllSalariesOrderBySalaryDesc()).thenReturn(salaries);

        ResponseEntity<Object> response = sellerController.getSellerSalaries();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(salaries, response.getBody());
    }

    @Test
    public void testGetSellerByIdSellerFound() {
        Long id = 1L;
        Seller seller = new Seller(id, "Bob", "bob@example.com", "5555555555", 5500.0);

        when(sellerRepository.findById(id)).thenReturn(Optional.of(seller));

        ResponseEntity<Object> response = sellerController.getSellerById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seller, response.getBody());
    }

    @Test
    public void testGetSellerByIdSellerNotFound() {
        Long id = 1L;

        when(sellerRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = sellerController.getSellerById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Vendedor com ID " + id + " não encontrado.", response.getBody());
    }

    @Test
    public void testUpdateSellerSellerFound() {
        Long id = 1L;
        Seller existingSeller = new Seller(id, "Existing Seller", "existing@example.com", "1111111111", 4000.0);
        Seller updatedSeller = new Seller(id, "Updated Seller", "updated@example.com", "9999999999", 4500.0);

        when(sellerRepository.findById(id)).thenReturn(Optional.of(existingSeller));
        when(sellerRepository.save(existingSeller)).thenReturn(updatedSeller);

        Seller response = sellerController.updateSeller(id, updatedSeller);

        assertNotNull(response);
        assertEquals(updatedSeller, response);
    }

    @Test
    public void testUpdateSellerSellerNotFound() {
        Long id = 1L;
        Seller updatedSeller = new Seller(id, "Updated Seller", "updated@example.com", "9999999999", 4500.0);

        when(sellerRepository.findById(id)).thenReturn(Optional.empty());

        Seller response = sellerController.updateSeller(id, updatedSeller);

        assertNull(response);
    }

    @Test
    public void testDeleteSeller() {
        Long id = 1L;

        ResponseEntity<String> response = sellerController.deleteSeller(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Vendedor com ID " + id + " foi deletado com sucesso.", response.getBody());
        verify(sellerRepository, times(1)).deleteById(id);
    }
}
