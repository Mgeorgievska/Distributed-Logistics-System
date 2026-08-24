package mk.finki.shipments;

public class ProcessingResult {

    private final String shipmentId;
    private final boolean valid;
    private final double score;
    private final long processingTime;
    private final String threadName;

    public ProcessingResult(
            String shipmentId,
            boolean valid,
            double score,
            long processingTime,
            String threadName) {

        this.shipmentId = shipmentId;
        this.valid = valid;
        this.score = score;
        this.processingTime = processingTime;
        this.threadName = threadName;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public boolean isValid() {
        return valid;
    }

    public double getScore() {
        return score;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public String getThreadName() {
        return threadName;
    }
}