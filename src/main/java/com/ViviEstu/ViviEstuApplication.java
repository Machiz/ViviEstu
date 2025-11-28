package com.ViviEstu;

import com.ViviEstu.model.entity.Distrito;
import com.ViviEstu.repository.DistritoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class ViviEstuApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViviEstuApplication.class, args);
    }

    @Bean
    CommandLineRunner initDistritos(DistritoRepository distritoRepository) {
        // La expresión lambda se ejecutará al inicio de la aplicación
        return args -> {
            // --- Datos de Ejemplo para Distritos de Lima ---

            // 1. Miraflores
            Distrito miraflores = new Distrito(
                    null, // ID (será generado)
                    "Miraflores",
                    "Distrito turístico y comercial, conocido por sus parques y acantilados con vista al Pacífico.",
                    800000, // Precio Promedio (ejemplo: precio en USD)
                    "Residencial/Comercial",
                    "https://limabywalking.com/wp-content/uploads/2025/11/free-walking-tour-miraflores-lima.webp", // URL de imagen de ejemplo
                    5, // Seguridad (Escala del 1 al 5)
                    LocalDate.now()
            );

            // 2. Santiago de Surco
            Distrito surco = new Distrito(
                    null,
                    "Santiago de Surco",
                    "Gran distrito residencial con amplias áreas verdes y centros comerciales importantes.",
                    550000,
                    "Residencial",
                    "https://urbania.pe/blog/wp-content/uploads/2018/10/imovelwebcomunicacaoltda_quintoandarperu_image_487.jpeg",
                    4,
                    LocalDate.now()
            );

            // 3. San Isidro
            Distrito sanIsidro = new Distrito(
                    null,
                    "San Isidro",
                    "Principal centro financiero de Lima, hogar de embajadas y edificios corporativos.",
                    950000,
                    "Financiero/Residencial",
                    "https://upload.wikimedia.org/wikipedia/commons/b/bf/Sanisidroskyscrapers.jpg",
                    5,
                    LocalDate.now()
            );

            // 4. Lince
            Distrito lince = new Distrito(
                    null,
                    "Lince",
                    "Distrito céntrico y pequeño, conocido por su vida comercial y su ubicación estratégica.",
                    300000,
                    "Comercial/Urbano",
                    "https://upload.wikimedia.org/wikipedia/commons/1/1a/Est%C3%A0tua_de_Joan_Pau_II_a_Lince%2C_Lima.jpg",
                    3,
                    LocalDate.now()
            );

            // 5. Los Olivos
            Distrito olivos = new Distrito(
                    null,
                    "Los Olivos",
                    "Distrito popular en el Cono Norte, con alta densidad poblacional y fuerte actividad comercial.",
                    150000,
                    "Urbano",
                    "https://blog.properati.com.pe/wp-content/uploads/2022/12/los-olivos-distrito-lima-properati-1.jpg",
                    3,
                    LocalDate.now()
            );

            // --- Guardar los Distritos en la Base de Datos ---
            distritoRepository.save(miraflores);
            distritoRepository.save(surco);
            distritoRepository.save(sanIsidro);
            distritoRepository.save(lince);
            distritoRepository.save(olivos);

            // También puedes usar saveAll para guardar una lista de una vez
            // List<Distrito> distritos = List.of(miraflores, surco, sanIsidro, lince, olivos);
            // distritoRepository.saveAll(distritos);
        };
    }

}
