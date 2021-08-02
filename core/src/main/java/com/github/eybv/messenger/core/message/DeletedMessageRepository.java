package com.github.eybv.messenger.core.message;

import java.util.Optional;

public interface DeletedMessageRepository {

    Optional<DeletedMessage> findById(long id);

    DeletedMessage save(DeletedMessage message);

    void delete(DeletedMessage message);

}
