package fei.tcc.parentalcontrol.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by thiagoretondar on 19/11/16.
 */
public class UserLoginIdResponseDto implements Serializable {

    @JsonProperty("logged")
    private boolean logged;

    @JsonProperty("user_id")
    private Integer userId;

    public boolean getLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
