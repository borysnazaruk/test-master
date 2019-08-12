package test.core.story.filters;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Ordering;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PriorityFilter extends AbstractFilter {
    private static final String requiredPriority = System.getProperty("min.priority");
    private static final List<String> priorities = Arrays.asList("high", "medium", "low");
    private static final Ordering<String> pOrdering = Ordering.explicit("high", "medium", "low");

    public static void filterScenariosByPriority(final Story story) {
        if (isPriority(requiredPriority)) {
            ArrayList<Scenario> scenarioList = getScenariosOfStory(story);
            scenarioList.removeIf(sc -> hasLowerPriorityThanRequired(sc, requiredPriority));
        }
    }

    private static boolean hasLowerPriorityThanRequired(final Scenario scenario, final String priority) {
        ArrayListMultimap<String, String> tags = getTagsFromScenario(scenario);
        List<String> scenarioPs = tags.get("p");
        scenarioPs.removeIf(p -> !priorities.contains(p));
        Collections.sort(scenarioPs, pOrdering);
        if (scenarioPs.isEmpty()) {
            return false; // keep scenarios without priorities
        }
        return pOrdering.compare(priority, scenarioPs.get(0)) < 0;
    }

    private static boolean isPriority(final String pCandidate) {
        return priorities.contains(pCandidate);
    }
}
