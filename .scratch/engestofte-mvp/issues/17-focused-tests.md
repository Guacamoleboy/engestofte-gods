# 17: Add focused backend and frontend tests

**Goal:** Add a small, maintainable test suite that demonstrates how the project's critical behavior is verified before the prototype is considered ready for real use.

**Status:** Planned

## Context

The project currently has no test files. The backend already declares JUnit 5, Rest Assured and Testcontainers dependencies, and the frontend already has Vitest and jsdom configured as development dependencies with `test` and `test:run` scripts. Avoid adding or upgrading dependencies unless implementation shows a concrete need.

Tests should verify behavior and contracts that matter to the project. They should not be added solely to increase test count, and should not assume that AI-generated code is correct without checking its behavior.

## In scope

- Add backend unit tests for deterministic, high-risk logic, prioritizing the AI flow request/response contract, validation and error handling, and other relevant pure services.
- Test domain behavior through the service layer, using the shared generic DAO test for common persistence behavior. Add HTTP/API integration tests only when they verify a separate boundary contract that is not already covered by service tests.
- Add frontend tests for `src/api/client.ts` and relevant API endpoint modules. Cover URL and request construction, JSON response handling, authorization headers, and `ApiError` behavior for failed or non-JSON responses.
- Add a small number of frontend component or flow tests only where they verify important user-visible behavior of the enquiry flow.
- Ensure external AI calls are mocked or replaced with deterministic fixtures so the suite is repeatable, fast, and does not require API credentials or incur provider costs.
- Document the commands to run the backend and frontend test suites and any required local test infrastructure.

## Out of scope

- Production deployment or publishing the application on Engestofte Gods' domain.
- End-to-end tests against the live website, real AI provider, production database, or third-party services.
- Broad refactoring, dependency upgrades, or adding tests for every class and component.
- Treating a passing test suite as proof that AI responses are always correct; model behavior still needs explicit evaluation and human review.

## Acceptance criteria

- [x] The backend has focused tests for AI-flow validation/provider behavior, custom DAO behavior, and date extraction.
- [x] Backend service tests cover representative AI-flow and enquiry success/error or idempotency behavior without touching production services or data.
- [x] Frontend API client tests cover successful JSON responses, authorization/request headers, API error payloads, and non-JSON/error responses.
- [x] Any future enquiry-flow UI tests must assert observable behavior rather than implementation details; no UI test was needed for this focused ticket.
- [x] Automated tests do not make live AI-provider calls and require no AI credentials or external AI network access.
- [x] Backend and frontend test commands are documented in `docs/testing.md` and can be run independently.
- [x] Tests pass locally; remaining evaluation limits are recorded in `docs/testing.md`.

## Suggested verification commands

- Backend: `mvn test` from `backend/`.
- Frontend: `npm run test:run` from `frontend/`.

## Handoff

Summarize the behaviors covered, commands executed and results, any test infrastructure prerequisites, and the main risks that remain outside automated coverage.
