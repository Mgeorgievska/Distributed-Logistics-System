package mk.finki.shipments;

public class Shipment {

    private int shipmentId;
    private String routeCode;
    private String origin;
    private String destination;
    private double weight;
    private String status;

    public Shipment() {
    }

    public Shipment(int shipmentId,
                    String routeCode,
                    String origin,
                    String destination,
                    double weight,
                    String status) {

        this.shipmentId = shipmentId;
        this.routeCode = routeCode;
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.status = status;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}