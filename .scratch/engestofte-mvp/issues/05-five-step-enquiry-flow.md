# 05: Build the complete five-step wedding enquiry flow

**What to build:** A customer can complete the agreed five-step wedding enquiry with critical, conditional and optional information.

**Blocked by:** 04: Harden AI flow behavior and guardrails

**Status:** implemented; review passed; behavioral verification deferred

## In scope

- Follow the authoritative sequence and field classifications in `docs/grilling/flow-definition.md`:
	1. Contact person's name.
	2. Desired wedding date or date range.
	3. Expected guest count and accommodation needs.
	4. Wedding format: ceremony, reception, dinner, party and ceremony location/form.
	5. Food, drink, dietary needs, optional extras, practical wishes and budget.
- Localize the fixed introductory question for each step in Danish, English and German.
- Show progress through five steps, then a distinct completed state.
- Preserve question/answer turns and show a readable summary before the flow's DONE handoff.
- Ask one relevant follow-up at a time; keep required, conditional and optional classifications from the rubric.
- Surface conflicting values and ask the customer to resolve them without discarding either answer.

## Out of scope

- Authentication or database submission.
- Final event dashboard.
- New arrangement types beyond wedding.

## Acceptance criteria

- [x] The customer can move through all five steps and see current progress.
- [x] Critical fields block completion when missing or unresolved.
- [ ] Conditional fields become relevant only when the customer’s answers require them.
- [x] Optional fields do not block completion.
- [x] Conflicting values are surfaced without silently choosing a winner.
- [x] The customer receives a readable summary before the login/register handoff.
- [x] Step order matches `docs/grilling/flow-definition.md` in all supported languages.
- [x] Completion is shown as a distinct state rather than as a sixth enquiry step.

## Verification

The owner declined tests for this ticket. The frontend typecheck was attempted but could not run because TypeScript dependencies are absent; `npm ci` was blocked by a locked Windows binary in `node_modules`. Inspect the localized progress indicator and final question/answer summary manually when dependencies are available.

## Stop and ask if

- A field classification differs from `flow-definition.md`.
- The flow needs a new business rule not recorded in the decision log.
- A missing answer would affect a safety, price or availability decision.

## Handoff

Report the completed step model, field states and any deferred questions for the authentication ticket.
