# Architecture and file conventions

This document records the project owner’s expected conventions for frontend and backend implementation. Agents must read it before creating or modifying implementation files.

These conventions are implementation standards, not domain decisions. Domain meaning still comes from [CONTEXT.md](../../CONTEXT.md), and feature behavior comes from the relevant specification and ticket.

## Technology stack

The current stack is based on the repository configuration. Add new technologies to this section when they are deliberately introduced.

### Backend

- Java 17.
- Maven, configured through `backend/pom.xml`.
- Javalin 7 as the HTTP and REST API framework.
- Jackson for JSON serialization and deserialization.
- Hibernate/JPA for persistence.
- PostgreSQL as the database.
- Lombok for the established Java boilerplate conventions.
- JWT and jBCrypt for the current authentication and password-related foundation.
- SLF4J and Logback for logging.
- JSoup and dotenv-java where the existing backend uses them.
- A normal REST API organized around domain-based packages.
- OpenAI may be integrated in the backend as the AI provider; this is a planned integration seam, not a current installed dependency.

### Frontend

- React.
- TypeScript as the project language and convention for new frontend code.
- Vite as the development and build tool.
- React Router for routing.
- React Helmet Async for document metadata.

The current `frontend/package.json` contains React and Vite, but does not yet declare TypeScript. TypeScript setup must be handled by an explicit ticket before new TypeScript files are introduced broadly.

## Shared CSS variables and colors

When a value is reused across the frontend, define it once in the global `:root` scope instead of duplicating it in component styles.

All Engestofte variables must use the `--engestofte-<name>` naming convention and be consumed with `var(--engestofte-<name>)`.

Example:

```css
:root {
	--engestofte-green: #4f6c41;
	--engestofte-white: #ffffff;
	--engestofte-text: #1f2933;
	--engestofte-overlay: rgba(79, 108, 65, 0.12);
}
```

Use either hexadecimal or `rgba(...)` color values. Do not introduce arbitrary repeated color literals in component styles when the value is shared or likely to be reused.

Add a new `--engestofte-<name>` variable whenever a color, spacing value, radius, shadow, typography value or other design value becomes a shared project convention.

## General formatting

- Use tabs for indentation. Do not use spaces for indentation.
- Keep the established visual separator comments between major sections and methods.
- Keep comments close to the code they document.
- Add the project-specific pathing comment at the top of frontend files that support comments.
- Preserve the existing package and folder structure unless a ticket explicitly authorizes a structural change.
- Do not perform unrelated formatting or refactoring while implementing a ticket.

## Frontend conventions

### Pathing header

Files such as `.jsx`, `.tsx`, `.ts` and `.js` should begin with a pathing block that makes the file’s location explicit:

```js
// Pathing
// _______
// src/api/client.js
```

The path in the comment must match the actual repository path.

### Separation of responsibilities

Keep responsibilities separated:

- API communication belongs in `src/api/`.
- Page composition belongs in `src/app/pages/`.
- Routing belongs in `src/app/routes/`.
- Layout belongs in `src/app/layout/`.
- Feature-specific UI belongs in the relevant `src/features/<feature>/` folder.
- Reusable UI belongs in `src/shared/components/`.
- Shared styling belongs in `src/shared/styles/`.
- Static or shared data belongs in `src/shared/data/`.

If a component needs hooks, create a sibling `.hooks.js` or `.hooks.ts` file. Hook functionality must not be placed directly in `.jsx` or `.tsx` files.

If a component needs styles, create a sibling `.module.css` file. Do not place large component-specific style blocks directly inside the component file.

Expected feature shape:

```text
<feature-name>/
├── <ComponentName>.jsx
├── <ComponentName>.hooks.js      # only when hooks are needed
└── <ComponentName>.module.css    # only when component styles are needed
```

The files are created when the component needs them. Do not create empty `.hooks.js` or `.module.css` files without a reason.

### API client shape

API client files should keep the base URL and request setup visible and documented. The current development-only style is:

```js
// Pathing
// _______
// src/api/client.js

// Development only for now.
const BASE_URL = 'http://localhost:7070/v1'

export async function client(endpoint, options = {}) {

	// ---- SETUP ----------------------------------------------------------------------------------------------------------

	const url = `${BASE_URL}${endpoint}`
	const token = localStorage.getItem('access_token')

	// DEBUG FOR NOW
	console.log('[API REQUEST]')
	console.log('URL:', url)
	console.log('METHOD:', options.method || 'GET')
	console.log('BODY:', options.body || null)

	// ---- CONFIG --------------------------------------------------------------------------------------------------------

	const config = {
		...options,
		headers: {
			'Content-Type': 'application/json',
			...(token ? { Authorization: `Bearer ${token}` } : {}),
			...options.headers,
		},
	}

	// The implementation may evolve, but API concerns stay in the API layer.
}
```

The debug logging is development-only and must not expose secrets or sensitive customer data in production.

## Backend conventions

### Domain-based package structure

Backend code is organized by domain. The current project package examples use `alpha` as the Java package root, even though the product is Engestofte Gods. Do not rename the package root as part of an unrelated feature.

Example imports:

```java
import alpha.domain.role.dao.RoleDAO;
import alpha.domain.role.entity.Role;
import alpha.domain.role.enums.RoleName;
import alpha.service.EntityManagerService;
```

Within a domain, keep related classes together:

```text
<domain>/
├── controller/
├── service/
├── dao/
├── entity/
├── dto/
└── mapper/
```

### Service convention

Services extend the shared `EntityManagerService<T>` where that base service is appropriate. The DAO is created through the constructor and stored as a typed field.

Every service includes an `// Attributes` section, even when the class has no attributes. Keep the separator between the attributes and each method.

```java
public class RoleService extends EntityManagerService<Role> {

	// Attributes
	private final RoleDAO roleDAO;

	// _________________________________________________________________________________________________________________

	public RoleService(EntityManager em) {
		super(new RoleDAO(em), Role.class);
		this.roleDAO = (RoleDAO) this.entityManagerDAO;
	}

	// _________________________________________________________________________________________________________________

	public Role getByName(RoleName name) {
		return roleDAO.getByName(name);
	}
}
```

### DTO conventions

Request and response DTOs are separate classes. DTOs follow the entity’s domain name, for example:

```text
RoleRequestDTO
RoleResponseDTO
```

DTOs should describe the JSON contract rather than expose database implementation details. Use the project’s established Lombok and Jackson conventions.

Every DTO includes a visible expected JSON section with:

- the expected JSON shape
- `Tested: YES` or `Tested: NO`
- `Last Tested: <date>` or `N/A`
- a clearly separated columns section

Request DTO example:

```java
import alpha.domain.role.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class RoleRequestDTO {

	// _________________________________________________________________________________________________________________

	// Expected JSON Input
	// __________________
	//
	//		{
	//			"name": "MEMBER"
	//		}
	//
	// __________________
	// Tested: NO
	// Last Tested: N/A

	// _________________________________________________________________________________________________________________

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("name")
	private RoleName name;
}
```

Response DTOs use the same comment and JSON-documentation structure, but document the response JSON and only expose fields that the API is allowed to return. Database IDs and internal fields must not be exposed unless the relevant specification explicitly requires them.

### Entity conventions

Entities use the established JPA and Lombok annotations. They document the expected database column layout and the current verification state.

Every entity includes:

- the expected database column layout
- `Tested: YES` or `Tested: NO`
- a date for the recorded layout
- a `COLUMNS` section
- a `NESTED FIELDS` section when field constants are useful

Example shape:

```java
package alpha.domain.role.entity;

import alpha.domain.role.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Table(name = "roles")
public class Role {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		PgAdmin
	//		_______
	//		id | name
	//
	// __________________
	// Tested: NO
	// Date: 18/09-2026

	// _________________________________________________________________________________________________________________

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "name", nullable = false, unique = true)
	private RoleName name;

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String NAME = "name";
	}
}
```

### Mapper conventions

Request and response mapping are separate and named after the direction:

```text
<domain>/mapper/request/<Domain>RequestMapper.java
<domain>/mapper/response/<Domain>ResponseMapper.java
```

The mapper should have one clear responsibility. A request mapper converts a request DTO into an entity. A response mapper converts an entity into a response DTO. Mapping should not perform business decisions or database access.

Request mapper example:

```java
package alpha.domain.role.mapper.request;

import alpha.domain.role.dto.request.RoleRequestDTO;
import alpha.domain.role.entity.Role;

public class RoleRequestMapper {

	// _________________________________________________________________________________________________________________

	// Maps RoleRequestDTO to Role entity
	// __________________________________
	//
	//		RoleRequestDTO
	//				↓
	//		RoleRequestMapper
	//				↓
	//		Role
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// _________________________________________________________________________________________________________________

	public static Role toEntity(RoleRequestDTO dto) {

		return Role.builder()
				.name(dto.getName())
				.build();
	}
}
```

### Open areas still requiring a project decision

The following conventions have not yet been specified in the project owner’s examples and must not be invented by an agent:

- Controller method and endpoint naming beyond the existing API convention.
- DAO method naming and query style beyond the existing DAO base classes.
- Exact response DTO field policy for each domain.
- Validation annotation and error-response format.
- Entity relationship and migration conventions.
- Whether mapper methods should remain static for every domain.

If a ticket requires one of these decisions, the agent must stop and ask before implementing it.

## Implementation checklist

Before handing off a new entity/domain implementation, check:

- [ ] The class is in the correct domain package.
- [ ] Request and response DTOs are separate.
- [ ] DTOs document expected JSON and test status.
- [ ] Entities document expected database columns and date/status.
- [ ] Entities expose only intentional API data through response DTOs.
- [ ] Request and response mappers are separate.
- [ ] Services contain an `// Attributes` section and method separators.
- [ ] Frontend files contain a correct pathing header.
- [ ] Hook logic is in `.hooks.js`/`.hooks.ts`, not in JSX/TSX.
- [ ] Component styles are in `.module.css` when needed.
- [ ] Tabs are used for indentation.
- [ ] No unrelated files were changed.