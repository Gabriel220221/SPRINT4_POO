# MOTIVA — API REST (Sprint 4)

API REST em **Spring Boot + Spring Data JPA** para priorização de manutenção de vegetação em rodovias.
Evolução do protótipo console (Sprint 1–2) e da persistência JDBC/DAO (Sprint 3), reaproveitando o banco Oracle FIAP.

## Stack
Java 17 · Spring Boot 4.1.x · Maven · Spring Web · Spring Data JPA · Bean Validation · Oracle (`ojdbc17`) · H2 (apenas testes)

## Arquitetura
```
src/main/java/br/com/motiva/
├── MotivaApplication.java   # @SpringBootApplication
├── model/                   # @Entity (TrechoRodovia, EquipeManutencao, Intervencao, RelatorioPrioridade, ItemRelatorio)
├── repository/              # interfaces JpaRepository + derived queries (sem SQL)
├── service/                 # @Service: regras de negócio + motor de prioridade
├── controller/              # @RestController: endpoints REST (só HTTP)
├── dto/                     # records de entrada/saída (contrato da API)
└── exception/               # @RestControllerAdvice + exceções de domínio
```
Fluxo: **Controller → Service → Repository → Banco**.

## Como executar
1. Crie as tabelas/sequences (se ainda não existirem da Sprint 3) com `docs/ddl-oracle.sql`.
   Se os nomes da sua Sprint 3 forem diferentes, ajuste `@Table`, `@Column` e `@SequenceGenerator` nas entities.
2. Configure as credenciais **por variáveis de ambiente** (nada de senha no repositório):
   ```bash
   export DB_USER=seu_rm
   export DB_PASSWORD=sua_senha
   ```
   (Windows PowerShell: `$env:DB_USER="rm"; $env:DB_PASSWORD="senha"`)
3. Rode:
   ```bash
   mvn spring-boot:run
   ```
   A API sobe em `http://localhost:8080`.
4. Testes (usam H2 em memória, não tocam no Oracle): `mvn test`

## Endpoints

### Trechos — `/api/trechos`
| Método | Rota | Ação | Sucesso | Erros |
|---|---|---|---|---|
| GET | `/api/trechos` (`?tipo=` ou `?nivelMinimo=`) | Listar (derived queries) | 200 | — |
| GET | `/api/trechos/{id}` | Buscar por ID | 200 | 404 |
| POST | `/api/trechos` | Criar | 201 + Location + corpo | 400 |
| PUT | `/api/trechos/{id}` | Atualizar | 200 | 400 / 404 |
| DELETE | `/api/trechos/{id}` | Remover | 204 | 404 / 409 |

### Equipes — `/api/equipes` (`?especialidade=` ou `?apenasAtivas=true`)
Mesma tabela de métodos/códigos de Trechos.

### Intervenções — `/api/intervencoes` (`?status=` ou `?trechoId=`)
Mesma tabela de métodos/códigos. Regras: trecho e equipe devem existir (404), equipe deve estar ativa e status ∈ {PLANEJADA, EM_ANDAMENTO, CONCLUIDA} (400).

### Relatórios — `/api/relatorios`
| Método | Rota | Ação | Sucesso |
|---|---|---|---|
| POST | `/api/relatorios` | Gera relatório dos trechos atuais e persiste | 201 |
| GET | `/api/relatorios` | Histórico (mais recente primeiro) | 200 |
| GET | `/api/relatorios/periodo?inicio=yyyy-MM-dd&fim=yyyy-MM-dd` | Histórico por período | 200 / 400 |

Classificação (no `RelatorioService`): **URGENTE ≥ 80 cm · CRÍTICO ≥ 50 cm · ATENÇÃO ≥ 25 cm · NORMAL < 25 cm**
(constantes `LIMIAR_*`; troque pelos da sua Sprint 2 se forem outros).

### Formato de erro
```json
{ "status": 400, "erro": "Bad Request", "mensagem": "Dados inválidos",
  "timestamp": "2026-10-23T10:00:00", "campos": { "nivelVegetacaoCm": "nivelVegetacaoCm não pode ser negativo" } }
```

## Exemplos cURL
```bash
# 1) Criar trecho (201)
curl -i -X POST http://localhost:8080/api/trechos -H "Content-Type: application/json" \
  -d '{"rodovia":"SP-150","kmInicial":10.0,"kmFinal":15.5,"tipo":"ACOSTAMENTO","nivelVegetacaoCm":85.0}'

# 2) Listar / buscar (200) e buscar inexistente (404)
curl -i http://localhost:8080/api/trechos
curl -i http://localhost:8080/api/trechos/1
curl -i http://localhost:8080/api/trechos/9999

# 3) Derived queries
curl -i "http://localhost:8080/api/trechos?tipo=ACOSTAMENTO"
curl -i "http://localhost:8080/api/trechos?nivelMinimo=50"

# 4) Atualizar (200) e remover (204)
curl -i -X PUT http://localhost:8080/api/trechos/1 -H "Content-Type: application/json" \
  -d '{"rodovia":"SP-150","kmInicial":10.0,"kmFinal":15.5,"tipo":"ACOSTAMENTO","nivelVegetacaoCm":40.0}'
curl -i -X DELETE http://localhost:8080/api/trechos/1

# 5) Dado inválido (400)
curl -i -X POST http://localhost:8080/api/trechos -H "Content-Type: application/json" \
  -d '{"rodovia":"SP-150","kmInicial":1,"kmFinal":2,"tipo":"PISTA","nivelVegetacaoCm":-5}'

# 6) Equipe e intervenção
curl -i -X POST http://localhost:8080/api/equipes -H "Content-Type: application/json" \
  -d '{"nome":"Equipe Alfa","especialidade":"ROCADA","qtdMembros":6}'
curl -i -X POST http://localhost:8080/api/intervencoes -H "Content-Type: application/json" \
  -d '{"descricao":"Roçada emergencial","dataPrevista":"2026-11-05","trechoId":1,"equipeId":1}'

# 7) Relatórios
curl -i -X POST http://localhost:8080/api/relatorios
curl -i http://localhost:8080/api/relatorios
curl -i "http://localhost:8080/api/relatorios/periodo?inicio=2026-10-01&fim=2026-10-31"
```
> Evidências (prints das ≥ 6 requisições): salve em `docs/evidencias/` e referencie aqui.

## Bônus implementados
DTOs (records) · Bean Validation (`@Valid`, `@NotNull`, `@NotBlank`, `@PositiveOrZero`) · `@RestControllerAdvice` · testes `@SpringBootTest` + MockMvc.

## Perguntas de reflexão

**1. Por que o Repository é uma interface e não uma classe? Quem escreve a implementação e quando?**
Porque o contrato (salvar, buscar, listar, consultas por nome de método) é tudo o que o código precisa conhecer; o *como* é repetitivo e previsível. Quem escreve a implementação é o Spring Data: na **inicialização da aplicação** (ao subir o contexto), ele cria em tempo de execução um proxy que implementa a interface (baseado em `SimpleJpaRepository`) e o registra como bean, pronto para injeção. Não existe `.java` com a implementação no projeto.

**2. O pattern DAO "morreu" na migração ou só mudou de forma?**
Só mudou de forma. A ideia do DAO — isolar o acesso a dados atrás de uma abstração para que o resto do sistema não saiba de SQL/conexões — continua inteira. O `EquipeManutencaoDAO` de 375 linhas virou uma interface de poucas linhas porque a parte mecânica (abrir conexão, `PreparedStatement`, mapear `ResultSet`, fechar recursos) passou a ser gerada pelo framework. O Repository é, na prática, um DAO gerado em tempo de execução; o `ConexaoBD` (Singleton) virou o pool do Spring configurado no `application.properties`.

**3. Por que a validação `nivelVegetacao >= 0` deve ficar no Service e não no Controller?**
Porque é regra de negócio, e não de transporte HTTP. No Service ela vale para **qualquer** chamador (outros controllers, jobs, futuras integrações IoT, testes) e fica em um único lugar, evitando duplicação e divergência. O Controller só traduz HTTP ⇄ objetos e devolve o status adequado. (As anotações de Bean Validation nos DTOs são uma checagem de formato na borda; a regra autoritativa continua no Service.)

**4. Onde está o SQL do `findByTipo()`? Quem o gerou?**
Não existe SQL escrito no projeto. Na inicialização, o Spring Data analisa o nome do método (*derived query*): `findBy` + propriedade `tipo` da entity `TrechoRodovia` e monta uma consulta JPQL (`select t from TrechoRodovia t where t.tipo = ?1`). O **Hibernate** (provedor JPA) traduz essa consulta para o SQL do dialeto Oracle (`select ... from TRECHO_RODOVIA where TIPO=?`) e o executa via JDBC. Com `spring.jpa.show-sql=true` dá para ver o SQL gerado no console.

## Git (boas práticas)
- `.gitignore` ignora `target/`, arquivos de IDE e `application-local.properties`.
- Credenciais só via `DB_USER` / `DB_PASSWORD`.
- Faça commits incrementais, ex.: `feat: entities JPA`, `feat: repositories`, `feat: services`, `feat: controllers CRUD`, `feat: relatórios`, `feat: DTOs + validação + advice`, `test: MockMvc`, `docs: README`.
