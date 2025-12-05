# Desafio Técnico Fullstack 1 - JTech

## API RESTful para Gerenciamento de Tarefas

### Contextualização e Objetivo

A **JTech** busca identificar profissionais que demonstrem sólido conhecimento nos fundamentos do desenvolvimento backend. Este desafio técnico foi elaborado para avaliar suas competências na construção de APIs RESTful utilizando Java e Spring Boot.

**Objetivo:** Desenvolver uma API completa para gerenciamento de tarefas (TODO List), aplicando boas práticas de desenvolvimento, arquitetura limpa e documentação técnica de qualidade.

## Especificações Técnicas

### Requisitos Funcionais

1. **Criar Tarefa**: Endpoint `POST /tasks` para adicionar uma nova tarefa. A tarefa deve conter título, descrição e status (ex: "pendente", "concluída").
2. **Listar Tarefas**: Endpoint `GET /tasks` para retornar todas as tarefas cadastradas.
3. **Buscar Tarefa por ID**: Endpoint `GET /tasks/{id}` para obter os detalhes de uma tarefa específica.
4. **Atualizar Tarefa**: Endpoint `PUT /tasks/{id}` para atualizar o título, a descrição ou o status de uma tarefa.
5. **Deletar Tarefa**: Endpoint `DELETE /tasks/{id}` para remover uma tarefa do sistema.

### Requisitos Não Funcionais

1. **Persistência de Dados**: As tarefas devem ser armazenadas em banco de dados. Recomenda-se H2 (em memória) para simplificação ou PostgreSQL para demonstrar conhecimento em bancos relacionais.
2. **Validação de Dados**: Implementar validação robusta das entradas do usuário (ex: título da tarefa obrigatório e não vazio).
3. **Tratamento de Erros**: A API deve retornar códigos de status HTTP apropriados e mensagens de erro claras (ex: 404 para tarefa não encontrada, 400 para dados inválidos).

### Stack Tecnológica Obrigatória

* **Linguagem**: Java
* **Framework**: Spring Boot
* **Persistência**: Spring Data JPA com Hibernate
* **Banco de Dados**: H2 (em memória) ou PostgreSQL
* **Testes**: Testes unitários com JUnit/Mockito.

## Critérios de Avaliação

* **Qualidade e Organização do Código**: Código limpo, legível e seguindo as convenções do Java.
* **Aplicação de Boas Práticas**: Utilização de princípios como Clean Code e KISS.
* **Funcionalidade**: Todos os endpoints devem funcionar conforme especificado.
* **Testes Automatizados**: Cobertura de testes unitários para as classes de serviço e controllers.
* **Uso Adequado da Stack**: Configuração correta do Spring Boot, JPA e do banco de dados.
* **Modelagem de Dados**: Estrutura da entidade `Task` bem definida.
* **Controle de Versão**: Commits claros e lógicos no Git.

## Expectativa de Entrega

* **Prazo**: Até 3 dias corridos a partir do recebimento.
* **Formato**: Entregar o código-fonte em um repositório Git, acompanhado de um `README.md` completo.

### Estrutura Obrigatória do `README.md`

1. **Visão Geral do Projeto**: Breve descrição da API e seus objetivos.
2. **Stack Utilizada**: Lista das tecnologias implementadas.
3. **Como Rodar Localmente**: Instruções para configurar o ambiente, instalar dependências e iniciar o servidor.
4. **Como Rodar os Testes**: Comando para executar os testes.
5. **Estrutura de Pastas**: Explicação da organização do projeto.
6. **Decisões Técnicas**: Justificativas para as escolhas feitas (ex: por que usou H2 em vez de PostgreSQL).
7. **Melhorias Futuras**: Sugestões para evoluir a API.

---

**Boa sorte! A JTech está ansiosa para conhecer sua solução.**

---

## 📚 Documentação dos Projetos

Este repositório contém a solução completa do desafio, dividida em frontend e backend:

### Backend (API REST)
📖 **[Documentação Completa do Backend →](./jtech-tasklist-backend/README.md)**

Stack: Java 21 + Spring Boot 3.5.5 + PostgreSQL + Flyway + JUnit

### Frontend (Interface Web)
📖 **[Documentação Completa do Frontend →](./jtech-tasklist-frontend/README.md)**

Stack: Vue 3 + TypeScript + Tailwind CSS + Pinia + Vite

---

## 🚀 Como Rodar o Projeto Completo

### Opção 1: Docker Compose (Recomendado)

Rodar toda a stack (PostgreSQL + Backend + Frontend) com um único comando:

```bash
# Na raiz do projeto
docker-compose up -d
```

**Acessos:**
- Frontend: http://localhost
- Backend API: http://localhost:8081
- Swagger UI: http://localhost:8081/swagger-ui.html

**Parar os serviços:**
```bash
docker-compose down
```

**Reconstruir as imagens:**
```bash
docker-compose up -d --build
```

### Opção 2: Execução Local (Desenvolvimento)

#### 1. Inicie o Backend

```bash
# Terminal 1 - Backend
cd jtech-tasklist-backend

# Suba o PostgreSQL
docker-compose -f composer/docker-compose.yml up -d

# Execute a aplicação
./gradlew bootRun
```

Backend disponível em: http://localhost:8081

#### 2. Inicie o Frontend

```bash
# Terminal 2 - Frontend
cd jtech-tasklist-frontend

# Instale as dependências (primeira vez)
npm install

# Crie o arquivo .env.local
echo "VITE_API_BASE_URL=http://localhost:8081/api/v1" > .env.local

# Execute em modo desenvolvimento
npm run dev
```

Frontend disponível em: http://localhost:5173

---

## 🏗️ Arquitetura da Solução

```
fullstack1/
├── jtech-tasklist-backend/      # API REST
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/            # Código-fonte Java
│   │   │   └── resources/       # Configurações e migrations
│   │   └── test/                # Testes unitários e E2E
│   ├── composer/                # Docker Compose configs
│   └── README.md                # 📖 Documentação do Backend
│
├── jtech-tasklist-frontend/     # Interface Web
│   ├── src/
│   │   ├── components/          # Componentes Vue
│   │   ├── views/               # Páginas
│   │   ├── stores/              # Pinia stores
│   │   ├── services/            # Integração com API
│   │   └── router/              # Rotas Vue Router
│   ├── docs/                    # Screenshots
│   └── README.md                # 📖 Documentação do Frontend
│
└── docker-compose.yml           # Orquestração completa
```

---

## ✅ Funcionalidades Implementadas

### Backend
- ✅ CRUD completo de tarefas
- ✅ Validação de dados com Bean Validation
- ✅ Soft delete (exclusão lógica)
- ✅ Paginação e ordenação
- ✅ Tratamento global de erros
- ✅ Migrations com Flyway
- ✅ Testes unitários e E2E (96 testes)
- ✅ Documentação Swagger/OpenAPI
- ✅ CORS configurado
- ✅ Docker ready

### Frontend
- ✅ Interface responsiva e moderna
- ✅ CRUD completo com modais
- ✅ Filtros por status (ALL, PENDING, IN_PROGRESS, COMPLETED)
- ✅ Paginação (5 itens por página)
- ✅ Sistema de notificações (Toast)
- ✅ Confirmação de ações destrutivas
- ✅ Tratamento de erros da API
- ✅ Página 404 customizada
- ✅ TypeScript com tipagem forte
- ✅ State management com Pinia
- ✅ Docker ready

---

## 🧪 Executar Testes

### Backend

```bash
cd jtech-tasklist-backend

# Todos os testes (unitários + integração + E2E)
./gradlew test

# Apenas testes unitários
./gradlew unitTest

# Apenas testes de integração
./gradlew integrationTest
```

### Frontend

```bash
cd jtech-tasklist-frontend

# Testes unitários
npm run test:unit

# Type checking
npm run type-check
```

---

## 🔧 Tecnologias Utilizadas

### Backend
- Java 21
- Spring Boot 3.5.5
- Spring Data JPA + Hibernate
- PostgreSQL 15
- Flyway
- MapStruct
- Lombok
- JUnit 5 + Mockito + TestContainers
- Swagger/OpenAPI

### Frontend
- Vue 3.5 (Composition API)
- TypeScript 5.6
- Vite 7.2
- Vue Router 4.5
- Pinia 3.0
- Axios
- Tailwind CSS 4.1
- Google Material Icons

### DevOps
- Docker & Docker Compose
- Multi-stage builds
- Nginx (frontend)
