# 14: Enforce Owner and Staff access boundaries

**What to build:** Owner and Staff receive the correct role-specific access to event information.

**Blocked by:** 10: Approve a request and open the concrete event

**Status:** complete

## In scope

- Owner full access to requests, events, communication, approvals and contact persons.
- Staff read-only access to relevant operational and kitchen information.
- Staff can send an event-specific, email-like message to the customer and see customer replies in the staff important-messages inbox.
- Hide customer email addresses and phone numbers from Staff views; customer names may be shown to Staff for kitchen operations.
- Staff cannot access Owner-Customer Messenger history or write operations other than sending event messages.

## Out of scope

- New role types.
- Customer-facing contact-person permissions.
- AI provider behavior.

## Acceptance criteria

- [x] Owner can perform all agreed internal actions.
- [x] Staff can see the whole relevant operational request and event arrangement.
- [x] Staff can see required allergies and dietary requirements read-only.
- [x] Staff cannot see Messenger, email addresses, phone numbers or other contact routes.
- [x] Staff can send a message from an event; it appears in the customer important messages and the customer can reply in the event conversation.
- [x] Staff can see customer replies in the staff important-messages inbox without access to Owner-Customer messages or a staff conversation thread.
- [x] Staff can see the customer name, event status, event title, guest count, requested date and structured kitchen details.
- [x] Staff can see the selected wedding package, expected vegan count, allergy status and allergy details as structured event facts.
- [x] Staff event cards and event details follow the customer and owner layouts, without contact-person management.
- [x] Staff checklist and resources remain locked.
- [x] Staff cannot edit, approve or manage contact persons; the only staff write action is sending an event message.
- [x] Unauthorized access is rejected before sensitive data is returned.

## Verification

Demonstrate the same event as Owner and Staff and compare visible data and available actions.

## Stop and ask if

- Staff access requires exposing direct identifiers or Messenger.
- A new permission role is needed.
- A controller or UI can bypass the same authorization rule.

## Handoff

Report the permission matrix and redacted fields for future implementation and review.
