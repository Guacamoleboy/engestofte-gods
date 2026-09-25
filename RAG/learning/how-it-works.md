<!-- ------------------------------------------------------------------------------------------ -->

<div style="margin-top: 100px"></div>

<div align="center">

# AIDA — AI Driven Applications

**© 2026 Guacamoleboy. All rights reserved.**

This document is part of the **AIDA — AI Driven Applications** project by Guacamoleboy.

</div>

<div style="margin-bottom: 100px"></div>

<!-- ------------------------------------------------------------------------------------------ -->

---

# How does RAG work and what are the different components used?

### Instructions

Instructions tell the AI how it should behave. Here you can tell it what kind of assistant it should be, how it should answer questions, and what it should do if it doesn't know the answer.

For example, we can tell it to answer in Danish, keep answers simple, and not make up information.

Input: raw text

---

### Variables

Variables are basically pieces of information that can change. They can be used to give the AI extra information when it is running.

For example, a variable could contain a user's name, a question, or some other value that we want to pass to the AI.

Input: data variables like github secrets

---

### Knowledge

Knowledge is where we give the AI its own information to work with.

This is where RAG becomes useful. Instead of expecting the AI to know everything itself, we can give it documents containing the information we want it to use.

The documents are split into smaller pieces and turned into embeddings. When someone asks a question, RAG searches through these pieces and finds the ones that are most relevant. Those pieces are then given to the AI, which uses them to create an answer.

So basically:

**Question → Search the knowledge → Find relevant information → AI generates an answer**

This makes it possible to build an AI that can answer questions about specific documents without having to train a whole new AI model.

Input: .md files for knowledge, context and information.

---

### Vision

Vision is used when we want the AI to understand images.

For example, instead of only giving the AI text, we could give it a screenshot, diagram, or another image and ask it about what is shown.

Input: Images

---

<!-- ------------------------------------------------------------------------------------------ -->

<div align="center">
    <sub>AIDA - AI Drevne Applikationer - © 2026 Guacamoleboy</sub>
</div>

<!-- ------------------------------------------------------------------------------------------ -->