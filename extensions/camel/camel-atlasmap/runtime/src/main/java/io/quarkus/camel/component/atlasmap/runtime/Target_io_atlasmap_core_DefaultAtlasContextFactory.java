package io.quarkus.camel.component.atlasmap.runtime;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

import io.atlasmap.core.DefaultAtlasContextFactory;

@TargetClass(className = "io.atlasmap.core.DefaultAtlasContextFactory")
public final class Target_io_atlasmap_core_DefaultAtlasContextFactory {
    @Substitute
    protected void registerFactoryJmx(DefaultAtlasContextFactory factory) {
        // no-op
    }
}
