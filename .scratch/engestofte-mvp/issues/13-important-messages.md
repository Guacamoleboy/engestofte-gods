# 13: Add unread messages and seven-day important-message escalation

**What to build:** New Messenger messages are tracked per recipient and become grouped `Vigtige beskeder` when a recipient has not opened the event platform for seven days.

**Blocked by:** 11: Add event-scoped Messenger and contact persons

**Status:** ready-for-agent

## In scope

- Per-recipient unread state.
- Opening the event platform marks relevant event messages read for that user.
- Seven-day escalation for contact persons and Owner.
- One grouped important message per conversation with unread count.

## Out of scope

- Email, SMS or push delivery.
- Staff notifications, because Staff has no Messenger.
- A general Messenger outside an event.

## Acceptance criteria

- [ ] A new message appears unread for each intended recipient.
- [ ] Opening the event platform marks that recipient’s relevant messages read.
- [ ] One recipient opening the event does not mark another recipient’s messages read.
- [ ] After seven days, unread messages are grouped under `Vigtige beskeder` for the affected recipient.
- [ ] The grouped item shows the unread count and the full history remains in Messenger.

## Verification

Demonstrate separate unread states for two contact persons and an Owner, including one recipient opening the event and one recipient reaching the seven-day threshold.

## Stop and ask if

- A separate general notification channel is needed.
- The seven-day rule would be applied globally instead of per recipient.
- The system would mark a message read without the recipient opening the event platform.

## Handoff

Report the unread/escalation state model and how it interacts with event-platform opening.
