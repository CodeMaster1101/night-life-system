package com.mile.mail.server.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;

@Service
class GmailService {

  @Value("${user-id}")
  private static String USER_ID;
  private static final String APPLICATION_NAME = "mail-server";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String[] SCOPES = {GmailScopes.GMAIL_READONLY};
  static Gmail gmail;

  GmailService() throws IOException, GeneralSecurityException {
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    Credential credential = authorize(httpTransport);
    gmail = new Gmail.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
  }

  private Credential authorize(HttpTransport httpTransport) {
    try {
      InputStream in = new FileInputStream("client_secret.json");
      GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
      GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Arrays.asList(SCOPES))
          .setAccessType("offline")
          .setApprovalPrompt("force")
          .setClientId("380252503641-dkr391bq6blm6id8oa2fk7r3scvhfsqj.apps.googleusercontent.com")
          .build();
      LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8082).build();
      return new AuthorizationCodeInstalledApp(flow, receiver).authorize(USER_ID);
    } catch (Exception e) {
      throw new EmailFetchingException("something went wrong when authorizing", new RuntimeException());
    }
  }
}

