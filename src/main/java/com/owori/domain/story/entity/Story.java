package com.owori.domain.story.entity;

import com.owori.domain.image.entity.Image;
import com.owori.global.audit.AuditListener;
import com.owori.global.audit.Auditable;
import com.owori.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story implements Auditable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder(access = AccessLevel.PRIVATE)
    private Story(String title, String contents, List<Image> images, LocalDate startDate, LocalDate endDate){
        this.title = title;
        this.contents = contents;
        this.images = images;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Story newStory(String title, String contents, List<Image> images, LocalDate startDate, LocalDate endDate){
        return Story.builder()
                .title(title)
                .contents(contents)
                .images(images)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
