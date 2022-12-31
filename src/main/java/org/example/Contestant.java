package org.example;

public class Contestant {
    private final String name;
    public Contestant(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Contestant))
            return false;
        Contestant p = (Contestant) obj;
        return this.name.equals(p.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}