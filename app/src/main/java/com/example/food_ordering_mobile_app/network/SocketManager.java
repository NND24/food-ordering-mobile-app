package com.example.food_ordering_mobile_app.network;

import android.content.Context;
import android.util.Log;

import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.NotificationViewModel;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SocketManager {
    private static Socket mSocket;
    private static SharedPreferencesHelper sharedPreferencesHelper;

    public static void connectSocket(Context context, NotificationViewModel notificationViewModel) {
        try {
            sharedPreferencesHelper = new SharedPreferencesHelper(context);
            String userId = sharedPreferencesHelper.getUserId();
            List<Notification> notifications = new ArrayList<>();

            if (userId == null) {
                Log.e("SocketManager", "User ID is not available");
                return;
            }

            mSocket = IO.socket("http://192.168.0.57:5000");
            mSocket.connect();

            mSocket.on(Socket.EVENT_CONNECT, args -> {
                Log.d("SocketManager", "Kết nối thành công đến server");
                // Khi kết nối thành công, gửi userId lên server
                mSocket.emit("registerUser", userId);
            });

            mSocket.on(Socket.EVENT_CONNECT_ERROR, args -> {
                Log.e("SocketManager", "Lỗi kết nối socket: " + args[0]);
            });

            mSocket.on(Socket.EVENT_DISCONNECT, args -> {
                Log.e("SocketManager", "Socket bị ngắt kết nối");
            });

            // Nhận danh sách thông báo
            mSocket.on("getAllNotifications", args -> {
                try {
                    if (args.length > 0 && args[0] instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) args[0];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String id = jsonObject.getString("_id");
                            String uId = jsonObject.getString("userId");
                            String title = jsonObject.getString("title");
                            String message = jsonObject.getString("message");
                            String type = jsonObject.getString("type");
                            String status = jsonObject.getString("status");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

                            // Chuyển đổi từ chuỗi JSON sang Date
                            Date createdDate = dateFormat.parse(jsonObject.getString("createdAt"));
                            Date updatedDate = dateFormat.parse(jsonObject.getString("updatedAt"));

                            // Chuyển Date thành Timestamp
                            Timestamp createdAt = new Timestamp(createdDate.getTime());
                            Timestamp updatedAt = new Timestamp(updatedDate.getTime());

                            // Tạo đối tượng Notification
                            Notification notification = new Notification(id, uId, title, message, type, status, createdAt, updatedAt);
                            notifications.add(notification);
                        }

                        Log.d("SocketManager", "Danh sách thông báo: " + notifications.toString());

                        // Cập nhật UI hoặc ViewModel nếu cần
                        notificationViewModel.updateNotifications(notifications);
                    } else {
                        Log.e("SocketManager", "Không có dữ liệu hợp lệ từ server!");
                    }
                } catch (Exception e) {
                    Log.e("SocketManager", "Lỗi khi xử lý thông báo: ", e);
                }
            });

            // 🟢 Lắng nghe thông báo mới từ server
            mSocket.on("newNotification", args -> {
                try {
                    if (args.length > 0 && args[0] instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) args[0];

                        String id = jsonObject.getString("_id");
                        String uId = jsonObject.getString("userId");
                        String title = jsonObject.getString("title");
                        String message = jsonObject.getString("message");
                        String type = jsonObject.getString("type");
                        String status = jsonObject.getString("status");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

                        // Chuyển đổi từ chuỗi JSON sang Date
                        Date createdDate = dateFormat.parse(jsonObject.getString("createdAt"));
                        Date updatedDate = dateFormat.parse(jsonObject.getString("updatedAt"));

                        // Chuyển Date thành Timestamp
                        Timestamp createdAt = new Timestamp(createdDate.getTime());
                        Timestamp updatedAt = new Timestamp(updatedDate.getTime());

                        // Tạo đối tượng Notification
                        Notification newNotification = new Notification(id, uId, title, message, type, status, createdAt, updatedAt);

                        Log.d("SocketManager", "Nhận thông báo mới: " + newNotification.toString());

                        notifications.add(newNotification);
                        notificationViewModel.updateNotifications(notifications);
                    }
                } catch (Exception e) {
                    Log.e("SocketManager", "Lỗi khi nhận thông báo mới: ", e);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🛑 Bỏ các `Emitter.Listener` thừa vì chúng không được gọi trong connectSocket()

    // Gửi thông báo
    public static void sendNotification(String userId, String title, String message, String type) {
        JSONObject notification = new JSONObject();
        try {
            notification.put("userId", userId);
            notification.put("title", title);
            notification.put("message", message);
            notification.put("type", type);

            mSocket.emit("sendNotification", notification);  // Gửi thông báo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Đóng kết nối
    public static void disconnectSocket() {
        if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
        }
    }
}
