# 08: Show the customer request overview

**What to build:** The authenticated customer can see submitted requests and their status under `/dashboard/events/` without accessing unapproved event content.

**Blocked by:** 07: Authenticate and submit the wedding enquiry

**Status:** ready-for-agent

## In scope

- Request list and request summary.
- Date, time and current status.
- Empty, loading and error states.
- Clear boundary before Owner approval.

## Out of scope

- Full event data editing.
- Messenger.
- Owner dashboard implementation.

## Acceptance criteria

- [ ] An authenticated customer can open `/dashboard/events/`.
- [ ] The customer sees only requests connected to the account.
- [ ] Each request shows date, time and current status.
- [ ] An unapproved request does not expose the concrete event platform or internal notes.
- [ ] The view handles no requests and backend errors clearly.

## Verification

Demonstrate one customer with no requests, one with a request under review and one account attempting to access another customer’s request.

## Stop and ask if

- The route would reveal another customer’s data.
- A status needs a new meaning not recorded in the status model.
- The page requires Owner-only information.

## Handoff

Report the visible statuses and the authorization boundary for the next Owner-review ticket.
