package activity.BeanVk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MotorParamListModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
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

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public class Response {

        @SerializedName("pmId")
        @Expose
        private Integer pmId;
        @SerializedName("parametersName")
        @Expose
        private String parametersName;
        @SerializedName("modbusaddress")
        @Expose
        private String modbusaddress;
        @SerializedName("mobBTAddress")
        @Expose
        private String mobBTAddress;
        @SerializedName("factor")
        @Expose
        private Integer factor;
        @SerializedName("pValue")
        @Expose
        private Integer pValue;
        @SerializedName("materialCode")
        @Expose
        private String materialCode;
        @SerializedName("unit")
        @Expose
        private String unit;
        @SerializedName("offset")
        @Expose
        private Integer offset;

        public Integer getPmId() {
            return pmId;
        }

        public void setPmId(Integer pmId) {
            this.pmId = pmId;
        }

        public String getParametersName() {
            return parametersName;
        }

        public void setParametersName(String parametersName) {
            this.parametersName = parametersName;
        }

        public String getModbusaddress() {
            return modbusaddress;
        }

        public void setModbusaddress(String modbusaddress) {
            this.modbusaddress = modbusaddress;
        }

        public String getMobBTAddress() {
            return mobBTAddress;
        }

        public void setMobBTAddress(String mobBTAddress) {
            this.mobBTAddress = mobBTAddress;
        }

        public Integer getFactor() {
            return factor;
        }

        public void setFactor(Integer factor) {
            this.factor = factor;
        }

        public Integer getpValue() {
            return pValue;
        }

        public void setpValue(Integer pValue) {
            this.pValue = pValue;
        }

        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

    }
}
