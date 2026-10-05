
# Coupon API

API REST para gerenciamento de cupons, desenvolvida como parte de um technical challenge.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Data JPA
- H2
- Maven
- JUnit 5
- Mockito
- JaCoCo
- Swagger / OpenAPI
- Docker
- Docker Compose

## Funcionalidades

- Criar cupom
- Consultar cupom por ID
- Excluir cupom com soft delete
- Validação das regras de negócio
- Normalização do código do cupom

## Endpoints

### Criar cupom

```http
POST /coupon
```

Exemplo:

```json
{
  "code": "ABC-123",
  "description": "Cupom de teste",
  "discountValue": 10,
  "expirationDate": "2027-12-31T23:59:59Z",
  "published": true
}
```

### Consultar cupom

```http
GET /coupon/{id}
```

### Excluir cupom

```http
DELETE /coupon/{id}
```

A exclusão é realizada através de **soft delete**, mantendo os dados do cupom e alterando seu status para `DELETED`.

## Regras de negócio

- O código deve possuir exatamente 6 caracteres alfanuméricos.
- Caracteres especiais são removidos antes da persistência.
- O desconto mínimo é `0.5`.
- A data de expiração não pode estar no passado.
- O campo `published` é opcional e possui `false` como padrão.
- Cupons publicados possuem status `ACTIVE`.
- Cupons não publicados possuem status `INACTIVE`.
- Um cupom excluído possui status `DELETED`.
- Não é permitido excluir um cupom que já foi excluído.

## Executando localmente

```bash
./mvnw clean verify
```

Para executar a aplicação:

```bash
./mvnw spring-boot:run
```

## Swagger

Após iniciar a aplicação:

http://localhost:8080/swagger-ui/index.html

## Docker

Construir a imagem:

```bash
docker build -t coupon-api .
```

Executar:

```bash
docker run -p 8080:8080 coupon-api
```

## Docker Compose

```bash
docker compose up --build
```

Para parar:

```bash
docker compose down
```

## Testes

Os testes cobrem as principais regras de negócio, casos de sucesso, validações, endpoints REST e integração com H2.

Para executar:

```bash
./mvnw clean verify
```

O projeto utiliza JaCoCo para controle de cobertura de código.
