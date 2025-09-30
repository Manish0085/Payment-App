package com.paypal.notification.service.impl;


import com.paypal.notification.entity.Notification;
import com.paypal.notification.repository.NotificationRepository;
import com.paypal.notification.service.INotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    private NotificationRepository repository;


    public NotificationServiceImpl(NotificationRepository repository){
        this.repository = repository;
    }

    @Override
    public Notification sendNotification(Notification notification) {
        notification.setSentAt(LocalDateTime.now());

        return repository.save(notification);
    }

    @Override
    public List<Notification> getNotification(Long userId) {
        return repository.findByUserId(userId);
    }
}
