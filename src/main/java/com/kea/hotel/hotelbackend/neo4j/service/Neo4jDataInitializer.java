package com.kea.hotel.hotelbackend.neo4j.service;

import com.kea.hotel.hotelbackend.neo4j.node.*;
import com.kea.hotel.hotelbackend.neo4j.repository.*;
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
public class Neo4jDataInitializer implements ApplicationRunner {

    private final Neo4jGuestRepository guestRepository;
    private final Neo4jReservationRepository reservationRepository;
    private final Neo4jRoomRepository roomRepository;
    private final Neo4jRoomTypeRepository roomTypeRepository;
    private final Neo4jCleanerRepository cleanerRepository;
    private final Neo4jExtraServiceRepository extraServiceRepository;
    private final Neo4jInventoryItemRepository inventoryItemRepository;
    private final Neo4jSeasonRateRepository seasonRateRepository;

    private final Random random = new Random(12345);
    private final String[] firstNames = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "James", "Emma", "Robert", "Olivia"};
    private final String[] lastNames = {"Anderson", "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez"};

    public Neo4jDataInitializer(Neo4jGuestRepository guestRepository,
                               Neo4jReservationRepository reservationRepository,
                               Neo4jRoomRepository roomRepository,
                               Neo4jRoomTypeRepository roomTypeRepository,
                               Neo4jCleanerRepository cleanerRepository,
                               Neo4jExtraServiceRepository extraServiceRepository,
                               Neo4jInventoryItemRepository inventoryItemRepository,
                               Neo4jSeasonRateRepository seasonRateRepository) {
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.cleanerRepository = cleanerRepository;
        this.extraServiceRepository = extraServiceRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.seasonRateRepository = seasonRateRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            System.out.println("🔄 Checking Neo4j connection status...");
            System.out.flush();

            long cleanerCount = waitForNeo4jConnection();

            if (cleanerCount == 0) {
                System.out.println("ℹ️  Neo4j is empty - data must be initialized via migrator");
                System.out.println("   → Run: POST http://localhost:8080/api/migrate");
                System.out.println("   → Or: POST http://localhost:8080/api/neo4j/migrate");
            } else {
                System.out.println("✓ Neo4j is initialized with " + cleanerCount + " cleaners");
            }
            System.out.flush();
        } catch (Exception e) {
            System.err.println("⚠️  Neo4j connection check failed");
            System.err.println("   This is non-blocking - Neo4j will be initialized via migrator API");
            System.err.println("   → Run: POST http://localhost:8080/api/migrate");
            System.err.println("   → Status: GET http://localhost:8080/api/neo4j/diagnostics/status");
        }
    }

    private long waitForNeo4jConnection() throws Exception {
        int maxRetries = 15;
        int retryDelay = 3000; // 3 seconds
        Exception lastException = null;

        for (int i = 0; i < maxRetries; i++) {
            try {
                long count = cleanerRepository.count();
                System.out.println("✓ Neo4j connection successful");
                return count;
            } catch (NullPointerException e) {
                lastException = e;
                System.out.println("   ⏳ Neo4j initialization in progress... (attempt " + (i + 1) + "/" + maxRetries + ")");
                if (i < maxRetries - 1) {
                    Thread.sleep(retryDelay);
                }
            } catch (Exception e) {
                lastException = e;
                System.out.println("   ⏳ Neo4j not ready yet, retrying... (attempt " + (i + 1) + "/" + maxRetries + ")");
                System.out.println("      Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                if (i < maxRetries - 1) {
                    Thread.sleep(retryDelay);
                }
            }
        }

        System.err.println("   ❌ Neo4j failed after " + maxRetries + " attempts (" + (maxRetries * retryDelay / 1000) + " seconds)");
        throw lastException;
    }

    private void initializeRoomTypes() {
        String[] types = {"Single", "Double", "Suite"};
        int[] occupancy = {1, 2, 4};

        for (int i = 0; i < 3; i++) {
            Neo4jRoomType roomType = new Neo4jRoomType();
            roomType.setRoomTypeId((long) (i + 1));
            roomType.setName(types[i]);
            roomType.setMaxOccupancy(occupancy[i]);
            roomTypeRepository.save(roomType);
        }
        System.out.println("✓ Created 3 room types in Neo4j");
    }

    private void initializeRooms() {
        List<Neo4jRoomType> roomTypes = roomTypeRepository.findAll();

        for (int i = 0; i < 110; i++) {
            Neo4jRoom room = new Neo4jRoom();
            room.setRoomId((long) (i + 1));
            room.setRoomNumber(generateRoomNumber(i));
            room.setRoomType(roomTypes.get(i % roomTypes.size()));
            room.setRoomStatus(i % 4 == 0 ? "OCCUPIED" : "AVAILABLE");
            room.setCleanStatus(i % 3 == 0 ? "DIRTY" : "CLEAN");
            room.setOccupied(random.nextBoolean());
            roomRepository.save(room);
        }
        System.out.println("✓ Created 110 rooms in Neo4j");
    }

    private void initializeCleaners() {
        for (int i = 0; i < 120; i++) {
            Neo4jCleaner cleaner = new Neo4jCleaner();
            cleaner.setCleanerId((long) (i + 1));
            cleaner.setFirstName(firstNames[random.nextInt(firstNames.length)]);
            cleaner.setLastName(lastNames[random.nextInt(lastNames.length)]);
            cleaner.setPhone(generatePhone());
            cleaner.setActive(i % 10 < 8);
            cleanerRepository.save(cleaner);
        }
        System.out.println("✓ Created 120 cleaners in Neo4j");
    }

    private void initializeExtraServices() {
        String[] services = {"Room Service", "Spa", "Airport Transfer", "City Tour", "Massage", "Laundry", "Breakfast", "Parking"};

        for (int i = 0; i < 150; i++) {
            Neo4jExtraService service = new Neo4jExtraService();
            service.setExtraServiceId((long) (i + 1));
            service.setName(services[i % services.length] + (i / services.length > 0 ? " - " + (i / services.length) : ""));
            service.setUnitPrice(new BigDecimal(15 + (i % 200)));
            service.setPriceUnit(i % 3 == 0 ? "per item" : "per session");
            service.setActive(random.nextBoolean());
            extraServiceRepository.save(service);
        }
        System.out.println("✓ Created 150 extra services in Neo4j");
    }

    private void initializeInventoryItems() {
        String[] items = {"Sheets", "Towels", "Shampoo", "Lotion", "Toilet Paper", "Light Bulb", "WiFi Router", "Cleaner"};

        for (int i = 0; i < 130; i++) {
            Neo4jInventoryItem item = new Neo4jInventoryItem();
            item.setInventoryItemId((long) (i + 1));
            item.setName(items[i % items.length] + (i / items.length > 0 ? " #" + (i / items.length) : ""));
            item.setUnitPrice(new BigDecimal(2 + (i % 300)));
            item.setActive(i % 8 < 7);
            inventoryItemRepository.save(item);
        }
        System.out.println("✓ Created 130 inventory items in Neo4j");
    }

    private void initializeSeasonRates() {
        List<Neo4jRoomType> roomTypes = roomTypeRepository.findAll();
        String[] seasons = {"Low", "High", "Peak"};

        int count = 0;
        for (Neo4jRoomType roomType : roomTypes) {
            for (int s = 0; s < 40; s++) {
                Neo4jSeasonRate rate = new Neo4jSeasonRate();
                rate.setRateId((long) count + 1);
                rate.setRoomType(roomType.getName());
                rate.setSeason(seasons[s % seasons.length]);
                rate.setPricePerNight(new BigDecimal(50 + (s * 20) + (roomType.getMaxOccupancy() * 50)));
                rate.setValidFrom(LocalDate.of(2026, (s % 12) + 1, 1).toString());
                rate.setValidTo(LocalDate.of(2026, ((s + 2) % 12) + 1, 28).toString());
                seasonRateRepository.save(rate);
                count++;
            }
        }
        System.out.println("✓ Created " + count + " season rates in Neo4j");
    }

    private void initializeGuests() {
        for (int i = 0; i < 150; i++) {
            Neo4jGuest guest = new Neo4jGuest();
            guest.setGuestId((long) (i + 1));
            guest.setFirstName(firstNames[random.nextInt(firstNames.length)]);
            guest.setLastName(lastNames[random.nextInt(lastNames.length)]);
            guest.setEmail(guest.getFirstName().toLowerCase() + "." + guest.getLastName().toLowerCase() + i + "@example.com");
            guest.setPhone(generatePhone());
            guest.setCreditCardLast4(String.format("%04d", random.nextInt(10000)));
            guestRepository.save(guest);
        }
        System.out.println("✓ Created 150 guests in Neo4j");
    }

    private void initializeReservations() {
        List<Neo4jGuest> guests = guestRepository.findAll();
        List<Neo4jRoom> rooms = roomRepository.findAll();
        List<Neo4jSeasonRate> rates = seasonRateRepository.findAll();

        LocalDate baseDate = LocalDate.of(2026, 1, 1);

        for (int i = 0; i < 120; i++) {
            Neo4jReservation res = new Neo4jReservation();
            res.setReservationId((long) (i + 1));
            res.setReferenceNo("RES" + String.format("%05d", i + 1));

            res.setCheckInDate(baseDate.plusDays(i * 2));
            int nights = (i % 7) + 1;
            res.setCheckOutDate(res.getCheckInDate().plusDays(nights));
            res.setNights(nights);
            res.setNumGuests((i % 5) + 1);
            res.setBookedNightlyPrice(new BigDecimal(100 + (i % 200)));
            res.setStatus(new String[]{"CONFIRMED", "CHECKED_OUT"}[i % 2]);
            res.setCreatedAt(LocalDateTime.now().minusDays(30 - (i % 30)).toString());

            res.setGuest(guests.get(i % guests.size()));
            res.setRoom(rooms.get(i % rooms.size()));

            reservationRepository.save(res);
        }
        System.out.println("✓ Created 120 reservations with relationships in Neo4j");
    }

    private void printNeo4jStats() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 NEO4J DATA SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("Room Types: " + roomTypeRepository.count());
        System.out.println("Rooms: " + roomRepository.count());
        System.out.println("Cleaners: " + cleanerRepository.count());
        System.out.println("Extra Services: " + extraServiceRepository.count());
        System.out.println("Inventory Items: " + inventoryItemRepository.count());
        System.out.println("Season Rates: " + seasonRateRepository.count());
        System.out.println("Guests: " + guestRepository.count());
        System.out.println("Reservations (with relationships): " + reservationRepository.count());
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
