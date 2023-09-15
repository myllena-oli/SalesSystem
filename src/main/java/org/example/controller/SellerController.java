package org.example.controller;

import org.example.model.Seller;
import org.example.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class SellerController {
    @Autowired
    private SellerRepository sellerRepository;

    @GetMapping
    public ResponseEntity<Object> getAllSellers() {
        List<Seller> sellers = sellerRepository.findAll();

        if (sellers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("A lista de vendedores está vazia.");
        }

        return ResponseEntity.ok(sellers);
    }


    @PostMapping
    public ResponseEntity<Object> createSeller(@RequestBody Seller seller) {
        sellerRepository.save(seller);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vendedor criado.");
    }

    @GetMapping("/sellerSalaries")
    public ResponseEntity<Object> getSellerSalaries() {
        List<Double> salaries = sellerRepository.findAllSalariesOrderBySalaryDesc();

        if (salaries.isEmpty()) {
            String mensagem = "Não há salários para exibir.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
        }

        return ResponseEntity.ok(salaries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSellerById(@PathVariable Long id) {
        Seller seller = sellerRepository.findById(id).orElse(null);

        if (seller != null) {
            return ResponseEntity.ok(seller);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendedor com ID " + id + " não encontrado.");
        }
    }


    @PutMapping("/{id}")
    public Seller updateSeller(@PathVariable Long id, @RequestBody Seller updatedSeller) {
        return sellerRepository.findById(id).map(seller -> {
            seller.setName(updatedSeller.getName());
            seller.setEmail(updatedSeller.getEmail());
            seller.setCpf(updatedSeller.getCpf());
            seller.setSalary(updatedSeller.getSalary());
            return sellerRepository.save(seller);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeller(@PathVariable Long id) {
        sellerRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Vendedor com ID " + id + " foi deletado com sucesso.");

    }
}

