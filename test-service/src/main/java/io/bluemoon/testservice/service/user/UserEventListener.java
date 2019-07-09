package io.bluemoon.testservice.service.user;

import io.bluemoon.testservice.domain.user.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;

@Component
public class UserEventListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = UserServiceImpl.UserCreateEvent.class)
    public void createUserHandle(UserServiceImpl.UserCreateEvent event) throws IOException {
        User user = event.getUser();


        // oauth
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = UserServiceImpl.UserUpdateEvent.class)
    public void updateUserHandle(UserServiceImpl.UserUpdateEvent event) throws IOException {
        User user = event.getUser();

        // oauth
    }
}
