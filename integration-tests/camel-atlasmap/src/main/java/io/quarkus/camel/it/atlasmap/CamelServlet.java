package io.quarkus.camel.it.atlasmap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

import io.quarkus.camel.runtime.CamelRuntime;

@Path("/")
@ApplicationScoped
public class CamelServlet {
    @Inject
    CamelRuntime runtime;

    @Path("/map")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Object map(String data) {
        CamelContext context = runtime.getContext();
        ProducerTemplate template = context.createProducerTemplate();

        return template.requestBody("direct:map", data);
    }
}
