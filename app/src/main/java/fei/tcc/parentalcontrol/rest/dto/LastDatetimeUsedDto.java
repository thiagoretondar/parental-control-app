package fei.tcc.parentalcontrol.rest.dto;

import java.io.Serializable;

/**
 * Created by thiagoretondar on 13/11/16.
 */
public class LastDatetimeUsedDto implements Serializable {

    private static final long serialVersionUID = -1739694796998667352L;

    private Long userId;

    private String lastAppUsageDatetime;

    private String lastLocationUsageDatetime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLastAppUsageDatetime() {
        return lastAppUsageDatetime;
    }

    public void setLastAppUsageDatetime(String lastAppUsageDatetime) {
        this.lastAppUsageDatetime = lastAppUsageDatetime;
    }

    public String getLastLocationUsageDatetime() {
        return lastLocationUsageDatetime;
    }

    public void setLastLocationUsageDatetime(String lastLocationUsageDatetime) {
        this.lastLocationUsageDatetime = lastLocationUsageDatetime;
    }

}
