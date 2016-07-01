package de.gimik.app.allpresanapp.networking;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.gimik.app.allpresanapp.viewmodel.SurveyInfo;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


/**
 * Created by QuyenDT on 2/10/15.
 */
public class AllpresansAPI {

    public static final String DOMAIN_NAME = "http://82.115.100.250:8080/allpresanbackend/rest/";
    //public static final String DOMAIN_NAME = "http://10.0.0.31:8080/rest/";
    private static AllpresanService service;
    private static final String USERNAME = "end_user";
    private static final String PASSWORD = "!get allpresan data!";

    //
    public static AllpresanService getService() {
        if (service != null) {
            return service;
        }

        // create client
        okhttp3.OkHttpClient.Builder okhttpBuilder = new okhttp3.OkHttpClient.Builder();
        okhttpBuilder
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        final String credentials = Credentials.basic(USERNAME, PASSWORD);
                        return response.request().newBuilder()
                                .header("Authorization", credentials)
                                .build();
                    }
                })
                /*
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest = request.newBuilder().addHeader("Accept", "application/json").build();
                        return chain.proceed(newRequest);
                    }
                })*/
                //.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectionPool(new okhttp3.ConnectionPool(0, 5 * 60, TimeUnit.SECONDS))
        ;

        okhttp3.OkHttpClient client = okhttpBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DOMAIN_NAME)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(AllpresanService.class);

        return service;
    }


    // MEvents service API
    public interface AllpresanService {

        @GET("member/checByMembernumberAndPostcode")
        Observable<JsonObject> login(@Query("memberNumber") String memberNumber, @Query("postCode") String postCode);

        // Download database from server
        @GET("clientapp/getData")
        //Observable<Response> downloadDB(@Query("lastTime") String lastTime);
        @Streaming
        Observable<retrofit2.Response<ResponseBody>> downloadDB(@Query("lastTime") String lastTime);

        @GET
        Observable<retrofit2.Response<ResponseBody>> fetchImage(@Url String url);

        // send training
        @POST("clientapp/email/survey")
        Observable<JsonObject> sendSurvey(@Body SurveyInfo surveyInfo);

        @POST("clientapp/email/contact")
        Observable<Result> sendContact(@Body ContactEmailInfo body);

        @POST("clientapp/email/eventregistration")
        Observable<Result> sendEventRegistration(@Body EventRegistrationInfo body);

        @Multipart
        @Headers({
                "Content-Type: application/octet-stream"
        })
        @POST("clientapp/email/request")
        Observable<Result> sendPictureContact(@Part("picture") RequestBody picture,
                                              @Part("customerNumber") String customerNumber,
                                              @Part("email") String email,
                                              @Part("subject") String subject,
                                              @Part("message") String message,
                                              @Part("copy") Integer copy);

        //
        @POST("pushservice/add-token")
        @FormUrlEncoded
        Observable<JsonObject> pushService(@Field("device_type") int device_type, @Field("device_token") String token);

        public class ContactEmailInfo {
            String firm;
            String forename;
            String surname;
            String street;
            String houseNumber;
            String zipCode;
            String city;
            String email;
            String phone;
            Integer recall;
            String message;
            Integer copy;
            String subject;

            public ContactEmailInfo(String firm, String forename, String surname, String street, String houseNumber,
                                    String zipCode, String city, String email, String phone, Integer recall,
                                    String message, Integer copy, String subject) {
                this.firm = firm;
                this.forename = forename;
                this.surname = surname;
                this.street = street;
                this.houseNumber = houseNumber;
                this.zipCode = zipCode;
                this.city = city;
                this.email = email;
                this.phone = phone;
                this.recall = recall;
                this.message = message;
                this.copy = copy;
                this.subject = subject;
            }
        }

        public class EventRegistrationInfo {
            String eventName;
            String firm;
            String forename;
            String surname;
            String birthday;
            String street;
            String houseNumber;
            String zipCode;
            String city;
            String email;
            String phone;
            Integer secondPerson;
            String personForename;
            String personSurname;
            String personBirthday;
            Integer copy;
            String subject;

            public EventRegistrationInfo(String eventName, String firm, String forename, String surname, String birthday, String street,
                                         String houseNumber, String zipCode, String city, String email, String phone,
                                         Integer secondPerson, String personForename, String personSurname, String personBirthday, Integer copy, String subject) {
                this.eventName = eventName;
                this.firm = firm;
                this.forename = forename;
                this.surname = surname;
                this.birthday = birthday;
                this.street = street;
                this.houseNumber = houseNumber;
                this.zipCode = zipCode;
                this.city = city;
                this.email = email;
                this.phone = phone;
                this.secondPerson = secondPerson;
                this.personForename = personForename;
                this.personSurname = personSurname;
                this.personBirthday = personBirthday;
                this.copy = copy;
                this.subject = subject;
            }
        }

        public class Result {
            String result;
            String message;
        }
    }


}
