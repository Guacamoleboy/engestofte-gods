# 15: Complete depositum, booking and customer cancellation

**What to build:** Customer and Owner can complete the school-project booking flow, while a primary-contact cancellation is explicit and irreversible.

**Blocked by:** 12: Add bilateral approval for event-data changes

**Status:** ready-for-agent

## In scope

- `Afventer depositum`, simulated `Betal depositum` and `Booket`.
- Friendly booking confirmation.
- Multi-step primary-contact cancellation.
- Paid-depositum consequence and non-reopenable cancelled event.

## Out of scope

- Real money, payment provider or refund processing.
- Owner approval of a primary-contact exit.
- Reopening an annulled event.

## Acceptance criteria

- [ ] The depositum action is available only after relevant customer and Owner approvals.
- [ ] The simulation shows `Depositum betalt` and transitions the event to `Booket`.
- [ ] Booking displays a friendly Engestofte confirmation.
- [ ] Primary-contact exit requires clear consequence text and multiple deliberate confirmations.
- [ ] Confirmed exit produces `Annulleret af kunde`.
- [ ] A paid depositum is not refunded after customer cancellation.
- [ ] An annulled event cannot be reopened; the customer must create a new enquiry.

## Verification

Demonstrate blocked payment before agreement, successful simulated booking, cancelled confirmation, cancellation after depositum and attempted reopening.

## Stop and ask if

- Real payment or refund behavior is requested.
- The cancellation flow can be completed accidentally.
- A cancelled event could be made active without a new enquiry.

## Handoff

Report the final status transitions and irreversible cancellation behavior.
