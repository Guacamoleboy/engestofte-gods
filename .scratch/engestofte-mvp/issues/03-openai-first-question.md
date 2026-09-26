# 03: Prove the OpenAI-backed structured AI-flow seam

**What to build:** The first real AI interaction: a customer can answer one enquiry question and receive the next structured flow state through an OpenAI-backed provider seam.

**Blocked by:** 01: Establish the application shell and generic UI foundation; 02: Build the Engestofte Gods contact entry side

**Status:** ready-for-agent

## In scope

- A provider boundary that can call OpenAI without coupling the UI directly to the provider.
- One structured enquiry interaction with a predictable response shape.
- Visible question, customer answer, pending state and recoverable provider error.
- Explicit indication that the customer is interacting with AI.

## Out of scope

- The complete five-step flow.
- RAG retrieval, authentication, persistence or Owner review.
- Storing real secrets in source control or browser code.

## Acceptance criteria

- [ ] `/ai-flow` displays one relevant wedding-enquiry question.
- [ ] A customer answer produces a structured next state rather than unstructured text only.
- [ ] Provider loading and provider failure are visible and recoverable.
- [ ] The interface identifies the interaction as AI-assisted.
- [ ] The provider can be replaced without rewriting the page’s domain state handling.

## Verification

Demonstrate one successful question/answer cycle and one provider-error recovery path. Use a safe local/mock response when credentials are unavailable; do not expose credentials.

## Stop and ask if

- The provider contract needs a database schema or dependency not already approved.
- OpenAI data handling would require sending real personal or sensitive data.
- The model response cannot be made structurally valid without guessing.

## Handoff

Document the structured interaction contract, provider assumptions and known limitations for the guardrails ticket.
