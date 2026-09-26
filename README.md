# Engestofte Gods

Engestofte Gods explores how a document-grounded AI solution can make it easier for potential wedding customers to submit a complete and qualified enquiry.

The first MVP focuses on weddings.

This repository is developed as an AP Computer Science project.

## MVP overview

Customers are guided through an AI-supported question flow that collects the most important wedding details and asks relevant follow-up questions. A draft can be saved locally and continued after the customer logs in or creates an account.

```text
engestofte-gods.dk/contact
        ↓
AI flow
        ↓
Draft saved locally
        ↓
Login or account creation
        ↓
/dashboard/events/
        ↓
Owner reviews the enquiry
        ↓
/dashboard/events/{id}
```

After approval, the customer gets access to the specific event. The customer and Engestofte Gods can view event details, communicate through Messenger, suggest changes and approve agreements. The status moves from draft and submitted enquiry to approval, deposit and booking.

The AI flow is intended to:

- collect the critical wedding details
- identify missing or conflicting answers
- present relevant, non-binding upsell suggestions
- handle known unavailable dates honestly
- flag uncertainty for review by the Owner

The raw customer draft is stored in `localStorage` until the customer logs in or creates an account. Only then is the enquiry sent to the backend and database. This is a school-prototype decision and is not production approval for GDPR or EU compliance.

## Scope

The MVP does not yet include:

- automatic calendar booking or guaranteed availability
- real payments or a payment provider
- binding offers, contracts or automatic booking
- full support for parties, hunting, conferences or Christmas markets
- automatic email delivery
- replacement of the existing Squarespace, Trello or finance systems

The deposit is simulated in the school project. Once the customer and Owner agree, the customer can mark the deposit as paid and the status becomes `Booked`.

## Technology

- Backend: Java 17, Maven, Javalin, Jackson, Hibernate/JPA and PostgreSQL
- Frontend: React, Vite and React Router
- AI: a document-grounded solution with OpenAI as a possible provider

## Documentation

- [Project documentation index](docs/README.md)
- [Current project description and MVP](docs/projekt/02-projekt.md)
- [Domain glossary](CONTEXT.md)
- [System diagram](docs/diagrammer/systemskitse.md)
- [Architecture and file conventions](docs/standards/architecture-and-file-conventions.md)
- [Expected system model](docs/forventet/)
- [RAG material and source documents](RAG/README.md)
- [Contributing guide](CONTRIBUTING.md)

---

<div align="center">
    <sub>Engestofte Gods — 2026</sub>
</div>
