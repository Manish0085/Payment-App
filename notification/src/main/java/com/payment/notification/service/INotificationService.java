package com.payment.notification.service;


import com.payment.notification.entity.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

public interface INotificationService {

    Notification sendNotification(Notification notification);

    List<Notification> getNotification(Long userId);
}
