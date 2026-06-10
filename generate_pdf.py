from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import cm
from reportlab.lib import colors
from reportlab.platypus import (SimpleDocTemplate, Paragraph, Spacer, Table,
                                 TableStyle, PageBreak, HRFlowable, Preformatted)
from reportlab.lib.enums import TA_LEFT, TA_CENTER
import os

OUTPUT = os.path.join(os.path.dirname(__file__), "Eksamen_Database_Guide.pdf")

doc = SimpleDocTemplate(
    OUTPUT,
    pagesize=A4,
    rightMargin=2*cm, leftMargin=2*cm,
    topMargin=2*cm, bottomMargin=2*cm
)

W = A4[0] - 4*cm  # usable width

styles = getSampleStyleSheet()

# Custom styles
H1 = ParagraphStyle("H1", parent=styles["Heading1"], fontSize=18, spaceAfter=8,
                    textColor=colors.HexColor("#1a1a2e"), spaceBefore=12)
H2 = ParagraphStyle("H2", parent=styles["Heading2"], fontSize=13, spaceAfter=5,
                    textColor=colors.HexColor("#16213e"), spaceBefore=10, borderPad=2)
H3 = ParagraphStyle("H3", parent=styles["Heading3"], fontSize=11, spaceAfter=4,
                    textColor=colors.HexColor("#0f3460"), spaceBefore=8)
BODY = ParagraphStyle("Body", parent=styles["Normal"], fontSize=9.5, leading=14,
                      spaceAfter=5)
BULLET = ParagraphStyle("Bullet", parent=BODY, leftIndent=12, bulletIndent=4)
CODE_STYLE = ParagraphStyle("Code", parent=styles["Normal"], fontSize=8,
                             fontName="Courier", backColor=colors.HexColor("#f4f4f4"),
                             leftIndent=8, rightIndent=8, leading=11,
                             spaceAfter=6, spaceBefore=4, borderColor=colors.HexColor("#dddddd"))
NOTE = ParagraphStyle("Note", parent=BODY, textColor=colors.HexColor("#555555"),
                      fontName="Helvetica-Oblique")

def h1(t): return Paragraph(t, H1)
def h2(t): return Paragraph(t, H2)
def h3(t): return Paragraph(t, H3)
def p(t):  return Paragraph(t, BODY)
def note(t): return Paragraph(t, NOTE)
def sp(n=1): return Spacer(1, n*0.3*cm)
def hr(): return HRFlowable(width="100%", thickness=0.5, color=colors.HexColor("#cccccc"), spaceAfter=6)

def code(text):
    lines = text.strip().split("\n")
    formatted = "\n".join(lines)
    return Preformatted(formatted, CODE_STYLE)

def bullet(items):
    out = []
    for i in items:
        out.append(Paragraph(f"• {i}", BULLET))
    return out

def table(data, col_widths=None, header=True):
    t = Table(data, colWidths=col_widths)
    style_cmds = [
        ("FONTNAME", (0,0), (-1,-1), "Helvetica"),
        ("FONTSIZE", (0,0), (-1,-1), 8.5),
        ("ROWBACKGROUNDS", (0,0), (-1,-1), [colors.white, colors.HexColor("#f8f9fa")]),
        ("GRID", (0,0), (-1,-1), 0.3, colors.HexColor("#cccccc")),
        ("VALIGN", (0,0), (-1,-1), "MIDDLE"),
        ("LEFTPADDING", (0,0), (-1,-1), 6),
        ("RIGHTPADDING", (0,0), (-1,-1), 6),
        ("TOPPADDING", (0,0), (-1,-1), 4),
        ("BOTTOMPADDING", (0,0), (-1,-1), 4),
    ]
    if header:
        style_cmds += [
            ("BACKGROUND", (0,0), (-1,0), colors.HexColor("#1a1a2e")),
            ("TEXTCOLOR", (0,0), (-1,0), colors.white),
            ("FONTNAME", (0,0), (-1,0), "Helvetica-Bold"),
        ]
    t.setStyle(TableStyle(style_cmds))
    return t

# ─────────────────────────────────────────────
story = []

# ── TITLE PAGE ──────────────────────────────
story.append(Spacer(1, 3*cm))
story.append(Paragraph("Hotel Management Backend", ParagraphStyle("Title", parent=H1,
    fontSize=26, alignment=TA_CENTER, textColor=colors.HexColor("#1a1a2e"))))
story.append(Spacer(1, 0.5*cm))
story.append(Paragraph("Komplet Eksamensguide — Databases for Developers",
    ParagraphStyle("Sub", parent=BODY, fontSize=13, alignment=TA_CENTER,
                   textColor=colors.HexColor("#0f3460"))))
story.append(Spacer(1, 0.4*cm))
story.append(Paragraph("Torsdag — Eksamen",
    ParagraphStyle("Sub2", parent=BODY, fontSize=11, alignment=TA_CENTER,
                   textColor=colors.HexColor("#555555"))))
story.append(Spacer(1, 2*cm))
story.append(hr())
story.append(Spacer(1, 0.5*cm))
story.append(Paragraph("Indhold", ParagraphStyle("TOC", parent=H2, alignment=TA_CENTER)))
toc_items = [
    "DEL 1 — Systemarkitektur",
    "DEL 2 — MySQL: Relational Database Design",
    "DEL 3 — Normalisering: 1NF, 2NF, 3NF",
    "DEL 4 — MongoDB Design",
    "DEL 5 — Neo4j Graph Model",
    "DEL 6 — Cross-Database Query",
    "DEL 7 — Transaktioner og ACID",
    "DEL 8 — Indexes og Performance",
    "DEL 9 — Security og Auditing",
    "DEL 10 — Stored Procedures, Views og Events",
    "DEL 11 — Trade-offs og Refleksioner",
    "DEL 12 — Fagtermer Quick Reference",
    "DEL 13 — Mundtlige Spørgsmål og Svar",
]
for item in toc_items:
    story.append(Paragraph(f"  {item}", BULLET))
    story.append(Spacer(1, 0.1*cm))
story.append(PageBreak())

# ── DEL 1: SYSTEM OVERVIEW ──────────────────
story.append(h1("DEL 1 — Systemarkitektur"))
story.append(hr())
story.append(p("Dit system er en <b>Hotel Management Backend</b> bygget med Spring Boot 4. "
               "Applikationen bruger tre databaser parallelt til at løse tre forskellige problemer:"))
story.append(sp())
story.append(code("""\
Frontend  →  Spring Boot App (port 8080)
                  ↓  JWT authentication (BCrypt)
     ┌────────────┼────────────┐
   MySQL       MongoDB       Neo4j
 (port 3307) (port 27017) (port 7687)
 Struktureret  Dokumenter   Grafer
"""))
story.append(sp())

story.extend(bullet([
    "<b>MySQL</b> — kilden til sandheden. Strukturerede, relationelle data med ACID-garantier. "
    "Bruges til reservationer, gæster, rum, priser og billing.",
    "<b>MongoDB</b> — kopi med embedded dokumenter. En gæsts fulde historik i ét kald. "
    "Bruges også til AI-interaktions-logs (ustrukturerede LLM-samtaler).",
    "<b>Neo4j</b> — relation-grafen. Beskriver forbindelser: hvem bookede hvad, "
    "hvilke cleaners er assigned til hvilke rum.",
]))
story.append(sp())
story.append(p("<b>Migration:</b> Data starter i MySQL og kopieres manuelt til MongoDB og Neo4j "
               "via <code>POST /api/migrate</code>. I produktion ville man bruge event-driven sync."))
story.append(PageBreak())

# ── DEL 2: MYSQL ────────────────────────────
story.append(h1("DEL 2 — MySQL: Relational Database Design"))
story.append(hr())

story.append(h2("Hvad er en relationel database?"))
story.append(p("En relationel database gemmer data i <b>tabeller</b> med <b>rækker</b> og <b>kolonner</b>. "
               "Tabeller relaterer til hinanden via nøgler. Databasemotoren (InnoDB i jeres tilfælde) "
               "håndhæver regler automatisk."))
story.append(sp())

story.append(h2("Primary Key (PK) — Primær nøgle"))
story.append(p("En <b>primær nøgle</b> identificerer unikt én enkelt række. Ingen to rækker har samme PK. "
               "<code>AUTO_INCREMENT</code> lader databasen selv tælle (1, 2, 3...)."))
story.append(code("guest_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY"))
story.append(sp())

story.append(h2("Foreign Key (FK) — Fremmed nøgle"))
story.append(p("En <b>fremmed nøgle</b> peger på en PK i en anden tabel og skaber relationen. "
               "Databasen afviser operationer der bryder referencen."))
story.append(code("-- I reservation-tabellen:\nFOREIGN KEY (guest_id) REFERENCES guest(guest_id) ON DELETE CASCADE"))
story.append(p("<b>ON DELETE CASCADE:</b> Slettes gæsten, slettes alle gæstens reservationer automatisk.<br/>"
               "<b>ON DELETE SET NULL:</b> Slettes gæsten, sættes FK til NULL.<br/>"
               "<b>ON DELETE RESTRICT:</b> Kan ikke slette gæsten mens der er reservationer."))
story.append(sp())

story.append(h2("Kardinalitet"))
story.append(p("Kardinalitet beskriver <i>hvor mange</i> af den ene type der relaterer til <i>hvor mange</i> af den anden:"))
story.append(sp(0.5))
story.append(table([
    ["Type", "Beskrivelse", "Eksempel i jeres system"],
    ["1:1", "Én til én", "reservation ↔ bill (én bill per reservation, UNIQUE FK)"],
    ["1:N", "Én til mange", "room_type → mange rooms"],
    ["M:N", "Mange til mange", "reservation ↔ guest (via reservation_guest junction)"],
], col_widths=[2*cm, 3.5*cm, W-5.5*cm]))
story.append(sp())

story.append(h2("Constraints — Begrænsninger"))
story.append(table([
    ["Constraint", "Hvad det gør", "Eksempel"],
    ["PRIMARY KEY", "Unik + NOT NULL", "guest_id PRIMARY KEY"],
    ["UNIQUE", "Ingen duplikater tilladt", "email VARCHAR UNIQUE"],
    ["NOT NULL", "Feltet må ikke være tomt", "check_in_date NOT NULL"],
    ["FOREIGN KEY", "Refereret række skal eksistere", "room_type_id FK→room_type"],
    ["ENUM", "Kun specificerede værdier", "role ENUM('ADMIN','STAFF','CLEANER')"],
    ["DEFAULT", "Standardværdi", "occupied BIT DEFAULT 0"],
], col_widths=[3*cm, 5*cm, W-8*cm]))
story.append(sp())

story.append(h2("Jeres 13 tabeller"))
story.append(table([
    ["Tabel", "PK", "Vigtige FK'er", "Formål"],
    ["user_account", "id (BIGINT)", "—", "Login, roller"],
    ["guest", "guest_id (BIGINT)", "—", "Gæstinfo"],
    ["room_type", "room_type_id (INT)", "—", "Single/Double/Suite"],
    ["season_rate", "rate_id (INT)", "room_type_id", "Priser per sæson"],
    ["room", "room_id (INT)", "room_type_id", "Individuelle rum"],
    ["reservation", "reservation_id (INT)", "guest_id, room_id, room_type_id, booked_rate_id", "Bookinger"],
    ["reservation_guest", "(reservation_id, guest_id)", "begge FK", "M:N junction"],
    ["bill", "bill_id (INT)", "reservation_id (UNIQUE)", "1:1 med reservation"],
    ["bill_item", "bill_item_id (INT)", "bill_id", "Regningslinjer"],
    ["cleaner", "cleaner_id (INT)", "—", "Rengøringspersonale"],
    ["room_cleaning_task", "task_id (INT)", "room_id", "Rengøringsopgaver"],
    ["room_cleaning_assignment", "(task_id, cleaner_id)", "begge FK", "M:N junction"],
    ["extra_service", "extra_service_id (INT)", "—", "Ekstraydelser"],
], col_widths=[3.5*cm, 3*cm, 5.5*cm, W-12*cm]))
story.append(PageBreak())

# ── DEL 3: NORMALISERING ────────────────────
story.append(h1("DEL 3 — Normalisering: 1NF, 2NF, 3NF"))
story.append(hr())
story.append(p("<b>Normalisering</b> er processen med at organisere din database for at undgå:"))
story.extend(bullet([
    "<b>Redundans</b> — samme data gemt flere steder (f.eks. rumtype-navn gentaget i alle rum)",
    "<b>Update anomaly</b> — opdaterer ét sted, glemmer et andet",
    "<b>Insertion anomaly</b> — kan ikke tilføje fornuftigt data",
    "<b>Deletion anomaly</b> — sletter én ting, mister utilsigtet andet",
]))
story.append(sp())

story.append(h2("1NF — First Normal Form"))
story.append(p("<b>Regel:</b> Hvert felt indeholder kun én <b>atomisk</b> (udelelig) værdi. "
               "Ingen lister, ingen arrays, ingen komma-separerede værdier i ét felt."))
story.append(sp(0.5))
story.append(p("<b>IKKE 1NF (dårligt design):</b>"))
story.append(code("guest_id | name   | rooms_booked\n1        | Magnus | \"101, 102, 105\"   ← FORKERT: liste i ét felt"))
story.append(p("<b>Jeres design (1NF overholdes):</b> Reservationer er i sin <i>egen</i> tabel med "
               "én reservation per række. <code>credit_card_last4</code> gemmer kun de 4 sidste "
               "cifre — en atomisk værdi, ikke hele kortnummeret."))
story.append(sp())

story.append(h2("2NF — Second Normal Form"))
story.append(p("<b>Regel:</b> Forudsætter 1NF, <i>plus</i>: alle ikke-nøgle-felter skal afhænge af "
               "<b>hele</b> primærnøglen. Relevant når du har <b>composite primary keys</b>."))
story.append(sp(0.5))
story.append(p("<b>Eksempel fra jeres junction-tabel:</b>"))
story.append(code("-- reservation_guest har composite PK:\nPK = (reservation_id, guest_id)\nis_primary  ← afhænger af BEGGE kolonner ✓\n\n-- IKKE 2NF (dårligt):\nguest_email ← afhænger KUN af guest_id, ikke reservation_id ✗"))
story.append(p("Løsning: <code>guest_email</code> hører til i <code>guest</code>-tabellen."))
story.append(sp())

story.append(h2("3NF — Third Normal Form"))
story.append(p("<b>Regel:</b> Forudsætter 2NF, <i>plus</i>: ingen <b>transitive afhængigheder</b>. "
               "Det vil sige: ikke-nøgle-felter må ikke afhænge af <i>andre</i> ikke-nøgle-felter."))
story.append(p("En <b>transitiv afhængighed</b> er: A → B → C, "
               "hvor C afhænger af B som afhænger af PK (A). C burde flyttes til sin egen tabel."))
story.append(sp(0.5))
story.append(p("<b>Dårligt design (IKKE 3NF):</b>"))
story.append(code("reservation(reservation_id PK, room_type_id, room_type_name, max_occupancy)"))
story.append(p("Her: <code>room_type_name</code> og <code>max_occupancy</code> afhænger af "
               "<code>room_type_id</code>, ikke af <code>reservation_id</code> direkte. "
               "Det er en transitiv afhængighed."))
story.append(sp(0.5))
story.append(p("<b>Jeres faktiske 3NF-design:</b>"))
story.append(code("reservation(reservation_id PK, room_type_id FK, ...)\nroom_type  (room_type_id PK, name, max_occupancy)  ← eget bord"))
story.append(sp())
story.append(p("<b>Snapshot-undtagelse (vigtig at forklare!):</b> "
               "<code>booked_nightly_price</code> gemmes direkte på <code>reservation</code> selvom "
               "prisen også findes i <code>season_rate</code>. "
               "Det er en <i>bevidst designbeslutning</i>: prisen på bookingstidspunktet skal bevares, "
               "selvom sæsonprisen ændrer sig næste år. Det er et <b>historisk snapshot</b>, "
               "ikke en normaliseringsovertrædelse."))
story.append(PageBreak())

# ── DEL 4: MONGODB ──────────────────────────
story.append(h1("DEL 4 — MongoDB Design"))
story.append(hr())

story.append(h2("Hvad er MongoDB?"))
story.append(p("MongoDB er en <b>dokumentdatabase</b> (NoSQL). I stedet for tabeller med rækker "
               "gemmer den <b>dokumenter</b> (JSON-lignende) i <b>collections</b>. "
               "Der er intet fast schema — hvert dokument kan have forskellige felter."))
story.append(sp(0.5))
story.append(table([
    ["MySQL", "MongoDB"],
    ["Database", "Database"],
    ["Tabel", "Collection"],
    ["Række", "Document"],
    ["Kolonne", "Field"],
    ["JOIN", "$lookup (eller embedding)"],
    ["Schema required", "Schema-free (fleksibelt)"],
], col_widths=[W/2, W/2]))
story.append(sp())

story.append(h2("Embedding vs. Referencing"))
story.append(p("Det centrale designvalg i MongoDB: skal relaterede data <b>embeddes</b> "
               "(kopieres ind i dokumentet) eller <b>refereres</b> (gemmes separat med et ID)?"))
story.append(sp())

story.append(h3("Embedded — gæst med reservationer (jeres valg)"))
story.append(code("""\
{
  "_id": "...",
  "guestId": 5,
  "firstName": "Magnus",
  "email": "magnus@hotel.dk",
  "reservations": [             ← EMBEDDED i guest-dokumentet
    {
      "reservationId": 42,
      "referenceNo": "RES-2024-001",
      "checkInDate": "2024-06-01",
      "roomType": "Double",
      "bills": [                ← EMBEDDED i reservation
        {
          "billId": 1,
          "totalAmount": 2400.00,
          "items": [
            { "itemType": "ROOM", "description": "2 nætter", "unitPrice": 1200 }
          ]
        }
      ]
    }
  ]
}"""))
story.append(p("<b>Fordel:</b> \"Hent alt om gæst Magnus\" = ét enkelt database-kald, ingen joins.<br/>"
               "<b>Ulempe:</b> Opdateres en reservation, skal den opdateres to steder."))
story.append(sp())

story.append(h3("Referenced — separate collections"))
story.append(p("<code>rooms</code>, <code>cleaners</code>, <code>season_rates</code> er "
               "<i>separate</i> collections med references via ID. Disse embeddes <i>ikke</i> i "
               "guest-dokumentet fordi:"))
story.extend(bullet([
    "Et room eksisterer uafhængigt af reservationer",
    "Mange reservationer deler samme room_type — embedding ville skabe massiv redundans",
    "Rooms opdateres hyppigt (status, clean_status)",
]))
story.append(sp())

story.append(h3("ai_interactions — kun MongoDB"))
story.append(code("""\
{
  "sessionId": "abc123",
  "userMessage": "Hvad koster et dobbeltværelse i juli?",
  "aiResponse": "Et dobbeltværelse koster...",
  "model": "llama3",
  "tokensUsed": 245,
  "timestamp": "2024-06-01T14:30:00"
}"""))
story.append(p("LLM-samtaler gemmes <i>kun</i> i MongoDB — de har ingen fast struktur, "
               "og der er ingen relationer der kræver ACID."))
story.append(PageBreak())

# ── DEL 5: NEO4J ────────────────────────────
story.append(h1("DEL 5 — Neo4j Graph Model"))
story.append(hr())

story.append(h2("Terminologi"))
story.append(table([
    ["Term", "Beskrivelse", "Eksempel"],
    ["Node", "Et objekt i grafen", "(:Guest), (:Room), (:Reservation)"],
    ["Relationship", "Forbindelsen mellem to noder", "[:BOOKED_BY], [:ASSIGNED_TO]"],
    ["Label", "Typen af en node", ":Guest, :Room, :Bill"],
    ["Property", "Egenskab på node eller relation", "firstName, roomNumber, checkIn"],
    ["Direction", "Relationer har retning (pil)", "Guest →BOOKED_BY→ Reservation"],
    ["Traversal", "At navigere langs piler i grafen", "Hop fra guest → reservation → room"],
    ["Cypher", "Neo4js query-sprog", "MATCH (g:Guest)-[:BOOKED_BY]->(r:Reservation)"],
], col_widths=[3*cm, 5*cm, W-8*cm]))
story.append(sp())

story.append(h2("Jeres graph model"))
story.append(code("""\
(:Guest {guestId, firstName, lastName, email})
    -[:BOOKED_BY]→
(:Reservation {reservationId, referenceNo, status, checkIn, checkOut, nights})
    -[:ASSIGNED_TO]→
(:Room {roomId, roomNumber, roomStatus, cleanStatus, occupied})
    -[:IS_TYPE]→
(:RoomType {name, maxOccupancy})
    -[:HAS_RATE]→
(:SeasonRate {season, pricePerNight})

(:Reservation)-[:HAS_BILL]→(:Bill)-[:CONTAINS_ITEM]→(:BillItem)

(:Room)-[:HAS_CLEANING_TASK]→(:RoomCleaningTask)
(:Cleaner)-[:ASSIGNED_TO]→(:RoomCleaningTask)"""))
story.append(sp())

story.append(h2("Hvorfor Neo4j frem for SQL til visse queries?"))
story.append(p("Spørgsmål: <i>\"Hvilke cleaners rengør rum der er booket af gæster med 3+ reservationer?\"</i>"))
story.append(code("""\
-- SQL: kræver 4 JOINs + subquery
SELECT DISTINCT c.first_name, c.last_name
FROM cleaner c
JOIN room_cleaning_assignment rca ON c.cleaner_id = rca.cleaner_id
JOIN room_cleaning_task rct ON rca.task_id = rct.task_id
JOIN room r ON rct.room_id = r.room_id
JOIN reservation res ON r.room_id = res.room_id
WHERE res.guest_id IN (
    SELECT guest_id FROM reservation GROUP BY guest_id HAVING COUNT(*) >= 3
);

-- Cypher: naturlig traversal langs piler
MATCH (g:Guest)-[:BOOKED_BY]->(res:Reservation)-[:ASSIGNED_TO]->(r:Room)
      -[:HAS_CLEANING_TASK]->(task:RoomCleaningTask)
      <-[:ASSIGNED_TO]-(c:Cleaner)
WHERE size((g)-[:BOOKED_BY]->()) >= 3
RETURN DISTINCT c.firstName, c.lastName"""))
story.append(PageBreak())

# ── DEL 6: CROSS-DB QUERY ───────────────────
story.append(h1("DEL 6 — Cross-Database Query"))
story.append(hr())
story.append(p("<b>Scenario:</b> Find alle CONFIRMED reservationer for rum 101"))
story.append(sp())

story.append(h2("MySQL"))
story.append(code("""\
SELECT g.first_name, g.last_name, r.reference_no,
       r.check_in_date, r.check_out_date
FROM reservation r
JOIN guest g ON r.guest_id = g.guest_id
JOIN room rm ON r.room_id = rm.room_id
WHERE rm.room_number = '101'
  AND r.status = 'CONFIRMED';"""))
story.append(p("<b>JOIN</b> kombinerer rækker fra to tabeller baseret på et fælles felt. "
               "<b>WHERE</b> filtrerer resultater."))
story.append(sp())

story.append(h2("MongoDB"))
story.append(code("""\
db.guests.aggregate([
  { $unwind: "$reservations" },      // fold reservations-array ud
  { $match: {
      "reservations.assignedRoomNumber": "101",
      "reservations.status": "CONFIRMED"
  }},
  { $project: {
      firstName: 1, lastName: 1,
      "reservations.referenceNo": 1,
      "reservations.checkInDate": 1
  }}
])"""))
story.append(p("<b>$unwind:</b> \"folder\" et embedded array ud til individuelle dokumenter.<br/>"
               "<b>$match:</b> filtrerer (som WHERE).<br/>"
               "<b>$project:</b> vælger felter (som SELECT).<br/>"
               "<b>aggregate:</b> en pipeline af operationer der kører i sekvens."))
story.append(sp())

story.append(h2("Neo4j (Cypher)"))
story.append(code("""\
MATCH (g:Guest)-[:BOOKED_BY]->(res:Reservation)-[:ASSIGNED_TO]->(r:Room)
WHERE r.roomNumber = '101' AND res.status = 'CONFIRMED'
RETURN g.firstName, g.lastName, res.referenceNo, res.checkIn"""))
story.append(p("<b>MATCH:</b> definerer mønsteret du leder efter i grafen.<br/>"
               "<b>WHERE:</b> filtrerer på properties.<br/>"
               "<b>RETURN:</b> specificerer hvad der returneres."))
story.append(sp())

story.append(h2("Sammenligning"))
story.append(table([
    ["", "MySQL", "MongoDB", "Neo4j"],
    ["Syntaks", "Tabelbaseret, eksplicit JOIN", "Pipeline-orienteret", "Mønstertilpasning"],
    ["Bedst til", "Strukturerede relationer", "Hierarkiske dokumenter", "Mange-hop traversals"],
    ["Skalering", "Vertikal", "Horisontal (sharding)", "Horisontal (clustering)"],
    ["ACID", "Fuld", "Dokument-niveau", "Fuld (enterprise)"],
    ["Query-sprog", "SQL", "MQL / aggregation", "Cypher"],
], col_widths=[2.5*cm, (W-2.5*cm)/3, (W-2.5*cm)/3, (W-2.5*cm)/3]))
story.append(PageBreak())

# ── DEL 7: TRANSAKTIONER ────────────────────
story.append(h1("DEL 7 — Transaktioner og ACID"))
story.append(hr())

story.append(h2("Hvad er en transaktion?"))
story.append(p("En <b>transaktion</b> er en gruppe af database-operationer der enten "
               "<b>alle lykkes</b> eller <b>alle fejler</b> — aldrig halvt. "
               "Det er som en atomisk handling der ikke kan afbrydes midt i."))
story.append(sp())

story.append(h2("Konkret eksempel: Gæst tjekker ind"))
story.append(p("Disse tre operationer skal alle lykkes eller alle rulles tilbage:"))
story.append(code("""\
BEGIN;  -- start transaktion

  UPDATE reservation
  SET status = 'CHECKED_IN'
  WHERE reservation_id = 42;          -- trin 1

  UPDATE room
  SET occupied = 1, room_status = 'OCCUPIED'
  WHERE room_id = 15;                 -- trin 2

  INSERT INTO bill (reservation_id, opened_at, total_amount)
  VALUES (42, NOW(), 0.00);           -- trin 3 — åbn regning

COMMIT;   -- gem alt permanent

-- Hvis noget fejler:
ROLLBACK; -- fortryd ALLE tre operationer"""))
story.append(p("I Spring Boot sker dette automatisk med <code>@Transactional</code> på service-klassen."))
story.append(sp())

story.append(h2("ACID — de fire garantier"))
story.append(table([
    ["Bogstav", "Navn", "Betyder", "Eksempel"],
    ["A", "Atomicity", "Enten alt eller intet", "Check-in: alle 3 steps lykkes eller ingen"],
    ["C", "Consistency", "Fra valid til valid tilstand", "FK-constraints valideres — kan ikke booke ikke-eksisterende rum"],
    ["I", "Isolation", "Concurrent transaktioner ser ikke hinandens uncommitted data", "To gæster booker samme rum — kun én lykkes"],
    ["D", "Durability", "Committed data forbliver gemt", "Server crasher efter COMMIT — data er stadig der"],
], col_widths=[1*cm, 2.5*cm, 5*cm, W-8.5*cm]))
story.append(PageBreak())

# ── DEL 8: INDEXES ──────────────────────────
story.append(h1("DEL 8 — Indexes og Performance"))
story.append(hr())

story.append(h2("Hvad er et index?"))
story.append(p("Et <b>index</b> er en separat datastruktur der gør søgning hurtigere. "
               "Tænk på det som en bogs indeks bagerst: i stedet for at læse alle sider "
               "slår du direkte op og får sidenummeret."))
story.extend(bullet([
    "<b>Uden index:</b> Full table scan — databasen gennemgår alle rækker én efter én → langsomt",
    "<b>Med index:</b> Springer direkte til rækken → O(log n) i stedet for O(n)",
]))
story.append(sp())

story.append(h2("B-tree index"))
story.append(p("Den mest almindelige index-type. Balanced Tree — sorteret hierarkisk struktur. "
               "God til:"))
story.extend(bullet([
    "Eksakt match: <code>WHERE email = 'magnus@hotel.dk'</code>",
    "Range queries: <code>WHERE check_in_date BETWEEN '2024-01-01' AND '2024-12-31'</code>",
    "Sortering: <code>ORDER BY last_name</code>",
]))
story.append(sp())

story.append(h2("Composite index"))
story.append(p("Et <b>composite index</b> dækker <i>flere kolonner</i>. "
               "Rækkefølgen af kolonner har betydning — søg altid på den første kolonne."))
story.append(code("INDEX idx_reservation_dates (check_in_date, check_out_date)\n-- Dækker: WHERE check_in_date = X (effektivt)\n-- Dækker: WHERE check_in_date = X AND check_out_date = Y (effektivt)\n-- Dækker IKKE: WHERE check_out_date = Y alene (ineffektivt)"))
story.append(sp())

story.append(h2("Jeres indexes fra 01_database_create.sql"))
story.append(table([
    ["Tabel", "Index", "Type", "Begrundelse"],
    ["guest", "idx_guest_email", "B-tree", "Login-lookup — hyppigt brugt"],
    ["guest", "idx_guest_name (last_name, first_name)", "Composite", "Søgning på navn"],
    ["reservation", "idx_reservation_dates", "Composite", "Dato-range queries (ledige rum)"],
    ["reservation", "idx_reservation_status", "B-tree", "Filter: alle CONFIRMED"],
    ["reservation", "idx_reservation_guest", "B-tree", "FK join: gæstens reservationer"],
    ["bill", "idx_bill_reservation", "B-tree", "FK join performance"],
    ["room", "idx_room_status + idx_room_clean_status", "B-tree", "Filtrer ledige/beskidte rum"],
    ["audit_log", "idx_audit_table/operation/timestamp", "B-tree", "Hurtig audit-søgning"],
], col_widths=[2.5*cm, 5.5*cm, 2*cm, W-10*cm]))
story.append(sp())
story.append(p("<b>Hvad I IKKE indexerede:</b> <code>bill_item.description</code> — "
               "tekstsøgning bruges ikke, og et index ville sænke inserts unødigt. "
               "<code>occupied BIT</code> — kun 2 værdier (lav cardinality), "
               "databasen henter alligevel ~50% af rækkerne."))
story.append(PageBreak())

# ── DEL 9: SECURITY ─────────────────────────
story.append(h1("DEL 9 — Security og Auditing"))
story.append(hr())

story.append(h2("JWT — JSON Web Token"))
story.append(p("JWT er en måde at bevise identitet uden at serveren skal huske hvem du er "
               "(<b>stateless</b>). En JWT består af tre dele separeret af punktum:"))
story.extend(bullet([
    "<b>Header</b> — algoritme brugt til signering (HS512 i jeres kode)",
    "<b>Payload</b> — brugerens data: username, rolle, udløbstid",
    "<b>Signature</b> — kryptografisk signatur der beviser tokenet ikke er manipuleret",
]))
story.append(sp(0.5))
story.append(code("""\
// Flow:
1. POST /api/auth/login  { username: "admin", password: "admin123" }
2. Server tjekker BCrypt-hash mod user_account-tabellen
3. Server returnerer: { token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbi..." }
4. Klienten sender: Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
5. Server verificerer signatur — ingen database-kald nødvendigt!"""))
story.append(sp())

story.append(h2("BCrypt — Password Hashing"))
story.append(p("<b>Passwords gemmes aldrig i plain text.</b> BCrypt er en one-way hash-funktion:"))
story.append(code('"admin123"  →  BCrypt  →  "$2a$10$N9qo8uLOickgx2ZMRZoMye..."'))
story.append(p("<b>One-way:</b> Kan ikke gå baglæns fra hash til password.<br/>"
               "<b>Salt:</b> BCrypt tilføjer automatisk random data til hvert password — "
               "to brugere med samme password får <i>forskellige</i> hashes."))
story.append(sp())

story.append(h2("Roller og adgangskontrol"))
story.append(table([
    ["Rolle", "Adgang"],
    ["ADMIN", "Fuld adgang — kan slette, oprette, se alt"],
    ["STAFF", "Kan se og opdatere reservationer, men ikke slette"],
    ["CLEANER", "Kan kun se og opdatere cleaning tasks"],
], col_widths=[3*cm, W-3*cm]))
story.append(sp())

story.append(h2("SQL Injection prevention"))
story.append(p("<b>SQL Injection</b> er et angreb hvor ondsindet input manipulerer SQL-queries:"))
story.append(code("""\
// FARLIGT — brug aldrig string-konkatenering:
String q = "SELECT * FROM guest WHERE email = '" + email + "'";
// Angreb: email = "' OR '1'='1"  → returnerer ALLE gæster!

// SIKKERT — parameterized query via Spring Data JPA:
Optional<Guest> findByEmail(String email);
// Genererer:  SELECT * FROM guest WHERE email = ?
//             email-værdien sendes separat — behandles som DATA, ikke kode"""))
story.append(sp())

story.append(h2("Auditing via Triggers (05_audit.sql)"))
story.append(p("En <b>trigger</b> er SQL-kode der automatisk kører ved INSERT/UPDATE/DELETE — "
               "applikationen behøver ikke aktivt at gøre noget."))
story.append(p("<b>audit_log</b> tabellen logger ændringer på: "
               "<code>reservation</code>, <code>bill</code>, <code>bill_item</code>, "
               "<code>guest</code>, <code>room</code>."))
story.append(sp(0.5))
story.append(p("Hvert audit-entry gemmer:"))
story.append(table([
    ["Felt", "Indhold"],
    ["table_name", "Hvilken tabel der ændrede sig"],
    ["operation_type", "INSERT / UPDATE / DELETE"],
    ["record_id", "ID på den ændrede record"],
    ["old_values (JSON)", "Hvad stod der FØR ændringen"],
    ["new_values (JSON)", "Hvad er der NU"],
    ["changed_by", "MySQL-brugeren (CURRENT_USER())"],
    ["changed_at", "Tidsstempel for ændringen"],
], col_widths=[4*cm, W-4*cm]))
story.append(PageBreak())

# ── DEL 10: STORED PROCEDURES ETC ───────────
story.append(h1("DEL 10 — Stored Procedures, Views og Events"))
story.append(hr())

story.append(h2("Stored Procedure — sp_CalculateFinalBill"))
story.append(p("En <b>stored procedure</b> er foruddefineret SQL gemt i databasen der kan kaldes "
               "med parametre — som en funktion i programmering. Logikken er i databasen, "
               "ikke i applikationskoden."))
story.append(code("CALL sp_CalculateFinalBill(42);\n-- Returnerer: { final_bill_dkk: 3600.00 }"))
story.append(p("Proceduren: (1) henter nætter og nattepris fra reservation, "
               "(2) beregner rumtotal = nætter × pris, "
               "(3) summer ekstra services fra bill_item, "
               "(4) opdaterer bill.total_amount, (5) returnerer beløbet."))
story.append(sp())

story.append(h2("Stored Function — fn_GetRoomRate"))
story.append(p("En <b>stored function</b> returnerer én enkelt værdi og kan bruges direkte i SQL:"))
story.append(code("SELECT fn_GetRoomRate(2, 'Summer') AS daily_rate;\n-- Returnerer: 1200.00"))
story.append(sp())

story.append(h2("Views — virtuelle tabeller"))
story.append(p("Et <b>view</b> er en gemt SQL-query der opfører sig som en tabel. "
               "Det gemmer <i>ikke</i> data — beregner resultatet live."))
story.append(table([
    ["View", "Viser"],
    ["vw_HousekeepingList", "Alle beskidte/inspekterede rum med rum-nr og type"],
    ["vw_ReservationDetails", "Reservationer med gæst-info, rum-nr og estimeret pris"],
    ["vw_BillDetails", "Bills med gæst-navn, åbnet/lukket, total og antal linjer"],
    ["vw_recent_audit_logs", "Seneste 100 audit-entries med formaterede tidsstempler"],
], col_widths=[4.5*cm, W-4.5*cm]))
story.append(sp())

story.append(h2("Events — automatisk kørende SQL"))
story.append(p("Events er som cron jobs i databasen — SQL der kører på et tidsplan:"))
story.append(table([
    ["Event", "Kører", "Gør"],
    ["ev_AutoCancelStaleReservations", "Dagligt kl. 02:00", "Annullerer PENDING-reservationer ældre end 7 dage — triggerer også tr_RoomStatusUpdate"],
    ["ev_MonthlyCloseOldBills", "1. i måneden kl. 03:00", "Lukker bills der er åbne 30+ dage efter CHECKED_OUT"],
], col_widths=[5*cm, 3*cm, W-8*cm]))
story.append(sp())

story.append(h2("Triggers i jeres system"))
story.append(table([
    ["Trigger", "Hvornår", "Hvad den gør"],
    ["tr_AfterCheckout", "AFTER UPDATE reservation", "Sætter rum til 'Vacant'/'Dirty' ved checkout"],
    ["tr_RoomStatusUpdate", "AFTER UPDATE reservation", "Sætter rum OCCUPIED=1 ved CONFIRMED, AVAILABLE=0 ved CANCELLED"],
    ["tr_audit_*", "AFTER INSERT/UPDATE/DELETE", "Logger alle ændringer til audit_log"],
], col_widths=[4.5*cm, 4*cm, W-8.5*cm]))
story.append(PageBreak())

# ── DEL 11: TRADE-OFFS ──────────────────────
story.append(h1("DEL 11 — Trade-offs og Refleksioner"))
story.append(hr())

story.append(h2("Hvorfor MySQL til hotel-core data?"))
story.extend(bullet([
    "Reservationsdata er <b>struktureret og relationelt</b> — gæst booker rum til en pris, alt hænger logisk sammen",
    "<b>ACID-garantier</b> er kritiske: du kan ikke have en betaling uden en reservation",
    "Velkendt schema der sjældent ændrer sig",
    "Komplekse <b>JOIN-queries</b> er naturlige (vw_ReservationDetails bruger 4 tabeller)",
]))
story.append(sp())

story.append(h2("Hvorfor MongoDB?"))
story.extend(bullet([
    "En gæsts <b>fulde historik</b> i ét dokument-kald — ingen joins nødvendige",
    "<code>ai_interactions</code> er helt ustrukturerede LLM-logs — passer ikke i et fast schema",
    "<b>Fleksibelt</b> for fremtidige felter uden schema-migration (bare tilføj et nyt felt)",
    "God til read-heavy workloads med nested/hierarkisk data",
]))
story.append(sp())

story.append(h2("Hvorfor Neo4j?"))
story.extend(bullet([
    "Komplekse <b>relationsnavigation</b> er triviel (følg pilene) — ville kræve mange JOINs i SQL",
    "Visualisering af hotellets forbindelser giver operationelt overblik",
    "Kan udvides til fx <b>anbefalinger</b>: \"gæster der bookede dette rum bookede også...\"",
    "Graf-traversals skalerer bedre end rekursive SQL-queries ved dybe relationer",
]))
story.append(sp())

story.append(h2("Hvad I ville gøre anderledes"))
story.extend(bullet([
    "<b>Dato-typer i Neo4j:</b> Datoer gemtes som strings (\"2024-06-01\"). Neo4j har native "
    "<code>date</code> og <code>datetime</code>-typer der er mere effektive og kan bruges "
    "i Cypher-beregninger. Opdaget undervejs i projektet.",
    "<b>Sync-strategi:</b> Data migreres manuelt via POST /api/migrate. I produktion ville man "
    "bruge <b>outbox pattern</b> eller <b>Change Data Capture (CDC)</b> med Kafka, "
    "så ændringer propageres automatisk.",
    "<b>Multi-store Spring Boot:</b> Kræver eksplicit konfiguration af to transaction managers "
    "(JpaTransactionManager + Neo4jTransactionManager). Spring Boot 4's auto-configuration "
    "håndterer ikke dette automatisk — lærte dette undervejs.",
]))
story.append(PageBreak())

# ── DEL 12: FAGTERMER ───────────────────────
story.append(h1("DEL 12 — Fagtermer Quick Reference"))
story.append(hr())

terms = [
    ("Primary Key (PK)", "Unikt ID for en tabel-række — ingen duplikater"),
    ("Foreign Key (FK)", "Reference til en anden tabels PK — skaber relationen"),
    ("Kardinalitet", "1:1, 1:N, M:N — 'hvor mange til hvor mange'"),
    ("Junction table", "Mellemtabel for M:N relationer (reservation_guest)"),
    ("Composite PK", "Primærnøgle bestående af to+ kolonner"),
    ("Constraint", "Regel databasen håndhæver automatisk (NOT NULL, UNIQUE, FK)"),
    ("1NF", "Atomare felter — ingen lister i én kolonne"),
    ("2NF", "Alle felter afhænger af hele PK (relevant ved composite PK)"),
    ("3NF", "Ingen transitive afhængigheder (A→B→C flyttes til eget bord)"),
    ("Transitiv afhængighed", "Ikke-nøgle-felt afhænger af andet ikke-nøgle-felt"),
    ("Snapshot", "Historisk kopi af data på et givent tidspunkt (booked_nightly_price)"),
    ("Redundans", "Samme data gemt unødvendigt flere steder"),
    ("NoSQL", "Ikke-relationel database — MongoDB, Neo4j"),
    ("Document", "JSON-lignende record i MongoDB"),
    ("Collection", "Gruppe af dokumenter i MongoDB (svarende til tabel)"),
    ("Embedding", "Relaterede data kopieres ind i samme dokument"),
    ("Referencing", "ID-reference til et separat dokument"),
    ("Aggregation pipeline", "MongoDB: sekvens af operationer ($unwind, $match, $project)"),
    ("Node", "Et objekt i en grafmodel"),
    ("Relationship", "Forbindelse/kant mellem to noder i Neo4j"),
    ("Label", "Typen af en node (:Guest, :Room)"),
    ("Property", "Egenskab på node eller relation (firstName, checkIn)"),
    ("Traversal", "At navigere langs kanter i en graf"),
    ("Cypher", "Neo4js query-sprog (MATCH, WHERE, RETURN)"),
    ("Transaktion", "Gruppe af operationer — alle lykkes eller alle fejler"),
    ("ACID", "Atomicity, Consistency, Isolation, Durability"),
    ("Atomicity", "Enten alt eller intet"),
    ("Consistency", "Fra valid til valid tilstand"),
    ("Isolation", "Concurrent transaktioner ser ikke hinandens uncommitted data"),
    ("Durability", "Committed data forbliver gemt ved crash"),
    ("COMMIT", "Gem alle transaktionens operationer permanent"),
    ("ROLLBACK", "Fortryd alle operationer i transaktionen"),
    ("Index", "Datastruktur der gør søgning hurtigere"),
    ("B-tree index", "Standard tree-baseret index — god til range-queries og exact match"),
    ("Composite index", "Index der dækker flere kolonner"),
    ("Full table scan", "Databasen gennemgår alle rækker — langsomt uden index"),
    ("Cardinality", "Antal unikke værdier i en kolonne"),
    ("JWT", "JSON Web Token — token-baseret authentication"),
    ("BCrypt", "One-way password hashing algoritme"),
    ("Salt", "Random data tilføjet password før hashing — forhindrer rainbow tables"),
    ("SQL Injection", "Angreb via ondsindet SQL-kode i inputfelter"),
    ("Parameterized query", "Sikker SQL med ? placeholders — input behandles som data"),
    ("Trigger", "SQL der automatisk kører ved INSERT/UPDATE/DELETE"),
    ("Stored procedure", "Gemt SQL-kode kaldt med parametre (CALL sp_...)"),
    ("Stored function", "Som procedure, returnerer én værdi — bruges i SQL"),
    ("View", "Gemt query der opfører sig som virtuel tabel"),
    ("Event", "SQL der kører automatisk på tidsplan (cron job i databasen)"),
    ("Sharding", "Horisontal skalering — data spredt på flere servere"),
    ("CDC / Outbox pattern", "Event-driven sync mellem databaser i produktion"),
    ("InnoDB", "MySQL's storage engine — understøtter ACID og FK"),
]

rows = [["Term", "Forklaring"]]
for term, desc in terms:
    rows.append([term, desc])

story.append(table(rows, col_widths=[4.5*cm, W-4.5*cm]))
story.append(PageBreak())

# ── DEL 13: Q&A ─────────────────────────────
story.append(h1("DEL 13 — Mundtlige Spørgsmål og Svar"))
story.append(hr())
story.append(p("De spørgsmål der stilles oftest ved mundtlig database-eksamen:"))
story.append(sp())

qas = [
    (
        "Hvorfor bruger I tre databaser i stedet for bare MySQL?",
        "MySQL giver ACID-garantier for kernedata der er relationelt struktureret. "
        "MongoDB giver os mulighed for at hente en gæsts fulde historik — alle reservationer, "
        "bills og items — i ét kald uden joins, fordi vi embedder data. "
        "Neo4j giver os naturlig graph-traversal for spørgsmål om forbindelser, "
        "f.eks. hvilke cleaners arbejder på rum booket af specifikke gæster. "
        "Hvert database-valg løser et problem de andre ikke er bedst til.",
    ),
    (
        "Er din reservation-tabel i 3NF?",
        "Ja. Alle attributter afhænger direkte af reservation_id. "
        "booked_nightly_price ser ud som en transitiv afhængighed af season_rate, "
        "men det er et bevidst snapshot — vi gemmer prisen på bookingstidspunktet "
        "fordi sæsonpriser kan ændre sig. Det er ikke en normaliseringsovertrædelse, "
        "det er en designbeslutning der sikrer historisk korrekthed.",
    ),
    (
        "Hvad er forskellen på embedding og referencing i MongoDB?",
        "Embedding betyder at relaterede data kopieres direkte ind i dokumentet — "
        "vi har reservationer embedded i gæster fordi vi næsten altid henter dem sammen. "
        "Referencing betyder at vi gemmer et ID til et separat dokument — "
        "vi referencer rooms og cleaners fordi de eksisterer uafhængigt og deles af mange.",
    ),
    (
        "Hvad sker der hvis MongoDB og MySQL kommer ud af sync?",
        "I vores nuværende system migreres data manuelt via POST /api/migrate. "
        "Hvis MySQL opdateres uden at køre migration, vil MongoDB have forældet data. "
        "I en produktionsløsning ville vi bruge Change Data Capture eller et outbox pattern "
        "med en message broker som Kafka, så ændringer propageres automatisk og pålideligt.",
    ),
    (
        "Vis mig SQL injection prevention i din kode",
        "Vi bruger Spring Data JPA som automatisk genererer parameterized queries. "
        "I stedet for at konkatenere bruger-input direkte i SQL-strings, "
        "bruges method-derived queries som findByEmail(String email), "
        "hvilket genererer 'SELECT * FROM guest WHERE email = ?' "
        "hvor databasen behandler input som data, aldrig som kode.",
    ),
    (
        "Hvorfor har reservation to FK'er til room?",
        "assigned_room_id er det rum der formelt er tildelt på bookingstidspunktet, "
        "room_id er det rum der faktisk bruges ved check-in. "
        "De kan afvige ved f.eks. rum-opgraderinger eller reparationer.",
    ),
    (
        "Hvad er formålet med audit_log tabellen?",
        "Audit-tabellen logger alle INSERT, UPDATE og DELETE på kritiske tabeller "
        "via database-triggers. Hvert entry gemmer hvad der stod FØR og EFTER ændringen "
        "som JSON, hvem der lavede ændringen, og hvornår. "
        "Det bruges til compliance, fejlsøgning, og sikkerhed — "
        "f.eks. 'hvem ændrede reservationens status og hvornår?'",
    ),
    (
        "Hvad er et composite index og hvornår bruger I det?",
        "Et composite index dækker flere kolonner i én datastruktur. "
        "Vi bruger idx_reservation_dates(check_in_date, check_out_date) "
        "fordi vi ofte søger på begge datoer i availability-queries. "
        "Rækkefølgen betyder noget — indexet dækker queries der starter med check_in_date.",
    ),
    (
        "Hvad er forskellen på en stored procedure og en trigger?",
        "En stored procedure kaldes eksplicit med parametre, f.eks. CALL sp_CalculateFinalBill(42). "
        "En trigger kører automatisk som reaktion på en database-hændelse som INSERT eller UPDATE — "
        "ingen eksplicit kald nødvendigt. tr_AfterCheckout kører fx automatisk "
        "når reservation.status ændres til 'Checked Out'.",
    ),
    (
        "Hvad er ACID og hvorfor er det vigtigt for et hotelsystem?",
        "ACID er fire garantier for database-transaktioner. "
        "Atomicity: check-in-operationen (opdater reservation + opdater rum + opret bill) "
        "lykkes komplet eller slet ikke. "
        "Consistency: FK-constraints sikrer at vi ikke kan have en reservation til et ikke-eksisterende rum. "
        "Isolation: to gæster der booker det samme rum samtidig ser ikke hinandens uncommitted data. "
        "Durability: når en betaling er committed forbliver den gemt selv ved server-nedbrud.",
    ),
]

for q, a in qas:
    story.append(Paragraph(f"<b>Spørgsmål: {q}</b>", ParagraphStyle("Q", parent=BODY,
        textColor=colors.HexColor("#0f3460"), spaceBefore=10)))
    story.append(Paragraph(f"Svar: {a}", BODY))
    story.append(hr())

story.append(Spacer(1, 1*cm))
story.append(Paragraph("Held og lykke til eksamen torsdag!", ParagraphStyle("GL",
    parent=H2, alignment=TA_CENTER, textColor=colors.HexColor("#1a1a2e"))))

doc.build(story)
print(f"PDF gemt: {OUTPUT}")
