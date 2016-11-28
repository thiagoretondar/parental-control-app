package fei.tcc.parentalcontrol.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by thiagoretondar on 19/11/16.
 */
public class UserLoginIdResponseDto implements Serializable {

    private static final long serialVersionUID = 7793589798623509974L;

    @JsonProperty("registered")
    private boolean registered;

    @JsonProperty("user_id")
    private Integer userId;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
