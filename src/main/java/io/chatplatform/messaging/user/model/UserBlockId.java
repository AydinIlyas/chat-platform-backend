package io.chatplatform.messaging.user.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserBlockId implements Serializable {
    private Long blockerId;
    private Long blockedId;
}
