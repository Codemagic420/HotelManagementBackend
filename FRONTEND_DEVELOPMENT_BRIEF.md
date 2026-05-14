# Hotel Management System - Frontend Development Brief

## Backend Status
✅ **COMPLETE & RUNNING** on `http://localhost:8080` or `http://localhost:8081`
- Spring Boot 4.0.5 REST API
- MySQL database with 120+ test records per entity
- Swagger documentation: `http://localhost:8080/swagger-ui.html`
- Authentication: HTTP Basic Auth (admin:admin123, staff:staff123, cleaner1:cleaner123)

---

## Core Entities & API Endpoints

### 1. **Guest Management**
```
GET    /api/guests              - List all guests
GET    /api/guests/{id}         - Get guest by ID
POST   /api/guests              - Create new guest
PUT    /api/guests/{id}         - Update guest
DELETE /api/guests/{id}         - Delete guest

Sample Guest:
{
  "guestId": 1,
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com",
  "phone": "555-1234",
  "creditCardLast4": "4242"
}
```

### 2. **Room Management**
```
GET    /api/rooms               - List all rooms (110 total)
GET    /api/rooms/{id}          - Get room by ID
POST   /api/rooms               - Create new room
PUT    /api/rooms/{id}          - Update room
DELETE /api/rooms/{id}          - Delete room

Sample Room:
{
  "roomId": 1,
  "roomNumber": "101",
  "roomType": {"roomTypeId": 1, "name": "Single", "maxOccupancy": 1},
  "roomStatus": "AVAILABLE|OCCUPIED|MAINTENANCE|CLEANING",
  "cleanStatus": "CLEAN|DIRTY|NEEDS_ATTENTION",
  "occupied": false,
  "type": "Single"
}
```

### 3. **Room Types**
```
GET    /api/room-types          - List all room types (3: Single, Double, Suite)
GET    /api/room-types/{id}     - Get room type by ID
POST   /api/room-types          - Create new room type
PUT    /api/room-types/{id}     - Update room type
DELETE /api/room-types/{id}     - Delete room type
```

### 4. **Reservations**
```
GET    /api/reservations        - List all reservations (120 total)
GET    /api/reservations/{id}   - Get reservation by ID
POST   /api/reservations        - Create new reservation
PUT    /api/reservations/{id}   - Update reservation
DELETE /api/reservations/{id}   - Delete reservation

Sample Reservation:
{
  "reservationId": 1,
  "referenceNo": "RES00001",
  "guest": {...},
  "assignedRoom": {...},
  "checkInDate": "2026-01-03",
  "checkOutDate": "2026-01-04",
  "nights": 1,
  "numGuests": 1,
  "bookedNightlyPrice": 150.00,
  "status": "CONFIRMED|PENDING|CHECKED_IN|CHECKED_OUT|CANCELLED",
  "createdAt": "2025-12-04T10:30:00"
}
```

### 5. **Reservation Guests** (Junction entity)
```
GET    /api/reservation-guests  - List all
POST   /api/reservation-guests  - Add guest to reservation
DELETE /api/reservation-guests/{resId}/{guestId} - Remove guest

Sample:
{
  "id": {"reservationId": 1, "guestId": 1},
  "reservation": {...},
  "guest": {...},
  "isPrimary": true
}
```

### 6. **Bills & Bill Items**
```
GET    /api/bills               - List all bills (120 total)
GET    /api/bills/{id}          - Get bill by ID
POST   /api/bills               - Create new bill
PUT    /api/bills/{id}          - Update bill
DELETE /api/bills/{id}          - Delete bill

GET    /api/bill-items          - List all bill items
GET    /api/bill-items/{id}     - Get bill item by ID
POST   /api/bill-items          - Create new item
PUT    /api/bill-items/{id}     - Update item
DELETE /api/bill-items/{id}     - Delete item

Sample Bill:
{
  "billId": 1,
  "reservation": {...},
  "openedAt": "2025-12-04T10:30:00",
  "closedAt": "2025-12-05T11:00:00",
  "totalAmount": 450.00
}
```

### 7. **Extra Services**
```
GET    /api/extra-services      - List all services (150 total)
GET    /api/extra-services/{id} - Get service by ID
POST   /api/extra-services      - Create new service
PUT    /api/extra-services/{id} - Update service
DELETE /api/extra-services/{id} - Delete service

Sample:
{
  "extraServiceId": 1,
  "name": "Room Service",
  "unitPrice": 25.00,
  "priceUnit": "per item|per session|per day",
  "active": true
}
```

### 8. **Cleaners**
```
GET    /api/cleaners            - List all cleaners (120 total)
GET    /api/cleaners/{id}       - Get cleaner by ID
POST   /api/cleaners            - Create new cleaner
PUT    /api/cleaners/{id}       - Update cleaner
DELETE /api/cleaners/{id}       - Delete cleaner

Sample:
{
  "cleanerId": 1,
  "firstName": "Maria",
  "lastName": "Garcia",
  "phone": "555-0001",
  "active": true
}
```

### 9. **Room Cleaning Tasks & Assignments**
```
GET    /api/room-cleaning-tasks - List all tasks (120 total)
GET    /api/room-cleaning-tasks/{id} - Get task by ID
POST   /api/room-cleaning-tasks - Create new task
PUT    /api/room-cleaning-tasks/{id} - Update task
DELETE /api/room-cleaning-tasks/{id} - Delete task

GET    /api/room-cleaning-assignments - List all assignments
POST   /api/room-cleaning-assignments - Create assignment
DELETE /api/room-cleaning-assignments/{taskId}/{cleanerId} - Remove assignment

Sample Task:
{
  "taskId": 1,
  "room": {...},
  "taskStatus": "PENDING|IN_PROGRESS|COMPLETED|CANCELLED",
  "createdAt": "2025-12-04T10:30:00",
  "note": "Cleaning task 1 for room 101"
}
```

### 10. **Season Rates**
```
GET    /api/season-rates        - List all rates (30+ total)
GET    /api/season-rates/{id}   - Get rate by ID
POST   /api/season-rates        - Create new rate
PUT    /api/season-rates/{id}   - Update rate
DELETE /api/season-rates/{id}   - Delete rate

Sample:
{
  "rateId": 1,
  "roomType": {...},
  "season": "Low|High|Peak|Off-Peak|Shoulder",
  "pricePerNight": 150.00,
  "validFrom": "2026-01-01",
  "validTo": "2026-02-28"
}
```

### 11. **Inventory Items**
```
GET    /api/inventory-items     - List all items (130 total)
GET    /api/inventory-items/{id} - Get item by ID
POST   /api/inventory-items     - Create new item
PUT    /api/inventory-items/{id} - Update item
DELETE /api/inventory-items/{id} - Delete item

Sample:
{
  "inventoryItemId": 1,
  "name": "Bed Sheets",
  "unitPrice": 15.00,
  "active": true
}
```

---

## Authentication

**Credentials (test data):**
```
Admin:    username=admin,    password=admin123    (Role: ADMIN)
Staff:    username=staff,    password=staff123    (Role: STAFF)
Cleaner1: username=cleaner1, password=cleaner123 (Role: CLEANER)
```

**Implementation:**
- HTTP Basic Authentication
- Spring Security with role-based access control
- Protected endpoints: `/api/migrate`, `/api/guests` (specific roles)
- BCrypt password encoding (strength 10)

**Frontend implementation:**
```javascript
// Add to all API requests
const auth = btoa('admin:admin123'); // Base64 encode credentials
fetch('http://localhost:8080/api/guests', {
  headers: {
    'Authorization': `Basic ${auth}`,
    'Content-Type': 'application/json'
  }
});
```

---

## CORS & Backend Configuration

**IMPORTANT:** Backend CORS is configured to accept requests from frontend on any port.

**Frontend should run on:** `http://localhost:3000` (React default)
**Backend on:** `http://localhost:8080`

**Axios setup with CORS:**
```typescript
import axios from 'axios';

const credentials = { username: 'admin', password: 'admin123' };
const encodedAuth = btoa(`${credentials.username}:${credentials.password}`);

export const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Authorization': `Basic ${encodedAuth}`,
    'Content-Type': 'application/json'
  },
  withCredentials: true
});
```

---

## API Response Format

**Success Response (200, 201):**
```json
{
  "guestId": 1,
  "firstName": "John",
  "lastName": "Smith",
  "email": "john@example.com",
  "phone": "555-1234",
  "creditCardLast4": "4242"
}
```

**Error Response (400, 404, 500):**
```json
{
  "timestamp": "2026-05-04T14:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Guest with id 999 not found",
  "path": "/api/guests/999"
}
```

**Common HTTP Status Codes:**
- `200` - OK (GET, PUT success)
- `201` - Created (POST success)
- `204` - No Content (DELETE success)
- `400` - Bad Request (validation error)
- `401` - Unauthorized (bad credentials)
- `403` - Forbidden (no permission for role)
- `404` - Not Found (resource doesn't exist)
- `500` - Server Error

---

## Field Validation Rules

**Guest:**
- `firstName` - Required, max 100 chars
- `lastName` - Required, max 100 chars
- `email` - Required, unique, valid email format
- `phone` - Optional, max 15 chars
- `creditCardLast4` - Optional, 4 digits

**Room:**
- `roomNumber` - Required, unique, max 255 chars
- `roomType` - Required (must exist)
- `roomStatus` - Required, enum: AVAILABLE|OCCUPIED|MAINTENANCE|CLEANING
- `cleanStatus` - Required, enum: CLEAN|DIRTY|NEEDS_ATTENTION
- `occupied` - Optional boolean

**Reservation:**
- `referenceNo` - Required, unique, max 20 chars
- `guest` - Required (must exist)
- `checkInDate` - Required, date format YYYY-MM-DD
- `checkOutDate` - Required, must be after checkInDate
- `nights` - Calculated from dates
- `numGuests` - Required, integer > 0
- `bookedNightlyPrice` - Required, decimal
- `status` - Enum: CONFIRMED|PENDING|CHECKED_IN|CHECKED_OUT|CANCELLED

**Bill:**
- `reservation` - Required (unique, one bill per reservation)
- `openedAt` - DateTime, auto-set on creation
- `closedAt` - Optional DateTime
- `totalAmount` - Auto-calculated from bill items

**Extra Service:**
- `name` - Required, unique, max 100 chars
- `unitPrice` - Required, positive decimal
- `priceUnit` - Required, enum: per item|per session|per day
- `active` - Boolean, default true

**Cleaner:**
- `firstName` - Required, max 50 chars
- `lastName` - Required, max 50 chars
- `phone` - Optional, max 15 chars
- `active` - Boolean, default true

---

## Pagination & Filtering

**List endpoints support:**
- `?page=0` - Page number (0-indexed)
- `?size=20` - Items per page (default 20)
- `?sort=name,asc` - Sort by field

**Example:**
```
GET /api/guests?page=0&size=10&sort=firstName,asc
GET /api/reservations?page=0&size=20&sort=checkInDate,desc
```

**Response wrapper:**
```json
{
  "content": [
    { "guestId": 1, ... },
    { "guestId": 2, ... }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 150,
    "totalPages": 15
  }
}
```

---

## Required Fields Checklist

When creating/updating entities, ensure:

✅ **Guest Create:** firstName, lastName, email
✅ **Room Create:** roomNumber, roomType, roomStatus, cleanStatus  
✅ **Reservation Create:** guest, checkInDate, checkOutDate, numGuests, bookedNightlyPrice, roomType
✅ **Bill Create:** reservation, openedAt
✅ **BillItem Create:** bill, itemType, description, quantity, unitPrice, lineTotal, postedAt
✅ **ExtraService Create:** name, unitPrice, priceUnit
✅ **Cleaner Create:** firstName, lastName
✅ **RoomCleaningTask Create:** room, taskStatus, createdAt
✅ **RoomCleaningAssignment Create:** task, cleaner, assignedAt
✅ **SeasonRate Create:** roomType, season, pricePerNight, validFrom, validTo
✅ **InventoryItem Create:** name, unitPrice

---

## Common Frontend Workflows

### 1. Create Reservation Flow
```
1. Fetch available rooms: GET /api/rooms (filter by roomStatus=AVAILABLE)
2. Fetch guests: GET /api/guests (or create new guest)
3. Fetch season rates: GET /api/season-rates (match room type & dates)
4. Create reservation: POST /api/reservations
5. Create bill: POST /api/bills (with reservation ID)
6. Add bill items: POST /api/bill-items
```

### 2. Check-In Flow
```
1. Find reservation: GET /api/reservations/{id}
2. Update reservation status: PUT /api/reservations/{id} (status=CHECKED_IN)
3. Update room status: PUT /api/rooms/{id} (roomStatus=OCCUPIED)
4. Add cleaner task: POST /api/room-cleaning-tasks
```

### 3. Check-Out Flow
```
1. Get bill: GET /api/bills (by reservation)
2. Add final charges: POST /api/bill-items (if needed)
3. Close bill: PUT /api/bills/{id} (closedAt = now)
4. Update reservation: PUT /api/reservations/{id} (status=CHECKED_OUT)
5. Update room: PUT /api/rooms/{id} (roomStatus=CLEANING)
6. Create cleaning task: POST /api/room-cleaning-tasks
```

### 4. Assign Cleaner to Task
```
1. Create task: POST /api/room-cleaning-tasks
2. Create assignment: POST /api/room-cleaning-assignments
3. Update task status: PUT /api/room-cleaning-tasks/{id} (taskStatus=IN_PROGRESS)
4. Mark complete: PUT /api/room-cleaning-tasks/{id} (taskStatus=COMPLETED)
```

---

## Frontend Features to Build

### Dashboard
- [ ] Welcome screen with quick stats (total guests, rooms, reservations, bills)
- [ ] Navigation menu (Guests, Rooms, Reservations, Bills, Staff, Settings)
- [ ] User profile & logout

### Guest Management
- [ ] List all guests with search/filter
- [ ] Add new guest form
- [ ] Edit guest details
- [ ] View guest reservation history
- [ ] Delete guest (with confirmation)

### Room Management
- [ ] List all rooms with status visualization
- [ ] Room detail view (type, status, cleanliness)
- [ ] Update room status (AVAILABLE → OCCUPIED → MAINTENANCE)
- [ ] Bulk operations (mark as cleaning, etc.)
- [ ] Visual room layout/grid view

### Reservations
- [ ] List all reservations with filtering by status/date
- [ ] Create new reservation (select guest, room, dates, price)
- [ ] Check-in/Check-out workflow
- [ ] View reservation details & guests
- [ ] Cancel reservation
- [ ] Reservation calendar view

### Bills & Invoicing
- [ ] List all bills
- [ ] Create bill from reservation
- [ ] Add bill items (room charge, extra services)
- [ ] Calculate total
- [ ] Mark as paid/closed
- [ ] Generate invoice/PDF (optional)

### Staff Management
- [ ] Manage cleaners
- [ ] Assign cleaning tasks to rooms
- [ ] Track task status
- [ ] View cleaner assignments & history

### Inventory Management
- [ ] List inventory items
- [ ] Track stock levels
- [ ] Add/remove items
- [ ] Price management

### Reporting (Optional)
- [ ] Occupancy rate
- [ ] Revenue by period
- [ ] Top services used
- [ ] Cleaner performance

---

## Frontend Tech Stack Recommendations

**Option 1: React + TypeScript**
```bash
npx create-react-app hotel-frontend --template typescript
npm install axios react-router-dom @mui/material @emotion/react @emotion/styled
```

**Option 2: Vue 3 + TypeScript**
```bash
npm create vue@latest hotel-frontend -- --typescript
npm install axios vue-router
```

**Option 3: Angular**
```bash
ng new hotel-frontend
ng add @angular/material
```

---

## API Testing
**Swagger UI:** http://localhost:8080/swagger-ui.html
- Test all endpoints interactively
- View request/response schemas
- Try authentication

**Sample cURL requests:**
```bash
# List guests
curl -X GET http://localhost:8080/api/guests \
  -H "Authorization: Basic $(echo -n 'admin:admin123' | base64)" \
  -H "Content-Type: application/json"

# Create new guest
curl -X POST http://localhost:8080/api/guests \
  -H "Authorization: Basic $(echo -n 'admin:admin123' | base64)" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Doe",
    "email": "jane@example.com",
    "phone": "555-5678",
    "creditCardLast4": "1234"
  }'

# Create reservation
curl -X POST http://localhost:8080/api/reservations \
  -H "Authorization: Basic $(echo -n 'admin:admin123' | base64)" \
  -H "Content-Type: application/json" \
  -d '{
    "referenceNo": "RES99999",
    "guest": {"guestId": 1},
    "assignedRoom": {"roomId": 1},
    "checkInDate": "2026-01-15",
    "checkOutDate": "2026-01-17",
    "nights": 2,
    "numGuests": 1,
    "bookedNightlyPrice": 150.00,
    "status": "CONFIRMED"
  }'
```

---

## Development Workflow

1. **Start Backend:**
   ```bash
   cd HotelManagementBackend
   export JAVA_HOME="/path/to/java"
   ./mvnw spring-boot:run
   ```

2. **Backend Ready:** http://localhost:8080
   - API: http://localhost:8080/api/
   - Swagger: http://localhost:8080/swagger-ui.html

3. **Create Frontend Project:**
   ```bash
   npx create-react-app hotel-frontend --template typescript
   cd hotel-frontend
   npm install axios react-router-dom
   ```

4. **Setup API Client:**
   ```typescript
   // src/services/api.ts
   import axios from 'axios';
   
   const API_BASE = 'http://localhost:8080/api';
   const AUTH = btoa('admin:admin123');
   
   export const api = axios.create({
     baseURL: API_BASE,
     headers: {
       'Authorization': `Basic ${AUTH}`,
       'Content-Type': 'application/json'
     }
   });
   ```

5. **Build Components:**
   - Pages for each entity (Guests, Rooms, Reservations, etc.)
   - Forms for CRUD operations
   - Tables/Lists with filtering & search
   - Detail views & modals

6. **Handle State:**
   - Use Context API or Redux for global state
   - Cache API responses
   - Handle loading & error states

---

## Important Notes for Frontend Dev

### Data Relationships
- Guest → Reservations (one-to-many)
- Reservation → ReservationGuests (one-to-many, junction table)
- Reservation → Bills (one-to-one)
- Bill → BillItems (one-to-many)
- Room → RoomCleaningTasks (one-to-many)
- RoomCleaningTask → RoomCleaningAssignments (one-to-many)
- RoomCleaningAssignment → Cleaner (many-to-one)

### Status Fields (Enums)
- **Reservation Status:** CONFIRMED, PENDING, CHECKED_IN, CHECKED_OUT, CANCELLED
- **Room Status:** AVAILABLE, OCCUPIED, MAINTENANCE, CLEANING
- **Clean Status:** CLEAN, DIRTY, NEEDS_ATTENTION
- **Task Status:** PENDING, IN_PROGRESS, COMPLETED, CANCELLED
- **Season:** Low, High, Peak, Off-Peak, Shoulder

### Known Limitations
- ⚠️ Neo4j not fully initialized (use REST API for frontend instead)
- ✅ MongoDB optional (use MySQL/JPA API)
- ✅ All core functionality in REST API

---

## Success Criteria

✅ Frontend Dashboard loads and displays test data from backend
✅ Users can view, create, edit, delete guests
✅ Users can manage rooms and reservations
✅ User can create bill from reservation
✅ Authentication works with test credentials
✅ Responsive design for desktop/tablet
✅ Error handling for API failures
✅ Loading states during API calls

---

## Next Steps for Frontend Agent

Use this command to begin frontend development:

```
/claude-code

You are building a hotel management frontend for a fully functional Spring Boot REST API.

BACKEND READY AT: http://localhost:8080
- Swagger Docs: http://localhost:8080/swagger-ui.html
- Auth: admin/admin123 (HTTP Basic)
- Test Data: 120+ records per entity

REQUIRED PAGES:
1. Dashboard - Stats & navigation
2. Guest Management - CRUD + history
3. Room Management - Status visualization
4. Reservations - Booking workflow
5. Bills & Invoicing - Line items & totals
6. Staff - Cleaner assignments

TECH STACK: React + TypeScript + Material-UI recommended

START by:
1. Setting up React project with TypeScript
2. Creating API client with axios
3. Building Dashboard with test data
4. Implementing Guest CRUD
5. Add Room & Reservation pages

Full API documentation and endpoints in: FRONTEND_DEVELOPMENT_BRIEF.md
```

---

**Created:** 2026-05-04
**Backend Version:** 1.0.0 (Composite Keys Fixed)
**API Base:** http://localhost:8080/api
