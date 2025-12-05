![Jtech Logo](http://www.jtech.com.br/wp-content/uploads/2015/06/logo.png)

# jtech-tasklist-backend

## Visão Geral

API REST para gerenciamento de tarefas desenvolvida com Spring Boot 3.5.5 e Java 21. A aplicação implementa Clean Code com suporte completo a CRUD, soft delete, paginação e testes automatizados.

### Objetivos
- Fornecer endpoints RESTful para gerenciar tarefas
- Implementar boas práticas de arquitetura e design
- Garantir qualidade através de testes unitários e E2E
- Permitir busca e listagem paginada de tarefas

---

## Stack Utilizada

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5.5 | Framework web |
| Spring Data JPA | 3.5.5 | ORM e persistência |
| Hibernate Validator | 8.0.0.Final | Validação |
| MapStruct | 1.6.3 | Mapeamento de DTOs |
| Lombok | Latest | Redução de boilerplate |
| PostgreSQL | 15-alpine | Banco de dados (produção) |
| H2 Database | Latest | Banco em memória (testes) |
| Flyway | Latest | Versionamento de migrations |
| JUnit 5 | 1.9.2 | Framework de testes |
| TestContainers | 1.19.8 | Testes de integração com containers |
| Mockito | Latest | Mock de dependências |
| AssertJ | 3.24.2 | Assertions em testes |
| SpringDoc OpenAPI | 2.7.0 | Documentação Swagger |

---

## Como Rodar Localmente

### Pré-requisitos
- Java 21+
- Gradle 8.14+
- Docker (para executar PostgreSQL localmente)

### Passos de Configuração

1. Clone o repositório
   ```bash
   git clone <repository-url>
   cd jtech-tasklist-backend
   ```

2. Inicie o PostgreSQL com Docker
   ```bash
   docker-compose -f composer/docker-compose.yml up -d
   ```

3. Configure as variáveis de ambiente (opcional)
   ```properties
   # application.yml já está configurado para localhost
   spring.datasource.url=jdbc:postgresql://localhost:5432/tasklist
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

4. Execute o projeto
   ```bash
   ./gradlew bootRun
   ```

5. Acesse a API
   ```
   http://localhost:8080
   ```

---

## Como Rodar os Testes

### Testes Unitários
```bash
# Executar apenas testes unitários (sem integração nem E2E)
./gradlew unitTest
```

### Testes de Integração
```bash
# Executar apenas testes de integração (usa TestContainers e PostgreSQL)
./gradlew integrationTest
```

### Todos os Testes
```bash
# Executar todos os testes
./gradlew test
```

### Build Completo
```bash
# Compilar sem executar testes
./gradlew build -x test

# Build completo com testes
./gradlew clean build
```

### Relatório de Testes
```bash
# Executar com relatório
./gradlew test --info

# Abrir relatório HTML
open build/reports/tests/test/index.html
```

---

## Estrutura de Pastas

```
src/
├── main/
│   ├── java/br/com/jtech/tasklist/
│   │   ├── adapters/          # Adaptadores (Controllers, Repositories)
│   │   │   ├── input/         # Entrada (Controllers)
│   │   │   │   ├── controllers/
│   │   │   │   ├── dto/
│   │   │   │   ├── facades/   # Facade para orquestração
│   │   │   │   └── mapper/
│   │   │   └── output/        # Saída (Persistência)
│   │   │       ├── persistence/
│   │   │       │   ├── entities/
│   │   │       │   └── repositories/
│   │   │       └── mapper/
│   │   ├── application/       # Núcleo da aplicação
│   │   │   ├── core/
│   │   │   │   ├── domains/   # Entidades de domínio
│   │   │   │   ├── usecases/  # Casos de uso
│   │   │   │   └── exceptions/
│   │   │   ├── dto/           # DTOs (Input/Output)
│   │   │   └── ports/         # Interfaces (In/Out)
│   │   └── config/            # Configurações
│   └── resources/
│       ├── application.yml    # Configuração
│       └── db.migration/      # Scripts SQL (Flyway)
│
└── test/
    ├── java/br/com/jtech/tasklist/
    │   ├── application/core/usecases/    # Testes unitários
    │   ├── adapters/input/controllers/   # Testes E2E
    │   └── adapters/input/facades/       # Testes de integração
    └── resources/
        └── application-test.properties
```

---

## Decisões Técnicas

### Arquitetura Clean Code
- Separação clara entre camadas: Entities, Use Cases, Interface Adapters
- Independência de frameworks e bibliotecas externas
- Facilita testes e manutenção do código
- Benefício: Alta coesão, baixo acoplamento

### PostgreSQL em Produção
- Banco de dados robusto, confiável e escalável
- Suporte nativo a JSON, arrays e tipos complexos
- Excelente performance e replicação

### H2 para Testes de Integração
- Banco em memória, rápido e sem dependências externas
- Facilita CI/CD sem necessidade de containers
- Compatível com SQL padrão usado em produção

### Spring Data JPA
- Abstração de banco sem boilerplate SQL
- Queries type-safe com Specification
- Suporte automático a paginação e sorting

### MapStruct para DTOs
- Geração de código em compile-time (zero overhead)
- Alternativa mais rápida ao ModelMapper
- Fácil manutenção e debugging

### Soft Delete
- Preserva histórico e referências sem remover dados
- Implementação: campo `deleted` boolean com queries filtradas
- Auditoria implícita dos dados

### Paginação com Spring Data Page
- Interface padrão do Spring
- Suporte automático a Pageable (page, size, sort)
- Integração direta com Spring MVC

### Flyway para Migrations
- Versionamento de schema
- Controle de versão do banco de dados
- Rollback automático em caso de erro

### TestContainers
- Testes E2E com banco real em Docker
- Isolamento entre testes
- Segurança: não impacta banco de desenvolvimento

---

## Endpoints da API

### Tarefas
```bash
# Criar tarefa
POST /api/v1/tasks
Content-Type: application/json
{
  "name": "Minha tarefa",
  "description": "Descrição",
  "status": "PENDING"
}

# Listar tarefas com paginação
GET /api/v1/tasks?page=0&size=20&sort=createdAt,desc

# Buscar tarefa por ID
GET /api/v1/tasks/{id}

# Atualizar tarefa
PUT /api/v1/tasks/{id}
Content-Type: application/json
{
  "name": "Tarefa atualizada",
  "description": "Nova descrição",
  "status": "IN_PROGRESS"
}

# Deletar tarefa (soft delete)
DELETE /api/v1/tasks/{id}
```

### Status disponíveis
- PENDING - Pendente
- IN_PROGRESS - Em progresso
- COMPLETED - Completada

---

## Testes

### Cobertura
- Testes Unitários: 60+ testes
- Testes E2E: 11 testes no Controller
- Taxa de sucesso: 100%

### Categorias de Teste
1. Unitários - Lógica de domínio e casos de uso
2. Integração - Facade com repositório
3. E2E - Controller com banco real

---

## Melhorias Futuras

### Curto Prazo
- Autenticação e autorização (JWT/OAuth2)
- Filtros avançados (status, data de criação)
- Busca por texto (name/description)
- Bulk operations (criar/atualizar múltiplos)

### Médio Prazo
- Categorias/Tags para tarefas
- Atribuição a usuários
- Prioridades
- Datas de vencimento
- Notificações por email

### Longo Prazo
- Comentários em tarefas
- Histórico de alterações (audit)
- Sincronização em tempo real (WebSocket)
- Mobile app (Flutter/React Native)
- Relatórios e dashboards

---

## Troubleshooting

### PostgreSQL não conecta
```bash
# Verificar se container está rodando
docker ps | grep postgres

# Reiniciar container
docker-compose -f composer/docker-compose.yml restart
```

### Build falha
```bash
# Limpar cache Gradle
./gradlew clean

# Reconectar dependencies
./gradlew --refresh-dependencies
```

### Testes falhando
```bash
# Executar com mais verbosidade
./gradlew test --info

# Executar teste específico
./gradlew test --tests "TaskControllerE2ETest"
```

---

## Licença

Propriedade J-Tech Soluções em Informática

---

## Desenvolvimento

Framework: Spring Boot 3.5.5
Java Version: 21
Build Tool: Gradle
Arquitetura: Clean Code