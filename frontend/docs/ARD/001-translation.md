# ADR-001: Frontend translations

## Status

Accepted

## Context

Scope 2 requires the public contact entry page to support Danish, English and German, with Danish as the default. The same labels are used by the public navigation, contact page and footer. The application should be straightforward to extend without putting all copy into one large source file.

## Decision

- Keep each language in its own JSON file under `frontend/src/shared/data/i18n/` (`da.json`, `en.json` and `de.json`).
- Use the same English key names and JSON structure in every language file.
- Keep Danish available as the initial fallback and load English or German through dynamic imports when selected.
- Keep the active language in the public layout, default it to Danish, and set the document's `lang` attribute to match.
- Do not infer language from IP or location. Persisting language across reloads is not part of this scope.

## Alternatives

### One TypeScript dictionary

Rejected because all languages and application logic would accumulate in one source file, making copy editing and review harder.

### A translation library

Not introduced because the current scope needs three small, static dictionaries and does not yet require plural rules, interpolation catalogs or server-side locale negotiation.

### Load every locale eagerly

Rejected in favor of dynamic imports so only the chosen language bundle needs to load at runtime.

## Consequences

- Copy is easy to review and update independently from React components.
- Every locale must retain matching keys and structure; the shared TypeScript contract helps catch missing fields.
- English or German copy may briefly retain the previously rendered language while its JSON chunk loads.
- Danish is included with the initial application bundle; the other locale dictionaries are loaded on demand.
- Future flows can reuse this loading mechanism, but need their own translated keys before becoming language-aware.
