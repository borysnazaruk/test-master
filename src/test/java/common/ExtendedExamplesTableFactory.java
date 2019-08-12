package common;

import com.test.engine.dictionary.TField;
import org.jbehave.core.io.ResourceLoader;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.model.TableTransformers;

public class ExtendedExamplesTableFactory extends ExamplesTableFactory {

    public ExtendedExamplesTableFactory(ResourceLoader resourceLoader, TableTransformers tableTransformers) {
        super(resourceLoader, tableTransformers);
    }

    @Override
    public ExamplesTable createExamplesTable(String input) {
        ExamplesTable examplesTable = super.createExamplesTable(input);
        examplesTable.getProperties().put(TField.TABLE_PATH_NAME.toString(), input);
        return examplesTable;
    }
}