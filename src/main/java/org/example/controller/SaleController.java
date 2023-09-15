package org.example.controller;

import org.example.model.Customer;
import org.example.model.Sale;
import org.example.model.Seller;
import org.example.repository.CustomerRepository;
import org.example.repository.SaleRepository;
import org.example.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sales")
public class SaleController {
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @GetMapping
    public ResponseEntity<Object> getAllSales() {
        List<Sale> sales = saleRepository.findAll();

        if (sales.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("A lista de vendas está vazia.");
        }

        return ResponseEntity.ok(sales);

    }

    @GetMapping("/salesAbove10")
    public ResponseEntity<Object> getSalesAbove10() {
        List<Sale> salesAbove10 = saleRepository.findByTotalValueGreaterThan(10.00);

        if (salesAbove10.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há vendas com valor total acima de 10.00.");
        }

        return ResponseEntity.ok(salesAbove10);
    }

    @PutMapping("/updateTotalValueToZero")
    public ResponseEntity<Void> updateTotalValueToZero() {
        saleRepository.updateTotalValueToZero();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Object> createSale(@RequestBody Sale sale) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sale.getSeller().getId());
        if (!sellerOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vendedor não encontrado.");
        }

        Optional<Customer> customerOptional = customerRepository.findById(sale.getCustomer().getId());
        if (!customerOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente não encontrado.");
        }

        saleRepository.save(sale);

        return ResponseEntity.status(HttpStatus.CREATED).body("Venda criada.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSaleById(@PathVariable Long id) {
        Sale sale = saleRepository.findById(id).orElse(null);

        if (sale != null) {
            return ResponseEntity.ok(sale);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda com ID " + id + " não encontrada.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody Sale updatedSale) {
        Optional<Sale> saleOptional = saleRepository.findById(id);

        if (saleOptional.isPresent()) {
            Sale sale = saleOptional.get();

            Optional<Seller> vendedorOptional = sellerRepository.findById(updatedSale.getSeller().getId());
            if (vendedorOptional.isPresent()) {
                sale.setSeller(vendedorOptional.get());
            } else {
                return ResponseEntity.notFound().build();
            }

            Optional<Customer> clienteOptional = customerRepository.findById(updatedSale.getCustomer().getId());
            if (clienteOptional.isPresent()) {
                sale.setCustomer(clienteOptional.get());
            } else {
                return ResponseEntity.notFound().build();
            }

            sale.setProductId(updatedSale.getProductId());
            sale.setProductName(updatedSale.getProductName());
            sale.setProductPrice(updatedSale.getProductPrice());
            sale.setProductQuantity(updatedSale.getProductQuantity());
            sale.setTotalValue(updatedSale.getTotalValue());

            Sale updatedSaleEntity = saleRepository.save(sale);
            return ResponseEntity.ok(updatedSaleEntity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable Long id) {
        saleRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Venda com ID " + id + " foi deletada com sucesso.");
    }
}

