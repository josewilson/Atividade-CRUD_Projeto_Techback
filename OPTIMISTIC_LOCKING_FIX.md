# Fix para Erro de Optimistic Locking (Status 400)

## Problema Identificado

O erro "Row was updated or deleted by another transaction" é um erro de **optimistic locking** no Hibernate/JPA. Ocorre quando:

1. Uma entidade é carregada em uma transação
2. Outra transação modifica/deleta a mesma entidade
3. A primeira transação tenta atualizar, mas encontra a linha modificada (versão não corresponde)

### Mensagem de Erro Original:
```
{
  "error": "Bad Request",
  "message": "Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [br.uniesp.si.techback.model.Usuario#9007199254740991]",
  "status": 400
}
```

## Raiz do Problema

1. **Campo @Version mal inicializado**: A anotação `@Default` do Lombok com inicialização inline não funciona corretamente com Hibernate
2. **Dados iniciais sem versão**: O arquivo `data.sql` não estava inserindo valores para o campo `version`
3. **Falta de transaction management**: O service não tinha anotação `@Transactional`
4. **Sem handler para exceções de concorrência**: Não havia tratamento específico para `ObjectOptimisticLockingFailureException`

## Alterações Realizadas

### 1. **Modelo Usuario.java** 
- Removido `@Default` e inicialização inline do campo `@Version`
- Mantém apenas a anotação `@Version` padrão, deixando o Hibernate gerenciar

```java
@Version
private Long version;
```

### 2. **Service - UsuarioService.java**
- Adicionado `@Transactional` a nível de classe
- Adicionado `@Transactional(readOnly = true)` aos métodos `listar()` e `buscarPorId()`
- Garantido que o campo `version` é inicializado ao converter DTO para entidade

### 3. **Dados Iniciais - data.sql**
- Atualizado INSERT para incluir valor inicial para `version`

```sql
INSERT INTO usuarios (username, password, email, version) 
VALUES ('admin', '$2a$10$...', 'admin@example.com', 0);
```

### 4. **Exception Handler - GlobalExceptionHandler.java**
- Adicionado handler específico para `ObjectOptimisticLockingFailureException`
- Retorna erro HTTP 409 (Conflict) com mensagem clara ao usuário

```java
@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
public ResponseEntity<Map<String, Object>> handleOptimisticLockingException(
    ObjectOptimisticLockingFailureException ex) {
    // Retorna 409 Conflict com mensagem amigável
}
```

## Como Funciona o Optimistic Locking

1. Quando uma entidade é carregada, o Hibernate registra o valor do `version`
2. Ao atualizar, o SQL gerado inclui `WHERE version = <version_carregada>`
3. Se o `version` mudou (outra transação atualizou), a atualização falha
4. Isso evita sobrescrita de dados acidental em atualizações concorrentes

## Benefícios da Solução

✅ Previne perda de dados em atualizações concorrentes  
✅ Mensagens de erro claras para o usuário  
✅ Melhor performance com `readOnly = true` em queries  
✅ Gerenciamento automático de transações  

## Testando a Solução

1. Limpe o banco: Delete os arquivos H2 em `~/teckback20262*`
2. Reinicie a aplicação
3. Agora os dados serão inseridos com `version = 0`
4. Tente atualizar um usuário - deve funcionar sem erro 400

## Referências

- [Spring Data JPA Optimistic Locking](https://docs.spring.io/spring-data/jpa/reference/)
- [Hibernate @Version Annotation](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#optimistic-locking)

