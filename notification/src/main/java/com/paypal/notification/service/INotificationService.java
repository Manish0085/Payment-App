package com.paypal.notification.service;


import com.paypal.notification.entity.Notification;

import java.util.List;

public interface INotificationService {

    Notification sendNotification(Notification notification);

    List<Notification> getNotification(Long userId);
}
