package com.hsdi.NetMe.network;

import com.hsdi.NetMe.models.response_models.AccountResponse;
import com.hsdi.NetMe.models.response_models.AvatarResponse;
import com.hsdi.NetMe.models.response_models.BaseResponse;
import com.hsdi.NetMe.models.response_models.GetChatLogsResponse;
import com.hsdi.NetMe.models.response_models.GetChatMessageResponse;
import com.hsdi.NetMe.models.response_models.GetChatResponse;
import com.hsdi.NetMe.models.response_models.GetMeetingLogsResponse;
import com.hsdi.NetMe.models.response_models.GetUsersResponse;
import com.hsdi.NetMe.models.response_models.JoinMeetingResponse;
import com.hsdi.NetMe.models.response_models.PinRequestResponse;
import com.hsdi.NetMe.models.response_models.SendMessageResponse;
import com.hsdi.NetMe.models.response_models.StartChatResponse;
import com.hsdi.NetMe.models.response_models.StartMeetingResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NetMeApi {

    /*production*/
    String BASE_URL = "https://idgmimobile.com/";
    String CONST_PARAMS = "netmeapp/xmlrpc.php?json=1&pfName=android&appName=NetMe&dev=1&cmd=";
//    /*staging*/
//    String CONST_PARAMS = "/xmlrpc.php?json=1&pfName=android&appName=NetMe&cmd=";
//    String BASE_URL = "http://staging.netmeapp.com/";

//----------------------------------------- USER NETWORK CALLS -------------------------------------

    @FormUrlEncoded     
    @POST(CONST_PARAMS + "getSecureimage" + "&v=2")
    Call<AvatarResponse> getAvatar(
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("source")        String sourceUrl
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "getUsersFromContacts" + "&v=2")
    Call<GetUsersResponse> getUsersFromContacts (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("contacts")      String contacts
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "forgotPassword" + "&v=2")
    Call<BaseResponse> userForgotPassword (
            @Field("username") String phoneNumber,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "userLogin" + "&v=2")
    Call<AccountResponse> userLogin (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("token")         String gcmToken,
            @Field("mac")           String macAddress,
            @Field("appVersion")    String applicationVersion,
            @Field("pfVersion")     String platformVersion
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "userLogout" + "&v=2")
    Call<BaseResponse> userLogout (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("token")         String gcmToken,
            @Field("mac")           String macAddress, // DeviceUtil.getDeviceId(context)
            @Field("appVersion")    String applicationVersion, // PreferenceManager.getAppVersion(context)
            @Field("pfVersion")     String platformVersion
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "userRegister" + "&v=2")
    Call<AccountResponse> userRegister (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("email")         String email,
            @Field("firstname")     String firstName,
            @Field("lastname")      String lastName,
            @Field("token")         String gcmToken,
            @Field("mac")           String macAddress,
            @Field("appVersion")    String applicationVersion,
            @Field("pfVersion")     String platformVersion
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "updateUser" + "&v=2")
    Call<AccountResponse> userTransferUpdate (
            @Field("countrycode")   String countryCode,
            @Field("username")      String username,
            @Field("password")      String usernameAsPassword,
            @Field("newPassword")   String newPassword,
            @Field("newFirstname")  String firstName,
            @Field("newLastname")   String lastName,
            @Field("newEmail")      String email,
            @Field("token")         String gcmToken,
            @Field("mac")           String macAddress,
            @Field("appVersion")    String applicationVersion,
            @Field("pfVersion")     String platformVersion
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "updateUser" + "&v=2")
    Call<BaseResponse> userChangePassword (
            @Field("email")     String email,
            @Field("password")     String oldPassword,
            @Field("newPassword")  String newPassword
    );

    @Multipart
    @POST(CONST_PARAMS + "updateUser" + "&v=2")
    Call<AccountResponse> userUpdate(
            @Part MultipartBody.Part countryCodePart,
            @Part MultipartBody.Part phoneNumberPart,
            @Part MultipartBody.Part passwordPart,
            @Part MultipartBody.Part firstNamePart,
            @Part MultipartBody.Part lastNamePart,
            @Part MultipartBody.Part gcmTokenPart,
            @Part MultipartBody.Part macAddressPart,
            @Part MultipartBody.Part applicationVersionPart,
            @Part MultipartBody.Part platformVersionPart,
            @Part MultipartBody.Part avatarPart
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "sendSms2" + "&v=2")
    Call<PinRequestResponse> verifyPhone(
            @Field("callingcode")   String countryCode,
            @Field("to")            String phoneNumber,
            @Field("countrycode")   String isoCountry,
            @Field("languagecode")  String isoLanguage
    );


//------------------------------------------ TEXT CHAT NETWORK CALLS -------------------------------


    @GET(CONST_PARAMS + "editChatMessage" + "&v=2")
    Call<BaseResponse> editChatMessage (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("chatId")        long chatId,
            @Field("messageId")     long messageId,
            @Field("message")       String newMessage

    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "getChat" + "&msg_order=desc&v=2")
    Call<GetChatResponse> getChat (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("chatId")        long chatId,
            @Field("messageId")     String messageId // this is a string to allow for empty param
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "getChatsLog" + "&msg_limit=1&box=unified" + "&v=2")
    Call<GetChatLogsResponse> getChatLogs (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("limit")         int countLimit,
            @Field("msg_date")      String olderThanDate
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "getChatMessage" + "&v=2")
    Call<GetChatMessageResponse> getChatMessage (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("chatId")        long chatId,
            @Field("messageId")     long messageId
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "removeChat" + "&v=2")
    Call<BaseResponse> removeChat (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("chatId")        long chatId
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "removeChatMessage" + "&v=2")
    Call<BaseResponse> removeChatMessage (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("chatId")        long chatId,
            @Field("messageId")     long messageId
    );

    @Multipart
    @POST(CONST_PARAMS + "sendChatMessage" + "&v=3")
    Call<SendMessageResponse> sendMessage (
            @Part MultipartBody.Part countryCodePart,
            @Part MultipartBody.Part phoneNumberPart,
            @Part MultipartBody.Part passwordPart,
            @Part MultipartBody.Part chatIdPart,
            @Part MultipartBody.Part subjectPart,
            @Part MultipartBody.Part textPart,
            @Part MultipartBody.Part mediaPart
    );

    @Multipart
    @POST(CONST_PARAMS + "startChat" + "&v=3")
    Call<StartChatResponse> startChat (
            @Part MultipartBody.Part countryCodePart,
            @Part MultipartBody.Part phoneNumberPart,
            @Part MultipartBody.Part passwordPart,
            @Part MultipartBody.Part invitedUsersPart, // comma separated string
            @Part MultipartBody.Part subjectPart,
            @Part MultipartBody.Part textPart,
            @Part MultipartBody.Part mediaPart
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "sendTypingType" + "&n=1&v=2")
    Call<BaseResponse> sendTypingType (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("chatId")        long chatId,
            @Field("typingType")   String typingType
    );



//----------------------------------------- VIDEO MEETING NETWORK CALLS ----------------------------


    @FormUrlEncoded
    @POST(CONST_PARAMS + "getMeetingsLog" + "&msg_limit=1&box=unified" + "&v=2")
    Call<GetMeetingLogsResponse> getMeetingLogs (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("page")          long oldestMeetingSeconds,
            @Field("limit")         int meetingRequestQuantity
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "removeMeeting" + "&v=2")
    Call<BaseResponse> removeMeeting (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("meetingId")     long meetingId
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "startMeeting" + "&n=1&v=2")
    Call<StartMeetingResponse> startMeeting (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("invite")        String invited,
            @Field("chatId")        String chatId,
            @Field("meetingType")  int meetingType

    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "joinMeeting" + "&n=1&v=2")
    Call<JoinMeetingResponse> joinMeeting (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("meetingId")     long meetingId
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "rejectMeetingInvite" + "&n=1&v=2")
    Call<BaseResponse> rejectVideo (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("meetingId")     long meetingId
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "leaveMeeting" + "&n=1&v=2")
    Call<BaseResponse> leaveVideo (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("meetingId")     long meetingId
    );

    @FormUrlEncoded
    @POST(CONST_PARAMS + "endMeeting" + "&n=1&v=2")
    Call<BaseResponse> closeVideo (
            @Field("countrycode")   String countryCode,
            @Field("username")      String phoneNumber,
            @Field("password")      String password,
            @Field("meetingId")     long meetingId
    );
}