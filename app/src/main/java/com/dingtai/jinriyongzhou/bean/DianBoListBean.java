package com.dingtai.jinriyongzhou.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class DianBoListBean implements Serializable {

    private List<VodClassListBean> VodClassList;

    public List<VodClassListBean> getVodClassList() {
        return VodClassList;
    }

    public void setVodClassList(List<VodClassListBean> VodClassList) {
        this.VodClassList = VodClassList;
    }


}
