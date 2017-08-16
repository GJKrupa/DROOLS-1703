package uk.me.krupa.drools_1703;

import java.io.Serializable;

public class Thingy implements Serializable {

    private String name;
    private String version;
    private Object[] items;

    public Thingy(String name) {
        this.name = name;
        this.version = null;
        this.items = null;
    }

    public Thingy(String name, Object... items) {
        this.name = name;
        this.version = null;
        this.items = items;
    }

    public Thingy(String name, String version, Object... items) {
        this.name = name;
        this.version = version;
        this.items = items;
    }

    public void print() {
        System.out.println("Printing rule " + name);
    }

    public String getName() {
        return name;
    }
}
