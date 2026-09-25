# Projekt 01 — AI-understøttet bryllupsforespørgsel

**Status:** Foreløbig valgt projektretning  
**Fokus:** Bryllupper  
**Primær bruger hos Engestofte:** Johan  
**Primær kundeværdi:** Fra løs henvendelse til komplet og kvalificeret forespørgsel  
**AI-tilgang:** Dokumentforankret samtale-AI / RAG med menneskelig godkendelse

## 1. Projektets idé

Vi vil udvikle en online kontaktløsning, hvor en potentiel bryllupskunde kan tale med Engestofte Gods gennem en naturlig, guidet samtale.

Løsningen skal føles som at tale med et menneske, men den skal arbejde struktureret i baggrunden. AI'en stiller ét relevant spørgsmål ad gangen, validerer svarene, finder mangler og fortsætter, indtil forespørgslen indeholder de oplysninger, som Johan har brug for til at vurdere den.

Når forespørgslen er komplet:

1. Kunden får en bekræftelse og et personligt link.
2. Forespørgslen gemmes i en database som én samlet sag.
3. Johan får sagen i et internt dashboard.
4. Johan kan gennemse, rette, stille opfølgende spørgsmål og godkende næste svar.
5. Kunden kan følge status på sin forespørgsel via det personlige link.

Løsningen skal dermed skabe den **Source of Truth**, som Johan efterspørger, uden at Engestofte nødvendigvis skal udskifte Trello eller alle eksisterende systemer fra dag ét.

## 2. Hvorfor bryllupper?

Bryllupper er det første og eneste arrangementstype i projektets scope, fordi:

- bryllupper er en central og værdifuld del af Engestofte Gods' forretning
- forespørgslerne indeholder mange detaljer og gentagne afklaringer
- fejl eller manglende oplysninger kan have stor betydning
- Johan står for en stor del af kontakten
- kundeoplevelsen skal være personlig og tryg
- der er mulighed for relevante tilkøb som ekstra mad, reception, overnatning, transport og andre arrangementselementer

Arkitekturen skal dog bygges, så `bryllup` senere kan udvides med eksempelvis fest, jagt, konference eller julemarked uden at hele løsningen skal bygges om.

## 3. Eksempel på kundeoplevelse

Kunden åbner kontaktløsningen og mødes af en venlig introduktion:

> Velkommen til Engestofte Gods. Hvad ønsker du hjælp til?
>
> - Bryllup
> - Fest
> - Jagt
> - Konference

I første version er kun **Bryllup** aktiv. De øvrige muligheder kan enten skjules eller vises som kommende arrangementstyper.

Kunden skriver eller vælger eksempelvis `Bryllup`. AI'en bekræfter forståelsen og fortsætter med relevante spørgsmål:

- ønsket dato eller datointerval
- antal gæster
- ønsket type arrangement og vielse
- reception, middag og fest
- ønsker til mad, drikke og tilvalg
- behov for overnatning
- behov for transport mellem overnatning og Engestofte
- kontaktoplysninger
- særlige ønsker, allergier eller praktiske forhold
- budget eller ønsket prisniveau, hvis kunden ønsker at oplyse det

AI'en skal kunne sige:

- “Det har jeg noteret.”
- “Jeg mangler lige en dato, før jeg kan sende forespørgslen videre.”
- “Du har skrevet 80 gæster ét sted og 90 gæster et andet sted. Hvilket antal er korrekt?”
- “Jeg kan registrere ønsket, men Johan skal bekræfte pris og tilgængelighed.”

AI'en må ikke fremstille en foreløbig vurdering som en bindende booking eller aftale.

## 4. AI'ens rolle

AI'en er en samtalebaseret intake- og kvalitetssikringsassistent. Den skal ikke være en fri chatbot, der finder på svar.

### AI'en skal

- forstå kundens naturlige sprog
- stille næste relevante spørgsmål ud fra allerede indsamlede oplysninger
- genkende og gemme strukturerede felter
- opdage manglende, uklare eller modstridende svar
- bruge godkendte dokumenter som vidensgrundlag
- svare på simple spørgsmål med kilde- eller dokumentforankring
- forklare, når Johan skal tage over
- opsummere forespørgslen for kunden og Johan
- foreslå relevante, men ikke påtrængende tilvalg

### AI'en må ikke

- love en ledig dato uden kalender-/medarbejdergodkendelse
- fastsætte en endelig pris uden godkendte regler og menneskelig kontrol
- indgå kontrakter eller acceptere en booking
- sende et vigtigt tilbud automatisk uden godkendelse
- opfinde faciliteter, priser, åbningstider eller regler
- bruge oplysninger fra én kunde til at besvare en anden kundes spørgsmål

## 5. Dokumenter og vidensgrundlag

AI'en skal arbejde ud fra et afgrænset og versioneret vidensgrundlag, eksempelvis:

- bryllupspakker og prisdokumenter
- regler for lokaler, gæsteantal og tilvalg
- information om overnatning
- information om transportmuligheder
- godkendte FAQ-svar
- kontaktpersoner og ansvarsområder
- åbningstider og perioder, hvor bryllupper kan afholdes
- relevante smiley- og compliance-links til hjemmesiden

Hvert svar, der bygger på et dokument, bør kunne spores til en kilde. Dokumenter med udløb eller ændrede priser skal kunne markeres som forældede, så AI'en ikke bruger dem ukritisk.

## 6. Kundeportal og status

Efter en komplet forespørgsel får kunden et personligt, sikkert link til sin sag. Kunden skal kunne se:

- at forespørgslen er modtaget
- hvilken status den har
- den samlede opsummering af ønsker
- eventuelle spørgsmål fra Johan
- næste forventede skridt
- eventuelle godkendte svar eller dokumenter

Kunden skal ikke kunne se interne noter, andre kunders data eller AI'ens interne vurderinger.

## 7. Johans dashboard

Dashboardet skal give Johan ét sted at arbejde med forespørgsler. En sag bør som minimum indeholde:

- kunde og kontaktoplysninger
- arrangementstype: bryllup
- ønsket dato/status for dato
- antal gæster
- ønsker og tilvalg
- overnatningsbehov
- transportbehov
- manglende eller modstridende oplysninger
- AI-genereret opsummering
- foreslået svar
- kilder brugt af AI'en
- status og næste handling
- historik over ændringer og godkendelser

Mulige statusser:

`Ny` → `Afventer kunde` → `Klar til vurdering` → `Under behandling` → `Svar sendt` → `Vundet / booket` eller `Afsluttet`

## 8. Transport og overnatning som forretningsmulighed

Engestofte har begrænset overnatningskapacitet, og kunder kan derfor skulle bo cirka 15 minutter væk. Det kan skabe friktion og få nogle kunder til at fravælge løsningen.

Kontaktflowet skal derfor spørge naturligt ind til:

- hvor mange gæster der ønsker overnatning
- om kunden accepterer overnatning uden for selve godset
- om transport mellem overnatning og festen er interessant

På sigt kan Engestofte tilbyde eller koordinere bus til og fra festen som en tryghedsskabende service eller et relevant tilvalg. AI'en må gerne identificere behovet og foreslå, at Johan følger op, men må ikke love bus, pris eller kapacitet uden manuel godkendelse.

## 8a. Mersalg og højere værdi pr. bryllup

### Hvad Engestofte allerede tilbyder

Det eksisterende materiale viser flere enkeltstående tilvalg og tillæg, blandt andet:

- reception
- ekstra ret
- bryllupskage
- cocktails og spiritus
- natmad
- morgenmad
- ekstra timer
- blomster og borddekoration
- koordinering
- overnatning og ekstra nætter

De fremgår som individuelle muligheder i prisdokumenter og på hjemmesiden. Materialet viser derimod ikke, at Engestofte allerede sælger en samlet **intim bryllupspakke**, **weekendpakke** eller **transportpakke**. Disse er derfor forslag til ny mersalgslogik i projektet — ikke funktioner, vi skal beskrive som eksisterende produkter.

### Forslag: intim bryllupspakke

Når kunden angiver et lavere gæsteantal, skal systemet kunne foreslå en særlig pakke, der forklarer værdien i stedet for blot at vise et mindre selskab med højere pris pr. person.

Eksempel:

> I har angivet 60 gæster. Ved mindre bryllupper anbefaler vi vores intime bryllupsløsning, som kan indeholde ekstra koordinering, mere personlig planlægning, udvalgte drikkevarer, borddækning og mulighed for transport. Johan tilpasser løsningen til jeres ønsker.

Den konkrete grænse skal være konfigurerbar. Kundematerialet nævner tillæg ved 60 gæster eller derunder, mens Johan i dialogen har omtalt en mulig grænse omkring 80 gæster. Projektet må derfor ikke hardcode 60 eller 80, før Johan har bekræftet den forretningsmæssige tærskel.

### Forslag: weekend- og transportpakke

Kunder med overnatningsbehov kan få forslag om:

- reception før brylluppet
- overnatning
- morgenmad dagen efter
- bus mellem Engestofte og nærliggende overnatning
- ekstra koordinering af gæster og transport

En busløsning er ikke dokumenteret som et nuværende standardprodukt på hjemmesiden. Den skal derfor vises som et muligt tilvalg, der kræver Johans bekræftelse af pris, leverandør og kapacitet.

### AI'ens rolle i mersalg

AI'en skal ikke presse kunden til at købe mere. Den skal opdage behov og foreslå relevante muligheder ud fra kundens egne svar.

Eksempler:

- Lavt gæsteantal → foreslå intim bryllupspakke.
- Ønske om overnatning → foreslå morgenmad og transport.
- Ønske om reception → foreslå relevante receptionstilvalg.
- Lang fest eller mange gæster → foreslå natmad, ekstra time eller drikkevarepakke.
- Ønske om en meget personlig dag → foreslå ekstra koordinering, borddækning eller blomster.

Alle forslag skal markeres som **forslag** og kræver Johans godkendelse. AI'en må ikke opfinde priser eller præsentere en pakke som officielt tilgængelig, før Johan har godkendt indhold og pris.

### Forretningsmål

Mersalgssporet skal undersøge, om Engestofte kan øge værdien pr. bryllup, når gæstantallet falder, uden at kundeoplevelsen føles som skjulte gebyrer. Fokus er derfor på:

- relevante behov
- tydelig forklaring af værdien
- bedre tryghed for kunden
- højere omsætning pr. arrangement
- mindre manuelt salgsarbejde for Johan

## 9. Flersproget løsning

Første version skal understøtte:

- dansk: `[DK]`
- engelsk: `[EN]`
- tysk: `[DE]`

Sprogvalget skal være tekstbaseret uden flag og skal skifte både samtale, navigation, statusportal, footer og systembeskeder konsekvent.

Forespørgslen skal gemme kundens valgte sprog, så Johan ved, hvilket sprog et svar bør skrives på.

## 10. Footer og compliance

Footerens indhold skal være korrekt på alle tre sprog. Den skal blandt andet indeholde relevante links til smiley-/kontrolrapporter, fordi Engestofte tidligere har haft et problem med manglende tilgængelige links.

Compliance-funktioner skal kunne kontrollere:

- at smiley-linket findes
- at linket virker
- at den viste rapport ikke er utilsigtet forældet
- at footerens links findes i alle sprogversioner
- at juridiske og praktiske sider ikke mangler i en sprogversion

Et AI-kvalitetstjek kan senere rapportere afvigelser, men juridisk ansvar og endelig kontrol ligger hos Engestofte.

## 11. UI- og designkrav

Løsningen skal følge den observerede visuelle identitet:

- primær grøn: `#4F6C41`
- primær hvid: `#FFFFFF`
- `border-radius: 0rem`
- skarpe, professionelle linjer
- fontfamilie med udgangspunkt i `Alice`
- grøn kant og hvid baggrund på knapper på hvid baggrund
- tydelig grøn hover-tilstand
- omvendt kontrast på grønne sektioner
- tekstbaserede sprogvalg: `[DK]`, `[EN]`, `[DE]`
- ingen cartoon-agtige, runde eller overdrevne UI-elementer

Designet skal prioritere tryghed, klarhed og menneskelig kontakt over en flashy chatbot-oplevelse.

## 12. Afgrænsning for første prototype

### Med i første prototype

- bryllup som eneste aktive arrangementstype
- samtalebaseret forespørgselsflow
- validering af svar og manglende felter
- dokumentforankrede svar på simple spørgsmål
- opsummering af komplet forespørgsel
- lagring af sag i database
- enkelt Johan-dashboard
- manuel godkendelse af AI-forslag
- kundelink med status
- dansk, engelsk og tysk UI
- visuel identitet og korrekt footerstruktur

### Ikke med i første prototype

- komplet erstatning af Squarespace
- fuld Trello- eller e-conomic-integration
- automatisk kalenderbooking
- automatisk kontraktindgåelse
- automatisk betaling
- fuld behandling af jagt, fest, konference og julemarked
- garanteret busbooking
- automatisk udsendelse af bindende tilbud

## 13. Succeskriterier

Prototypen er vellykket, hvis den kan demonstrere, at:

1. En potentiel bryllupskunde kan gennemføre en naturlig samtale uden at kende systemets felter.
2. AI'en opdager manglende og modstridende oplysninger.
3. En komplet forespørgsel bliver samlet ét sted.
4. Johan kan se og kvalitetssikre sagen uden at lede i flere e-mails.
5. Kunden får en forståelig status og ved, hvad næste skridt er.
6. AI'en bruger kilder korrekt og eskalerer usikkerhed til Johan.
7. Sprog, footer, smiley-link og UI er konsistente på DK, EN og DE.
8. Løsningen reducerer unødvendig frem-og-tilbage-kommunikation og dermed Johans arbejdsbelastning.

## 14. Mulig teknisk retning

En mulig arkitektur er:

`Squarespace-kontaktpunkt` → `chat/intake-UI` → `AI-agent med dokumentkilder` → `valideret forespørgsel i database` → `Johan-dashboard` → `godkendt svar` → `kundeportal`

Dify eller en tilsvarende RAG-/agentløsning kan undersøges som AI-lag. Den endelige platform skal vælges efter krav til databeskyttelse, integrationer, pris, kontrol og mulighed for at vise kildegrundlag.

## 15. Projektets næste fase

Før implementering skal vi afklare:

- den præcise minimumsliste af oplysninger for en bryllupsforespørgsel
- hvilke PDF’er og dokumenter der er aktuelle
- hvordan dato- og kapacitetskontrol skal foregå
- hvilke svar Johan må sende uden ekstra godkendelse
- hvordan kundelogin og personlige statuslinks sikres
- hvilke GDPR- og databehandlerkrav der gælder
- om løsningen skal integreres med Squarespace eller blot linkes fra den
- hvordan prisforslag og tilvalg skal præsenteres uden at blive bindende
