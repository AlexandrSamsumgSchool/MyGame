package com.example.mygame;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends AsyncTask<Void, String, Void> {
    private static final String SERVER_IP = "192.168.0.72";
    private static final int SERVER_PORT = 8081;
    public Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Log.d("SimpleClient", "Подключение к серверу установлено");
            updatePlayerData(Game.getPlayer());
        } catch (IOException e) {
            Log.e("SimpleClient", "Ошибка подключения", e);
        } finally {
            closeConnections();
        }
        return null;
    }

    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            Log.d("SimpleClient", "Соединение закрыто");
        } catch (IOException e) {
            Log.e("SimpleClient", "Ошибка закрытия соединения", e);
        }
    }

    // Метод, проверяющий, съеден ли игрок, и разрывающий соединение
    private void disconnectIfEaten(Player player) {
        if (player.isEaten) {
            player.radius = 0;
            Log.d("SimpleClient", "Игрок съеден");
            closeConnections();
            cancel(true);
        }
    }

    public void sendPlayerData(Player player) {
        // Если игрок съеден, разрываем соединение
        if (player.isEaten) {
            player.radius = 0;
            disconnectIfEaten(player);
            return;
        }
        try {
            String data =
                    String.valueOf(player.positionX) + ',' +
                            String.valueOf(player.positionY) + ',' +
                            String.valueOf(player.radius * Math.pow(1.05, player.Scale)) + ',' +
                            String.valueOf(player.velocityX) + ',' +
                            String.valueOf(player.velocityY) + ',' +
                            player.name + ',' +
                            String.valueOf(player.isEaten) + ',' +
                            String.valueOf(player.color);
            out.println(data);
            Log.d("SimpleClient", "Отправлено на сервер: " + data);
        } catch (Exception e) {
            Log.e("SimpleClient", "Ошибка отправки данных", e);
        }
    }

    private void updatePlayerData(Player player) {
        String response;
        try {
            while ((response = in.readLine()) != null) {
                // Периодически проверяем, не изменилось ли состояние игрока
                if (player.isEaten) {
                    disconnectIfEaten(player);
                    break;
                }
                String[] playersData = response.split("Игрок: ");
                for (String playerData : playersData) {
                    if (!playerData.trim().isEmpty()) {
                        playerData = playerData.trim();
                        if (playerData.endsWith(";")) {
                            playerData = playerData.substring(0, playerData.length() - 1).trim();
                        }
                        String[] parts = playerData.split(":"); // Разделяем данные игрока по двоеточию

                        if (parts.length >= 5) {
                            try {
                                String socketId = parts[0].trim();
                                double positionX = Double.parseDouble(parts[1].trim());
                                double positionY = Double.parseDouble(parts[2].trim());
                                double radius = Double.parseDouble(parts[3].trim()) * Math.pow(1.05, player.Scale);
                                int color = Integer.parseInt(parts[4].trim());
                                String name = parts[7].trim();
                                try {
                                    if (!Game.findPl(socketId)) {
                                        Bot updatedPlayer = new Bot(color, positionX, positionY, radius, name);
                                        Game.setPlayers(socketId, updatedPlayer);
                                    } else {
                                        Game.updatePlayer(socketId, positionX, positionY);
                                    }
                                } catch (Exception e) {
                                    // обработка исключения, если необходимо
                                }
                            } catch (NumberFormatException e) {
                                Log.e("SimpleClient", "Ошибка парсинга данных игрока", e);
                            }
                        } else {
                            Log.e("SimpleClient", "Неверное количество данных для игрока: " + playerData);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.e("SimpleClient", "Ошибка получения данных", e);
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("SimpleClient", values[0]);
    }
}
