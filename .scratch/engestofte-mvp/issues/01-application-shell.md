# 01: Establish the application shell and generic UI foundation

**What to build:** A minimal, navigable React/TypeScript application shell with the generic layout primitives needed by the later customer and internal flows.

**Blocked by:** None (can start immediately)

**Status:** ready-for-agent

## In scope

- Establish the agreed design tokens, including the Engestofte green, typography, spacing and sharp-corner visual language.
- Establish the TypeScript/Vite setup required for new frontend code.
- Provide reusable primitives for page layout, buttons, inputs, cards, status labels, dialogs and loading/error states.
- Provide placeholder navigation for `/kontakt`, `/ai-flow`, `/login`, `/register`, `/dashboard/events/` and `/dashboard/events/{id}`.

## Out of scope

- Real AI calls, authentication, persistence or business rules.
- Final page content or event functionality.
- Changes to the existing backend domain model.

## Acceptance criteria

- [ ] The placeholder routes can be opened directly and through navigation.
- [ ] New frontend implementation files use TypeScript/TSX consistently after this setup.
- [ ] The routes share a consistent layout and reusable visual primitives.
- [ ] Reused design values are defined as `--engestofte-<name>` variables in `:root` and consumed with `var(--engestofte-<name>)`.
- [ ] The foundation follows the agreed sharp, professional Engestofte visual identity.
- [ ] Loading, empty and error states have a consistent presentation.

## Verification

Demonstrate the route walkthrough in a browser and show that each placeholder route renders consistently. Do not run automated tests unless the prompt explicitly requests them.

## Stop and ask if

- A dependency or broad configuration change is required.
- The existing frontend structure contradicts the agreed routes.
- A design decision is needed that is not covered by the spec or glossary.

## Handoff

Report the reusable primitives, routes and assumptions. The next ticket should be able to build the real `/kontakt` page without redesigning the foundation.
