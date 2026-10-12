# Eksamensforberedelse – Engestofte Gods

Dette dokument samler forslag til svar på spørgsmål om projektet. Svarene er skrevet i jeg-form, så de kan bruges som støtte til fremlæggelsen. De beskriver skoleprojektets prototype og intentioner; løsningen er ikke sat i produktion.

## 1. Kort beskrivelse og elevatorpitch

**Hvad har jeg bygget?**

Jeg har bygget en prototype til håndtering af bryllupsforespørgsler for Engestofte Gods. Kunden bliver guidet gennem et AI-understøttet forløb, hvor oplysningerne samles. Efterfølgende kan kunden og Engestofte følge forespørgslen og aftale ændringer i en fælles eventløsning.

**Hvem er løsningen til?**

Den er til par, der undersøger muligheden for at holde bryllup på Engestofte Gods, og til medarbejderne, især Johan, som gennemgår forespørgsler. Staff kan få adgang til relevante praktiske oplysninger, for eksempel kosthensyn.

**Hvilket problem løser den?**

En bryllupsforespørgsel indeholder mange detaljer, som ellers kan være spredt på tværs af beskeder og opfølgning. Løsningen skal hjælpe med at indsamle og organisere dem, så Johan lettere kan se, hvad kunden ønsker, hvad der mangler, og hvad der kræver et menneskeligt svar.

**Kort pitch:**

> Jeg har bygget en prototype, der hjælper Engestofte Gods med at samle bryllupsønsker ét sted. AI'en stiller opfølgende spørgsmål og laver et struktureret overblik, mens Johan stadig gennemgår forespørgslen og godkender aftalerne.

## 2. Projekt og værdiskabelse

**Kundens behov og brugere**

Kunden har brug for at få mange praktiske oplysninger om et bryllup samlet og fulgt op på. Den primære bruger på kundesiden er det kommende brudepar. På Engestofte-siden er det især Owner/Johan, der gennemgår forespørgslerne. Staff kan bruge begrænsede oplysninger til planlægning.

**Hvad ville kunden få ud af løsningen?**

- Kunden kan besvare spørgsmål i et roligt flow, ét trin ad gangen.
- Johan får en struktureret forespørgsel med mangler, konflikter og usikkerheder fremhævet.
- Kunden og Engestofte kan se godkendte eventdata og foreslå ændringer med historik.
- Relevante medarbejdere kan få de oplysninger, de behøver for at forberede arrangementet.

Det er den forventede værdi. Projektet er ikke i produktion, så jeg kan ikke dokumentere en faktisk tidsbesparelse hos Engestofte endnu.

**Hvilke antagelser byggede projektet på?**

- Mange spørgsmål og opfølgninger kan gøres lettere, hvis oplysninger indsamles struktureret fra starten.
- Kunden har gavn af at få stillet ét relevant spørgsmål ad gangen.
- Johan skal stadig være den, der vurderer særlige ønsker, datoer og endelige aftaler.
- Projektets kalenderregler og demodata er skoleprojektets forudsætninger. De er ikke en integration til Engestofte Gods' faktiske kalender.
- En lokal kladde før login er acceptabel i prototypen, men er ikke en godkendelse til at håndtere rigtige persondata i produktion.

**Hvad blev bevidst fravalgt?**

MVP'en kontrollerer ikke rigtige kalenderdatoer, sender ikke automatiske mails, tager ikke rigtige betalinger og giver ikke AI'en lov til at godkende bookinger eller ændringer. Projektet implementerer heller ikke en fuld integration til Trello eller andre af Engestofte Gods' systemer. Andre arrangementstyper end bryllupper er uden for den første version.

## 3. AI-delen

**Hvor og hvorfor bruger jeg AI?**

AI bruges i kundens bryllupsflow og til at lave et overblik, når en Owner gennemgår en forespørgsel. En almindelig formular kan indsamle felter, men AI kan også forstå svar skrevet med kundens egne ord, finde manglende eller modstridende oplysninger og stille et relevant opfølgende spørgsmål.

**Input og output**

- **Input:** Kundens seneste svar, samtalens tidligere svar, det aktuelle trin, sproget samt projektets systeminstruktioner og rubric.
- **Output:** En kort anerkendelse, eventuelt ét opfølgende spørgsmål, flowets status og udvalgte strukturerede oplysninger. Ved Owner-gennemgang kan AI også lave et resumé og pege på mangler, usikkerheder, konflikter og mulige tilvalg.

**Hvad AI'en er god til – og hvor den kan fejle**

AI kan forstå naturligt sprog og hjælpe med at sortere kundens oplysninger. Den kan stadig misforstå et svar, overse en konflikt eller formulere noget forkert. Den kan også virke sikker, selv om den mangler viden. Derfor må den ikke selv love datoer, priser eller booking, når systemet ikke har et pålideligt grundlag.

**Hvor er menneskelig godkendelse nødvendig?**

Johan vurderer forespørgslen, afklarer særlige forhold og godkender løsningen. Kunden og Owner godkender relevante ændringer hver for sig. AI'en kan støtte med et overblik eller et udkast, men den sender ikke beskeder som en person og træffer ikke den endelige kommercielle beslutning.

**Værktøjer og metoder**

- OpenAI Responses API kaldes fra Java-backenden; integrationen bruger modellen `gpt-4.1-mini`.
- Systeminstruktioner ligger i Markdown-filer, kundens aktuelle input samles i en user prompt, og regler for felter og trin ligger i en JSON-rubric.
- API'et forventes at returnere JSON efter et fast JSON Schema. Backenden læser og validerer svaret, før det bruges af applikationen.
- Der bruges ikke RAG, MCP eller en separat vidensdatabase i MVP'en. AI'en får projektets relevante regler og oplysninger med i kaldet.

**Hvordan har jeg arbejdet med prompts og kontekst?**

Systemprompten beskriver AI'ens rolle og grænser. User prompt indeholder den aktuelle samtale og flowtilstand. Rubricen beskriver, hvilke oplysninger der er nødvendige, hvornår der skal spørges videre, og hvilke betingelser der gælder. Det strukturerede svar gør det lettere for Java-koden at kontrollere outputtet og for frontend at vise den rigtige tilstand.

## 4. Teknisk løsning og arkitektur

**Hvordan er løsningen bygget?**

- **Frontend:** React, TypeScript, Vite og React Router. Den viser kontaktindgang, AI-flow, login, dashboards, eventvisning og gæsteinvitation.
- **Backend:** Java 17 med Javalin. Den håndterer REST-endpoints, login, adgangskontrol, forretningsregler og forbindelsen til OpenAI.
- **Data:** PostgreSQL med Hibernate/JPA. Applikationens egne data, såsom AI-flowinteraktioner, forespørgsler, events, beskeder og ændringsforslag, hører til i backendens datamodel.

**Hvordan bevæger data sig?**

1. Kunden starter på `/kontakt` og åbner AI-flowet.
2. En genoptagelig kladde gemmes lokalt i browseren. Når kunden sender et svar, går det til Java-backenden.
3. Backenden tilføjer systeminstruktioner og rubric og kalder OpenAI. AI'ens strukturerede svar kontrolleres og sendes tilbage til frontend. Interaktioner gemmes som AI-flowhistorik.
4. Efter login og indsendelse behandles forespørgslen i kundens oversigt. Owner kan gennemgå og afklare den.
5. Når et event er godkendt, kan kunden og Owner arbejde med eventdata, beskeder og ændringsforslag. Depositum er simuleret.

**Hvor kaldes AI-modellen?**

OpenAI kaldes fra Java-backenden, aldrig direkte fra browseren. API-nøglen bliver derfor på serversiden. Backendens provider-lag samler overgangen mellem applikationens egne DTO'er og OpenAI's API-format.

**Fejl, svartid og usikre svar**

AI-kaldet har en timeout på 45 sekunder. Hvis kaldet fejler, bliver svaret mangler eller ikke kan læses som forventet, returnerer backend en fejl i stedet for at behandle outputtet som et gyldigt svar. JSON Schema og efterfølgende validering kontrollerer format og centrale værdier. Faglig usikkerhed håndteres ved at bede kunden eller Owner om afklaring frem for at gætte.

**Deployment**

Projektet har ikke et aktivt website eller en deployet backend; links og deployment står som `N/A` i projektets README. Løsningen er derfor en skoleprototype, der er tænkt til at kunne demonstreres lokalt. En rigtig idriftsættelse kræver blandt andet hosting, sikker konfiguration, databeskyttelse og afklaring af håndtering af personoplysninger.

**Tekniske valg jeg er tilfreds med**

- OpenAI-nøglen og AI-kaldet ligger på backend, ikke i browseren.
- AI'ens output oversættes til applikationens egne DTO'er i stedet for at lade OpenAI's svarformat blive datamodellen.
- Struktureret output og validering gør flowets resultat mere forudsigeligt.
- Kunde og Owner godkender ændringer, så vigtig eventdata ikke ændres lydløst.

**Hvad ville jeg ændre, hvis jeg byggede projektet igen?**

Jeg ville afklare de vigtigste arbejdsgange og databeskyttelseskrav endnu tidligere og bygge en mindre ende-til-ende-version først. Før rigtig brug ville jeg også lave en grundig gennemgang af opbevaring, sletning, adgang og de data, der sendes til en ekstern AI-tjeneste. Jeg ville teste samtaleflowet med virkelige, anonymiserede eksempler fra kunden, før jeg udvidede resten af portalen.

## 5. Blogposts og refleksion

Mine blogindlæg viser, hvordan min holdning til AI og projektet har udviklet sig. I starten var jeg skeptisk over for AI, der laver løsninger for udvikleren. Jeg skrev, at AI hellere skulle bruges som vejledning end som en erstatning for forståelse ([blog 1](https://aida.guacamoleboy.dk/blog/1), [blog 2](https://aida.guacamoleboy.dk/blog/2)). Den holdning har jeg stadig, men arbejdet har vist mig, at AI kan være nyttig, når opgaven er afgrænset, og jeg selv forstår og kontrollerer resultatet.

**De vigtigste faglige pointer fra mine blogindlæg**

- **Afprøv idéen selv:** Arbejdet med RAG og Dify lærte mig, at AI-løsninger også kræver forståelse af data, integrationer og begrænsninger ([blog 2](https://aida.guacamoleboy.dk/blog/2), [blog 3](https://aida.guacamoleboy.dk/blog/3)). Jeg brugte ikke RAG i Engestofte-MVP'en, fordi projektets godkendte regler og oplysninger kunne sendes direkte med AI-kaldet.
- **Design før kode:** Jeg arbejdede med rubric, systemprompt, user prompt og forventet output, før jeg byggede AI-endpointet ([blog 5](https://aida.guacamoleboy.dk/blog/5), [blog 6](https://aida.guacamoleboy.dk/blog/6)). I det færdige projekt bruger AI'en et fast JSON-format, og backend validerer resultatet.
- **Beregninger og afgørelser skal ligge i systemet:** I et tidligere API-forløb valgte jeg, at backend selv skulle beregne den endelige score frem for at lade modellen gøre det ([blog 6](https://aida.guacamoleboy.dk/blog/6)). Samme tanke går igen her: AI'en kan indsamle og foreslå, mens backendens regler og mennesker styrer adgang og godkendelser.
- **Specifikationer og agentregler giver fælles forståelse:** En prompt alene er ikke en tilstrækkelig beskrivelse af et større projekt. Domæneord, beslutninger, systemskitse, spec, små tickets og klare instruktioner til kodeagenter hjælper med at bevare forståelsen og holde arbejdet inden for opgaven ([blog 4](https://aida.guacamoleboy.dk/blog/4), [blog 7](https://aida.guacamoleboy.dk/blog/7), [blog 10](https://aida.guacamoleboy.dk/blog/10)).
- **Løsningen skal tage udgangspunkt i kundens behov:** Jeg startede med at overveje julemarkedet og Trello, men valgte efter arbejdet med Engestofte at fokusere MVP'en på bryllupsforespørgsler ([blog 8](https://aida.guacamoleboy.dk/blog/8), [blog 9](https://aida.guacamoleboy.dk/blog/9), [blog 12](https://aida.guacamoleboy.dk/blog/12)).
- **Sikkerhed kan ikke klares med en prompt:** Nøgler, adgangsrettigheder og persondata skal beskyttes af applikationen og dens opsætning. Prototypevalg som kladde i `localStorage` kræver en ny vurdering før rigtig brug ([blog 11](https://aida.guacamoleboy.dk/blog/11)).

**Hvad ser jeg anderledes nu?**

I mine første indlæg var jeg bekymret for, at AI-brug ville gøre mig mindre selvstændig som udvikler. Nu ser jeg, at det afhænger af, hvordan jeg bruger den. Jeg bruger den gerne til afgrænsede opgaver og som hjælp, men jeg skal selv forstå arkitekturen, vælge reglerne og kontrollere resultatet. AI er én del af systemet; den må ikke stå alene med kundens eller Engestofte Gods' beslutninger.

**Hvad lærte jeg?**

Jeg lærte, at det svære ikke kun er at få AI til at svare. Jeg skal også beslutte, hvilke oplysninger den må bruge, hvad et godt svar betyder, hvordan programmet kontrollerer resultatet, og hvornår et menneske skal tage over. Mine refleksioner påvirkede derfor løsningen ved at holde AI'ens opgave afgrænset, lade backend håndtere regler og validering og lade Owner og kunden godkende vigtige forhold.

## 6. Mit individuelle bidrag

Projektet er beskrevet som et solo-projekt, så jeg har haft ansvar for helheden frem for én isoleret del. Jeg har taget udgangspunkt i Engestofte Gods' behov, defineret bryllupsflowet og dets grænser, skrevet projektets specifikationer og tickets og bygget prototypens frontend og Java-backend.

Jeg forstår især AI-flowet og forbindelsen mellem frontend, backend og OpenAI: hvilke oplysninger modellen får, hvordan rubric og prompts styrer forløbet, hvordan output bliver struktureret, og hvorfor Owner stadig skal godkende vigtige beslutninger. Jeg har også været med til at forme eventets godkendelsesflow, brugerroller og håndtering af ændringsforslag.

Det vigtigste, jeg har lært gennem mit eget arbejde, er at omsætte en åben kundesamtale til klare beslutninger og derefter holde implementeringen inden for det aftalte scope. Jeg har også lært, at AI-løsninger kræver mere arbejde med grænser, data og fejlhåndtering end en almindelig promptdemo.

## 7. Afsluttende perspektivering

**Næste skridt for Engestofte**

Jeg ville først vise prototypen til Johan og få feedback på, om spørgsmålene, overblikket og godkendelsesflowet passer til deres faktiske arbejdsgang. Derefter ville jeg prioritere de vigtigste ændringer og afklare, hvilke eksisterende systemer der skal integreres. En kalenderintegration kunne for eksempel give AI-flowet et værktøj til at slå datoer op, men der findes ikke sådan en integration i MVP'en.

**Hvad skal testes før rigtig brug?**

- Om kunder kan gennemføre flowet uden at miste eller misforstå oplysninger.
- Om AI'en håndterer uklare, manglende og modstridende svar ordentligt.
- Om roller og adgang holder kunders, Owners og Staffs oplysninger adskilt.
- Om systemet fungerer med realistisk belastning, sikker backup og fejl i eksterne tjenester.
- Om databeskyttelse, opbevaring, sletning, samtykke og brug af AI-tjenesten er afklaret.

**Største risiko**

Den største risiko er, at AI'en misforstår kundens oplysninger eller giver et svar, der lyder mere sikkert, end grundlaget tillader. En anden væsentlig risiko før produktion er håndtering af personoplysninger. Derfor skal AI-resultater kunne gennemgås, og de rigtige regler for data skal være på plads, før rigtige kunder bruger løsningen.

**Min vigtigste læring**

AI skaber mest værdi, når den løser en afgrænset opgave i et system med tydelige regler. I dette projekt hjælper den med at indsamle og strukturere oplysninger; kunden og Engestofte beholder kontrollen over aftalerne.

## Hurtige stikord før fremlæggelsen

- **Løsning:** AI-støttet indsamling af bryllupsforespørgsler og fælles eventoverblik.
- **AI:** OpenAI fra Java-backend, prompts + JSON-rubric + struktureret JSON-output.
- **Kontrol:** Backend validerer; Owner og kunde godkender vigtige forhold.
- **Teknik:** React/TypeScript frontend, Java/Javalin backend, PostgreSQL.
- **Begrænsninger:** Ingen rigtig kalender, ingen rigtige betalinger, ingen produktion eller MCP.
- **Metode:** Kundebehov → afklaringer → spec → små tickets → implementering og review.

## Projektmateriale, jeg kan slå op i

- [Projektbeskrivelse](../docs/projekt/02-projekt.md)
- [MVP-specifikation](../.scratch/engestofte-mvp/spec.md)
- [AI-flow og felter](../docs/grilling/flow-definition.md)
- [AI'ens faktagrundlag](../docs/grilling/ai-flow-factual-context.md)
- [AI-integrationens beslutninger](../backend/docs/ARD/001-openai-backend.md)
- [Systemprompt](../backend/src/main/resources/prompts/system-prompt.md) og [rubric](../backend/src/main/resources/rubric/rubric.json)
- [Undervisningsnoter om spec-driven development](../docs/undervisning/gange/07-spec-driven-development.md)
- [Undervisningsnoter om LLM-API](../docs/undervisning/gange/05-llm-api-og-struktureret-output.md)
- [Mine blogposts 1–12](https://aida.guacamoleboy.dk/blog/1) – brug bloggens serieoversigt til at åbne de øvrige indlæg.
