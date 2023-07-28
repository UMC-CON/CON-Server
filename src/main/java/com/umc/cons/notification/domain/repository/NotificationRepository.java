package com.umc.cons.notification.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.notification.domain.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("SELECT n FROM  Notification  n WHERE n.isDeleted = false AND n.member = :member")
	public List<Notification> findAllByMember(Member member);

	Optional<Notification> findByUuid(String uuid);
}
