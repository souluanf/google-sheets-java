package dev.luanfernandes.sheets.infraestruture.implementation;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import dev.luanfernandes.sheets.domain.service.GoogleSheetsService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GoogleSheetsServiceImpl implements GoogleSheetsService {
    private final GoogleSheetsProperties properties;
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    @SneakyThrows
    @Override
    public Credential getCredentials(final NetHttpTransport httpTransport){
        InputStream in = GoogleSheetsServiceImpl.class.getResourceAsStream(properties.getCredentialsFilePath());
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + properties.getCredentialsFilePath());
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(properties.getTokensDirectoryPath())))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    @SneakyThrows
    @Override
    public void readSheet(String spreadsheetId, String range) {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Sheets service = new Sheets
                .Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(properties.getApplicationName()).build();
        ValueRange response = service
                .spreadsheets()
                .values()
                .get(spreadsheetId, range)
                .execute();



        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            log.warn("No data found.");
        } else {
            for (var row : values) {
                log.info("{}", row);
            }
        }
    }

    @Override
    public void testService(){
        readSheet(properties.getSpreadSheetId(),properties.getRangeData());
    }

}