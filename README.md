# Engestofte Gods — AI-understøttet bryllupsforespørgsel

Engestofte Gods-projektet undersøger, hvordan en dokumentforankret AI-løsning kan gøre det lettere for potentielle bryllupskunder at sende en komplet og kvalificeret forespørgsel. Kunden skal opleve en personlig og tryg kontakt, mens Johan/Owner får én samlet sag at arbejde videre med.

Projektet udvikles af Jonas Meinert Larsen som skoleprojekt på datamatikeruddannelsen. Første MVP fokuserer udelukkende på bryllupper.

## MVP’en kort fortalt

Kundens flow er:

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

### `/kontakt`

Entry-siden skal føles som Engestofte Gods’ egen hjemmeside og indeholder blandt andet:

- smiley-rapport i footeren
- billeder af ledelsen for en personlig oplevelse
- dansk som standardsprog
- sprogdropdown for dansk, engelsk og tysk
- knap, der sender kunden videre til AI-flowet

### AI-flowet

AI-flowet er den centrale del af MVP’en. Det skal:

- indsamle de kritiske grundoplysninger om brylluppet
- stille relevante opfølgende spørgsmål
- opdage manglende eller modstridende svar
- vise relevante, ikke-bindende mersalgsforslag
- håndtere kendt utilgængelige datoer ærligt
- markere usikkerhed og sende uklare forhold videre til Owner
- vise et synligt flow med trin og en afsluttende `DONE`-tilstand

Kundens rå kladde gemmes i `localStorage`, indtil kunden opretter bruger eller logger ind. Først derefter sendes forespørgslen til backend og databasen. Dette er en skoleprototypebeslutning og er ikke en produktionsgodkendelse af GDPR- eller EU-compliance.

### Dashboard og event

Efter login/register sendes kunden til `/dashboard/events/`, hvor kunden kan se status på sine forespørgsler. Kunden kan ikke se den fulde event-platform, før Owner/Johan har godkendt forespørgslen.

Når forespørgslen er godkendt, oprettes eller åbnes kundens konkrete event under `/dashboard/events/{id}`. Her findes:

- eventdata og relevante noter i venstre side
- eventets Messenger i højre side
- beskeder mellem kontaktpersoner og Owner
- ændringsforslag, forklaringer og godkendelser
- status for forespørgsel, depositum og booking

Alle ændringer i eventdata kræver godkendelse fra både kunde og Owner. En primær kontaktperson har særligt adgangsansvar, mens øvrige kontaktpersoner har lige rettigheder til eventets indhold og Messenger.

Owner har fuld adgang. Staff har read-only-adgang til relevante arrangements- og køkkenoplysninger, men ikke Messenger eller direkte identificerende brugerdata.

## Statusflow

```text
Kladde
  → Indsendt
  → Under gennemgang
  → Afventer kunde
  → Godkendt / Event-platform åben
  → Afventer godkendelse
  → Afventer depositum
  → Booket
```

Depositum er simuleret i skoleprojektet. Når både kunde og Owner er enige, kan kunden trykke `Betal depositum`, hvorefter status går til `Booket`. En primær kontaktperson kan med en tydelig flertrinsbekræftelse annullere eventet. Status bliver da `Annulleret af kunde`, depositum refunderes ikke, og eventet kan ikke genåbnes.

## Dokumenter en agent skal læse først

For at minimere gentagen kontekst bør en agent normalt starte her:

1. [Gældende projektbeskrivelse og MVP](docs/projekt/02-projekt.md)
2. [Godkendt grilling og beslutningslog](docs/grilling/01-projekt-afklaring.md)
3. [Fastlagt AI-flow og feltklassifikation](docs/grilling/flow-definition.md)
4. [Domæneordbog](CONTEXT.md)
5. [Visuel systemskitse](docs/diagrammer/systemskitse.md)
6. [Samlet MVP-specifikation](.scratch/engestofte-mvp/spec.md)
7. [Projektviden og kildemateriale](docs/README.md)
8. [Arkitektur- og filkonventioner](docs/standards/architecture-and-file-conventions.md)
9. [Forventede entities, database og frontend](docs/forventet/)
10. [Agent-instruktioner](.github/agent-instructions.md)

RAG-materialet ligger i [RAG/](RAG/), hvis opgaven handler om AI-viden, dokumentkilder eller promptgrundlag.

## Dokumentation og projektfiler

- [Projektets dokumentationsindeks](docs/README.md)
- [Systemskitse som SVG](docs/diagrammer/systemskitse.svg)
- [RAG-indeks](RAG/README.md)
- [Bidragsguide](CONTRIBUTING.md)
- [Arkitektur- og filkonventioner](docs/standards/architecture-and-file-conventions.md)
- [Forventet systemmodel](docs/forventet/)
- [Agent-instruktioner](.github/agent-instructions.md)

## Afgrænsning

MVP’en implementerer ikke endnu:

- automatisk kalenderbooking eller garanteret tilgængelighed
- rigtige betalinger eller betalingsudbyder
- bindende tilbud, kontrakter eller automatisk booking
- fuld understøttelse af fest, jagt, konference eller julemarked
- automatisk e-mailafsendelse
- fuld erstatning af eksisterende Squarespace-, Trello- eller økonomisystemer

## Teknisk struktur

Backendens og frontendens overordnede mappestruktur er fastlagt som grundlag for den videre implementering. Den aktuelle implementering af disse områder ændres ikke som en del af dokumentationsarbejdet.

```text
backend/
└── src/main/
    ├── resources/
    │   ├── prompts/
    │   ├── rubric/
    │   └── http/
    └── java/<base-package>/
        ├── <domain>/
        │   ├── controller/
        │   ├── service/
        │   ├── dao/
        │   ├── entity/
        │   ├── dto/
        │   └── mapper/
        ├── config/
        ├── exception/
        ├── security/
        └── Main.java

frontend/src/
├── api/
├── app/
│   ├── pages/
│   ├── routes/
│   └── layout/
├── features/
└── shared/
    ├── styles/
    ├── data/
    └── components/
```

## Technology stack and shared conventions

The backend is a Java 17 Maven project using `pom.xml`, Javalin, Jackson and a normal domain-based REST API. It currently also includes Hibernate/JPA, PostgreSQL, Lombok, JWT, jBCrypt, SLF4J and Logback. OpenAI may be integrated in the backend as the AI provider, but that integration is not installed yet.

The frontend uses React and Vite with React Router and React Helmet Async. TypeScript is the project convention for new frontend code and must be set up through an explicit ticket before it is used broadly.

Reusable frontend values belong in global `:root` variables named `--engestofte-<name>` and are consumed with `var(--engestofte-<name>)`. Shared colors use hexadecimal or `rgba(...)` values. Repeated colors, spacing, typography and other design values must not be duplicated in individual components.

See the full rules in [Architecture and file conventions](docs/standards/architecture-and-file-conventions.md).

## Arbejdsform

Projektet arbejdes videre med dokumenteret scope, domænebeslutninger og små vertikale tickets. Agenten må ikke køre tests, lint, typecheck, build eller Maven, medmindre det specifikt står i brugerens prompt.

---

<div align="center">
    <sub>Engestofte Gods — Created by Jonas Meinert Larsen — 2026</sub>
</div>
