import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Conversor {

    // Nombres completos de las monedas
    static String[] monedas = {
        "Dólar estadounidense", // USD
        "Euro",                 // EUR
        "Peso mexicano",        // MXN
        "Yen japonés",          // JPY
        "Libra esterlina",      // GBP
        "Real brasileño",       // BRL
        "Peso argentino",       // ARS
        "Dólar canadiense",     // CAD
        "Peso chileno"          // CLP
    };

    static String[] codigos = {
        "USD", "EUR", "MXN", "JPY", "GBP", "BRL", "ARS", "CAD", "CLP"
    };

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===================================");
            System.out.println("\n      CONVERSOR DE MONEDAS       ");
            System.out.println("===================================");
            for (int i = 0; i < monedas.length; i++) {
                System.out.println((i + 1) + ". " + monedas[i]);
            }
            System.out.println("0. Salir");

            System.out.print("Seleccione moneda base (número): ");
            int opcionBase = scanner.nextInt();

            if (opcionBase == 0) {
                System.out.println("¡Gracias por usar el conversor!");
                break;
            }

            System.out.print("Seleccione moneda destino (número): ");
            int opcionDestino = scanner.nextInt();

            System.out.print("Cantidad a convertir: ");
            double cantidad = scanner.nextDouble();

            String base = codigos[opcionBase - 1];
            String destino = codigos[opcionDestino - 1];

            String url = "https://v6.exchangerate-api.com/v6/b729f536a1e84ac0b29b1374/latest/" + base;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            // Validación
            if (!json.get("result").getAsString().equals("success")) {
                System.out.println("Error al obtener tasas de cambio:");
                System.out.println(json);
                continue;
            }

            JsonObject rates = json.getAsJsonObject("conversion_rates");

            if (!rates.has(destino)) {
                System.out.println("Moneda destino no válida o no disponible.");
                continue;
            }

            double tasa = rates.get(destino).getAsDouble();
            double resultado = cantidad * tasa;
            System.out.println("___________________________________________________________________");
            System.out.printf("\n%.2f %s equivalen a %.2f %s\n", cantidad, monedas[opcionBase - 1], resultado, monedas[opcionDestino - 1]);
            System.out.println("____________________________________________________________________");

        }

        scanner.close();

    }
}
