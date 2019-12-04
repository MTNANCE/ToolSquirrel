package no.purplecloud.toolsquirrel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetroFitApi {
    @Multipart
    @POST("projects")
    Call<String> createPost(@Part("name") RequestBody name, @Part("desc") RequestBody desc,
                            @Part("location") RequestBody location, @Part("employee_id") RequestBody eid,
                            @Part MultipartBody.Part image);
}
