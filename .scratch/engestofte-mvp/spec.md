# Engestofte Gods — MVP-specifikation

**Status:** Klar til implementation
**Kilde:** `docs/projekt/02-projekt.md` og den godkendte grilling-session
**Relaterede tickets:** `.scratch/engestofte-mvp/issues/`
**Test-seam:** Én samlet end-to-end brugerrejse fra offentlig kontakt til godkendt event

## Problem Statement

Engestofte Gods modtager bryllupsforespørgsler, som kan indeholde mange detaljer, gentagelser og efterfølgende afklaringer. Kunden skal i dag kunne have svært ved at vide, hvilke oplysninger der er relevante, mens Johan risikerer at bruge tid på at samle oplysninger fra flere steder.

Projektet skal derfor skabe én samlet og overskuelig forespørgsel. Kunden skal kunne starte trygt og uforpligtende, bruge tid på at beskrive sit bryllup og først oprette bruger eller logge ind, når AI-flowet er færdigt. Johan/Owner skal derefter kunne gennemgå sagen, afklare mangler og godkende eventet, uden at AI’en overtager den menneskelige beslutning.

## Solution

MVP’en består af et sammenhængende kundeflow:

```text
engestofte-gods.dk/kontakt
        ↓
AI-flow på en ny page
        ↓
localStorage-kladde
        ↓
Login eller register
        ↓
/dashboard/events/
        ↓ Owner/Johan gennemgår og godkender
/dashboard/events/{id}
        ↓
Event-data, Messenger, godkendelser og booking
```

Den offentlige entry-side præsenterer Engestofte Gods med smiley-rapport, billeder af ledelsen og sprogvalg. AI-flowet indsamler kritiske grundoplysninger, relevante opfølgende oplysninger og mulige mersalgsbehov. Kunden kan forlade flowet uden at oprette konto, fordi kladden gemmes lokalt.

Efter login eller register sendes forespørgslen til backend og databasen. Kunden kan se status i `/dashboard/events/`, men får først adgang til den konkrete event-platform efter Owner/Johans godkendelse. Den godkendte event-platform ligger under `/dashboard/events/{id}` og indeholder eventdata samt en Messenger, der kun hører til dette event.

## Target group

The primary external user is a potential wedding customer. The primary internal user is Johan as `Owner`. Additional contact persons share the customer-side event relationship. `Staff` is an operational read-only role, not a communication or approval role.

## Goals

- Make it easy to begin a wedding enquiry without early authentication friction.
- Collect a complete, structured and source-aware enquiry.
- Reduce repeated manual clarification for Johan.
- Preserve human control over availability, pricing, upsell and approval.
- Give the customer a trustworthy status and event experience after approval.
- Create one source of truth for the request and later event.

## Non-goals

- Replace all existing Engestofte systems.
- Automate commercial or booking decisions without Owner approval.
- Build a production-ready legal, payment or notification platform in the school MVP.

## User flow

1. Customer opens `engestofte-gods.dk/kontakt`.
2. Customer sees Engestofte presentation, smiley-report, leadership images and language selector.
3. Customer starts the AI-flow on a separate page.
4. AI collects five steps of wedding information and shows progress.
5. AI stores the raw draft locally and reaches `DONE`.
6. Customer registers or logs in.
7. The local draft is submitted and appears under `/dashboard/events/`.
8. Owner reviews, asks clarifying questions and approves the request.
9. The customer receives access to `/dashboard/events/{id}`.
10. Customer and Owner communicate, propose changes and approve the final data.
11. Customer simulates depositum payment and reaches `Booket`, or deliberately cancels the event.

## Functional requirements

- The system must support the agreed five-step wedding enquiry.
- Critical, conditional and optional fields must behave according to `flow-definition.md`.
- The AI must return structured flow state and must not guess unsupported facts.
- The system must preserve local drafts before authentication and persist requests after authentication.
- The system must separate request overview access from approved event-platform access.
- The system must isolate event data and Messenger per event.
- The system must enforce Owner, primary contact person, additional contact person and Staff boundaries.
- The system must preserve bilateral field-level approval history.
- The system must track unread messages per recipient and apply seven-day escalation.
- The system must support simulated depositum and irreversible customer cancellation behavior.

## Content requirements

- Customer-facing information must use Engestofte’s personal, clear and trustworthy tone.
- AI answers based on project material must expose an appropriate source reference.
- Commercial suggestions must be labelled as non-binding proposals.
- Internal notes and AI assessments must not be shown to customers.
- The interface must explain uncertainty, unavailable dates, approval states and cancellation consequences.
- The interface must support Danish, English and German, with Danish as default.

## Design requirements

- `/kontakt` should feel like an Engestofte Gods website rather than a generic chatbot.
- The flow should show progress and one manageable question at a time.
- The event platform should use a two-panel layout: event data on the left and Messenger on the right.
- Important statuses and approval consequences must be visually clear.
- The design should follow the documented Engestofte green, Alice-inspired typography, sharp corners and professional contrast.
- The system must make AI involvement visible rather than presenting AI as a human.

## Acceptance criteria

- [ ] A customer can complete the agreed public-to-event journey without authentication before the AI-flow is complete.
- [ ] A local draft can be resumed and is only persisted to the system after login/register.
- [ ] Owner can review and approve a submitted request without exposing internal information to the customer.
- [ ] Approval opens the correct event-specific platform and Messenger.
- [ ] Customer and Owner approvals are required for final event-data changes.
- [ ] Owner and Staff permissions match the documented role boundaries.
- [ ] Unread and seven-day important-message behavior is recipient-specific.
- [ ] Simulated depositum produces `Booket`, while deliberate customer exit produces non-reopenable `Annulleret af kunde`.

## Testable boundaries

- Public boundary: `/kontakt` to `/ai-flow` navigation and language behavior.
- AI boundary: structured question/answer, source grounding, uncertainty and non-binding suggestions.
- Persistence boundary: local draft, authentication handoff and request creation.
- Authorization boundary: customer, primary contact person, Owner and Staff access to the same event.
- State boundary: request review, approval, change proposals, unread escalation, depositum, booking and cancellation.
- Event boundary: isolation of event data and Messenger between two events.

## User Stories

1. As a potential wedding customer, I want to visit `/kontakt`, so that I can understand that I am contacting Engestofte Gods.
2. As a potential wedding customer, I want to see a smiley-report link, so that I can assess the venue’s visible food-compliance information.
3. As a potential wedding customer, I want to see images of the leaders, so that the first contact feels personal and trustworthy.
4. As a potential wedding customer, I want Danish as the default language, so that I can start without configuring anything.
5. As a potential wedding customer, I want to choose Danish, English or German from a dropdown, so that I can use the enquiry flow in a language I understand.
6. As a potential wedding customer, I want a clear button from `/kontakt` to the AI-flow, so that I know how to begin.
7. As a potential wedding customer, I want the AI-flow to ask one relevant question at a time, so that the process feels manageable.
8. As a potential wedding customer, I want to see progress through the flow, so that I understand how far I am from completing my enquiry.
9. As a potential wedding customer, I want to provide a desired date or date range, so that Owner can assess the request.
10. As a potential wedding customer, I want to provide the number of guests, so that the enquiry can be assessed correctly.
11. As a potential wedding customer, I want to describe the ceremony, location and basic arrangement, so that Engestofte receives the necessary context.
12. As a potential wedding customer, I want to describe reception, dinner and party wishes, so that the request represents the whole day.
13. As a potential wedding customer, I want to describe food, drinks, cake, cocktails, late-night food and other extras, so that relevant options can be considered.
14. As a potential wedding customer, I want to provide overnight-stay and transport needs, so that practical arrangements are included early.
15. As a potential wedding customer, I want to provide allergies, dietary requirements, special wishes and practical notes, so that important details are not forgotten.
16. As a potential wedding customer, I want optional information to remain optional, so that I am not blocked by details I cannot answer yet.
17. As a potential wedding customer, I want the AI to identify missing critical information, so that I know what must be clarified before submission.
18. As a potential wedding customer, I want the AI to show conflicting answers clearly, so that I can choose the correct value instead of the system guessing.
19. As a potential wedding customer, I want the AI to explain when it is uncertain, so that I can trust the boundary between known and unknown information.
20. As a potential wedding customer, I want the AI to identify an explicitly unavailable date, so that I do not waste Owner’s or my own time on an obviously impossible request.
21. As a potential wedding customer, I want to continue after an unavailable-date warning if I wish, so that Owner can help find an alternative date.
22. As a potential wedding customer, I want to see relevant non-binding suggestions, so that I can discover options that may improve our day.
23. As a potential wedding customer, I want a lower guest count to be eligible for an intimate-wedding suggestion, so that I can hear about a relevant Engestofte opportunity.
24. As a potential wedding customer, I want overnight needs to trigger relevant breakfast or transport suggestions, so that practical solutions are visible.
25. As a potential wedding customer, I want suggestions to be clearly non-binding, so that I am not misled into believing that a price or booking is final.
26. As a potential wedding customer, I want the raw draft to be saved locally, so that I can return to the flow without losing my work.
27. As a potential wedding customer, I want to complete the AI-flow before login or register, so that authentication does not make me abandon the enquiry too early.
28. As a potential wedding customer, I want a clear `DONE` state, so that I understand that the enquiry information is complete.
29. As a potential wedding customer, I want to create an account or log in after finishing the flow, so that my enquiry can be submitted.
30. As a customer with a password problem, I want normal password reset, so that account access problems are not confused with leaving an event.
31. As a customer, I want my submitted enquiry to appear under `/dashboard/events/`, so that I can follow its progress.
32. As a customer, I want to see date, time and status for my submitted enquiry, so that I know whether action is needed.
33. As a customer, I want the full event platform hidden until Owner approval, so that unfinished internal work is not presented as final.
34. As Owner, I want to receive submitted enquiries, so that I can review potential customers in one place.
35. As Owner, I want a structured summary, so that I do not need to reconstruct the customer’s answers manually.
36. As Owner, I want to see missing information, conflicts and uncertainty, so that I know what requires human judgement.
37. As Owner, I want to see the AI’s source references and source versions, so that I can assess the basis for an answer.
38. As Owner, I want to see AI upsell suggestions, so that I can follow up on relevant opportunities for additional revenue.
39. As Owner, I want to add internal notes, so that private assessments are kept separate from customer-facing information.
40. As Owner, I want to ask the customer for clarification, so that an unclear request can move forward.
41. As Owner, I want to approve a request, so that the customer can receive access to the concrete event platform.
42. As a customer, I want access to `/dashboard/events/{id}` after approval, so that I can see my own event.
43. As a customer, I want event data on the left side of the event platform, so that I can see the current arrangement and relevant shared notes.
44. As a customer, I want an event-specific Messenger on the right side, so that communication stays connected to the correct event.
45. As a contact person, I want messages to show sender, image, date, time and content, so that communication has clear human context.
46. As a contact person, I want my account to be usable across multiple events, so that I do not need a separate account for every event.
47. As a contact person, I want each event’s data and Messenger to remain isolated, so that one customer event cannot expose another event.
48. As the primary contact person, I want to manage other contact persons, so that I can control who has access to the customer’s event.
49. As an additional contact person, I want to remove myself from an event, so that I can leave an event I should no longer access.
50. As Owner, I want full access to contact-person management, so that I can correct access when necessary.
51. As a contact person, I want to propose a change to event data, so that the shared arrangement stays accurate.
52. As Owner, I want to propose a change to event data, so that I can record information agreed during a conversation or meeting.
53. As a customer, I want to approve or reject Owner’s changes, so that no change becomes final without my explicit confirmation.
54. As Owner, I want customer changes to require my approval, so that critical information is always reviewed.
55. As a customer, I want rejected changes to include an explanation, so that I understand what must be clarified.
56. As a customer or Owner, I want change history preserved, so that previous approved and rejected values remain traceable.
57. As a message recipient, I want new Messenger messages to be visibly unread, so that I know when action may be needed.
58. As a contact person, I want opening the event platform to count as reading the event’s new messages, so that a separate mark-as-read action is unnecessary.
59. As a contact person or Owner, I want an unread message to become a grouped `Vigtig besked` after seven days, so that important communication is not forgotten.
60. As a contact person or Owner, I want the seven-day rule evaluated separately for me, so that another recipient opening the event does not mark my messages as read.
61. As Owner, I want Staff to see relevant operational event information, so that the kitchen and event team can do their work.
62. As Staff, I want read-only access to dietary requirements and allergies, so that I can prepare safely without editing customer decisions.
63. As Staff, I want no access to Messenger or direct customer identifiers, so that access is limited to operational needs.
64. As a customer, I want a depositum button only after customer and Owner agreement, so that payment follows an agreed request.
65. As a customer, I want the school-project depositum simulation to show `Depositum betalt`, so that the booking flow can be demonstrated without real money.
66. As a customer, I want the status to become `Booket` after simulated payment, so that the event has a clear customer-friendly outcome.
67. As a customer, I want a friendly confirmation after booking, so that the final interaction feels personal and positive.
68. As the primary contact person, I want a strong multi-step confirmation before leaving an event, so that I cannot accidentally cancel a major life event.
69. As a customer, I want cancellation to be explicit about the loss of a paid depositum, so that I understand the consequence before confirming.
70. As Owner, I want an annulled event to remain historical and not reopen, so that the request history is trustworthy.

## Implementation Decisions

- The first active arrangement type is wedding; other arrangement types are out of scope for the MVP.
- The public entry route is `/kontakt`.
- The AI-flow is a separate page reached from the entry side and contains the enquiry functionality.
- The AI-flow has five visible steps plus a final `DONE` state.
- Critical, conditional and optional information are defined in `docs/grilling/flow-definition.md`.
- Contact data is collected as part of login/register at the end of the flow, not as an early abandonment barrier.
- Raw draft data is stored in browser `localStorage` before authentication.
- The localStorage choice is acceptable only as a school-prototype decision and is not a production GDPR approval.
- The request is persisted in the backend/database only after successful login or registration.
- `/dashboard/events/` is the customer’s request overview and status page.
- `/dashboard/events/{id}` is the concrete event platform and is opened only after Owner/Johan approval.
- An event has its own data and Messenger thread; there is no general customer Messenger.
- Contact persons and Owner can send Messenger messages. Staff cannot send messages, and AI cannot appear as sender.
- The primary contact person has special access responsibility. The primary contact person and Owner can manage other contact persons; additional contact persons can remove themselves.
- A user account may be connected to multiple events, with separate permissions and data isolation per event.
- Customer and Owner approvals are separate. Both approvals are required for relevant final event data.
- Every event-data change is represented as a field-level proposal with old value, proposed value, proposing party, approval state and history.
- Rejected proposals preserve the previous approved value and require an explanation.
- Owner is the final authority when official documents conflict with an explicit Owner decision. The conflict remains visible internally and the Owner gives a reason for the override.
- The AI must not guess unavailable dates, prices, facilities, capacity or rules.
- Known unavailable dates are communicated to the customer, who may continue so Owner can help find an alternative.
- AI upsell is permitted both during the customer flow and in the Owner view, but suggestions are non-binding until Owner approves the final solution and price.
- Danish is the default language. Danish, English and German are available through a dropdown, and the selected language belongs to the request.
- IP-based language detection is a future possibility and is not implemented in the school project.
- New messages are unread per recipient. Opening the event platform marks the relevant event messages read for that recipient.
- After seven days without opening the event platform, unread messages are grouped into one `Vigtig besked` per conversation for each affected contact person or Owner.
- Owner has full access. Staff has read-only access to operational data, including dietary information where necessary, but not Messenger or direct identifiers.
- Depositum is simulated; no real payment provider or real money is used.
- `Booket` requires both-party approval and simulated depositum payment.
- A primary-contact exit is a deliberate multi-step cancellation. It produces `Annulleret af kunde`, does not refund paid depositum and cannot be reopened.
- A cancelled event requires a new enquiry if the customer returns later.
- The highest verification seam is one end-to-end customer journey with role-aware transitions through the public page, AI-flow, authentication, request dashboard, Owner approval, event platform and booking.

## Testing Decisions

- Tests should verify externally visible behavior and domain outcomes rather than implementation details.
- The primary seam is an end-to-end journey across the customer and Owner experience. It should verify the route transitions and state changes from `/kontakt` through `/dashboard/events/{id}`.
- The same journey should verify that an unauthenticated draft remains local, that authentication persists it, and that the customer cannot access the concrete event before approval.
- Role behavior should be verified at the boundary: contact person, primary contact person, Owner and Staff must see and change only what their role permits.
- Domain behavior should be verified for conflicts, field-level approvals, unread messages, seven-day escalation, depositum simulation and irreversible customer cancellation.
- AI behavior should be verified through observable structured outcomes: required fields, uncertainty markers, unavailable-date handling, source references and non-binding upsell suggestions.
- No test, lint, typecheck, build or Maven command is run by the agent unless the prompt explicitly requests it.
- No existing project test prior art was used as a basis for this documentation-only spec; the implementation phase should first identify the highest existing browser/API seam before adding new test infrastructure.

## Out of Scope

- Production GDPR or EU-law approval of raw sensitive data in localStorage.
- Real payment processing, payment-provider integration or depositum refunds.
- Automatic calendar availability or booking confirmation.
- Binding prices, contracts, offers or automatic acceptance of bookings.
- Automatic email sending; deployment limitations are accepted in the school project.
- Full replacement of Squarespace, Trello, e-conomic or other existing systems.
- Full support for parties, hunting, conferences or Christmas markets.
- Cloudflare/IP-based language detection.
- Guaranteed bus availability, transport price or external supplier booking.
- A general customer Messenger outside a concrete event.
- Staff editing, approval, user administration or customer communication.
- AI sending messages as a human or making final commercial decisions.

## Further Notes

- `docs/grilling/01-projekt-afklaring.md` contains the detailed decision history and should be consulted when a ticket appears ambiguous.
- `docs/grilling/flow-definition.md` is authoritative for the five AI-flow steps and field classification.
- `CONTEXT.md` is the shared domain vocabulary; new tickets should use its terms instead of inventing synonyms.
- `docs/diagrammer/systemskitse.md` and `docs/diagrammer/systemskitse.svg` show the agreed high-level system flow.
- The tickets are stored locally because no issue-tracker configuration is present in the repository. They are numbered in dependency order and each ticket is intended to fit in a fresh implementation context.
- Existing changes in `frontend/`, `backend/`, `9.md`, `10.md` and `11.md` are outside this documentation/specification change and must be preserved.
