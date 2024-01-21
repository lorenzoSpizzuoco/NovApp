package com.example.novapp2.repository.notification;

import com.example.novapp2.entity.Notification;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface INotificationRepository {

    Task<Void> insertNotification(Notification notification);

    Task<List<Notification>> getAllNotifications();

    Task<Notification> getNotificationById(String notificationId);

    Task<Void> updateNotificationById(String notificationId, Notification updatedNotification);


}
