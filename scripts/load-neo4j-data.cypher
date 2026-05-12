// Neo4j Load Script - Hotel Management System
// Create nodes and relationships for graph database

// Create Guest nodes
CREATE (:Guest {id: 1, firstName: "John", lastName: "Doe", email: "john.doe@example.com", phone: "555-1001"}),
       (:Guest {id: 2, firstName: "Jane", lastName: "Smith", email: "jane.smith@example.com", phone: "555-1002"}),
       (:Guest {id: 3, firstName: "Bob", lastName: "Johnson", email: "bob.johnson@example.com", phone: "555-1003"});

// Create Room nodes
CREATE (:Room {id: 1, roomNumber: "101", roomType: "Single", status: "AVAILABLE"}),
       (:Room {id: 11, roomNumber: "201", roomType: "Double", status: "AVAILABLE"}),
       (:Room {id: 21, roomNumber: "301", roomType: "Twin", status: "OCCUPIED"});

// Create Reservation nodes
CREATE (:Reservation {id: 1, referenceNo: "RES001", checkInDate: date("2026-05-05"), checkOutDate: date("2026-05-07"), nights: 2, status: "CONFIRMED"}),
       (:Reservation {id: 2, referenceNo: "RES002", checkInDate: date("2026-05-06"), checkOutDate: date("2026-05-09"), nights: 3, status: "CONFIRMED"}),
       (:Reservation {id: 3, referenceNo: "RES003", checkInDate: date("2026-05-07"), checkOutDate: date("2026-05-10"), nights: 3, status: "CONFIRMED"});

// Create relationships: Guest -> Reservation
MATCH (g:Guest {id: 1}), (r:Reservation {id: 1})
CREATE (g)-[:MADE_RESERVATION]->(r);

MATCH (g:Guest {id: 2}), (r:Reservation {id: 2})
CREATE (g)-[:MADE_RESERVATION]->(r);

MATCH (g:Guest {id: 3}), (r:Reservation {id: 3})
CREATE (g)-[:MADE_RESERVATION]->(r);

// Create relationships: Reservation -> Room
MATCH (r:Reservation {id: 1}), (ro:Room {id: 1})
CREATE (r)-[:BOOKED_ROOM]->(ro);

MATCH (r:Reservation {id: 2}), (ro:Room {id: 11})
CREATE (r)-[:BOOKED_ROOM]->(ro);

MATCH (r:Reservation {id: 3}), (ro:Room {id: 21})
CREATE (r)-[:BOOKED_ROOM]->(ro);

// Create Bill nodes
CREATE (:Bill {id: 1, reservationId: 1, totalAmount: 160.00, isPaid: false}),
       (:Bill {id: 2, reservationId: 2, totalAmount: 360.00, isPaid: false}),
       (:Bill {id: 3, reservationId: 3, totalAmount: 330.00, isPaid: false});

// Create relationships: Reservation -> Bill
MATCH (r:Reservation {id: 1}), (b:Bill {id: 1})
CREATE (r)-[:HAS_BILL]->(b);

MATCH (r:Reservation {id: 2}), (b:Bill {id: 2})
CREATE (r)-[:HAS_BILL]->(b);

MATCH (r:Reservation {id: 3}), (b:Bill {id: 3})
CREATE (r)-[:HAS_BILL]->(b);

// Verify data load
MATCH (g:Guest)-[r:MADE_RESERVATION]->(res:Reservation)-[b:BOOKED_ROOM]->(room:Room)
RETURN COUNT(g) as GuestCount, COUNT(res) as ReservationCount, COUNT(room) as RoomCount;
