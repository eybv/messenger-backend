package com.github.eybv.messenger.persistence;

import com.github.eybv.messenger.core.message.DeletedMessage;
import com.github.eybv.messenger.core.message.DeletedMessageRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDeletedMessageRepository extends DeletedMessageRepository, JpaRepository<DeletedMessage, Long> {

}
