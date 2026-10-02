# ADR-001: Backend ownership of AI and enquiry data

## Status

Accepted

## Context

The Engestofte application uses a browser frontend, a Java REST backend and a database. Its main business record is a wedding enquiry that starts as a local draft, is submitted after authentication, and may later be reviewed and approved into an event. AI helps collect and structure the enquiry, while the customer and Owner remain responsible for confirming its facts and decisions.

The AI provider is an external service. Its credentials and wire protocol must not become part of the browser application. Provider output also needs to be validated and mapped into the application's own enquiry model so that provider-specific formats do not define the domain or database schema.

## Decision

- The Java backend owns the connection to OpenAI and holds provider credentials in backend environment configuration.
- The frontend calls application endpoints and exchanges application-owned request and response DTOs. It does not call OpenAI directly.
- The backend translates between the application contract and the selected AI provider behind a replaceable provider interface.
- The database is the system of record for submitted enquiries and the structured answers, assessments and source references needed to review them. Provider output is mapped into these application-owned records; the database does not store arbitrary provider wire payloads as the domain contract.
- The AI interaction endpoint persists each submitted answer together with its current question, language and structured AI result as an application-owned interaction record. This is not the complete submitted wedding enquiry; that aggregate and its authentication/submission workflow remain a later scope.
- The frontend may keep a resumable draft before authentication and final submission in browser storage. Each answer sent to the AI endpoint is also persisted as an interaction record; the complete wedding-enquiry aggregate is persisted after authentication and submission.
- Provider response state is not retained by OpenAI for the interaction (`store: false`). Provider data handling must still be reviewed before real customer enquiries are used.
- AI output is assistive. Owner review and customer confirmation remain explicit workflow steps; AI output does not approve availability, pricing or a booking.

## Alternatives

### Call OpenAI directly from the frontend

Rejected because browser-delivered credentials are exposed to users, provider details would couple the UI to OpenAI, and backend control over validation, usage and error handling would be reduced.

### Use OpenAI as the system of record

Rejected because the project needs application-owned enquiry history and workflow state that remains independent of the provider. OpenAI may generate content, but it does not own submitted enquiries, approvals or event status.

### Persist raw provider requests and responses as the domain model

Rejected because provider wire formats are integration details and can change independently from the project's enquiry model. The backend stores the submitted enquiry and the structured results that the application needs to review. Raw transport traces are not treated as business records.

## Consequences

- The OpenAI key stays out of frontend bundles and browser network requests.
- Provider changes stay behind the backend provider interface and DTO mapping.
- Submitted enquiries and relevant structured AI results can be reviewed and retained under the application's own data model.
- The AI endpoint stores each submitted interaction before authentication; the complete wedding-enquiry aggregate and account-linked request are created only after authentication and submission.
- The current scope implements the provider seam, one-interaction flow and persistence of those submitted AI interactions. The complete wedding-enquiry aggregate, authentication/submission workflow and retention details remain for the relevant later implementation scopes.
- `store: false` disables Responses API response-state storage; it does not replace a project-level data-handling review.

## References

- `.scratch/engestofte-mvp/spec.md`
- `.scratch/engestofte-mvp/issues/03-openai-first-question.md`
- `docs/forventet/entities.md`
- `docs/forventet/database.md`
- `docs/grilling/flow-definition.md`
- [OpenAI Responses API](https://developers.openai.com/api/reference/responses)
- [OpenAI Structured Outputs](https://developers.openai.com/api/docs/guides/structured-outputs)
- [Responses API storage behavior](https://developers.openai.com/api/docs/guides/migrate-to-responses#additional-differences)