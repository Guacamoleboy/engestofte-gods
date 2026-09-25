# Samarbejde med agentiske værktøjer

> Kilde fra undervisningsmaterialet. Struktureret fra den tidligere nummererede fil.


## Praktisk
**Gang:** 10 (18/9)

**Tema:** Collaborative Agentic Development

## Indhold

Nyt undervisningstema om samarbejde med agentiske udviklingsværktøjer, hvor flere mennesker og AI-agenter kan indgå i samme udviklingsproces.

## Kilder

Slides om co-work
Coworking with agents
Git worktrees og fra Claude om worktrees
Code owners
## Output / portfolio`r`n`r`n Del erfaringer fra jeres projekt om at arbejde sammen med AI-agenter og med hinanden.

Coworking with agents
En god agent-ticket bør fungere som en lille kontrakt: Den beskriver ikke kun, hvad der skal bygges, men også hvilke områder agenten må ændre, hvad den skal bevare, hvordan arbejdet verificeres, og hvornår den skal stoppe.

Her er et konkret eksempel til et Java/Javalin-projekt.

Eksempel på ticket med guardrails
# Ticket #142 – Søgning efter kunder på navn

## Formål

Tilføj mulighed for at søge efter kunder via:

GET /api/customers?name=<search-term>

Søgningen skal være case-insensitive og må gerne matche dele af navnet.

## Ansvarlig

Human owner: @jon
Agent branch: jon/142-customer-search
Base branch: main

## Tilladt scope

Agenten må ændre:

- src/main/java/customer/CustomerController.java
- src/main/java/customer/CustomerService.java
- src/main/java/customer/CustomerRepository.java
- src/test/java/customer/CustomerResourceIT.java

Agenten må oprette:

- src/main/java/customer/dto/CustomerSearchResponse.java

## Må ikke ændres

Agenten må ikke ændre:

- pom.xml
- docker-compose.yml
- src/main/java/security/
- src/main/java/config/
- src/main/resources/db/migration/
- eksisterende public API-endpoints
- eksisterende JPA-entiteter

Agenten må ikke:

- tilføje dependencies,
- ændre database-schemaet,
- omdøbe eksisterende klasser eller metoder,
- foretage generel refaktorering,
- formatere eller omskrive ikke-relaterede filer.

Hvis en sådan ændring synes nødvendig, skal arbejdet stoppes og behovet
beskrives i ticketen.

## Funktionskrav

1. `GET /api/customers?name=anna` returnerer kunder, hvis navn
   indeholder `anna`.
2. Søgningen skal være case-insensitive.
3. Manglende `name`-parameter skal bevare endpointets nuværende adfærd.
4. En blank `name`-parameter skal behandles som manglende.
5. Ingen resultater skal returnere HTTP 200 med en tom JSON-liste.
6. Responsens nuværende JSON-format må ikke ændres.

## Tekniske guardrails

- Brug den eksisterende repository- og servicearkitektur.
- Brug JPQL eller den eksisterende query-strategi.
- Introducer ikke en ny abstraktion til kun denne funktion.
- Controlleren må ikke tilgå databasen direkte.
- Eksisterende fejlbehandling skal genbruges.
- Ingen ændring må kræve migration af databasen.

## Forventet adfærd

Eksempel:

GET /api/customers?name=ann

HTTP 200

[
  {
    "id": 17,
    "name": "Anna Jensen"
  },
  {
    "id": 31,
    "name": "Hanne Sørensen"
  }
]
Testkrav
Der skal som minimum testes:

eksakt match,
delvist match,
forskel på store og små bogstaver,
ingen resultater,
manglende parameter,
blank parameter,
at eksisterende kunde-endpoints stadig virker.
Kør:

mvn test
Kør desuden specifikt:

mvn -Dtest=CustomerResourceIT test
Agenten må ikke markere opgaven som færdig, hvis en af disse tests fejler.

Definition of done
Opgaven er færdig, når:

funktionskravene er implementeret,
alle krævede tests består,
der ikke er ændringer uden for det tilladte scope,
der ikke er tilføjet dependencies eller migrationer,
ændringerne er samlet i højst tre fokuserede commits,
pull requesten beskriver implementering, testresultat og eventuelle antagelser.
Stopbetingelser
Agenten skal stoppe og spørge den ansvarlige, hvis:

API-kontrakten skal ændres,
en databaseændring bliver nødvendig,
en fil uden for det tilladte scope skal ændres,
eksisterende tests modsiger ticketens krav,
kravene kan fortolkes på flere måder med forskellig brugeradfærd,
branchen indeholder nye ændringer, som overlapper med agentens arbejde.
Aflevering
Aflever følgende:

Kort beskrivelse af løsningen.
Liste over ændrede filer.
Testkommandoer og resultater.
Antagelser foretaget under arbejdet.
Kendte begrænsninger.
Eventuelle foreslåede opfølgningsopgaver.

## De vigtigste typer guardrails

| Guardrail | Formål | Eksempel |
|---|---|---|
| Resultat | Afgrænser, hvad der skal leveres | “Tilføj navnesøgning” |
| Filområde | Reducerer overlap med andre agenter | “Må kun ændre `customer/`” |
| Forbud | Forhindrer unødvendig ekspansion | “Ingen nye dependencies” |
| Kontrakt | Beskytter andre dele af systemet | “JSON-format må ikke ændres” |
| Arkitektur | Bevarer projektets struktur | “Controller må ikke bruge DAO direkte” |
| Verifikation | Gør færdig entydigt | “Disse tests skal bestå” |
| Stopbetingelser | Forhindrer agenten i at gætte | “Stop ved behov for migration” |
| Aflevering | Gør arbejdet reviewbart | “Oplist ændrede filer og antagelser” |

## Bløde og hårde guardrails

Ticketen alene indeholder primært bløde guardrails. Agenten instrueres i at overholde dem, men der er ingen garanti.

De vigtigste regler bør derfor også håndhæves teknisk:

- `CODEOWNERS` kræver review af eksempelvis migrations- og securityfiler.
- Branch protection forhindrer direkte push til `main`.
- CI kører test, formattering og arkitekturkontrol.
- Et script kan afvise ændringer uden for ticketens tilladte filområder.
- Maven Enforcer kan opdage uønskede dependencies.
- ArchUnit kan kontrollere arkitekturregler.
- En pull request-template sikrer, at testresultater og antagelser dokumenteres.

Et simpelt CI-tjek kan eksempelvis sammenligne ændrede filer med ticketens tilladte paths. Dermed bliver “må kun ændre `customer/`” en reel kontrol og ikke blot en formulering i prompten.

## En kortere version til almindelige tickets

Det fulde eksempel er nok for omfattende til alle opgaver. En praktisk minimumsskabelon kunne være:

```markdown
## Goal

[Entydigt forventet resultat]

## In scope

- [Tilladte komponenter og funktioner]

## Allowed paths

- `src/main/java/customer/**`
- `src/test/java/customer/**`

## Out of scope

- Ingen databaseændringer
- Ingen nye dependencies
- Ingen generel refaktorering
- Ingen ændringer af eksisterende API-kontrakter

## Acceptance criteria

- [Observerbar adfærd 1]
- [Observerbar adfærd 2]
- [Observerbar adfærd 3]

## Verification

- `mvn test`
- [Specifik integrationstest]

## Stop and ask if

- Der kræves ændringer uden for allowed paths
- Krav eller eksisterende tests modsiger hinanden
- En offentlig kontrakt skal ændres

## Handoff

Angiv ændrede filer, tests, antagelser og åbne spørgsmål.
Bruge denne korte skabelon til normale tickets og den udførlige model til databaseændringer, delte API-kontrakter og opgaver, hvor flere udviklere eller agenter arbejder parallelt.