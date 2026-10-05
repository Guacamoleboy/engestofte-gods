# 12: Add bilateral approval for event-data changes

**What to build:** Customer and Owner can propose, explain, approve and reject concrete changes without losing the approved history.

**Blocked by:** 11: Add event-scoped Messenger and contact persons

**Status:** in-progress

## In scope

- Field-level old-value/new-value change proposals.
- Separate customer and Owner approval.
- Rejection explanation and preserved history.
- Status transition to `Afventer godkendelse`.

## Out of scope

- Depositum payment.
- AI automatic approval.
- Staff approval.

## Acceptance criteria

- [X] A customer change becomes a proposal rather than silently replacing the approved value.
- [X] An Owner change also requires customer approval, even if agreed verbally or in Messenger.
- [X] Both approvals are recorded separately.
- [X] Rejection requires an explanation and keeps the previous approved value.
- [X] A later proposal preserves earlier proposal history.
- [X] Unresolved critical changes prevent final deposit approval/booking.

## Verification

Demonstrate customer change, Owner change, approval, rejection, repeated change and conflict resolution for one critical field.

## Stop and ask if

- A whole-request approval would replace field-level approval.
- A change could be finalized without both parties.
- The data model would erase the previous approved value or proposal history.

## Handoff

Report the proposal lifecycle and status transitions for the notification and booking tickets.
