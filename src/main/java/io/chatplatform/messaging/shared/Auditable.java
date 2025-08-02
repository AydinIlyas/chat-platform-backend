package io.chatplatform.messaging.shared;


import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public class Auditable extends BaseEntity {
    private Instant createdAt;
    private Instant updatedAt;
}
