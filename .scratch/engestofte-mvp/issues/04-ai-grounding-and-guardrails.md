# 04: Add source grounding and AI guardrails

**What to build:** The AI-flow answers only from approved project knowledge and clearly escalates uncertainty, dates and commercial decisions.

**Blocked by:** 03: Prove the OpenAI-backed structured AI-flow seam

**Status:** ready-for-agent

## In scope

- Retrieval or equivalent use of approved Engestofte project knowledge.
- Simple source references for customer-facing answers and fuller source metadata for Owner.
- No-guessing behavior for missing, conflicting or unsupported information.
- Known unavailable-date handling and an option to continue for Owner follow-up.
- Non-binding upsell suggestion behavior.

## Out of scope

- Automatic calendar availability checks.
- Binding prices, packages, offers or bookings.
- Uploading unapproved copyrighted or personal material to an external model.

## Acceptance criteria

- [ ] A supported answer includes a traceable source reference.
- [ ] An unsupported answer is marked uncertain and asks for clarification or escalates to Owner.
- [ ] The AI never invents a date, price, facility, capacity or rule.
- [ ] A known unavailable date is explained and the customer can continue for human follow-up.
- [ ] Upsell suggestions are clearly optional and non-binding.

## Verification

Demonstrate one sourced answer, one unsupported question, one unavailable date and one relevant upsell scenario. Inspect the visible output rather than model internals.

## Stop and ask if

- A source is outdated, contradictory or missing an Owner decision.
- Personal or sensitive data would be sent to an external provider without an approved basis.
- The AI output cannot be distinguished from a binding commercial commitment.

## Handoff

List the sources, guardrails and unresolved knowledge gaps. The next ticket may use these rules to build the complete flow.
