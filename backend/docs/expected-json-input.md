# Expect JSON input

Used at initial setup to establish entities and DTOs.

## Context

In order to redesign the workflow around DTOs, entities and DAOs, the design starts with the expected JSON structure.
This provides the basis for the first DTOs and entities.

NOTE: This is an estimate and not the final solution.

# Member (US-01)

- Basic customer information.

## Expected Input

```JSON
{
  "first_name": "Name",
  "last_name": "Lastname",
  "email": "mail@mail.dk",
  "phone": "+4560606060",
  "date_of_birth": "date",
  "gender": "gender",
  "membership_id": "membershipId"
}
```

# Membership (US-01)

- Either registered as a Populate.java file or from frontend itself.

## Expected Input

```JSON
{
  "name": "Premium",
  "description": "This is a text...",
  "price": 99.00,
  "currency": "DKK",
  "duration": "YEARLY",
  "start_date": "date",
  "end_date": "date",
  "guest_pass": true
}
```
