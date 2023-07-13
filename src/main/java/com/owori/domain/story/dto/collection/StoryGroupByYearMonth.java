package com.owori.domain.story.dto.collection;

import com.owori.domain.story.entity.Story;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

