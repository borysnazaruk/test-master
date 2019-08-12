package test.core.story.filters;

import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;

import java.util.List;

public class ScenarioIdFilter extends AbstractFilter {

    public static void filterScenariosByIds(final Story story, final List<String> targetIds) {
        if (!targetIds.isEmpty()) {
            List<Scenario> scenarios = getScenariosOfStory(story);
            scenarios.removeIf(scenario -> !((scenario.getMeta().hasProperty("preconditions")
                    || scenario.getMeta().hasProperty("precondition"))
                    || (scenario.getMeta().hasProperty("postconditions")
                            || scenario.getMeta().hasProperty("postcondition")))
                    && !targetIds.stream().anyMatch(id -> scenario.getTitle().contains(id)));
        }
    }
}
