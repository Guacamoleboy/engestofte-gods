# Grundlag: Robuste LLM-applikationer

> Omskrevet fra den tidligere `toolbox.md`. Fokus er prompts, output, validering, fejl, sikkerhed og drift.

1. Start med en enkel version
Når du bygger første version, så hold løsningen enkel.

En god første version kan ofte nøjes med:

én systemprompt
én userprompt
ét endpoint i din backend
ét klart input
ét klart outputformat
Eksempel:

input: en tekst
systemprompt: beskriver modellens rolle
userprompt: indeholder opgaven
output: et JSON-svar med nogle få felter
Det vigtigste er først at få hele kæden til at virke:

klient → backend → LLM API → backend → svar tilbage

2. Definér formålet tydeligt
Før du skriver prompts eller kode, bør du kunne svare klart på:

Hvad skal applikationen hjælpe med?
Hvad skal modellen gøre?
Hvad skal modellen ikke gøre?
Hvem er brugeren?
Hvordan skal svaret bruges bagefter?
Jo mere præcist formålet er, jo lettere er det at designe en god løsning.

Dårligt formål:

“Brug AI til noget smart med teksten.”

Bedre formål:

“Giv struktureret feedback på en opgave ud fra en rubric og returnér resultatet som JSON.”

3. Tænk i systemprompt og userprompt
Det er ofte nyttigt at skelne mellem:

Systemprompt
Systemprompten beskriver typisk:

modellens rolle
opgaven
regler for adfærd
ønsket tone
begrænsninger
outputformat
Eksempler på ting, der kan stå i systemprompten:

du er en faglig feedbackassistent
du må kun bruge det materiale, du får
du må ikke opfinde information
du skal være konkret og konstruktiv
du skal returnere gyldig JSON
Userprompt
Userprompten indeholder typisk:

selve inputdata
brugerens konkrete spørgsmål
det materiale, der skal analyseres
eventuelle kriterier, rubric eller instruktioner
En god tommelfingerregel er:

systemprompt = regler og rolle
userprompt = opgave og data
4. Gør input tydeligt og afgrænset
Et LLM svarer bedre, når input er klart struktureret.

Det hjælper ofte at:

sætte tydelige overskrifter i prompten
markere data med labels
adskille instruktioner fra selve indholdet
undgå unødigt støj i input
Eksempel:

Rubric:
...

Student submission:
...

Task:
Assess the submission using the rubric.
Return valid JSON.
Hvis input er langt eller rodet, bliver output ofte dårligere.

5. Definér outputformat på forhånd
Mange fejl i AI-drevne applikationer opstår, fordi udvikleren ikke har besluttet, hvordan output skal se ud.

Derfor bør du tidligt definere:

hvilke felter svaret skal indeholde
hvilke typer data de skal være
om svaret skal være fri tekst eller struktureret JSON
hvad der skal ske, hvis modellen ikke følger formatet
Det er ofte en stor fordel at bede modellen returnere struktureret output.

Eksempel:

{
  "summary": "Kort vurdering",
  "strengths": ["..."],
  "weaknesses": ["..."],
  "suggestions": ["..."]
}
Det hjælper også meget at give modellen et eksempel på ønsket output.

6. Giv gerne et output-eksempel
Hvis du vil have stabilt output, er det ofte en god idé at vise modellen et eksempel på den struktur, du ønsker.

Det kan gøre det lettere for modellen at forstå:

hvilke felter der skal med
hvordan de skal navngives
hvor detaljeret svaret skal være
hvordan lister og værdier skal formateres
Eksempel:

Return JSON in this format:
{
  "overallAssessment": "...",
  "criteria": [
    {
      "name": "...",
      "score": 1,
      "comment": "..."
    }
  ],
  "suggestions": ["..."]
}
7. Validér og håndtér output i backenden
Du må ikke gå ud fra, at modellen altid svarer perfekt.

Din backend bør derfor være klar til at håndtere tilfælde, hvor:

output ikke er gyldig JSON
et felt mangler
modellen returnerer ekstra tekst
værdier har forkert datatype
svaret er tomt eller ufuldstændigt
Overvej derfor:

at validere output
at lave fallback-håndtering
at logge fejl
at returnere en kontrolleret fejl til klienten
Med andre ord: stol ikke blindt på modellen.

8. Håndtér fejl fra API’et
Et eksternt API kan fejle af mange grunde:

timeout
netværksfejl
rate limit
ugyldig API-nøgle
serverfejl hos udbyderen
Derfor bør du have en plan for:

hvad brugeren ser ved fejl
hvad der logges
om requesten skal prøves igen
hvornår du stopper med at prøve igen
Det er vigtigt, at din applikation fejler på en kontrolleret måde.

9. Brug retries med omtanke
Hvis et request fejler midlertidigt, kan det give mening at prøve igen. Men det skal gøres fornuftigt.

Typiske teknikker er:

retry: prøv igen et begrænset antal gange
backoff: vent længere mellem hvert nyt forsøg
exponential backoff: ventetiden vokser for hvert forsøg
jitter: tilføj lidt tilfældig variation i ventetiden, så mange klienter ikke prøver igen på samme tidspunkt
Det er især relevant ved:

rate limits
midlertidige netværksfejl
kortvarige serverproblemer
Brug ikke uendelige retries.

10. Undgå at sende for mange requests på én gang
Hvis du har mange opgaver, dokumenter eller prompts, så lad være med bare at sende dem alle afsted samtidig.

Det kan give problemer med:

rate limits
unødigt høje omkostninger
timeout
kø på din egen server
ustabil brugeroplevelse
Overvej i stedet:

throttling
kø-system
begrænset parallelitet
batching, hvis use casen tillader det
Nyttige begreber
Throttling
Begrænser hvor hurtigt eller hvor mange requests du sender afsted.

Queue
Læg opgaver i kø og behandl dem i kontrolleret tempo.

Worker
En separat proces eller tråd, der tager opgaver fra en kø og udfører dem.

Rate limiting
En grænse for hvor mange kald der må laves i et givent tidsrum.

11. Tænk over synkront vs. asynkront arbejde
Nogle AI-opgaver er små og hurtige. Andre kan tage længere tid.

Synkront request
Klienten sender en request og venter på svar med det samme.

Det passer godt til:

korte prompts
hurtige analyser
simple chat-lignende funktioner
Asynkront job
Klienten starter en opgave, og systemet behandler den i baggrunden.

Det passer godt til:

store dokumenter
mange samtidige opgaver
batch-behandling
længere analyser
Et typisk mønster kan være:

klienten kalder et endpoint
backend opretter et job
jobbet behandles i en worker eller separat tråd
resultatet gemmes i database eller fil
klienten kan senere hente status eller resultat
Det er ofte en bedre arkitektur end at lade brugeren vente længe på ét HTTP-kald.

12. Gem status og resultater
Hvis du arbejder med længere AI-processer, er det en fordel at gemme:

job-id
status
tidspunkt
input
output
fejlbeskeder
Mulige statustilstande:

queued
running
completed
failed
Det gør det lettere at:

vise status i en frontend
fejlfinde
genkøre opgaver
analysere brugen af systemet
13. Log det vigtige
Når du udvikler AI-drevne applikationer, er logs meget vigtige.

Du bør som minimum logge:

hvornår et request blev startet
hvilket endpoint der blev kaldt
responstid
fejltyper
statuskode fra API’et
eventuelt modelnavn
Vær dog forsigtig med at logge følsomme data eller fulde prompts, hvis de indeholder persondata.

14. Beskyt API-nøgler og hemmeligheder
API-nøgler skal behandles som hemmeligheder.

Derfor bør du:

aldrig hardcode dem i koden
aldrig lægge dem i Git
læse dem fra environment variables eller secrets
holde dem på backend, ikke i frontend
En frontend bør som hovedregel ikke kalde modeludbyderen direkte med en hemmelig nøgle.

Den sikre løsning er normalt:

frontend → din backend → LLM API

15. Tænk over pris og ressourceforbrug
LLM-kald koster ofte penge og tid. Derfor bør du overveje:

hvor store prompts du sender
hvor ofte du kalder modellen
om samme prompt bliver sendt mange gange
om du kan cache svar
om hele dokumentet behøver at sendes hver gang
Det er god praksis at tænke i:

begrænset input
genbrug af resultater
tydelig afgrænsning af opgaven
16. Vær opmærksom på sikkerhed og data
Mange AI-drevne applikationer arbejder med tekster, dokumenter eller brugerinput. Derfor skal du tænke over:

indeholder input persondata?
må disse data sendes til en ekstern modeludbyder?
hvor bliver data gemt?
hvor længe bliver de gemt?
hvem har adgang til dem?
Du bør også tænke over prompt injection og skadeligt input.

Eksempler:

brugeren prøver at få modellen til at ignorere reglerne
brugeren indsætter indhold, der ændrer modellens instruktioner
input forsøger at få systemet til at afsløre skjulte prompts
Hvis input kommer fra brugere eller dokumenter, skal du ikke automatisk stole på det.

17. Evaluer kvaliteten kritisk
Et LLM-svar kan lyde overbevisende uden at være godt.

Derfor bør du teste din løsning på flere eksempler og spørge:

Er svaret relevant?
Er det for generisk?
Holder det sig til opgaven?
Følger det rubricen eller kriterierne?
Er output stabilt nok?
Er der fejl eller hallucinationer?
I mange tilfælde er det nødvendigt at justere:

prompts
inputstruktur
outputkrav
temperature og andre parametre
validering og efterbehandling
18. Byg løsningen i lag
Det er ofte smartest at bygge i denne rækkefølge:

Niveau 1 – minimal prototype
ét endpoint
én prompt
ét simpelt output
manuel test
Niveau 2 – brugbar løsning
bedre prompts
struktureret output
validering
fejlhåndtering
enkel frontend
Niveau 3 – robust løsning
retries med backoff
throttling
job-kø
asynkron behandling
status-endpoints
lagring i database
bedre logging og overvågning
Niveau 4 – mere skalerbar løsning
worker-arkitektur
begrænset parallelitet
caching
rate-limit-kontrol
tydelig sikkerhedsmodel
monitorering af omkostninger og performance
19. Stil dig selv disse spørgsmål
Når du bygger en AI-drevet applikation, er det ofte nyttigt at stoppe op og spørge:

Hvad er det præcist, modellen skal hjælpe med?
Er min prompt tydelig nok?
Er input afgrænset og relevant?
Er output let at bruge programmatisk?
Hvad gør jeg, hvis modellen svarer forkert?
Hvad gør jeg, hvis API’et ikke svarer?
Hvor mange requests kan systemet håndtere?
Hvornår bør opgaven flyttes til baggrundsbehandling?
Hvordan beskytter jeg data og API-nøgler?
Hvordan evaluerer jeg, om løsningen faktisk virker?
20. En god tommelfingerregel
Jo mere kritisk en funktion er, jo mindre bør du stole blindt på fri tekst fra modellen.

Hvis output skal bruges direkte i et system, så bør du:

gøre instruktionerne tydelige
kræve struktur
validere output
logge fejl
have fallback-planer
Kort opsummering
Når du laver en AI-drevet applikation, bør du som minimum tænke over:

formål
systemprompt
userprompt
inputstruktur
outputformat
validering
fejlhåndtering
sikkerhed
pris og performance
Når løsningen bliver større, bør du også tænke over:

retries
backoff og jitter
throttling
kø-systemer
workers
asynkron behandling
lagring af status og resultater
logging og monitorering
Målet er ikke bare at “få svar fra en model”, men at bygge en softwareløsning, hvor AI indgår på en robust, gennemtænkt og ansvarlig måde.

