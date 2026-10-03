# 03: Prove the OpenAI-backed structured AI-flow seam

**What to build:** The first real AI interaction: a customer can answer one enquiry question and receive the next structured flow state through an OpenAI-backed provider seam.

**Blocked by:** 01: Establish the application shell and generic UI foundation; 02: Build the Engestofte Gods contact entry side

**Status:** complete

## In scope

- A provider boundary that can call OpenAI without coupling the UI directly to the provider.
- One structured enquiry interaction with a predictable response shape.
- Visible question, customer answer, pending state and recoverable provider error.
- Explicit indication that the customer is interacting with AI.

## Out of scope

- The complete five-step flow.
- Authentication, complete wedding-enquiry submission or Owner review.
- Storing real secrets in source control or browser code.
- Persisting the complete wedding-enquiry aggregate; this ticket stores only the submitted AI interaction and its structured result.

## Acceptance criteria

- [x] `/ai-flow` displays one relevant wedding-enquiry question.
- [x] A customer answer produces a structured next state rather than unstructured text only.
- [x] Provider loading and provider failure are visible and recoverable.
- [x] The interface identifies the interaction as AI-assisted.
- [x] The provider can be replaced without rewriting the page’s domain state handling.
- [x] The backend stores each submitted answer, current question, language and structured AI result as an application-owned AI interaction.

## Verification

Demonstrate one successful question/answer cycle and one provider-error path. When OpenAI credentials or access are unavailable, the backend returns an error and the frontend presents its localized pause state; do not use a local/mock provider or expose credentials.

## Stop and ask if

- OpenAI data handling would require sending real personal or sensitive data.
- The model response cannot be made structurally valid without guessing.

## Handoff

Document the structured interaction contract, provider assumptions and known limitations for the guardrails ticket.
