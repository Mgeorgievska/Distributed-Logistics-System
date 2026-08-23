package mk.finki.shipments;

import java.util.List;

public class OsmTest {

    public static void main(String[] args) {

    System.setOut(new java.io.PrintStream(
            new java.io.FileOutputStream(java.io.FileDescriptor.out),
            true,
            java.nio.charset.StandardCharsets.UTF_8
    ));

    System.out.println("=================================");
    System.out.println("OSM DATA LOADER TEST");
    System.out.println("=================================");

    String filePath = "../data/macedonia-260821.osm.pbf";

    System.out.println("OSM file:");
    System.out.println(filePath);

    OsmDataLoader loader = new OsmDataLoader(filePath);

    List<OsmRoad> roads = loader.loadRoads(1000);

    System.out.println();
    System.out.println("=================================");
    System.out.println("RESULT");
    System.out.println("=================================");

    System.out.println("Total roads loaded: " + roads.size());

    System.out.println();
    System.out.println("First roads:");

    int numberToShow = Math.min(20, roads.size());

    for (int i = 0; i < numberToShow; i++) {

        OsmRoad road = roads.get(i);

        System.out.println(
                (i + 1) + ". " +
                "ID=" + road.getId() +
                " | Name=" + road.getName() +
                " | Type=" + road.getHighwayType() +
                " | Nodes=" + road.getNodeIds().size()
        );
    }

    System.out.println("=================================");
}
}