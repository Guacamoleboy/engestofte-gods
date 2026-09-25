# Spec-Driven Development

> Kilde fra undervisningsmaterialet. Struktureret fra den tidligere nummererede fil.


## Praktisk
**Gang:** 9 (14/9)

**Tema:** Spec-Driven Development

## Indhold

Introduktion til systemudviklingsmetoder, der er tilpasset softwareudvikling med kodeagenter. Vi arbejder med specifikationer som styrende artefakter for krav, design, implementering og review. Fokus er på hvordan features brydes ned i klare opgaver, flows og acceptkriterier, som en kodeagent kan arbejde ud fra. Vi runder også en artikel af Peter Naur, en pioner inden for softwareudvikling, der introducerede begrebet “programming as theory building”, og diskuterer hvordan hans tanker stadig er relevante i dag.

## Output / portfolio`r`n`r`n Lav en kort refleksionsartikel om hvad det vil sige at arbejde med specifikationer i AI-drevet udvikling - og kom med et bud på hvordan du har tænkt dig at inddrage specs i projekter fremover.

Dagens slide-deck
Spec Driven Development and Naur - slide deck
Øvelse
Mini AI Hero: Spec-drevet quizwebsite
Litteratur
Wikipedia article
Fra Martin Fowlers Blog
Peter Naur, “Programming as Theory Building - original”
Peter Naur, “Programming as Theory Building - markdown version”
Hvem var Peter Naur?
Why AI Coding Agents Still Need Clear Specs

Mini AI Hero: Spec-drevet quizwebsite
I denne øvelse skal I bruge en miniudgave af AI Hero-flowet til at udvikle et lille quizwebsite med en kodeagent. Formålet er ikke at følge et stort framework slavisk, men at træne en arbejdsgang hvor mennesker først opbygger en fælles forståelse, og agenten derefter hjælper med at omsætte den til kode.

Øvelsen tager udgangspunkt i Peter Naurs pointe om, at programmering ikke kun er produktion af kode, men opbygning af en teori om problemet, løsningen og sammenhængen mellem systemet og den virkelige verden.

Case
I skal bygge et lille website med en quiz om meditation, der skal kunne bruges til læringsmæssige formål. Selve metoden skal tilføjes websitet demo.scenius.dk, og tjekker om man har forstået de grundliggende 4 principper i metoden som beskrevet på websitet.

Brugeren skal kunne:

læse et spørgsmål ad gangen eller se en oversigt over spørgsmål
vælge mellem multiple choice-svar
få en samlet score eller feedback
se hvilke svar der var rigtige og forkerte
Jeres underviser agerer kunde og domæneekspert. Kunden har en eksisterende quiz i PDF-format og kan forklare, hvad spørgsmålene betyder, hvordan svarene skal fortolkes, og hvad tonen i websitet skal være.

Læringsmål
Efter øvelsen skal I kunne:

interviewe en domæneekspert, før I beder en kodeagent implementere
formulere et lille fælles domænesprog for projektet
skelne mellem spørgsmål, beslutninger, specifikation og implementation
skrive en spec med mål, ikke-mål, acceptkriterier og testbare grænser
dele arbejdet op i små agent-egnede tickets
bruge Naur til at diskutere, om teamet faktisk forstår systemet
Mini AI Hero-flow
Vi bruger denne forenklede proces:

Grill kunden: Interview kunden og afklar domæne, mål og beslutninger.
Skriv kontekst: Lav en kort glossary og eventuelle ADR’er.1
Lav spec: Skriv en specifikation over det, der er besluttet.
Lav tickets: Del arbejdet i små vertikale opgaver.
Implementer: Lad en kodeagent bygge en lille version.
Review: Tjek om løsningen både er bygget rigtigt og er den rigtige løsning.
Roller
Arbejd i grupper på 2-4.

Fordel rollerne:

Rolle	Ansvar
Kundeinterviewer	Stiller spørgsmål til kunden og sikrer, at gruppen ikke går direkte til kode.
Spec-ansvarlig	Skriver spec, acceptkriterier og ikke-mål.
Agentfører	Formulerer prompts til kodeagenten.
Reviewer	Tjekker løsning, spec og kodeagentens antagelser.
Rollerne kan rotere undervejs.

Trin 1: Grill kunden
Start ikke med at bygge. Start med at interviewe kunden.

Målet er at finde ud af, hvilken teori kunden har om quizzen:

Hvem er quizzen til?
Hvad skal brugeren lære eller opdage?
Skal quizzen føles som en test, en refleksionsøvelse eller noget tredje?
Skal brugeren have feedback efter hvert spørgsmål eller til sidst?
Skal der være én korrekt svarmulighed, flere korrekte svar eller nuancerede svar?
Hvordan skal rigtige og forkerte svar forklares?
Må man tage quizzen igen?
Skal svar gemmes, eller skal alt være lokalt i browseren?
Skal websitet være neutralt, roligt, klinisk, legende eller noget andet?
Er der noget, websitet ikke må gøre?
Promptforslag
Brug en prompt i denne stil:

Du skal hjælpe os med at forberede et lille quizwebsite om meditation.

Før du skriver specifikation eller kode, skal du interviewe kunden. Stil højst 5 spørgsmål ad gangen. Formålet er at afklare domænesprog, målgruppe, brugerflow, scoring, feedback og vigtige designbeslutninger.

Efter hver svarrunde skal du opsummere:
1. Hvad vi nu ved
2. Hvilke begreber der bør indgå i en glossary
3. Hvilke beslutninger der måske skal skrives som ADR'er
4. Hvilke åbne spørgsmål der stadig blokerer for en god spec
Trin 2: Skriv kontekst
Når I har interviewet kunden, skal I skrive to korte artefakter.

Glossary
Lav en CONTEXT.md eller en sektion i jeres arbejdsdokument med centrale begreber.

Eksempel:

Begreb	Betydning i dette projekt
Spørgsmål	En quizopgave med en tekst og et antal svarmuligheder.
Svarmulighed	En valgmulighed brugeren kan markere.
Korrekt svar	Det svar kunden mener passer bedst til meditationsteorien bag quizzen.
Feedback	Kort tekst der hjælper brugeren med at forstå svaret.
Score	Antal korrekte svar, eventuelt suppleret med tekstlig feedback.
ADR’er
Skriv kun ADR’er for beslutninger, der er vigtige, overraskende eller svære at ændre senere.

Mulige ADR’er i denne øvelse:

Skal quizzen være statisk i browseren uden backend?
Skal brugeren se feedback efter hvert spørgsmål eller først til sidst?
Skal der være én korrekt svarmulighed eller mulighed for flere korrekte svar?
Skal quizdata ligge som JSON, Markdown eller direkte i koden?
Kort ADR-format:

# ADR: Quizzen kører uden backend

## Status
Accepted

## Context
Vi skal bygge en lille demo i undervisningen, og der er ikke behov for login eller datalagring.

## Decision
Quizzen implementeres som et statisk website, hvor alle spørgsmål og svar ligger lokalt i frontend-koden eller i en lokal datafil.

## Consequences
Det er let at bygge og deploye. Til gengæld kan rigtige svar ses i kildekoden, så løsningen egner sig ikke til en eksamenstest eller high-stakes vurdering.
Trin 3: Lav spec
Spec’en skal ikke være en brainstorm. Den skal fastholde de beslutninger, I allerede har taget.

Minimumsstruktur:

# Spec: Quizwebsite om meditation

## Problem
Hvilket behov løser quizzen?

## Målgruppe
Hvem skal bruge den?

## Mål
- ...

## Ikke-mål
- ...

## Brugerflow
1. ...
2. ...
3. ...

## Funktionelle krav
- ...

## Indholdskrav
- ...

## Designkrav
- ...

## Acceptkriterier
- [ ] Brugeren kan vælge et svar til hvert spørgsmål.
- [ ] Brugeren kan se sin score.
- [ ] Brugeren kan se hvilke svar der var korrekte.
- [ ] Quizzen kan gennemføres uden login.

## Testbare grænser
Hvor kan vi teste løsningen udefra?
Promptforslag
Lav en kort spec for quizwebsitet baseret på samtalen ovenfor.

Du må ikke opfinde nye krav. Marker åbne spørgsmål tydeligt i stedet for at gætte.

Spec'en skal indeholde:
- problem
- målgruppe
- mål
- ikke-mål
- brugerflow
- funktionelle krav
- indholdskrav
- designkrav
- acceptkriterier
- testbare grænser
Trin 4: Lav tickets
Del arbejdet op i små vertikale tickets. En vertikal ticket skal give noget, der kan afprøves, ikke bare et teknisk lag.

Dårlig opdeling:

Lav HTML
Lav CSS
Lav JavaScript
Tilføj data
Bedre opdeling:

Ticket	Beskrivelse	Demo efter ticket
1	Vis første spørgsmål med svarmuligheder fra quizdata	Man kan se ét rigtigt spørgsmål på siden.
2	Lad brugeren vælge svar og gå videre	Man kan gennemføre 2-3 spørgsmål.
3	Beregn score og vis resultat	Man får et resultat efter sidste spørgsmål.
4	Vis feedback på rigtige og forkerte svar	Resultatsiden forklarer svarene.
5	Gør siden brugbar på mobil og tastatur	Quizzen kan bruges på mobil og med keyboard.
Promptforslag
Del spec'en op i små vertikale tickets, som en kodeagent kan implementere én ad gangen.

Hver ticket skal:
- kunne bygges i én kort agent-session
- have egne acceptkriterier
- kunne demonstreres alene
- undgå afhængighed af uafsluttet arbejde i andre tickets

Lav ikke tickets opdelt efter HTML/CSS/JavaScript-lag.
Trin 5: Implementer en lille version
Vælg én ticket og lad en kodeagent implementere den.

Agenten skal have:

spec’en
den relevante ticket
quizdata eller et lille udsnit af quizdata
eventuelle ADR’er
klare acceptkriterier
Promptforslag
Implementer kun Ticket 1 fra spec'en.

Læs først spec, glossary og ADR'er. Byg den mindste fungerende løsning, der opfylder ticketens acceptkriterier. Lav ikke ekstra funktioner.

Efter implementeringen skal du forklare:
1. Hvilke filer du ændrede
2. Hvordan løsningen kan afprøves
3. Hvilke antagelser du måtte gøre
4. Hvad næste ticket bør være
Trin 6: Review
Reviewet skal have to spor.

Spor	Spørgsmål
Standards	Er løsningen bygget på en fornuftig måde for denne kodebase?
Spec	Løser løsningen faktisk det, spec’en og ticketen bad om?
Reviewspørgsmål
Har agenten implementeret noget, der ikke stod i spec’en?
Mangler der noget fra acceptkriterierne?
Har agenten gættet på domænet i stedet for at spørge kunden?
Er quizdata struktureret, så det er let at ændre spørgsmål senere?
Er der en klar sammenhæng mellem kundens begreber og koden?
Kan næste gruppe forstå løsningen uden at have været med i samtalen?
Aflevering (vis frem)
glossary
0-2 ADR’er
spec
ticketliste
en lille demo eller agentens implementeringsplan
kort refleksion over Naur
Refleksion: Naur og kodeagenter
Afslut med 5 minutters diskussion:

Hvor i processen opbyggede gruppen en teori om quizzen?
Hvor var der risiko for bare at producere dokumenter og kode?
Hvad forstod kunden, som agenten ikke kunne vide på forhånd?
Hvilke dele af teorien blev skrevet ned?
Hvilke dele lå stadig kun i gruppens hoveder?
Gjorde spec’en teamet klogere, eller blev den bare en formalitet?
Facilitatornoter
Som kunde kan du med fordel starte med en kort og ufuldstændig brief:

Jeg har en quiz om meditation. Den består af nogle spørgsmål og multiple choice-svar. Jeg vil gerne have et lille website, hvor brugeren kan tage quizzen og få feedback.

Giv ikke alle detaljer med det samme. Lad de studerende opdage behovet for at spørge.

Gode ting at udfordre sig selv på:

Om quizzen skal være læring, test eller refleksion
Om der findes entydigt rigtige svar
Om brugeren skal have forklaringer på svarene
Om alle spørgsmål fra PDF’en skal med i første version
Om statisk frontend er godt nok
Om rigtige svar må ligge synligt i browserkoden
Om meditationsemnet kræver en særlig tone
Pointen er, at man mærker forskellen på:

“Agent, byg en quizside ud fra denne PDF”
“Agent, hjælp os med at fastholde den teori, vi har opbygget sammen med kunden, og byg derefter en lille verificerbar del”
En ADR er en Architecture Decision Record: et kort dokument, der fastholder en vigtig teknisk beslutning, hvorfor den blev truffet, hvilke alternativer der blev overvejet, og hvilke konsekvenser beslutningen har. Se også AI Hero for den fulde tilgang til agent-understøttet spec-drevet udvikling. ↩︎