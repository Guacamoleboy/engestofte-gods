# Engestofte Gods — projektviden

Dette er den redigerede indgang til projektets kundemateriale. Projektets foreløbige retning er at undersøge, hvordan AI kan reducere manuelt administrativt arbejde hos Engestofte Gods uden at fjerne den personlige service.

## Start her

Hvis du skal forstå den nuværende MVP hurtigt, så læs i denne rækkefølge:

1. [Projektets README](../README.md) — kort over scope, routes og arbejdsform.
2. [Gældende projektbeskrivelse](projekt/02-projekt.md) — godkendt scope og source of truth.
3. [Historisk projektidé](projekt/01-projekt.md) — tidligere retning og baggrund.
4. [Godkendt grilling](grilling/01-projekt-afklaring.md) — beslutninger fra afklaringssessionen.
5. [Flow-definition](grilling/flow-definition.md) — konkrete trin og feltklassifikation.
6. [Domæneordbog](../CONTEXT.md) — fælles betydning af projektets begreber.
7. [Systemskitse](diagrammer/systemskitse.md) — applikationens hovedflow.
8. [MVP-specifikation](../.scratch/engestofte-mvp/spec.md) — samlet buildbar kontrakt for implementation.
9. [Arkitektur- og filkonventioner](standards/architecture-and-file-conventions.md) — forventet struktur for frontend og backend.
10. [Forventede entities](forventet/entities.md) — foreløbige domæneområder og entities.
11. [Forventede database-relationer](forventet/database.md) — PostgreSQL-relationer og åbne databasevalg.
12. [Forventet frontend](forventet/frontend.md) — pages, components, hooks og route guards.

De øvrige dokumenter er baggrund, kilder eller undervisningsmateriale. De bør kun læses, når opgaven kræver det.

1. [Kunde og forretning](01-kunde-og-forretning.md) — hvem kunden er, og hvordan virksomheden fungerer.
2. [Problemer og muligheder](02-problemer-og-muligheder.md) — de vigtigste arbejdsgange, smertepunkter og mulige AI-anvendelser.
3. [Julemarkedet](03-julemarkedet.md) — afgrænset problemområde med konkrete observationer fra mødet.
4. [Mulige projektretninger](04-mulige-projektretninger.md) — foreløbige idéer, scope og anbefalet næste skridt.
5. [Website- og kontaktfund](05-website-og-kontaktfund.md) — observerede UX-problemer og kontaktmuligheder.
6. [Visuel identitet](06-visuel-identitet.md) — farver, typografi, former og sprogvalg til fremtidige UI'er.
7. [Projektversioner](projekt/) — projektets valgte scope, mål og senere ændringer.

## Kildemateriale

De oprindelige noter er bevaret i [kilder](kilder/). De er ikke nødvendigvis sprogligt eller faktuelt kvalitetssikrede, men de skal bruges som sporbar baggrund for de redigerede dokumenter.

- [Mødenoter](kilder/1009-notes.md)
- [AI-opsummering af mødet](kilder/1009-ai-notes.md)
- [Spørgsmål og svar](kilder/previous-questions.md)
- [Rå transskribering](kilder/transkribering.md)
- [Stadeholderliste 2025](kilder/stadeholderliste-2025.md)
- [Smileyrapporter og fødevare-compliance](kilder/smileyrapporter/README.md)
- [Websnapshot fra Engestofte Gods](kilder/web/engestofte-website-2026-09-26.md)
- [PDF-tekstudtræk](kilder/pdf-tekst/)
- [PDF-backup](kilder/pdf-backup/)
- [PDF-materialer](materials/)
- [Undervisningsmateriale](undervisning/)

## Sådan læses dokumenterne

Udsagn markeret som **skal afklares** er ikke stærke nok til at bygge en løsning på endnu. Tal og procesdetaljer fra mødenoterne skal valideres hos kunden, før de bruges som krav eller KPI'er.

Persondata, kundeeksempler og fortrolige oplysninger skal anonymiseres, før de bruges i kode, demoer, prompts eller præsentationer.
