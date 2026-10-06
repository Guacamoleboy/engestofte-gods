# 15: Complete depositum, booking and customer cancellation

**What to build:** The Owner requests a deposit only after approval is complete. The primary customer can then record the simulated deposit to book the event. Customer cancellation is explicit and irreversible.

**Blocked by:** 12: Add bilateral approval for event-data changes

**Status:** complete

## In scope

- `Afventer depositum`, simulated `Betal depositum` and `Booket`.
- Only the Owner can start the deposit step after approvals are complete and pending event changes are resolved.
- The customer deposit action is unavailable until the Owner requests it; the action only records a simulated deposit and processes no money.
- Event data can still be changed through bilateral proposals while awaiting or after the deposit; those proposals do not undo the deposit or booking status.
- Friendly booking confirmation.
- Multi-step primary-contact cancellation.
- Paid-depositum consequence and non-reopenable cancelled event.

## Out of scope

- Real money, payment provider or refund processing.
- Owner approval of a primary-contact exit.
- Reopening an annulled event.

## Acceptance criteria

- [x] The Owner can request a deposit only after relevant customer and Owner approvals and with no unresolved event changes.
- [x] The customer cannot record a deposit before the Owner's request; the simulation then shows `Depositum betalt` and transitions the event to `Booket`.
- [x] Customer and Owner can continue proposing event-data changes after the deposit, and resolving those changes preserves `Afventer depositum` or `Booket` as applicable.
- [x] Only events with status `BOOKED` are listed as openable booking cards for Owner and Customer; approved events awaiting deposit remain under enquiries.
- [x] Booking displays a friendly Engestofte confirmation.
- [x] Primary-contact exit requires clear consequence text and multiple deliberate confirmations.
- [x] Confirmed exit produces `Annulleret af kunde`.
- [x] A paid depositum is not refunded after customer cancellation.
- [x] An annulled event cannot be reopened; the customer must create a new enquiry.

## Verification

Demonstrate blocked payment before agreement, successful simulated booking, cancelled confirmation, cancellation after depositum and attempted reopening.

## Stop and ask if

- Real payment or refund behavior is requested.
- The cancellation flow can be completed accidentally.
- A cancelled event could be made active without a new enquiry.

## Handoff

Report the final status transitions and irreversible cancellation behavior.
