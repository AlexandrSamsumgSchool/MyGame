package com.example.mygame;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends AsyncTask<Void, String, Void> {
    //"192.168.0.72""145.255.3.185"
    private static final String SERVER_IP = "145.255.3.185";
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

    public void sendPlayerData(Player player) {
        try {
            String data =
                    String.valueOf(player.positionX) + ',' +
                    String.valueOf(player.positionY) + ',' +
                    String.valueOf(player.radius * Math.pow(1.05,player.Scale)) + ',' +//*Math.pow(1.05,player.Scale)
                    String.valueOf(player.velocityX) + ',' +
                    String.valueOf(player.velocityY)+ ',' +
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
                String[] playersData = response.split("Игрок: ");
                for (String playerData : playersData) {
                    if (!playerData.trim().isEmpty()) {
                        // Убираем лишние пробелы и символы
                        playerData = playerData.trim();

                        if (playerData.endsWith(";")) {
                            playerData = playerData.substring(0, playerData.length() - 1).trim();
                        }

                        String[] parts = playerData.split(":"); // Разделяем данные игрока по двоеточию

                        // Проверяем, что количество частей соответствует ожидаемому
                        if (parts.length >= 5) {
                            try {
                                String socketId = parts[0].trim();
                                double positionX = Double.parseDouble(parts[1].trim());
                                double positionY = Double.parseDouble(parts[2].trim());
                                double radius = Double.parseDouble(parts[3].trim())* Math.pow(1.05,player.Scale);
                                int color = Integer.parseInt(parts[4].trim());
                                String name = parts[7].trim();
                                try {
                                    // Проверяем, существует ли игрок с таким socketId
                                    if (!Game.findPl(socketId)) {
                                        // Создаем нового игрока с обновленными данными
                                        Bot updatedPlayer = new Bot(color, positionX, positionY, radius,name);
                                        Game.setPlayers(socketId, updatedPlayer);
                                    } else {
                                    Game.updatePlayer(socketId, positionX, positionY);
                                    }
                                }
                                catch (Exception e){

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
        // Обновление UI, например, добавление сообщений в TextView
        Log.d("SimpleClient", values[0]);
    }
}
