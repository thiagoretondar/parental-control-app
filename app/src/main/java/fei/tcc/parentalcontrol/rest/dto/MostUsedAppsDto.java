package fei.tcc.parentalcontrol.rest.dto;

import java.io.Serializable;

/**
 * Created by thiagoretondar on 30/10/16.
 */
public class MostUsedAppsDto implements Serializable {

    private static final long serialVersionUID = -1947047148576591456L;

    private String name;

    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
