# 11: Add event-scoped Messenger and contact persons

**What to build:** Customers and Owner can communicate inside the correct event, and contact-person access is managed per event.

**Blocked by:** 10: Approve a request and open the concrete event

**Status:** ready-for-agent

## In scope

- Two-panel event view with event data and Messenger.
- Event-specific message history and message metadata.
- Contact persons, primary contact person and Owner access management.
- Multiple events per account with strict event isolation.

## Out of scope

- Automated email sending.
- Staff Messenger access.
- Read/unread escalation and important-message grouping.

## Acceptance criteria

- [ ] The event platform shows event data on the left and Messenger on the right.
- [ ] Messages show sender, profile image, date, time and content.
- [ ] Contact persons and Owner can send messages; AI is never the human sender.
- [ ] The same account can be connected to multiple events without cross-event data leakage.
- [ ] The primary contact person and Owner can add/remove other contact persons.
- [ ] An additional contact person can remove themselves but cannot remove the primary contact person.

## Verification

Demonstrate two events for one account, two contact persons on one event and message isolation between events.

## Stop and ask if

- A message would need to exist outside an event.
- A contact-person permission contradicts the domain glossary.
- Real email or external notification delivery is proposed.

## Handoff

Report the event-scoped message and contact-person boundaries for the approval and notification tickets.
