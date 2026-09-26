# 10: Approve a request and open the concrete event

**What to build:** Owner can approve a request, after which the customer receives access to `/dashboard/events/{id}` with approved customer-visible data.

**Blocked by:** 09: Let Owner review and clarify a request

**Status:** ready-for-agent

## In scope

- Explicit Owner approval action.
- Creation/opening of the concrete event relationship.
- Customer access transition from request overview to event dashboard.
- Customer-visible notes separated from internal notes.

## Out of scope

- Messenger conversation.
- Bilateral field-level changes.
- Depositum or booking.

## Acceptance criteria

- [ ] Owner can approve only a request with sufficient required information.
- [ ] Approval creates or opens exactly one concrete event for the request.
- [ ] The customer can open `/dashboard/events/{id}` after approval.
- [ ] The customer sees approved event data and customer-visible notes only.
- [ ] Internal notes and AI internal assessments remain hidden from the customer.

## Verification

Demonstrate an unapproved request being denied event access, followed by Owner approval and successful access to the correct event.

## Stop and ask if

- Approval would bypass unresolved critical information.
- More than one event could be created for one request without an explicit decision.
- Internal information would be exposed by the event response.

## Handoff

Report the approval transition, event identity boundary and customer-visible data model.
