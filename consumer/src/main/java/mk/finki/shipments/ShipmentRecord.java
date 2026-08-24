package mk.finki.shipments;

public class ShipmentRecord {

    private String date;
    private String routeCode;
    private String carrier;
    private String goods;
    private String exporter;
    private String exporterCountry;
    private String importer;
    private String importerCountry;
    private String declarationType;
    private String declarationNumber;
    private double revenueMKD;
    private double revenueEUR;

    public ShipmentRecord() {
    }

    public ShipmentRecord(
            String date,
            String routeCode,
            String carrier,
            String goods,
            String exporter,
            String exporterCountry,
            String importer,
            String importerCountry,
            String declarationType,
            String declarationNumber,
            double revenueMKD,
            double revenueEUR) {

        this.date = date;
        this.routeCode = routeCode;
        this.carrier = carrier;
        this.goods = goods;
        this.exporter = exporter;
        this.exporterCountry = exporterCountry;
        this.importer = importer;
        this.importerCountry = importerCountry;
        this.declarationType = declarationType;
        this.declarationNumber = declarationNumber;
        this.revenueMKD = revenueMKD;
        this.revenueEUR = revenueEUR;
    }

    public String getDate() {
        return date;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getGoods() {
        return goods;
    }

    public String getExporter() {
        return exporter;
    }

    public String getExporterCountry() {
        return exporterCountry;
    }

    public String getImporter() {
        return importer;
    }

    public String getImporterCountry() {
        return importerCountry;
    }

    public String getDeclarationType() {
        return declarationType;
    }

    public String getDeclarationNumber() {
        return declarationNumber;
    }

    public double getRevenueMKD() {
        return revenueMKD;
    }

    public double getRevenueEUR() {
        return revenueEUR;
    }

    @Override
    public String toString() {
        return "ShipmentRecord{" +
                "date='" + date + '\'' +
                ", routeCode='" + routeCode + '\'' +
                ", carrier='" + carrier + '\'' +
                ", goods='" + goods + '\'' +
                ", exporter='" + exporter + '\'' +
                ", exporterCountry='" + exporterCountry + '\'' +
                ", importer='" + importer + '\'' +
                ", importerCountry='" + importerCountry + '\'' +
                ", declarationType='" + declarationType + '\'' +
                ", declarationNumber='" + declarationNumber + '\'' +
                ", revenueMKD=" + revenueMKD +
                ", revenueEUR=" + revenueEUR +
                '}';
    }
}