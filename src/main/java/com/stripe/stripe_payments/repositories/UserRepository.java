package com.stripe.stripe_payments.repositories;

import com.stripe.stripe_payments.commons.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
}
