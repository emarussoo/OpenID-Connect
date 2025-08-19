package com.example.openidconnect.controller;

import com.example.openidconnect.properties.GoogleProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class MainController {

    private final GoogleProperties googleProperties = new GoogleProperties();


    private String token;
    private String CLIENT_ID = googleProperties.getClientId();
    private String CLIENT_SECRET = googleProperties.getClientSecret();
    private static final String REDIRECT_URI = "http://localhost:8888";
    private static final String SCOPE = "openid email profile";

    public void login() {
        String authUrl;
        try {
            authUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                    "?client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8") +
                    "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
                    "&response_type=code" +
                    "&scope=" + URLEncoder.encode(SCOPE, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        openBrowser(authUrl);
        try {
            String code = waitForCode();
            String accessToken = getAccessToken(code);
            this.token = accessToken;
            System.out.println("Access token: " + accessToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void viewUserInfo() {
        final String REDIRECT_URI = "http://localhost:8080/callback";
        final String SCOPE = "openid email profile";
        try {
            URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo"); // endpoint OpenID Connect per info utente
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + this.getToken());

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Scanner s = new Scanner(conn.getInputStream()).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                System.out.println("Info utente: " + result);
            } else {
                System.out.println("Errore chiamata API: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Chiamata al backend per ottenere info utente...");
    }

    public void logout() {
        try {
            URL url = new URL("https://accounts.google.com/o/oauth2/revoke?token=" + this.getToken());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Logout effettuato con successo (token revocato).");
            } else {
                System.out.println("Errore nel logout: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String waitForCode() throws IOException {
        final ServerSocket server = new ServerSocket(8888);
        System.out.println("In attesa del login...");

        Socket socket = server.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = reader.readLine();
        String code = null;
        if (line != null && line.startsWith("GET")) {
            int start = line.indexOf("code=") + 5;
            int end = line.indexOf(" ", start);
            code = URLDecoder.decode(line.substring(start, end), "UTF-8");
        }

        // Risposta semplice al browser
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n");
        writer.write("<h1>Login effettuato! Puoi chiudere questa finestra.</h1>");
        writer.flush();
        writer.close();
        socket.close();
        server.close();

        System.out.println("Code ricevuto: " + code);
        return code;
    }

    private String getAccessToken(String code) throws Exception {
        String body = "code=" + URLEncoder.encode(code, "UTF-8") +
                "&client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8") +
                "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
                "&grant_type=authorization_code";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://oauth2.googleapis.com/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.body());
        String token = node.get("access_token").asText();
        System.out.println("Access Token: " + token);
        return token;
    }

    public String getToken(){
        return this.token;
    }

    public static void openBrowser(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                // Windows
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                // macOS
                Runtime.getRuntime().exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                // Linux
                Runtime.getRuntime().exec("xdg-open " + url);
            } else {
                System.out.println("Apri manualmente il link nel browser:");
                System.out.println(url);
            }
            System.out.println("Browser aperto per il login!");
        } catch (IOException e) {
            System.out.println("Errore nell'aprire il browser: " + e.getMessage());
        }
    }
}






