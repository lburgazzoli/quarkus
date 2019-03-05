package io.quarkus.camel.it.atlasmap;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Pojo {
    private String field1;

    public String getField1() {
        return field1;
    }

    public void setField1(final String field1) {
        this.field1 = field1;
    }

    @Override
    public String toString() {
        return "Pojo{" +
                "field1='" + field1 + '\'' +
                '}';
    }
}
