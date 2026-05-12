// MongoDB Load Script - Hotel Management System
// Connect to MongoDB and load test data

use hotel_db;

// Create collections with test data
db.createCollection("guests");
db.guests.insertMany([
  { _id: ObjectId(), firstName: "John", lastName: "Doe", email: "john.doe@example.com", phone: "555-1001", creditCardLast4: "1234" },
  { _id: ObjectId(), firstName: "Jane", lastName: "Smith", email: "jane.smith@example.com", phone: "555-1002", creditCardLast4: "5678" },
  { _id: ObjectId(), firstName: "Bob", lastName: "Johnson", email: "bob.johnson@example.com", phone: "555-1003", creditCardLast4: "9012" }
  // ... (150+ guests from relational database)
]);

db.createCollection("rooms");
db.rooms.insertMany([
  { _id: ObjectId(), roomNumber: "101", roomType: "Single", status: "AVAILABLE", cleanStatus: "CLEAN", occupied: false },
  { _id: ObjectId(), roomNumber: "201", roomType: "Double", status: "AVAILABLE", cleanStatus: "CLEAN", occupied: false },
  { _id: ObjectId(), roomNumber: "301", roomType: "Twin", status: "OCCUPIED", cleanStatus: "CLEAN", occupied: true }
  // ... (50+ rooms)
]);

db.createCollection("reservations");
db.reservations.insertMany([
  {
    _id: ObjectId(),
    referenceNo: "RES001",
    checkInDate: new Date("2026-05-05"),
    checkOutDate: new Date("2026-05-07"),
    nights: 2,
    numGuests: 1,
    roomTypeId: ObjectId(),
    bookedNightlyPrice: 80.00,
    status: "CONFIRMED",
    guestId: ObjectId(),
    createdAt: new Date()
  }
  // ... (120+ reservations)
]);

db.createCollection("bills");
db.bills.insertMany([
  {
    _id: ObjectId(),
    reservationId: ObjectId(),
    openedAt: new Date("2026-05-05 10:30:00"),
    closedAt: new Date("2026-05-07 11:00:00"),
    totalAmount: 160.00,
    isPaid: false,
    billItems: [
      { itemType: "ROOM_CHARGE", description: "Single Room - 2 nights", quantity: 2, unitPrice: 80.00, lineTotal: 160.00 }
    ]
  }
  // ... (120+ bills with embedded items)
]);

print("✅ MongoDB test data loaded successfully");
print("Collections created: guests, rooms, reservations, bills, etc.");
