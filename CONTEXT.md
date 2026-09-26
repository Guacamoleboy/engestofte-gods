# Engestofte Gods — Domæneordbog

Dette dokument indeholder projektets fælles domænesprog. Det beskriver begreber og deres betydning, men ikke tekniske implementeringsdetaljer.

## Kunde

En potentiel bryllupskunde, som bruger Engestofte Gods' kontaktoplevelse og indsender oplysninger om et ønsket bryllup.

## Konto

En adgang, som en kontaktperson opretter for at kunne være tilknyttet en forespørgsel. En konto har ingen selvstændig funktion, før den er knyttet til en forespørgsel.

## Read-only-link

Et genereret link til en forespørgsel, som giver læseadgang uden at knytte personen til en fuld kontaktpersonskonto. Linket giver ikke ret til at skrive eller slette.

## Kontaktperson

En person, der er knyttet til en forespørgsel. En forespørgsel kan have flere kontaktpersoner, så planlægningen kan deles. Kontaktpersoner har samme rettigheder til eventets indhold og Messenger, men den person, der oprettede forespørgslen, er primær kontaktperson med særligt adgangsansvar. Den primære kontaktperson og Owner kan administrere andre kontaktpersoner; øvrige kontaktpersoner kan kun fjerne sig selv.

Den primære kontaktperson kan ikke forlade eventet ved et almindeligt enkelt klik. Et bevidst exit annullerer eventet som `Annulleret af kunde`; hvis depositum er betalt, refunderes det ikke. Et annulleret event kan ikke genåbnes, og kunden skal oprette en ny forespørgsel.

En fuld kontaktperson skal være registreret via e-mail på forespørgslen og have en bruger på siden. Fuld adgang omfatter `READ`, `WRITE` og `DELETE` inden for forespørgslen.

## AI-flow

Den guidede samtale, hvor AI'en stiller spørgsmål, indsamler oplysninger, opdager mangler eller konflikter og foreslår relevante næste spørgsmål.

## Forespørgsel

Kundens samlede henvendelse om et bryllup. En forespørgsel består af de oplysninger, kunden har afgivet, og kan først indsendes som en sag, når kunden har oprettet bruger eller logget ind.

## Kladde

En igangværende forespørgsel, som endnu ikke er sendt til Johan. En kladde kan midlertidigt ligge lokalt hos kunden, mens kunden gennemfører flowet.

## Sag

Den samlede arbejdsgenstand, som Johan kan gennemse, rette, følge op på og godkende. En sag oprettes først i systemet, når kunden har indsendt sin forespørgsel efter brugeroprettelse eller login. Sagen indeholder forespørgslen, status, relevante spørgsmål, svar, forslag og historik.

## Obligatoriske oplysninger

Oplysninger, der skal være kendt, før en forespørgsel kan oprettes som en sag.

## Grundoplysninger

De centrale oplysninger om et bryllup: ønsket dato eller datointerval, antal gæster, vielsesform/-sted og grundlæggende arrangementsform.

## Opfølgende oplysninger

Oplysninger, som AI'en kan spørge efter, når de er relevante for kundens situation, men som ikke nødvendigvis skal blokere oprettelsen af en sag.

## Konflikt

En situation, hvor kontaktpersoner har indgivet forskellige værdier for den samme oplysning. En konflikt bevarer de oprindelige værdier og kræver, at kontaktpersonerne selv bekræfter den korrekte værdi.

## Kritisk felt

Et felt, hvor en uafklaret konflikt blokerer indsendelsen af en forespørgsel. Felter, der ikke er kritiske, kan sendes videre som afklaringspunkter til Johan.

## Betinget felt

Et felt, der først bliver nødvendigt, når kundens egne svar gør det relevant. Eksempelvis bliver antal overnattende personer nødvendigt, hvis kunden ønsker overnatning.

## Mersalgsforslag

Et forslag om et relevant ekstra produkt, tilvalg eller en service, der udspringer af kundens egne svar. Et mersalgsforslag er ikke en pris, booking eller aftale, før Johan har godkendt det.

AI'en kan vise ikke-bindende mersalgsforslag under kundens flow og samtidig vise dem i Johans interne forespørgselsvisning. Johan godkender eller justerer den endelige løsning.

AI'en må vise priser fra aktuelle og godkendte kilder som vejledende, ikke-bindende prisinformation. Johan skal godkende den endelige pris eller løsning.

## Johan

Den menneskelige ansvarlige, som vurderer forespørgsler, godkender svar og mersalgsforslag og overtager dialogen, når AI'en ikke kan eller må afgøre næste skridt.

## Owner og Staff

Interne brugerroller. `Owner` har fuld adgang. `Staff` har read-only-adgang til relevante oplysninger, eksempelvis køkkenoplysninger som antal vegetarer, men kan ikke ændre, godkende, kommunikere eller administrere brugere.

Staff må se forespørgslens arrangements- og driftsdata, men ikke Messenger-beskeder eller brugerens direkte identificerende oplysninger som fulde navn og fuld e-mail.

Allergier og kosthensyn regnes som operationelle oplysninger, som Staff må se read-only, når de er nødvendige for køkkenets eller arrangementets arbejde.

## Supportside

Kundens efterfølgende adgang til status, opsummering, spørgsmål fra Johan og næste skridt i den konkrete sag.

## Event-portal

Det godkendte dashboard-/event-område, hvor kunden kan se den delte arrangementssag og kommunikere direkte med Johan. Kunden får først adgang til portalens indhold, når Johan har godkendt forespørgslen.

## Godkendt forespørgsel

En indsendt forespørgsel, som Johan har gennemgået og godkendt til deling med kunden i event-portalen.

## Kundevendt note

En note, som Johan aktivt har valgt at dele med kunden i event-portalen. Kundevendte noter er adskilt fra interne noter.

## Intern note

En note, vurdering eller AI-oplysning, som kun Johan må se.

## Afventer godkendelse

En status, der bruges, når kunden eller Johan har ændret eventdata. Den anden part skal godkende ændringen, og den aktuelle værdi er først endelig, når begge parter har godkendt den.

## Depositum

Den betaling, der skal være registreret, efter at kunden og Johan er enige, før forespørgslen kan blive booket.

## Booket

Den kundevendte slutstatus for et arrangement, hvor kunden og Johan har godkendt de relevante oplysninger, og depositum er betalt.

## Godkendelse

En eksplicit handling fra enten kunden eller Johan, hvor den pågældende bekræfter de relevante oplysninger. Kundens og Johans godkendelser er separate og skal begge være registreret, før depositum kan betales.

## Prototypedepositum

En simuleret betaling i skoleprototypen. Den repræsenterer ikke en rigtig pengeoverførsel, men bruges til at demonstrere overgangen fra `Afventer depositum` til `Booket`.

## Ændringsforslag

En ændring i eventdata, som den anden part skal godkende, før den bliver den gældende værdi. Kunden og Johan kan udveksle ændringsforslag, indtil begge har godkendt oplysningerne. Reglen gælder også, når ændringen allerede er aftalt mundtligt eller i Messenger.

Et ændringsforslag gælder én konkret før-værdi og foreslået værdi. Godkendelsen gælder ikke automatisk andre ændringer i sagen.

Hvis samme felt ændres igen, før forslaget er afgjort, oprettes et nyt aktivt forslag, mens de tidligere forslag bevares som historik.

Hvis et forslag afvises, bevares den tidligere godkendte værdi. Det afviste forslag gemmes som historik, og et nyt forslag kan oprettes.

En afvisning kræver altid en forklaring, som skal kunne findes i beskedtråden eller under `Vigtige beskeder`.

## Vigtige beskeder

Et særskilt overblik over vigtige beslutnings- og systemhændelser: afvisninger, godkendelser, kritiske ændringer, Johans beskeder samt booking- og depositumshændelser. Den fulde dialog ligger fortsat i beskedtråden.

## Beskedtråd

Den historiske, direkte kommunikation mellem kunden og Johan i event-portalen. Beskeder bevares som historik og overskrives ikke af senere beskeder.

Beskeder kan sendes af kontaktpersoner og Owner. Staff sender ikke beskeder, og AI'en står ikke som afsender. Hver besked har afsender, profilbillede, dato, tidspunkt og indhold.

AI'en må oprette interne, tydeligt markerede udkast til Owner. Udkast bliver først kundevendt, når Owner aktivt sender det.

## Sprogvalg

Kundens valgte sprog for AI-flow, event-portal, status og beskeder. Første version understøtter dansk, engelsk og tysk, og valget foretages via en dropdown.

Dansk er standardsproget. Automatisk sprogforslag ud fra IP eller entry-lokation er en fremtidig mulighed, ikke en del af skoleprojektets implementering.

## Kildehenvisning

En sporbar reference til det dokumentgrundlag, som et AI-svar bygger på. Kunden får en enkel kildehenvisning, mens Johan og Owner kan se fuld kilde og dokumentversion.

Ved konflikt mellem et officielt dokument og en eksplicit Owner-beslutning har Owner-beslutningen øverste autoritet. Konflikten bør stadig være synlig internt.
