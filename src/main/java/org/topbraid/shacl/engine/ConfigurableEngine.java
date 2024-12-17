package org.topbraid.shacl.engine;

import org.topbraid.shacl.validation.ValidationEngineConfiguration;

public interface ConfigurableEngine {
    ValidationEngineConfiguration getConfiguration();

    void setConfiguration(ValidationEngineConfiguration configuration);
}
