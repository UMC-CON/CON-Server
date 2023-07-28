package com.umc.cons.notification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umc.cons.notification.domain.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
