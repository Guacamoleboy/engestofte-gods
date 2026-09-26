# 07: Authenticate and submit the wedding enquiry

**What to build:** A customer can register or log in after `DONE`, and the completed local draft becomes a persisted request.

**Blocked by:** 06: Persist the local draft and complete the DONE state

**Status:** ready-for-agent

## In scope

- Login and register entry after the AI-flow.
- Normal password reset path.
- Transfer of the local draft after successful authentication.
- Creation of the primary contact person and initial request relationship.

## Out of scope

- Event approval or event-platform access.
- Additional contact-person management.
- Real email notifications.

## Acceptance criteria

- [ ] The customer can choose login or register after `DONE`.
- [ ] A successful login/register submits the local draft exactly once.
- [ ] The request is persisted with its primary contact person and selected language.
- [ ] Failed authentication leaves the local draft available for retry.
- [ ] Password reset is treated separately from leaving or cancelling an event.

## Verification

Demonstrate register, login, failed authentication and password-reset entry. Verify that a successful submission creates one request and does not duplicate it on refresh.

## Stop and ask if

- Authentication requires changing an existing security policy or public contract.
- A request could be submitted without a known account/contact person.
- Email delivery is assumed to work in the school deployment.

## Handoff

Report the request-submission boundary, idempotency assumption and resulting request status.
