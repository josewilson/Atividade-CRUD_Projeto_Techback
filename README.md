# 🎬 Techback

API REST em **Spring Boot** com CRUD de Filmes, Funcionários e Usuários, autenticação **JWT** e controle de concorrência por **lock otimista**.
Projeto da disciplina *Tecnologias para Backend (Spring Boot)* — UNIESP, sobre a base do Prof. Rodrigo Fujioka.

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![H2](https://img.shields.io/badge/Database-H2-1021FF)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![Tests](https://img.shields.io/badge/tests-96%20passing-brightgreen)
![OpenAPI](https://img.shields.io/badge/OpenAPI-Swagger%20UI-85EA2D?logo=swagger&logoColor=black)

## ✨ Funcionalidades

- CRUD completo de **Filmes**, **Funcionários** e **Usuários**
- Autenticação **stateless com JWT** (`/auth/login`) e senhas com **BCrypt**
- **Lock otimista** (`@Version`) com resposta `409 Conflict` em edições concorrentes
- Validação de entrada (Bean Validation) e tratamento global de erros
- Documentação interativa via **Swagger UI**
- Testes de controller, service, repository e mapper

## 🏗️ Arquitetura

```mermaid
flowchart LR
    C[Cliente] -->|HTTP + Bearer JWT| F[AuthTokenFilter]
    F --> SC[SecurityConfig]
    SC --> CT[Controllers]
    CT -->|DTO| SV[Services]
    SV --> MP[Mappers]
    SV --> RP[Repositories]
    RP -->|JPA/Hibernate| DB[(H2)]
    CT -. erros .-> EH[GlobalExceptionHandler]
    AC[AuthController] --> JU[JwtUtils]
    F --> JU
    F --> UD[UserDetailsServiceImpl] --> RP
```

**Fluxo de autenticação**

```mermaid
sequenceDiagram
    participant C as Cliente
    participant A as AuthController
    participant J as JwtUtils
    participant F as AuthTokenFilter
    participant R as Recurso protegido
    C->>A: POST /auth/login (username, password)
    A->>J: gera token (HS256)
    A-->>C: 200 {token}
    C->>F: GET /filmes (Authorization: Bearer token)
    F->>J: valida token
    F->>R: SecurityContext preenchido
    R-->>C: 200 / 401
```

### Estrutura de pastas

```
src/main/java/br/uniesp/si/techback/
├── controller/   # REST: Auth, Filme, Funcionario, Usuario + GlobalExceptionHandler
├── service/      # Regras de negócio + UserDetailsServiceImpl
├── repository/   # Spring Data JPA
├── model/        # Entidades JPA (Usuario, Filme, Funcionario, ...)
├── dto/          # Objetos de transferência e validação
├── mapper/       # Entity <-> DTO
├── security/     # JwtUtils, AuthTokenFilter, SecurityConfig
└── TechbackApplication.java
src/test/java/... # controller, service, repository, mapper
```

## 🚀 Como executar

Requisitos: **JDK 21** (o Maven Wrapper já vem no projeto).

```bash
# 1. Defina o segredo JWT (mínimo 32 caracteres) — nunca versione este valor
export JWT_SECRET="$(openssl rand -base64 48)"

# 2. Suba a aplicação
./mvnw spring-boot:run
```

No PowerShell: `$env:JWT_SECRET = "<segredo>"; .\mvnw spring-boot:run`

| Variável | Obrigatória | Padrão | Descrição |
|---|---|---|---|
| `JWT_SECRET` | ✅ | — | Chave de assinatura HS256 |
| `JWT_EXPIRATION_MS` | ❌ | `3600000` | Validade do token (1 h) |
| `H2_CONSOLE_ENABLED` | ❌ | `false` | Habilita o console H2 em `/h2/login` |

Swagger UI: <http://localhost:8080/swagger-ui/index.html>

## 🔐 Endpoints

| Método | Rota | Acesso |
|---|---|---|
| POST | `/auth/login` | Público |
| POST | `/usuarios` | Público (cadastro) |
| GET/PUT/DELETE | `/usuarios/**` | Autenticado |
| CRUD | `/filmes`, `/funcionarios` | Autenticado |

```bash
TOKEN=$(curl -s -X POST localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"<senha>"}' | jq -r .token)

curl -H "Authorization: Bearer $TOKEN" localhost:8080/filmes
```

## 🧪 Testes

```bash
./mvnw test
```

Relatório de cobertura (JaCoCo) em `target/site/jacoco/index.html`.

## 🔒 Segurança

- Segredo JWT somente via variável de ambiente
- Rotas de usuários protegidas; senha nunca é retornada nas respostas
- Console H2 desligado por padrão
- Troque a senha do usuário `admin` do `data.sql` antes de qualquer deploy

## 👥 Integrantes

Veja [INTEGRANTES.md](INTEGRANTES.md).
