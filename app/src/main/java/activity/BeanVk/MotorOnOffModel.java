package activity.BeanVk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MotorOnOffModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Show")
    @Expose
    private Boolean show;
    @SerializedName("response")
    @Expose
    private List<Response> response;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public class Response {

        @SerializedName("MId")
        @Expose
        private Integer mId;
        @SerializedName("Latitude")
        @Expose
        private String latitude;
        @SerializedName("Longitude")
        @Expose
        private String longitude;
        @SerializedName("Result")
        @Expose
        private String result;
        @SerializedName("CEnergyF")
        @Expose
        private Double cEnergyF;
        @SerializedName("CFlowF")
        @Expose
        private Double cFlowF;
        @SerializedName("CTimeF")
        @Expose
        private Double cTimeF;
        @SerializedName("onOFF")
        @Expose
        private Boolean onOFF;
        @SerializedName("energy")
        @Expose
        private Double energy;
        @SerializedName("flow")
        @Expose
        private Double flow;
        @SerializedName("time")
        @Expose
        private Double time;
        @SerializedName("valid")
        @Expose
        private Boolean valid;

        public Integer getMId() {
            return mId;
        }

        public void setMId(Integer mId) {
            this.mId = mId;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Double getCEnergyF() {
            return cEnergyF;
        }

        public void setCEnergyF(Double cEnergyF) {
            this.cEnergyF = cEnergyF;
        }

        public Double getCFlowF() {
            return cFlowF;
        }

        public void setCFlowF(Double cFlowF) {
            this.cFlowF = cFlowF;
        }

        public Double getCTimeF() {
            return cTimeF;
        }

        public void setCTimeF(Double cTimeF) {
            this.cTimeF = cTimeF;
        }

        public Boolean getOnOFF() {
            return onOFF;
        }

        public void setOnOFF(Boolean onOFF) {
            this.onOFF = onOFF;
        }

        public Double getEnergy() {
            return energy;
        }

        public void setEnergy(Double energy) {
            this.energy = energy;
        }

        public Double getFlow() {
            return flow;
        }

        public void setFlow(Double flow) {
            this.flow = flow;
        }

        public Double getTime() {
            return time;
        }

        public void setTime(Double time) {
            this.time = time;
        }

        public Boolean getValid() {
            return valid;
        }

        public void setValid(Boolean valid) {
            this.valid = valid;
        }

    }
}
