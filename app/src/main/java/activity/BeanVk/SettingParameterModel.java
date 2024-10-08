
package activity.BeanVk;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class SettingParameterModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("response")
    private List<SettingParameterResponse> mResponse;
    @SerializedName("status")
    private Boolean mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
    public List<SettingParameterResponse> getResponse() {
        return mResponse;
    }

    public void setResponse(List<SettingParameterResponse> response) {
        mResponse = response;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

}
