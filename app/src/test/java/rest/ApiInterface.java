package rest;


import model.VersionResponse;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {

    @GET("app_version.htm")
    Call<VersionResponse> getVersionCode();

}


