# Test strategy

Tests cover behavior that protects important project contracts. `EntityManagerDAOTest` covers shared persistence behavior once; domain tests exercise custom behavior through the service layer to avoid repeating the same flows in both DAO and service tests. Frontend API tests protect request construction and error handling. AI provider calls use deterministic test doubles or fixtures and do not require live credentials.

## Run tests

- Backend: run `mvn test` from `backend/`. The backend suite uses Testcontainers with PostgreSQL, so Docker must be running.
- Frontend: run `npm run test:run` from `frontend/`.

## Current focus and limits

The backend suite covers generic DAO CRUD/query/transaction behavior, AI-flow validation, persistence and request idempotency with deterministic providers, explicit OpenAI response token limits, authentication registration/login/refresh, enquiry submission idempotency, event deposit and booking status transitions, role lookup, database health, guest-token handling, requested-date extraction, and staff-facing contact-data redaction. The frontend suite covers API request/response handling and confirms that AI-flow request IDs are sent with interactions. Change-approval, message authorization, and several other enquiry review paths are not yet covered. Tests do not evaluate live model quality or replace manual review of AI behavior, and they do not claim the application is production ready.
