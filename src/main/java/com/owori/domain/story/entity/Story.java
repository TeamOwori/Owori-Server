package com.owori.domain.story.entity;

import com.owori.domain.comment.entity.Comment;
import com.owori.domain.heart.entity.Heart;
import com.owori.domain.image.entity.Image;
import com.owori.domain.member.entity.Member;
import com.owori.global.audit.AuditListener;
import com.owori.global.audit.Auditable;
import com.owori.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private Set<Heart> hearts = new LinkedHashSet<>();

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Story(String title, String contents, LocalDate startDate, LocalDate endDate, Member member){
        this.title = title;
        this.contents = contents;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
    }

    public void updateImage(Image image){
        this.images.add(image);
    }

    public void addHeart(Heart heart){
       this.hearts.add(heart);
    }

    public void removeHeart(Heart heart){
        this.hearts.remove(heart);
    }

    public void addComment(Comment comment){ this.comments.add(comment); }

    public void removeComment(Comment comment){ this.comments.remove(comment); }

}
