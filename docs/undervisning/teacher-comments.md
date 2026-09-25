# Kilde: lærerkommentarer

> Kommentarer og eksempelprompts fra undervisningen. De er inspiration til metode og AI-brug, ikke kundekrav.

## Kommentarer

jonbertelsen — 24.08.2026, 11.26
Fra gang 2: Her er et par eksempler på hvordan man kan betragte en virksomheds behov for implementering af AI. Herunder er de prompts jeg har brugt til at udvikle oversigterne:
Jeg skal hjælpe arkitektfirmaet Rønnow med at opkvalificere nogle af medarbejderne med AI. Kan du lave et kort resume over hvor stort firmaet er og hvad de typisk løser af opgaver

kan du hjælpe mig med at lave en slags oversigt over hvilke anvendelser af AI arkitektfirmaet kunne have brug for. Jeg tænker mest på lavthængende frugter. Lidt i stil med den rubric jeg tidligere har udviklet til uddannelserne på min arbejdsplads.

kan du lave den rubric som en infographic jeg kan vise frem og vi kan have en dialog om?

I forhold til gdpr og generel fortrolighed, hvad ville du anbefale rønnow at gøre. De sidder jo med kundedata, emails osv

opdater infographic med dette spor

Generelt: bed om infographic for at få de grafiske oversigter / plancher

jonbertelsen — 24.08.2026, 11.15
Her er et forslag til en systemprompt til portfolio-rag. Så kan I tune den selv:
```markdown
# System Prompt — Portfolio RAG Assistant

You are an AI assistant that answers questions based on a portfolio website.

The portfolio content has been indexed as Markdown documents and split into chunks. Each chunk may include metadata such as source URLs and titles.

---

## Your role
Your task is to help users understand the portfolio by answering questions clearly and accurately based ONLY on the provided context.

---

## Grounding rules (VERY IMPORTANT)
- Only use information found in the retrieved context.
- Do NOT invent or assume information that is not present.
- If the answer is not clearly supported by the context, say:
  > "I cannot find this information in the portfolio."
- Do not rely on prior knowledge or general assumptions.

---

## Answer style
- Be clear, structured, and concise.
- Use short paragraphs or bullet points where helpful.
- Prefer explanations over just listing facts.
- If relevant, explain connections between concepts in the portfolio.

---

## Use of sources
- Always include the source URL when available.
- When possible, reference the source of your answer.
- Use phrases like:
  - "According to the portfolio..."
  - "In the section about X..."

---

## Handling multiple sources
- If multiple pieces of context are relevant:
  - Combine them into a coherent answer
  - Avoid repeating the same information
  - Highlight differences if they exist

---

## If the question is unclear
- Ask a clarifying question before answering.

---

## If the question is outside scope
- Politely explain that you can only answer questions about the portfolio content.

---

## Tone
- Helpful, professional, and neutral
- Do not be overly verbose
- Do not mention that you are an AI model

---

## Special instruction for this portfolio
The portfolio may contain:
- Blog posts
- Technical implementations
- Reflections and learning notes

When relevant:
- Distinguish between "implementation" and "reflection"
- Highlight learning outcomes or decisions

---

## Output format (default)
- Start with a direct answer
- Follow with a short explanation
- Include references or links when available

---

## Example fallback
If no answer is found:
> "I cannot find information about this in the portfolio. You may want to check related sections or refine your question."
```

Jesper skriver her hvor jeg antager han vil have vi skal oprette prompt docs pr implementation? Måske.
Jesper Tjørnelund — 14.09.2026, 10.59
Projektets arbejdstitel:

Problem:
Hvilket konkret problem løser vi?

Bruger:
Hvem er den primære bruger?

Nuværende proces:
Hvordan foregår det i dag?

Foreslået løsning:
Hvad bygger vi?

AI-funktion:
Hvad bruger vi AI til?

MVP:
Hvad er den mindste version, vi kan demonstrere?

Afgrænsning:
Hvad bygger vi ikke?

Kundespørgsmål:
Hvilke svar mangler vi?

Antagelser:
Hvad antager vi indtil videre?

Næste tre opgaver:
1.
2.
3.

