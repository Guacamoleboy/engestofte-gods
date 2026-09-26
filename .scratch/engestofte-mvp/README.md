# Engestofte MVP ticket map

These tickets are the implementation queue for the approved MVP specification. They follow the teaching material’s spec-driven workflow:

1. Grill and record domain decisions.
2. Maintain the glossary and specification.
3. Implement one small, vertical ticket at a time.
4. Demonstrate the ticket externally before moving to the next frontier.
5. Review both standards and specification compliance.

## Dependency graph

```text
01 → 02 → 03 → 04 → 05 → 06 → 07 → 08 → 09 → 10 → 11 → 12 → 15
                                                        ↘ 13
                                   10 ───────────────────→ 14
```

Ticket 13 may start after Ticket 11 and can proceed in parallel with Ticket 12. Ticket 14 may start after Ticket 10 and can proceed in parallel with Tickets 11–13. Ticket 15 remains blocked by bilateral approval in Ticket 12.

## Shared agent guardrails

- Read the MVP specification, `CONTEXT.md`, `docs/grilling/flow-definition.md` and the relevant ticket before changing code.
- Implement only the ticket’s scope. Do not perform unrelated refactors or dependency upgrades.
- Preserve existing public contracts unless the ticket explicitly changes them.
- Do not modify `9.md`, `10.md` or `11.md` as part of implementation.
- Do not change unrelated frontend or backend domains.
- Stop and ask when a requirement conflicts with the spec, glossary or an existing ADR.
- Verification is behavioral and proportional. The agent does not run tests, lint, typecheck, build or Maven unless the prompt explicitly requests it.
- Handoff must include changed areas, manual verification, assumptions, limitations and the recommended next ticket.
