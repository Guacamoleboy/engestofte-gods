# Flowdefinition — Bryllupsforespørgsel

Dette dokument fastlægger, hvilke oplysninger AI-flowet skal indsamle, og hvornår de er kritiske. Det skal bruges som fælles grundlag for senere tickets, domænemodel og DTO/entity-design.

## Statusser for felter

- **Kritisk:** Skal have et svar eller en eksplicit status som eksempelvis “ikke besluttet”, før forespørgslen kan indsendes. Uafklarede konflikter kan også blokere booking.
- **Betinget:** Bliver kritisk, når kundens egne svar gør feltet relevant.
- **Valgfrit:** Kan indsamles og sendes til Johan, men blokerer ikke indsendelsen.
- **Mersalg:** Et relevant forslag baseret på kundens svar. Det er aldrig automatisk en pris, booking eller aftale.

## Samtalens fem trin

Samtalen præsenterer de godkendte oplysninger i denne personlige rækkefølge. Feltklassifikationerne nedenfor er fortsat styrende.

1. Kontaktpersonens navn til personlig tiltale; et fornavn er tilstrækkeligt.
2. Ønsket bryllupsdato eller datointerval. Datoens ledighed er ikke bekræftet, før Engestofte har undersøgt den.
3. Forventet gæsteantal (maksimalt 150) og overnatningsbehov. Hvis kunden siger, at alle gæster overnatter, og gæsteantallet allerede er kendt, bruges det antal også som overnatningsantal; spørg ikke igen hvor mange. Når der er én kendt bryllupsdato, og kunden bekræfter, at alle overnatter i perioden, forstås det som én nat fra bryllupsdatoen til dagen efter. Brug datoerne uden at spørge om nætterne igen, medmindre kunden selv nævner et længere eller andet ophold, som stadig er uklart. Hvis kunden har gæster, men ikke ønsker overnatning på godset, anbefaler AI'en at arrangere bus og tilbyder at undersøge muligheden.
4. Kunden vælger mellem Normal, Intim (kun højst 60 gæster) og Skræddersyet. AI'en afklarer kun, om vielsen ønskes på Engestofte, et andet sted eller endnu ikke er besluttet.
5. Mad, drikke, relevante kosthensyn, tilvalg og øvrige særlige ønsker. Budget er frivilligt.

Introspørgsmålene er faste og lokaliserede. Trin 4 viser valgene som cards, og kunden kan vælge med knapperne eller ved at skrive "Mulighed 1", "Mulighed 2" eller "Mulighed 3". AI'en spørger ikke til detaljeret program, kirke eller by. Valgfrie oplysninger må ikke blokere.
## Grundoplysninger

| Felt | Status | Begrundelse |
| --- | --- | --- |
| Arrangementstype | Kritisk | MVP'en understøtter bryllup; flowet skal kende den aktive arrangementstype. |
| Ønsket dato eller datointerval | Kritisk | Johan skal kunne vurdere kapacitet og næste skridt. En manglende dato er specifikt nævnt som en blokering i projektbeskrivelsen. |
| Antal gæster | Kritisk | Gæsteantallet påvirker kapacitet, tilbud, tillæg og mersalgslogik. |
| Bryllupsretning | Kritisk | Kunden vælger Normal, Intim (kun højst 60 gæster) eller Skræddersyet. Svar med kortets navn eller mulighedens nummer accepteres. |
| Vielse på Engestofte eller andet sted | Kritisk med “ikke besluttet” som gyldigt svar | Den første forespørgsel skal kun fastslå, om vielsen ønskes på godset, et andet sted eller ikke er besluttet. Detaljer om kirke, by og program afklares senere. |

### Mersalg ved 60 eller færre gæster

Ved 60 eller færre gæster vises Intim som et skoleprojektforslag, der samler Standardpakken med reception i Værkstedet. Det må ikke vises eller anbefales over 60 gæster. Kortet viser kun, hvad retningen indeholder; priser vises ikke i kortene. Se [faktagrundlaget for AI-flowet](ai-flow-factual-context.md).

I skoleprojektets demo antages alle bryllupsdatoer i 2027 at være fuldt bookede. Kunden kan fortsætte forespørgslen for at blive taget i betragtning ved et eventuelt afbud, men der må ikke loves en plads eller booking. De dokumenterede normale bryllupsmåneder er maj, juni, august og september. I april, juli og oktober afholdes der normalt ikke bryllupper, men kunden må fortsætte forespørgslen til manuel vurdering. November, december, januar, februar og marts holdes lukket for bryllupper; kunden kan vælge en ny dato eller lukke forespørgslen. Alle datoer i 2028 antages ubeskrevne i demokalenderen, men dette tilsidesætter ikke månedernes sæsonregler og kunden må ikke loves en booking. Belægning i andre år er ukendt. Se [faktagrundlaget for AI-flowet](ai-flow-factual-context.md).

## Brylluppets program

| Felt | Status | Begrundelse |
| --- | --- | --- |
| Reception, middag, fest og øvrige programpunkter | Udskudt til eventdialogen | AI-flowet skal ikke afhøre kunden om programdetaljer. Den valgte bryllupsretning giver en overordnet forståelse; Johan afklarer resten senere. |

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
