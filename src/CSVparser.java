//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//public class CSVparser {
//
//    // Loads the Map from our CSV file
//    //public static Map loadMap(String filePath) throws IOException {
//
//
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            br.readLine(); // skip header
//            br.readLine(); // skip blank
//
//            while ((line = br.readLine()) != null) {
//                String[] p = line.split(",", -1);
//
//                // Node Parsing (Index 0, 1, 2, 3)
//                if (p.length > 0 && !p[0].trim().isEmpty()) {
//                    String id = p[0].trim();
//                    String nodeFlags = (p.length > 1) ? p[1].trim() : "";
//
//                    double lat = 0, lon = 0;
//                    try {
//                        if (p.length > 2 && !p[2].trim().isEmpty()) lat = Double.parseDouble(p[2].trim());
//                        if (p.length > 3 && !p[3].trim().isEmpty()) lon = Double.parseDouble(p[3].trim());
//                    } catch (NumberFormatException ignored) {
//                    }
//
//                    MapNode node = map.getMapNode(id);
//                    if (node == null) {
//                        map.addMapNode(id, new MapNode(id, nodeFlags, lat, lon));
//                    } else {
//                        // Update the placeholder with real data
//                        node.updateData(nodeFlags, lat, lon);
//                    }
//                }
//
//                // Connection Parsing (Index 5, 6, 7, 8)
//                if (p.length > 8 && !p[5].trim().isEmpty()) {
//                    String connId = p[5].trim();
//                    String connFlags = p[6].trim();
//                    String n1 = p[7].trim();
//                    String n2 = p[8].trim();
//
//                    map.addMapConnection(connId, new MapConnection(connId, connFlags, n1, n2));
//
//                    ensureNodeExists(map, n1).registerConnection(connId);
//                    ensureNodeExists(map, n2).registerConnection(connId);
//                }
//            }
//        }
//        System.out.println("CSVparser successfully loaded " + map.getMapNodeCount() + " nodes from " + filePath + ".");
//        System.out.println("CSVparser successfully loaded " + map.getMapConnectionCount() + " connections from " + filePath + ".");
//        map.printNodesMissingCoordinates();
//        map.printNodesMissingFlags();
//        //System.out.println(map);
//
//        return map;
//    }
//
//    // Helper to get a node or create a "empty" one if we haven't reached its coordinate row yet
//    private static MapNode ensureNodeExists(Map map, String id) {
//        if (!map.containsMapNode(id)) {
//            map.addMapNode(id, new MapNode(id, "", 0, 0));
//        }
//        return map.getMapNode(id);
//    }
//
//}
