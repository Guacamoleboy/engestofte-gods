# Engestofte Gods - Aflastning og øgning af omsætning.

Project created by Jonas for the 4th Term of AP Computer Science Denmark.

[![Visit my solution](https://img.shields.io/badge/Visit-ffffff?style=for-the-badge&color=f99e00)](http://engestofte.guacamoleboy.dk)

---

> [!NOTE]  
> All files in the **[main]** branch are final

---

## Visual Presentation

N/A for now.

---

## Links

Backend: N/A\
Frontend: N/A\
Docs: N/A

---

## Short presentation

Project Presentation at some point.

---

## MVP

Minimal Viable Product

---

## Folder Structure

This section is to showcase our folder structure. I am using a shared / feature architecture for out frontend application and a normal CRUD REST API Setup for our Java backend.

### Backend

```text
backend/
└── src/
    |
    └── main/
        |
        ├── resources/
        |   |
        |   ├── config.properties
        |   ├── .env
        |   ├── .env.development
        |   ├── .env.test
        |   ├── logback.xml
        |   ├── prompts/
        |   ├── rubric/
        |   └── http/
        |
        └── java/
            |
            └── alpha/
                |
                ├── <domain>/
                |   |
                |   ├── controller/
                |   ├── service/
                |   ├── dao/
                |   ├── entity/
                |   ├── dto/
                |   ├── mapper/
                |   └── ...
                |
                ├── config/
                ├── crud/
                ├── exception/
                ├── server/
                ├── security/
                ├── service/
                ├── route/
                ├── dao/
                ├── util/
                └── Main.java
```

### Frontend

```text
frontend/
└── src/ 
    |
    |
    ├── api/
    |   ├── endpoints/
    |   ├── client.js
    |   ├── crud.js
    |
    ├── app/
    |   ├── pages/
    |   ├── routes/
    |   ├── layout/
    |   ├── App.jsx
    |   └── main.jsx
    |
    ├── features/
    |       └── home-page/
    |               └── component-name/
    |                       ├── ComponentName.jsx
    |                       ├── ComponentName.hooks.js (If needed)
    |                       └── ComponentName.module.css
    |
    └── shared/
            ├── styles/
            |   └── globals.css
            ├── data/
            └── components/
                    └── component-name/
                            ├── ComponentName.jsx
                            ├── ComponentName.hooks.js (If needed)
                            └── ComponentName.module.css
```

---

<div align="center">
    <sub>Engestofte Gods - Created by Jonas - 2026</sub>
</div>