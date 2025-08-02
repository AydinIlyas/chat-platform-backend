package io.chatplatform.messaging.user.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_block",
        uniqueConstraints = @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"}))
public class UserBlock {

    @EmbeddedId
    private UserBlockId id;

    @MapsId("blockerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private User blocker;

    @MapsId("blockedId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id")
    private User blocked;
}

