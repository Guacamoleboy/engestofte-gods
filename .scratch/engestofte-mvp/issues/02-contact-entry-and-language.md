# 02: Build the Engestofte Gods contact entry side

**What to build:** A convincing customer-facing `/kontakt` page that starts the enquiry without requiring authentication.

**Blocked by:** 01: Establish the application shell and generic UI foundation

**Status:** ready-for-agent

## In scope

- Engestofte Gods introduction, leadership images and CTA to `/ai-flow`.
- Smiley-report link in the footer.
- Danish, English and German language dropdown with Danish as default.
- Language-aware page labels and accessible navigation.

## Out of scope

- AI conversation, user accounts or request persistence.
- IP-based language detection.
- Full translation of future event-platform content.

## Acceptance criteria

- [ ] A customer understands what Engestofte Gods offers and how to start a wedding enquiry.
- [ ] The CTA opens `/ai-flow` without opening login first.
- [ ] The smiley-report link is visible in the footer.
- [ ] The language dropdown changes the entry-page language and defaults to Danish.
- [ ] The page works with the generic shell from Ticket 01.

## Verification

Walk through `/kontakt` in Danish, English and German and follow the CTA. Verify the footer link and visible leadership imagery manually.

## Stop and ask if

- The available approved source material does not support an image, text or compliance link.
- A translation changes domain meaning rather than only wording.
- The page requires a new design system or unrelated refactor.

## Handoff

Report content sources, language assumptions and the exact handoff into `/ai-flow`.
