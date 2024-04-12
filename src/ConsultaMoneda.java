import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class ConsultaMoneda {

    Scanner teclado = new Scanner(System.in);

    // Método para consultar la API
    public Moneda consultaMonedaJson(String moneda){

        URI direccion = URI.create("https://v6.exchangerate-api.com/v6/880cc4692801c1aa00592d99/latest/" + moneda);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .build();
        try {
            HttpResponse<String>response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), Moneda.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Método para mostrar el menú
    public void muestraMenu(){
        System.out.println("*******************************");
        System.out.println("Convertidor de moneda online\n");
        System.out.println("""
                1. USD a ARS
                2. ARS a USD
                3. USD a BRL
                4. BRL a USD
                5. USD a COP
                6. COP a USD
                7. Salir
                """);
        System.out.println("Elija una opción válida");
        System.out.println("*******************************");
    }

    // Método para la conversión de la moneda
    public void convierteMoneda(String monedaOrigen, String monedaDestino){
        System.out.println("Ingrese el valor a convertir: ");
        double valorAConvertir = teclado.nextDouble();

        Moneda resultado = consultaMonedaJson(monedaOrigen);

        Map<String, Double> tasasDeConversion = resultado.conversion_rates();
        Double tasaDeConversion = tasasDeConversion.get(monedaDestino);

        if (tasaDeConversion != null) {
            double valorConvertido = valorAConvertir * tasaDeConversion;
            System.out.println("El valor de " + valorAConvertir + " " + monedaOrigen + " es equivalente a "
                    + valorConvertido + " " + monedaDestino);
        } else {
            System.out.println("No se encontró la tasa de conversión para " + monedaOrigen + " a " + monedaDestino);
        }
    }

}
