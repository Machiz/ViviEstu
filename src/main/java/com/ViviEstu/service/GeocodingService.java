package com.ViviEstu.service;

import com.ViviEstu.model.dto.CoordenadasDTO;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;

@Service
public class GeocodingService {

    private final GeoApiContext context;

    public GeocodingService(GeoApiContext context) {
        this.context = context;
    }

    public CoordenadasDTO getCoordinates(String address) {
        try {
            // Llama al API de Geocodificación
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();

            // Verifica si se obtuvieron resultados
            if (results != null && results.length > 0) {
                LatLng location = results[0].geometry.location;
                double latitude = location.lat;
                double longitude = location.lng;

                System.out.println("Dirección '" + address + "' geocodificada con Google a: Lat=" + latitude + ", Lon=" + longitude);
                return new CoordenadasDTO(latitude, longitude);
            }

        } catch (Exception e) {
            // En una app real, usa un logger.
            System.err.println("Error al geocodificar con Google Maps API: " + e.getMessage());
            // Asegúrate de cerrar el contexto si hay un error grave o al apagar la app
            // context.shutdown();
        }

        return null; // Retorna null si no se encuentran resultados o si hay un error
    }
}