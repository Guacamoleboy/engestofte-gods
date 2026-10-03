# Faktagrundlag for AI-flowet

Dette dokument samler forretningsoplysninger, der kan påvirke svar i bryllupsflowet. Det er et manuelt vedligeholdt redaktionelt grundlag, ikke en kalenderintegration eller en automatisk videnssøgning. Kun oplysninger markeret **Bekræftet** må bruges som generelle fakta i AI-flowet. Datofølsomme priser skal fortsat omtales som dokumentpriser fra den angivne periode og må ikke fremstilles som et aktuelt tilbud.

## Bekræftede forhold

| Oplysning | Værdi | AI-brug | Kilde |
| --- | --- | --- | --- |
| Bryllupper i juli | Engestofte holder typisk juli fri for bryllupper af hensyn til ferie. Enkelte undtagelser i begyndelsen eller slutningen af måneden kan forekomme efter konkret vurdering. | Ved en dato i juli må AI'en forklare den sædvanlige praksis og spørge, om kunden vil fortsætte forespørgslen, så Engestofte kan undersøge en mulig undtagelse, eller hellere vælge en anden dato. AI'en må ikke love undtagelsen. | `docs/kilder/transkribering.md`, mødet 10. september 2026, linje 426; `docs/kilder/transkribering.md`, linje 22 |
| Typisk bryllupssæson | Johan beskriver weekenderne i maj, juni, august og september som de dage, Engestofte grundlæggende har at gøre godt med. | Brug kun som generel kontekst. Det er ikke en udtømmende åbningstidsregel og beviser ikke, at datoer uden for månederne er umulige. | `docs/kilder/transkribering.md`, linje 22 |
## Ubekræftede oplysninger og mangler

| Oplysning | Status | Håndtering i AI-flowet | Kilde / næste afklaring |
| --- | --- | --- | --- |
| Skoleprojektets håndtering af bryllupsmåneder | Noterne angiver maj, juni, august og september som de normale bryllupsmåneder. Juli holdes normalt fri. April, juli og oktober er nabomåneder, hvor bryllupper normalt ikke afholdes, men forespørgsler kan vurderes. November, december, januar, februar og marts holdes lukket for bryllupper. | Følg månedsreglen: nabomåneder må fortsætte efter kundens tydelige ja; lukkede måneder kræver ny dato eller at kunden lukker forespørgslen. | `docs/kilder/transkribering.md`, mødet 10. september 2026, linje 22 og 426; brugerens præcisering 3. oktober 2026. |
| Kalenderantagelse for 2028 i skoleprojektet | Alle datoer i 2028 antages at være ubeskrevne/ikke-bookede i demoen. Det er en projektantagelse, ikke en oplysning om den virkelige kalender. Sæsonreglen gælder uafhængigt af kalenderantagelsen. | Datoer i maj, juni, august og september kan beskrives som ubeskrevne i projektets demokalender, men aldrig som booking. Nabomåneder udløser en forespørgsel om kunden vil fortsætte til manuel vurdering; lukkede måneder kræver ny dato eller afslutning. | Skoleprojektets eksplicitte antagelse; ikke en faktisk kalenderkilde. |
| Kalenderantagelse for 2027 i skoleprojektet | Alle bryllupsdatoer i 2027 antages fuldt bookede. Dette er en skoleprojektantagelse, ikke en oplysning om den faktiske kalender. | Fortæl kunden, at 2027 er fuldt booket, men tilbyd at fortsætte forespørgslen, så ønsket kan noteres til overvejelse ved et eventuelt afbud. Det er ikke en reservation eller garanti. Kræv et tydeligt ja før flowet går videre; ved nej afsluttes forespørgslen, og kunden sendes til /kontakt. | Skoleprojektets eksplicitte antagelse; ikke en faktisk kalenderkilde. |
| Belægning i andre år | Ikke dokumenteret ud over projektantagelserne for 2027 og 2028. | Udled ikke fuld belægning eller ledighed ud fra generelle bookingmønstre. Indhent ønsket dato og lad Engestofte kontrollere den. | Aktuel kalender kræves for virkelige datoer. |
| Intim-retning i skoleprojektet | Et nyt projektforslag, ikke et eksisterende Engestofte-produkt. Gælder kun bryllupper med højst 60 gæster og kombinerer Standardpakken med reception i Værkstedet. Med 2026-referencepriser giver det 2.085 kr. pr. kuvert og 20.275 kr. samlet lokaleleje (Standard inkl. 115 kr. småselskabstillæg + reception). | Vis kortet kun ved højst 60 gæster. Beskriv det som et skoleprojektforslag og vis kun pakkens indhold, aldrig priser. Anbefal retningen til den relevante målgruppe. 2026-tallene her er intern dokumentation, ikke kundevendt kortindhold. | Beregnet fra 2026-standard- og receptionspriser på https://www.engestofte.com/da/bryllup, snapshot i `docs/kilder/web/engestofte-website-2026-09-26.md`. |
| Aktuelle standardpakkepriser | 2026-materialet dokumenterer kun priser for den angivne prisliste; fremtidig gyldighed er ikke bekræftet. | Oplys ikke priser til kunder uden aktuel godkendt prisinformation i flowets instruktioner. | Aktuel prisliste fra Engestofte. |
| Formelt vedtagne lukkemåneder | Noterne omtaler juli som normalt fri og nævner sæsonweekender i maj, juni, august og september, men formelle lukkemåneder er ikke særskilt bekræftet. | Brug nabomåneder og lukkede måneder som skoleprojektets operationelle fortolkning. Fremstil ikke denne kategorisering som en officiel offentlig politik. | Brugeren præciserede den ønskede skoleprojektadfærd 3. oktober 2026. |

## Historisk prisreference — ikke kundegrundlag

Følgende værdier står i 2026-bryllupsmaterialet og er samlet til menneskelig sammenligning. Teksten er et automatisk PDF-udtræk; kontrollér originalen og få aktuelle priser bekræftet, før værdierne bruges i AI-instruktioner eller tilbud.

| Del | Værdi fra 2026-materialet | Kilde |
| --- | --- | --- |
| Standardbryllupspakke | Middag og fest i Laden: 1.495 kr. pr. kuvert; lokaleleje 16.900 kr. Omfatter velkomstdrink, 3-retters middag med husets vin ad libitum, kaffe med sødt, isvand, soft bar (øl, vand og vin), fem timers koordinering/tidsplan og overnatning i brudesuite på bryllupsnatten. | `docs/kilder/pdf-tekst/bryllup-grundpakkeovernatning-2026.md`, side 2 |
| Små selskaber, standardpakke | Tillæg på 115 kr. pr. person ved 60 personer eller derunder og 175 kr. pr. person ved 40 personer eller derunder. | Samme PDF-tekstudtræk, side 2 |
| Reception | Værkstedet, 2 timer: 475 kr. pr. kuvert; lokaleleje 3.375 kr. Angivet inkluderet: tapas, crémant/øl/vand ad libitum samt kaffe/te. | Samme PDF-tekstudtræk, side 2 |
| Vielse ved Havnen | Lokationsleje 4.995 kr.; bænke 85 kr. pr. bænk (4 personer) plus 1.500 kr. levering; opsætning af bænke 2.595 kr.; vielsesbue 1.595 kr. (uden blomster). | Samme PDF-tekstudtræk, side 2 |
| Tilkøb | Bryllupskage 45 kr./kuvert; avec 29 kr./kuvert; natmad fra 100 kr./kuvert; cocktails og spiritus ad libitum 155 kr./kuvert; ekstra ret fra 225 kr./kuvert; ekstra time 175 kr./deltager; blomster fra 399 kr.; projektor/lærred 1.800 kr.; bryllupskoordinator 495 kr./time. | Samme PDF-tekstudtræk, side 3 |

## Kildestyring

- Kundevendte fakta kræver en kilde, der er aktuel nok til den konkrete oplysning og udtrykkeligt understøtter påstanden.
- Mødenoter og automatisk PDF-tekstudtræk er spor, ikke automatisk gældende vilkår. Et automatisk udtræk kontrolleres mod originalen, og priser kontrolleres mod en aktuel prisliste.
- Et fravær af kalenderdata betyder **ukendt**, ikke ledig eller fuldt booket. Projektantagelserne er, at 2027 er fuldt booket, og 2028 står fri i demokalenderen. Månedsreglen klassificerer normale måneder, nabomåneder til manuel vurdering og lukkede måneder særskilt.
- Opdatér dette dokument, `docs/grilling/flow-definition.md`, systemprompten og rubricen samlet, når Engestofte godkender nye AI-relevante fakta.
