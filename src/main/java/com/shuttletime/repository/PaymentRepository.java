package com.shuttletime.repository;

// PaymentRepository.java
import com.shuttletime.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
