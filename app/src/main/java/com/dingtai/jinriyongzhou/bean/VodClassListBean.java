package com.dingtai.jinriyongzhou.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class VodClassListBean implements Serializable {
    /**
     * ID : Mw==
     * CreateTime : MjAxOC8xLzEzIDE2OjAzOjMw
     * VodClassName : 5LiT6aKY5qCP55uu
     * VodClassLogo : L1VwbG9hZHMvSW1hZ2VzL01pZFRodW1ibmFpbC8yMDE4MDExMzE2MDMyNzk4OTE1MDAwMC5qcGc=
     * ParentID : MA==
     * Remark :
     */

    private String ID;
    private String CreateTime;
    private String VodClassName;
    private String VodClassLogo;
    private String ParentID;
    private String Remark;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getVodClassName() {
        return VodClassName;
    }

    public void setVodClassName(String VodClassName) {
        this.VodClassName = VodClassName;
    }

    public String getVodClassLogo() {
        return VodClassLogo;
    }

    public void setVodClassLogo(String VodClassLogo) {
        this.VodClassLogo = VodClassLogo;
    }

    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String ParentID) {
        this.ParentID = ParentID;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
}
