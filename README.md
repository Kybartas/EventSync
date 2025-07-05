![frontendDemonstration.gif](frontendDemonstration.gif)

# Quick start

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (Latest version)
- [Git](https://git-scm.com/downloads)

### Setup & run

1. **Clone the repository**
   ```bash
   git clone https://github.com/Kybartas/EventSync
   ```

2. **Navigate to the project directory**
    ```
    cd EventSync
    ```

3. **Start the application**
   ```bash
   docker compose up --build
   ```

### URLS ( when docker containers up and running )

- API: `http://localhost:8080`
- Frontend: `http://localhost:3000`
- Documentation: `http://localhost:8080/swagger-ui.html`
- H2 database: `http://localhost:8080/h2-console`

#### H2 console setup: 
JDBC URL: `jdbc:h2:mem:eventsync`

Username: `username`

Password: `password`

`Generic H2 (Embedded)` option.

## Deployed API

URL: `https://eventsync.up.railway.app` (no front-end deployed)

To communicate with public api check the [postman collection](https://www.postman.com/kristijonaskybartas/workspace/eventsync/collection/44482661-945aab67-bed8-4cd2-81d3-180ad397a68b?action=share&creator=44482661)