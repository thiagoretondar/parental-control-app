package fei.tcc.parentalcontrol.rest.dto;

import java.io.Serializable;

/**
 * Created by thiagoretondar on 30/10/16.
 */
public class MostUsedAppsDto implements Serializable {

    private static final long serialVersionUID = -1947047148576591456L;

    private String name;

    private Integer hours;

    private Integer minutes;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}
