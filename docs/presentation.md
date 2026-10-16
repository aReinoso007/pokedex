# Walkthrough notes

About 20 minutes to show the practice, then questions. Screen share: the repository and the IDE, then the running app.

## What to show

| Criterion | Evidence |
| --- | --- |
| Clean Architecture | `backend/src/main/java/.../{domain,application,infrastructure,web}`, `ArchitectureTest`, `docs/architecture.md` |
| Testing / TDD | Test-first where it mattered most (domain rules, PokemonNameMatcher, tag spelling rule); each layer’s commit groups its test suite with the code that makes it pass. ./gradlew test (153 tests, Testcontainers), npm test (59 tests), JaCoCo report |
| Code quality | Small use-case classes, self-validating `Pokemon`, consistent problem+json, no controller logic |
| Functionality | Live demo script below, no browser console warnings |
| Presentation | This outline |
| GenAI fluency | `docs/genai.md` + how AI was used and verified in this repo |

## Outline (20 min)

1. **Problem and stories (2 min)** - four user stories, protected vs public routes, PokeAPI as a source we replicate and extend (localized name, region, tags).
2. **Architecture (5 min)** - diagram from `docs/architecture.md`. Explain the dependency rule and show `ArchitectureTest` failing if broken. Show one use case (`SyncPokemon`) end to end: controller -> use case -> ports -> adapters.
3. **Key design choices (4 min)**
   - `Pokemon` validates its own invariants, so sync, PUT and PATCH share one rulebook.
   - Cache at the HTTP client level, parallel fan-out for the list, pagination caps.
   - JWT + BCrypt, 404-vs-403 reasoning, problem+json everywhere.
   - Flyway owns the schema, Hibernate only validates; seed data kept out of tests.
4. **Testing strategy (3 min)** - pyramid: many fast unit tests on domain/application, adapter tests with real Postgres, a handful of full-stack MockMvc tests, ArchUnit. Frontend: component tests with MSW.
5. **Demo (5 min)** - script below.
6. **GenAI (1 min)** - prompt structure, tests first, what was rejected/fixed, guard-rails.

## Demo script

1. `docker compose up --build` already running. Open http://localhost:3000.
2. **US01**: scroll the grid (sprite, category, weight, abilities), go to page 2, search with a typo ("charzard" finds Charizard) from the header.
3. **US02**: Eevee detail - stats bars, description, branching evolution chain; click Vaporeon to navigate the lineage.
4. Try saving while anonymous -> prompted to sign in. Sign in as `demo` / `Demo1234!`.
5. **US03**: "Save to my Pokedex" on Eevee -> toast, button becomes "Saved locally". Open **My Pokemon**: seeded ten plus Eevee.
6. **US04**: Edit Eevee - set region "Kanto", tags "normal, favourite", localized name; show client-side validation (weight `-5`), then save. Show the server-side error path by opening Swagger and sending a PATCH with `{"weight": -1}` (400 with `errors`).
7. Delete a Pokemon -> confirm dialog -> list refreshes.
8. Swagger UI (http://localhost:8089/swagger-ui.html): authorize with the token, call `GET /api/local/pokemon`. Show 401 without token and 404 for unknown id.
9. Stop PokeAPI access (or point `POKEAPI_BASE_URL` at a dead host) -> `502` and the UI's retry state. Cached pages still load.
10. Open DevTools: console is clean.

## Likely questions and answers

- **Why Clean Architecture for a small API?** I wanted the domain and use cases testable without Spring or a database, and swapping PokeAPI or Postgres means writing a new adapter. Cost: some wiring boilerplate.
- **Why are use cases not annotated with `@Service`?** To keep `application` free of framework imports; `UseCaseConfig` is the composition root and ArchUnit enforces it.
- **Where does validation live?** Shape validation (required, sizes) in request DTOs for fast, field-level feedback; business invariants in `Pokemon`; the same violations surface as 400.
- **PUT vs PATCH?** PUT replaces all editable fields (omitted custom fields are cleared); PATCH only touches what is sent and rejects an empty body.
- **Why is a Pokemon's id the PokeAPI id?** Natural, stable key; makes sync idempotent (409 on duplicate) and avoids a mapping table.
- **Caching details?** Caffeine, 1h TTL, per-resource caches (`pokeapi-page|pokemon|species|evolution`). Errors are never cached. For multiple instances, swap for Redis behind the same `@Cacheable` annotations.
- **What happens when PokeAPI is down?** `CatalogUnavailableException` -> 502 with a generic message (details logged); cached data still serves.
- **Security gaps / next steps?** Refresh tokens and revocation, rate limiting on login, account lockout, roles beyond `USER`, per-user local collections (today the local store is shared by all authenticated users, which matches the single shared dataset in the practice scope), HTTPS termination in front of nginx.
- **Per-user data?** The scope is a shared local replica plus a user management API; I kept the replica global. Adding `owner_id` to `pokemon` and scoping queries is the natural extension.
- **Concurrency?** Sync relies on the PK; a race returns 409 through `DataIntegrityViolationException` handling. Optimistic locking (`@Version`) would be the next step for concurrent edits.
- **How did you use AI and how do you know it was right?** See `docs/genai.md`: tests first, architecture rules in the prompt, ArchUnit and integration tests as guard-rails, deliberate breakage to prove tests can fail, manual security review.
- **Performance of the list endpoint?** 2 upstream calls per item, run in parallel on a bounded pool and cached; first cold page is the slow one. Possible improvement: pre-warm or persist a catalog snapshot.
- **Transaction boundary?** `@Transactional` lives on the adapters because `application` is framework-free, so `UpdateLocalPokemon` reads and saves in two transactions: concurrent PATCHes can lose an update. Fix: `@Version` optimistic locking, or a `TransactionTemplate` decorator wired in `UseCaseConfig`.
- **Login timing?** When the username does not exist, BCrypt is skipped, so response time can reveal valid usernames. Fix: compare against a dummy hash.
- **Deleted user's token?** Still valid until expiry; the role comes from the claim. Short lifetime (2h) limits it; revocation list or DB check is the next step.
- **Cache stampede / multiple instances?** Caffeine is per-instance and `@Cacheable` is not `sync = true`, so concurrent cold requests duplicate upstream calls. Fix: `sync = true` + Redis.
- **Fan-out robustness?** `join()` has no timeout and one failing Pokemon fails the page. Fix: `orTimeout` per future and degrade that item; Java 21 virtual threads instead of a fixed pool.
- **Monitoring / deployment?** Today only `/actuator/health` and CI tests. Next: Micrometer + Prometheus, alerts on 502 rate and p95 latency, CI publishing the image and deploying.

## Known limitations (be upfront)

- Evolution chains are flattened into an ordered list with `stage` and `evolvesFrom`; the UI groups by stage. Complex trigger conditions (level, item) are not modelled.
- Names are shown in English only; `localizedName` is the user-provided override.
- Stats and evolutions of local records are read-only; editable fields are name, measurements, category, description, abilities, localized name, region, tags.
- Token revocation is not implemented.
