# Projekt 02 — Engestofte Gods AI-understøttet bryllupsforespørgsel

**Status:** Godkendt MVP-retning og ny source of truth  
**Fokus:** Bryllupper  
**Udvikler:** Jonas Meinert Larsen  
**Primær interne bruger:** `Owner` / Johan  
**Primær kundeværdi:** Fra løs henvendelse til komplet, kvalificeret og godkendelig bryllupsforespørgsel  
**AI-tilgang:** Dokumentforankret samtale-AI med struktureret output og menneskelig godkendelse  
**Backend:** Java 17, Maven, Javalin, Jackson og PostgreSQL  
**Frontend:** React, TypeScript og Vite  

## 1. Source of truth

Dette dokument er den godkendte, samlede projektbeskrivelse for MVP’en. Det erstatter `01-projekt.md`, som bevares som historisk projektretning.

Detaljer findes i:

- [Godkendt grilling og beslutningslog](../grilling/01-projekt-afklaring.md)
- [AI-flow og feltklassifikation](../grilling/flow-definition.md)
- [Domæneordbog](../../CONTEXT.md)
- [MVP-specifikation](../../.scratch/engestofte-mvp/spec.md)
- [Forventede entities](../forventet/entities.md)
- [Forventede database-relationer](../forventet/database.md)
- [Forventet frontend](../forventet/frontend.md)
- [Arkitektur- og filkonventioner](../standards/architecture-and-file-conventions.md)

Hvis dokumenter er i konflikt, gælder den nyeste godkendte beslutning i grilling-loggen og denne projektbeskrivelse. En agent må ikke gætte ved uafklarede konflikter.

## 2. Problem

Potentielle bryllupskunder har mange oplysninger, ønsker og praktiske forhold, som skal afklares. Johan bruger tid på at samle oplysninger, følge op på mangler og holde ændringer konsistente.

Kunden skal have en tryg og personlig oplevelse, mens Engestofte skal have én struktureret source of truth for forespørgslen og det efterfølgende event.

## 3. MVP-løsningen

Kundens hovedflow er:

```text
engestofte-gods.dk/kontakt
        ↓
AI-flow på en ny page
        ↓
Rå kladde i localStorage
        ↓
Login eller register
        ↓
/dashboard/events/
        ↓ Owner/Johan gennemgår og godkender
/dashboard/events/{id}
        ↓
Event-data, Messenger, ændringsgodkendelser og booking
```

### `/kontakt`

Entry-siden skal føles som Engestofte Gods’ egen offentlige hjemmeside og indeholde:

- Engestofte Gods’ visuelle identitet
- smiley-rapport i footeren
- billeder af ledelsen for en personlig og tryg oplevelse
- dansk som standardsprog
- sprogdropdown for dansk, engelsk og tysk
- tydelig knap til AI-flowet

### AI-flowet

AI-flowet er den centrale intake-oplevelse. Det skal:

- stille ét relevant spørgsmål ad gangen
- vise fem synlige trin og en afsluttende `DONE`-tilstand
- indsamle kritiske grundoplysninger
- stille relevante opfølgende spørgsmål
- opdage manglende, uklare og modstridende svar
- markere usikkerhed og eskalere til Owner i stedet for at gætte
- håndtere kendt utilgængelige datoer ærligt
- vise relevante, ikke-bindende mersalgsforslag

De fem trin er:

1. Grundoplysninger
2. Brylluppet
3. Mad og tilvalg
4. Overnatning og transport
5. Ekstra information

Den præcise feltklassifikation findes i [flow-definition.md](../grilling/flow-definition.md).

### Lokal kladde og authentication

Kundens rå flowdata gemmes i browserens `localStorage`, så kunden kan vende tilbage uden at miste sit arbejde. Data sendes ikke til Johan eller databasen, før kunden har oprettet bruger eller logget ind.

Dette er en skoleprototypebeslutning og er ikke en produktionsgodkendelse af GDPR eller EU-compliance. En produktionsløsning kræver særskilt vurdering af dataminimering, opbevaring, sikkerhed, samtykke, sletning og brug af eksterne AI-tjenester.

### `/dashboard/events/`

Efter login eller register sendes kunden til forespørgselsoversigten. Kunden kan se:

- indsendte forespørgsler, som kontoen er tilknyttet
- dato og tidspunkt for indsendelse
- neutral status, eksempelvis `Under gennemgang`
- om der mangler en handling fra kunden

Kunden kan ikke se den fulde event-platform eller interne Owner-noter, før forespørgslen er godkendt.

### `/dashboard/events/{id}`

Når Owner/Johan har godkendt forespørgslen, oprettes eller åbnes kundens konkrete event-platform. Den indeholder:

- eventdata og kundevendte noter i venstre side
- eventets Messenger i højre side
- historik over beskeder mellem kontaktpersoner og Owner
- ændringsforslag og godkendelser
- status for depositum og booking

Messenger er altid knyttet til det konkrete event. Der findes ikke en generel kunde-Messenger uden for et event.

## 4. Roller og adgang

### Customer og kontaktpersoner

En `UserAccount` kan være kontaktperson på flere events. Hvert event har separat adgang, data og Messenger.

Den kontaktperson, som oprettede forespørgslen, er primær kontaktperson og har særligt adgangsansvar. Kontaktpersoner har ellers lige rettigheder til eventets indhold og Messenger.

- Primær kontaktperson og Owner kan administrere kontaktpersoner.
- Øvrige kontaktpersoner kan fjerne sig selv.
- Primær kontaktperson kan ikke fjernes af andre end Owner.
- En konto har ingen eventfunktion, før den er tilknyttet en forespørgsel eller et event.

### Owner

Owner har fuld adgang til:

- forespørgsler og events
- AI-opsummeringer, kilder og interne vurderinger
- Messenger
- kontaktpersoner
- ændringsforslag og approvals
- status, depositumssimulation og booking

### Staff

Staff har read-only-adgang til relevante operationelle oplysninger, eksempelvis arrangementsdata, allergier og kosthensyn.

Staff må ikke:

- se Messenger
- se fuldt navn, fuld email eller andre direkte identifikatorer
- redigere eller godkende eventdata
- sende beskeder
- administrere kontaktpersoner eller brugere

Adgang skal håndhæves i backend med JWT og rolle-/eventkontrol. Frontend guards er kun et ekstra brugeroplevelseslag.

## 5. AI’ens rolle og begrænsninger

AI’en må:

- forstå kundens naturlige sprog
- stille næste relevante spørgsmål
- udtrække strukturerede felter
- opdage mangler og konflikter
- bruge godkendte Engestofte-kilder
- vise relevante source references
- markere usikkerhed
- foreslå relevante og ikke-bindende tilvalg
- skrive tydeligt markerede interne udkast til Owner

AI’en må ikke:

- love tilgængelighed eller booking
- opfinde priser, kapacitet, faciliteter eller regler
- sende bindende tilbud
- godkende ændringer eller booking på Owner eller kundens vegne
- sende en Messenger-besked som om den var en person
- bruge én kundes data til at svare en anden kunde

OpenAI kan integreres som AI-provider i backend, men provider-valget må ikke ændre på kravene om struktureret output, kildegrundlag, usikkerhed og menneskelig godkendelse.

## 6. Mersalg

AI-flowet og Owner-visningen må identificere relevante mersalgsmuligheder ud fra kundens egne svar. Eksempler er:

- lavere gæsteantal → forslag om intim bryllupsløsning
- overnatning → forslag om morgenmad og transport
- reception → relevante receptionstilvalg
- lang fest eller mange gæster → natmad, ekstra tid eller drikkevarer
- ønske om personlig hjælp → koordinering, blomster eller borddækning

Forslag er altid ikke-bindende. Konkrete tærskler, indhold og priser skal kunne konfigureres og skal godkendes af Owner, før de bliver en endelig løsning.

## 7. Godkendelse og ændringer

Alle ændringer i eventdata — også ændringer, der allerede er aftalt mundtligt eller i Messenger — bliver til et konkret field-level `ChangeProposal`.

Et proposal indeholder mindst:

- tidligere godkendte værdi
- foreslået værdi
- hvem der foreslog ændringen
- tidspunkt
- status
- eventuel afvisningsforklaring

Kunde og Owner godkender separat. Begge godkendelser kræves, før den nye værdi er endelig. Afviste forslag bevarer den tidligere værdi og historikken.

## 8. Messenger og vigtige beskeder

Messenger-beskeder sendes kun af kontaktpersoner og Owner. AI kan skrive interne udkast, men er aldrig afsender.

Hver besked viser:

- afsenderens navn
- profilbillede
- dato
- tidspunkt
- beskedindhold

Nye beskeder er ulæste pr. modtager. Når en bruger åbner den relevante event-platform, regnes eventets nye beskeder som læst for denne bruger.

Hvis en kontaktperson eller Owner ikke har åbnet event-platformen inden syv dage, vises én samlet `Vigtig besked` pr. samtaletråd med antal ulæste beskeder. Eskaleringen vurderes individuelt. Staff er ikke omfattet, fordi Staff ikke har Messenger.

## 9. Statusflow

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

En primær kontaktperson kan aktivt forlade eventet gennem en vanskelig flertrinsbekræftelse. Eventet får da status `Annulleret af kunde`.

- Owner skal ikke godkende kundens exit.
- Et allerede betalt depositum refunderes ikke.
- Et annulleret event kan ikke genåbnes.
- Kunden skal oprette en ny forespørgsel.

Depositum er kun simuleret i skoleprojektet. Knappen `Betal depositum` viser `Depositum betalt` og kan flytte eventet til `Booket`, når kunde og Owner har godkendt de relevante oplysninger.

## 10. MVP-afgrænsning

### Med i MVP

- `/kontakt` med Engestofte-identitet, smiley-rapport, ledelsesbilleder og sprogvalg
- femtrins AI-flow for bryllupper
- localStorage-kladde før login/register
- login, register og password reset
- forespørgselsoversigt under `/dashboard/events/`
- Owner-gennemgang, AI-opsummering, kilder og afklaringer
- godkendt event-platform under `/dashboard/events/{id}`
- event-scoped Messenger
- kontaktpersoner og roller
- field-level ændringsforslag og bilateral godkendelse
- ulæste beskeder og syv-dages `Vigtige beskeder`
- Staff read-only-adgang
- simuleret depositum og `Booket`
- tydelig kundecancellation
- dansk, engelsk og tysk via dropdown
- React/TypeScript/Vite-frontend
- Java/Javalin/Jackson/PostgreSQL-backend

### Ikke med i MVP

- rigtige betalinger eller depositum-refunds
- automatisk kalenderbooking
- bindende tilbud eller kontrakter
- automatisk emailafsendelse
- Cloudflare/IP-baseret sprogdetektion
- komplet erstatning af Squarespace, Trello eller økonomisystem
- fuld understøttelse af fest, jagt, konference eller julemarked
- garanteret busbooking eller ekstern leverandørbooking
- produktionsgodkendelse af raw sensitive data i localStorage

## 11. Succes for MVP’en

MVP’en er vellykket, hvis:

1. En potentiel bryllupskunde kan starte trygt på `/kontakt`.
2. Kunden kan gennemføre AI-flowet uden tidlig login-barriere.
3. AI’en indsamler strukturerede oplysninger og opdager mangler eller konflikter.
4. En lokal kladde kan genoptages og først indsendes efter login/register.
5. Owner kan vurdere forespørgslen ét samlet sted.
6. Kunden kan se status uden at se interne oplysninger for tidligt.
7. Godkendte events har separate eventdata og Messenger.
8. Kunde og Owner har tydelig bilateral kontrol over ændringer.
9. Staff får nødvendige driftsoplysninger uden følsom kundekommunikation.
10. AI’en understøtter mersalg og aflastning uden at overtage menneskelige beslutninger.
