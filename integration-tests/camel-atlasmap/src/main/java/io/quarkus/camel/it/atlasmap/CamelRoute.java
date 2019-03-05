package io.quarkus.camel.it.atlasmap;

import org.apache.camel.builder.RouteBuilder;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class CamelRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:map")
                .to("atlas:{{mapping}}")
                .convertBodyTo(String.class)
                .log("${body}");
    }
}
