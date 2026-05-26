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
import org.bson.Document;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
            mongoTemplate.dropCollection("rooms");
            mongoTemplate.dropCollection("reservations");
            mongoTemplate.dropCollection("bills");
            System.out.println("Cleared existing MongoDB collections");

            List<Room> rooms = roomRepository.findAll();
            System.out.println("Migrating " + rooms.size() + " rooms to MongoDB");
            for (Room room : rooms) {
                Document doc = new Document()
                    .append("roomId", room.getRoomId())
                    .append("roomNumber", room.getRoomNumber())
                    .append("type", room.getType())
                    .append("occupied", room.getOccupied());
                mongoTemplate.getDb().getCollection("rooms").insertOne(doc);
            }

            List<Reservation> reservations = reservationRepository.findAll();
            System.out.println("Migrating " + reservations.size() + " reservations to MongoDB");
            for (Reservation res : reservations) {
                Document doc = new Document()
                    .append("reservationId", res.getReservationId())
                    .append("referenceNo", res.getReferenceNo())
                    .append("status", res.getStatus())
                    .append("checkInDate", res.getCheckInDate() != null ? res.getCheckInDate().toString() : null)
                    .append("checkOutDate", res.getCheckOutDate() != null ? res.getCheckOutDate().toString() : null)
                    .append("nights", res.getNights());

                if (res.getGuest() != null) {
                    Guest g = res.getGuest();
                    Document guestDoc = new Document()
                        .append("guestId", g.getGuestId())
                        .append("firstName", g.getFirstName())
                        .append("lastName", g.getLastName())
                        .append("email", g.getEmail());
                    doc.append("guest", guestDoc);
                }

                if (res.getRoom() != null) {
                    Room r = res.getRoom();
                    Document roomDoc = new Document()
                        .append("roomId", r.getRoomId())
                        .append("roomNumber", r.getRoomNumber())
                        .append("type", r.getType());
                    doc.append("room", roomDoc);
                }

                mongoTemplate.getDb().getCollection("reservations").insertOne(doc);
            }

            List<Bill> bills = billRepository.findAll();
            System.out.println("Migrating " + bills.size() + " bills to MongoDB");
            for (Bill bill : bills) {
                Document doc = new Document()
                    .append("billId", bill.getBillId())
                    .append("totalAmount", bill.getTotalAmount() != null ? bill.getTotalAmount().toString() : null)
                    .append("openedAt", bill.getOpenedAt() != null ? bill.getOpenedAt().toString() : null)
                    .append("closedAt", bill.getClosedAt() != null ? bill.getClosedAt().toString() : null);

                if (bill.getReservation() != null) {
                    doc.append("reservationId", bill.getReservation().getReservationId());
                }

                List<Document> billItems = new ArrayList<>();
                for (BillItem item : billItemRepository.findAll()) {
                    if (item.getBill() != null && item.getBill().getBillId().equals(bill.getBillId())) {
                        Document itemDoc = new Document()
                            .append("billItemId", item.getBillItemId())
                            .append("itemType", item.getItemType())
                            .append("description", item.getDescription())
                            .append("quantity", item.getQuantity())
                            .append("unitPrice", item.getUnitPrice() != null ? item.getUnitPrice().toString() : null)
                            .append("lineTotal", item.getLineTotal() != null ? item.getLineTotal().toString() : null)
                            .append("postedAt", item.getPostedAt() != null ? item.getPostedAt().toString() : null);
                        billItems.add(itemDoc);
                    }
                }
                doc.append("items", billItems);

                mongoTemplate.getDb().getCollection("bills").insertOne(doc);
            }

            System.out.println("MongoDB migration completed successfully!");
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
