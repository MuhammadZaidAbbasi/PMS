package com.example.system;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static final int PORT = 12345;
    private static Map<String, PrintWriter> clientWriters = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        String username = null;
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Step 1: Receive username
            username = in.readLine();
            if (username == null || username.isBlank()) {
                socket.close();
                return;
            }
            clientWriters.put(username, out);
            System.out.println(username + " connected.");

            // Step 2: Listen for messages
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("TO:")) {
                    String[] parts = message.substring(3).split(":", 2);
                    if (parts.length == 2) {
                        String receiver = parts[0];
                        String text = parts[1];
                        sendMessage(username, receiver, text);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Connection lost with " + username);
        } finally {
            if (username != null) {
                clientWriters.remove(username);
                System.out.println(username + " disconnected.");
            }
            try {
                socket.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    private static void sendMessage(String sender, String receiver, String text) {
        PrintWriter receiverOut = clientWriters.get(receiver);
        if (receiverOut != null) {
            receiverOut.println("FROM:" + sender + ":" + text);
        } else {
            System.out.println("User " + receiver + " not found.");
        }
    }
}
