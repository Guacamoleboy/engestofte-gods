# 09: Let Owner review and clarify a request

**What to build:** Owner/Johan can review a submitted request, understand the AI’s assessment and ask the customer for clarification.

**Blocked by:** 08: Show the customer request overview

**Status:** ready-for-agent

## In scope

- Owner request list and request detail.
- Structured answers, AI summary, source references, uncertainty, conflicts and upsell suggestions.
- Internal notes and customer-facing clarification questions.
- Neutral review statuses and next action.

## Out of scope

- Opening the approved event platform.
- Final bilateral event-data approval.
- Staff access.

## Acceptance criteria

- [ ] Owner can find a submitted request and see its structured information.
- [ ] Owner can distinguish customer-visible information from internal notes.
- [ ] Owner can see the AI summary, source basis, uncertainty and upsell opportunities.
- [ ] Owner can ask the customer for clarification.
- [ ] The customer sees an understandable review status and any customer-facing question.

## Verification

Demonstrate a request with complete information, a request with a conflict and a request waiting for customer clarification.

## Stop and ask if

- The Owner view would expose secrets or unrelated customer data.
- A source conflict needs an Owner override without a reason.
- The request cannot be safely distinguished from another request.

## Handoff

Report the Owner/customer visibility boundary and statuses used by the approval ticket.
