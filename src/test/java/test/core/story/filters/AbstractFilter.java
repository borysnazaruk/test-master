package test.core.story.filters;

import com.google.common.collect.ArrayListMultimap;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFilter.class.getSimpleName());
    private static final List<String> tagsNames = Arrays.asList("tags", "tag");

    protected static ArrayList<Scenario> getScenariosOfStory(final Story story) {
        try {
            final Field scenariosField = story.getClass().getDeclaredField("scenarios");
            scenariosField.setAccessible(true);
            return (ArrayList<Scenario>) scenariosField.get(story);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException("Some problems while getting a private field from object");
        }
    }

    protected static void collectTags(final ArrayListMultimap<String, String> tags, final String tagValue) {
        for (String tagStr : tagValue.split(",")) {
            final String[] tagArr = tagStr.split(":");
            try {
                tags.put(tagArr[0].trim(), tagArr[1].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                LOG.error("Tag has incorrect format: {}. Must be key:value", tagValue);
            }
        }
    }

    protected static ArrayListMultimap<String, String> getTagsFromScenario(final Scenario scenario) {
        ArrayListMultimap<String, String> tags = ArrayListMultimap.create();
        for (String tagName : tagsNames) {
            String tagValue = scenario.getMeta().getProperty(tagName);
            if (!tagValue.isEmpty()) {
                collectTags(tags, tagValue);
            }
        }
        return tags;
    }
}
