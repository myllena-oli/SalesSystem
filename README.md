# Sales System

# Aplicação de Controle de Clientes, Vendas e Vendedores

Esta é uma aplicação de controle de clientes, vendas e vendedores desenvolvida em Java com o uso do framework Spring Boot. Ela fornece endpoints RESTful para realizar operações relacionadas a clientes, vendas e vendedores, incluindo a criação, recuperação, atualização e exclusão de registros. 
A aplicação também oferece algumas consultas específicas, como a busca de vendas com valor total acima de 10.00, a atualização de valores totais de vendas nulas para zero, ordenação de salário dos vendedores do maior para o menor e pesquisa de quantos clientes tem um email específico.

## Estrutura do Projeto

A aplicação está dividida em três principais pacotes: `org.example.controller`, `org.example.model`, e `org.example.repository`. Cada pacote tem uma função específica no projeto:

### Pacote `org.example.controller`

Este pacote contém classes de controle que definem os endpoints da API REST. Aqui estão as principais classes e suas funcionalidades:

#### `CustomerController`

- Gerencia operações relacionadas a clientes.
- Oferece endpoints para listar todos os clientes, criar, recuperar, atualizar e excluir clientes.
- Possui um endpoint para contar clientes com um determinado email contendo um texto específico.

#### `SaleController`

- Gerencia operações relacionadas a vendas.
- Oferece endpoints para listar todas as vendas, criar, recuperar, atualizar e excluir vendas.
- Possui um endpoint para listar vendas com valores totais acima de 10.00.
- Oferece um endpoint para atualizar o valor total de todas as vendas nulas para zero.

#### `SellerController`

- Gerencia operações relacionadas a vendedores.
- Oferece endpoints para listar todos os vendedores, criar, recuperar, atualizar e excluir vendedores.
- Possui um endpoint para listar os salários de todos os vendedores em ordem decrescente.

### Pacote `org.example.model`

Este pacote contém as classes de modelo que representam os objetos de domínio da aplicação. Cada classe representa um tipo de entidade:

- `Customer`: Representa um cliente com atributos como nome, email, CPF e endereço.
- `Sale`: Representa uma venda com informações sobre o vendedor, cliente, produto, quantidade, preço e valor total da venda.
- `Seller`: Representa um vendedor com atributos como nome, email, CPF e salário.

### Pacote `org.example.repository`

Este pacote contém interfaces de repositório que estendem o `JpaRepository` do Spring Data JPA. As interfaces são responsáveis por fornecer métodos de acesso aos dados no banco de dados para cada entidade:

- `CustomerRepository`: Fornece métodos para acessar dados de clientes, incluindo a contagem de clientes por email.
- `SaleRepository`: Fornece métodos para acessar dados de vendas, incluindo a busca de vendas com valores totais acima de 10.00 e a atualização de valores totais para zero.
- `SellerRepository`: Fornece métodos para acessar dados de vendedores, incluindo a busca por salários em ordem decrescente.

## Executando a Aplicação

Para executar a aplicação, siga os passos abaixo:

1. Certifique-se de ter o Java instalado em sua máquina.
2. Clone este repositório para sua máquina local.
3. Importe o projeto em sua IDE preferida (por exemplo, IntelliJ IDEA ou Eclipse).
4. Certifique-se de que as dependências do projeto sejam baixadas automaticamente pelo Maven.
5. Execute a classe `Main` como uma aplicação Spring Boot.

Após a execução bem-sucedida, a aplicação estará disponível em `http://localhost:8080`. Você pode usar ferramentas como o Postman ou o cURL para interagir com os endpoints da API.

## Exemplos de Uso

Aqui estão alguns exemplos de como usar os endpoints da API:

- Para listar todos os clientes: `GET http://localhost:8080/customers`
- Para criar um novo cliente: `POST http://localhost:8080/customers`
- Para contar clientes por email contendo um texto específico: `POST http://localhost:8080/customers/countByEmailContaining`
- Para listar todas as vendas: `GET http://localhost:8080/sales`
- Para criar uma nova venda: `POST http://localhost:8080/sales`
- Para listar todas as vendas com valores totais acima de 10.00: `GET http://localhost:8080/sales/salesAbove10`
- Para atualizar o valor total de todas as vendas nulas para zero: `PUT http://localhost:8080/sales/updateTotalValueToZero`
- Para listar todos os vendedores: `GET http://localhost:8080/sellers`
- Para criar um novo vendedor: `POST http://localhost:8080/sellers`
- Para listar os salários de todos os vendedores em ordem decrescente: `GET http://localhost:8080/sellers/sellerSalaries`

Certifique-se de fornecer os dados necessários no corpo das solicitações POST e PUT, conforme necessário.

Este é um exemplo básico de uma aplicação de controle de clientes, vendas e vendedores. Você pode estender e personalizar a aplicação de acordo com suas necessidades específicas.
