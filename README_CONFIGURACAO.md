# README_CONFIGURACAO

## Descrição do Projeto

Este projeto é uma aplicação backend desenvolvida em **Spring Boot** para gerenciamento de funcionários e filmes, implementando operações CRUD (Create, Read, Update, Delete) completas. O projeto inclui funcionalidades de busca, validações, tratamento de erros, paginação, documentação da API com Swagger/OpenAPI e cobertura de testes abrangente. É um exercício acadêmico focado em boas práticas de desenvolvimento backend com Java.

## Tecnologias Utilizadas

### Linguagem e Framework Principal
- **Java 17**: Linguagem de programação utilizada.
- **Spring Boot 3.5.10**: Framework para desenvolvimento de aplicações Java, facilitando a configuração e execução.
- **Spring Data JPA**: Para interação com o banco de dados relacional, abstraindo operações CRUD.
- **Spring Web**: Para criação de APIs RESTful.
- **Spring Validation**: Para validações de entrada de dados usando Bean Validation (Jakarta Validation).

### Banco de Dados
- **H2 Database**: Banco de dados em memória para desenvolvimento e testes (configurado em `application.properties`).
- **PostgreSQL**: Banco de dados para testes de integração (via Testcontainers).
- **Hibernate**: ORM (Object-Relational Mapping) para mapeamento objeto-relacional.

### Testes
- **JUnit 5**: Framework para testes unitários e de integração.
- **Mockito**: Para criação de mocks em testes unitários.
- **Spring Boot Test**: Para testes de contexto Spring.
- **Testcontainers**: Para testes com containers reais (PostgreSQL).
- **JaCoCo**: Para cobertura de código (relatórios em `target/site/jacoco/index.html`).

### Documentação e Ferramentas de Desenvolvimento
- **SpringDoc OpenAPI (Swagger)**: Para documentação automática da API REST (acessível em `/swagger-ui.html`).
- **Lombok**: Para redução de código boilerplate (getters, setters, constructors).
- **Maven**: Gerenciador de dependências e build.
- **SLF4J + Logback**: Para logging estruturado.

### Outras Dependências
- **Jakarta Validation**: Para anotações de validação (@NotBlank, etc.).
- **Jackson**: Para serialização/deserialização JSON.
- **H2 Console**: Interface web para o banco H2 (acessível em `/h2`).

## Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 17** ou superior (JDK).
- **Maven 3.6+** para gerenciamento de dependências e build.
- **Git** para clonagem do repositório (opcional).
- **IDE** recomendada: IntelliJ IDEA, Eclipse ou VS Code com extensões Java.

## Configurações do Projeto

### Arquivo `pom.xml`
O arquivo `pom.xml` define as dependências e configurações do Maven:

- **Java Version**: 17
- **Spring Boot Version**: 3.5.10
- **Dependências Principais**:
  - `spring-boot-starter-web`: Para APIs REST.
  - `spring-boot-starter-data-jpa`: Para JPA.
  - `spring-boot-starter-validation`: Para validações.
  - `h2`: Banco de dados H2.
  - `springdoc-openapi-starter-webmvc-ui`: Para Swagger.
  - `lombok`: Para redução de código.
  - `spring-boot-starter-test`: Para testes.
  - `testcontainers`: Para testes com containers.
  - `jacoco-maven-plugin`: Para cobertura de código.

### Arquivo `application.properties`
Configurações principais em `src/main/resources/application.properties`:

```properties
# Banco de dados H2
spring.datasource.url=jdbc:h2:file:~/teckback20262
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.br.uniesp.si.techback=DEBUG
logging.level.org.springframework.web=DEBUG

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2

# Swagger/OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Arquivo `application-test.properties`
Configurações específicas para testes em `src/test/resources/application-test.properties`:

```properties
# Banco de dados PostgreSQL via Testcontainers
spring.datasource.url=jdbc:tc:postgresql:15-alpine:///testdb
spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver

# JPA
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=create-drop

# Logging reduzido para testes
logging.level.br.uniesp.si.techback=INFO
```

### Estrutura do Projeto
```
tecback/
├── src/
│   ├── main/
│   │   ├── java/br/uniesp/si/techback/
│   │   │   ├── controller/          # Controladores REST (FuncionarioController, FilmeController, GlobalExceptionHandler)
│   │   │   ├── dto/                 # Data Transfer Objects (FuncionarioDTO, FilmeDTO)
│   │   │   ├── mapper/              # Mappers manuais (FuncionarioMapper, FilmeMapper)
│   │   │   ├── model/               # Entidades JPA (Funcionario, Filme)
│   │   │   ├── repository/          # Repositórios (FuncionarioRepository, FilmeRepository)
│   │   │   ├── service/             # Serviços de negócio (FuncionarioService, FilmeService)
│   │   │   └── TechbackApplication.java  # Classe principal
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/br/uniesp/si/techback/
│       │   ├── controller/          # Testes de controladores
│       │   ├── mapper/              # Testes de mappers
│       │   ├── repository/          # Testes de repositórios
│       │   └── service/             # Testes de serviços
│       └── resources/
│           └── application-test.properties
├── target/                          # Arquivos compilados e relatórios
├── pom.xml                          # Configuração Maven
└── README_CONFIGURACAO.md           # Este arquivo
```

## Como Executar o Projeto

### 1. Clonagem e Build
```bash
git clone <url-do-repositorio>
cd tecback
mvn clean install
```

### 2. Execução
```bash
mvn spring-boot:run
```
A aplicação estará disponível em `http://localhost:8080`.

### 3. Acesso aos Recursos
- **API Base**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **H2 Console**: `http://localhost:8080/h2` (usuário: `sa`, senha: vazia)

## Endpoints da API

### Funcionários (`/funcionarios`)
- `GET /funcionarios`: Lista todos os funcionários.
- `GET /funcionarios/paginado`: Lista funcionários paginados.
- `GET /funcionarios/{id}`: Busca funcionário por ID.
- `GET /funcionarios/search?nome={termo}`: Busca por nome.
- `POST /funcionarios`: Cria novo funcionário.
- `PUT /funcionarios/{id}`: Atualiza funcionário.
- `DELETE /funcionarios/{id}`: Exclui funcionário.

### Filmes (`/filmes`)
- `GET /filmes`: Lista todos os filmes.
- `GET /filmes/{id}`: Busca filme por ID.
- `GET /filmes/search?titulo={termo}`: Busca por título.
- `POST /filmes`: Cria novo filme.
- `PUT /filmes/{id}`: Atualiza filme.
- `DELETE /filmes/{id}`: Exclui filme.

## Como Executar os Testes

### Testes Unitários e de Integração
```bash
mvn test
```
- **Cobertura**: Relatório em `target/site/jacoco/index.html`.

### Testes com Testcontainers
Os testes de repositório usam PostgreSQL em containers. Certifique-se de ter Docker instalado.

## Validações e Tratamento de Erros

- **Validações**: Campos obrigatórios (@NotBlank) em DTOs.
- **Erros**: Tratamento global via `GlobalExceptionHandler` (retorna JSON padronizado com status HTTP adequados).
- **Logs**: Níveis INFO, DEBUG e ERROR para monitoramento.

## Boas Práticas Implementadas

- **Arquitetura em Camadas**: Controller → Service → Repository.
- **DTO Pattern**: Separação entre entidades e contratos da API.
- **Mapper Manual**: Conversão entre entidades e DTOs.
- **Transações**: @Transactional para operações de escrita.
- **Paginação**: Suporte a Pageable em listagens.
- **Testes Abrangentes**: Unitários, integração e cobertura alta.

## Contribuição

Este é um projeto acadêmico. Para contribuições, siga os padrões de código e adicione testes para novas funcionalidades.

## Licença

Este projeto é para fins educacionais e não possui licença específica.
