# API de Ingressos — Alta Demanda

API REST desenvolvida em Java e Spring Boot para simular um sistema de venda e reserva de ingressos, com controle de estoque, autenticação de usuários e proteção contra problemas de concorrência.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Security
- JWT (autenticação por token)
- Spring Data JPA
- PostgreSQL
- Maven
- Docker e Docker Compose

## Funcionalidades

- Cadastro de usuários.
- Login com autenticação JWT.
- Criação de pedidos.
- Reserva de ingressos.
- Cancelamento de reservas.
- Limite de 2 reservas ativas por usuário.
- Controle de estoque com capacidade total de 100 ingressos.
- Consulta de ingressos disponíveis e ocupados.
- Proteção contra reservas simultâneas acima da capacidade.

## Como executar o projeto

### Requisitos

- Docker Desktop instalado.
- Git instalado.

### Inicialização

Na pasta principal do projeto, execute:

```bash
docker compose down -v
docker compose up --build -d
```

A API ficará disponível em:

`http://localhost:8080`

Para verificar os containers:

```bash
docker compose ps
```

Para visualizar os logs da API:

```bash
docker compose logs -f api
```

## Endpoints

### Autenticação

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/registrar` | Cadastrar usuário |
| POST | `/auth/login` | Realizar login e obter token |

### Pedidos

As rotas de pedidos exigem autenticação por token JWT no cabeçalho:

`Authorization: Bearer SEU_TOKEN`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/pedidos` | Criar pedido |
| POST | `/pedidos/{id}/reservar` | Reservar ingresso |
| POST | `/pedidos/{id}/cancelar` | Cancelar reserva |
| GET | `/pool` | Consultar estoque |

## Regras de negócio

- O sistema possui 100 ingressos disponíveis inicialmente.
- Cada usuário pode ter no máximo 2 pedidos com status `RESERVADO`.
- Um pedido começa com status `NO_CARRINHO`.
- Após a reserva, passa para `RESERVADO`.
- Após o cancelamento, passa para `CANCELADO`.
- Pedidos cancelados deixam de ocupar o estoque.

## Controle de concorrência

Para evitar que duas requisições reservem o último ingresso ao mesmo tempo, o sistema utiliza transações com `@Transactional` e bloqueio pessimista (`PESSIMISTIC_WRITE`) no registro de controle do estoque.

Esse bloqueio garante que as operações de reserva e cancelamento sejam processadas de forma sincronizada, evitando que o estoque ultrapasse o limite de 100 ingressos.

Também é realizada a verificação do limite de reservas por usuário dentro da transação.

## Testes

O projeto possui um teste automatizado de concorrência para verificar se duas tentativas simultâneas de reservar o mesmo pedido resultam em apenas uma reserva bem-sucedida.

Para executar os testes:

```bash
./mvnw test
```

No Windows PowerShell:

```powershell
.\mvnw.cmd test
```

## Autor

Desenvolvido como projeto acadêmico para a disciplina/proposta de desenvolvimento de API sob alta demanda.

Atenção: no README, o comando docker compose down -v apaga o volume do banco e, consequentemente, os dados cadastrados. Ele serve para reiniciar o projeto do zero.