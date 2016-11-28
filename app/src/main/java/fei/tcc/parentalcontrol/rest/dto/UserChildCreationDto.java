package fei.tcc.parentalcontrol.rest.dto;

import java.io.Serializable;

/**
 * Created by thiagoretondar on 20/11/16.
 */
public class UserChildCreationDto implements Serializable {

    private static final long serialVersionUID = -2114492168275181769L;

    private String name;

    private String birthdate;

    private String sex;

    private Integer parentId;

    private String deviceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
