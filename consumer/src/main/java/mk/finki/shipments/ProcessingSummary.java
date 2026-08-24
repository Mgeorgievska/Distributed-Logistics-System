package mk.finki.shipments;

public class ProcessingSummary {

    private final String processingType;
    private final int shipments;
    private final int validShipments;
    private final int invalidShipments;
    private final long totalTime;
    private final double throughput;
    private final int threads;

    public ProcessingSummary(
            String processingType,
            int shipments,
            int validShipments,
            int invalidShipments,
            long totalTime,
            double throughput,
            int threads) {

        this.processingType = processingType;
        this.shipments = shipments;
        this.validShipments = validShipments;
        this.invalidShipments = invalidShipments;
        this.totalTime = totalTime;
        this.throughput = throughput;
        this.threads = threads;
    }

    public String getProcessingType() {
        return processingType;
    }

    public int getShipments() {
        return shipments;
    }

    public int getValidShipments() {
        return validShipments;
    }

    public int getInvalidShipments() {
        return invalidShipments;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public double getThroughput() {
        return throughput;
    }

    public int getThreads() {
        return threads;
    }
}