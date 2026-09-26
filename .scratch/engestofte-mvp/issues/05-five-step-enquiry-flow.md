# 05: Build the complete five-step wedding enquiry flow

**What to build:** A customer can complete the agreed five-step wedding enquiry with critical, conditional and optional information.

**Blocked by:** 04: Add source grounding and AI guardrails

**Status:** ready-for-agent

## In scope

- Step 1: basic information.
- Step 2: wedding, ceremony, reception, dinner and party.
- Step 3: food, drinks, cake, cocktails, late-night food and extras.
- Step 4: overnight stay and transport.
- Step 5: additional information, allergies, special wishes, practical notes and budget where offered.
- Visible progress and one relevant question at a time.

## Out of scope

- Authentication or database submission.
- Final event dashboard.
- New arrangement types beyond wedding.

## Acceptance criteria

- [ ] The customer can move through all five steps and see current progress.
- [ ] Critical fields block completion when missing or unresolved.
- [ ] Conditional fields become relevant only when the customer’s answers require them.
- [ ] Optional fields do not block completion.
- [ ] Conflicting values are shown without silently choosing a winner.
- [ ] The customer receives a readable summary before completion.

## Verification

Complete one normal wedding path, one path with optional omissions, one path with a conditional overnight requirement and one path with a conflict.

## Stop and ask if

- A field classification differs from `flow-definition.md`.
- The flow needs a new business rule not recorded in the decision log.
- A missing answer would affect a safety, price or availability decision.

## Handoff

Report the completed step model, field states and any deferred questions for the authentication ticket.
