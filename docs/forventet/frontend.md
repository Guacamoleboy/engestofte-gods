# Forventet frontendstruktur

**Status:** Foreløbigt MVP-design

Frontend bygges med React, TypeScript og Vite. Nye frontend-filer skal være `.ts` eller `.tsx`; JavaScript er ikke den ønskede standard for ny kode.

## Routes og pages

### Public routes

| Route | Page | Formål |
|---|---|---|
| `/kontakt` | `ContactPage` | Engestofte entry side med smiley-rapport, ledelsesbilleder, sprogvalg og CTA. |
| `/ai-flow` | `AiFlowPage` | Guidet bryllupsforespørgsel med fem synlige trin. |
| `/login` | `LoginPage` | Login efter `DONE` eller for eksisterende brugere. |
| `/register` | `RegisterPage` | Opret konto efter gennemført AI-flow. |
| `/forgot-password` | `ForgotPasswordPage` | Normal password-reset indgang. |

### Protected customer routes

| Route | Page | Formål |
|---|---|---|
| `/dashboard/events/` | `EventsDashboardPage` | Liste over kundens forespørgsler og statusser. |
| `/dashboard/events/:id` | `EventPage` | Kundens godkendte event med eventdata og event-scoped Messenger. |

### Protected internal routes

| Route | Page | Formål |
|---|---|---|
| `/owner/requests/` | `OwnerRequestsPage` | Owners oversigt over indsendte forespørgsler. |
| `/owner/requests/:id` | `OwnerRequestPage` | Gennemgang, AI-opsummering, kilder, konflikter og godkendelse. |
| `/staff/events/:id` | `StaffEventPage` | Read-only operationel eventvisning uden Messenger og direkte persondata. |

Routes kan ændres, hvis den eksisterende router eller en senere ticket fastlægger et bedre navn, men den rollebaserede adgang må ikke fjernes.

## Route protection

### `ProtectedRoute`

`ProtectedRoute` skal kontrollere, at:

- brugeren har en gyldig JWT-session
- token ikke er udløbet
- brugeren er autentificeret, før protected content renderes
- unauthenticated users sendes til `/login`
- den oprindelige destination kan genoptages efter login, når det er sikkert

JWT skal valideres på backend. Frontend-guards er brugeroplevelse og må aldrig være den eneste sikkerhed.

### `RoleRoute` / `RequireRole`

En rollebeskyttelse skal begrænse interne routes:

- `OWNER`: fuld adgang til requests, events, Messenger, approvals og kontaktpersoner.
- `STAFF`: read-only operationel adgang uden Messenger eller direkte persondata.
- `CUSTOMER`: adgang til egne forespørgsler og events, som kontoen er tilknyttet.

### `EventAccessGuard`

Event-routes skal kontrollere, at den aktuelle bruger faktisk er tilknyttet det konkrete event. Et gyldigt JWT må ikke alene give adgang til et valgfrit event-id.

## Expected shared components

### App and layout

- `AppRouter`
- `PublicLayout`
- `DashboardLayout`
- `OwnerLayout`
- `StaffLayout`
- `PageContainer`
- `Header`
- `Footer`
- `LoadingState`
- `EmptyState`
- `ErrorState`
- `StatusBadge`
- `LanguageSelect`

### Contact and public flow

- `LeadershipIntro`
- `SmileyReportLink`
- `StartEnquiryButton`
- `AiDisclosure`
- `AiFlowStepper`
- `FlowStep`
- `QuestionCard`
- `AnswerInput`
- `ConditionalQuestion`
- `ConflictQuestion`
- `UnavailableDateNotice`
- `UpsellSuggestionCard`
- `SourceHint`
- `EnquirySummary`
- `DraftResumeBanner`
- `DoneCard`

### Authentication

- `LoginForm`
- `RegisterForm`
- `ForgotPasswordForm`
- `AuthError`
- `AuthSubmitButton`

### Customer dashboard and event

- `EventsList`
- `EventSummaryCard`
- `RequestStatus`
- `EventDataPanel`
- `CustomerNote`
- `MessengerPanel`
- `MessageList`
- `MessageItem`
- `MessageComposer`
- `UnreadMessageBadge`
- `ImportantMessagesPanel`
- `ContactPersonsPanel`
- `ChangeProposalCard`
- `ApprovalActions`
- `DepositumButton`
- `BookingConfirmation`
- `CancelEventDialog`

### Owner and Staff

- `OwnerRequestList`
- `OwnerRequestSummary`
- `AiAssessmentPanel`
- `SourceReferenceList`
- `InternalNoteEditor`
- `CustomerQuestionComposer`
- `OwnerApprovalPanel`
- `StaffOperationalView`
- `SensitiveDataRedaction`

## Hooks and styling

Hooks must not be implemented inside `.tsx` page or component files. Create sibling `.hooks.ts` files when a component or page needs reusable stateful behavior.

Expected hooks include, when needed:

- `useAuth`
- `useCurrentUser`
- `useEvents`
- `useEvent`
- `useEnquiryDraft`
- `useAiFlow`
- `useMessages`
- `useUnreadMessages`
- `useContactPersons`
- `useChangeProposals`
- `useOwnerRequests`

Component-specific styling belongs in sibling `.module.css` files. Shared design values belong in global `:root` variables with the `--engestofte-<name>` naming convention. Colors use hex or `rgba(...)` values.

## Expected API/data boundaries

- API communication belongs in `src/api/` and uses the shared API client.
- Pages compose components and coordinate route-level state.
- Feature components own feature-specific presentation.
- Hooks own reusable stateful behavior.
- Shared components must not contain wedding-specific business decisions unless explicitly designed for that purpose.
- Frontend DTO/types must represent API contracts and must not expose internal database assumptions unnecessarily.
- Customer, Owner and Staff data must be filtered by backend authorization as well as frontend presentation.

## MVP page completion checklist

- [ ] `/kontakt` matches Engestofte’s entry experience.
- [ ] `/ai-flow` supports the five-step wedding flow and local draft.
- [ ] `/login`, `/register` and `/forgot-password` support the authentication handoff.
- [ ] `/dashboard/events/` shows customer request status.
- [ ] `/dashboard/events/:id` shows event data and Messenger after approval.
- [ ] Owner can review and approve through the internal pages.
- [ ] Staff sees only permitted read-only operational data.
- [ ] JWT and event/role guards protect all private routes.
- [ ] Shared hooks and module CSS are separated from `.tsx` files.
