package fei.tcc.parentalcontrol.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by thiagoretondar on 20/11/16.
 */
public class ParentLoginIdResponseDto implements Serializable {

    private static final long serialVersionUID = 4818616539481755876L;

    @JsonProperty("logged")
    private boolean logged;

    @JsonProperty("parent_id")
    private Integer parentId;

    public boolean getLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
