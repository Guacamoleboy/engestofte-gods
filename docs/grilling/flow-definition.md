# Flowdefinition — Bryllupsforespørgsel

Dette dokument fastlægger, hvilke oplysninger AI-flowet skal indsamle, og hvornår de er kritiske. Det skal bruges som fælles grundlag for senere tickets, domænemodel og DTO/entity-design.

## Statusser for felter

- **Kritisk:** Skal have et svar eller en eksplicit status som eksempelvis “ikke besluttet”, før forespørgslen kan indsendes. Uafklarede konflikter kan også blokere booking.
- **Betinget:** Bliver kritisk, når kundens egne svar gør feltet relevant.
- **Valgfrit:** Kan indsamles og sendes til Johan, men blokerer ikke indsendelsen.
- **Mersalg:** Et relevant forslag baseret på kundens svar. Det er aldrig automatisk en pris, booking eller aftale.

## Samtalens fem trin

Samtalen præsenterer de godkendte oplysninger i denne personlige rækkefølge. Feltklassifikationerne nedenfor er fortsat styrende.

1. Kontaktpersonens navn.
2. Ønsket bryllupsdato eller datointerval. Datoens ledighed er ikke bekræftet, før Engestofte har undersøgt den.
3. Forventet gæsteantal og overnatningsbehov. Hvis kunden ønsker overnatning, afklares antal personer og datoer. Intimpakken må ikke anbefales alene ud fra gæsteantal, da dens indhold, pris og forskel fra standardpakken ikke er dokumenteret. Hvis kunden ikke ønsker overnatning, må AI'en tilbyde at undersøge bus mellem et nærliggende hotel og Engestofte.
4. Brylluppets grundform: vielse, reception, middag, fest og vielsesform/-sted.
5. Mad, drikke, relevante kosthensyn, tilvalg og øvrige særlige ønsker. Budget er frivilligt.

Introspørgsmålene er faste og lokaliserede. AI'en stiller personlige opfølgende spørgsmål, indtil de kritiske og relevante betingede oplysninger for det aktuelle trin er tilstrækkelige. Valgfrie oplysninger må ikke blokere.
## Grundoplysninger

| Felt | Status | Begrundelse |
| --- | --- | --- |
| Arrangementstype | Kritisk | MVP'en understøtter bryllup; flowet skal kende den aktive arrangementstype. |
| Ønsket dato eller datointerval | Kritisk | Johan skal kunne vurdere kapacitet og næste skridt. En manglende dato er specifikt nævnt som en blokering i projektbeskrivelsen. |
| Antal gæster | Kritisk | Gæsteantallet påvirker kapacitet, tilbud, tillæg og mersalgslogik. |
| Grundlæggende arrangementsform | Kritisk | Flowet skal kende, om kunden ønsker vielse, reception, middag og/eller fest — eller om noget endnu ikke er besluttet. |
| Vielsesform/-sted | Kritisk med “ikke besluttet” som gyldigt svar | Kilderne nævner egen kirke, privat park, Maribo Domkirke og andre lokationer. Det har betydning for den videre dialog, men kunden behøver ikke have valgt endeligt ved første kontakt. |

### Mersalg ved 60 eller færre gæster

Ved 60 eller færre gæster må AI'en ikke automatisk anbefale Intimpakken. Der mangler godkendt viden om pakkens indhold, pris og værdi sammenlignet med standardpakken. Afvent en aktuel produktbeskrivelse og pris fra Engestofte, før sammenligningsforslaget aktiveres. Se [faktagrundlaget for AI-flowet](ai-flow-factual-context.md).

I skoleprojektets demo antages alle datoer i 2028 at være ubeskrevne. AI'en må sige, at en dato uden for juli står fri i projektkalenderen, men må ikke love en booking. Juli holdes typisk fri for bryllupper, også i 2028, men Johan har nævnt sjældne undtagelser nær månedens start eller slutning. Ved en juliønskning skal kunden få valget mellem at fortsætte, så Engestofte kan vurdere en undtagelse, eller overveje en anden dato. AI'en må ikke love en undtagelse. Belægning i andre år er ukendt og må ikke udledes af generelle bookingmønstre. Se [faktagrundlaget for AI-flowet](ai-flow-factual-context.md).

## Brylluppets program

| Felt | Status | Begrundelse |
| --- | --- | --- |
| Vielse ønskes | Betinget kritisk | Relevant hvis kunden ønsker vielse som del af arrangementet. |
| Reception ønskes | Betinget | Reception er et dokumenteret tilvalg og kan afdækkes uden at blokere, hvis kunden ikke ved det endnu. |
| Middag ønskes | Kritisk for middag/fest-flow | Grundpakken omfatter middag og fest, men kunden skal kunne angive, hvad de ønsker hjælp til. |
| Fest ønskes | Betinget | Relevant, hvis kunden ønsker et egentligt festforløb efter middagen. |
| Ønsket varighed eller særlige tidsønsker | Valgfrit | Kilderne viser forskellige arrangementslængder og sluttider; Johan kan afklare dette senere. |

## Mad og tilvalg

| Felt | Status | Begrundelse |
| --- | --- | --- |
| Mad- og drikkeønsker | Valgfrit | Grundpakke og individuelle løsninger findes, men kundens ønsker kan afklares efter første henvendelse. |
| Allergier og kosthensyn | Betinget | Skal registreres, hvis kunden oplyser dem eller ønsker mad. Uafklarede detaljer sendes til Johan. |
| Bryllupskage | Valgfrit / mersalg | Dokumenteret tilvalg. |
| Cocktails og spiritus | Valgfrit / mersalg | Dokumenteret tilvalg. |
| Natmad | Valgfrit / mersalg | Dokumenteret tilvalg. |
| Ekstra tid | Valgfrit / mersalg | Dokumenteret tilvalg. |
| Blomster, borddekoration og særlige bordbehov | Valgfrit / mersalg | Dokumenterede tilvalg og særlige behov fra kundedialogen. |
| Ekstra koordinering | Valgfrit / mersalg | Dokumenteret mulighed, især relevant ved ønske om mere personlig planlægning. |

## Overnatning og transport

| Felt | Status | Begrundelse |
| --- | --- | --- |
| Behov for overnatning | Valgfrit | Overnatning er en vigtig forretningsmulighed, men ikke alle kunder har behovet. |
| Antal personer med behov for overnatning | Betinget | Skal indsamles, hvis kunden ønsker overnatning. Kilderne viser begrænset kapacitet. |
| Ønskede overnatningsdatoer | Betinget | Kunder kan ankomme før brylluppet eller blive længere; det påvirker kapacitet og klargøring. |
| Accept af overnatning uden for godset | Betinget | Relevant, hvis Engestofte ikke har kapacitet til alle gæster. |
| Interesse for transport | Betinget / mersalg | Må foreslås ved overnatning uden for godset, men må ikke loves uden Johans godkendelse. |
| Specifik bolig eller værelse | Valgfrit | Johan skal vurdere tilgængelighed og egnethed; kunden behøver ikke vælge bolig i første flow. |

## Ekstra information

| Felt | Status | Begrundelse |
| --- | --- | --- |
| Budget eller ønsket prisniveau | Valgfrit | Projektbeskrivelsen siger udtrykkeligt, at kunden kun skal oplyse det, hvis kunden ønsker det. |
| Særlige ønsker og praktiske forhold | Valgfrit | Vigtige for personlig behandling, men kan sendes som afklaringspunkter til Johan. |
| Forventninger til personlig hjælp | Valgfrit / mersalg | Kan udløse forslag om koordinering, borddækning, blomster eller lignende. |
| Frie noter | Valgfrit | Skal kunne indeholde forhold, som flowets faste felter ikke dækker. |

## DONE — Konto og indsendelse

Før indsendelse skal kunden:

- have mindst én kontaktperson med navn
- have en brugerkonto eller logge ind
- have mindst én kontaktmetode, som Johan kan bruge
- have afklaret alle kritiske felter og kritiske konflikter

Flere kontaktpersoner kan tilknyttes efterfølgende. De har samme rettigheder til eventets indhold og Messenger, mens den kontaktperson, der oprettede forespørgslen, har særligt adgangsansvar. En konflikt i et valgfrit felt blokerer ikke nødvendigvis indsendelsen, men alle ændringer i eventdata skal godkendes af både kunde og Johan, før de er endelige.

## Statusforløb

`Kladde` → `Indsendt` → `Under gennemgang` → `Afventer kunde` → `Godkendt / Event-portal åben` → `Afventer godkendelse` → `Afventer depositum` → `Booket` eller `Afsluttet`

`Kladde` findes kun lokalt hos kunden. `Indsendt` betyder, at sagen er gemt i databasen. Alle ændringer i eventdata skifter sagen til `Afventer godkendelse`, indtil både kunde og Johan har godkendt den aktuelle værdi. Den primære kontaktperson kan med en særlig flertrinsbekræftelse annullere eventet; status bliver `Annulleret af kunde`, og eventet kan ikke genåbnes. `Booket` kræver, at begge har godkendt oplysningerne, og at depositum er betalt.

Kunden og Johan godkender separat med hver sin handling. Begge godkendelser gemmes med dato og tidspunkt, før `Betal depositum` bliver tilgængelig.

I skoleprototypen er `Betal depositum` en simulation. Knappen viser `Depositum betalt`, udløser booking-statussen og lukker bekræftelsesbeskeden automatisk efter cirka fem sekunder. Der bruges ingen rigtig betalingsudbyder eller rigtige penge.

## Kildegrundlag

- `docs/projekt/02-projekt.md`
- `docs/kilder/1009-notes.md`
- `docs/kilder/previous-questions.md`
- `docs/kilder/pdf-tekst/bryllup-grundpakkeovernatning-2026.md`
- `docs/kilder/web/engestofte-website-2026-09-26.md`

Priser, kapacitet, kontaktpersoner og tærskler skal valideres mod aktuelle kilder, før de bruges som bindende systemdata.
