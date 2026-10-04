# 10: Approve a request and open the concrete event

**What to build:** Owner can approve a request or send a customer-facing follow-up with an explanation. A follow-up opens the event conversation so the customer can answer or close the request, and Owner can continue the conversation, approve it or close it with an explanation.

**Blocked by:** 09: Let Owner review and clarify a request

**Status:** in-progress

## In scope

- Explicit Owner approval action.
- Creation/opening of the concrete event relationship.
- Customer access transition from request overview to event dashboard.
- Customer-visible notes separated from internal notes.
- Owner/customer event conversation with messages persisted per event.
- Creation of the event on the first Owner message when follow-up starts before approval.
- Customer can answer an Owner follow-up or close the unapproved request.
- Owner sees customer replies as follow-up required and can respond or close with an explanation.
- Owner can send a message from every active request/event state.
- `/dashboard/events/{id}` uses a four-column event-information area and a four-column conversation area below the dashboard navigation.
- A unique event access-token hash is stored for a later guest invitation link; no guest-link endpoint is included here.

## Out of scope

- Bilateral field-level changes.
- Depositum or booking.

## Acceptance criteria

- [ ] Owner can approve only a request with sufficient required information.
- [ ] Approval creates or opens exactly one concrete event for the request.
- [ ] The customer can open `/dashboard/events/{id}` after approval.
- [ ] The customer sees approved event data and customer-visible notes only.
- [ ] Internal notes and AI internal assessments remain hidden from the customer.
- [ ] Owner can send a customer-facing follow-up from every active request/event state; an event is created if the conversation starts before approval.
- [ ] A customer can open the event after an Owner message, reply, or close the unapproved request.
- [ ] Customer replies mark Owner follow-up as required; Owner can reply or close with a customer-facing reason.
- [ ] Closed requests are terminal and no longer accept messages.
- [ ] The customer event page separates event information and conversation into responsive columns.
- [ ] Each enquiry can create at most one event; each event has its own ID, separate from the enquiry submission UUID.
- [ ] Guest capability tokens are stored only as hashes and are not exposed in customer event responses.

## Verification

Demonstrate an unapproved request being denied event access before an Owner message, then customer access after follow-up, a customer reply returning to Owner, and final approval or explained closure. Confirm an unrelated customer cannot load the event and customer responses never include internal notes or AI assessments.

## Stop and ask if

- Approval would bypass unresolved critical information.
- More than one event could be created for one request without an explicit decision.
- Internal information would be exposed by the event response.

## Handoff

Report the approval transition, event identity boundary and customer-visible data model.
