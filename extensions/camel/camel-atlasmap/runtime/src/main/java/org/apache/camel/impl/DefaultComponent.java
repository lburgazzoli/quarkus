package org.apache.camel.impl;

import org.apache.camel.CamelContext;

public abstract class DefaultComponent extends org.apache.camel.support.DefaultComponent {
    public DefaultComponent() {
    }

    public DefaultComponent(CamelContext context) {
        super(context);
    }
}
