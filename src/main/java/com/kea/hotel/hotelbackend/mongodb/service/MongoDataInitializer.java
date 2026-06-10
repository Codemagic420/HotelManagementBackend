package com.kea.hotel.hotelbackend.mongodb.service;

import com.kea.hotel.hotelbackend.mongodb.document.*;
import com.kea.hotel.hotelbackend.mongodb.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Profile("!test")
public class MongoDataInitializer implements ApplicationRunner {

    private final MongoGuestRepository mongoGuestRepository;
    private final MongoRoomRepository mongoRoomRepository;
    private final MongoCleanerRepository mongoCleanerRepository;
    private final MongoRoomTypeRepository mongoRoomTypeRepository;
    private final MongoExtraServiceRepository mongoExtraServiceRepository;
    private final MongoInventoryItemRepository mongoInventoryItemRepository;
    private final MongoSeasonRateRepository mongoSeasonRateRepository;

    private final Random random = new Random(12345);

    private final String[] firstNames = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "James", "Emma", "Robert", "Olivia",
            "William", "Ava", "Richard", "Isabella", "Joseph", "Mia", "Thomas", "Charlotte", "Charles", "Amelia",
            "Christopher", "Harper", "Daniel", "Evelyn", "Matthew", "Abigail", "Mark", "Elizabeth", "Donald", "Sofia",
            "Steven", "Ella", "Paul", "Scarlett", "Andrew", "Victoria", "Joshua", "Madison", "Kenneth", "Chloe",
            "Kevin", "Penelope", "Brian", "Layla", "George", "Riley", "Edward", "Nora", "Ronald", "Lily"};

    private final String[] lastNames = {"Anderson", "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
            "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Harris", "Taylor", "Moore",
            "Thomas", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark",
            "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres",
            "Peterson", "Phillips", "Campbell", "Parker", "Evans", "Edwards", "Collins", "Reeves", "Morris", "Murphy"};

    private final String[] hotelServices = {"Room Service", "Breakfast", "Lunch", "Dinner", "Laundry", "Massage",
            "Spa", "Airport Transfer", "City Tour", "Wine Tasting", "Gym", "Personal Training", "Concierge"};

    private final String[] inventoryItems = {"Bed Sheets", "Towels", "Shampoo", "Lotion", "Toilet Paper",
            "Light Bulbs", "WiFi Router", "Furniture Polish", "Glass Cleaner", "First Aid Kit"};

    public MongoDataInitializer(MongoGuestRepository mongoGuestRepository,
                               MongoRoomRepository mongoRoomRepository,
                               MongoCleanerRepository mongoCleanerRepository,
                               MongoRoomTypeRepository mongoRoomTypeRepository,
                               MongoExtraServiceRepository mongoExtraServiceRepository,
                               MongoInventoryItemRepository mongoInventoryItemRepository,
                               MongoSeasonRateRepository mongoSeasonRateRepository) {
        this.mongoGuestRepository = mongoGuestRepository;
        this.mongoRoomRepository = mongoRoomRepository;
        this.mongoCleanerRepository = mongoCleanerRepository;
        this.mongoRoomTypeRepository = mongoRoomTypeRepository;
        this.mongoExtraServiceRepository = mongoExtraServiceRepository;
        this.mongoInventoryItemRepository = mongoInventoryItemRepository;
        this.mongoSeasonRateRepository = mongoSeasonRateRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (mongoCleanerRepository.findAll().isEmpty()) {
            System.out.println("🔄 Initializing MongoDB with 100+ records per collection...");
            initializeRoomTypes();
            initializeCleaners();
            initializeExtraServices();
            initializeInventoryItems();
            initializeSeasonRates();
            initializeRooms();
            initializeGuests();
            printMongoStats();
        }
    }

    private void initializeRoomTypes() {
        mongoRoomTypeRepository.deleteAll();
        String[] types = {"Single", "Double", "Suite"};
        int[] occupancy = {1, 2, 4};

        for (int i = 0; i < 3; i++) {
            MongoRoomType roomType = new MongoRoomType();
            roomType.setRoomTypeId((long) (i + 1));
            roomType.setName(types[i]);
            roomType.setMaxOccupancy(occupancy[i]);
            mongoRoomTypeRepository.save(roomType);
        }
        System.out.println("✓ Created 3 room types in MongoDB");
    }

    private void initializeCleaners() {
        mongoCleanerRepository.deleteAll();
        for (int i = 0; i < 120; i++) {
            MongoCleaner cleaner = new MongoCleaner();
            cleaner.setCleanerId((long) (i + 1));
            cleaner.setFirstName(firstNames[random.nextInt(firstNames.length)]);
            cleaner.setLastName(lastNames[random.nextInt(lastNames.length)]);
            cleaner.setPhone(generatePhone());
            cleaner.setActive(i % 10 < 8);
            mongoCleanerRepository.save(cleaner);
        }
        System.out.println("✓ Created 120 cleaners in MongoDB");
    }

    private void initializeExtraServices() {
        mongoExtraServiceRepository.deleteAll();
        for (int i = 0; i < 150; i++) {
            MongoExtraService service = new MongoExtraService();
            service.setExtraServiceId((long) (i + 1));
            String baseName = hotelServices[i % hotelServices.length];
            service.setName(baseName + (i / hotelServices.length > 0 ? " - " + (i / hotelServices.length) : ""));
            service.setUnitPrice(new BigDecimal(15 + (i % 200)));
            service.setPriceUnit(i % 3 == 0 ? "per item" : (i % 3 == 1 ? "per session" : "per day"));
            service.setActive(random.nextBoolean());
            mongoExtraServiceRepository.save(service);
        }
        System.out.println("✓ Created 150 extra services in MongoDB");
    }

    private void initializeInventoryItems() {
        mongoInventoryItemRepository.deleteAll();
        for (int i = 0; i < 130; i++) {
            MongoInventoryItem item = new MongoInventoryItem();
            item.setInventoryItemId((long) (i + 1));
            String baseName = inventoryItems[i % inventoryItems.length];
            item.setName(baseName + (i / inventoryItems.length > 0 ? " #" + (i / inventoryItems.length) : ""));
            item.setUnitPrice(new BigDecimal(2 + (i % 300)));
            item.setActive(i % 8 < 7);
            mongoInventoryItemRepository.save(item);
        }
        System.out.println("✓ Created 130 inventory items in MongoDB");
    }

    private void initializeSeasonRates() {
        mongoSeasonRateRepository.deleteAll();
        List<MongoRoomType> roomTypes = mongoRoomTypeRepository.findAll();
        String[] seasons = {"Low", "High", "Peak", "Off-Peak", "Shoulder"};

        int count = 0;
        for (MongoRoomType roomType : roomTypes) {
            for (int s = 0; s < 40; s++) {
                MongoSeasonRate rate = new MongoSeasonRate();
                rate.setRateId((long) count + 1);
                rate.setRoomType(roomType.getName());
                rate.setSeason(seasons[s % seasons.length]);
                rate.setPricePerNight(new BigDecimal(50 + (s * 20) + (roomType.getMaxOccupancy() * 50)));
                rate.setValidFrom(LocalDate.of(2026, (s % 12) + 1, 1));
                rate.setValidTo(LocalDate.of(2026, ((s + 2) % 12) + 1, 28));
                mongoSeasonRateRepository.save(rate);
                count++;
            }
        }
        System.out.println("✓ Created " + count + " season rates in MongoDB (100+ required)");
    }

    private void initializeRooms() {
        mongoRoomRepository.deleteAll();
        List<MongoRoomType> roomTypes = mongoRoomTypeRepository.findAll();

        for (int i = 0; i < 110; i++) {
            MongoRoom room = new MongoRoom();
            room.setRoomId((long) (i + 1));
            room.setRoomNumber(generateRoomNumber(i));
            MongoRoomType roomType = roomTypes.get(i % roomTypes.size());
            room.setRoomType(roomType.getName());
            room.setRoomStatus(i % 4 == 0 ? "OCCUPIED" : (i % 4 == 1 ? "CLEANING" : (i % 4 == 2 ? "MAINTENANCE" : "AVAILABLE")));
            room.setCleanStatus(i % 3 == 0 ? "DIRTY" : (i % 3 == 1 ? "NEEDS_ATTENTION" : "CLEAN"));
            room.setOccupied(random.nextBoolean());
            mongoRoomRepository.save(room);
        }
        System.out.println("✓ Created 110 rooms in MongoDB");
    }

    private void initializeGuests() {
        mongoGuestRepository.deleteAll();
        for (int i = 0; i < 150; i++) {
            MongoGuest guest = new MongoGuest();
            guest.setGuestId((long) (i + 1));
            guest.setFirstName(firstNames[random.nextInt(firstNames.length)]);
            guest.setLastName(lastNames[random.nextInt(lastNames.length)]);
            guest.setEmail(guest.getFirstName().toLowerCase() + "." + guest.getLastName().toLowerCase() + i + "@example.com");
            guest.setPhone(generatePhone());
            guest.setCreditCardLast4(String.format("%04d", random.nextInt(10000)));

            // Add embedded reservations with bills
            for (int r = 0; r < Math.min(2, (i % 4) + 1); r++) {
                MongoReservation reservation = new MongoReservation();
                reservation.setReservationId((long) (i * 100 + r));
                reservation.setReferenceNo("RES" + String.format("%05d", i * 100 + r));
                LocalDate checkIn = LocalDate.of(2026, 1, 1).plusDays(i + r * 10);
                reservation.setCheckInDate(checkIn);
                reservation.setCheckOutDate(checkIn.plusDays((r % 7) + 1));
                reservation.setNights((r % 7) + 1);
                reservation.setNumGuests((r % 5) + 1);
                reservation.setRoomType(new String[]{"Single", "Double", "Suite"}[r % 3]);
                reservation.setAssignedRoomNumber(String.format("R%03d", (i + r) % 110 + 1));
                reservation.setBookedNightlyPrice(new BigDecimal(100 + (r * 50)));
                reservation.setStatus(new String[]{"CONFIRMED", "PENDING", "CHECKED_OUT", "CANCELLED"}[r % 4]);
                reservation.setCreatedAt(LocalDateTime.now().minusDays(30 - r));

                // Add embedded bills
                MongoBill bill = new MongoBill();
                bill.setBillId((long) (i * 100 + r));
                bill.setReservationId(reservation.getReservationId());
                bill.setOpenedAt(reservation.getCreatedAt());
                if (r % 2 == 0) {
                    bill.setClosedAt(bill.getOpenedAt().plusDays(5 + r));
                }
                bill.setTotalAmount(reservation.getBookedNightlyPrice().multiply(new BigDecimal(reservation.getNights())));

                // Add bill items
                MongoBillItem billItem = new MongoBillItem();
                billItem.setBillItemId((long) (i * 1000 + r));
                billItem.setItemType("ROOM_CHARGE");
                billItem.setDescription(reservation.getRoomType() + " - " + reservation.getNights() + " night(s)");
                billItem.setQuantity(reservation.getNights());
                billItem.setUnitPrice(reservation.getBookedNightlyPrice());
                billItem.setLineTotal(bill.getTotalAmount());
                billItem.setPostedAt(bill.getOpenedAt());

                bill.getItems().add(billItem);
                reservation.getBills().add(bill);
                guest.getReservations().add(reservation);
            }

            mongoGuestRepository.save(guest);
        }
        System.out.println("✓ Created 150 guests with embedded reservations and bills in MongoDB");
    }

    private void printMongoStats() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 MONGODB DATA SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("Room Types: " + mongoRoomTypeRepository.count());
        System.out.println("Cleaners: " + mongoCleanerRepository.count());
        System.out.println("Extra Services: " + mongoExtraServiceRepository.count());
        System.out.println("Inventory Items: " + mongoInventoryItemRepository.count());
        System.out.println("Season Rates: " + mongoSeasonRateRepository.count());
        System.out.println("Rooms: " + mongoRoomRepository.count());
        System.out.println("Guests (with embedded reservations & bills): " + mongoGuestRepository.count());
        System.out.println("=".repeat(60) + "\n");
    }

    private String generatePhone() {
        return String.format("555-%04d", random.nextInt(10000));
    }

    private String generateRoomNumber(int index) {
        int floor = (index / 10) + 1;
        int roomInFloor = (index % 10) + 1;
        return String.format("%d%02d", floor, roomInFloor);
    }
}
