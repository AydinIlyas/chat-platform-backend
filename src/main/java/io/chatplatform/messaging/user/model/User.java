package io.chatplatform.messaging.user.model;

import io.chatplatform.messaging.shared.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Auditable {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private Boolean enabled;
}
