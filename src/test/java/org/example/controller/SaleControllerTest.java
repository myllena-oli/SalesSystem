package org.example.controller;

import org.example.model.Customer;
import org.example.model.Sale;
import org.example.model.Seller;
import org.example.repository.CustomerRepository;
import org.example.repository.SaleRepository;
import org.example.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SaleControllerTest {

    @InjectMocks
    private SaleController saleController;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllSalesEmptyList() {
        List<Sale> emptySalesList = new ArrayList<>();
        when(saleRepository.findAll()).thenReturn(emptySalesList);

        ResponseEntity<Object> response = saleController.getAllSales();

        assert (response.getStatusCode() == HttpStatus.NOT_FOUND);
        assert (response.getBody().equals("A lista de vendas está vazia."));

        verify(saleRepository, times(1)).findAll();
    }

    @Test
    void testGetAllSalesNonEmptyList() {
        List<Sale> nonEmptySalesList = new ArrayList<>();
        nonEmptySalesList.add(new Sale());
        when(saleRepository.findAll()).thenReturn(nonEmptySalesList);

        ResponseEntity<Object> response = saleController.getAllSales();

        assert (response.getStatusCode() == HttpStatus.OK);
        assert (response.getBody().equals(nonEmptySalesList));

        verify(saleRepository, times(1)).findAll();
    }

    @Test
    void testGetSalesAbove10EmptyList() {
        List<Sale> emptySalesListAbove10 = new ArrayList<>();
        when(saleRepository.findByTotalValueGreaterThan(10.00)).thenReturn(emptySalesListAbove10);

        ResponseEntity<Object> response = saleController.getSalesAbove10();

        assert (response.getStatusCode() == HttpStatus.NOT_FOUND);
        assert (response.getBody().equals("Não há vendas com valor total acima de 10.00."));

        verify(saleRepository, times(1)).findByTotalValueGreaterThan(10.00);
    }

    @Test
    void testGetSalesAbove10NonEmptyList() {
        List<Sale> nonEmptySalesListAbove10 = new ArrayList<>();
        Sale sale1 = new Sale();
        sale1.setTotalValue(15.00);
        nonEmptySalesListAbove10.add(sale1);
        when(saleRepository.findByTotalValueGreaterThan(10.00)).thenReturn(nonEmptySalesListAbove10);

        ResponseEntity<Object> response = saleController.getSalesAbove10();

        assert (response.getStatusCode() == HttpStatus.OK);
        assert (response.getBody().equals(nonEmptySalesListAbove10));

        verify(saleRepository, times(1)).findByTotalValueGreaterThan(10.00);
    }

    @Test
    void testUpdateTotalValueToZero() {
        ResponseEntity<Void> response = saleController.updateTotalValueToZero();

        assert (response.getStatusCode() == HttpStatus.OK);

        verify(saleRepository, times(1)).updateTotalValueToZero();
    }

    @Test
    void testCreateSaleValidSellerAndCustomer() {
        Sale newSale = new Sale();
        Seller seller = new Seller();
        newSale.setSeller(seller);
        Customer customer = new Customer();
        newSale.setCustomer(customer);

        when(sellerRepository.findById(newSale.getSeller().getId())).thenReturn(Optional.of(seller));
        when(customerRepository.findById(newSale.getCustomer().getId())).thenReturn(Optional.of(customer));

        ResponseEntity<Object> response = saleController.createSale(newSale);

        assert (response.getStatusCode() == HttpStatus.CREATED);
        assert (response.getBody().equals("Venda criada."));

        verify(saleRepository, times(1)).save(newSale);
    }

    @Test
    void testCreateSaleInvalidSellerNotFound() {
        Sale newSale = new Sale();
        Seller seller = new Seller();
        newSale.setSeller(seller);
        Customer customer = new Customer();
        newSale.setCustomer(customer);

        Optional<Seller> sellerOptional = Optional.empty();

        when(sellerRepository.findById(newSale.getSeller().getId())).thenReturn(sellerOptional);
        when(customerRepository.findById(newSale.getCustomer().getId())).thenReturn(Optional.of(customer));

        ResponseEntity<Object> response = saleController.createSale(newSale);

        assert (response.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert (response.getBody().equals("Vendedor não encontrado."));

        verify(saleRepository, never()).save(newSale);
    }


    @Test
    void testCreateSaleInvalidCustomer() {
        Sale newSale = new Sale();
        Seller seller = new Seller();
        newSale.setSeller(seller);
        Customer customer = new Customer();
        newSale.setCustomer(customer);

        Optional<Customer> customerOptional = Optional.empty();

        when(sellerRepository.findById(newSale.getSeller().getId())).thenReturn(Optional.of(seller));
        when(customerRepository.findById(newSale.getCustomer().getId())).thenReturn(customerOptional);

        ResponseEntity<Object> response = saleController.createSale(newSale);

        assert (response.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert (response.getBody().equals("Cliente não encontrado."));

        verify(saleRepository, never()).save(newSale);
    }

    @Test
    void testGetSaleByIdFound() {
        Long id = 1L;
        Sale sale = new Sale();
        sale.setId(id);

        when(saleRepository.findById(id)).thenReturn(Optional.of(sale));

        ResponseEntity<Object> response = saleController.getSaleById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sale, response.getBody());

        verify(saleRepository, times(1)).findById(id);
    }

    @Test
    void testGetSaleByIdNotFound() {
        Long id = 1L;

        when(saleRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = saleController.getSaleById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Venda com ID " + id + " não encontrada.", response.getBody());

        verify(saleRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteSaleSuccess() {
        Long id = 1L;

        doNothing().when(saleRepository).deleteById(id);

        ResponseEntity<String> response = saleController.deleteSale(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Venda com ID " + id + " foi deletada com sucesso.", response.getBody());

        verify(saleRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdateSaleSuccess() {
        Long id = 1L;

        Seller existingSeller = new Seller();
        existingSeller.setId(1L);
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);

        Sale existingSale = new Sale();
        existingSale.setId(id);
        existingSale.setSeller(existingSeller);
        existingSale.setCustomer(existingCustomer);

        Sale updatedSale = new Sale();
        updatedSale.setId(id);
        updatedSale.setSeller(existingSeller);
        updatedSale.setCustomer(existingCustomer);

        when(saleRepository.findById(id)).thenReturn(Optional.of(existingSale));
        when(sellerRepository.findById(updatedSale.getSeller().getId())).thenReturn(Optional.of(existingSeller));
        when(customerRepository.findById(updatedSale.getCustomer().getId())).thenReturn(Optional.of(existingCustomer));
        when(saleRepository.save(existingSale)).thenReturn(existingSale);

        ResponseEntity<Sale> response = saleController.updateSale(id, updatedSale);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingSale, response.getBody());

        verify(saleRepository, times(1)).findById(id);
        verify(sellerRepository, times(1)).findById(updatedSale.getSeller().getId());
        verify(customerRepository, times(1)).findById(updatedSale.getCustomer().getId());
        verify(saleRepository, times(1)).save(existingSale);
    }

    @Test
    void testUpdateSaleSaleNotFound() {
        Long id = 1L;
        Sale updatedSale = new Sale();
        updatedSale.setId(id);

        Seller seller = new Seller();
        seller.setId(1L);

        Customer customer = new Customer();
        customer.setId(2L);

        updatedSale.setSeller(seller);
        updatedSale.setCustomer(customer);

        when(saleRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Sale> response = saleController.updateSale(id, updatedSale);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(saleRepository, times(1)).findById(id);
        verify(sellerRepository, never()).findById(updatedSale.getSeller().getId());
        verify(customerRepository, never()).findById(updatedSale.getCustomer().getId());
        verify(saleRepository, never()).save(updatedSale);
    }

    @Test
    void testUpdateSaleSellerNotFound() {
        Long id = 1L;
        Sale existingSale = new Sale();
        existingSale.setId(id);

        Sale updatedSale = new Sale();
        updatedSale.setId(id);

        Seller seller = new Seller();
        seller.setId(1L);

        Customer customer = new Customer();
        customer.setId(2L);

        updatedSale.setSeller(seller);
        updatedSale.setCustomer(customer);

        when(saleRepository.findById(id)).thenReturn(Optional.of(existingSale));
        when(sellerRepository.findById(updatedSale.getSeller().getId())).thenReturn(Optional.empty());

        ResponseEntity<Sale> response = saleController.updateSale(id, updatedSale);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(saleRepository, times(1)).findById(id);
        verify(sellerRepository, times(1)).findById(updatedSale.getSeller().getId());
        verify(customerRepository, never()).findById(updatedSale.getCustomer().getId());
        verify(saleRepository, never()).save(updatedSale);
    }

    @Test
    void testUpdateSaleCustomerNotFound() {
        Long id = 1L;

        Sale existingSale = new Sale();
        existingSale.setId(id);

        Sale updatedSale = new Sale();
        updatedSale.setId(id);

        Seller existingSeller = new Seller();
        existingSeller.setId(1L);

        Customer existingCustomer = new Customer();
        existingCustomer.setId(2L);


        updatedSale.setSeller(existingSeller);
        updatedSale.setCustomer(existingCustomer);

        when(saleRepository.findById(id)).thenReturn(Optional.of(existingSale));
        when(sellerRepository.findById(updatedSale.getSeller().getId())).thenReturn(Optional.of(existingSeller));
        when(customerRepository.findById(updatedSale.getCustomer().getId())).thenReturn(Optional.empty());

        ResponseEntity<Sale> response = saleController.updateSale(id, updatedSale);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(saleRepository, times(1)).findById(id);
        verify(sellerRepository, times(1)).findById(updatedSale.getSeller().getId());
        verify(customerRepository, times(1)).findById(updatedSale.getCustomer().getId());
        verify(saleRepository, never()).save(updatedSale);
    }
}
