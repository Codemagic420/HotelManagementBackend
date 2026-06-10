# -*- coding: utf-8 -*-
"""Generate a 16:9 PDF presentation for the Hotel Management Backend exam."""

from reportlab.lib.pagesizes import landscape
from reportlab.pdfgen import canvas
from reportlab.lib import colors
from reportlab.lib.units import cm, mm
from reportlab.platypus import Paragraph, Frame, KeepInFrame
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.enums import TA_LEFT, TA_CENTER, TA_RIGHT
from reportlab.pdfbase.pdfmetrics import stringWidth
import textwrap

# ── Page setup (16:9) ────────────────────────────────────────────────────────
W, H = 960, 540          # slide points  (16:9)
MARGIN = 22

# ── Colour palette ───────────────────────────────────────────────────────────
NAVY   = colors.HexColor("#1a1a2e")
BLUE   = colors.HexColor("#0f3460")
ACCENT = colors.HexColor("#16a085")
WHITE  = colors.white
LIGHT  = colors.HexColor("#f4f6f9")
DARK   = colors.HexColor("#2c2c2c")
YELLOW = colors.HexColor("#f39c12")
RED    = colors.HexColor("#c0392b")
GREEN  = colors.HexColor("#27ae60")
PURPLE = colors.HexColor("#6a257a")
ORANGE = colors.HexColor("#aa5500")
MYSQL  = colors.HexColor("#00788F")
MONGO  = colors.HexColor("#1a8a45")
NEO    = colors.HexColor("#4a54cc")
TEAL   = colors.HexColor("#1a6a5a")

OUTPUT = r"C:\Users\magnu\IdeaProjects\HotelManagementBackend1\Hotel_Eksamen_Slides.pdf"
c = canvas.Canvas(OUTPUT, pagesize=(W, H))

def tri(pts, fill=1, stroke=0):
    """Draw a filled triangle from a flat list [x0,y0,x1,y1,x2,y2]."""
    p = c.beginPath()
    p.moveTo(pts[0], pts[1])
    for i in range(2, len(pts), 2):
        p.lineTo(pts[i], pts[i+1])
    p.close()
    c.drawPath(p, fill=fill, stroke=stroke)


# ═══════════════════════════════════════════════════════════════════════════
# DRAWING PRIMITIVES
# ═══════════════════════════════════════════════════════════════════════════

def page_bg(col=LIGHT):
    c.setFillColor(col)
    c.rect(0, 0, W, H, fill=1, stroke=0)

def header_bar(title, subtitle=None, bg=NAVY, fg=WHITE):
    bar_h = 72
    c.setFillColor(bg)
    c.rect(0, H - bar_h, W, bar_h, fill=1, stroke=0)
    c.setFillColor(fg)
    c.setFont("Helvetica-Bold", 22)
    c.drawString(MARGIN, H - 44, title)
    if subtitle:
        c.setFillColor(colors.HexColor("#aaccee"))
        c.setFont("Helvetica", 11)
        c.drawString(MARGIN, H - 62, subtitle)

def box(x, y, w, h, fill=NAVY, stroke=None, radius=4):
    c.setFillColor(fill)
    if stroke:
        c.setStrokeColor(stroke)
        c.roundRect(x, y, w, h, radius, fill=1, stroke=1)
    else:
        c.roundRect(x, y, w, h, radius, fill=1, stroke=0)

def text(x, y, s, font="Helvetica", size=11, col=DARK, anchor="left"):
    c.setFillColor(col)
    c.setFont(font, size)
    if anchor == "center":
        c.drawCentredString(x, y, s)
    elif anchor == "right":
        c.drawRightString(x, y, s)
    else:
        c.drawString(x, y, s)

def mtext(x, y, w, lines, font="Helvetica", size=10, col=DARK,
          leading=14, align=TA_LEFT):
    """Multi-line wrapped text block."""
    style = ParagraphStyle("t", fontName=font, fontSize=size,
                           leading=leading, textColor=col, alignment=align)
    paras = [Paragraph(l.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"),
                       style) for l in lines]
    f = Frame(x, y - sum(p.wrap(w,9999)[1] for p in paras),
              w, sum(p.wrap(w,9999)[1] for p in paras) + 10,
              showBoundary=0, leftPadding=0, rightPadding=0,
              topPadding=0, bottomPadding=0)
    # simpler: just draw line by line
    cy = y
    for line in lines:
        c.setFillColor(col)
        c.setFont(font, size)
        c.drawString(x, cy, line)
        cy -= leading

def bullet_list(x, y, items, size=10, col=DARK, leading=15, w=400):
    """Draw bullet list; items may be multi-line via \n."""
    cy = y
    for item in items:
        c.setFillColor(ACCENT)
        c.setFont("Helvetica-Bold", size)
        c.drawString(x, cy, "•")
        c.setFillColor(col)
        c.setFont("Helvetica", size)
        # wrap text
        wrapped = textwrap.wrap(item, width=int(w / (size * 0.52)))
        for i, line in enumerate(wrapped):
            c.drawString(x + 12, cy - i * (leading - 1), line)
        cy -= leading * max(len(wrapped), 1)
    return cy  # return bottom y

def card(x, y, w, h, title, body_lines, title_bg=BLUE, body_bg=WHITE,
         title_fg=WHITE, body_fg=DARK, tsize=10, bsize=9, leading=13):
    th = 22
    box(x, y + h - th, w, th, fill=title_bg)
    text(x + 8, y + h - th + 7, title, "Helvetica-Bold", tsize, col=title_fg)
    box(x, y, w, h - th, fill=body_bg)
    cy = y + h - th - 14
    for line in body_lines:
        c.setFillColor(body_fg)
        c.setFont("Helvetica", bsize)
        c.drawString(x + 8, cy, line)
        cy -= leading

def code_box(x, y, w, h, lines, fsize=8):
    box(x, y, w, h, fill=colors.HexColor("#1e1e2e"))
    cy = y + h - 14
    for line in lines:
        c.setFillColor(colors.HexColor("#a8ff78"))
        c.setFont("Courier", fsize)
        c.drawString(x + 8, cy, line)
        cy -= (fsize + 3)

def hline_rel(x1, x2, y, name, arrow_right=True):
    """Horizontal relationship line with coloured label pill."""
    c.setStrokeColor(colors.HexColor("#555588"))
    c.setLineWidth(1.5)
    c.line(x1, y, x2, y)
    # arrowhead
    ax = x2 if arrow_right else x1
    dx = 6 if arrow_right else -6
    c.setFillColor(colors.HexColor("#555588"))
    tri([ax, y, ax-dx, y+4, ax-dx, y-4])
    # label pill
    mid = (x1 + x2) / 2
    lw = stringWidth(name, "Helvetica-Bold", 7) + 10
    box(mid - lw/2, y + 3, lw, 14, fill=ORANGE)
    c.setFillColor(WHITE)
    c.setFont("Helvetica-Bold", 7)
    c.drawCentredString(mid, y + 8, name)

def vline_rel(x, y1, y2, name, arrow_up=False):
    """Vertical relationship line (top=y2, bottom=y1 in PDF coords) with pill."""
    c.setStrokeColor(colors.HexColor("#555588"))
    c.setLineWidth(1.5)
    c.line(x, y1, x, y2)
    # arrowhead
    ay = y2 if not arrow_up else y1
    dy = 6 if not arrow_up else -6
    c.setFillColor(colors.HexColor("#555588"))
    tri([x, ay, x-4, ay-dy, x+4, ay-dy])
    # label pill
    mid = (y1 + y2) / 2
    lw = stringWidth(name, "Helvetica-Bold", 7) + 10
    box(x + 4, mid - 7, lw, 14, fill=ORANGE)
    c.setFillColor(WHITE)
    c.setFont("Helvetica-Bold", 7)
    c.drawString(x + 9, mid - 2, name)

def node_box(x, y, w, h, label, props, hcol=NEO):
    """Neo4j-style node box."""
    hh = 20
    box(x, y + h - hh, w, hh, fill=hcol)
    c.setFillColor(WHITE)
    c.setFont("Helvetica-Bold", 8.5)
    c.drawString(x + 5, y + h - hh + 6, label)
    box(x, y, w, h - hh, fill=colors.HexColor("#eaecff"))
    cy = y + h - hh - 12
    for p in props:
        c.setFillColor(DARK)
        c.setFont("Helvetica", 7.5)
        c.drawString(x + 5, cy, p)
        cy -= 12

def entity_box(x, y, w, h, name, fields, hcol=MYSQL):
    """ERD entity box."""
    hh = 20
    box(x, y + h - hh, w, hh, fill=hcol)
    c.setFillColor(WHITE)
    c.setFont("Helvetica-Bold", 8.5)
    c.drawString(x + 5, y + h - hh + 6, name)
    box(x, y, w, h - hh, fill=colors.HexColor("#e6f2ff"))
    cy = y + h - hh - 12
    for f in fields:
        c.setFillColor(DARK)
        c.setFont("Helvetica", 7.5)
        c.drawString(x + 5, cy, f)
        cy -= 12

def junction_box(x, y, w, h, name, fields):
    entity_box(x, y, w, h, name, fields, hcol=colors.HexColor("#5a3a8a"))

def new_slide():
    c.showPage()
    page_bg()


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 1 — TITLE
# ═══════════════════════════════════════════════════════════════════════════
page_bg(NAVY)
# bottom accent bar
box(0, 0, W, 80, fill=ACCENT)
# title
c.setFillColor(WHITE)
c.setFont("Helvetica-Bold", 38)
c.drawCentredString(W/2, 360, "Hotel Management Backend")
c.setFillColor(colors.HexColor("#aaccff"))
c.setFont("Helvetica", 17)
c.drawCentredString(W/2, 325, "Databases for Developers  —  Oral Exam Presentation")

# three db pills
db_pills = [
    ("MySQL  :3307", MYSQL),
    ("MongoDB  :27017", MONGO),
    ("Neo4j  :7687", NEO),
]
for i, (lbl, col) in enumerate(db_pills):
    px = 200 + i * 195
    box(px, 255, 175, 34, fill=col)
    c.setFillColor(WHITE)
    c.setFont("Helvetica-Bold", 12)
    c.drawCentredString(px + 87, 268, lbl)

c.setFillColor(colors.HexColor("#ccddff"))
c.setFont("Helvetica", 12)
c.drawCentredString(W/2, 220, "Spring Boot 4  ·  JWT Auth  ·  Docker Compose")


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 2 — SYSTEM ARCHITECTURE
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("1 · System Architecture",
           "Data flow and responsibilities of each service")

# Spring Boot centre box
box(250, 130, 340, 270, fill=NAVY)
c.setFillColor(WHITE)
c.setFont("Helvetica-Bold", 14)
c.drawCentredString(420, 377, "Spring Boot 4  ·  API :8080")
c.setFillColor(colors.HexColor("#556688"))
c.rect(262, 370, 316, 1, fill=1, stroke=0)
details = [
    "JWT Authentication  ·  BCrypt",
    "Roles: ADMIN / STAFF / CLEANER",
    "@Transactional  (JPA + Neo4j)",
    "REST controllers → Services → DB",
]
for i, d in enumerate(details):
    c.setFillColor(colors.HexColor("#aabbdd"))
    c.setFont("Helvetica", 9.5)
    c.drawCentredString(420, 350 - i * 50, d)

# Client box
box(20, 255, 170, 80, fill=colors.HexColor("#333355"))
c.setFillColor(WHITE)
c.setFont("Helvetica-Bold", 11)
c.drawCentredString(105, 302, "Client")
c.setFont("Helvetica", 9)
c.drawCentredString(105, 285, "(Browser / Frontend)")
# arrow
c.setStrokeColor(colors.HexColor("#777799"))
c.setLineWidth(1.5)
c.line(190, 295, 250, 295)
c.setFillColor(colors.HexColor("#777799"))
tri([250,295, 244,299, 244,291])
c.setFillColor(colors.HexColor("#888899"))
c.setFont("Helvetica", 8)
c.drawCentredString(220, 303, "REST →")

# AI box
box(20, 148, 170, 80, fill=PURPLE)
c.setFillColor(WHITE)
c.setFont("Helvetica-Bold", 11)
c.drawCentredString(105, 196, "AI Service")
c.setFont("Helvetica", 9)
c.drawCentredString(105, 179, "(Ollama / LLM)")
c.setStrokeColor(colors.HexColor("#9955aa"))
c.setLineWidth(1.5)
c.line(190, 188, 250, 188)
c.setFillColor(colors.HexColor("#9955aa"))
c.setFont("Helvetica", 8)
c.drawCentredString(220, 196, "← HTTP →")

# Three DB boxes right side
dbs = [
    ("MySQL  :3307", "Source of truth · ACID · FK", "Transactions · JOINs · Triggers", MYSQL),
    ("MongoDB  :27017", "Embedded guest docs · AI logs", "Schema-free · Aggregation pipeline", MONGO),
    ("Neo4j  :7687", "Graph: nodes, relationships", "Cypher · multi-hop traversal", NEO),
]
for i, (title, l1, l2, col) in enumerate(dbs):
    dy = 370 - i * 100
    box(610, dy - 55, 8, 60, fill=col)
    box(618, dy - 55, 318, 60, fill=WHITE)
    c.setFillColor(DARK)
    c.setFont("Helvetica-Bold", 11)
    c.drawString(626, dy - 12, title)
    c.setFont("Helvetica", 8.5)
    c.setFillColor(colors.HexColor("#444444"))
    c.drawString(626, dy - 28, l1)
    c.drawString(626, dy - 42, l2)
    # arrow from API
    c.setStrokeColor(colors.HexColor("#888899"))
    c.setLineWidth(1.5)
    c.line(590, dy - 25, 610, dy - 25)
    c.setFillColor(col)
    tri([610, dy-25, 604, dy-21, 604, dy-29])

# migration banner
box(20, 22, W - 40, 30, fill=colors.HexColor("#e0e8f8"))
c.setFillColor(DARK)
c.setFont("Helvetica", 9)
c.drawCentredString(W/2, 33,
    "POST /api/migrate  →  Copies MySQL data into MongoDB & Neo4j   ·   "
    "Production: CDC (Change Data Capture) + Kafka for real-time sync")


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 3 — ERD  (clean 3-row layout)
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("2 · Relational Database Design — ERD",
           "Entities, PK/FK, cardinalities · 13 tables · InnoDB (ACID)")

BILL_COL  = colors.HexColor("#7a3a00")
TEAL2     = colors.HexColor("#1a6a5a")
GREEN2    = colors.HexColor("#1a5a1a")
JUNC_COL  = colors.HexColor("#5a3a8a")

# Entity heights (header=20, each field=13, top-pad=5)
def eh(n): return 20 + 5 + n * 13

# ── Row Y bottoms (PDF y=0 is page bottom) ──────────────────────────────────
RT_Y  = 398   # "top" row  — room_type, season_rate
MID_Y = 248   # main row  — guest … cleaner
BOT_Y = 90    # bottom row — bill, bill_item

# ── Column X + widths for MAIN ROW ──────────────────────────────────────────
# 5 entities spread evenly across 18..942  (total span = 924)
# widths: 130, 170, 140, 155, 130  = 725  →  4 gaps = (924-725)/4 = ~50
G_X,  G_W  = 18,  130   # guest
RV_X, RV_W = 198, 170   # reservation  (wider: 4 fields)
RM_X, RM_W = 418, 140   # room
CT_X, CT_W = 608, 155   # room_cleaning_task
CL_X, CL_W = 813, 130   # cleaner  (right=943 ≈ slide edge)

G_CX  = G_X  + G_W  // 2   # 83
RV_CX = RV_X + RV_W // 2   # 283
RM_CX = RM_X + RM_W // 2   # 488
CT_CX = CT_X + CT_W // 2   # 685
CL_CX = CL_X + CL_W // 2   # 878

# ── Top row: room_type above room, season_rate to the right ─────────────────
RT2_X, RT2_W = RM_X, 140              # room_type aligned with room
SR_X,  SR_W  = CT_X + 5, 145         # season_rate roughly above task area

RT2_CX = RT2_X + RT2_W // 2          # 488  (same as RM_CX)
SR_CX  = SR_X  + SR_W  // 2          # 690

# ── Bottom row: bill + bill_item ─────────────────────────────────────────────
BL_X, BL_W = RV_X, 150               # bill aligned under reservation
BI_X, BI_W = 28,   145               # bill_item to the left

BL_CX = BL_X + BL_W // 2            # 283
BI_CX = BI_X + BI_W // 2            # 100

# ── Draw entities ────────────────────────────────────────────────────────────
# Top row
entity_box(RT2_X, RT_Y, RT2_W, eh(3), "room_type", [
    "PK  room_type_id  INT",
    "name (Single/Double/Suite)",
    "max_occupancy",
], hcol=TEAL2)

entity_box(SR_X, RT_Y, SR_W, eh(2), "season_rate", [
    "PK  rate_id  INT",
    "FK  room_type_id, season",
], hcol=TEAL2)

# Main row
entity_box(G_X, MID_Y, G_W, eh(3), "guest", [
    "PK  guest_id  BIGINT",
    "first_name, last_name",
    "email UNIQUE, phone",
])
entity_box(RV_X, MID_Y, RV_W, eh(4), "reservation", [
    "PK  reservation_id",
    "FK  guest_id, room_id",
    "FK  room_type_id, booked_rate_id",
    "status, booked_nightly_price ★",
])
entity_box(RM_X, MID_Y, RM_W, eh(3), "room", [
    "PK  room_id  INT",
    "FK  room_type_id",
    "room_number, status",
], hcol=TEAL2)
entity_box(CT_X, MID_Y, CT_W, eh(3), "room_cleaning_task", [
    "PK  task_id  INT",
    "FK  room_id",
    "task_type, scheduled_date",
], hcol=GREEN2)
entity_box(CL_X, MID_Y, CL_W, eh(3), "cleaner", [
    "PK  cleaner_id  INT",
    "first_name, last_name",
    "phone, is_active",
], hcol=GREEN2)

# Bottom row
entity_box(BL_X, BOT_Y, BL_W, eh(3), "bill", [
    "PK  bill_id  INT",
    "FK  reservation_id  UNIQUE",
    "total_amount, opened_at",
], hcol=BILL_COL)
entity_box(BI_X, BOT_Y, BI_W, eh(3), "bill_item", [
    "PK  bill_item_id  INT",
    "FK  bill_id",
    "item_type, unit_price, qty",
], hcol=BILL_COL)

# Junction tables (compact, shown between their parents)
junction_box(G_X + G_W + 5, MID_Y - eh(2) - 22, 130, eh(2),
             "reservation_guest", [
    "PK (reservation_id, guest_id)",
    "is_primary  BIT",
])
junction_box(CT_X + CT_W + 5, MID_Y - eh(2) - 22, 130, eh(2),
             "room_cleaning_assignment", [
    "PK (task_id, cleaner_id)",
])

# ── Cardinality helper ────────────────────────────────────────────────────────
def card_label(x, y, txt_l, txt_r, gap):
    """Draw '1' and 'N' at each end of a relationship line."""
    c.setFillColor(ACCENT)
    c.setFont("Helvetica-Bold", 9)
    c.drawString(x + 2,       y + 3, txt_l)
    c.drawRightString(x + gap - 2, y + 3, txt_r)

def junc_label(mx, y, label):
    """Small italic 'via …' note below a line midpoint."""
    c.setFillColor(colors.HexColor("#6644aa"))
    c.setFont("Helvetica-Oblique", 7)
    c.drawCentredString(mx, y - 12, label)

# ── Relationship lines ────────────────────────────────────────────────────────
c.setStrokeColor(colors.HexColor("#444466"))
c.setLineWidth(1.3)

# Mid row horizontal centre (use reservation centre since it's tallest)
MH_Y = MID_Y + eh(4) // 2   # ≈ 248+37 = 285

# 1. guest ─M:N─ reservation  (gap = RV_X - (G_X+G_W) = 50)
gap1 = RV_X - (G_X + G_W)
c.line(G_X + G_W, MH_Y, RV_X, MH_Y)
card_label(G_X + G_W, MH_Y, "M", "N", gap1)
junc_label((G_X + G_W + RV_X) // 2, MH_Y, "via reservation_guest")

# 2. reservation ─N:1─ room
gap2 = RM_X - (RV_X + RV_W)
c.line(RV_X + RV_W, MH_Y, RM_X, MH_Y)
card_label(RV_X + RV_W, MH_Y, "N", "1", gap2)

# 3. room ─1:N─ room_cleaning_task
gap3 = CT_X - (RM_X + RM_W)
c.line(RM_X + RM_W, MH_Y, CT_X, MH_Y)
card_label(RM_X + RM_W, MH_Y, "1", "N", gap3)

# 4. room_cleaning_task ─M:N─ cleaner
gap4 = CL_X - (CT_X + CT_W)
c.line(CT_X + CT_W, MH_Y, CL_X, MH_Y)
card_label(CT_X + CT_W, MH_Y, "M", "N", gap4)
junc_label((CT_X + CT_W + CL_X) // 2, MH_Y, "via room_cleaning_assignment")

# 5. room_type ─1:N─ season_rate (top row, horizontal)
TR_Y_MID = RT_Y + eh(3) // 2
gap5 = SR_X - (RT2_X + RT2_W)
c.line(RT2_X + RT2_W, TR_Y_MID, SR_X, TR_Y_MID)
card_label(RT2_X + RT2_W, TR_Y_MID, "1", "N", gap5)

# 6. room_type ─1:N─ room (vertical: from room top up to room_type bottom)
c.line(RT2_CX, MID_Y + eh(3), RT2_CX, RT_Y)
c.setFillColor(ACCENT); c.setFont("Helvetica-Bold", 9)
c.drawString(RT2_CX + 3, RT_Y - 10,      "1")
c.drawString(RT2_CX + 3, MID_Y + eh(3) + 2, "N")

# 7. reservation ─1:1─ bill (vertical)
c.setStrokeColor(colors.HexColor("#444466"))
c.line(RV_CX, MID_Y, RV_CX, BOT_Y + eh(3))
c.setFillColor(ACCENT); c.setFont("Helvetica-Bold", 9)
c.drawString(RV_CX + 3, MID_Y - 10,          "1")
c.drawString(RV_CX + 3, BOT_Y + eh(3) + 2,   "1")

# 8. bill ─1:N─ bill_item (horizontal, bottom row)
BH_Y = BOT_Y + eh(3) // 2
gap8 = BL_X - (BI_X + BI_W)
c.line(BI_X + BI_W, BH_Y, BL_X, BH_Y)
card_label(BI_X + BI_W, BH_Y, "N", "1", gap8)

# ── Legend ────────────────────────────────────────────────────────────────────
leg = [(MYSQL,"Core entity"),(TEAL2,"Type/Rate"),(GREEN2,"Housekeeping"),
       (BILL_COL,"Billing"),(JUNC_COL,"Junction")]
lx2 = 18
for lc, ll in leg:
    box(lx2, 28, 9, 9, fill=lc)
    c.setFillColor(DARK); c.setFont("Helvetica", 7.5)
    c.drawString(lx2 + 12, 30, ll)
    lx2 += 150
c.setFillColor(colors.HexColor("#806000")); c.setFont("Helvetica-Oblique", 7.5)
c.drawString(18, 14, "★ booked_nightly_price = historical snapshot (deliberate design — not a 3NF violation)")


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 4 — NORMALIZATION
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("3 · Normalization — 1NF → 3NF",
           "How we reached our schema and key design choices")

cols_nf = [
    ("1NF", MYSQL, "Atomic values only", [
        "No lists / arrays in one column",
        "Each field = one indivisible value",
        "✓  credit_card_last4: only 4 digits",
        "✓  status: one ENUM value per row",
        "✓  Reservations in own table,",
        "    not comma-list in guest row",
    ]),
    ("2NF", MONGO, "Full key dependency", [
        "All non-key fields depend on",
        "the WHOLE primary key",
        "Relevant for COMPOSITE PKs",
        "✓  reservation_guest.is_primary",
        "    depends on BOTH key columns",
        "✗  guest_email here → only guest_id",
        "    → moved to guest table",
    ]),
    ("3NF", ACCENT, "No transitive dependencies", [
        "Non-key fields must not depend",
        "on other non-key fields",
        "A → B → C: move C to own table",
        "✓  room_type_name lives in room_type,",
        "    NOT in room or reservation",
        "⚡  booked_nightly_price = intentional",
        "    snapshot — NOT a violation",
    ]),
]

for i, (nf, col, title, bullets) in enumerate(cols_nf):
    cx = 30 + i * 307
    # big NF circle
    c.setFillColor(col)
    c.circle(cx + 60, 418, 30, fill=1, stroke=0)
    c.setFillColor(WHITE)
    c.setFont("Helvetica-Bold", 20)
    c.drawCentredString(cx + 60, 411, nf)
    # title
    c.setFillColor(col)
    c.setFont("Helvetica-Bold", 11)
    c.drawCentredString(cx + 140, 375, title)
    # card
    card(cx, 60, 286, 300, title, bullets,
         title_bg=col, body_bg=WHITE, body_fg=DARK,
         tsize=10, bsize=9, leading=20)


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 5 — MONGODB
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("4 · MongoDB Design",
           "Collections, embedding vs. referencing, document model")

# Left: code block
code_box(18, 60, 390, 400, [
    "{",
    '  "guestId": 5,',
    '  "firstName": "Magnus",',
    '  "email": "magnus@hotel.dk",',
    '  "reservations": [         <- EMBEDDED',
    '    {',
    '      "reservationId": 42,',
    '      "checkInDate": "2024-06-01",',
    '      "roomType": "Double",',
    '      "bills": [            <- EMBEDDED',
    '        {',
    '          "totalAmount": 2400.00,',
    '          "items": [...]',
    '        }',
    '      ]',
    '    }',
    '  ]',
    '}',
], fsize=8.5)
c.setFillColor(MONGO)
c.setFont("Helvetica-Bold", 9)
c.drawString(18, 468, "guest collection  (embedded model)")

# Right: decision cards
card(420, 355, 520, 105, "EMBEDDED — Guest + Reservations + Bills", [
    "One document = full guest history",
    "Single query — no JOINs needed",
    "Best when data is always read together",
    "Trade-off: updates must sync with MySQL",
], title_bg=MONGO, body_bg=WHITE, body_fg=DARK, tsize=9, bsize=9, leading=17)

card(420, 235, 520, 105, "REFERENCED — Rooms / Cleaners / Rates", [
    "Exist independently of reservations",
    "Shared across many documents",
    "Updated frequently (room status)",
    "Stored as separate collections + IDs",
], title_bg=colors.HexColor("#1a6080"), body_bg=WHITE, body_fg=DARK, tsize=9, bsize=9, leading=17)

card(420, 110, 520, 110, "ai_interactions  (MongoDB ONLY)", [
    "Unstructured LLM chat logs",
    "Schema-free — no fixed columns",
    "Fields: sessionId, userMessage,",
    "  aiResponse, model, tokensUsed",
    "Not a fit for any relational schema",
], title_bg=PURPLE, body_bg=WHITE, body_fg=DARK, tsize=9, bsize=9, leading=17)

card(420, 60, 520, 35, "Collections in this project", [
    "guests  ·  rooms  ·  cleaners  ·  season_rates  ·  ai_interactions",
], title_bg=NAVY, body_bg=colors.HexColor("#e8eeff"), body_fg=DARK,
   tsize=9, bsize=8.5, leading=14)


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 6 — NEO4J  (clean 3-row graph diagram)
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("5 · Neo4j Graph Model",
           "Nodes, relationships, properties — the hotel graph")

# ── Layout ────────────────────────────────────────────────────────────────────
# 3 rows of nodes, well spaced
# Node dimensions
NW1 = 130  # standard node width
NW2 = 155  # wide node
NH3 = 20 + 3*13 + 8   # header + 3 props + padding = 67
NH2 = 20 + 2*13 + 8   # 2 props = 54

# Row Y bottoms (PDF coords)
NR1 = 378   # row 1 (top row)
NR2 = 232   # row 2
NR3 = 82    # row 3

# Column X positions (adjusted so the whole diagram fits 18 to 850)
NC1 = 18    # Guest
NC2 = 185   # Reservation
NC3 = 378   # Room
NC4 = 560   # RoomType
NC5 = 720   # (right side stacks)

# Row 1 nodes
node_box(NC1,      NR1, NW1, NH3, ":Guest",
         ["guestId  |  firstName", "lastName  |  email", "phone"])
node_box(NC2,      NR1, NW2, NH3, ":Reservation",
         ["reservationId  |  status", "checkIn  |  checkOut", "nights  |  price"])
node_box(NC3,      NR1, NW2, NH3, ":Room",
         ["roomId  |  roomNumber", "roomStatus  |  cleanStatus", "occupied"])
node_box(NC4,      NR1, NW1, NH2, ":RoomType",
         ["name  (S/D/Suite)", "maxOccupancy"])

# Row 2 nodes
node_box(NC2,      NR2, NW1+15, NH2, ":Bill",
         ["billId  |  totalAmount", "openedAt  |  closedAt"])
node_box(NC3,      NR2, NW2,    NH2, ":RoomCleaningTask",
         ["taskId  |  taskType", "scheduledDate"])
node_box(NC4,      NR2, NW1,    NH2, ":SeasonRate",
         ["season  |  price", "validFrom  |  validTo"])

# Row 3 nodes
node_box(NC2,      NR3, NW1+15, NH3, ":BillItem",
         ["billItemId  |  itemType", "quantity  |  unitPrice", "lineTotal"])
node_box(NC3,      NR3, NW1,    NH3, ":Cleaner",
         ["cleanerId  |  firstName", "lastName  |  phone", "isActive"])

# ── Horizontal row 1 connections ─────────────────────────────────────────────
R1_MID = NR1 + NH3/2

hline_rel(NC1+NW1, NC2,      R1_MID, "[:BOOKED_BY]")
hline_rel(NC2+NW2, NC3,      R1_MID, "[:ASSIGNED_TO]")
hline_rel(NC3+NW2, NC4,      R1_MID, "[:IS_TYPE]")

# ── Vertical connections ──────────────────────────────────────────────────────
# Centre X for each vertical line
RV_CX = NC2 + (NW2//2)    # Reservation & Bill centre ~ 262
RM_CX = NC3 + (NW2//2)    # Room & Task centre ~ 455
RT_CX = NC4 + (NW1//2)    # RoomType & SeasonRate ~ 625
BI_CX = NC2 + (NW1+15)//2  # Bill & BillItem ~ 277
CL_CX = NC3 + NW1//2       # Task & Cleaner ~ 443

# Reservation ↓ Bill
vline_rel(RV_CX, NR2 + NH2, NR1, "[:HAS_BILL]")
# Bill ↓ BillItem
vline_rel(BI_CX, NR3 + NH3, NR2, "[:CONTAINS_ITEM]")
# Room ↓ RoomCleaningTask
vline_rel(RM_CX, NR2 + NH2, NR1, "[:HAS_CLEANING_TASK]")
# Cleaner ↑ RoomCleaningTask (arrow at top = toward Task)
vline_rel(CL_CX, NR3 + NH3, NR2, "[:ASSIGNED_TO]", arrow_up=True)
# RoomType ↓ SeasonRate
vline_rel(RT_CX, NR2 + NH2, NR1, "[:HAS_RATE]")

# ── Cypher box ────────────────────────────────────────────────────────────────
code_box(570, 60, 370, 58, [
    "MATCH (g:Guest)-[:BOOKED_BY]->",
    "  (res:Reservation)-[:ASSIGNED_TO]->(r:Room)",
    "WHERE g.guestId = 5",
    "RETURN g.firstName, res.status, r.roomNumber",
], fsize=8)
c.setFillColor(DARK)
c.setFont("Helvetica", 8.5)
c.drawString(570, 124, "Cypher — traverse 3 hops, no JOINs")


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 7 — CROSS-DB QUERY
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("6 · Cross-Database Query Comparison",
           "Scenario: find all CONFIRMED reservations for room 101")

titles_q = ["MySQL  (SQL)", "MongoDB  (Aggregation)", "Neo4j  (Cypher)"]
colors_q  = [MYSQL, MONGO, NEO]
sql_code = [
    "SELECT g.first_name,",
    "       g.last_name,",
    "       r.reference_no,",
    "       r.check_in_date",
    "FROM reservation r",
    "JOIN guest g",
    "  ON r.guest_id = g.guest_id",
    "JOIN room rm",
    "  ON r.room_id = rm.room_id",
    "WHERE rm.room_number = '101'",
    "  AND r.status = 'CONFIRMED';",
]
mongo_code = [
    "db.guests.aggregate([",
    "  { $unwind: '$reservations'},",
    "  { $match: {",
    "    'reservations.room': '101',",
    "    'reservations.status':",
    "      'CONFIRMED'",
    "  }},",
    "  { $project: {",
    "    firstName: 1, lastName: 1,",
    "    'reservations.referenceNo':1",
    "  }}",
    "])",
]
cypher_code = [
    "MATCH (g:Guest)",
    "  -[:BOOKED_BY]->",
    "  (res:Reservation)",
    "  -[:ASSIGNED_TO]->",
    "  (r:Room)",
    "WHERE",
    "  r.roomNumber = '101'",
    "  AND res.status = 'CONFIRMED'",
    "RETURN",
    "  g.firstName, g.lastName,",
    "  res.referenceNo",
]
all_code = [sql_code, mongo_code, cypher_code]
CW = 295
for i, (ttl, col, code) in enumerate(zip(titles_q, colors_q, all_code)):
    cx = 18 + i * (CW + 18)
    box(cx, 445, CW, 22, fill=col)
    c.setFillColor(WHITE); c.setFont("Helvetica-Bold", 10)
    c.drawCentredString(cx + CW/2, 452, ttl)
    code_box(cx, 210, CW, 234, code, fsize=8.5)

# comparison bar
box(18, 60, W-36, 135, fill=WHITE)
comp = [
    (MYSQL, "MySQL", "Explicit JOINs · set-based · ACID-strong · schema-enforced"),
    (MONGO, "MongoDB", "Pipeline: $unwind → $match → $project · one document fetch"),
    (NEO,   "Neo4j", "Pattern-match traversal (MATCH) · no JOINs · best for graph hops"),
]
for i, (col, db, desc) in enumerate(comp):
    cy2 = 175 - i * 38
    box(26, cy2 - 10, 8, 22, fill=col)
    c.setFillColor(DARK); c.setFont("Helvetica-Bold", 9.5)
    c.drawString(42, cy2 + 4, db + ": ")
    c.setFont("Helvetica", 9.5)
    c.drawString(42 + stringWidth(db + ": ", "Helvetica-Bold", 9.5), cy2 + 4, desc)


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 8 — TRANSACTIONS
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("7 · Transactions — ACID Guarantees",
           "Concrete example: Guest check-in — 3 operations, one transaction")

acid = [
    ("A", "Atomicity",   "All 3 steps succeed\nor none at all",      MYSQL),
    ("C", "Consistency", "FK constraints ensure\nno orphan records",  MONGO),
    ("I", "Isolation",   "Concurrent bookings:\nonly one wins",       ACCENT),
    ("D", "Durability",  "Committed data\nsurvives server crash",     RED),
]
for i, (letter, name, desc, col) in enumerate(acid):
    ax = 30 + i * 220
    c.setFillColor(col)
    c.circle(ax + 50, 430, 28, fill=1, stroke=0)
    c.setFillColor(WHITE); c.setFont("Helvetica-Bold", 22)
    c.drawCentredString(ax + 50, 423, letter)
    c.setFillColor(DARK); c.setFont("Helvetica-Bold", 10)
    c.drawCentredString(ax + 50, 387, name)
    c.setFont("Helvetica", 8.5)
    for j, line in enumerate(desc.split("\n")):
        c.drawCentredString(ax + 50, 372 - j*12, line)

code_box(18, 55, 490, 290, [
    "BEGIN;",
    "",
    "  UPDATE reservation",
    "  SET status = 'CHECKED_IN'",
    "  WHERE reservation_id = 42;        -- step 1",
    "",
    "  UPDATE room",
    "  SET occupied = 1,",
    "      room_status = 'OCCUPIED'",
    "  WHERE room_id = 15;               -- step 2",
    "",
    "  INSERT INTO bill",
    "    (reservation_id, opened_at, total_amount)",
    "  VALUES (42, NOW(), 0.00);         -- step 3",
    "",
    "COMMIT;",
    "-- If anything fails → ROLLBACK",
], fsize=8.5)

card(520, 55, 420, 290, "@Transactional — Spring Boot", [
    "Service methods annotated with",
    "@Transactional",
    "",
    "Spring wraps method in",
    "BEGIN ... COMMIT automatically",
    "",
    "Any exception triggers",
    "automatic ROLLBACK",
    "",
    "Two TX managers configured:",
    "JpaTransactionManager (@Primary)",
    "Neo4jTransactionManager",
], title_bg=NAVY, body_bg=WHITE, body_fg=DARK, tsize=9, bsize=9, leading=18)


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 9 — INDEXES
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("8 · Indexes & Performance",
           "Key indexes, design decisions, and what we deliberately left out")

# analogy bar
box(18, 440, W-36, 26, fill=NAVY)
c.setFillColor(WHITE); c.setFont("Helvetica", 10)
c.drawCentredString(W/2, 450,
    "Index = book's back index — jump directly to the right row instead of scanning all rows  ·  "
    "No index: O(n)  ·  B-tree index: O(log n)")

# index table
rows_idx = [
    ("Table",       "Index",                        "Type",      "Why?"),
    ("guest",       "idx_guest_email",              "B-tree",    "Login lookup — most frequent query"),
    ("guest",       "idx_guest_name (last, first)", "Composite", "Name search, multi-column ORDER BY"),
    ("reservation", "idx_reservation_dates",        "Composite", "Date-range availability queries"),
    ("reservation", "idx_reservation_status",       "B-tree",    "Filter CONFIRMED / CHECKED_IN fast"),
    ("reservation", "idx_reservation_guest",        "B-tree",    "FK join: all reservations for a guest"),
    ("bill",        "idx_bill_reservation",         "B-tree",    "FK join performance on billing"),
    ("room",        "idx_room_status",              "B-tree",    "Filter AVAILABLE rooms quickly"),
    ("audit_log",   "idx_audit_table/op/time",      "B-tree",    "Fast audit trail queries"),
]
col_ws = [80, 190, 85, 330]
tx = 18; ty = 425
for ri, row in enumerate(rows_idx):
    rx = tx
    fill = NAVY if ri == 0 else (WHITE if ri % 2 == 0 else colors.HexColor("#f0f4fa"))
    box(tx, ty - ri*30, sum(col_ws)+6, 28, fill=fill, radius=0)
    for ci, (cell, cw) in enumerate(zip(row, col_ws)):
        c.setFillColor(WHITE if ri == 0 else DARK)
        c.setFont("Helvetica-Bold" if ri == 0 else "Helvetica", 8.5)
        c.drawString(rx + 4, ty - ri*30 + 9, cell)
        rx += cw

# not indexed note
box(18, 30, W-36, 60, fill=colors.HexColor("#fff0f0"))
box(18, 30, 5, 60, fill=RED)
c.setFillColor(RED); c.setFont("Helvetica-Bold", 9)
c.drawString(30, 75, "What we did NOT index — and why:")
c.setFillColor(DARK); c.setFont("Helvetica", 8.5)
c.drawString(30, 60, "bill_item.description — text search unused, adds write cost with no read benefit")
c.drawString(30, 45, "occupied BIT — only 2 values (low cardinality): index won't help, DB scans ~50% of rows anyway")


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 10 — SECURITY
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("9 · Security & Auditing",
           "JWT, BCrypt, SQL injection prevention, database audit triggers")

CW2 = 443
# Left column
card(18, 305, CW2, 155, "JWT — JSON Web Token (stateless auth)", [
    "1.  POST /api/auth/login  →  server returns signed token",
    "2.  Client sends: Authorization: Bearer <token>",
    "3.  Server verifies signature — NO database call needed",
    "Token payload: username, role, expiry  (HS512 signed)",
    "Stateless: server holds no session state at all",
], title_bg=MYSQL, body_bg=WHITE, body_fg=DARK, tsize=9, bsize=9, leading=18)

card(18, 168, CW2, 120, "BCrypt — one-way password hashing", [
    '"admin123"  →  BCrypt  →  "$2a$10$N9qo8uLO..."',
    "One-way: cannot reverse hash back to password",
    "Salt built-in: same password → different hash each time",
    "Prevents rainbow table attacks",
], title_bg=RED, body_bg=WHITE, body_fg=DARK, tsize=9, bsize=9, leading=18)

card(18, 55, CW2, 98, "Roles & Access Control", [
    "ADMIN — full access (delete, create, view all)",
    "STAFF — read/update reservations, cannot delete",
    "CLEANER — view/update cleaning tasks only",
    "Enforced via Spring Security @PreAuthorize",
], title_bg=NAVY, body_bg=WHITE, body_fg=DARK, tsize=9, bsize=9, leading=18)

# Right column
card(479, 305, CW2, 155, "SQL Injection Prevention", [
    "UNSAFE: String q = \"SELECT * FROM guest WHERE email = '\"",
    "         + email + \"'\"  → attacker sends: ' OR '1'='1",
    "SAFE: Spring Data JPA parameterized queries:",
    "  findByEmail(String email)",
    "  → SELECT * FROM guest WHERE email = ?",
    "  Input is treated as DATA, never as SQL code",
], title_bg=RED, body_bg=WHITE, body_fg=DARK, tsize=9, bsize=9, leading=18)

card(479, 168, CW2, 120, "Database Audit Triggers (05_audit.sql)", [
    "Triggers on: reservation, bill, bill_item, guest, room",
    "Each INSERT/UPDATE/DELETE appends a row to audit_log",
    "Stores: old_values (JSON), new_values (JSON),",
    "        changed_by (CURRENT_USER()), changed_at",
    "App does NOTHING — database fires trigger automatically",
], title_bg=ACCENT, body_bg=WHITE, body_fg=DARK, tsize=9, bsize=9, leading=18)

code_box(479, 55, CW2, 98, [
    "AFTER UPDATE ON reservation FOR EACH ROW",
    "INSERT INTO audit_log(...) VALUES (",
    "  'reservation','UPDATE', NEW.reservation_id,",
    "  JSON_OBJECT('status', OLD.status),",
    "  JSON_OBJECT('status', NEW.status),",
    "  CURRENT_USER());",
], fsize=8)


# ═══════════════════════════════════════════════════════════════════════════
# SLIDE 11 — TRADE-OFFS
# ═══════════════════════════════════════════════════════════════════════════
new_slide()
header_bar("10 · Trade-offs & Reflections",
           "Why three databases — and what we learned")

why = [
    ("Why MySQL?",   MYSQL, [
        "Structured, relational hotel data",
        "ACID guarantees for payments",
        "Complex multi-table JOINs & views",
        "Referential integrity via FK"]),
    ("Why MongoDB?", MONGO, [
        "Full guest history in one call",
        "Unstructured AI/LLM chat logs",
        "Schema-free — add fields freely",
        "Read-heavy with nested data"]),
    ("Why Neo4j?",   NEO, [
        "Multi-hop relationship traversal",
        "Natural: who cleaned what booked by whom",
        "Graph queries > recursive SQL",
        "Future: recommendation engine"]),
]
for i, (title, col, points) in enumerate(why):
    cx = 18 + i * 302
    box(cx, 355, 288, 105, fill=col)
    c.setFillColor(WHITE); c.setFont("Helvetica-Bold", 11)
    c.drawCentredString(cx + 144, 432, title)
    c.setFillColor(WHITE); c.setFont("Helvetica", 9)
    for j, pt in enumerate(points):
        c.drawString(cx + 12, 416 - j * 16, "• " + pt)

card(18, 55, W-36, 275, "What we would do differently", [
    "Date types: stored dates as String in Neo4j instead of native date/datetime → less efficient,"
    " cannot use Cypher date arithmetic",
    "",
    "Sync strategy: manual POST /api/migrate → in production: Change Data Capture (CDC) + Kafka"
    " for automatic event-driven propagation",
    "",
    "Multi-store Spring Boot: JPA + Neo4j TX managers do NOT auto-configure together in Spring Boot 4"
    " → required explicit @Bean for both (JpaTransactionManager @Primary + Neo4jTransactionManager)",
    "",
    "Data consistency: MongoDB/Neo4j counts drift from MySQL if migration is not re-run after changes",
], title_bg=YELLOW, body_bg=WHITE, body_fg=DARK, tsize=10, bsize=9, leading=19)


# ── Save ──────────────────────────────────────────────────────────────────────
c.save()
print(f"PDF gemt: {OUTPUT}")
