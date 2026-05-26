package com.kea.hotel.hotelbackend.security;

import com.kea.hotel.hotelbackend.model.*;
import com.kea.hotel.hotelbackend.service.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Profile("!test")
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CleanerService cleanerService;
    private final ExtraServiceService extraServiceService;
    private final InventoryItemService inventoryItemService;
    private final RoomTypeService roomTypeService;
    private final SeasonRateService seasonRateService;
    private final RoomService roomService;
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final BillService billService;
    private final BillItemService billItemService;
    private final RoomCleaningTaskService roomCleaningTaskService;
    private final RoomCleaningAssignmentService roomCleaningAssignmentService;
    private final ReservationGuestService reservationGuestService;

    private final Random random = new Random(12345); // Seeded for reproducibility
    private final String[] firstNames = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "James", "Emma", "Robert", "Olivia",
            "William", "Ava", "Richard", "Isabella", "Joseph", "Mia", "Thomas", "Charlotte", "Charles", "Amelia",
            "Christopher", "Harper", "Daniel", "Evelyn", "Matthew", "Abigail", "Mark", "Elizabeth", "Donald", "Sofia",
            "Steven", "Ella", "Paul", "Scarlett", "Andrew", "Victoria", "Joshua", "Madison", "Kenneth", "Chloe",
            "Kevin", "Penelope", "Brian", "Layla", "George", "Riley", "Edward", "Nora", "Ronald", "Lily",
            "Anthony", "Grace", "Frank", "Zoey", "Ryan", "Norah", "Gary", "Hannah", "Nicholas", "Lily",
            "Eric", "Ellie", "Jonathan", "Stella", "Stephen", "Violet", "Larry", "Aurora", "Justin", "Natalie",
            "Scott", "Avery", "Brandon", "Lucy", "Benjamin", "Audrey", "Samuel", "Bella", "Raymond", "Ariana",
            "Patrick", "Gianna", "Jack", "Aaliyah", "Dennis", "Iris", "Jerry", "Hazel", "Tyler", "Lily",
            "Aaron", "Paisley", "Jose", "Faith", "Adam", "Sophia", "Henry", "Emma", "Douglas", "Scarlett"};

    private final String[] lastNames = {"Anderson", "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
            "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Harris", "Taylor", "Moore",
            "Thomas", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark",
            "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres",
            "Peterson", "Phillips", "Campbell", "Parker", "Evans", "Edwards", "Collins", "Reeves", "Morris", "Murphy",
            "Cook", "Morgan", "Peterson", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson", "Watson",
            "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes", "Price",
            "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Foster", "Jimenez", "Powell", "Long",
            "Patterson", "Hughes", "Flores", "Washington", "Butler", "Simmons", "Bryant", "Alexander", "Russell", "Griffin",
            "Hayes", "Byington", "Tate", "Curry", "Howell", "Porter", "Conway", "Rowe", "Lamb", "Diaz"};

    private final String[] hotelServices = {"Room Service", "Breakfast", "Lunch", "Dinner", "Laundry", "Dry Cleaning",
            "Massage", "Spa Treatment", "Facial", "Sauna", "Steam Room", "Hot Tub", "Gym Access", "Personal Training",
            "Airport Transfer", "City Tour", "Restaurant Reservation", "Theatre Tickets", "Concert Tickets", "Wine Tasting",
            "Cooking Class", "Yoga Session", "Meditation Session", "Baby Sitting", "Pet Sitting", "Concierge Service",
            "Business Center", "Conference Room", "Wedding Ceremony", "Honeymoon Package", "Birthday Party", "Corporate Event",
            "Early Check-in", "Late Checkout", "Room Upgrade", "Priority Booking", "VIP Service", "Express Checkout",
            "Newspaper Delivery", "Room Cleaning Service", "Ironing Service", "Shoe Shining", "Wake Up Call", "Turndown Service",
            "Late Night Snacks", "Mini Bar", "Room Phone", "WiFi Premium", "Parking", "EV Charging",
            "Adventure Sports", "Hiking Tour", "Bike Rental", "Kayak Rental", "Scuba Diving", "Surfing Lessons"};

    private final String[] inventoryItems = {"Bed Sheets", "Pillowcases", "Comforter", "Blanket", "Mattress Protector",
            "Bath Towels", "Hand Towels", "Face Cloths", "Bath Robes", "Slippers", "Shampoo", "Conditioner", "Body Wash",
            "Lotion", "Toothbrush", "Toothpaste", "Razor", "Shaving Cream", "Deodorant", "Moisturizer",
            "Soap", "Hand Sanitizer", "Tissues", "Toilet Paper", "Paper Towels", "Cleaning Supplies", "Disinfectant",
            "Vacuum Bags", "Mop Heads", "Broom", "Dust Pan", "Light Bulbs", "Air Filter", "Batteries",
            "WiFi Router", "Phone", "Television", "Remote Control", "Door Lock", "Key Card", "Hanger",
            "Furniture Polish", "Glass Cleaner", "Trash Bags", "Laundry Detergent", "Iron", "Ironing Board",
            "Hairdryer", "Coffee Maker", "Tea Kettle", "Glasses", "Plates", "Utensils", "Refrigerator",
            "Microwave", "Toiletries Kit", "Emergency Kit", "Fire Extinguisher", "First Aid Kit", "Safety Equipment"};

    private final String[] roomTypeNames = {"Single", "Double", "Suite"};

    private final String[] seasons = {"Low", "High", "Peak", "Off-Peak", "Shoulder"};

    private final String[] reservationStatuses = {"CONFIRMED", "PENDING", "CHECKED_IN", "CHECKED_OUT", "CANCELLED"};

    private final String[] taskStatuses = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"};

    private final String[] roomStatuses = {"AVAILABLE", "OCCUPIED", "MAINTENANCE", "CLEANING"};

    private final String[] cleanStatuses = {"CLEAN", "DIRTY", "NEEDS_ATTENTION"};

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          CleanerService cleanerService, ExtraServiceService extraServiceService,
                          InventoryItemService inventoryItemService, RoomTypeService roomTypeService,
                          SeasonRateService seasonRateService, RoomService roomService,
                          GuestService guestService, ReservationService reservationService,
                          BillService billService, BillItemService billItemService,
                          RoomCleaningTaskService roomCleaningTaskService,
                          RoomCleaningAssignmentService roomCleaningAssignmentService,
                          ReservationGuestService reservationGuestService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cleanerService = cleanerService;
        this.extraServiceService = extraServiceService;
        this.inventoryItemService = inventoryItemService;
        this.roomTypeService = roomTypeService;
        this.seasonRateService = seasonRateService;
        this.roomService = roomService;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.billService = billService;
        this.billItemService = billItemService;
        this.roomCleaningTaskService = roomCleaningTaskService;
        this.roomCleaningAssignmentService = roomCleaningAssignmentService;
        this.reservationGuestService = reservationGuestService;
    }

    @Override
    public void run(ApplicationArguments args) {
        initializeUsers();

        if (cleanerService.findAll().isEmpty()) {
            System.out.println("🔄 Initializing 100+ records for each entity...");
            initializeCleaners();
            initializeExtraServices();
            initializeInventoryItems();
            initializeRoomTypes();
            initializeSeasonRates();
            initializeRooms();
            initializeGuests();
            initializeReservations();
            initializeBills();
            initializeRoomCleaningTasks();
            System.out.println("✅ Test data initialized successfully!");
            printDataStats();
        }
    }

    private void initializeUsers() {
        String adminPassword = System.getenv("MYSQL_ADMIN_PASSWORD");
        String staffPassword = System.getenv("MYSQL_STAFF_PASSWORD");
        String cleanerPassword = System.getenv("CLEANER_PASSWORD");

        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException("MYSQL_ADMIN_PASSWORD environment variable is required");
        }
        if (staffPassword == null || staffPassword.isBlank()) {
            throw new IllegalStateException("MYSQL_STAFF_PASSWORD environment variable is required");
        }
        if (cleanerPassword == null || cleanerPassword.isBlank()) {
            throw new IllegalStateException("CLEANER_PASSWORD environment variable is required");
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            UserAccount admin = new UserAccount();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("staff").isEmpty()) {
            UserAccount staff = new UserAccount();
            staff.setUsername("staff");
            staff.setPasswordHash(passwordEncoder.encode(staffPassword));
            staff.setRole(Role.STAFF);
            userRepository.save(staff);
        }

        for (int i = 1; i <= 20; i++) {
            String username = "cleaner" + i;
            if (userRepository.findByUsername(username).isEmpty()) {
                UserAccount cleaner = new UserAccount();
                cleaner.setUsername(username);
                cleaner.setPasswordHash(passwordEncoder.encode(cleanerPassword));
                cleaner.setRole(Role.CLEANER);
                userRepository.save(cleaner);
            }
        }
    }

    private void initializeCleaners() {
        for (int i = 0; i < 120; i++) {
            Cleaner cleaner = new Cleaner();
            cleaner.setFirstName(firstNames[random.nextInt(firstNames.length)]);
            cleaner.setLastName(lastNames[random.nextInt(lastNames.length)]);
            cleaner.setPhone(generatePhone());
            cleaner.setActive(i % 10 < 8); // 80% aktive
            cleanerService.save(cleaner);
        }
        System.out.println("✓ Created 120 cleaners");
    }

    private void initializeExtraServices() {
        for (int i = 0; i < 150; i++) {
            ExtraService service = new ExtraService();
            String baseName = hotelServices[i % hotelServices.length];
            service.setName(baseName + (i / hotelServices.length > 0 ? " - " + (i / hotelServices.length) : ""));
            service.setUnitPrice(new BigDecimal(15 + (i % 200)));
            service.setPriceUnit(i % 3 == 0 ? "per item" : (i % 3 == 1 ? "per session" : "per day"));
            service.setActive(random.nextBoolean());
            extraServiceService.save(service);
        }
        System.out.println("✓ Created 150 extra services");
    }

    private void initializeInventoryItems() {
        for (int i = 0; i < 130; i++) {
            InventoryItem item = new InventoryItem();
            String baseName = inventoryItems[i % inventoryItems.length];
            item.setName(baseName + (i / inventoryItems.length > 0 ? " #" + (i / inventoryItems.length) : ""));
            item.setUnitPrice(new BigDecimal(2 + (i % 300)));
            item.setActive(i % 8 < 7); // 87% aktive
            inventoryItemService.save(item);
        }
        System.out.println("✓ Created 130 inventory items");
    }

    private void initializeRoomTypes() {
        String[] types = {"Single", "Double", "Suite"};
        int[] occupancy = {1, 2, 4};

        for (int i = 0; i < 3; i++) {
            RoomType roomType = new RoomType();
            roomType.setName(types[i]);
            roomType.setMaxOccupancy(occupancy[i]);
            roomTypeService.save(roomType);
        }
        System.out.println("✓ Created 3 room types (Single, Double, Suite)");
    }

    private void initializeSeasonRates() {
        List<RoomType> roomTypes = roomTypeService.findAll();
        int count = 0;
        for (RoomType roomType : roomTypes) {
            for (int s = 0; s < 10; s++) {
                SeasonRate rate = new SeasonRate();
                rate.setRoomType(roomType);
                rate.setSeason(seasons[s % seasons.length]);
                rate.setPricePerNight(new BigDecimal(50 + (s * 20) + (roomType.getMaxOccupancy() * 50)));
                rate.setValidFrom(LocalDate.of(2026, (s % 12) + 1, 1));
                rate.setValidTo(LocalDate.of(2026, ((s + 2) % 12) + 1, 28));
                seasonRateService.save(rate);
                count++;
            }
        }
        System.out.println("✓ Created " + count + " season rates (100+ required)");
    }

    private void initializeRooms() {
        List<RoomType> roomTypes = roomTypeService.findAll();
        for (int i = 0; i < 110; i++) {
            Room room = new Room();
            room.setRoomNumber(generateRoomNumber(i));
            room.setRoomType(roomTypes.get(i % roomTypes.size()));
            room.setRoomStatus(roomStatuses[random.nextInt(roomStatuses.length)]);
            room.setCleanStatus(cleanStatuses[random.nextInt(cleanStatuses.length)]);
            room.setOccupied(random.nextBoolean());
            room.setType(room.getRoomType().getName());
            roomService.save(room);
        }
        System.out.println("✓ Created 110 rooms");
    }

    private void initializeGuests() {
        for (int i = 0; i < 150; i++) {
            Guest guest = new Guest();
            guest.setFirstName(firstNames[random.nextInt(firstNames.length)]);
            guest.setLastName(lastNames[random.nextInt(lastNames.length)]);
            guest.setEmail(guest.getFirstName().toLowerCase() + "." + guest.getLastName().toLowerCase() +
                          i + "@example.com");
            guest.setPhone(generatePhone());
            guest.setCreditCardLast4(String.format("%04d", random.nextInt(10000)));
            guestService.save(guest);
        }
        System.out.println("✓ Created 150 guests");
    }

    private void initializeReservations() {
        List<Guest> guests = guestService.findAllList();
        List<RoomType> roomTypes = roomTypeService.findAll();
        List<SeasonRate> rates = seasonRateService.findAll();
        List<Room> rooms = roomService.findAllList();

        LocalDate baseDate = LocalDate.of(2026, 1, 1);

        for (int i = 0; i < 120; i++) {
            Reservation res = new Reservation();
            res.setReferenceNo("RES" + String.format("%05d", i + 1));

            int daysOffset = i * 2;
            res.setCheckInDate(baseDate.plusDays(daysOffset));
            int nights = (i % 7) + 1;
            res.setCheckOutDate(res.getCheckInDate().plusDays(nights));
            res.setNights(nights);
            res.setNumGuests((i % 5) + 1);

            res.setRoomType(roomTypes.get(i % roomTypes.size()));
            res.setAssignedRoom(rooms.get(i % rooms.size()));
            res.setBookedRate(rates.get(i % rates.size()));
            res.setBookedNightlyPrice(res.getBookedRate().getPricePerNight());
            res.setStatus(reservationStatuses[i % reservationStatuses.length]);
            res.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
            res.setGuest(guests.get(i % guests.size()));
            res.setRoom(rooms.get((i + 5) % rooms.size()));

            reservationService.save(res);
        }
        System.out.println("✓ Created 120 reservations");

        // Add reservation guests
        List<Reservation> reservations = reservationService.findAllList();
        int rgCount = 0;
        for (Reservation res : reservations) {
            if (rgCount < 120) {
                ReservationGuest rg = new ReservationGuest();
                ReservationGuestKey key = new ReservationGuestKey(res.getReservationId(), res.getGuest().getGuestId());
                rg.setId(key);
                rg.setReservation(res);
                rg.setGuest(res.getGuest());
                rg.setIsPrimary(true);
                reservationGuestService.save(rg);
                rgCount++;

                // Add secondary guests for larger reservations
                if (res.getNumGuests() > 1 && rgCount < 120) {
                    Guest secondaryGuest = guests.get((rgCount + random.nextInt(50)) % guests.size());
                    ReservationGuest rg2 = new ReservationGuest();
                    ReservationGuestKey key2 = new ReservationGuestKey(res.getReservationId(), secondaryGuest.getGuestId());
                    rg2.setId(key2);
                    rg2.setReservation(res);
                    rg2.setGuest(secondaryGuest);
                    rg2.setIsPrimary(false);
                    reservationGuestService.save(rg2);
                    rgCount++;
                }
            }
        }
        System.out.println("✓ Created 120+ reservation guests");
    }

    private void initializeBills() {
        List<Reservation> reservations = reservationService.findAllList();
        int billCount = 0;
        int billItemCount = 0;

        for (Reservation res : reservations) {
            if (billCount < 120) {
                Bill bill = new Bill();
                bill.setReservation(res);
                bill.setOpenedAt(LocalDateTime.now().minusDays(random.nextInt(30)));

                if (res.getStatus().equals("CHECKED_OUT") || random.nextBoolean()) {
                    bill.setClosedAt(bill.getOpenedAt().plusDays(random.nextInt(10)));
                }

                BigDecimal roomTotal = res.getBookedNightlyPrice().multiply(new BigDecimal(res.getNights()));
                bill.setTotalAmount(roomTotal);

                billService.save(bill);
                billCount++;

                // Add bill items
                BillItem billItem = new BillItem();
                billItem.setBill(bill);
                billItem.setItemType("ROOM_CHARGE");
                billItem.setDescription(res.getRoomType().getName() + " - " + res.getNights() + " night(s)");
                billItem.setQuantity(res.getNights());
                billItem.setUnitPrice(res.getBookedNightlyPrice());
                billItem.setLineTotal(roomTotal);
                billItem.setPostedAt(bill.getOpenedAt());
                billItemService.save(billItem);
                billItemCount++;

                // Add extra service items (random)
                if (random.nextBoolean() && billItemCount < 120) {
                    BillItem serviceItem = new BillItem();
                    serviceItem.setBill(bill);
                    serviceItem.setItemType("EXTRA_SERVICE");
                    serviceItem.setDescription("Additional service charge");
                    serviceItem.setQuantity(random.nextInt(5) + 1);
                    serviceItem.setUnitPrice(new BigDecimal(10 + random.nextInt(100)));
                    serviceItem.setLineTotal(serviceItem.getUnitPrice().multiply(new BigDecimal(serviceItem.getQuantity())));
                    serviceItem.setPostedAt(bill.getOpenedAt().plusHours(random.nextInt(24)));
                    billItemService.save(serviceItem);
                    billItemCount++;
                }
            }
        }
        System.out.println("✓ Created " + billCount + " bills");
        System.out.println("✓ Created " + billItemCount + " bill items (100+ required)");
    }

    private void initializeRoomCleaningTasks() {
        List<Room> rooms = roomService.findAllList();
        List<Cleaner> cleaners = cleanerService.findAll();

        int taskCount = 0;
        for (int i = 0; i < 120; i++) {
            RoomCleaningTask task = new RoomCleaningTask();
            task.setRoom(rooms.get(i % rooms.size()));
            task.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
            task.setTaskStatus(taskStatuses[random.nextInt(taskStatuses.length)]);
            task.setNote("Cleaning task " + (i + 1) + " for room " + task.getRoom().getRoomNumber());
            roomCleaningTaskService.save(task);
            taskCount++;

            // Add assignment
            Cleaner cleaner = cleaners.get(i % cleaners.size());
            RoomCleaningAssignment assignment = new RoomCleaningAssignment();
            RoomCleaningAssignmentKey assignmentKey = new RoomCleaningAssignmentKey(task.getTaskId(), cleaner.getCleanerId());
            assignment.setId(assignmentKey);
            assignment.setTask(task);
            assignment.setCleaner(cleaner);
            assignment.setAssignedAt(task.getCreatedAt());
            roomCleaningAssignmentService.save(assignment);
        }
        System.out.println("✓ Created " + taskCount + " room cleaning tasks (100+ required)");
        System.out.println("✓ Created " + taskCount + " room cleaning assignments (100+ required)");
    }

    private void printDataStats() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 DATA INITIALIZATION SUMMARY");
        System.out.println("=".repeat(50));
        System.out.println("Cleaners: " + cleanerService.findAll().size());
        System.out.println("Extra Services: " + extraServiceService.findAll().size());
        System.out.println("Inventory Items: " + inventoryItemService.findAll().size());
        System.out.println("Room Types: " + roomTypeService.findAll().size());
        System.out.println("Season Rates: " + seasonRateService.findAll().size());
        System.out.println("Rooms: " + roomService.findAllList().size());
        System.out.println("Guests: " + guestService.findAllList().size());
        System.out.println("Reservations: " + reservationService.findAllList().size());
        System.out.println("Bills: " + billService.findAllList().size());
        System.out.println("Bill Items: " + billItemService.findAll().size());
        System.out.println("Room Cleaning Tasks: " + roomCleaningTaskService.findAll().size());
        System.out.println("Room Cleaning Assignments: " + roomCleaningAssignmentService.findAll().size());
        System.out.println("=".repeat(50) + "\n");
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
