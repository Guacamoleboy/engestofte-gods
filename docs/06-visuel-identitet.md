# Visuel identitet og UI-principper

Dette dokument fastholder observationer om Engestofte Gods' nuværende visuelle identitet. Det skal bruges som designreference, hvis vi senere laver en prototype eller nye UI-elementer.

## Kilde og sikkerhed

Oplysningerne er baseret på brugerens gennemgang af den nuværende hjemmeside den 26. september 2026. Squarespace er den huskede platform og bør bekræftes, før tekniske løsninger planlægges.

## Farver

| Rolle | Hex |
| --- | --- |
| Primær grøn | `#4F6C41` |
| Primær hvid | `#FFFFFF` |

Den grønne og hvide farve skal behandles som de primære brandfarver i eventuelle prototyper.

## Former og kanter

- Alle elementer har aktuelt `border-radius: 0` / `0rem`.
- UI'et skal derfor bruge skarpe, professionelle linjer.
- Undgå runde kort, pill-knapper, overdreven blødhed og et cartoon-agtigt udtryk.
- Nye komponenter skal føles som en naturlig videreførelse af det eksisterende udtryk.

## Typografi

- Den observerede fontfamilie er `Alice`.
- Den præcise kilde, licens og indlæsning af fonten er endnu ikke bekræftet.
- Brug ikke en tilfældig erstatning i en endelig prototype uden først at undersøge, hvordan `Alice` anvendes på den eksisterende side.

## Knapper

På hvid baggrund:

- grøn kant
- hvid baggrund
- grøn tekst
- grøn hover-tilstand

På grøn baggrund bruges den omvendte kontrastlogik. Den præcise hover- og tekstfarve skal måles fra den aktuelle side, hvis komponenterne skal genskabes nøjagtigt.

## Sprogvalg

Engestofte har primært tre sprog:

- `[DK]`
- `[EN]`
- `[DE]`

Mette ønsker tekstbaserede sprogvalg i stedet for flag. UI'et bør derfor vise korte tekstlabels og ikke tre store flagikoner.

## Designkrav til fremtidigt UI

1. Bevar den grønne/hvide identitet.
2. Brug skarpe hjørner og tydelig struktur.
3. Sørg for ensartet navngivning og casing i navigationen.
4. Design sprogskift, så layoutet ikke hopper unødigt mellem sprog.
5. Gør kontakt og næste handling visuelt tydelig uden at gøre udtrykket aggressivt.
6. Test både dansk, engelsk og tysk, før en komponent anses som færdig.