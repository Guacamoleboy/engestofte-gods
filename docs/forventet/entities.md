# Forventede entities og domæner

**Status:** Foreløbigt MVP-design

Dette dokument er et bud på de domæneområder og entities, som sandsynligvis er nødvendige for en komplet MVP. Det er ikke en færdig JPA-model eller migrationsplan. Entities skal først implementeres, når den relevante ticket og API-kontrakt er klar.

## Vigtig modelbeslutning

`Customer`, `Owner` og `Staff` bør ikke nødvendigvis være tre forskellige bruger-tabeller. De er primært roller eller relationer omkring en fælles `UserAccount`.

- `UserAccount` er den autentificerede konto.
- `Role` beskriver systemrollen, eksempelvis `OWNER`, `STAFF` eller `CUSTOMER`.
- En `UserAccount` kan være kontaktperson på flere events.
- Den primære kontaktperson er en relationsegenskab på en konkret forespørgsel/event, ikke nødvendigvis en særskilt kontotype.

## MVP-entities

| Entity | Domæne | Ansvar | MVP-status |
|---|---|---|---|
| `UserAccount` | Identity | Login, registrering, password-hash, email og kontoens status. | Skal med |
| `Role` | Identity | Roller som `OWNER`, `STAFF` og `CUSTOMER`. | Skal med |
| `UserRole` | Identity | Relation mellem konto og rolle, hvis en konto kan have flere roller. | Skal med ved flere roller |
| `WeddingEnquiry` | Enquiry | Den indsendte bryllupsforespørgsel og dens samlede svar. | Skal med |
| `EnquiryAnswer` | Enquiry | Et struktureret felt, værdi og eventuel kilde/konflikt. | Skal med |
| `EnquiryContact` | Enquiry | Knytter en `UserAccount` til forespørgslen og markerer primær kontaktperson. | Skal med |
| `Event` | Event | Den konkrete event-platform, som oprettes efter Owner-godkendelse. | Skal med |
| `Message` | Communication | En besked knyttet til ét event. | Skal med |
| `MessageRecipient` | Communication | Modtagerrelation med `readAt` og syv-dages eskalering pr. modtager. | Skal med |
| `ChangeProposal` | Approval | En konkret før-/efter-ændring i eventdata. | Skal med |
| `Approval` | Approval | En kundes eller Owners separate godkendelse af et proposal. | Skal med |
| `StatusHistory` | Workflow | Historik over statusændringer med aktør og tidspunkt. | Anbefalet |
| `DepositumRecord` | Booking | Simuleret depositumstatus og tidspunkt. | Skal med i prototype |
| `AiAssessment` | AI | AI-opsummering, usikkerheder, konflikter og interne vurderinger. | Skal med efter behov |
| `UpsellSuggestion` | AI/Sales | Et ikke-bindende mersalgsforslag med status og eventuel Owner-beslutning. | Skal med efter behov |
| `SourceReference` | AI/Knowledge | Kilde, version og reference til grundlaget for AI-svar. | Skal med efter behov |

## Entities der ikke bør oprettes automatisk endnu

- `ImportantMessage` bør i første omgang være en afledt visning af `MessageRecipient`, hvor `readAt` mangler efter syv dage. En separat tabel bør først oprettes, hvis der senere er behov for en selvstændig notifikationshistorik.
- En lokal AI-kladde bør ikke være en database-entity før login/register. Den ligger i browserens `localStorage` i skoleprototypen.
- `KnowledgeDocument` og vektordata kan ligge i OpenAI/RAG-infrastrukturen eller en separat knowledge store. De bør ikke kopieres ind i den almindelige event-database uden en konkret beslutning.
- `Customer` og `Staff` bør ikke duplikere `UserAccount`; roller og eventrelationer bør styre adgangen.

## Forventede statusværdier

### `WeddingEnquiry.status`

```text
DRAFT
SUBMITTED
UNDER_REVIEW
AWAITING_CUSTOMER
APPROVED
CANCELLED_BY_CUSTOMER
```

### `Event.status`

```text
AWAITING_APPROVAL
APPROVED
AWAITING_DEPOSIT
BOOKED
CANCELLED_BY_CUSTOMER
```

Statusnavne skal mappes til kundevendte tekster som `Under gennemgang`, `Afventer depositum`, `Booket` og `Annulleret af kunde`.

## Entity-regler

- Entities må ikke bruges direkte som API-response.
- Request- og response-DTO’er skal være separate.
- Mappere skal holde konvertering mellem DTO og entity adskilt fra business logic.
- Direkte identificerende brugerdata skal begrænses efter rolle.
- Staff må se nødvendige operationelle oplysninger, men ikke Messenger eller fulde direkte identifikatorer.
- Eventets beskeder og data skal være isoleret fra andre events.
- Alle ændringer, approvals og statusændringer skal kunne spores med aktør og tidspunkt.
- Alle nye entities følger [architecture-and-file-conventions.md](../standards/architecture-and-file-conventions.md).

## Åbne modelspørgsmål

Disse punkter må ikke gættes af en agent under implementation:

- Om IDs skal være UUID eller database-genererede integers.
- Om `EnquiryAnswer` skal være en normaliseret tabel eller en JSONB-struktur.
- Om `UserRole` er nødvendig, eller om én konto kun må have én rolle i MVP’en.
- Om AI-vurdering og source references skal gemmes i PostgreSQL eller kun i en ekstern AI/RAG-løsning.
- Om `Event` skal være én-til-én med `WeddingEnquiry` eller kunne oprettes flere gange gennem historikken.
