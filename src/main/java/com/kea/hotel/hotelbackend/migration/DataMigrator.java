package com.kea.hotel.hotelbackend.migration;

import com.kea.hotel.hotelbackend.model.Bill;
import com.kea.hotel.hotelbackend.model.BillItem;
import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.model.Reservation;
import com.kea.hotel.hotelbackend.model.Room;
import com.kea.hotel.hotelbackend.repository.BillItemRepository;
import com.kea.hotel.hotelbackend.repository.BillRepository;
import com.kea.hotel.hotelbackend.repository.GuestRepository;
import com.kea.hotel.hotelbackend.repository.ReservationRepository;
import com.kea.hotel.hotelbackend.repository.RoomRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/migrate")
@Profile("!test")
public class DataMigrator {

    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final MongoTemplate mongoTemplate;
    private final Neo4jClient neo4jClient;

    public DataMigrator(GuestRepository guestRepository,
                        RoomRepository roomRepository,
                        ReservationRepository reservationRepository,
                        BillRepository billRepository,
                        BillItemRepository billItemRepository,
                        MongoTemplate mongoTemplate,
                        Neo4jClient neo4jClient) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
        this.mongoTemplate = mongoTemplate;
        this.neo4jClient = neo4jClient;
    }

    @PostMapping
    public String migrate() {
        migrateToMongo();
        migrateToNeo4j();
        return "Migration completed successfully.";
    }

    public void migrateToMongo() {
        try {
            List<Room> rooms = roomRepository.findAll();
            System.out.println("Found " + rooms.size() + " rooms to migrate");

            for (Room room : rooms) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("roomId", room.getRoomId());
                doc.put("roomNumber", room.getRoomNumber());
                doc.put("type", room.getType());
                doc.put("occupied", room.getOccupied());
                System.out.println("Saving room document: " + doc);
                mongoTemplate.save(doc, "rooms");
            }

            List<Reservation> reservations = reservationRepository.findAll();
            System.out.println("Found " + reservations.size() + " reservations to migrate");

            for (Reservation res : reservations) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("reservationId", res.getReservationId());
                doc.put("status", res.getStatus());
                doc.put("checkInDate", res.getCheckInDate() != null ? res.getCheckInDate().toString() : null);
                doc.put("checkOutDate", res.getCheckOutDate() != null ? res.getCheckOutDate().toString() : null);

                Guest g = res.getGuest();
                if (g != null) {
                    Map<String, Object> guestDoc = new HashMap<>();
                    guestDoc.put("guestId", g.getGuestId());
                    guestDoc.put("firstName", g.getFirstName());
                    guestDoc.put("lastName", g.getLastName());
                    guestDoc.put("email", g.getEmail());
                    doc.put("guest", guestDoc);
                }

                Room r = res.getRoom();
                if (r != null) {
                    Map<String, Object> roomDoc = new HashMap<>();
                    roomDoc.put("roomId", r.getRoomId());
                    roomDoc.put("roomNumber", r.getRoomNumber());
                    roomDoc.put("type", r.getType());
                    doc.put("room", roomDoc);
                }

                System.out.println("Saving reservation document: " + doc);
                mongoTemplate.save(doc, "reservations");
            }

            List<Bill> bills = billRepository.findAll();
            System.out.println("Found " + bills.size() + " bills to migrate");

            for (Bill bill : bills) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("billId", bill.getBillId());
                doc.put("totalAmount", bill.getTotalAmount() != null ? bill.getTotalAmount().toString() : null);
                doc.put("closedAt", bill.getClosedAt() != null ? bill.getClosedAt().toString() : null);

                Reservation res = bill.getReservation();
                if (res != null) {
                    doc.put("reservationId", res.getReservationId());
                }

                List<Map<String, Object>> billItems = new ArrayList<>();
                for (BillItem item : billItemRepository.findAll()) {
                    if (item.getBill() != null && item.getBill().getBillId().equals(bill.getBillId())) {
                        Map<String, Object> itemDoc = new HashMap<>();
                        itemDoc.put("billItemId", item.getBillItemId());
                        itemDoc.put("itemType", item.getItemType());
                        itemDoc.put("description", item.getDescription());
                        itemDoc.put("quantity", item.getQuantity());
                        itemDoc.put("unitPrice", item.getUnitPrice() != null ? item.getUnitPrice().toString() : null);
                        itemDoc.put("lineTotal", item.getLineTotal() != null ? item.getLineTotal().toString() : null);
                        itemDoc.put("postedAt", item.getPostedAt() != null ? item.getPostedAt().toString() : null);
                        itemDoc.put("lineTotal", item.getLineTotal() != null ? item.getLineTotal().toString() : null);
                        billItems.add(itemDoc);
                    }
                }
                doc.put("items", billItems);

                System.out.println("Saving bill document: " + doc);
                mongoTemplate.save(doc, "bills");
            }

            System.out.println("Migration to MongoDB completed successfully");
        } catch (Exception e) {
            System.err.println("Error during MongoDB migration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void migrateToNeo4j() {
        for (Guest g : guestRepository.findAll()) {
            neo4jClient.query(
                    "MERGE (g:Guest {guestId: $id}) SET g.firstName = $firstName, g.lastName = $lastName, g.email = $email"
            ).bind(g.getGuestId()).to("id")
             .bind(g.getFirstName()).to("firstName")
             .bind(g.getLastName()).to("lastName")
             .bind(g.getEmail()).to("email")
             .run();
        }

        for (Room r : roomRepository.findAll()) {
            neo4jClient.query(
                    "MERGE (r:Room {roomId: $id}) SET r.roomNumber = $roomNumber, r.type = $type"
            ).bind(r.getRoomId()).to("id")
             .bind(r.getRoomNumber()).to("roomNumber")
             .bind(r.getType()).to("type")
             .run();
        }

        for (Reservation res : reservationRepository.findAll()) {
            neo4jClient.query(
                    "MERGE (res:Reservation {reservationId: $id}) SET res.status = $status, res.checkIn = $checkIn, res.checkOut = $checkOut"
            ).bind(res.getReservationId()).to("id")
             .bind(res.getStatus()).to("status")
             .bind(res.getCheckInDate() != null ? res.getCheckInDate().toString() : null).to("checkIn")
             .bind(res.getCheckOutDate() != null ? res.getCheckOutDate().toString() : null).to("checkOut")
             .run();

            if (res.getGuest() != null && res.getRoom() != null) {
                neo4jClient.query(
                        "MATCH (g:Guest {guestId: $guestId}), (r:Room {roomId: $roomId}), (res:Reservation {reservationId: $resId}) " +
                        "MERGE (g)-[:STAYED_IN]->(res)-[:BOOKED_ROOM]->(r)"
                ).bind(res.getGuest().getGuestId()).to("guestId")
                 .bind(res.getRoom().getRoomId()).to("roomId")
                 .bind(res.getReservationId()).to("resId")
                 .run();
            }
        }
    }
}
