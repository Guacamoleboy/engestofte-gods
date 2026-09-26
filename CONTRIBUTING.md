# Contribution guide for Engestofte Gods Project

This document establishes the code standards for the Engestofte Gods project. Any future contributor is expected to follow them when modifying the project.

## 1. Naming and Formatting

### General

- English unless the document is intended for the project owner or customer.
- Use clear and descriptive names for variables, classes and other features.
- Avoid abbreviations.

### Java

- Classes use PascalCase
- Methods and variables use camelCase
- Constants use UPPER_SNAKE_CASE

### JavaScript

- Variables and functions use camelCase
- React components use PascalCase
- Constants use UPPER_SNAKE_CASE

### Markdown

- Use parent / child node layout
- Be clear and professional

## 2. Folder Structure

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
        |   └── http/
        |
        └── java/
            |
            └── <base-package>/
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

## 3. Branch rules

- Do not work in main or development
- Create your own branch from development

## 4. Commit Messages

Should be clear, professional, short and no long essays. It's simply to establish what was done. The overall depth will be in the Pull Request.

## 5. Pull Requests

- Should have a title
- Should always go to the development branch unless it's a main release
- Should follow the pull_request_template provided during pull request
- Should allow the other person to approve the pull request prior to codebase entry (Tine ?)
- Pass all CI checks

## 6. DTO

In order to comply with EU law, GDPR and internal structures, the DTO logic is treated as a strict project rule. The following principles always apply.

- Do not expose database IDs or other critial information in the DTOs.
- Request and response DTOs should be seperated.
- DTO names should follow the entity names.

Example:

Member (Entity)
MemberResponse
MemberRequest

## 7. Validation

Validation happens before service logic runs. The application should respond immediately when a person without access tries to enter.
Validation errors should be consistent and professional.

## 8. Exception handle

The project uses Java exception handling to provide customer feedback through a notification UI element.
