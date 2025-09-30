package com.paypal.notification.controller;


import com.paypal.notification.entity.Notification;
import com.paypal.notification.service.INotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private INotificationService service;

    public NotificationController(INotificationService iNotificationService){
        this.service = iNotificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestBody Notification notification){
        return ResponseEntity.ok(service.sendNotification(notification));
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotificationByUser(@PathVariable Long userId){
        return ResponseEntity.ok(service.getNotification(userId));
    }
}
