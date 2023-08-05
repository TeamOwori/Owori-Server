package com.owori.domain.test;

import com.owori.domain.comment.entity.Comment;
import com.owori.domain.comment.repository.CommentRepository;
import com.owori.domain.family.dto.request.FamilyRequest;
import com.owori.domain.family.dto.response.InviteCodeResponse;
import com.owori.domain.family.entity.Family;
import com.owori.domain.family.repository.FamilyRepository;
import com.owori.domain.family.service.FamilyService;
import com.owori.domain.image.entity.Image;
import com.owori.domain.image.repository.ImageRepository;
import com.owori.domain.member.entity.*;
import com.owori.domain.member.repository.MemberRepository;
import com.owori.domain.saying.entity.Saying;
import com.owori.domain.saying.repository.SayingRepository;
import com.owori.domain.schedule.entity.Schedule;
import com.owori.domain.schedule.entity.ScheduleType;
import com.owori.domain.schedule.repository.ScheduleRepository;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {
    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;
    private final ScheduleRepository scheduleRepository;
    private final StoryRepository storyRepository;
    private final SayingRepository sayingRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final FamilyService familyService;

    @Transactional
    public String addTestData() {
        // member
        Member member1 = new Member(new OAuth2Info("13478919138471", AuthProvider.KAKAO));
        Member member2 = new Member(new OAuth2Info("13478919138472", AuthProvider.APPLE));
        Member member3 = new Member(new OAuth2Info("13478919138473", AuthProvider.KAKAO));
        Member member4 = new Member(new OAuth2Info("13478919138474", AuthProvider.APPLE));

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // family
        InviteCodeResponse inviteCode = familyService.saveFamily(new FamilyRequest("금쪽이 네명"));
        Family family = familyRepository.findByInviteCode(inviteCode.getInviteCode()).orElseThrow();
        family.addMember(member1);
        family.addMember(member2);
        family.addMember(member3);
        family.addMember(member4);

        member1.updateProfile("고마워감자탕탕", LocalDate.of(2000, 4, 22), Color.PINK);
        member1.updateProfileImage("https://owori.s3.ap-northeast-2.amazonaws.com/profile-image/32lkrj.png");
        member1.updateEmotionalBadge(EmotionalBadge.JOY);

        member2.updateProfile("고삼이", LocalDate.of(1962, 11, 30), Color.GREEN);
        member2.updateProfileImage("https://owori.s3.ap-northeast-2.amazonaws.com/profile-image/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2023-07-22+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.04.20.png");

        member3.updateProfile("지롱", LocalDate.of(2012, 5, 10), Color.PURPLE);
        member3.updateProfileImage("https://owori.s3.ap-northeast-2.amazonaws.com/profile-image/lal.png");
        member3.updateEmotionalBadge(EmotionalBadge.ANGRY);

        member4.updateProfile("푸학학학", LocalDate.of(1975, 12, 8), Color.YELLOW);
        member4.updateProfileImage("https://owori.s3.ap-northeast-2.amazonaws.com/profile-image/ghQkd23.png");
        member4.updateEmotionalBadge(EmotionalBadge.CRY);

        // story
        Story member1Story = new Story("여기에만 사진과 댓글이 있음", "오늘은 데모데이 대상을 탔다. 즐거웠다. 즐거웠다. 즐거웠다. 재미났다. 뿌듯하다 야호 야호 야호 야호 !!!!", LocalDate.of(2023, 3, 12), LocalDate.of(2023, 3, 12), member1);
        Story member1Story2 = new Story("경주로 떠난 가족 여행!", "오늘은 데모데이 대상을 탔다. 즐거웠다. 즐거웠다. 즐거웠다. 재미났다. 뿌듯하다 야호 야호 야호 야호 !!!! 오늘은 데모데이 대상을 탔다. 즐거웠다. 즐거웠다. 즐거웠다. 재미났다. 뿌듯하다 야호 야호 야호 야호 !!!!", LocalDate.of(2022, 1, 2), LocalDate.of(2022, 1, 5), member1);
        Story member1Story3 = new Story("생일은 즐거워", "푸학", LocalDate.of(2013, 3, 12), LocalDate.of(2013, 3, 12), member1);
        Story member1Story4 = new Story("야호", "야야야호 호 호 호 호 호 ", LocalDate.of(2009, 5, 2), LocalDate.of(2009, 5, 5), member1);

        Story member2Story = new Story("배고프다", "오늘은 데모데이 대상을 탔다. 즐거웠다. 즐거웠다. 즐거웠다. 재미났다. 뿌듯하다 야호 야호 야호 야호 !!!!", LocalDate.of(2022, 3, 12), LocalDate.of(2022, 3, 19), member2);
        Story member2Story2 = new Story("애국가 부르기", "동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세 남산 위에 저 소나무 철갑을 두른듯 바람서리 불변함은 우리 기상일세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세 가을 하늘 공활한데 높고 구름 없이 밝은 달은 우리 가슴 일편단심일세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세 이 기상과 이 마음으로 충성을 다하여 괴로우나 즐거우나 나라사랑하세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세", LocalDate.of(2010, 11, 12), LocalDate.of(2010, 11, 15), member2);

        Story member3Story = new Story("도로로로", "얍 ", LocalDate.of(2023, 5, 18), LocalDate.of(2023, 5, 18), member3);
        Story member3Story2 = new Story("푸하하하하학", "야야호", LocalDate.of(2009, 5, 2), LocalDate.of(2009, 5, 5), member3);

        Story member4Story = new Story("야", "동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세 남산 위에 저 소나무 철갑을 두른듯 바람서리 불변함은 우리 기상일세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세 가을 하늘 공활한데 높고 구름 없이 밝은 달은 우리 가슴 일편단심일세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세 이 기상과 이 마음으로 충성을 다하여 괴로우나 즐거우나 나라사랑하세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세 동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세 남산 위에 저 소나무 철갑을 두른듯 바람서리 불변함은 우리 기상일세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세 가을 하늘 공활한데 높고 구름 없이 밝은 달은 우리 가슴 일편단심일세 무궁화 삼천리 화려 강산 대한사람 대한으로 길이 보전하세", LocalDate.of(2019, 9, 02), LocalDate.of(2019, 9, 5), member4);

        storyRepository.save(member1Story);
        storyRepository.save(member1Story2);
        storyRepository.save(member1Story3);
        storyRepository.save(member1Story4);
        storyRepository.save(member2Story);
        storyRepository.save(member2Story2);
        storyRepository.save(member3Story);
        storyRepository.save(member3Story2);
        storyRepository.save(member4Story);

        //saying
        sayingRepository.save(Saying.builder().content("hello1").member(member1).tagMembers(List.of(member2, member3)).build());
        sayingRepository.save(Saying.builder().content("hello2").member(member2).tagMembers(List.of(member3)).build());
        sayingRepository.save(Saying.builder().content("hello3").member(member3).tagMembers(List.of(member1)).build());

        //family image
        family.addImage("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3.jpeg");

        // image
        Image image = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3+(1).jpeg", 1L);
        Image image1 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3.jpeg", 2L);
        Image image2 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2023-07-22+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.35.04.png", 3L);
        Image image3 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2023-07-22+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.35.17.png", 4L);
        Image image4 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2023-07-22+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.35.28.png", 5L);
        Image image5 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2023-07-22+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.35.50.png", 6L);
        Image image6 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2023-07-22+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.35.57.png", 7L);
        Image image7 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2023-07-22+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.36.22.png", 8L);
        Image image8 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2023-07-22+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.36.41.png", 9L);
        Image image9 = new Image("https://owori.s3.ap-northeast-2.amazonaws.com/story/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2023-07-22+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.36.48.png", 10L);

        imageRepository.save(image);
        imageRepository.save(image1);
        imageRepository.save(image2);
        imageRepository.save(image3);
        imageRepository.save(image4);
        imageRepository.save(image5);
        imageRepository.save(image6);
        imageRepository.save(image7);
        imageRepository.save(image8);
        imageRepository.save(image9);

        image.updateStory(member1Story);
        image1.updateStory(member1Story);
        image2.updateStory(member1Story);
        image3.updateStory(member1Story);
        image4.updateStory(member1Story);
        image5.updateStory(member1Story);
        image6.updateStory(member1Story);
        image7.updateStory(member1Story);
        image8.updateStory(member1Story);
        image9.updateStory(member1Story);


        // comment
        Comment story1comment = new Comment(member1, member1Story, null, "참 재밌었지");
        Comment story1comment2 = new Comment(member1, member1Story, story1comment, "맞아 완전 재밌었음 !");
        Comment story1comment3 = new Comment(member1, member1Story, null, "댓글 테스트 중 중");
        Comment story1comment4 = new Comment(member1, member1Story, null, "재밌도");
        Comment story1comment5 = new Comment(member1, member1Story, story1comment4, "야 호 야 호 야야호 ~");

        commentRepository.save(story1comment);
        commentRepository.save(story1comment2);
        commentRepository.save(story1comment3);
        commentRepository.save(story1comment4);
        commentRepository.save(story1comment5);

        // schedule
        scheduleRepository.save(new Schedule("가족 사진 찍는 날", LocalDate.of(2022, 01, 18), LocalDate.of(2022, 1, 19), ScheduleType.FAMILY, true, null, member1));
        scheduleRepository.save(new Schedule("외식 day", LocalDate.of(2022, 02, 18), LocalDate.of(2022, 3, 18), ScheduleType.FAMILY, true, null, member2));
        scheduleRepository.save(new Schedule("지렁이 졸업식", LocalDate.of(2022, 03, 18), LocalDate.of(2022, 3, 19), ScheduleType.FAMILY, true, null, member3));

        return inviteCode.getInviteCode();
    }
}
