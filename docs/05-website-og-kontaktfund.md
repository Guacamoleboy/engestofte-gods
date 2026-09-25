# Website- og kontaktfund

Dette dokument samler observerede problemer og muligheder på den nuværende hjemmeside. Observationerne er input til projektets afgrænsning og skal valideres teknisk, før de bruges som endelige krav.

## Overordnet retning

Målet er ikke at bygge hele hjemmesiden om. Den interessante del er kontaktflowet: hvordan en potentiel kunde går fra interesse til en kvalificeret forespørgsel, og hvordan Johan kan aflastes i den proces.

Bryllupper er den vigtigste forretningsmæssige indgang i materialet. Derfor bør en prototype især undersøge vejen fra bryllupsinteresse til kundehenvendelse og intern opfølgning.

## Observerede websiteproblemer

### Sprog og navigation

- Footeren står altid på engelsk, uanset valgt sprog.
- Dansk viser syv nav-elementer, mens engelsk viser fem og tysk seks.
- Navigationselementer skifter ikke konsekvent sprog.
- `RESTAURANT VÆRKSTEDET` er skrevet med uppercase, mens `Kontakt` ikke følger samme navnestil.
- Sprogskiftet loader siden på ny i stedet for at skifte indhold dynamisk.
- Elementhøjder ændrer sig ved sprogskift, selv når teksten tilsyneladende har samme funktion — eksempelvis kontaktsektioner.
- Der vises tre flag som sprogvalg. Mette ønsker i stedet tekstbaserede valg som `[DK]`, `[EN]` og `[DE]`.

### Kontakt og konvertering

- Kontakten er primært formuleret som “Kontakt Lise på telefon ...” under de enkelte afdelinger.
- Kontaktsiden består af navn, titel, e-mail og telefonnummer.
- Der mangler en tydelig, struktureret kontaktformular til eksempelvis bryllupsforespørgsler.
- En potentiel bryllupskunde, der overvejer et stort arrangement, får ikke nødvendigvis nok tryghed om, hvem personen er, før kontakt tages.
- Der mangler efter brugerens vurdering billeder af de personer, kunden skal kommunikere med.

### Hastighed og teknik

- Hjemmesiden har mærkbar load-tid og føles ikke øjeblikkelig.
- En mulig årsag er uoptimerede billeder eller uhensigtsmæssige billedstørrelser. Det er en hypotese, der skal måles og ikke behandles som et faktum endnu.
- Google Maps er integreret med kort og zoom. Om løsningen medfører aktuelle omkostninger afhænger af opsætning og brug; dette skal verificeres, før man anbefaler en ændring.

## Forretningsmæssig konsekvens

Problemerne er ikke kun kosmetiske. Utydelig navigation, svagt kontaktflow og manglende information om den konkrete kontaktperson kan skabe friktion mellem interesse og forespørgsel. Det kan betyde mistede eller dårligere kvalificerede leads.

## Muligt fokuseret projektområde

En god afgrænsning er en **bryllupsforespørgselsløsning** på eller ved siden af den eksisterende Squarespace-side:

1. Kunden vælger arrangementstype og ønsket dato.
2. Kunden udfylder strukturerede oplysninger om gæster, ønsker, budget og overnatning.
3. AI kvalitetstjekker forespørgslen for mangler, modstridende oplysninger og spørgsmål, der bør afklares.
4. AI laver et personligt, men ikke-bindende svarudkast til Johan.
5. Johan godkender, ændrer og sender svaret.

AI skal dermed aflaste informationsindsamling, sortering og første udkast — ikke automatisk love en pris, bekræfte en dato eller indgå en aftale.

## Kundens arbejdssituation

Johan bor i Valby og har cirka halvanden times transport til Engestofte. En løsning, der reducerer unødvendige frem-og-tilbage-mails og gør henvendelser komplette fra starten, kan derfor give både praktisk og mental aflastning.

## Kilder og status

- Observationer fra brugerens gennemgang af hjemmesiden, 26. september 2026.
- [Websnapshot](kilder/web/engestofte-website-2026-09-26.md)
- [Kunde og forretning](01-kunde-og-forretning.md)
- [PDF-udtræk af bryllupsmateriale](kilder/pdf-tekst/bryllup-grundpakkeovernatning-2026.md)