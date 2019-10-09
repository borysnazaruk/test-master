package test.core;

import com.test.CtcProperties;
import com.test.engine.exception.IncompatibleDataException;
import com.test.utils.OsCheck;
import net.serenitybdd.jbehave.SerenityStories;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import org.jbehave.core.io.StoryFinder;
import ch.lambdaj.Lambda;
import org.jbehave.core.steps.ParameterConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import test.core.story.CustomStoryParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AcceptanceTestSuite extends SerenityStories {
    private static final Logger LOG = LoggerFactory.getLogger(AcceptanceTestSuite.class.getSimpleName());
    private static final String X64_ARCH = "amd64";
    private static EnvironmentVariables environmentVariables = Injectors.getInjector()
            .getProvider(EnvironmentVariables.class).get();
    protected Integer agentNumber;
    protected Integer agentTotal;
    private Map<String, List<String>> mapToRun;


    public AcceptanceTestSuite() {
        try {
            Class.forName("com.test.CtcProperties");
        } catch (ClassNotFoundException e) {
            LOG.error("Error instantiating com.test.CtcProperties", e);
        }
        mapToRun = Collections.synchronizedMap(getStoryScenariosMapToRun());
        assignBatches();
        setDriverAccordingToOS();
        configureJbehave();
        selectStoryFilesForRunningSuite();
    }

    private void configureJbehave() {
        configuration().useParameterConverters(new ParameterConverters().addConverters(customConverters()))
                .useStoryParser(new CustomStoryParser(mapToRun));
    }

    private void selectStoryFilesForRunningSuite() {
        if (mapToRun.isEmpty()) {
            LOG.info("No suite key or pattern was provided, trying to run all stories in parallel");
            parallelAcceptanceTestSuite(
                    storyPaths().stream().collect(Collectors.toMap(x -> x, x -> Collections.emptyList())));
        } else {
            LOG.info("Trying to run following stories: {}", mapToRun);
            parallelAcceptanceTestSuite(mapToRun);
        }
    }

    private void parallelAcceptanceTestSuite(final Map<String, List<String>> storyScenarioMap) {
        rearangeStoriesScenarioMapByRegex(storyScenarioMap);
        // To give matrix jobs an ability to use unique workspaces Batch names are named like on Jenkins:
        // JobName_BatchNumber.
        List<String> storiesForThisAgent;

        failIfAgentIsNotConfiguredCorrectly(agentNumber, agentTotal);
        storiesForThisAgent = StoryBatchSorter.getStoriesForCurrentAgent(mapToRun, agentTotal, agentNumber);

        outputWhichStoriesAreBeingRun(storiesForThisAgent);
        findStoriesCalled(Lambda.join(storiesForThisAgent, ";"));
    }

    private void outputWhichStoriesAreBeingRun(final List<String> stories) {
        LOG.info("Running " + stories.size() + " stories: ");
        for (String story : stories) {
            LOG.info(" - {}", story);
        }
    }

    private void failIfAgentIsNotConfiguredCorrectly(final Integer agentPosition, final Integer agentCount) {
        if (agentPosition == null) {
            throw new RuntimeException("The agent number needs to be specified");
        } else if (agentCount == null) {
            throw new RuntimeException("The agent total needs to be specified");
        } else if (agentPosition < 1) {
            throw new RuntimeException("The agent number is invalid");
        } else if (agentCount < 1) {
            throw new RuntimeException("The agent total is invalid");
        } else if (agentPosition > agentCount) {
            throw new RuntimeException(String.format(
                    "There were %d agents in total specified and this agent is outside that range (it is specified as %d)",
                    agentPosition, agentCount));
        }
    }

    protected void assignBatches() {
        final String[] agentNumberStrArr = environmentVariables
                .getProperty(CtcProperties.Key.PARALLEL_AGENT_NUMBER.toString(), "1").split("_");
        agentNumber = Integer.parseInt(agentNumberStrArr[agentNumberStrArr.length - 1]);
        agentTotal = environmentVariables.getPropertyAsInteger(CtcProperties.Key.PARALLEL_AGENT_TOTAL.toString(), 1);
    }

    public Map<String, List<String>> getStoryScenariosMapToRun() {
        String storiesPattern = environmentVariables.getProperty(CtcProperties.Key.CTC_STORIES.toString());
        if (null == storiesPattern) {
            return Collections.emptyMap();
        } else {
            Map<String, List<String>> suiteStoryPathsScenariosMap = getStoryPathsForSuite(storiesPattern);

            // CTC_STORIES contains stories
            if (suiteStoryPathsScenariosMap.isEmpty()) {
                LOG.info("No suite was found for the given {} key, trying to extract stories from the key",
                        storiesPattern);
                final Map<String, List<String>> storyScenariosMap = new HashMap<>();
                final Map<String, List<String>> storyPathsScenariosMap = getStoryPathScenarioMap(
                        Arrays.asList(storiesPattern.split(";|,")));
                for (Map.Entry<String, List<String>> entry : storyPathsScenariosMap.entrySet()) {
                    storyScenariosMap.put("**/" + entry.getKey(), entry.getValue());
                }
                return storyScenariosMap;
            } else {
                // CTC_STORIES contains suite
                return suiteStoryPathsScenariosMap;
            }
        }
    }

    private Map<String, List<String>> getStoryPathsForSuite(final String runningSuite) {
        File suiteOfStories = findFile(runningSuite, new File(System.getProperty("suites.path")));
        return collectStoryPathsFromSuiteFile(suiteOfStories);
    }

    private Map<String, List<String>> getStoryPathScenarioMap(final List<String> storiesList) {
        Map<String, List<String>> storyScenariosMap = new LinkedHashMap<>();
        storiesList.forEach(story -> {
            String[] storySc = story.split(":");
            new IncompatibleDataException(
                    "The 'ctc.stories' parameter accepts either 'storyName.story' or 'storyName.story:TCID-1#TCID-2' or even 'suiteName'")
                    .throwIf(storySc.length < 1 || storySc.length > 2);
            if (storySc.length == 1) {
                storyScenariosMap.put(storySc[0], Collections.emptyList());
            } else {
                storyScenariosMap.put(storySc[0], Arrays.asList(storySc[1].split("#")));
            }
        });
        return storyScenariosMap;
    }

    private File findFile(String searchedFile, File searchInDirectory) {
        File[] listOfAllFilesInDirectory = searchInDirectory.listFiles();
        File suiteOfStories;
        if (listOfAllFilesInDirectory != null) {
            for (File singleFileFromDirectory : listOfAllFilesInDirectory) {
                if (singleFileFromDirectory.isDirectory()) {
                    suiteOfStories = findFile(searchedFile, singleFileFromDirectory);
                    if (suiteOfStories != null) {
                        return suiteOfStories;
                    }
                } else if (searchedFile.equalsIgnoreCase(singleFileFromDirectory.getName().replaceAll("\\..+$", ""))) {
                    return singleFileFromDirectory;
                }
            }
        }
        LOG.info("There is no suite: {} in directory {}", searchedFile, searchInDirectory);
        return null;
    }

    private Map<String, List<String>> collectStoryPathsFromSuiteFile(final File suiteFile) {
        if (null == suiteFile) {
            return Collections.emptyMap();
        }
        List<String> storyList;
        try {
            storyList = Files.readAllLines(Paths.get(suiteFile.getPath()), Charset.defaultCharset());
        } catch (IOException e) {
            LOG.error("Failed to open suite file, exiting", e);
            throw new RuntimeException(e);
        }
        Map<String, List<String>> storyScenarioMap = getStoryPathScenarioMap(storyList);
        LOG.info("Got story paths {}", storyScenarioMap);
        return storyScenarioMap;
    }

    private void rearangeStoriesScenarioMapByRegex(final Map<String, List<String>> storyScenarioMap) {
        Map<String, List<String>> mapRearangedByRegexps = new HashMap<>();
        storyScenarioMap.entrySet().forEach(entry -> {
            new StoryFinder().findPaths(System.getProperty("stories.path"), entry.getKey(), null)
                    .forEach(story -> mapRearangedByRegexps.put(story, entry.getValue()));

        });
        mapToRun.clear();
        mapToRun.putAll(mapRearangedByRegexps);
    }

    private void setDriverAccordingToOS() {
        OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
        switch (ostype) {
            case Windows:
                setChromeDriverWindows();
                setPhantomJSDriverWindows();
                if (X64_ARCH.equals(System.getProperty("os.arch"))) {
                    setIeDriverWindows64();
                } else {
                    setIeDriverWindows32();
                }
                break;
            case MacOS:
                setChromeDriverOsx();
                setPhantomJSDriverOsx();
                break;
            case Linux:
                if (X64_ARCH.equals(System.getProperty("os.arch"))) {
                    setChromeDriverLinux64();
                    setPhantomJSDriverLinux64();
                } else {
                    setChromeDriverLinux32();
                    setPhantomJSDriverLinux32();
                }
                break;
            case Other:
                LOG.error("Can't define OS");
                break;
        }
    }

    private ParameterConverter[] customConverters() {
        List<ParameterConverter> converters = new ArrayList<ParameterConverter>();
        //converters.add(new ParameterConverters.ExamplesTableConverter(new ExtendedExamplesTableFactory()));

        return converters.toArray(new ParameterConverter[converters.size()]);
    }

    private void setChromeDriverLinux32() {
        System.setProperty("webdriver.chrome.driver", "drivers/linux/32bit/chromedriver");
    }

    private void setChromeDriverLinux64() {
        System.setProperty("webdriver.chrome.driver", "drivers/linux/64bit/chromedriver");
    }

    private void setChromeDriverWindows() {
        System.setProperty("webdriver.chrome.driver", "drivers/windows/chromedriver.exe");
    }

    private void setChromeDriverOsx() {
        System.setProperty("webdriver.chrome.driver", "drivers/osx/chromedriver");
    }

    private void setPhantomJSDriverLinux32() {
        System.setProperty("phantomjs.binary.path", "drivers/linux/32bit/phantomjs");
    }

    private void setPhantomJSDriverLinux64() {
        System.setProperty("phantomjs.binary.path", "drivers/linux/64bit/phantomjs");
    }

    private void setPhantomJSDriverWindows() {
        System.setProperty("phantomjs.binary.path", "drivers/windows/phantomjs.exe");
    }

    private void setPhantomJSDriverOsx() {
        System.setProperty("webdriver.phantomjs.driver", "drivers/osx/phantomjs");
    }

    private void setIeDriverWindows32() {
        System.setProperty("webdriver.ie.driver", "drivers/windows/32bit/iedriver.exe");
    }

    private void setIeDriverWindows64() {
        System.setProperty("webdriver.ie.driver", "drivers/windows/64bit/iedriver.exe");
    }
}
