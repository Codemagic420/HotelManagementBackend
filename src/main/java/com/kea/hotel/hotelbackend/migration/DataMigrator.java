package com.kea.hotel.hotelbackend.migration;

import com.kea.hotel.hotelbackend.model.*;
import com.kea.hotel.hotelbackend.repository.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/migrate")
public class DataMigrator {

    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final CleanerRepository cleanerRepository;
    private final ExtraServiceRepository extraServiceRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final SeasonRateRepository seasonRateRepository;
    private final RoomCleaningTaskRepository roomCleaningTaskRepository;
    private final RoomCleaningAssignmentRepository roomCleaningAssignmentRepository;
    private final ReservationGuestRepository reservationGuestRepository;
    private final MongoTemplate mongoTemplate;
    private final Neo4jClient neo4jClient;

    public DataMigrator(GuestRepository guestRepository,
                        RoomRepository roomRepository,
                        ReservationRepository reservationRepository,
                        BillRepository billRepository,
                        BillItemRepository billItemRepository,
                        CleanerRepository cleanerRepository,
                        ExtraServiceRepository extraServiceRepository,
                        InventoryItemRepository inventoryItemRepository,
                        RoomTypeRepository roomTypeRepository,
                        SeasonRateRepository seasonRateRepository,
                        RoomCleaningTaskRepository roomCleaningTaskRepository,
                        RoomCleaningAssignmentRepository roomCleaningAssignmentRepository,
                        ReservationGuestRepository reservationGuestRepository,
                        MongoTemplate mongoTemplate,
                        Neo4jClient neo4jClient) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
        this.cleanerRepository = cleanerRepository;
        this.extraServiceRepository = extraServiceRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.seasonRateRepository = seasonRateRepository;
        this.roomCleaningTaskRepository = roomCleaningTaskRepository;
        this.roomCleaningAssignmentRepository = roomCleaningAssignmentRepository;
        this.reservationGuestRepository = reservationGuestRepository;
        this.mongoTemplate = mongoTemplate;
        this.neo4jClient = neo4jClient;
    }

    @PostMapping
    public String migrate() {
        if (isMigrationAlreadyDone()) {
            return "Migration has already been completed. One-time migration cannot be run again.";
        }

        migrateToMongo();
        migrateToNeo4j();
        return "Migration completed successfully.";
    }

    private boolean isMigrationAlreadyDone() {
        try {
            long mongoReservationCount = mongoTemplate.count(new Query(), "reservations");
            System.out.println("MongoDB reservations count: " + mongoReservationCount);
            return mongoReservationCount > 0;
        } catch (Exception e) {
            System.err.println("Migration check error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void migrateToMongo() {
        try {
            System.out.println("\n=== Starting MongoDB Migration for All 13 Entities ===\n");

            // 1. RoomTypes
            List<RoomType> roomTypes = roomTypeRepository.findAll();
            System.out.println("Migrating " + roomTypes.size() + " room types");
            for (RoomType rt : roomTypes) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("roomTypeId", rt.getRoomTypeId());
                doc.put("name", rt.getName());
                doc.put("maxOccupancy", rt.getMaxOccupancy());
                mongoTemplate.save(doc, "room_types");
            }

            // 2. Rooms
            List<Room> rooms = roomRepository.findAll();
            System.out.println("Migrating " + rooms.size() + " rooms");
            for (Room room : rooms) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("roomId", room.getRoomId());
                doc.put("roomNumber", room.getRoomNumber());
                doc.put("roomTypeId", room.getRoomType() != null ? room.getRoomType().getRoomTypeId() : null);
                doc.put("roomStatus", room.getRoomStatus());
                doc.put("cleanStatus", room.getCleanStatus());
                doc.put("occupied", room.getOccupied());
                doc.put("type", room.getType());
                mongoTemplate.save(doc, "rooms");
            }

            // 3. Cleaners
            List<Cleaner> cleaners = cleanerRepository.findAll();
            System.out.println("Migrating " + cleaners.size() + " cleaners");
            for (Cleaner c : cleaners) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("cleanerId", c.getCleanerId());
                doc.put("firstName", c.getFirstName());
                doc.put("lastName", c.getLastName());
                doc.put("phone", c.getPhone());
                doc.put("active", c.getActive());
                mongoTemplate.save(doc, "cleaners");
            }

            // 4. ExtraServices
            List<ExtraService> services = extraServiceRepository.findAll();
            System.out.println("Migrating " + services.size() + " extra services");
            for (ExtraService s : services) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("extraServiceId", s.getExtraServiceId());
                doc.put("name", s.getName());
                doc.put("unitPrice", s.getUnitPrice() != null ? s.getUnitPrice().toString() : null);
                doc.put("priceUnit", s.getPriceUnit());
                doc.put("active", s.getActive());
                mongoTemplate.save(doc, "extra_services");
            }

            // 5. InventoryItems
            List<InventoryItem> items = inventoryItemRepository.findAll();
            System.out.println("Migrating " + items.size() + " inventory items");
            for (InventoryItem item : items) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("inventoryItemId", item.getInventoryItemId());
                doc.put("name", item.getName());
                doc.put("unitPrice", item.getUnitPrice() != null ? item.getUnitPrice().toString() : null);
                doc.put("active", item.getActive());
                mongoTemplate.save(doc, "inventory_items");
            }

            // 6. SeasonRates
            List<SeasonRate> rates = seasonRateRepository.findAll();
            System.out.println("Migrating " + rates.size() + " season rates");
            for (SeasonRate rate : rates) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("rateId", rate.getRateId());
                doc.put("roomTypeId", rate.getRoomType() != null ? rate.getRoomType().getRoomTypeId() : null);
                doc.put("season", rate.getSeason());
                doc.put("pricePerNight", rate.getPricePerNight() != null ? rate.getPricePerNight().toString() : null);
                doc.put("validFrom", rate.getValidFrom() != null ? rate.getValidFrom().toString() : null);
                doc.put("validTo", rate.getValidTo() != null ? rate.getValidTo().toString() : null);
                mongoTemplate.save(doc, "season_rates");
            }

            // 7. RoomCleaningTasks
            List<RoomCleaningTask> tasks = roomCleaningTaskRepository.findAll();
            System.out.println("Migrating " + tasks.size() + " room cleaning tasks");
            for (RoomCleaningTask task : tasks) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("taskId", task.getTaskId());
                doc.put("roomId", task.getRoom() != null ? task.getRoom().getRoomId() : null);
                doc.put("taskStatus", task.getTaskStatus());
                doc.put("note", task.getNote());
                doc.put("createdAt", task.getCreatedAt() != null ? task.getCreatedAt().toString() : null);
                mongoTemplate.save(doc, "room_cleaning_tasks");
            }

            // 8. RoomCleaningAssignments
            List<RoomCleaningAssignment> assignments = roomCleaningAssignmentRepository.findAll();
            System.out.println("Migrating " + assignments.size() + " room cleaning assignments");
            for (RoomCleaningAssignment assign : assignments) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("taskId", assign.getId() != null ? assign.getId().getTaskId() : null);
                doc.put("cleanerId", assign.getId() != null ? assign.getId().getCleanerId() : null);
                doc.put("assignedAt", assign.getAssignedAt() != null ? assign.getAssignedAt().toString() : null);
                mongoTemplate.save(doc, "room_cleaning_assignments");
            }

            // 9. Guests
            List<Guest> guests = guestRepository.findAll();
            System.out.println("Migrating " + guests.size() + " guests");
            for (Guest g : guests) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("guestId", g.getGuestId());
                doc.put("firstName", g.getFirstName());
                doc.put("lastName", g.getLastName());
                doc.put("email", g.getEmail());
                doc.put("phone", g.getPhone());
                doc.put("creditCardLast4", g.getCreditCardLast4());
                mongoTemplate.save(doc, "guests");
            }

            // 10. Reservations
            List<Reservation> reservations = reservationRepository.findAll();
            System.out.println("Migrating " + reservations.size() + " reservations");
            for (Reservation res : reservations) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("reservationId", res.getReservationId());
                doc.put("referenceNo", res.getReferenceNo());
                doc.put("status", res.getStatus());
                doc.put("checkInDate", res.getCheckInDate() != null ? res.getCheckInDate().toString() : null);
                doc.put("checkOutDate", res.getCheckOutDate() != null ? res.getCheckOutDate().toString() : null);
                doc.put("nights", res.getNights());
                doc.put("numGuests", res.getNumGuests());
                doc.put("guestId", res.getGuest() != null ? res.getGuest().getGuestId() : null);
                doc.put("roomId", res.getRoom() != null ? res.getRoom().getRoomId() : null);
                doc.put("roomTypeId", res.getRoomType() != null ? res.getRoomType().getRoomTypeId() : null);
                doc.put("bookedNightlyPrice", res.getBookedNightlyPrice() != null ? res.getBookedNightlyPrice().toString() : null);
                mongoTemplate.save(doc, "reservations");
            }

            // 11. ReservationGuests
            List<ReservationGuest> resGuests = reservationGuestRepository.findAll();
            System.out.println("Migrating " + resGuests.size() + " reservation guests");
            for (ReservationGuest rg : resGuests) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("reservationId", rg.getId() != null ? rg.getId().getReservationId() : null);
                doc.put("guestId", rg.getId() != null ? rg.getId().getGuestId() : null);
                doc.put("isPrimary", rg.getIsPrimary());
                mongoTemplate.save(doc, "reservation_guests");
            }

            // 12. Bills
            List<Bill> bills = billRepository.findAll();
            System.out.println("Migrating " + bills.size() + " bills");
            for (Bill bill : bills) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("billId", bill.getBillId());
                doc.put("reservationId", bill.getReservation() != null ? bill.getReservation().getReservationId() : null);
                doc.put("totalAmount", bill.getTotalAmount() != null ? bill.getTotalAmount().toString() : null);
                doc.put("openedAt", bill.getOpenedAt() != null ? bill.getOpenedAt().toString() : null);
                doc.put("closedAt", bill.getClosedAt() != null ? bill.getClosedAt().toString() : null);
                mongoTemplate.save(doc, "bills");
            }

            // 13. BillItems
            List<BillItem> billItems = billItemRepository.findAll();
            System.out.println("Migrating " + billItems.size() + " bill items");
            for (BillItem item : billItems) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("billItemId", item.getBillItemId());
                doc.put("billId", item.getBill() != null ? item.getBill().getBillId() : null);
                doc.put("itemType", item.getItemType());
                doc.put("description", item.getDescription());
                doc.put("quantity", item.getQuantity());
                doc.put("unitPrice", item.getUnitPrice() != null ? item.getUnitPrice().toString() : null);
                doc.put("lineTotal", item.getLineTotal() != null ? item.getLineTotal().toString() : null);
                doc.put("postedAt", item.getPostedAt() != null ? item.getPostedAt().toString() : null);
                mongoTemplate.save(doc, "bill_items");
            }

            System.out.println("\n=== MongoDB Migration completed successfully for all 13 entities ===\n");
        } catch (Exception e) {
            System.err.println("Error during MongoDB migration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void migrateToNeo4j() {
        try {
            System.out.println("\n=== Starting Neo4j Migration for All 13 Entities ===\n");

            // 1. RoomTypes
            List<RoomType> roomTypes = roomTypeRepository.findAll();
            System.out.println("Creating " + roomTypes.size() + " RoomType nodes");
            for (RoomType rt : roomTypes) {
                neo4jClient.query(
                        "MERGE (rt:RoomType {roomTypeId: $id}) SET rt.name = $name, rt.maxOccupancy = $maxOcc"
                ).bind(rt.getRoomTypeId()).to("id")
                 .bind(rt.getName()).to("name")
                 .bind(rt.getMaxOccupancy()).to("maxOcc")
                 .run();
            }

            // 2. Rooms
            List<Room> rooms = roomRepository.findAll();
            System.out.println("Creating " + rooms.size() + " Room nodes");
            for (Room r : rooms) {
                neo4jClient.query(
                        "MERGE (r:Room {roomId: $id}) SET r.roomNumber = $roomNumber, r.roomStatus = $roomStatus, r.cleanStatus = $cleanStatus, r.occupied = $occupied"
                ).bind(r.getRoomId()).to("id")
                 .bind(r.getRoomNumber()).to("roomNumber")
                 .bind(r.getRoomStatus()).to("roomStatus")
                 .bind(r.getCleanStatus()).to("cleanStatus")
                 .bind(r.getOccupied()).to("occupied")
                 .run();
            }

            // 3. Room-[:IS_TYPE]->RoomType relationships
            System.out.println("Creating Room-[:IS_TYPE]->RoomType relationships");
            for (Room r : rooms) {
                if (r.getRoomType() != null) {
                    neo4jClient.query(
                            "MATCH (r:Room {roomId: $roomId}) MATCH (rt:RoomType {roomTypeId: $roomTypeId}) MERGE (r)-[:IS_TYPE]->(rt)"
                    ).bind(r.getRoomId()).to("roomId")
                     .bind(r.getRoomType().getRoomTypeId()).to("roomTypeId")
                     .run();
                }
            }

            // 4. Cleaners
            List<Cleaner> cleaners = cleanerRepository.findAll();
            System.out.println("Creating " + cleaners.size() + " Cleaner nodes");
            for (Cleaner c : cleaners) {
                neo4jClient.query(
                        "MERGE (c:Cleaner {cleanerId: $id}) SET c.firstName = $firstName, c.lastName = $lastName, c.phone = $phone, c.active = $active"
                ).bind(c.getCleanerId()).to("id")
                 .bind(c.getFirstName()).to("firstName")
                 .bind(c.getLastName()).to("lastName")
                 .bind(c.getPhone()).to("phone")
                 .bind(c.getActive()).to("active")
                 .run();
            }

            // 5. ExtraServices
            List<ExtraService> services = extraServiceRepository.findAll();
            System.out.println("Creating " + services.size() + " ExtraService nodes");
            for (ExtraService s : services) {
                neo4jClient.query(
                        "MERGE (es:ExtraService {extraServiceId: $id}) SET es.name = $name, es.unitPrice = $price, es.priceUnit = $priceUnit, es.active = $active"
                ).bind(s.getExtraServiceId()).to("id")
                 .bind(s.getName()).to("name")
                 .bind(s.getUnitPrice() != null ? s.getUnitPrice().toString() : null).to("price")
                 .bind(s.getPriceUnit()).to("priceUnit")
                 .bind(s.getActive()).to("active")
                 .run();
            }

            // 6. InventoryItems
            List<InventoryItem> items = inventoryItemRepository.findAll();
            System.out.println("Creating " + items.size() + " InventoryItem nodes");
            for (InventoryItem item : items) {
                neo4jClient.query(
                        "MERGE (ii:InventoryItem {inventoryItemId: $id}) SET ii.name = $name, ii.unitPrice = $price, ii.active = $active"
                ).bind(item.getInventoryItemId()).to("id")
                 .bind(item.getName()).to("name")
                 .bind(item.getUnitPrice() != null ? item.getUnitPrice().toString() : null).to("price")
                 .bind(item.getActive()).to("active")
                 .run();
            }

            // 7. SeasonRates
            List<SeasonRate> rates = seasonRateRepository.findAll();
            System.out.println("Creating " + rates.size() + " SeasonRate nodes");
            for (SeasonRate rate : rates) {
                neo4jClient.query(
                        "MERGE (sr:SeasonRate {rateId: $id}) SET sr.season = $season, sr.pricePerNight = $price, sr.validFrom = $from, sr.validTo = $to"
                ).bind(rate.getRateId()).to("id")
                 .bind(rate.getSeason()).to("season")
                 .bind(rate.getPricePerNight() != null ? rate.getPricePerNight().toString() : null).to("price")
                 .bind(rate.getValidFrom() != null ? rate.getValidFrom().toString() : null).to("from")
                 .bind(rate.getValidTo() != null ? rate.getValidTo().toString() : null).to("to")
                 .run();
            }

            // 8. SeasonRate-[:FOR_ROOM_TYPE]->RoomType relationships
            System.out.println("Creating SeasonRate-[:FOR_ROOM_TYPE]->RoomType relationships");
            for (SeasonRate rate : rates) {
                if (rate.getRoomType() != null) {
                    neo4jClient.query(
                            "MATCH (sr:SeasonRate {rateId: $rateId}) MATCH (rt:RoomType {roomTypeId: $roomTypeId}) MERGE (sr)-[:FOR_ROOM_TYPE]->(rt)"
                    ).bind(rate.getRateId()).to("rateId")
                     .bind(rate.getRoomType().getRoomTypeId()).to("roomTypeId")
                     .run();
                }
            }

            // 9. RoomCleaningTasks
            List<RoomCleaningTask> tasks = roomCleaningTaskRepository.findAll();
            System.out.println("Creating " + tasks.size() + " RoomCleaningTask nodes");
            for (RoomCleaningTask task : tasks) {
                neo4jClient.query(
                        "MERGE (rct:RoomCleaningTask {taskId: $id}) SET rct.taskStatus = $status, rct.note = $note, rct.createdAt = $createdAt"
                ).bind(task.getTaskId()).to("id")
                 .bind(task.getTaskStatus()).to("status")
                 .bind(task.getNote()).to("note")
                 .bind(task.getCreatedAt() != null ? task.getCreatedAt().toString() : null).to("createdAt")
                 .run();
            }

            // 10. RoomCleaningTask-[:FOR_ROOM]->Room relationships
            System.out.println("Creating RoomCleaningTask-[:FOR_ROOM]->Room relationships");
            for (RoomCleaningTask task : tasks) {
                if (task.getRoom() != null) {
                    neo4jClient.query(
                            "MATCH (rct:RoomCleaningTask {taskId: $taskId}) MATCH (r:Room {roomId: $roomId}) MERGE (rct)-[:FOR_ROOM]->(r)"
                    ).bind(task.getTaskId()).to("taskId")
                     .bind(task.getRoom().getRoomId()).to("roomId")
                     .run();
                }
            }

            // 11. RoomCleaningAssignments
            List<RoomCleaningAssignment> assignments = roomCleaningAssignmentRepository.findAll();
            System.out.println("Creating " + assignments.size() + " RoomCleaningAssignment relationships");
            for (RoomCleaningAssignment assign : assignments) {
                if (assign.getId() != null) {
                    neo4jClient.query(
                            "MATCH (rct:RoomCleaningTask {taskId: $taskId}) MATCH (c:Cleaner {cleanerId: $cleanerId}) MERGE (c)-[:ASSIGNED_TO {assignedAt: $assignedAt}]->(rct)"
                    ).bind(assign.getId().getTaskId()).to("taskId")
                     .bind(assign.getId().getCleanerId()).to("cleanerId")
                     .bind(assign.getAssignedAt() != null ? assign.getAssignedAt().toString() : null).to("assignedAt")
                     .run();
                }
            }

            // 12. Guests
            List<Guest> guests = guestRepository.findAll();
            System.out.println("Creating " + guests.size() + " Guest nodes");
            for (Guest g : guests) {
                neo4jClient.query(
                        "MERGE (g:Guest {guestId: $id}) SET g.firstName = $firstName, g.lastName = $lastName, g.email = $email, g.phone = $phone"
                ).bind(g.getGuestId()).to("id")
                 .bind(g.getFirstName()).to("firstName")
                 .bind(g.getLastName()).to("lastName")
                 .bind(g.getEmail()).to("email")
                 .bind(g.getPhone()).to("phone")
                 .run();
            }

            // 13. Reservations
            List<Reservation> reservations = reservationRepository.findAll();
            System.out.println("Creating " + reservations.size() + " Reservation nodes");
            for (Reservation res : reservations) {
                neo4jClient.query(
                        "MERGE (res:Reservation {reservationId: $id}) SET res.referenceNo = $ref, res.status = $status, res.checkIn = $checkIn, res.checkOut = $checkOut, res.nights = $nights, res.numGuests = $numGuests"
                ).bind(res.getReservationId()).to("id")
                 .bind(res.getReferenceNo()).to("ref")
                 .bind(res.getStatus()).to("status")
                 .bind(res.getCheckInDate() != null ? res.getCheckInDate().toString() : null).to("checkIn")
                 .bind(res.getCheckOutDate() != null ? res.getCheckOutDate().toString() : null).to("checkOut")
                 .bind(res.getNights()).to("nights")
                 .bind(res.getNumGuests()).to("numGuests")
                 .run();
            }

            // Guest-[:MADE_RESERVATION]->Reservation
            System.out.println("Creating Guest-[:MADE_RESERVATION]->Reservation relationships");
            for (Reservation res : reservations) {
                if (res.getGuest() != null) {
                    neo4jClient.query(
                            "MATCH (g:Guest {guestId: $guestId}) MATCH (res:Reservation {reservationId: $resId}) MERGE (g)-[:MADE_RESERVATION]->(res)"
                    ).bind(res.getGuest().getGuestId()).to("guestId")
                     .bind(res.getReservationId()).to("resId")
                     .run();
                }
            }

            // Room-[:BOOKED_FOR]->Reservation
            System.out.println("Creating Room-[:BOOKED_FOR]->Reservation relationships");
            for (Reservation res : reservations) {
                if (res.getRoom() != null) {
                    neo4jClient.query(
                            "MATCH (r:Room {roomId: $roomId}) MATCH (res:Reservation {reservationId: $resId}) MERGE (r)-[:BOOKED_FOR]->(res)"
                    ).bind(res.getRoom().getRoomId()).to("roomId")
                     .bind(res.getReservationId()).to("resId")
                     .run();
                }
            }

            // 14. ReservationGuests
            List<ReservationGuest> resGuests = reservationGuestRepository.findAll();
            System.out.println("Creating " + resGuests.size() + " ReservationGuest relationships");
            for (ReservationGuest rg : resGuests) {
                if (rg.getId() != null) {
                    neo4jClient.query(
                            "MATCH (res:Reservation {reservationId: $resId}) MATCH (g:Guest {guestId: $guestId}) MERGE (g)-[:GUEST_OF {isPrimary: $isPrimary}]->(res)"
                    ).bind(rg.getId().getReservationId()).to("resId")
                     .bind(rg.getId().getGuestId()).to("guestId")
                     .bind(rg.getIsPrimary()).to("isPrimary")
                     .run();
                }
            }

            // 15. Bills
            List<Bill> bills = billRepository.findAll();
            System.out.println("Creating " + bills.size() + " Bill nodes");
            for (Bill bill : bills) {
                neo4jClient.query(
                        "MERGE (b:Bill {billId: $id}) SET b.totalAmount = $total, b.openedAt = $opened, b.closedAt = $closed"
                ).bind(bill.getBillId()).to("id")
                 .bind(bill.getTotalAmount() != null ? bill.getTotalAmount().toString() : null).to("total")
                 .bind(bill.getOpenedAt() != null ? bill.getOpenedAt().toString() : null).to("opened")
                 .bind(bill.getClosedAt() != null ? bill.getClosedAt().toString() : null).to("closed")
                 .run();
            }

            // Reservation-[:HAS_BILL]->Bill
            System.out.println("Creating Reservation-[:HAS_BILL]->Bill relationships");
            for (Bill bill : bills) {
                if (bill.getReservation() != null) {
                    neo4jClient.query(
                            "MATCH (res:Reservation {reservationId: $resId}) MATCH (b:Bill {billId: $billId}) MERGE (res)-[:HAS_BILL]->(b)"
                    ).bind(bill.getReservation().getReservationId()).to("resId")
                     .bind(bill.getBillId()).to("billId")
                     .run();
                }
            }

            // 16. BillItems
            List<BillItem> billItems = billItemRepository.findAll();
            System.out.println("Creating " + billItems.size() + " BillItem nodes");
            for (BillItem item : billItems) {
                neo4jClient.query(
                        "MERGE (bi:BillItem {billItemId: $id}) SET bi.itemType = $type, bi.description = $desc, bi.quantity = $qty, bi.unitPrice = $unitPrice, bi.lineTotal = $total"
                ).bind(item.getBillItemId()).to("id")
                 .bind(item.getItemType()).to("type")
                 .bind(item.getDescription()).to("desc")
                 .bind(item.getQuantity()).to("qty")
                 .bind(item.getUnitPrice() != null ? item.getUnitPrice().toString() : null).to("unitPrice")
                 .bind(item.getLineTotal() != null ? item.getLineTotal().toString() : null).to("total")
                 .run();
            }

            // Bill-[:HAS_ITEM]->BillItem
            System.out.println("Creating Bill-[:HAS_ITEM]->BillItem relationships");
            for (BillItem item : billItems) {
                if (item.getBill() != null) {
                    neo4jClient.query(
                            "MATCH (b:Bill {billId: $billId}) MATCH (bi:BillItem {billItemId: $itemId}) MERGE (b)-[:HAS_ITEM]->(bi)"
                    ).bind(item.getBill().getBillId()).to("billId")
                     .bind(item.getBillItemId()).to("itemId")
                     .run();
                }
            }

            System.out.println("\n=== Neo4j Migration completed successfully for all 13 entities ===\n");
        } catch (Exception e) {
            System.err.println("Error during Neo4j migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
