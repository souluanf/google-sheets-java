package dev.luanfernandes.sheets.core.init;

import dev.luanfernandes.sheets.infraestruture.implementation.GoogleSheetsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile(value = "dev")
@Configuration
@AllArgsConstructor
public class TesteSheets implements CommandLineRunner {
    private final GoogleSheetsServiceImpl googleSheetsService;

    @Override
    public void run(String... args) {
        googleSheetsService.testService();
    }
}