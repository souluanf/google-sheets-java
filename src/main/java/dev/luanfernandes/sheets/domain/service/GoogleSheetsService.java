package dev.luanfernandes.sheets.domain.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleSheetsService {
    Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException;

    void readSheet(String spreadsheetId, String range) throws GeneralSecurityException, IOException;

    void testService();
}
