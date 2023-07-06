package com.owori.domain.image.entity;

import com.owori.domain.story.entity.Story;
import com.owori.global.audit.AuditListener;
import com.owori.global.audit.Auditable;
import com.owori.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image implements Auditable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;

    @Column
    private Long orderNum;

    @Column(nullable = false)
    private String url;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Image (String url, Long orderNum){
        this.url = url;
        this.orderNum = orderNum;
    }

    public void updateStory(Story story){
        this.story = story;
    }

}