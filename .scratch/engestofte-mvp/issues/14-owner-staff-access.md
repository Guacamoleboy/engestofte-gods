# 14: Enforce Owner and Staff access boundaries

**What to build:** Owner and Staff receive the correct role-specific access to event information.

**Blocked by:** 10: Approve a request and open the concrete event

**Status:** ready-for-agent

## In scope

- Owner full access to requests, events, communication, approvals and contact persons.
- Staff read-only access to relevant operational and kitchen information.
- Redaction of direct customer identifiers from Staff views.
- Staff denial of Messenger and write operations.

## Out of scope

- New role types.
- Customer-facing contact-person permissions.
- AI provider behavior.

## Acceptance criteria

- [ ] Owner can perform all agreed internal actions.
- [ ] Staff can see the whole relevant operational request and event arrangement.
- [ ] Staff can see required allergies and dietary requirements read-only.
- [ ] Staff cannot see Messenger, full name, full email or other direct identifiers.
- [ ] Staff cannot edit, approve, message or manage contact persons.
- [ ] Unauthorized access is rejected before sensitive data is returned.

## Verification

Demonstrate the same event as Owner and Staff and compare visible data and available actions.

## Stop and ask if

- Staff access requires exposing direct identifiers or Messenger.
- A new permission role is needed.
- A controller or UI can bypass the same authorization rule.

## Handoff

Report the permission matrix and redacted fields for future implementation and review.
