package mk.finki.shipments;

public class RouteCodeGeneratorTest {

    public static void main(String[] args) {

        RouteCodeGenerator generator =
                new RouteCodeGenerator();

        String route1 = generator.generate(
                "Sigal Diametamor Grcija",
                "Atlantik FM Skopje"
        );

        String route2 = generator.generate(
                "Sigal Diametamor Grcija",
                "Atlantik FM Skopje"
        );

        String route3 = generator.generate(
                "Best fruit Grcija",
                "Merfruit Brcko Bih"
        );

        System.out.println("Route 1: " + route1);
        System.out.println("Route 2: " + route2);
        System.out.println("Route 3: " + route3);

        if (!route1.equals(route2)) {
            throw new RuntimeException(
                    "Same route generated different routeCodes!"
            );
        }

        System.out.println("RouteCodeGenerator test passed.");
    }
}