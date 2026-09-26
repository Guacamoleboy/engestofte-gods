# Grilling: Projekt 01

## Formål

At etablere og fastholde en fælles og præcis forståelse af projektet, som nu er samlet i `docs/projekt/02-projekt.md`.

## Kilde

- Gældende projektbeskrivelse: [Projekt 02](../projekt/02-projekt.md)
- Historisk udgangspunkt: [Projekt 01](../projekt/01-projekt.md)
- Supplerende metode: undervisningsmateriale om RAG, spec-driven development og agenttickets

## Status

Grillingen er i gang. Beslutninger registreres her, når hvert spørgsmål er afklaret.

## Beslutninger

### D1 — MVP'en starter på en kundeorienteret entry-side

MVP'en skal opleves, som om kunden besøger Engestofte Gods' offentlige hjemmeside. Entry-siden skal give personlig og troværdig kontekst gennem:

- Engestofte Gods' visuelle identitet.
- Tilgængelig smiley-rapport i footeren.
- Billeder af lederne, så kunden får en mere personlig forståelse af stedet og menneskene bag.
- En tydelig adgang til et separat AI-flow på en ny side.

Kunden bruger AI-flowet til at beskrive sit bryllup og afgive en forespørgsel. Når AI-flowet vurderer, at de nødvendige oplysninger er indsamlet, oprettes forespørgslen som en sag, som Johan kan gennemse, godkende og bruge til videre dialog med kunden.

AI-flowet skal desuden identificere relevante muligheder for ekstra salg og omsætning ud fra kundens behov og læringen fra kundemødet den 10. september. Sådanne muligheder skal behandles som forslag, indtil Johan godkender dem.

**Konsekvenser:**

- MVP'en består af både en offentlig entry-side, et kundeorienteret AI-flow og en intern Johan-del.
- Kundeoplevelse, tillid og visuel identitet er en del af MVP'en — ikke kun pynt omkring AI-funktionen.
- Johan er den menneskelige godkender og næste led i processen.
- Mersalg bliver et eksplicit mål, men AI'en må ikke gøre forslag til bindende produkter eller priser.

### D2 — Forespørgslen opdeles i obligatoriske og opfølgende oplysninger

AI-flowet skal skelne mellem:

- **Obligatoriske oplysninger**, som skal være på plads, før forespørgslen kan oprettes.
- **Opfølgende oplysninger**, som AI'en kan spørge efter, hvis de er relevante for kundens situation, men som ikke altid skal blokere oprettelsen.

Flowet skal visuelt vise fremdrift gennem nummererede trin og afsluttes med et `DONE`-trin. Før den endelige oprettelse skal kunden kunne oprette en bruger eller logge ind, så kunden efterfølgende kan tilgå sin supportside.

AI'en skal forsøge at identificere relevante mersalgsmuligheder ud fra kundens svar. Et lavt gæsteantal kan eksempelvis udløse et forslag om en intim bryllupspakke. Tærsklen og det konkrete indhold skal være konfigurerbart og må ikke fastlåses til 60 eller 80 gæster, før Johan har bekræftet den forretningsmæssige regel.

**Konsekvenser:**

- Flowet skal tydeligt vise forskel på fremdrift og valgfrie ekstra spørgsmål.
- Brugeroprettelse/login placeres efter kundens udfyldelse af AI-flowet.
- Kundens arbejde i flowet skal skabe tilstrækkelig værdi og motivation til, at login ikke opleves som den første barriere.
- Mersalg bliver en foreslået næste handling, ikke en automatisk pris eller aftale.

### D3 — Flowet gemmes lokalt, men indsendes først efter login

Kundens igangværende flow skal gemmes lokalt i browseren som rå kladdedata. Hvis kunden vender tilbage, skal systemet vise, at arbejdet stadig kan færdiggøres ved at oprette en bruger eller logge ind.

Kundedata må først sendes til Johan og gemmes som en indsendt sag i databasen, når kunden har gennemført brugeroprettelse eller login. En lokal kladde er derfor ikke det samme som en indsendt forespørgsel eller en sag.

**Konsekvenser:**

- Kunden mister ikke nødvendigvis sit arbejde, hvis login-barrieren får kunden til at forlade flowet.
- Johan modtager kun sager fra kunder, der har gennemført sidste trin.
- Løsningen skal kunne skelne tydeligt mellem lokal kladde, forespørgsel og indsendt sag.
- Opbevaring af rå kundeoplysninger lokalt kræver en særskilt beslutning om datatyper, levetid og oprydning.

### D4 — GDPR dokumenteres som prototypeforbehold

Som skoleprojekt må MVP-prototypen gemme rå flowdata i `localStorage` for at demonstrere brugeroplevelsen og muligheden for at vende tilbage til en ufærdig forespørgsel.

Dokumentationen skal tydeligt notere, at denne prototypebeslutning ikke er en produktionsgodkendelse. En rigtig løsning skal vurderes efter GDPR-principper om blandt andet lovligt formål, dataminimering, opbevaringsbegrænsning og passende sikkerhed. Særlige kategorier af personoplysninger kræver særskilt vurdering.

**Kilde:** GDPR artikel 5, 9 og 32.

### D5 — Juridisk dokumentation er ikke en blocker for skoleprototypen

Prototypefasen må demonstrere localStorage-flowet uden at gennemføre en fuld produktionsjuridisk vurdering. Før løsningen eventuelt bruges med rigtige kundedata i produktion, skal GDPR-vurdering, databehandlingsgrundlag, opbevaring og sikkerhed afklares.

### D6 — Fem trin er tilstrækkelige for MVP-flowet

Kildematerialet kræver ikke et ekstra selvstændigt trin. De fem trin fastholdes med denne præcisering:

1. **Grundoplysninger:** ønsket dato eller datointerval, antal gæster, vielsesform/-sted og grundlæggende arrangementsform.
2. **Brylluppet:** reception, middag og fest.
3. **Mad og tilvalg:** mad, drikkevarer, kage, cocktails, natmad, ekstra tid, blomster, koordinering og andre relevante tilvalg.
4. **Overnatning og transport:** behov for overnatning, antal personer, ekstra nætter, overnatning uden for godset og mulig transport.
5. **Ekstra information:** allergier, særlige ønsker, praktiske forhold, budget og andre oplysninger, der kan hjælpe Johan.

Kundens kontaktoplysninger indsamles som obligatoriske oplysninger, men placeres sammen med konto-/login-trinnet ved `DONE`, så kunden ikke møder en konto-barriere før værdien af flowet er tydelig.

Kildematerialet viser mange relevante tilvalg, men de kan håndteres som betingede spørgsmål og mersalgsforslag i de fem eksisterende trin. Det er ikke i sig selv grundlag for et ekstra trin.

### D7 — Flere kontaktpersoner kan knyttes til samme forespørgsel

MVP'en skal lade en potentiel kunde oprette en konto som primær kontaktperson og senere knytte flere kontaktpersoner til den samme forespørgsel. En konto har ingen selvstændig funktion, hvis den ikke er knyttet til en forespørgsel.

En forespørgsel kan derfor have flere deltagende konti, så kunden ikke står alene med planlægningen. Selve adgangsniveauet mellem kontaktpersonerne er endnu ikke besluttet.

**Konsekvenser:**

- Kontooprettelse skal ske i sammenhæng med en forespørgsel eller en invitation til en eksisterende forespørgsel.
- En konto uden forespørgsel skal ikke vise et tomt selvstændigt produktområde.
- Datamodellen skal skelne mellem konto, kontaktperson og forespørgsel.

### D8 — Kontaktpersoner har lige adgang

Alle kontaktpersoner på samme forespørgsel har samme adgang til at se og håndtere forespørgslen. Det forventede scenarie er primært brud og gom, men også forældre eller andre nære deltagere kan være kontaktpersoner.

Der er ikke en ejer-/redaktør-/læserhierarki mellem kontaktpersonerne i MVP'en.

**Konsekvenser:**

- Alle kontaktpersoner kan bidrage til den samme planlægning.
- Adgangsstyringen bliver enklere i MVP'en.
- Systemet skal senere håndtere, hvis to lige kontaktpersoner ændrer samme oplysning.

### D9 — Modstridende værdier bevares og afklares af kontaktpersonerne

Hvis to kontaktpersoner indgiver forskellige værdier for samme oplysning, skal systemet gemme og vise begge værdier. AI'en må ikke vælge den mest sandsynlige værdi, og systemet må ikke automatisk lade den seneste ændring overskrive den anden.

Kunden skal informeres tydeligt, eksempelvis: “Vi har bemærket, at I har indgivet forskellige svar fra to konti. Vælg den værdi, der er korrekt.” Kontaktpersonerne skal selv bekræfte den korrekte værdi.

**Konsekvenser:**

- Konflikter bliver synlige og forståelige for kunden.
- Den valgte værdi skal kunne spores tilbage til de oprindelige svar.
- Det skal senere afklares, om en uafklaret konflikt blokerer indsendelse.

### D10 — Kun kritiske konflikter blokerer indsendelse

En uafklaret konflikt i et kritisk felt blokerer indsendelsen, indtil kontaktpersonerne har bekræftet den korrekte værdi. En konflikt i et valgfrit felt må ikke nødvendigvis blokere; den skal i stedet noteres og sendes videre til Johan til afklaring.

**Konsekvenser:**

- Kritiske felter skal kunne identificeres særskilt fra valgfrie felter.
- Kunden får mulighed for selv at løse konflikter, før Johan modtager sagen.
- Johan modtager stadig relevante uafklarede valgfrie forhold som en del af sagen.

### D11 — Kritiske og valgfrie felter fastlægges i en flowdefinition

Kritiske felter vælges ud fra Johans dokumenterede behov for at kunne vurdere en bryllupsforespørgsel. Dato/datointerval, gæsteantal, arrangementstype, grundlæggende arrangementsform, vielsesform/-sted samt mindst én kontaktperson og kontaktmetode er kritiske ved indsendelse.

Tilvalg, budget, særlige ønsker, detaljer om mad, overnatning og transport håndteres som valgfrie eller betingede felter, medmindre kundens egne svar gør dem nødvendige for næste afklaring.

Den komplette felt- og trinmodel findes i [flowdefinitionen](flow-definition.md), så senere implementeringer ikke skal gætte på feltstatusser.

**Kildegrundlag:** Kundemødenoter fra 10. september, tidligere spørgsmål og svar, bryllupspakken, websnapshot samt `02-projekt.md`.

### D12 — Johan godkender før kunden får adgang til event-portalen

Efter indsendelse modtager Johan forespørgslen sammen med AI'ens opsummering og relevante afklaringspunkter. Johan skal kunne godkende forespørgslen, rette eller tilføje relevant information og skrive beskeder, som kunden skal afklare.

Kunden ser umiddelbart efter indsendelse kun en kvittering med eksempelvis:

- at forespørgslen er afsendt
- dato og tidspunkt for afsendelse
- den aktuelle status

Kunden kan ikke åbne sagens indhold eller bruge event-portalen, før Johan har gennemgået og godkendt forespørgslen. Efter godkendelsen får kunden adgang til et dashboard/event-område, som fungerer som portal for arrangementet og den direkte dialog med Johan.

**Konsekvenser:**

- Systemet skal skelne mellem indsendt, under Johans gennemgang og godkendt.
- Kundens adgang til detaljer er statusafhængig.
- Johan kan berige sagen, før kunden ser den som den fælles arbejdsflade.
- Event-portalen bliver den godkendte samarbejdsflade mellem kunden og Johan.

### D13 — Event-portalen er delt data plus vedvarende beskedtråd

Efter Johans godkendelse skal kunden kunne se hele den godkendte event-/arrangementsvisning og ændre relevante oplysninger. Layoutet tænkes som to paneler:

- **Venstre panel:** Arrangementets data, kundens oplysninger og relevante noter, som Johan har valgt at dele med kunden.
- **Højre panel:** En Messenger-lignende direkte beskedtråd mellem kunden og Johan med historik over tid.

Johan skal kunne se den samme kundevendte information samt et udvidet internt view med AI-vurderinger, kilder, interne noter og ikke-godkendte mersalgsforslag.

Hvis kunden ændrer kritiske oplysninger efter godkendelsen, skal sagen skifte tilbage til `Afventer godkendelse`, indtil ændringen er godkendt af begge parter. Beskeder skal bevares som historik frem for at blive overskrevet.

**Konsekvenser:**

- Kundens portal er både læse-, redigerings- og kommunikationsflade.
- Kundevendte noter skal adskilles fra Johans interne noter.
- Godkendelse er ikke permanent; kritiske ændringer kan genåbne sagen.
- Beskeder er historiske hændelser og skal kunne ses over tid.

### D14 — Kritiske ændringer kræver altid Johans vurdering

Alle ændringer i kritiske oplysninger skal altid sendes til Johans vurdering. Kunden skal altid kunne bede Johan om svar eller afklaring via den direkte beskedtråd.

Kunden kan dermed både ændre eventens oplysninger og bruge Messenger-dialogen til at få menneskelig hjælp, når kunden er i tvivl.

**Konsekvenser:**

- Kritiske ændringer kan ikke blive aktive som godkendt sandhed uden Johan.
- Valgfrie oplysninger har en lettere arbejdsgang.
- Messenger er en aktiv support- og afklaringskanal, ikke kun historisk kommunikation.

### D15 — Statusforløbet bruger neutral gennemgangsstatus

Statusforløbet for en forespørgsel er:

`Kladde` → `Indsendt` → `Under gennemgang` → `Afventer kunde` → `Godkendt / Event-portal åben` → `Afventer godkendelse` → `Afventer depositum` → `Booket` eller `Afsluttet`

Statussen hedder `Under gennemgang` og ikke `Under Johans gennemgang`, så status beskriver sagens tilstand neutralt. `Kladde` findes kun lokalt hos kunden, mens `Indsendt` betyder, at sagen er gemt i databasen.

**Konsekvenser:**

- Kunden kan se et klart og neutralt statusforløb.
- Johan kan bruge `Afventer kunde`, når der mangler et svar.
- `Godkendt / Event-portal åben` markerer overgangen til den delte portal.
- Ændringer af eventdata efter godkendelse sender sagen til `Afventer godkendelse`.

### D16 — Booking kræver enighed og betalt depositum

Statussen `Vundet / booket` bruges ikke. Den endelige status hedder `Booket`, fordi “vundet” lyder som en intern salgsstatus og ikke som en kundevendt bekræftelse.

En forespørgsel må først blive `Booket`, når kunden og Johan har godkendt de relevante oplysninger, og depositum er betalt.

Når begge parter er enige, vises handlingen `Betal depositum`. Når depositummet er registreret som betalt, skifter sagen til `Booket`. Kunden modtager en varm bekræftelse om, at Johan og resten af Engestofte Gods glæder sig til at give dem en god dag.

**Konsekvenser:**

- En AI-vurdering, et tilbud eller kundens interesse er ikke en booking.
- Enighed mellem kunden og Johan skal kunne registreres separat fra betaling.
- Betaling af depositum er den sidste forretningsmæssige betingelse for `Booket`.
- Booking-beskeden skal være kundevendt, personlig og positiv.

### D17 — Kunde og Johan godkender separat

Kunden og Johan skal hver have en separat handling, eksempelvis `Godkend oplysninger`. Begge godkendelser gemmes med dato og tidspunkt. Først når begge godkendelser findes, bliver handlingen `Betal depositum` tilgængelig for kunden.

En ændring i kritiske oplysninger skal ugyldiggøre den relevante godkendelse og kræve ny vurdering. Systemet skal kunne vise, om kunden, Johan eller begge mangler at godkende.

### D18 — Depositum simuleres i skoleprojektet

`Betal depositum` er en prototypehandling og integrerer ikke med en rigtig betalingsudbyder. Når kunden trykker på knappen, vises beskeden `Depositum betalt`, sagen kan skifte til `Booket`, og bekræftelsesbeskeden lukkes automatisk efter cirka fem sekunder.

Ingen rigtige penge eller betalinger skal bruges for at demonstrere denne funktionalitet.

### D19 — Johans ændringer kræver kundens godkendelse

Når Johan ændrer en oplysning, skal ændringen behandles som et ændringsforslag, som kunden skal godkende. Det gælder også, hvis ændringen allerede er aftalt mundtligt eller i Messenger. Kunden og Johan kan derfor gå frem og tilbage med ændringer, indtil begge parter er enige.

Ingen ændring af eventdata bliver den endelige godkendte værdi, før både kunden og Johan har godkendt den. Eksempelvis skal Johans ændring fra tre til fem vegetarer godkendes af kunden, selv om ændringen er aftalt i en samtale.

**Konsekvenser:**

- Godkendelse er gensidig og kan genåbnes af begge parter.
- Johan har faglig kontrol, men kan ikke ensidigt ændre eventdata.
- Ændringsforslag skal kunne ses tydeligt i event-portalen og beskedtråden.

### D20 — Alle eventdataændringer kræver gensidig godkendelse

Når kunden eller Johan ændrer eventdata, oprettes et ændringsforslag, og sagen går til `Afventer godkendelse`. Den anden part skal godkende ændringen, og ændringen er først endeligt gældende, når begge parter har godkendt den aktuelle værdi.

Reglen gælder både kritiske og valgfrie eventdata. Kritiske felter har desuden den særlige konsekvens, at de kan blokere indsendelse eller booking, hvis de er uafklarede.

Statussen `Afventer godkendelse` bruges for alle eventdataændringer, indtil begge parter har godkendt den aktuelle værdi.

## Åbne spørgsmål

### Q21 — Hvad skal én godkendelse omfatte?

Valgt model: Én godkendelse knytter sig til ét konkret ændringsforslag. Eksempelvis godkendes ændringen fra tre til fem vegetarer separat fra ændringer i dato eller gæsteantal.

### D21 — Godkendelse sker pr. konkret ændringsforslag

Et ændringsforslag har én før-værdi, én foreslået værdi, en afsender og en modtager, som skal godkende. Godkendelsen gælder kun dette konkrete forslag og ikke automatisk andre ændringer i sagen.

**Konsekvenser:**

- Kunden og Johan kan se præcis, hvad de godkender.
- Ændringshistorikken bliver sporbar.
- Flere ændringer i samme samtale kan behandles som separate forslag.

### D22 — Nye ændringer opretter nye forslag

Hvis samme felt ændres igen, før det aktive ændringsforslag er afgjort, oprettes et nyt ændringsforslag. Den fulde historik bevares, men kun det seneste aktive forslag kræver handling fra modparten.

**Konsekvenser:**

- Tidligere forslag overskrives ikke.
- Brugeren skal ikke godkende forældede forslag efter et nyere forslag er oprettet.
- Systemet skal kunne vise både historik og aktuelt aktivt forslag.

### Q23 — Hvad sker der, når et ændringsforslag afvises?

Valgt model: Den tidligere godkendte værdi bevares. Ændringsforslaget markeres som afvist, og der kan oprettes et nyt forslag.

### D23 — Afviste forslag bevarer den tidligere værdi

En afvisning ændrer ikke den senest godkendte værdi. Det afviste forslag bevares i historikken, og kunden eller Johan kan oprette et nyt forslag. Afvisningen skal kunne ses i beskedtråden eller ændringshistorikken.

### Q24 — Skal en afvisning kræve en forklaring?

Valgt model: En afvisning kræver altid en forklaring. Forklaringen kan vises i Messenger-historikken og/eller under en særskilt sektion med `Vigtige beskeder`.

### D24 — Afvisninger kræver forklaring og synlig historik

Et ændringsforslag må ikke afvises uden en forklaring. Forklaringen skal være tilgængelig for både kunden og Johan gennem beskedtråden eller `Vigtige beskeder`, så den vigtige beslutning ikke gemmes væk i en teknisk historik.

**Konsekvenser:**

- En afvisning er både en statusændring og en kommunikativ hændelse.
- Brugerne kan forstå, hvorfor den tidligere værdi fortsætter.
- Vigtige beslutninger skal kunne findes igen efterfølgende.

### Q25 — Hvordan skal `Vigtige beskeder` fungere?

Valgt model: Sektionen indeholder automatisk afviste ændringsforslag, godkendelser, kritiske ændringer, beskeder fra Johan samt booking- og depositumshændelser. Den fulde samtale bliver i Messenger.

### D25 — `Vigtige beskeder` samler beslutningshændelser

`Vigtige beskeder` er et særskilt overblik over beslutninger og hændelser med betydning for arrangementet. Det omfatter afvisninger, godkendelser, kritiske ændringer, Johans beskeder og booking-/depositumshændelser.

Den almindelige Messenger-tråd indeholder hele dialogen; `Vigtige beskeder` giver et kondenseret overblik, så afgørende information ikke forsvinder i samtalen.

### Q26 — Hvordan tilknyttes flere kontaktpersoner?

Valgt model: Der sendes ikke rigtige invitationse-mails i skoleprojektet. En kontaktperson kan oprettes ved, at en e-mail indtastes i dashboardet. Derudover kan systemet generere et read-only-link til en forespørgsel eller et generelt forespørgselslink, som oprettes automatisk ved hver forespørgsel.

Kun kontaktpersoner, der både er registreret via e-mail og har en bruger på siden, har `WRITE`, `DELETE` og `READ`-adgang. Links uden tilknyttet bruger giver kun `READ`-adgang.

### D26 — Adgang skelnes mellem read-only-link og fuld kontaktperson

Projektet bruger ikke rigtige mails til invitationer, fordi deploymentmiljøet begrænser udgående mail. I stedet kan dashboardet registrere en kontaktpersons e-mail, og systemet kan generere adgangslinks.

En read-only-link giver kun indblik i forespørgslen. En person bliver fuld kontaktperson med lige rettigheder, når personen er registreret på forespørgslen via e-mail og har en bruger på siden. Fuld kontaktperson har `READ`, `WRITE` og `DELETE`-adgang inden for forespørgslen.

**Konsekvenser:**

- Links alene giver ikke redigeringsadgang.
- Konto og e-mailtilknytning er nødvendige for at ændre data.
- Projektet behøver ikke en rigtig mailudsendelsesfunktion for at demonstrere adgangsmodellen.

### Q27 — Hvem må tilføje eller fjerne kontaktpersoner?

Valgt model: Alle fulde kontaktpersoner kan tilføje og fjerne kontaktpersoner. Det er kundens dag, og kontaktpersonerne vælger selv, hvem der skal have adgang.

Alle interne brugere med rollen `Owner` eller `Staff` kan se forespørgsler. Adgangen er derfor ikke begrænset til Johan.

### D27 — Kunden administrerer kontaktpersoner, interne brugere kan se forespørgsler

Kontaktpersonerne har lige rettigheder til at administrere adgangen til deres forespørgsel. De kan tilføje eller fjerne andre kontaktpersoner, når personen har den nødvendige e-mailtilknytning og bruger.

Interne `Owner`- og `Staff`-brugere kan se forespørgsler i det interne dashboard. Den præcise forskel på, hvad `Owner` og `Staff` må ændre, godkende eller sende, er endnu ikke besluttet.

### Q28 — Hvilke interne handlinger må `Owner` og `Staff` udføre?

Valgt model: `Owner` har fuld adgang. `Staff` har read-only-adgang og kan eksempelvis se relevante køkkenoplysninger som antal vegetarer.

### D28 — Owner har fuld adgang, Staff har read-only-adgang

`Owner` kan se, redigere, kommunikere, godkende, booke og administrere adgang. `Staff` kan se forespørgsler, men kan ikke ændre data, sende beskeder, godkende ændringsforslag eller administrere brugere.

Staff-adgang skal gøre det muligt for relevante medarbejdere, eksempelvis kokke, at se de oplysninger de skal bruge for at udføre deres arbejde.

### Q29 — Hvilke data må Staff se?

Valgt model: Staff må se hele forespørgslen og arrangements-/driftsdata, men ikke Messenger-beskeder. Staff må heller ikke se brugerens direkte identificerende oplysninger som fulde navn, fuld e-mail eller tilsvarende private kontaktdata.

### D29 — Staff ser driftsdata uden kommunikation og direkte identifikation

Staff har read-only-adgang til hele forespørgslen, sådan at de kan udføre deres arbejde for arrangementet. Messenger-beskeder, Johans kundedialog og direkte identificerende brugerdata er undtaget fra Staff-visningen.

Staff-visningen er derfor en operationel eventvisning og ikke en konto- eller kommunikationsvisning.

### Q30 — Må Staff se allergier og andre kosthensyn?

Valgt model: Staff må se allergier og kosthensyn, fordi køkkenet skal kunne håndtere dem. Oplysningerne er read-only og vises uden direkte kontaktdata eller Messenger-beskeder.

### D30 — Kosthensyn er operationelle oplysninger

Allergier og andre kosthensyn må indgå i Staffs read-only driftsvisning, når oplysningerne er nødvendige for at udføre arrangementet. De må ikke bruges til at give Staff adgang til kundens identitet, kontaktdata eller private dialog.

### Q31 — Hvem skal se AI’ens mersalgsforslag først?

Valgt model: AI’en må foreslå relevante tilvalg både i kundens AI-flow og i Johans interne forespørgselsvisning.

### D31 — Mersalg sker både i kundeflowet og hos Johan

AI’en skal identificere mersalgsmuligheder ud fra kundens svar og kunne præsentere dem som relevante, valgfrie og ikke-bindende forslag under oprettelsen af forespørgslen. De samme forslag skal være synlige for Johan i den interne sag, så han kan vurdere, justere eller godkende dem.

Kundens AI-flow må ikke fremstille et forslag som en bindende pakke, booking eller endelig pris. Den godkendte løsning og eventuelle priser skal komme fra Johan.

**Konsekvenser:**

- Mersalg er en del af kundeoplevelsen og ikke kun en intern analyse.
- Johan får synlighed i, hvilke behov AI’en har identificeret.
- Kundens forslag skal kunne adskilles fra Johans godkendte tilbud eller beslutning.

### Q32 — Må AI’en vise konkrete priser i kundeflowet?

Valgt model: AI’en må vise konkrete priser fra aktuelle og godkendte dokumenter, men priserne skal tydeligt markeres som vejledende og ikke-bindende.

### D32 — Priser må vises som vejledende kildedata

AI’en må vise konkrete priser i kundeflowet, når de kommer fra en aktuel og godkendt kilde. Kunden skal samtidig kunne se eller forstå, at prisen ikke er et bindende tilbud, før Johan har godkendt den.

Forældede, uklare eller ikke-godkendte priser må ikke vises som aktuelle priser. Johan skal kunne rette eller afklare prisforslag i den interne sag.

### Q33 — Hvordan skal sprogvalg fungere?

Valgt model: Sprog vælges via en dropdown på entry-siden. Dansk, engelsk og tysk vises som valgmuligheder, men alle sprog vises ikke samtidigt i navigationen. Det valgte sprog bruges i AI-flow, portal, navigation, status og beskeder.

### D33 — Sprog vælges via dropdown og gemmes på forespørgslen

Sprogvalg er en eksplicit kundehandling via dropdown. Det valgte sprog gemmes sammen med forespørgslen, så Johan kan se, hvilket sprog den videre dialog bør bruge.

### Q34 — Hvilket sprog skal være valgt som standard?

Valgt model: Dansk er standard. Kunden kan vælge engelsk eller tysk via dropdown-menuen.

### D34 — Dansk er default, automatisk lokationssprog er fremtidig mulighed

Entry-siden starter på dansk. Projektet er opmærksom på, at Cloudflare eller tilsvarende infrastruktur senere kan foreslå sprog ud fra IP-adresse eller entry-lokation, men denne funktion implementeres ikke i skoleprojektet.

Den fremtidige mulighed må ikke erstatte kundens eksplicitte sprogvalg.

### D35 — Datotilgængelighed vurderes manuelt i MVP'en

AI-flowet indsamler ønsket dato eller datointerval, men slår ikke selv op i en kalender og lover ikke tilgængelighed. Johan vurderer datoen manuelt i den interne behandling.

Hvis systemet har en eksplicit kendt blokering eller utilgængelig dato, skal AI-flowet informere kunden om, at datoen ikke kan bruges. Kunden skal derefter kunne vælge, om forespørgslen fortsat skal sendes, så Johan eller en Owner kan hjælpe med at finde en dato, der passer kunden.

**Konsekvenser:**

- AI’en kan markere en manglende dato, men ikke bekræfte en ledig dato.
- AI’en skal stoppe kendte umulige datoer fra at blive præsenteret som realistiske, men må stadig lade kunden fortsætte med en tydelig advarsel og menneskelig opfølgning.
- Kunden får ingen automatisk booking eller kalenderreservation.
- En senere kalenderintegration kan bygges oven på det eksisterende datofelt.

### Q36 — Hvordan skal kildegrundlag vises i AI-svar?

Valgt model: Kunden får en enkel kildehenvisning eller et forståeligt kildelink, når svaret bygger på dokumentation. Johan kan se den fulde kilde og dokumentversion.

### D36 — Kilder vises forskelligt for kunde og interne brugere

AI-svar skal kunne spores til den dokumentation, de bygger på. Kunden får en enkel og forståelig kildehenvisning. Johan og Owner får adgang til den fulde kilde, dokumentversion og eventuelle forældelsesoplysninger.

### Q37 — Hvilke kilder må bruges til at markere en dato som umulig?

Valgt model: Både eksplicitte Owner-blokeringer og aktuelle officielle Engestofte-dokumenter må bruges. AI’en må ikke selv udlede en blokering fra indirekte oplysninger.

### D37 — Datoblokeringer kræver eksplicit eller officiel kilde

En dato må markeres som umulig, når blokeringen enten er registreret eksplicit af en Owner eller fremgår af et aktuelt officielt Engestofte-dokument. Indirekte AI-slutninger er ikke tilstrækkelige.

### Q38 — Hvad sker der, hvis kilderne er uenige?

Valgt model: En eksplicit Owner-beslutning overruler altid officielle dokumenter ved konflikt.

### D38 — Owner har øverste autoritet ved kildekonflikter

Når en Owner-beslutning og et officielt Engestofte-dokument er uenige, skal Owner-beslutningen bruges som gældende grundlag. Kildekonflikten bør stadig kunne ses internt, så dokumentet kan opdateres senere.

Denne autoritetsregel gælder kildegrundlag, datoblokeringer og andre interne forretningsregler. Den ændrer ikke reglen om, at kunde og Johan skal godkende eventdata gensidigt.

### Q39 — Skal Owner-overrides kræve en begrundelse?

Valgt model: En Owner skal altid skrive en kort begrundelse, som gemmes i intern historik.

### D39 — Owner-overrides er begrundede og sporbare

Et Owner-override kræver en kort intern begrundelse. Override, begrundelse, tidspunkt og Owner gemmes, så fremtidigt review kan forstå, hvorfor Owner-beslutningen overrulerede en officiel kilde.

### Q40 — Hvad skal AI’en gøre, når den er usikker?

Valgt model: AI’en skal markere usikkerheden, stille kunden et opklarende spørgsmål hvis muligt og ellers sende det videre til Johan. AI’en må ikke gætte eller skjule manglende grundlag.

### D40 — Usikkerhed eskaleres og opfindes ikke

AI’en skal håndtere usikkerhed aktivt: først ved at forsøge at få et tydeligere svar fra kunden, derefter ved at markere forholdet som et afklaringspunkt til Johan. Manglende eller modstridende viden må ikke udfyldes med AI’ens gæt.

### Q41 — Hvem må sende beskeder i event-portalen?

Valgt model: Beskeder i event-portalen kan sendes af kontaktpersoner og `Owner`. `Staff` sender ikke beskeder, og AI’en står ikke som afsender.

### D41 — Messenger-beskeder har menneskelig afsender og fuld metadata

Hver besked viser afsenderens navn, profilbillede, dato, tidspunkt og beskedindhold. Eksempel:

> Johan [billede af Johan] — 10/09/2026 — 11:35
>
> Besked her.

Beskedtråden er en menneskelig kommunikationskanal mellem kontaktpersoner og Owner. AI’en kan ikke sende en besked som om den var en person.

### Q42 — Må AI’en skrive udkast til Owner?

Valgt model: AI’en må oprette et tydeligt markeret internt udkast, som Johan/Owner kan rette, godkende og sende som sig selv.

### D42 — AI-udkast aflaster Owner uden at overtage afsenderrollen

AI’en må hjælpe Owner med udkast til Messenger-beskeder, opsummeringer og relevante afklaringer. Udkastet er kun internt, indtil Owner har rettet eller godkendt det og aktivt sendt beskeden. AI’en må ikke stå som afsender.

### Q43 — Hvordan opdager kontaktpersoner nye beskeder?

Valgt model: Nye beskeder vises som ulæste i Messenger/event-portalen. Hvis modtageren ikke har åbnet event-platformen inden for syv dage, markeres beskeden også under `Vigtige beskeder`.

### D43 — Event-platformen markerer Messenger-beskeder som læst

At åbne event-platformen tæller som, at brugeren har læst de nye beskeder i højre Messenger-panel. Der kræves ikke en separat “markér som læst”-handling. Hvis event-platformen ikke åbnes inden for syv dage efter en ny besked, oprettes en `Vigtige beskeder`-markering.

**Konsekvenser:**

- Ulæste beskeder er synlige direkte i Messenger.
- `Vigtige beskeder` fungerer som eskalering for manglende opmærksomhed.
- En åbnet portal stopper syv-dages-eskaleringen for beskederne.

### Q44 — Skal syv-dages-reglen gælde alle modtagere ens?

Valgt model: Syv-dages-reglen gælder individuelt for alle kontaktpersoner og alle `Owner`-brugere. `Staff` er ikke omfattet, fordi Staff ikke har adgang til Messenger.

### D44 — Manglende reaktion eskaleres individuelt for kontaktpersoner og Owner

Hver kontaktperson vurderes separat: Hvis den enkelte kontaktperson ikke har åbnet event-platformen inden for syv dage efter en ny besked, vises beskeden også under `Vigtige beskeder` for denne kontaktperson. Det samme gælder hver `Owner`: Hvis en Owner ikke har åbnet event-platformen og dermed ikke har set en besked fra en kunde inden for syv dage, bliver den en `Vigtig besked`, så Owner bliver gjort opmærksom på, at der skal reageres.

Åbning af event-platformen markerer kun beskeder som læst for den bruger, der åbner platformen. Én brugers åbning nulstiller derfor ikke andre modtageres syv-dages-frist.

### Q45 — Hvad skal ske, hvis flere nye beskeder kommer fra samme afsender inden for syv dage?

Valgt model: `Vigtige beskeder` samler beskeder fra samme samtaletråd i én eskalering med antal ulæste beskeder. Hele beskedhistorikken er fortsat tilgængelig i Messenger.

### D45 — Vigtige beskeder samler ulæste beskeder pr. samtaletråd

Systemet skal ikke oprette én vigtig besked pr. Messenger-besked, hvis flere beskeder venter på samme modtager. I stedet vises én samlet markering med eksempelvis "3 ulæste beskeder fra Johan". Når modtageren åbner event-platformen, behandles de relevante beskeder som læst for denne modtager.

### Q46 — Skal en vigtig besked forblive synlig, hvis modtageren kun åbner Messenger uden at åbne hele event-platformen?

Afklaret: Det scenarie findes ikke. Messenger kan kun ses som en del af den relevante event-platform. Når en bruger åbner event-platformen, åbnes Messenger samtidig, og nye beskeder for denne bruger regnes som læst.

### D46 — Messenger er event-specifik og altid bundet til event-platformen

Der findes ikke en generel kunde-Messenger uden for et event. Hvert event har sin egen beskedtråd, hvor beskeder mellem kontaktpersoner og Owner registreres med eventets kontekst. Beskeder fra ét event må ikke blandes med eller vises i et andet event. Adgang til beskeder følger derfor adgang til det konkrete event.

### Q47 — Kan den samme brugerkonto være kontaktperson på flere forskellige events?

Valgt model: Ja. En brugerkonto kan være kontaktperson på flere events, men hvert event har separat adgang, data og Messenger-tråd.

### D47 — En konto kan have flere eventrelationer

En bruger skal kunne tilknyttes flere forskellige events. Eventoversigten viser kun de events, som brugeren har fået adgang til. Kontaktpersonens rettigheder og synlige data vurderes pr. event, og beskeder fra ét event må ikke blandes med beskeder fra et andet.

### Q48 — Skal en kontaktperson kunne fjerne sig selv fra et event?

Valgt model: Den kontaktperson, som oprettede event-forespørgslen, kan ikke fjerne sig selv gennem den almindelige kontaktpersonfunktion. Kontaktpersoner, som er tilføjet af denne person, kan godt fjerne sig selv fra eventet.

### D48 — Forespørgeren har særligt adgangsansvar

Kontaktpersoner har samme rettigheder til at se og håndtere eventets oplysninger samt kommunikere i Messenger. Der er dog et begrænset hierarki for adgang: Kontaktpersonen, som oprettede forespørgslen, er den primære kontaktperson og kan ikke selv fjernes via den almindelige funktion. Kontaktpersoner, som den primære kontaktperson har tilføjet, kan selv forlade eventet.

Dette nuancerer den tidligere beslutning om lige kontaktpersoner: Lige rettigheder gælder eventets indhold og kommunikation, mens den primære kontaktperson har særligt ansvar for tilknytning og adgang.

### Q49 — Hvem må tilføje eller fjerne andre kontaktpersoner?

Valgt model: Den primære kontaktperson og `Owner` må tilføje eller fjerne kontaktpersoner. Øvrige kontaktpersoner må kun fjerne sig selv. Den primære kontaktperson kan ikke fjernes af andre end `Owner`.

### D49 — Adgangsstyring har primær kontaktperson og Owner som administratorer

Den primære kontaktperson har ansvar for kundesidens kontaktpersoner og kan administrere deres adgang. `Owner` har altid fuld adgang til at administrere kontaktpersoner. Øvrige kontaktpersoner kan ikke ændre andres adgang, men kan selv forlade eventet. En invitation eller tilføjelse skal fortsat ske pr. konkret event.

### Q50 — Hvad skal ske med eventet, hvis den primære kontaktperson forlader eller mister sin konto?

Valgt model: Manglende adgang til kontoen håndteres med almindelig password-nulstilling. Hvis den primære kontaktperson aktivt forlader eventet, annulleres eventet med status `Annulleret af kunde`.

Det skal være svært og tydeligt at forlade eventet som primær kontaktperson, så handlingen ikke kan udføres ved en fejl eller efter en uklar misforståelse. Brugeren skal aktivt forstå konsekvensen, før handlingen gennemføres.

### D50 — Primær kontaktperson kan ikke forlade eventet ved et uheld

Password-problemer må ikke behandles som et ønske om at forlade eventet; brugeren skal kunne nulstille sit password normalt. Et aktivt exit fra eventet som primær kontaktperson er derimod en destruktiv forretningshandling, som kræver en særlig bekræftelsesproces og tydelig advarsel om, at eventet bliver `Annulleret af kunde`.

### Q51 — Skal Owner godkende den primære kontaktpersons ønske om at forlade eventet, eller er en flertrinsbekræftelse nok?

Valgt model: Owner skal ikke godkende exit. Den primære kontaktperson har selv ansvaret for beslutningen, men handlingen skal stadig kræve en tydelig flertrinsbekræftelse.

Hvis depositum allerede er betalt, refunderes det ikke, hvis den primære kontaktperson efterfølgende forlader eventet og eventet derfor bliver `Annulleret af kunde`.

### D51 — Kunden bærer ansvaret for exit og mister depositum ved efterfølgende annullering

Owner kan ikke blokere eller godkende den primære kontaktpersons exit. Systemet skal i stedet vise konsekvensen klart før bekræftelse: Eventet annulleres af kunden, og et allerede betalt depositum refunderes ikke. Dette gælder også, selvom exit-handlingen udføres efter dialog med Owner.

### Q52 — Skal et event uden betalt depositum kunne genåbnes, hvis kunden fortryder en annullering?

Valgt model: Nej. Kunden skal oprette en ny forespørgsel, hvis kunden fortryder en annullering.

### D52 — Annullerede events genåbnes ikke

En forespørgsel med status `Annulleret af kunde` kan ikke genåbnes, heller ikke hvis der ikke er betalt depositum. Kunden skal oprette en ny forespørgsel. Den oprindelige forespørgsel og dens historik bevares som afsluttet annullering.
