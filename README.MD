# 🌐 Sistema Web de Gerenciamento de Biblioteca

Uma **aplicação web full-stack** para gerenciamento de uma biblioteca, composta por:

- Um **front-end responsivo** que consome a API HATEOAS e oferece interfaces de cadastro de usuários, busca e controle de empréstimos.
- Uma **API RESTful** robusta e autodocumentada, construída com **Java 21+**, **Spring Boot 3** e arquitetura inspirada em **Domain-Driven Design (DDD)**.

O projeto foi concebido não apenas para ser funcional, mas também para servir como exemplo de **código limpo**, **baixo acoplamento** e **alta coesão**, facilitando manutenção e evolução contínua.

---

## 🎯 Motivação e Objetivos Técnicos

Mais do que CRUD, este sistema foi uma oportunidade para demonstrar padrões que resolvem problemas de sistemas complexos:

- **UX guiada por HATEOAS:** O **RootEntryPointController** expõe links para todas as operações, e cada recurso inclui no JSON relações que indicam ações possíveis, permitindo que o front-end descubra dinamicamente rotas e estados sem hard-code de URLs.
- **Desacoplamento extremo:**
   - O **front-end** consome a API via hiperlinks; não há dependência direta de rotas no cliente.
   - A **API** segue DDD, mantendo o domínio puro e livre de dependências de framework.
- **Separation of Concerns (SoC):**
   1. **Web UI** (`web/`): HTML, CSS, JavaScript—interfaces que interagem via fetch com os endpoints HATEOAS.
   2. **API Layer** (`api/`): Controllers REST, links HATEOAS e documentação OpenAPI.
   3. **Application Layer** (`application/`): Casos de uso orquestram fluxo e validações.
   4. **Domain Layer** (`domain/`): Entidades, agregados, VOs e regras de negócio sem conhecimento de JPA ou Spring.
   5. **Infrastructure Layer** (`infrastructure/`): Configuração de banco, implementações JPA, mapeadores e integrações.
- **Programação por Interfaces & DIP:** O domínio define contratos (ex: `EmprestimoRepository`); a infraestrutura implementa. Isso garante flexibilidade para trocar tecnologias sem alterar regras de negócio.

---

## 🔧 Ferramentas e Versões — Por que escolhi

| Tecnologia                | Versão | Propósito e Benefício                                                                                                                     |
| ------------------------- | ------ |-------------------------------------------------------------------------------------------------------------------------------------------|
| **Java**                  | 21+    | LTS moderno com recursos como Records e Pattern Matching, além de desempenho e segurança aprimorados.                                     |
| **Spring Boot**           | 3.3.12 | Framework para acelerar configuração, injeção de dependências e execução de APIs de produção.                                             |
| **Spring HATEOAS**        | –      | Cria respostas hipertextuais com links, tornando a API autodocumentada e permitindo descoberta de ações pelo front-end.                   |
| **Spring Data JPA**       | –      | Persistência via **repositórios customizados** definidos como contratos no domínio, com implementações em `infrastructure`, seguindo DDD. |
| **H2 Database**           | –      | Banco em memória em função da primeira versão do projeto ter sido utilizada para meu projeto final da matéria de POO.                     |
| **Springdoc OpenAPI**     | –      | Gera documentação interativa (Swagger UI), mantendo-a sempre atualizada com o código.                                                     |
| **Lombok**                | –      | Elimina boilerplate em getters, setters e construtores, deixando classes de modelo mais limpas.                                           |
| **Front-end Web**         | HTML/CSS/JS | Interface que consome endpoints HATEOAS via fetch, demonstrando integração desacoplada entre cliente e servidor.                          |

---

## 🏛️ Arquitetura e Organização de Pacotes

```plaintext
com.poo.projeto_final
├── web                   # Front-end (HTML, CSS, JavaScript)
├── api.controller        # Camada REST (Controllers + HATEOAS)
│   └── RootEntryPointController
├── application           # Camada de Aplicação (Casos de Uso e DTOs)
│   ├── dto
│   └── usecase
├── domain                # Domínio puro (Entidades, VOs, Repositórios como interfaces)
│   ├── enums
│   ├── model
│   ├── repository
│   └── service
├── impl.domain           # Implementações do Domínio (RepositoryImpl, Domain Services)
│   ├── repository
│   └── service
└── infrastructure        # Infraestrutura (JPA, mappers, configs, scheduled)
    ├── config
    ├── persistence
    │   ├── entities
    │   └── mappers
    └── scheduled

