# 04: Harden AI flow behavior and guardrails

**What to build:** Make the OpenAI-backed intake assistant reliably validate customer answers, surface uncertainty and conflicts, and follow the approved enquiry rules without inventing facts.

**Blocked by:** 03: Prove the OpenAI-backed structured AI-flow seam

**Status:** complete; owner approved

## In scope

- Keep the AI focused on the current wedding-enquiry step and the structured response contract.
- Apply the critical, conditional and optional field classifications in `docs/grilling/flow-definition.md`.
- Detect missing, unclear or conflicting answers in the supplied conversation and ask one focused follow-up without silently choosing a value.
- Preserve facts volunteered earlier in the conversation and avoid asking the customer to repeat them.
- State uncertainty when a requested venue fact is not explicitly available in the system instructions, rubric or supplied flow context; direct the customer to Engestofte for confirmation.
- Never claim that availability, prices, capacity, package details, facilities, policies, transport or a booking have been checked or confirmed unless that exact fact is supplied as trusted context.
- Treat calendar availability as unknown except for the school-project assumption that all 2028 dates are unbooked in the demo calendar. A 2028 date outside July may be described as free in the project calendar, without promising a booking. For July, explain the documented typical closure and rare possible exceptions, then let the customer choose whether Engestofte should check or they prefer another date. Do not infer availability for other years from seasonal patterns.
- Do not recommend the Intimpakke based on guest count until its contents, current price and value versus the standard package are approved. The 2026 standard package document is historical and not a current quote. The bus investigation may be offered when accommodation is declined; it is optional and non-binding.
- Keep system instructions, user prompt and JSON rubric in backend resources, separate from provider request construction.

## Out of scope

- RAG, Dify, vector stores, retrieval services or a separate knowledge base.
- Customer or Owner source citations and source-version tracking.
- Calendar or availability integration and automatic date checks.
- New arrangement types, authentication, final enquiry submission or Owner review UI.
- Calendar integration, complete booking horizon, current prices, confirmed Intimpakke contents, or capacity integration.

## Acceptance criteria

- [x] The model returns the current step unchanged and follows the structured status contract.
- [x] Missing critical information produces one focused follow-up; conditional information blocks only when relevant; optional information never blocks completion.
- [x] Conflicting answers are surfaced as a question and the model does not silently select a value.
- [x] An unsupported venue-fact question is answered with clear uncertainty and a human follow-up path, without an invented fact or claim of verification.
- [x] The model applies the project assumption that 2028 is unbooked, while preserving the July policy and avoiding booking promises. Availability for other years is not inferred.
- [x] The model does not recommend or describe an undocumented Intimpakke. It may offer to investigate bus transport when accommodation is declined.

## Verification

The prompt and rubric encode the required outcomes. Live-provider evaluation may be performed separately, but is not a completion blocker for this school-project ticket.

## Stop and ask if

- A requested behavior requires an unapproved business rule or trusted data source.
- Flow classifications conflict with `docs/grilling/flow-definition.md`.
- The structured response cannot represent uncertainty or conflict without changing an approved API contract.

## Handoff

Report the implemented guardrails, observable response behavior, date-availability boundary and any AI behavior that still requires evaluation against a live provider.
