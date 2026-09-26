# 06: Persist the local draft and complete the DONE state

**What to build:** A customer can leave and resume the AI-flow locally, then reach a clear `DONE` state without authenticating prematurely.

**Blocked by:** 05: Build the complete five-step wedding enquiry flow

**Status:** ready-for-agent

## In scope

- Raw draft persistence in browser `localStorage`.
- Resume behavior and clear handling of an existing draft.
- Visible `DONE` state and handoff to login/register.
- Prototype caveat about localStorage and GDPR/EU production readiness.

## Out of scope

- Server persistence before authentication.
- Password handling or account creation.
- Production legal approval of sensitive local storage.

## Acceptance criteria

- [ ] Leaving and reopening the flow restores the current raw draft.
- [ ] The customer is told that a draft can still be submitted after returning.
- [ ] The customer cannot submit the request to Owner while it remains local.
- [ ] `DONE` directs the customer to login or register.
- [ ] The prototype’s localStorage limitation is documented without presenting it as production compliance.

## Verification

Fill part of the flow, leave it, reopen it, complete it and follow the `DONE` handoff. Inspect that no request appears in the Owner flow before authentication.

## Stop and ask if

- A production-safe storage design is being requested instead of the school prototype behavior.
- The draft contains data that the agreed prototype scope does not permit.
- Clearing or migrating draft data could cause silent data loss.

## Handoff

Report the draft key/version assumptions and the exact authentication handoff payload.
