# 🔴 FAKTISKE PROBLEMER IDENTIFICERET VIA KØRING

## Sammenfatning
Spring Boot applikationen STARTER (port 8080 er åben) men der er 2 kritiske problemer:

1. **Neo4j forbindelsen fejler** (NullPointerException) - non-fatal men blocking for data
2. **MySQL foreign key constraints** - kompatibilitetsproblemer under Hibernate schema update

Disse problemer forhindrer containerisering og korrekt database synkronisering.

---

## 🧪 TEST RESULTAT

**Status**: ✅ App starter og Tomcat køres på port 8080  
**MongoDB**: ✅ Forbinder korrekt  
**MySQL**: ⚠️ Schema opdatering virker men med advarsler  
**Neo4j**: ❌ Forbinder ikke - NullPointerException  

---

## ❌ PROBLEM 1: Neo4j NullPointerException ved forbindelse

**Symptom** (fra app-startup.log):
```
⏳ Neo4j not ready yet, retrying in 2s... (attempt 1/5)
⏳ Neo4j not ready yet, retrying in 2s... (attempt 2/5)
⏳ Neo4j not ready yet, retrying in 2s... (attempt 3/5)
⏳ Neo4j not ready yet, retrying in 2s... (attempt 4/5)
❌ Neo4j initialization failed: Unknown error
   Exception type: NullPointerException

java.lang.NullPointerException
	at java.base/java.util.Objects.requireNonNull(Objects.java:233)
	at org.springframework.data.neo4j.core.Neo4jTemplate.executeReadOnly(Neo4jTemplate.java:203)
	at org.springframework.data.neo4j.repository.support.SimpleNeo4jRepository.count(SimpleNeo4jRepository.java:110)
	at com.kea.hotel.hotelbackend.neo4j.service.Neo4jDataInitializer.waitForNeo4jConnection(Neo4jDataInitializer.java:100)
```

**Årsag**:
- Neo4j HTTP er tilgængelig (testede: `curl http://localhost:7474/db/`)
- Men Bolt forbindelsen (`bolt://localhost:7687`) får NullPointerException når der køres queries
- Problemet opstår i `Neo4jTemplate.executeReadOnly()` - Session eller Transaction er null
- Retries efter 2 sekunder 5 gange, så mislykkes med "Unknown error"

**Filer involveret**:
- `src/main/java/com/kea/hotel/hotelbackend/neo4j/service/Neo4jDataInitializer.java:100` - kalder `cleanerRepository.count()`
- `src/main/resources/application.properties:14-16` - Neo4j konfiguration

**Root Cause**:
Neo4j driver oprettes, men Spring Data Neo4j Template er ikke korrekt initialiseret med aktive session/transaction. Dette er typisk en timing-issue hvor Neo4j containeren ikke er fuldt initialiseret når applikationen prøver at forbinde.

**Løsning**:
```properties
spring.neo4j.uri=bolt://${NEO4J_HOST:localhost}:${NEO4J_PORT:7687}
spring.neo4j.authentication.username=${NEO4J_USERNAME:neo4j}
spring.neo4j.authentication.password=${NEO4J_PASSWORD:yourpassword}
```

Plus: Øg timeout i `Neo4jDataInitializer.waitForNeo4jConnection()` fra 5 sekunder til 30+ sekunder, og tilføj bedre error handling.

---

## ❌ PROBLEM 2: MySQL Foreign Key Incompatibility Warnings

**Symptom** (fra app-startup.log):
```
WARN GenerationTarget encountered exception accepting command : Error executing DDL 
"alter table reservation modify column booked_rate_id bigint not null" 
via JDBC [Referencing column 'booked_rate_id' and referenced column 'rate_id' 
in foreign key constraint 'reservation_ibfk_3' are incompatible.]

Caused by: java.sql.SQLException: Referencing column 'booked_rate_id' and 
referenced column 'rate_id' in foreign key constraint 'reservation_ibfk_3' are incompatible.
```

**Årsag**:
Hibernate prøver at ændre kolonner med `ddl-auto=update`, men MySQL tillader ikke at ændre kolonner der deltager i foreign key constraints uden først at slette constrainten.

**Filer involveret**:
- `src/main/resources/application.properties:6` - `spring.jpa.hibernate.ddl-auto=update`
- MySQL tabel: `reservation` med foreign key til `season_rate` (`booked_rate_id` -> `rate_id`)

**Status**:
⚠️ App starter alligevel - MySQL ignorerer fejlen og fortsætter. Men constrainten bliver ikke oprettet korrekt.

**Løsning**:
```properties
# Ændre fra:
spring.jpa.hibernate.ddl-auto=update

# Til:
spring.jpa.hibernate.ddl-auto=validate
```

Eller brug manual migration med Flyway/Liquibase i stedet.

---

## ❌ PROBLEM 3: Applikationen kan ikke containeriseres

**Symptom**:
- Der er `docker-compose.yml` for databasene
- Men INGEN `Dockerfile` for Spring Boot applikationen selv
- Applikationen kan kun køres lokalt med Maven (`mvn spring-boot:run`)
- Kan ikke bygges til Docker image

**Filer manglende**:
- `Dockerfile` - for at containerisere Spring Boot app
- `.dockerignore` - optional men anbefalet
- `docker-compose-app.yml` eller integration i `docker-compose.yml` for app service

**Løsning**:
Opret `Dockerfile` i project root:

```dockerfile
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw && ./mvnw dependency:resolve

COPY src src

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/hotelbackend-0.0.1-SNAPSHOT.jar"]
```

---

## ⚠️ PROBLEM 4: Docker Network Integration

**Status**: Lokalt virker MongoDB forbindelsen godt (`localhost:27017`)

**Problem ved containerisering**:
Når applikationen containeriseres:
- `localhost:27017` i containeren henviser til applikationens egen container
- Skal bruge `mongodb` (service navn fra docker-compose)
- MySQL skal være `mysql` i stedet for `localhost`
- Neo4j skal være `neo4j` i stedet for `localhost`

**Løsning**:
Update `docker-compose.yml` med app service:

```yaml
  app:
    build: .
    container_name: hotel_app_container
    ports:
      - "8080:8080"
    environment:
      # MySQL (intern Docker navn)
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/hotel_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      
      # MongoDB (intern Docker navn)
      SPRING_MONGODB_URI: mongodb://admin:admin123@mongodb:27017/hotel_db?authSource=admin
      MONGODB_HOST: mongodb
      
      # Neo4j (intern Docker navn)
      NEO4J_HOST: neo4j
      NEO4J_PORT: 7687
    networks:
      - hotel_network
    depends_on:
      mysql:
        condition: service_healthy
      mongodb:
        condition: service_healthy
      neo4j:
        condition: service_healthy
```

Og update `application.properties` for at acceptere environment variables for alle hosts:

```properties
# MySQL - default localhost, men kan overskrives af ENV
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3307}/${MYSQL_DATABASE:hotel_db}

# MongoDB - allerede OK
spring.mongodb.uri=mongodb://${MONGODB_USER:admin}:${MONGODB_PASSWORD:admin123}@${MONGODB_HOST:localhost}:${MONGODB_PORT:27017}/${MONGODB_DATABASE:hotel_db}?authSource=admin

# Neo4j - skal opdateres
spring.neo4j.uri=bolt://${NEO4J_HOST:localhost}:${NEO4J_PORT:7687}
```

---

## 📋 PRIORITY FIX CHECKLIST

### HIGH PRIORITY (blocker for container deployment):

- [ ] **Steg 1**: Opret `Dockerfile` i project root
- [ ] **Steg 2**: Opdater `docker-compose.yml` - tilføj app service med environment variables
- [ ] **Steg 3**: Fix `application.properties` - Neo4j skal bruge `${NEO4J_HOST}` environment variable
- [ ] **Steg 4**: Øg Neo4j timeout i `Neo4jDataInitializer.java` fra 5 retries til 15+ eller bedre error handling

### MEDIUM PRIORITY (database issues):

- [ ] **Steg 5**: Fix MySQL `ddl-auto` fra `update` til `validate` - eller brug Flyway migrations
- [ ] **Steg 6**: Tester Neo4j forbindelsen med kommando: `POST http://localhost:8080/api/migrate`

### OPTIONAL:

- [ ] Opret `.env` fil for lokal udvikling (men ikke nødvendig hvis du bruger docker-compose med environment variabler)
- [ ] Opret `.dockerignore` for at reducere build context

---

## 🧪 TEST PLAN

### BEFORE FIX:
```bash
# App starter men Neo4j forbindelsen fejler
mvn spring-boot:run  
# Result: App på http://localhost:8080, Neo4j fejler efter 10 sekunder

# Check Neo4j status
curl -s http://localhost:7474/db/  
# Result: {"errors":[{"code":"Neo.ClientError.Security.Unauthorized"...}]}
```

### AFTER FIX:
```bash
# Start alle services (after fixes)
docker-compose up --build

# Check app health
curl http://localhost:8080/swagger-ui.html
# Expected: ✅ Swagger UI loads

# Trigger Neo4j initialization
curl -X POST http://localhost:8080/api/migrate
# Expected: ✅ Returns success message

# Check Neo4j status endpoint
curl -s http://localhost:8080/api/neo4j/diagnostics/status
# Expected: ✅ Neo4j connected and initialized

# View logs
docker-compose logs app
# Expected: No errors, Neo4j data initialized

# Stop
docker-compose down
```

---

## 📚 REFERENCES

- **Current database setup**: `docker-compose.yml`
- **Spring config**: `src/main/resources/application.properties`
- **Main app**: `src/main/java/com/kea/hotel/hotelbackend/HotelManagementBackendApplication.java`
- **SQL scripts**: `sql/` folder (auto-executed ved MySQL startup)

