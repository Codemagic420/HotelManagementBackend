// Neo4j Export Script - Hotel Management System
// Export all nodes and relationships to JSON format

// Get all guests
MATCH (g:Guest)
RETURN g AS node, "Guest" AS type
UNION ALL

// Get all rooms
MATCH (r:Room)
RETURN r AS node, "Room" AS type
UNION ALL

// Get all reservations
MATCH (res:Reservation)
RETURN res AS node, "Reservation" AS type
UNION ALL

// Get all relationships
MATCH (a)-[r]->(b)
RETURN {
  fromNode: labels(a),
  toNode: labels(b),
  relationship: type(r),
  properties: properties(r)
} AS relationship
