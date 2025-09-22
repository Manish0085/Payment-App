package com.payment.notification.service.impl;


import com.payment.notification.entity.Notification;
import com.payment.notification.repository.NotificationRepository;
import com.payment.notification.service.INotificationService;
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
