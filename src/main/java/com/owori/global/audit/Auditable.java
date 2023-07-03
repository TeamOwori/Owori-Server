package com.owori.global.audit;

import java.time.LocalDateTime;

@SoftDelete
public interface Auditable {
    BaseTime getBaseTime();
    void setBaseTime(BaseTime baseTime);
    default void delete() {
        getBaseTime().setDeletedAt(LocalDateTime.now().plusHours(9L));
    }
}
