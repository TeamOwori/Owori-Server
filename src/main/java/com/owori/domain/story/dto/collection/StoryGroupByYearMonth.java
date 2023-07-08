package com.owori.domain.story.dto.collection;

import com.owori.domain.story.entity.Story;

import java.util.*;

public class StoryGroupByYearMonth {
    private Map<String, List<Story>> storyByYearMonth;

    public StoryGroupByYearMonth() {
        this.storyByYearMonth = new TreeMap<>(Collections.reverseOrder());
    }

    public void addStories(Map<String, List<Story>> groupedStories) {
        storyByYearMonth.putAll(groupedStories);
    }

    public Map<String, List<Story>> getStoryByYearMonth() {
        return storyByYearMonth;
    }
}

