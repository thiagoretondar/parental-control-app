package fei.tcc.parentalcontrol.rest.dto;

import java.io.Serializable;

/**
 * Created by thiagoretondar on 13/11/16.
 */
public class LastDatetimeUsedDto implements Serializable {

    private static final long serialVersionUID = -1739694796998667352L;

    private Long userId;

    private Long lastAppUsageDatetime;

    private Long lastLocationUsageDatetime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLastLocationUsageDatetime() {
        return lastLocationUsageDatetime;
    }

    public void setLastLocationUsageDatetime(Long lastLocationUsageDatetime) {
        this.lastLocationUsageDatetime = lastLocationUsageDatetime;
    }

    public Long getLastAppUsageDatetime() {
        return lastAppUsageDatetime;
    }

    public void setLastAppUsageDatetime(Long lastAppUsageDatetime) {
        this.lastAppUsageDatetime = lastAppUsageDatetime;
    }

}
