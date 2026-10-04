# 16: Create a guest invitation for an approved event

**What to build:** The primary contact person can create a shareable, read-only guest invitation for an approved event. Guests open the event through a public access link and see the approved guest-facing schedule and planning information in an invitation-style page.

**Blocked by:** 15: Complete depositum, booking and customer cancellation

**Status:** blocked

## In scope

- An `Opret invitation` action for an approved event.
- A guest URL in the form `/events/{id}?access={access}`. The access value is an unguessable invitation capability, separate from account JWTs.
- Copying the generated guest URL so it can be shared outside the application.
- A public guest route that checks the invitation capability and renders only approved guest-facing event information.
- A read-only invitation page with event details, schedule, timing and relevant planning/logistics information intended for guests.
- One shared invitation-page structure for all arrangement categories, with category-specific visual themes selected from the event category.
- A wedding visual theme as the only implemented theme in this MVP. Keep the category-to-theme seam ready for later conference, summer-house and other themes.

## Out of scope

- Editing invitation content or choosing a different template in the user interface.
- Conference, summer-house, party or other non-wedding theme visuals.
- Sending invitations by email, SMS or a third-party service.
- Guest accounts, guest messaging, RSVPs or guest-side changes to event data.
- Exposing Messenger, internal notes, direct contact details or other non-guest-facing information.

## Acceptance criteria

- [ ] The primary contact person can create an invitation only for an Owner-approved event.
- [ ] The application displays a shareable URL using `/events/{id}?access={access}` and supports copying it.
- [ ] A guest can open the link without an account and sees only the matching event's approved guest-facing information.
- [ ] Missing or invalid access values do not expose event information.
- [ ] The guest page is read-only and does not expose the dashboard or internal event controls.
- [ ] The page presents the event as an invitation, including its approved schedule, timing and relevant guest logistics, rather than as a dashboard.
- [ ] All categories use the same invitation-page structure; the event category selects the visual theme.
- [ ] Wedding is the only category with completed visuals in this MVP, and adding another category's theme does not require a separate page structure.
- [ ] Invitation content and theme cannot be edited by guests or selected by users.

## Verification

Demonstrate creating and copying an invitation for an approved wedding, opening it in a signed-out browser, verifying the guest-facing event information and read-only behavior, and denying access with an invalid capability. Confirm the category-to-theme selection seam with wedding as the implemented theme.

## Stop and ask if

- The invitation would include unapproved or internal event data.
- The project owner wants Owner or another role, in addition to the primary contact person, to create or rotate invitation links.
- The invitation access capability requires a lifetime, revocation or regeneration policy that is not specified by the project owner.

## Handoff

Report the invitation access boundary, guest-visible fields, category-to-theme mapping and how future themes can be added without changing the shared page structure.
