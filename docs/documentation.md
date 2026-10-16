# Pokedex coding practice

## Project overview

I built this as a coding practice: a RESTful API in Java and Spring Boot, using Clean Architecture and TDD, backed by a relational database, plus a React frontend that consumes it.

The Pokemon user stories below are the scope I worked through. The service talks to PokeAPI for data retrieval, then replicates entries locally so they can be extended and edited.

PokeAPI reference: [https://pokeapi.co/docs/v2](https://pokeapi.co/docs/v2) 

## Requirements

### Functional requirements

- **PokeAPI Integration** 
  - Construct a RESTful API using Spring Boot that communicates with the external PokeAPI.
- **User Story 01: Pokemon Enumeration** 
  - The system should allow users to browse Pokemon via paginated results, displaying each entry's sprite, category, mass, and a collection of their skills. ■ Nice to have: Implement caching for service responses.
- **User Story 02: Detailed View** 
  - Users must be able to access comprehensive data for a chosen Pokemon, specifically viewing its image, core statistics, narrative description, and evolutionary lineage.
- **User Story 03: Data Synchronization** 
  - Develop a mechanism to persist Pokemon data into a local relational store. 
  - This replication layer is intended to facilitate the addition of proprietary fields. 
    - Use cases include localized nomenclature, geographical metadata, or internal classification tags.
- **User Story 04: Local Data Modification** 
  - Enable update operations for any Pokemon currently stored within the local database. 
  - Ensure robust validation: provide 404 responses for missing records, 400 status codes for malformed payloads, and incorporate further defensive logic as required.



### Technical requirements

#### Mandatory

- Host the code in a public Git repository 
- Include tests 
- Proper error handling 
- Nice to have: Implement a caching layer for PokeAPI responses 
- Front-end that consumes the API (React with TypeScript) 
- **Optional** 
  - Any additional functionality is welcome
- **Database** 
  - Establish a relational database or appropriate data storage solution containing a primary entity and a secondary collection for user management. 
    - Records must include a unique primary key and a minimum of two descriptive attributes.
- **API** 
  - Construct an Java Web API facilitating comprehensive CRUD operations for the defined dataset. 
    - Ensure all endpoints utilize standard HTTP verbs, required parameters, and consistent return structures.
  - Implement an auxiliary API for user registration, authentication, and the management of protected versus public routes.
- **Data Layer** 
  - Design a specialized data access layer to manage interactions with the persistence store, providing the foundational logic for the API controllers.
- **Core Business Logic** 
  - Develop a dedicated business logic layer to encapsulate all domain rules and data validation procedures. 
  - This layer should maintain architectural independence from both the API and the data access components.
- **Testing and Validation** 
  - Provide thorough unit test coverage for every core component within the application suite.



### Frontend

The backend is the main focus. I still wanted a full-stack practice, so the API is consumed by a React + TypeScript app (Tailwind CSS, TanStack Query):

- A responsive, usable interface.
- The same create, read, update, and delete flows as the user stories.
- Clear component structure and straightforward state management.

The README covers how to run it, and the app starts with seeded Pokemon plus a demo account.

### Delivery

- README with setup and technical notes.
- Seeded demonstration data.
- A Dockerfile so the app can run in a container.

### Generative AI

As part of the same practice I wrote up how I would prompt a coding agent for a small task-management API, and how I actually used one on this Pokedex. The write-up is in [`genai.md`](genai.md).

The task API I used as the prompt example supports:

- Create, read, update, and delete tasks
- Each task has a `title`, `description`, `status`, and `due_date`
- Tasks belong to a user

The notes cover the prompt, a sample of the output, and how I checked, corrected, and handled edge cases, authentication, and validation.

