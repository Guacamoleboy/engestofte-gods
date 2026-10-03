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
| Kalenderantagelse for 2028 i skoleprojektet | Alle datoer i 2028 antages at være ubeskrevne/ikke-bookede i demoen. Det er en projektantagelse, ikke en oplysning om den virkelige kalender. Juli-reglen gælder stadig: bryllupper afholdes typisk ikke dér, og undtagelser skal vurderes af Engestofte. | For en dato i 2028 uden for juli må AI'en sige, at datoen står fri i projektets kalender/demo, men må ikke love en booking. Ved juli skal AI'en forklare den sædvanlige praksis og spørge, om kunden vil have Engestofte til at undersøge en undtagelse. | Skoleprojektets eksplicitte antagelse; ikke en faktisk kalenderkilde. |
| Belægning i andre år | Ikke dokumenteret. Kilderne viser, at Engestofte håndterer bryllupper i 2026, 2027 og 2028, men ikke datoernes faktiske kalenderstatus. | Udled ikke fuld belægning eller ledighed ud fra generelle bookingmønstre. Indhent ønsket dato og lad Engestofte kontrollere den. | Aktuel kalender kræves for virkelige datoer. |
| Intimpakkens indhold, pris og forskel fra standardpakken | Ikke dokumenteret i de gennemgåede kilder. Den historiske projektbeskrivelse foreslår mulige elementer, men markerer dem som en idé, ikke et eksisterende produkt. | Foreslå ikke automatisk Intimpakken alene ud fra gæsteantal. AI'en må ikke beskrive indhold, besparelse eller værdi. Genaktivér først sammenligningsforslaget, når Engestofte har godkendt indhold, pris og målgruppe. | `docs/projekt/01-projekt.md`, historisk idé; kræver aktuel produktbeskrivelse og pris fra Johan. |
| Aktuelle standardpakkepriser | 2026-materialet dokumenterer kun priser for den angivne prisliste; fremtidig gyldighed er ikke bekræftet. | Oplys ikke priser til kunder uden aktuel godkendt prisinformation i flowets instruktioner. | Aktuel prisliste fra Engestofte. |
| Datoer uden for den typiske sæson | Sæsonnotatet siger ikke, at alle datoer uden for maj, juni, august og september er lukkede. | Undlad at kalde dem lukkede eller fuldt bookede. Indhent datoen og henvis til manuel kontrol. | Johan skal bekræfte officiel sæson/undtagelsespolitik, hvis AI'en skal kunne afvise datoer. |

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
- Et fravær af kalenderdata betyder **ukendt**, ikke ledig eller fuldt booket. Undtagelsen er den eksplicitte, skoleprojektspecifikke antagelse om, at 2028 står fri.
- Opdatér dette dokument, `docs/grilling/flow-definition.md`, systemprompten og rubricen samlet, når Engestofte godkender nye AI-relevante fakta.
