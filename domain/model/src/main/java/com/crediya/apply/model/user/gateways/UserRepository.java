package com.crediya.apply.model.user.gateways;

import com.crediya.apply.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> getUserByEmail(String email);
}
