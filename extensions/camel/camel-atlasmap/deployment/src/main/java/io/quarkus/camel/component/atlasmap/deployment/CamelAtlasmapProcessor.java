package io.quarkus.camel.component.atlasmap.deployment;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlType;

import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;

import io.atlasmap.api.AtlasConverter;
import io.atlasmap.api.AtlasFieldAction;
import io.atlasmap.java.module.JavaModule;
import io.atlasmap.java.v2.JavaField;
import io.atlasmap.json.module.JsonModule;
import io.atlasmap.json.v2.JsonComplexType;
import io.atlasmap.json.v2.JsonDataSource;
import io.atlasmap.v2.ActionsJsonDeserializer;
import io.atlasmap.v2.ActionsJsonSerializer;
import io.atlasmap.v2.AtlasMapping;
import io.atlasmap.v2.DataSource;
import io.atlasmap.v2.Mapping;
import io.atlasmap.xml.module.XmlModule;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.substrate.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.substrate.ReflectiveMethodBuildItem;
import io.quarkus.deployment.builditem.substrate.ServiceProviderBuildItem;
import io.quarkus.deployment.builditem.substrate.SubstrateResourceBuildItem;
import io.quarkus.deployment.builditem.substrate.SubstrateResourceBundleBuildItem;

class CamelAtlasmapProcessor {

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FeatureBuildItem.CAMEL_ATLASMAP);
    }

    @BuildStep(applicationArchiveMarkers = { "io/atlasmap", "io/atlasmap/v2" })
    void preocess(CombinedIndexBuildItem combinedIndexBuildItem,
            BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
            BuildProducer<ReflectiveMethodBuildItem> reflectiveMethod,
            BuildProducer<SubstrateResourceBuildItem> resource,
            BuildProducer<SubstrateResourceBundleBuildItem> resourceBundle,
            BuildProducer<ServiceProviderBuildItem> serviceProvider,
            ApplicationArchivesBuildItem applicationArchivesBuildItem) {

        IndexView view = combinedIndexBuildItem.getIndex();

        serviceProvider.produce(new ServiceProviderBuildItem(
                AtlasConverter.class.getName(),
                getImplementations(view, AtlasConverter.class).toArray(new String[0])));

        serviceProvider.produce(new ServiceProviderBuildItem(
                AtlasFieldAction.class.getName(),
                getImplementations(view, AtlasFieldAction.class).toArray(new String[0])));

        resource.produce(new SubstrateResourceBuildItem("atlas-actions-v2.xsd"));
        resource.produce(new SubstrateResourceBuildItem("atlas-model-v2.xjb"));
        resource.produce(new SubstrateResourceBuildItem("atlas-model-v2.xsd"));
        resource.produce(new SubstrateResourceBuildItem("atlas-xml-schemaset-model-v2.xsd"));
        resource.produce(new SubstrateResourceBuildItem("atlas-xml-model-v2.xjb"));
        resource.produce(new SubstrateResourceBuildItem("atlas-xml-model-v2.xsd"));
        resource.produce(new SubstrateResourceBuildItem("catalog.cat"));
        resource.produce(new SubstrateResourceBuildItem("META-INF/sun-jaxb.episode"));

        for (String s : getImplementations(view, JsonDeserializer.class)) {
            reflectiveClass.produce(new ReflectiveClassBuildItem(true, false, s));
        }
        for (String s : getImplementations(view, JsonSerializer.class)) {
            reflectiveClass.produce(new ReflectiveClassBuildItem(true, false, s));
        }

        Stream.of(
                "java.lang.Boolean",
                "java.lang.Byte",
                "java.lang.Character",
                "java.lang.Double",
                "java.lang.Float",
                "java.lang.Integer",
                "java.lang.Long",
                "java.lang.Short",
                "java.lang.String",
                "java.util.ArrayList")
                .forEach(t -> reflectiveClass.produce(new ReflectiveClassBuildItem(true, false, t)));

        Stream.of(
                JavaModule.class,
                JsonModule.class,
                XmlModule.class,
                AtlasMapping.class,
                DataSource.class,
                Mapping.class,
                JsonDataSource.class,
                JsonComplexType.class,
                JavaField.class,
                ActionsJsonDeserializer.class,
                ActionsJsonSerializer.class)
                .forEach(t -> reflectiveClass.produce(new ReflectiveClassBuildItem(true, false, t)));

        Stream.of(
                XmlRegistry.class,
                XmlType.class)
                .map(Class::getName)
                .map(DotName::createSimple)
                .map(view::getAnnotations)
                .flatMap(Collection::stream)
                .forEach(v -> {
                    if (v.target().kind() == AnnotationTarget.Kind.CLASS) {
                        reflectiveClass
                                .produce(new ReflectiveClassBuildItem(true, false, v.target().asClass().name().toString()));
                    }
                    if (v.target().kind() == AnnotationTarget.Kind.METHOD) {
                        reflectiveMethod.produce(new ReflectiveMethodBuildItem(v.target().asMethod()));
                    }
                });
    }

    protected Collection<String> getImplementations(IndexView view, Class<?> type) {
        return view.getAllKnownImplementors(DotName.createSimple(type.getName())).stream()
                .map(ClassInfo::toString)
                .collect(Collectors.toList());
    }
}
