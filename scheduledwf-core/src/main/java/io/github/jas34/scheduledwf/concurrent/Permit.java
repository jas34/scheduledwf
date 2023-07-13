package io.github.jas34.scheduledwf.concurrent;

import io.github.jas34.scheduledwf.utils.IDGenerator_;

/**
 * @author Jasbir Singh Vivian Zheng
 */
public class Permit {

    private String id;

    private String name;

    private long inUseUpto;

    private boolean used;

    public Permit() {
    }

    public Permit(String name) {
        this.name = name;
        this.id = IDGenerator_.generate();
        this.inUseUpto = System.currentTimeMillis() + 500;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public long getInUseUpto() {
        return inUseUpto;
    }

    public void setInUseUpto(long inUseUpto) {
        this.inUseUpto = inUseUpto;
    }

    @Override
    public String toString() {
        return "Permit{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", inUseUpto=" + inUseUpto
                + ", used=" + used + '}';
    }
}
