package fei.tcc.parentalcontrol.rest.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by thiagoretondar on 30/10/16.
 */
public class AppUsageInfoDto implements Serializable {

    private static final long serialVersionUID = 166393999337710558L;

    private String appName;

    private List<Long> dateTimes;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<Long> getDateTimes() {
        return dateTimes;
    }

    public void setDateTimes(List<Long> dateTimes) {
        this.dateTimes = dateTimes;
    }

}
