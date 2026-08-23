package mk.finki.shipments;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat;
import crosby.binary.file.BlockInputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OsmDataLoader {

    private final String filePath;

    public OsmDataLoader(String filePath) {
        this.filePath = filePath;
    }

    public List<OsmRoad> loadRoads(int maxRoads) {

        List<OsmRoad> roads = new ArrayList<>();

        System.out.println("=================================");
        System.out.println("LOADING OSM DATA");
        System.out.println("=================================");
        System.out.println("File: " + filePath);

        try (FileInputStream inputStream =
                     new FileInputStream(filePath)) {

            RoadParser parser =
                    new RoadParser(roads, maxRoads);

            try (BlockInputStream blockInputStream =
                         new BlockInputStream(inputStream, parser)) {

                blockInputStream.process();
            }

            System.out.println();
            System.out.println("OSM file processed successfully.");
            System.out.println(
                    "Total roads loaded: " + roads.size()
            );

        } catch (IOException e) {

            System.err.println("Error reading OSM file:");
            e.printStackTrace();

        } catch (Exception e) {

            System.err.println("Error processing OSM file:");
            e.printStackTrace();
        }

        return roads;
    }

    /**
     * Parser compatible with osmpbf 1.6.1.
     */
    private static class RoadParser extends BinaryParser {

        private final List<OsmRoad> roads;
        private final int maxRoads;

        RoadParser(
                List<OsmRoad> roads,
                int maxRoads) {

            this.roads = roads;
            this.maxRoads = maxRoads;
        }

        /**
         * Required by BlockReaderAdapter.
         */
        @Override
        public void complete() {
            // Parsing finished.
        }

        /**
         * OSM header.
         */
        @Override
        protected void parse(
                Osmformat.HeaderBlock header) {

            // Header is not needed.
        }

        /**
         * Normal OSM nodes.
         */
        @Override
        protected void parseNodes(
                List<Osmformat.Node> nodes) {

            // Nodes are not processed separately.
        }

        /**
         * Dense OSM nodes.
         */
        @Override
        protected void parseDense(
                Osmformat.DenseNodes nodes) {

            // Dense nodes are not processed separately.
        }

        /**
         * OSM relations.
         */
        @Override
        protected void parseRelations(
                List<Osmformat.Relation> relations) {

            // Relations are not needed.
        }

        /**
         * Process OSM ways.
         */
        @Override
        protected void parseWays(
                List<Osmformat.Way> ways) {

            if (roads.size() >= maxRoads) {
                return;
            }

            for (Osmformat.Way way : ways) {

                if (roads.size() >= maxRoads) {
                    return;
                }

                String highwayType = null;
                String roadName = null;

                /*
                 * Read OSM tags.
                 *
                 * Each key/value pair is represented by
                 * indexes into the StringTable.
                 */
                for (int i = 0; i < way.getKeysCount(); i++) {

                    int keyId = way.getKeys(i);
                    int valueId = way.getVals(i);

                    String key = getStringById(keyId);
                    String value = getStringById(valueId);

                    if ("highway".equals(key)) {
                        highwayType = value;
                    }

                    if ("name".equals(key)) {
                        roadName = value;
                    }
                }

                /*
                 * We only want actual roads/highways.
                 *
                 * OSM highway values can include:
                 *
                 * motorway
                 * trunk
                 * primary
                 * secondary
                 * tertiary
                 * residential
                 * service
                 * unclassified
                 * living_street
                 * road
                 * etc.
                 */
                if (highwayType == null) {
                    continue;
                }

                /*
                 * Decode the delta-encoded node references.
                 */
                List<Long> nodeIds = new ArrayList<>();

                long previousReference = 0;

                for (long delta : way.getRefsList()) {

                    previousReference += delta;

                    nodeIds.add(previousReference);
                }

                /*
                 * If the road has no name, use a readable
                 * fallback value.
                 */
                if (roadName == null || roadName.isBlank()) {
                    roadName = "Unnamed road";
                }

                OsmRoad road =
                        new OsmRoad(
                                way.getId(),
                                roadName,
                                highwayType,
                                nodeIds
                        );

                roads.add(road);
            }
        }
    }
}