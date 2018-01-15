package com.dingtai.jinriyongzhou.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class DianBoDetialListBean implements Serializable {

    private List<ChannleByProgramBean> ChannleByProgram;

    public List<ChannleByProgramBean> getChannleByProgram() {
        return ChannleByProgram;
    }

    public void setChannleByProgram(List<ChannleByProgramBean> ChannleByProgram) {
        this.ChannleByProgram = ChannleByProgram;
    }

    public static class ChannleByProgramBean implements Serializable {
        /**
         * ID : MTIx
         * CreateTime : MjAxOC8xLzEzIDE2OjE2OjE2
         * LiveChannelName : 5aSa5p2h57q/6Lev55u05pKt
         * LiveImageUrl : aHR0cDovL21haW4uaG4wNzQ2LmNvbS9VcGxvYWRzL0ltYWdlcy8yMDE3MTIyMzEwNDA0NTA5MTg1NzAwMC5qcGc=
         * ParentID : MA==
         * IsHide : RmFsc2U=
         * IsAD : RmFsc2U=
         * IsShowHome : RmFsc2U=
         * IsDel : RmFsc2U=
         * IsTopic : RmFsc2U=
         * StID : MA==
         * ShowOrder : MTIx
         * PicPath :
         * VideoUrl :
         * LiveRandomNum : MWViZGYxNDUtNGNhZi00MGZkLWE2NjItOTc0MDY2NDg4N2Vh
         * LiveNativeRandomNum : ZDNhNjQwMzAtMDhiOC00MzZiLWE2OGItY2NiYTI3NDMyODgy
         * LiveRTMPUrl : aHR0cDovLzEudm9kLmdkLmN6LmhuLmQ1bXQuY29tLmNuOjkwMDEvbGl2ZS9saXZlNy9zdHJlYW0ubTN1OA==
         * LiveType : MQ==
         * Status : MA==
         * ReMark :
         * VODChannelsProgram : [{"ID":"NTQ3","CreateTime":"MjAxOC8xLzEzIDE2OjE4OjA2","ProgramName":"5rWL6K+V5L+h5Y+3","ProgramLogo":"L1VwbG9hZHMvSW1hZ2VzL01pZFRodW1ibmFpbC8yMDE4MDExMzE2MTgwNTA1MzUwMTAwMC5qcGc=","IsRec":"RmFsc2U=","VODChID":"MTIy","VODType":"MQ==","Status":"MQ==","ReMark":""}]
         */

        private String ID;
        private String CreateTime;
        private String LiveChannelName;
        private String LiveImageUrl;
        private String ParentID;
        private String IsHide;
        private String IsAD;
        private String IsShowHome;
        private String IsDel;
        private String IsTopic;
        private String StID;
        private String ShowOrder;
        private String PicPath;
        private String VideoUrl;
        private String LiveRandomNum;
        private String LiveNativeRandomNum;
        private String LiveRTMPUrl;
        private String LiveType;
        private String Status;
        private String ReMark;
        private List<VODChannelsProgramBean> VODChannelsProgram;

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

        public String getLiveChannelName() {
            return LiveChannelName;
        }

        public void setLiveChannelName(String LiveChannelName) {
            this.LiveChannelName = LiveChannelName;
        }

        public String getLiveImageUrl() {
            return LiveImageUrl;
        }

        public void setLiveImageUrl(String LiveImageUrl) {
            this.LiveImageUrl = LiveImageUrl;
        }

        public String getParentID() {
            return ParentID;
        }

        public void setParentID(String ParentID) {
            this.ParentID = ParentID;
        }

        public String getIsHide() {
            return IsHide;
        }

        public void setIsHide(String IsHide) {
            this.IsHide = IsHide;
        }

        public String getIsAD() {
            return IsAD;
        }

        public void setIsAD(String IsAD) {
            this.IsAD = IsAD;
        }

        public String getIsShowHome() {
            return IsShowHome;
        }

        public void setIsShowHome(String IsShowHome) {
            this.IsShowHome = IsShowHome;
        }

        public String getIsDel() {
            return IsDel;
        }

        public void setIsDel(String IsDel) {
            this.IsDel = IsDel;
        }

        public String getIsTopic() {
            return IsTopic;
        }

        public void setIsTopic(String IsTopic) {
            this.IsTopic = IsTopic;
        }

        public String getStID() {
            return StID;
        }

        public void setStID(String StID) {
            this.StID = StID;
        }

        public String getShowOrder() {
            return ShowOrder;
        }

        public void setShowOrder(String ShowOrder) {
            this.ShowOrder = ShowOrder;
        }

        public String getPicPath() {
            return PicPath;
        }

        public void setPicPath(String PicPath) {
            this.PicPath = PicPath;
        }

        public String getVideoUrl() {
            return VideoUrl;
        }

        public void setVideoUrl(String VideoUrl) {
            this.VideoUrl = VideoUrl;
        }

        public String getLiveRandomNum() {
            return LiveRandomNum;
        }

        public void setLiveRandomNum(String LiveRandomNum) {
            this.LiveRandomNum = LiveRandomNum;
        }

        public String getLiveNativeRandomNum() {
            return LiveNativeRandomNum;
        }

        public void setLiveNativeRandomNum(String LiveNativeRandomNum) {
            this.LiveNativeRandomNum = LiveNativeRandomNum;
        }

        public String getLiveRTMPUrl() {
            return LiveRTMPUrl;
        }

        public void setLiveRTMPUrl(String LiveRTMPUrl) {
            this.LiveRTMPUrl = LiveRTMPUrl;
        }

        public String getLiveType() {
            return LiveType;
        }

        public void setLiveType(String LiveType) {
            this.LiveType = LiveType;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getReMark() {
            return ReMark;
        }

        public void setReMark(String ReMark) {
            this.ReMark = ReMark;
        }

        public List<VODChannelsProgramBean> getVODChannelsProgram() {
            return VODChannelsProgram;
        }

        public void setVODChannelsProgram(List<VODChannelsProgramBean> VODChannelsProgram) {
            this.VODChannelsProgram = VODChannelsProgram;
        }

        public static class VODChannelsProgramBean implements Serializable {
            /**
             * ID : NTQ3
             * CreateTime : MjAxOC8xLzEzIDE2OjE4OjA2
             * ProgramName : 5rWL6K+V5L+h5Y+3
             * ProgramLogo : L1VwbG9hZHMvSW1hZ2VzL01pZFRodW1ibmFpbC8yMDE4MDExMzE2MTgwNTA1MzUwMTAwMC5qcGc=
             * IsRec : RmFsc2U=
             * VODChID : MTIy
             * VODType : MQ==
             * Status : MQ==
             * ReMark :
             */

            private String ID;
            private String CreateTime;
            private String ProgramName;
            private String ProgramLogo;
            private String IsRec;
            private String VODChID;
            private String VODType;
            private String Status;
            private String ReMark;

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

            public String getProgramName() {
                return ProgramName;
            }

            public void setProgramName(String ProgramName) {
                this.ProgramName = ProgramName;
            }

            public String getProgramLogo() {
                return ProgramLogo;
            }

            public void setProgramLogo(String ProgramLogo) {
                this.ProgramLogo = ProgramLogo;
            }

            public String getIsRec() {
                return IsRec;
            }

            public void setIsRec(String IsRec) {
                this.IsRec = IsRec;
            }

            public String getVODChID() {
                return VODChID;
            }

            public void setVODChID(String VODChID) {
                this.VODChID = VODChID;
            }

            public String getVODType() {
                return VODType;
            }

            public void setVODType(String VODType) {
                this.VODType = VODType;
            }

            public String getStatus() {
                return Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }

            public String getReMark() {
                return ReMark;
            }

            public void setReMark(String ReMark) {
                this.ReMark = ReMark;
            }
        }
    }
}
