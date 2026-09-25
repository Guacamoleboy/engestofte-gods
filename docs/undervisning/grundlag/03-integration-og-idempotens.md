# Grundlag: Integration, deduplikering og idempotens

> Omskrevet fra den tidligere `toolbox.md`. Fokus er importer, ændringsdetektion, hashes og genbrug af LLM-resultater.

Integration og automatisering
Når du automatiserer større AI-jobs
Hvis du bygger et system, der henter data fra eksterne API’er, sammenligner med din egen database og derefter sender nye eller ændrede data videre til et LLM API, bør du tænke i idempotens, deduplikering, change detection og genbrug af tidligere resultater.

Målet er ikke kun at få processen til at virke, men også at undgå:

at importere den samme række flere gange
at behandle uændrede data igen
at sende det samme indhold til et LLM flere gange
at bruge tokens på arbejde, du allerede har gjort
Hash som værktøj til change detection
I mange integrationsløsninger er det en meget god idé at gemme en hash af hver importeret række eller af de vigtigste felter i rækken.

En hash er især nyttig, hvis du vil afgøre:

om indholdet i en række har ændret sig siden sidst
om to rækker i praksis indeholder det samme
om et tidligere LLM-resultat stadig kan genbruges
En hash er dog sjældent nok alene. I praksis er det ofte bedst at kombinere flere ting:

en ekstern id fra kildesystemet, hvis den findes
en hash af det relevante indhold
et tidspunkt for seneste import eller opdatering
en registrering af hvilken model og prompt-version der blev brugt til behandlingen
Tænk i tre forskellige problemer
Når man arbejder med dataintegration og LLM-behandling, er det nyttigt at skelne mellem tre forskellige spørgsmål.

1. Findes rækken allerede?
Her handler det om klassisk deduplikering på dataniveau.

Det løses ofte med:

ekstern id fra kildesystemet
unik nøgle i databasen
upsert i stedet for blind insert
Hvis et eksternt API leverer et stabilt id, bør du normalt gemme det og bruge det som primær reference.

2. Har indholdet ændret sig?
Her handler det ikke kun om, at rækken findes, men om den er den samme som sidst.

Det løses ofte med:

hash af de felter, der betyder noget
updated_at fra kildesystemet, hvis det er troværdigt
felt-for-felt-sammenligning ved behov
Hvis en række findes, men hash er uændret, er der ofte ingen grund til at sende den til et LLM igen.

3. Er denne LLM-behandling allerede udført?
Det er et separat spørgsmål. Selv hvis inputdata er de samme, kan resultatet være forskelligt, hvis du har ændret:

model
systemprompt
userprompt-skabelon
outputformat
parametre som temperature
Derfor bør du ikke kun gemme data om kilderækken, men også om selve behandlingen.

En praktisk tommelfingerregel
Hvis du vil undgå redundans, bør du som minimum kunne svare på:

Hvilken ekstern række kom data fra?
Hvad var indholdet, da vi sidst så det?
Har indholdet ændret sig siden sidst?
Er der allerede lavet en LLM-behandling af netop denne version?
Med hvilken model og prompt-version blev den behandling lavet?
Hvis du ikke kan svare på de spørgsmål, bliver det svært at undgå overflødige LLM-kald.

Anbefalet strategi
En robust løsning kan typisk bygges sådan:

hent data fra ekstern kilde
identificér rækken via ekstern id eller anden stabil nøgle
normalisér de felter, du vil sammenligne
beregn en hash af det relevante indhold
tjek om rækken allerede findes lokalt
hvis hash er uændret, spring LLM-behandling over
hvis hash er ændret eller rækken er ny, opret et behandlingsjob
gem LLM-resultat sammen med metadata om model, prompt-version og input-hash
Det gør pipelinen mere idempotent og billigere i drift.

Hvad skal hashen beregnes ud fra?
En hash bør normalt ikke laves af hele rå API-responsen, hvis responsen indeholder støj eller felter, der ændrer sig uden at være fagligt relevante.

Det er ofte bedre at hashe et normaliseret udsnit af data, for eksempel:

titel
brødtekst
kategori
tags
andre felter, som faktisk påvirker LLM-opgaven
Undgå gerne at lade disse felter påvirke hash-værdien, hvis de ikke er fagligt relevante:

importtidspunkt
interne databasenøgler
skiftende metadata
felter med tilfældig rækkefølge
Hvis du hasher støj, vil systemet tro, at data har ændret sig, selv når indholdet reelt er det samme.

Normalisering før hashing
Før du beregner hash, kan det være en fordel at normalisere input.

Det kan for eksempel være:

trim af whitespace
ensretning af linjeskift
ensretning af store og små bogstaver, hvis det er relevant
sortering af lister eller tags
fjernelse af irrelevante felter
Normalisering reducerer risikoen for falske ændringer.

Gem ikke kun import-hash, men også behandlingsmetadata
Hvis du kun gemmer en hash af rækken, ved du kun noget om kildedata. Du ved ikke nødvendigvis, om der allerede er lavet den rigtige LLM-behandling.

Det er derfor ofte en god idé at gemme:

source_id
content_hash
processed_at
model
prompt_version
eventuelt temperature
result_version
status for behandlingen
På den måde kan du skelne mellem:

samme data, samme behandling
samme data, ny behandling
nye data, ny behandling
Tænk i cache eller behandlingslog
For mange use cases giver det mening at have en tabel eller samling, der fungerer som en behandlingslog.

Den kan for eksempel koble disse ting sammen:

input-hash
model
prompt-version
outputformat-version
LLM-resultat
Så kan systemet genbruge et tidligere resultat, hvis præcis samme behandling allerede er udført.

Det er i praksis en form for cache, men med mere tydelig sporbarhed.

Hvornår bør man køre LLM igen?
Det giver ofte mening at genbehandle, hvis:

kildedata faktisk har ændret sig
du har skiftet model
du har forbedret prompten
du har ændret outputformat eller schema
du vil genkøre tidligere data for at hæve kvaliteten
Det vigtige er, at genkørslen sker bevidst og ikke bare som følge af en upræcis pipeline.

Brug idempotente jobs
Større integrationsjobs bør designes, så de kan køres igen uden at skabe dubletter eller unødige nye LLM-kald.

Det betyder typisk:

samme input må ikke give flere identiske inserts
samme job må kunne genstartes efter fejl
allerede behandlede rækker skal kunne springes over
fejlramte rækker skal kunne genkøres isoleret
Det er især vigtigt, hvis du bruger queue, workers eller batch-jobs.

Overvej en pipeline i flere trin
Det er ofte en god idé at opdele processen i trin:

import
normalisering
deduplikering
change detection
LLM-berigelse
lagring af resultat
Fordelen er, at du lettere kan:

logge hvor ting går galt
genkøre enkelte trin
teste logikken uden at bruge tokens
ændre LLM-delen uden at omskrive hele importen
Typiske felter i databasen
En praktisk datamodel kan ofte have felter som:

ekstern id
kilde
rå data eller normaliseret data
content hash
senest set
senest ændret hos kilden
senest behandlet
behandlingsstatus
modelnavn
prompt-version
LLM-output
Det er ikke altid nødvendigt at gemme alt, men jo mindre metadata du gemmer, jo sværere bliver det at styre genbrug og genkørsler.

Faldgruber
Nogle almindelige fejl i denne type integration er:

at bruge hele rå JSON-responsen som grundlag for hashing
at stole blindt på updated_at fra en ekstern kilde
at genkalde LLM for hver import, selv om indholdet er uændret
at overskrive tidligere resultater uden versionsstyring
at glemme at model- eller promptskift også kan kræve genbehandling
En god minimumsløsning
Hvis du vil holde det enkelt, men stadig undgå meget redundans, er dette ofte et fornuftigt minimum:

gem ekstern id
gem en hash af de vigtigste felter
gem LLM-resultat
gem modelnavn og prompt-version
spring behandling over, hvis både indhold og behandlingskonfiguration er uændret
Det er ofte nok til at spare mange tokens og samtidig holde systemet nogenlunde simpelt.

Afsluttende anbefaling
Når du bygger automatiserede AI-jobs med import, deduplikering og LLM-behandling, er det vigtigt at tænke i mere end bare “findes rækken allerede?”.

Du bør også kunne afgøre:

om indholdet har ændret sig
om netop denne version allerede er behandlet
om et tidligere resultat kan genbruges sikkert
Hvis du vil undgå redundans i automatiserede AI-jobs, bør du typisk kombinere:

stabil identifikation af kildedata
hash-baseret change detection
idempotent importlogik
registrering af model og prompt-version
genbrug af tidligere LLM-resultater, når input og behandling er de samme
På den måde kan du bygge en pipeline, der er mere robust, billigere i drift og bedre til at undgå at sende allerede behandlede data til et LLM igen.

