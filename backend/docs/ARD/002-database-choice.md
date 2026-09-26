# ADR-002: Database Choice

## Status
Approved

## Context
The application needs a place to store its data. The database should work with Java and be able to store the required information.
and the needed size of data.

## Decision
PostgreSQL is selected because it is a well-supported relational database for the application and the technology is familiar to the developer.

## Alternatives
MySQL
MongoDB
MariaDB

The alternatives are rejected so the project can focus on completing the required tasks instead of extending the technology scope.

## Consequences
- The application uses a reliable database system that is able to handle the information it needs to.
- The frontend user input has a place to be stored.
- PostgreSQL works well with Java.
- Developers need a local environment set up for development.
