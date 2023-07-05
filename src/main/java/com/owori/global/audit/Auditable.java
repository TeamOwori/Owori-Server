package com.owori.global.audit;

import java.time.LocalDateTime;

public interface Auditable {
    BaseTime getBaseTime();
    void setBaseTime(BaseTime baseTime);
    default void delete() {
        getBaseTime().setDeletedAt(LocalDateTime.now());
    }
}
