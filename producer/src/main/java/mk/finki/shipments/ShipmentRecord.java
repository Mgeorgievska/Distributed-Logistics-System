package mk.finki.shipments;

public class ShipmentRecord {

    private final String date;
    private final String carrier;
    private final String goods;
    private final String exporter;
    private final String exporterCountry;
    private final String importer;
    private final String importerCountry;
    private final String declarationType;
    private final String declarationNumber;
    private final double revenueMKD;
    private final double revenueEUR;
    private final String routeCode;

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