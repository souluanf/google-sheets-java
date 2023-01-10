package dev.luanfernandes.sheets.infraestruture.implementation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Validated
@Component
@ConfigurationProperties("google.sheets")
public class GoogleSheetsProperties {
    @NotBlank
    private String applicationName;
    @NotBlank
    private String tokensDirectoryPath;
    @NotBlank
    private String credentialsFilePath;
    @NotBlank
    private String spreadSheetId;
    @NotBlank
    private String rangeData;
}