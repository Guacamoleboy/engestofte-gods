# MCP i Engestofte Gods

## Hvad MCP gør

MCP kan forstås som et maskinlæsbart register over værktøjer, som en AI-klient kan opdage og bede om at få udført. Et værktøj har en beskrivelse, et inputformat og en tilknyttet funktion. MCP-serveren udfører funktionen og sender resultatet tilbage til AI-klienten.

En API-beskrivelse i `docs/` kan fortælle OpenAI, hvilke REST-endpoints der findes, og hvordan de bruges. Hvis beskrivelsen sendes som prompt, kan modellen bruge den som viden, men den kan ikke selv udføre HTTP-kaldet. Applikationen skal stadig modtage modellens forslag og kalde endpointet. MCP kobler værktøjsbeskrivelsen til en funktion, som AI-klienten kan opdage og kalde gennem MCP.

MCP er derfor hverken selve REST-API'en eller et krav for, at OpenAI kan bruge REST. Backenden kan også selv kalde REST-endpoints eller give modellen værktøjer gennem OpenAI's eget værktøjskaldsformat. MCP er relevant, hvis man ønsker et fælles register og en standardiseret adgang til værktøjer fra AI-klienter.

## Eksempel: opslag af bryllupsdato

MVP'en har ikke en kalenderintegration. Hvis Engestofte senere får en kalender med aktuelle bookinger, kunne den stille et værktøj til rådighed:

```text
check_wedding_date(date)
→ status: ledig | optaget | ukendt
```

Når kunden nævner en dato, kan AI-klienten opdage værktøjet og bede om et opslag. MCP-serveren kalder en backendfunktion, som fx `findDateByText()` eller en mere præcist typet kalenderfunktion. Funktionen slår datoen op i kalenderen og returnerer status. AI'en formulerer derefter svaret ud fra resultatet og systempromptens regler.

MCP-serveren kunne kalde backendens funktion direkte. Hvis kalenderen i stedet blev gjort tilgængelig via et REST-endpoint, kunne værktøjet kalde det endpoint. I begge tilfælde kræver et ledigt opslag fortsat Ownerens bekræftelse, før kunden får en endelig aftale.

## Mulig placering i projektet

I den nuværende løsning sender Java-backenden systeminstruktioner, rubric og kundens oplysninger til OpenAI og modtager et struktureret svar. Der er ingen MCP-server eller kalenderintegration. Hvis projektet senere fik flere systemer, som en AI-assistent skulle kunne slå op i, kunne en Java-baseret MCP-server registrere afgrænsede værktøjer som kalenderopslag eller opslag i aktuelle, godkendte pakkeoplysninger.

Backenden skal fortsat håndhæve adgang, validere input og begrænse, hvilke kunde- og eventdata værktøjerne kan se. MCP registrerer og formidler værktøjskald; det erstatter ikke systemprompten, rubricen, REST-API'ens sikkerhed eller projektets godkendelsesflow. Værktøjer til at sende beskeder, ændre eventdata eller bekræfte bookinger bør ikke træffe beslutninger på AI'ens vegne.

For MVP'en løser prompts og rubric opgaven, fordi aktuelle eksterne opslag ikke indgår. MCP bliver først konkret nyttigt, hvis en AI-klient skal opdage og bruge rigtige funktioner eller datakilder, fx en kalenderintegration.