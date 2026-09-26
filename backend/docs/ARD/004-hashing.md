# ADR-004: Hashing

## Status
Approved

## Context
The REST API needs to comply with EU regulation and responsible data handling. This results in the need for hashing.
Passwords, email and other sensitive information must not be stored in plaintext.

## Decision
The REST API uses BCrypt for password and email hashing.
Passwords and emails will be hashed before going into the database.
When a member logs in the provided password will be verified using BCrypt against the stored hash.

## Alternatives
SHA-256
MD5
SHA-512
SHA-1

## Consequences
- Data is never stored in plaintext.
- Password verification happens once a provided password is available.
- BCrypt might be slower than alternatives, but it's minor.
