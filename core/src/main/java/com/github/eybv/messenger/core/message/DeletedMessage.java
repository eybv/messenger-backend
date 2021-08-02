package com.github.eybv.messenger.core.message;

import com.github.eybv.messenger.core.user.User;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "deleted_messages" , uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "message_id"})
})
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class DeletedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Message message;

}
