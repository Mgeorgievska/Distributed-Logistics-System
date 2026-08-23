package mk.finki.shipments;

import java.util.List;

public class OsmRoad {

    private final long id;
    private final String name;
    private final String highwayType;
    private final List<Long> nodeIds;

    public OsmRoad(
            long id,
            String name,
            String highwayType,
            List<Long> nodeIds) {

        this.id = id;
        this.name = name;
        this.highwayType = highwayType;
        this.nodeIds = nodeIds;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHighwayType() {
        return highwayType;
    }

    public List<Long> getNodeIds() {
        return nodeIds;
    }

    @Override
    public String toString() {

        return "OsmRoad{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", highwayType='" + highwayType + '\'' +
                ", nodes=" + nodeIds.size() +
                '}';
    }
}