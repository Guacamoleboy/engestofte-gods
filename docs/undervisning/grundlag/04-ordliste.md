# Grundlag: Ordliste for AI-applikationer

> Omskrevet fra den tidligere `toolbox.md`.

Ordliste
Ordliste
Her er en kort ordliste over centrale begreber, når du arbejder med LLM-integration, API’er og dataintegration. Begreberne er sorteret alfabetisk.

API-nøgle
En hemmelig nøgle, der bruges til at autentificere din applikation over for API’et. Den skal opbevares sikkert på backend og aldrig eksponeres i frontend eller i Git.

Asynkront job
En opgave, der startes nu, men behandles i baggrunden. Det bruges ofte til længere AI-processer, hvor brugeren ikke skal vente på ét langt HTTP-kald.

Backoff
En strategi, hvor systemet venter længere mellem nye forsøg, når et API-kald fejler. Bruges ofte sammen med retries.

Batching
At samle flere opgaver eller input i færre kald, hvis use casen og API’et tillader det. Det kan reducere overhead og nogle gange forbedre performance.

Cache
Midlertidig lagring af tidligere svar eller mellemresultater, så du undgår unødvendige nye API-kald og sparer både tid og penge.

Change detection
En metode til at afgøre, om data har ændret sig siden sidst. Det bruges ofte til at undgå at importere, behandle eller sende uændrede data til et LLM igen.

Context window
Den maksimale mængde tekst og data en model kan tage med i ét kald. Hvis du sender for meget, skal input forkortes, opdeles eller udvælges bedre.

Content hash
En hashværdi beregnet ud fra det indhold, der er vigtigt i en række eller et dokument. Den bruges ofte til at opdage ændringer og afgøre, om tidligere LLM-resultater kan genbruges.

Deduplikering
Processen med at finde og undgå dubletter i data. I integrationsløsninger bruges det til at sikre, at samme række eller samme indhold ikke behandles flere gange.

Endpoint
Den specifikke URL eller funktion i et API, som din backend kalder. Et endpoint kan for eksempel bruges til at sende prompts til en model.

Ekstern id
Et id fra et eksternt system eller API, som bruges til at identificere en række entydigt. Det er ofte den vigtigste reference i dataintegration.

Exponential backoff
En type backoff, hvor ventetiden vokser markant for hvert nyt forsøg. Det er en almindelig teknik ved midlertidige fejl og rate limits.

Fallback
En alternativ plan, hvis modellen eller API’et ikke svarer som forventet. Det kan være en fejlbesked, et standardsvar eller et nyt forsøg.

Fejlhåndtering
Den del af din kode, der håndterer problemer som timeout, ugyldig JSON, netværksfejl eller serverfejl på en kontrolleret måde.

Hallucination
Når modellen opfinder information, som ikke er korrekt eller ikke findes i inputtet. Det er en væsentlig grund til at validere og kvalitetssikre output.

Idempotens
Et princip hvor det samme job eller den samme handling kan køres flere gange uden at skabe dubletter eller uønskede ekstraeffekter. Det er centralt i robuste integrationsjobs.

Inputstruktur
Den måde du organiserer input på, så modellen lettere kan forstå opgaven. Tydelige labels, overskrifter og afgrænsninger giver ofte bedre svar.

Jitter
En lille tilfældig variation i ventetiden mellem retries. Det hjælper med at undgå, at mange klienter prøver igen samtidig.

JSON
Et struktureret dataformat, som ofte bruges til modeloutput, fordi det er lettere at validere og bruge programmatisk end fri tekst.

Job-id
Et unikt id for en baggrundsopgave. Det gør det muligt at gemme og hente status eller resultat senere.

Logning
Registrering af vigtige hændelser som kald, responstid, fejl og statuskoder. Logs er centrale til fejlfinding og drift.

Max output tokens
En indstilling, der begrænser hvor langt et modelsvar må være. Den bruges til at styre både pris, svartid og længde på output.

Model
Den konkrete AI-model, du vælger at kalde via API’et. Modelvalget påvirker blandt andet kvalitet, hastighed, pris og hvilke funktioner der er tilgængelige.

Monitorering
Løbende overvågning af systemets performance, fejlrate, svartider og omkostninger. Det er vigtigt i robuste AI-løsninger.

Normalisering
Forberedelse og ensretning af data før sammenligning, hashing eller videre behandling. Det kan for eksempel være trim af whitespace, sortering af lister eller fjernelse af irrelevante felter.

Output-eksempel
Et konkret eksempel på det svarformat, du ønsker fra modellen. Det hjælper modellen med at levere mere stabilt og ensartet output.

Outputformat
Den struktur du forventer i svaret fra modellen, for eksempel fri tekst eller JSON med bestemte felter. Et tydeligt outputformat gør integrationen mere robust.

Parallelitet
Hvor mange opgaver eller requests systemet behandler samtidig. For høj parallelitet kan give rate limits, timeout og unødig belastning.

Prompt
Den tekst og de instruktioner, du sender til modellen. Prompten kan bestå af roller, data, regler og den konkrete opgave.

Prompt injection
Et angreb eller et problem, hvor input forsøger at ændre modellens instruktioner eller få systemet til at ignorere regler og afsløre skjult information.

Prompt-version
En versionsmarkering af den prompt eller prompt-skabelon, der bruges i behandlingen. Den gør det muligt at afgøre, om tidligere LLM-resultater stadig kan genbruges.

Queue
En kø, hvor opgaver venter på at blive behandlet i kontrolleret rækkefølge. Den bruges ofte sammen med workers og asynkrone jobs.

Rate limit
En grænse for hvor mange API-kald du må lave inden for et tidsrum. Overskrides den, kan kald blive afvist eller forsinket.

Request
Et enkelt kald fra din applikation til et API. Et request indeholder typisk modelvalg, prompts og andre parametre.

Response format
En API-indstilling eller kontrakt for, hvordan svaret skal struktureres. Den bruges ofte til at gøre output mere stabilt og lettere at validere.

Retry
Et nyt forsøg på et API-kald efter en fejl. Retries bør være begrænsede og kombineres med backoff.

Status
Den aktuelle tilstand for en opgave eller et API-kald, for eksempel queued, running, completed eller failed.

Streaming
En måde at modtage modelens svar løbende i små dele i stedet for at vente på hele svaret. Det kan forbedre oplevet hastighed i brugerflader.

Structured output
Output, der følger en tydelig struktur, ofte JSON eller et schema. Det er normalt lettere at arbejde sikkert med end fri tekst.

Systemprompt
Den del af prompten, der beskriver modellens rolle, regler, begrænsninger og ønsket adfærd.

Temperature
En parameter, der styrer hvor varieret eller kreativt modellen svarer. Lav temperature giver ofte mere stabile og forudsigelige svar, mens højere temperature giver mere variation.

Throttling
Kontrol af hvor hurtigt requests sendes afsted. Det bruges til at undgå overbelastning, rate limits og ustabil drift.

Timeout
En tidsgrænse for hvor længe systemet venter på svar fra et API-kald. Hvis grænsen overskrides, betragtes kaldet som mislykket.

Tokens
Små tekststykker, som modellen arbejder med internt. Antallet af tokens påvirker både pris, svartid og hvor meget tekst der kan være med i et kald.

Upsert
En databaseoperation, der enten opretter en ny række eller opdaterer en eksisterende, hvis den allerede findes. Den bruges ofte til importjobs og deduplikering.

Userprompt
Den del af prompten, der indeholder brugerens konkrete opgave, spørgsmål og data, som modellen skal arbejde med.

Validering
Kontrol af om modellens output faktisk følger det forventede format og indeholder de nødvendige felter og datatyper.

Worker
En separat proces eller tråd, der henter opgaver fra en kø og udfører dem i baggrunden.
