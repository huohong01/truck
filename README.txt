Anthor�� Yi
Date��2016.06.28
Message �� add textFragment and voiceFragment switch function when users are voicing call.
Modify files��
java\com\hsdi\NetMe\ui\chat\audio\VoiceFragment.java
java/com/hsdi/NetMe/ui/chat/ChatActivity.java
java/com/hsdi/NetMe/ui/chat/ChatInterface.java
java/com/hsdi/NetMe/ui/chat/video/VideoFragment.java
res/layout/fragment_voice.xml


Anthor�� Yi
Date��2016.06.28
Message �� fix abnormal account bug
bug��When the user kill the process of NetMe, and then they restart it.
 Sometimes because of the poor network or other unpredictable things,the account will be replaced by a strange account
Modify files��
app/src/main/java/com/hsdi/NetMe/ui/startup/LoginActivity.java



Anthor�� Yi
Date��2016.06.29
Message �� fix abnormal account bug
bug��When the user kill the process of NetMe, and then they restart it.
 Sometimes because of the poor network or other unpredictable things,the account will be replaced by a strange account
Modify files��
app/src/main/java/com/hsdi/NetMe/ui/startup/LoginActivity.java



Anthor�� Yi
Date��2016.07.04
Message ��fix GcmBroadcastReceiver.onReceive ANR bug
bug��ANR
Modify files��
app/src/main/AndroidManifest.xml
app/src/main/java/com/hsdi/NetMe/network/GcmBroadcastReceiver.java
app/src/main/java/com/hsdi/NetMe/util/Constants.java
Add file:
app/src/main/java/com/hsdi/NetMe/network/NotificationService.java


Anthor�� Yi
Date��2016.07.05
Message ��fix setFilteredChats NullPointerException crash
bug��crash
Modify files��
app/src/main/java/com/hsdi/NetMe/ui/main/message_logs/MessageLogAdapter.java


Anthor�� Yi
Date��2016.07.05
Message ��fix Snackbar NullPointerException crash
bug��crash
Modify files��
app/src/main/java/com/hsdi/NetMe/ui/main/message_logs/MessageLogFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/recent_logs/RecentFragment.java


Anthor�� Yi
Date��2016.07.06
Message ��fix google OOM caused by toString
bug��OOM crash
Modify files��
app/src/main/java/com/hsdi/NetMe/util/AESCrypt.java


Anthor�� Yi
Date��2016.07.17
Message ��fix pin verify problem
Modify files��
app/src/main/java/com/hsdi/NetMe/network/GCMRegisterTask.java
app/src/main/java/com/hsdi/NetMe/ui/startup/PhoneRetrievalActivity.java
app/src/main/res/values/strings.xml


Anthor�� Yi
Date��2016.07.18
Message ��fix RejectedExecutionException crash
Modify files��
app/src/main/java/com/hsdi/NetMe/ui/chat/util/MessageManager.java
app/src/main/java/com/hsdi/NetMe/ui/startup/PhoneRetrievalActivity.java
app/src/main/res/values/strings.xml

Add files:
app/src/main/java/com/hsdi/NetMe/util/ThreadPool.java


Anthor�� Yi
Date��2016.07.28
Message ��add message cache
Modify files��
app/src/main/java/com/hsdi/NetMe/database/ChatMessageManager.java
app/src/main/java/com/hsdi/NetMe/database/ChatTrackerManager.java
app/src/main/java/com/hsdi/NetMe/database/DatabaseHelper.java
app/src/main/java/com/hsdi/NetMe/database/ParticipantManager.java
app/src/main/java/com/hsdi/NetMe/models/Participant.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/chat/util/MessageManager.java
app/src/main/java/com/hsdi/NetMe/ui/main/message_logs/MessageLogFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/message_logs/MessageLogHolder.java
app/src/main/java/com/hsdi/NetMe/util/DialogUtils.java



Anthor�� Yi
Date��2016.07.28
Message ��modify chatName
Modify files��
app/src/main/java/com/hsdi/NetMe/database/ChatTrackerManager.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/message_logs/MessageLogFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/message_logs/MessageLogHolder.java




Anthor�� Yi
Date��2016.07.31
Message ��add recent record memory cache
Modify files��
app/src/main/java/com/hsdi/NetMe/database/DatabaseContract.java
app/src/main/java/com/hsdi/NetMe/database/DatabaseHelper.java
app/src/main/java/com/hsdi/NetMe/models/Meeting.java
app/src/main/java/com/hsdi/NetMe/ui/main/recent_logs/RecentFragment.java

Add files:
app/src/main/java/com/hsdi/NetMe/database/MeetingManager.java




Anthor�� Yi
Date��2016.08.02
Message ��add voice function
Modify files��
app/src/main/java/com/hsdi/NetMe/models/GcmPush.java
app/src/main/java/com/hsdi/NetMe/network/GcmBroadcastReceiver.java
app/src/main/java/com/hsdi/NetMe/network/NetMeApi.java
app/src/main/java/com/hsdi/NetMe/ui/chat/audio/VoiceFragment.java
app/src/main/java/com/hsdi/NetMe/ui/chat/ChatActivity.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/chat/video/VideoFragment.java
app/src/main/java/com/hsdi/NetMe/util/Constants.java
app/build.gradle

Anthor�� Yi
Date��2016.08.08
Message ��modify chatLoad page
Modify files��
app/src/main/java/com/hsdi/NetMe/ui/chat/audio/VoiceFragment.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/chat/util/MessageManager.java
app/src/main/java/com/hsdi/NetMe/ui/chat/util/MsgManagerCallback.java
app/src/main/java/com/hsdi/NetMe/ui/main/recent_logs/RecentFragment.java
app/src/main/res/drawable-hdpi/hands_free.png
app/src/main/res/drawable-hdpi/voice.png
app/src/main/res/drawable-xxhdpi/hands_free.png
app/src/main/res/drawable-xxhdpi/voice.png

Add files:
app/src/main/res/drawable-hdpi/hands_free_select.png
app/src/main/res/drawable-ldpi/voice.png
app/src/main/res/drawable-mdpi/voice.png
app/src/main/res/drawable-xhdpi/voice.png
app/src/main/res/drawable-xxhdpi/hands_free_select.png
app/src/main/res/drawable-xxxhdpi/voice.png
app/src/main/res/layout/dialog_select_call.xml

Anthor�� Yi
Date��2016.08.09
Message ��clean sharedPreference cache and update version
Modify files��
app/build.gradle
app/src/main/java/com/hsdi/MinitPay/LoginInMiNitActivity.java
app/src/main/java/com/hsdi/MinitPay/LoginOutActivity.java
app/src/main/java/com/hsdi/MinitPay/MinitPayActivity.java
app/src/main/java/com/hsdi/MinitPay/util/SharePrefManager.java
app/src/main/java/com/hsdi/NetMe/database/DatabaseHelper.java
app/src/main/java/com/hsdi/NetMe/NetMeApp.java
app/src/main/java/com/hsdi/NetMe/ui/startup/PhoneRetrievalActivity.java
app/src/main/java/com/hsdi/NetMe/util/PreferenceManager.java

Add files:
app/src/main/java/com/hsdi/NetMe/util/VersionUtils.java

Anthor�� Yi
Date��2016.08.09
Message ��set voice call Speakerphone
Modify files��
app/build.gradle
app/src/main/java/com/hsdi/NetMe/database/DatabaseHelper.java
app/src/main/java/com/hsdi/NetMe/ui/chat/audio/VoiceFragment.java
app/src/main/res/layout/fragment_voice.xml


Anthor�� Yi
Date��2016.08.15
Message ��add show typing function 
Modify files��
app/src/main/java/com/hsdi/NetMe/models/GcmPush.java
app/src/main/java/com/hsdi/NetMe/network/GcmBroadcastReceiver.java
app/src/main/java/com/hsdi/NetMe/network/NetMeApi.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/chat/video/AnswerFragment.java
app/src/main/java/com/hsdi/NetMe/ui/chat/video/CallFragment.java
app/src/main/java/com/hsdi/NetMe/util/Constants.java
app/src/main/res/layout/fragment_text.xml
app/src/main/res/menu/menu_text_chat_single.xml
app/src/main/res/values/strings.xml

Add files:
app/src/main/java/com/hsdi/NetMe/ui/chat/text/IUpdateInputtingListener.java
app/src/main/res/drawable-hdpi/video_voice_green.png
app/src/main/res/drawable-hdpi/video_voice_red.png
app/src/main/res/drawable-ldpi/video_voice_green.png
app/src/main/res/drawable-ldpi/video_voice_red.png
app/src/main/res/drawable-mdpi/video_voice_green.png
app/src/main/res/drawable-mdpi/video_voice_red.png
app/src/main/res/drawable-xhdpi/video_voice_green.png
app/src/main/res/drawable-xhdpi/video_voice_red.png
app/src/main/res/drawable-xxhdpi/video_voice_green.png
app/src/main/res/drawable-xxhdpi/video_voice_red.png
app/src/main/res/drawable-xxxhdpi/video_voice_green.png
app/src/main/res/drawable-xxxhdpi/video_voice_red.png
app/src/main/res/drawable/animation_red_green.xml



Anthor�� Yi
Date��2016.08.17
Message ��fix voice/video call abnormal bug and update gradle version
Modify files��
app/build.gradle
app/src/main/AndroidManifest.xml
app/src/main/java/com/hsdi/NetMe/database/DatabaseHelper.java
app/src/main/java/com/hsdi/NetMe/NetMeApp.java
app/src/main/java/com/hsdi/NetMe/ui/chat/audio/VoiceFragment.java
app/src/main/java/com/hsdi/NetMe/ui/chat/video/CallFragment.java
app/src/main/java/com/hsdi/NetMe/ui/chat/video/VideoFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/recent_logs/RecentFragment.java
app/src/main/java/com/hsdi/NetMe/util/PreferenceManager.java
build.gradle
gradle/wrapper/gradle-wrapper.properties
minitpay/build.gradle

Add files:
app/src/main/java/com/hsdi/NetMe/network/HeadsetPlugReceiver.java

Anthor�� Yi
Date��2016.08.17
Message ��add detect AES password write and read logcat
Modify files��
app/src/main/java/com/hsdi/NetMe/util/PreferenceManager.java

Add files:
app/src/main/java/com/hsdi/NetMe/pwd
app/src/main/java/com/hsdi/NetMe/pwd/PwdManager.java



Anthor�� Yi
Date��2016.08.21
Message ��add change skin function and delete useless file
Modify files��
app/build.gradle
app/src/main/AndroidManifest.xml
app/src/main/java/com/hsdi/NetMe/ui/main/MainActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/LoginActivity.java
app/src/main/java/com/hsdi/NetMe/util/MyLog.java
app/src/main/res/layout/activity_contact_detail.xml
app/src/main/res/layout/activity_login.xml
app/src/main/res/layout/activity_main.xml
app/src/main/res/layout/activity_register.xml
app/src/main/res/layout/fragment_phone_entry.xml
app/src/main/res/layout/fragment_pin_entry.xml
app/src/main/res/layout/fragment_recovery_failed.xml
app/src/main/res/layout/fragment_recovery_method.xml
app/src/main/res/layout/fragment_recovery_success.xml
app/src/main/res/layout/item_contact.xml
app/src/main/res/layout/item_contact_detail.xml
app/src/main/res/layout/item_tab.xml
app/src/main/res/mipmap-mdpi/ic_launcher.png
app/src/main/res/mipmap-xhdpi/ic_launcher.png
app/src/main/res/mipmap-xxhdpi/ic_launcher.png
app/src/main/res/values/colors.xml
app/src/main/res/values/dimens.xml
app/src/main/res/values/strings.xml
app/src/main/res/values/styles.xml
settings.gradle

Add files:
app/src/main/java/com/hsdi/NetMe/ActivityCollector.java
app/src/main/java/com/hsdi/NetMe/BaseActivity.java
app/src/main/java/com/hsdi/NetMe/models/Theme.java
app/src/main/java/com/hsdi/NetMe/ui/startup/ThemeActivity.java
app/src/main/java/com/hsdi/NetMe/util/PermissionUtils.java
app/src/main/res/drawable/icon_check.png
app/src/main/res/drawable/icon_uncheck.png
app/src/main/res/drawable/theme_blue.png
app/src/main/res/drawable/theme_green.png
app/src/main/res/drawable/theme_orange.png
app/src/main/res/drawable/theme_red.png
app/src/main/res/layout/activity_theme.xml
app/src/main/res/layout/item_theme.xml
move-cmd.cmd
result.txt
theme
theme/.gitignore
theme/libs
theme/proguard-rules.pro
theme/src
theme/src/androidTest
theme/src/androidTest/java
theme/src/androidTest/java/com
theme/src/androidTest/java/com/hsdi
theme/src/androidTest/java/com/hsdi/theme
theme/src/androidTest/java/com/hsdi/theme/ExampleInstrumentedTest.java
theme/src/main
theme/src/main/AndroidManifest.xml
theme/src/main/assets
theme/src/main/assets/THEME
theme/src/main/assets/THEME/ORANGE
theme/src/main/assets/THEME/RED
theme/src/main/java
theme/src/main/java/com
theme/src/main/java/com/hsdi
theme/src/main/java/com/hsdi/theme
theme/src/main/java/com/hsdi/theme/basic
theme/src/main/java/com/hsdi/theme/basic/BaseThemeActivity.java
theme/src/main/java/com/hsdi/theme/basic/ThemeHandlerFactory.java
theme/src/main/java/com/hsdi/theme/basic/ThemeLayoutInflaterFactory.java
theme/src/main/java/com/hsdi/theme/basic/ThemeManager.java
theme/src/main/java/com/hsdi/theme/file
theme/src/main/java/com/hsdi/theme/file/FileOperater.java
theme/src/main/java/com/hsdi/theme/handler
theme/src/main/java/com/hsdi/theme/handler/BackgroundColorThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/BackgroundDrawableThremeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ColorThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/DrawableLeftThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/DrawableThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/SrcThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/StyleBackgroundDrawableThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/StyleTextColorThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/TextColorHintThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/TextColorThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ThemeHandler.java
theme/src/main/res
theme/src/main/res/drawable
theme/src/main/res/values
theme/src/main/res/values/strings.xml
theme/src/test
theme/src/test/java
theme/src/test/java/com
theme/src/test/java/com/hsdi
theme/src/test/java/com/hsdi/theme
theme/src/test/java/com/hsdi/theme/ExampleUnitTest.java

delete files:
app/src/main/res/drawable-hdpi/hands_free_on.png
app/src/main/res/drawable-hdpi/ringing_phone.png
app/src/main/res/drawable-hdpi/typing.png
app/src/main/res/drawable-ldpi/typing.png
app/src/main/res/drawable-mdpi/ringing_phone.png
app/src/main/res/drawable-mdpi/typing.png
app/src/main/res/drawable-xhdpi/ringing_phone.png
app/src/main/res/drawable-xhdpi/typing.png
app/src/main/res/drawable-xxhdpi/hands_free_on.png
app/src/main/res/drawable-xxhdpi/ringing_phone.png
app/src/main/res/drawable-xxhdpi/typing.png
app/src/main/res/drawable-xxxhdpi/btn_options_dwn.png
app/src/main/res/drawable-xxxhdpi/btn_options_up.png
app/src/main/res/drawable-xxxhdpi/typing.png
app/src/main/res/layout/activity_change_pwd2.xml
app/src/main/res/layout/activity_start_chat.xml
app/src/main/res/layout/activity_test.xml
app/src/main/res/mipmap-hdpi/audio_button.png
app/src/main/res/mipmap-hdpi/ic_action_chat.png
app/src/main/res/mipmap-hdpi/ic_action_edit.png
app/src/main/res/mipmap-hdpi/ic_action_group.png
app/src/main/res/mipmap-hdpi/ic_action_paste.png
app/src/main/res/mipmap-hdpi/ic_action_search.png
app/src/main/res/mipmap-hdpi/ic_action_video.png
app/src/main/res/mipmap-hdpi/ic_action_volume_muted.png
app/src/main/res/mipmap-hdpi/ic_action_volume_on.png
app/src/main/res/mipmap-hdpi/ic_launcher.png
app/src/main/res/mipmap-hdpi/video_voice.png
app/src/main/res/mipmap-hdpi/voice.png
app/src/main/res/mipmap-hdpi/voice_button.png
app/src/main/res/mipmap-mdpi/audio_button.png
app/src/main/res/mipmap-mdpi/ic_action_chat.png
app/src/main/res/mipmap-mdpi/ic_action_edit.png
app/src/main/res/mipmap-mdpi/ic_action_group.png
app/src/main/res/mipmap-mdpi/ic_action_paste.png
app/src/main/res/mipmap-mdpi/ic_action_search.png
app/src/main/res/mipmap-mdpi/ic_action_video.png
app/src/main/res/mipmap-mdpi/ic_action_volume_muted.png
app/src/main/res/mipmap-mdpi/ic_action_volume_on.png
app/src/main/res/mipmap-mdpi/video_voice.png
app/src/main/res/mipmap-mdpi/voice.png
app/src/main/res/mipmap-mdpi/voice_button.png
app/src/main/res/mipmap-xhdpi/audio_button.png
app/src/main/res/mipmap-xhdpi/ic_action_chat.png
app/src/main/res/mipmap-xhdpi/ic_action_edit.png
app/src/main/res/mipmap-xhdpi/ic_action_group.png
app/src/main/res/mipmap-xhdpi/ic_action_paste.png
app/src/main/res/mipmap-xhdpi/ic_action_search.png
app/src/main/res/mipmap-xhdpi/ic_action_video.png
app/src/main/res/mipmap-xhdpi/ic_action_volume_muted.png
app/src/main/res/mipmap-xhdpi/ic_action_volume_on.png
app/src/main/res/mipmap-xhdpi/video_voice.png
app/src/main/res/mipmap-xhdpi/voice.png
app/src/main/res/mipmap-xhdpi/voice_button.png
app/src/main/res/mipmap-xxhdpi/audio_button.png
app/src/main/res/mipmap-xxhdpi/ic_action_chat.png
app/src/main/res/mipmap-xxhdpi/ic_action_edit.png
app/src/main/res/mipmap-xxhdpi/ic_action_group.png
app/src/main/res/mipmap-xxhdpi/ic_action_paste.png
app/src/main/res/mipmap-xxhdpi/ic_action_search.png
app/src/main/res/mipmap-xxhdpi/ic_action_video.png
app/src/main/res/mipmap-xxhdpi/ic_action_volume_muted.png
app/src/main/res/mipmap-xxhdpi/ic_action_volume_on.png
app/src/main/res/mipmap-xxhdpi/video_voice.png
app/src/main/res/mipmap-xxhdpi/voice.png
app/src/main/res/mipmap-xxhdpi/voice_button.png
app/src/main/res/mipmap-xxxhdpi/audio_button.png
app/src/main/res/mipmap-xxxhdpi/ic_launcher.png
app/src/main/res/mipmap-xxxhdpi/video_voice.png
app/src/main/res/mipmap-xxxhdpi/voice.png
app/src/main/res/mipmap-xxxhdpi/voice_button.png


Anthor�� Yi
Date��2016.08.21
Message ��modify login and register page skin
Modify files��
app/build.gradle
app/src/main/java/com/hsdi/NetMe/ui/ContactListUtils/ContactHolder.java
app/src/main/java/com/hsdi/NetMe/ui/main/favorites/FavoritesHolder.java
app/src/main/java/com/hsdi/NetMe/ui/main/MainActivity.java
app/src/main/java/com/hsdi/NetMe/ui/main/message_logs/MessageLogHolder.java
app/src/main/java/com/hsdi/NetMe/ui/main/recent_logs/RecentHolder.java
app/src/main/java/com/hsdi/NetMe/ui/main/slidingtab/SlidingTabLayout.java
app/src/main/java/com/hsdi/NetMe/ui/startup/AccountRecoveryActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/ChangePwdActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/PhoneRetrievalActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/RegisterActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/VersionActivity.java
app/src/main/res/layout/activity_contact_detail.xml
app/src/main/res/layout/item_favorite.xml
app/src/main/res/layout/item_log_meeting.xml
app/src/main/res/layout/item_log_message.xml
app/src/main/res/mipmap-hdpi/ic_launcher.png
app/src/main/res/mipmap-mdpi/ic_launcher.png
app/src/main/res/mipmap-xhdpi/ic_launcher.png
app/src/main/res/mipmap-xxhdpi/ic_launcher.png
app/src/main/res/mipmap-xxxhdpi/ic_launcher.png
move-cmd.cmd
settings.gradle
theme/src/main/java/com/hsdi/theme/basic/BaseThemeActivity.java
theme/src/main/java/com/hsdi/theme/basic/ThemeHandlerFactory.java
theme/src/main/java/com/hsdi/theme/basic/ThemeLayoutInflaterFactory.java
theme/src/main/java/com/hsdi/theme/handler/BackgroundDrawableThremeHandler.java
theme/src/main/java/com/hsdi/theme/handler/DrawableLeftThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/DrawableThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/SrcThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ThemeHandler.java

Add files:
app/src/main/assets/THEME
app/src/main/assets/THEME/GREEN
app_theme_green
app_theme_green/.gitignore
app_theme_green/libs
app_theme_green/proguard-rules.pro
app_theme_green/src
app_theme_green/src/androidTest
app_theme_green/src/androidTest/java
app_theme_green/src/androidTest/java/com
app_theme_green/src/androidTest/java/com/hsdi
app_theme_green/src/androidTest/java/com/hsdi/app_theme_green
app_theme_green/src/androidTest/java/com/hsdi/app_theme_green/ExampleInstrumentedTest.java
app_theme_green/src/main
app_theme_green/src/main/AndroidManifest.xml
app_theme_green/src/main/res
app_theme_green/src/main/res/512.png
app_theme_green/src/main/res/color
app_theme_green/src/main/res/color/btn_textcolor.xml
app_theme_green/src/main/res/drawable
app_theme_green/src/main/res/drawable-hdpi
app_theme_green/src/main/res/drawable-hdpi/add.png
app_theme_green/src/main/res/drawable-hdpi/attach.png
app_theme_green/src/main/res/drawable-hdpi/audio_button.png
app_theme_green/src/main/res/drawable-hdpi/back.png
app_theme_green/src/main/res/drawable-hdpi/btn_edit_dwn.png
app_theme_green/src/main/res/drawable-hdpi/btn_edit_up.png
app_theme_green/src/main/res/drawable-hdpi/camera.png
app_theme_green/src/main/res/drawable-hdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-hdpi/check.png
app_theme_green/src/main/res/drawable-hdpi/circle_close.png
app_theme_green/src/main/res/drawable-hdpi/clear.png
app_theme_green/src/main/res/drawable-hdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-hdpi/empty_avatar.png
app_theme_green/src/main/res/drawable-hdpi/favorite.png
app_theme_green/src/main/res/drawable-hdpi/group_avatar.png
app_theme_green/src/main/res/drawable-hdpi/hands_free.png
app_theme_green/src/main/res/drawable-hdpi/hands_free_select.png
app_theme_green/src/main/res/drawable-hdpi/logo.png
app_theme_green/src/main/res/drawable-hdpi/mail.png
app_theme_green/src/main/res/drawable-hdpi/menu_overflow.png
app_theme_green/src/main/res/drawable-hdpi/message.png
app_theme_green/src/main/res/drawable-hdpi/missed_call.png
app_theme_green/src/main/res/drawable-hdpi/password_icon.png
app_theme_green/src/main/res/drawable-hdpi/phone.png
app_theme_green/src/main/res/drawable-hdpi/record.png
app_theme_green/src/main/res/drawable-hdpi/search.png
app_theme_green/src/main/res/drawable-hdpi/send_btn.png
app_theme_green/src/main/res/drawable-hdpi/sync.png
app_theme_green/src/main/res/drawable-hdpi/tabs_contacts.png
app_theme_green/src/main/res/drawable-hdpi/tabs_contacts_selected.png
app_theme_green/src/main/res/drawable-hdpi/tabs_favorites.png
app_theme_green/src/main/res/drawable-hdpi/tabs_favorites_selected.png
app_theme_green/src/main/res/drawable-hdpi/tabs_messages.png
app_theme_green/src/main/res/drawable-hdpi/tabs_messages_selected.png
app_theme_green/src/main/res/drawable-hdpi/tabs_recent.png
app_theme_green/src/main/res/drawable-hdpi/tabs_recent_selected.png
app_theme_green/src/main/res/drawable-hdpi/username_icon.png
app_theme_green/src/main/res/drawable-hdpi/video.png
app_theme_green/src/main/res/drawable-hdpi/video_voice.png
app_theme_green/src/main/res/drawable-hdpi/video_voice_green.png
app_theme_green/src/main/res/drawable-hdpi/video_voice_red.png
app_theme_green/src/main/res/drawable-hdpi/voice.png
app_theme_green/src/main/res/drawable-ldpi
app_theme_green/src/main/res/drawable-ldpi/add.png
app_theme_green/src/main/res/drawable-ldpi/attach.png
app_theme_green/src/main/res/drawable-ldpi/back.png
app_theme_green/src/main/res/drawable-ldpi/camera.png
app_theme_green/src/main/res/drawable-ldpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-ldpi/drawer_bg.png
app_theme_green/src/main/res/drawable-ldpi/empty_avatar.png
app_theme_green/src/main/res/drawable-ldpi/favorite.png
app_theme_green/src/main/res/drawable-ldpi/group_avatar.png
app_theme_green/src/main/res/drawable-ldpi/mail.png
app_theme_green/src/main/res/drawable-ldpi/message.png
app_theme_green/src/main/res/drawable-ldpi/missed_call.png
app_theme_green/src/main/res/drawable-ldpi/password_icon.png
app_theme_green/src/main/res/drawable-ldpi/phone.png
app_theme_green/src/main/res/drawable-ldpi/record.png
app_theme_green/src/main/res/drawable-ldpi/search.png
app_theme_green/src/main/res/drawable-ldpi/send_btn.png
app_theme_green/src/main/res/drawable-ldpi/sync.png
app_theme_green/src/main/res/drawable-ldpi/tabs_contacts.png
app_theme_green/src/main/res/drawable-ldpi/tabs_contacts_selected.png
app_theme_green/src/main/res/drawable-ldpi/tabs_favorites.png
app_theme_green/src/main/res/drawable-ldpi/tabs_favorites_selected.png
app_theme_green/src/main/res/drawable-ldpi/tabs_messages.png
app_theme_green/src/main/res/drawable-ldpi/tabs_messages_selected.png
app_theme_green/src/main/res/drawable-ldpi/tabs_recent.png
app_theme_green/src/main/res/drawable-ldpi/tabs_recent_selected.png
app_theme_green/src/main/res/drawable-ldpi/username_icon.png
app_theme_green/src/main/res/drawable-ldpi/video.png
app_theme_green/src/main/res/drawable-ldpi/video_voice_green.png
app_theme_green/src/main/res/drawable-ldpi/video_voice_red.png
app_theme_green/src/main/res/drawable-ldpi/voice.png
app_theme_green/src/main/res/drawable-mdpi
app_theme_green/src/main/res/drawable-mdpi/add.png
app_theme_green/src/main/res/drawable-mdpi/attach.png
app_theme_green/src/main/res/drawable-mdpi/back.png
app_theme_green/src/main/res/drawable-mdpi/btn_edit_dwn.png
app_theme_green/src/main/res/drawable-mdpi/btn_edit_up.png
app_theme_green/src/main/res/drawable-mdpi/camera.png
app_theme_green/src/main/res/drawable-mdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-mdpi/check.png
app_theme_green/src/main/res/drawable-mdpi/circle_close.png
app_theme_green/src/main/res/drawable-mdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-mdpi/empty_avatar.png
app_theme_green/src/main/res/drawable-mdpi/favorite.png
app_theme_green/src/main/res/drawable-mdpi/group_avatar.png
app_theme_green/src/main/res/drawable-mdpi/logo.png
app_theme_green/src/main/res/drawable-mdpi/mail.png
app_theme_green/src/main/res/drawable-mdpi/menu_overflow.png
app_theme_green/src/main/res/drawable-mdpi/message.png
app_theme_green/src/main/res/drawable-mdpi/missed_call.png
app_theme_green/src/main/res/drawable-mdpi/password_icon.png
app_theme_green/src/main/res/drawable-mdpi/phone.png
app_theme_green/src/main/res/drawable-mdpi/record.png
app_theme_green/src/main/res/drawable-mdpi/search.png
app_theme_green/src/main/res/drawable-mdpi/send_btn.png
app_theme_green/src/main/res/drawable-mdpi/sync.png
app_theme_green/src/main/res/drawable-mdpi/tabs_contacts.png
app_theme_green/src/main/res/drawable-mdpi/tabs_contacts_selected.png
app_theme_green/src/main/res/drawable-mdpi/tabs_favorites.png
app_theme_green/src/main/res/drawable-mdpi/tabs_favorites_selected.png
app_theme_green/src/main/res/drawable-mdpi/tabs_messages.png
app_theme_green/src/main/res/drawable-mdpi/tabs_messages_selected.png
app_theme_green/src/main/res/drawable-mdpi/tabs_recent.png
app_theme_green/src/main/res/drawable-mdpi/tabs_recent_selected.png
app_theme_green/src/main/res/drawable-mdpi/username_icon.png
app_theme_green/src/main/res/drawable-mdpi/video.png
app_theme_green/src/main/res/drawable-mdpi/video_voice_green.png
app_theme_green/src/main/res/drawable-mdpi/video_voice_red.png
app_theme_green/src/main/res/drawable-mdpi/voice.png
app_theme_green/src/main/res/drawable-xhdpi
app_theme_green/src/main/res/drawable-xhdpi/add.png
app_theme_green/src/main/res/drawable-xhdpi/attach.png
app_theme_green/src/main/res/drawable-xhdpi/attachment_icon.png
app_theme_green/src/main/res/drawable-xhdpi/back.png
app_theme_green/src/main/res/drawable-xhdpi/bottom_gradient.png
app_theme_green/src/main/res/drawable-xhdpi/btn_edit_dwn.png
app_theme_green/src/main/res/drawable-xhdpi/btn_edit_up.png
app_theme_green/src/main/res/drawable-xhdpi/camera.png
app_theme_green/src/main/res/drawable-xhdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-xhdpi/check.png
app_theme_green/src/main/res/drawable-xhdpi/check_icon.png
app_theme_green/src/main/res/drawable-xhdpi/circle_close.png
app_theme_green/src/main/res/drawable-xhdpi/close_icon.png
app_theme_green/src/main/res/drawable-xhdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-xhdpi/empty_avatar.png
app_theme_green/src/main/res/drawable-xhdpi/fav_chat.png
app_theme_green/src/main/res/drawable-xhdpi/fav_video.png
app_theme_green/src/main/res/drawable-xhdpi/favorite.png
app_theme_green/src/main/res/drawable-xhdpi/group_avatar.png
app_theme_green/src/main/res/drawable-xhdpi/img_callrec_icon.png
app_theme_green/src/main/res/drawable-xhdpi/img_callto_icon.png
app_theme_green/src/main/res/drawable-xhdpi/img_greyvid_icon.png
app_theme_green/src/main/res/drawable-xhdpi/logo.png
app_theme_green/src/main/res/drawable-xhdpi/mail.png
app_theme_green/src/main/res/drawable-xhdpi/menu_overflow.png
app_theme_green/src/main/res/drawable-xhdpi/message.png
app_theme_green/src/main/res/drawable-xhdpi/messenger_48_icon.png
app_theme_green/src/main/res/drawable-xhdpi/missed_call.png
app_theme_green/src/main/res/drawable-xhdpi/password_icon.png
app_theme_green/src/main/res/drawable-xhdpi/pause_btn.png
app_theme_green/src/main/res/drawable-xhdpi/phone.png
app_theme_green/src/main/res/drawable-xhdpi/play_btn.png
app_theme_green/src/main/res/drawable-xhdpi/play_msg_icon.png
app_theme_green/src/main/res/drawable-xhdpi/record.png
app_theme_green/src/main/res/drawable-xhdpi/search.png
app_theme_green/src/main/res/drawable-xhdpi/send.png
app_theme_green/src/main/res/drawable-xhdpi/send_btn.png
app_theme_green/src/main/res/drawable-xhdpi/start_rec.png
app_theme_green/src/main/res/drawable-xhdpi/stop_rec.png
app_theme_green/src/main/res/drawable-xhdpi/sync.png
app_theme_green/src/main/res/drawable-xhdpi/tabs_contacts.png
app_theme_green/src/main/res/drawable-xhdpi/tabs_contacts_selected.png
app_theme_green/src/main/res/drawable-xhdpi/tabs_favorites.png
app_theme_green/src/main/res/drawable-xhdpi/tabs_favorites_selected.png
app_theme_green/src/main/res/drawable-xhdpi/tabs_messages.png
app_theme_green/src/main/res/drawable-xhdpi/tabs_messages_selected.png
app_theme_green/src/main/res/drawable-xhdpi/tabs_recent.png
app_theme_green/src/main/res/drawable-xhdpi/tabs_recent_selected.png
app_theme_green/src/main/res/drawable-xhdpi/top_gradient.png
app_theme_green/src/main/res/drawable-xhdpi/username.png
app_theme_green/src/main/res/drawable-xhdpi/username_icon.png
app_theme_green/src/main/res/drawable-xhdpi/video.png
app_theme_green/src/main/res/drawable-xhdpi/video_voice_green.png
app_theme_green/src/main/res/drawable-xhdpi/video_voice_red.png
app_theme_green/src/main/res/drawable-xhdpi/voice.png
app_theme_green/src/main/res/drawable-xxhdpi
app_theme_green/src/main/res/drawable-xxhdpi/add.png
app_theme_green/src/main/res/drawable-xxhdpi/attach.png
app_theme_green/src/main/res/drawable-xxhdpi/audio_button.png
app_theme_green/src/main/res/drawable-xxhdpi/back.png
app_theme_green/src/main/res/drawable-xxhdpi/bottom_gradient.png
app_theme_green/src/main/res/drawable-xxhdpi/btn_edit_dwn.png
app_theme_green/src/main/res/drawable-xxhdpi/btn_edit_up.png
app_theme_green/src/main/res/drawable-xxhdpi/cam_switch_icon.png
app_theme_green/src/main/res/drawable-xxhdpi/camera.png
app_theme_green/src/main/res/drawable-xxhdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-xxhdpi/chat_bubble_sent.9.png
app_theme_green/src/main/res/drawable-xxhdpi/chat_button.png
app_theme_green/src/main/res/drawable-xxhdpi/check.png
app_theme_green/src/main/res/drawable-xxhdpi/circle_close.png
app_theme_green/src/main/res/drawable-xxhdpi/close_chat_button.png
app_theme_green/src/main/res/drawable-xxhdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-xxhdpi/empty_avatar.png
app_theme_green/src/main/res/drawable-xxhdpi/favorite.png
app_theme_green/src/main/res/drawable-xxhdpi/group_avatar.png
app_theme_green/src/main/res/drawable-xxhdpi/hands_free.png
app_theme_green/src/main/res/drawable-xxhdpi/hands_free_select.png
app_theme_green/src/main/res/drawable-xxhdpi/logo.png
app_theme_green/src/main/res/drawable-xxhdpi/mail.png
app_theme_green/src/main/res/drawable-xxhdpi/menu_overflow.png
app_theme_green/src/main/res/drawable-xxhdpi/message.png
app_theme_green/src/main/res/drawable-xxhdpi/missed_call.png
app_theme_green/src/main/res/drawable-xxhdpi/no_pic_avatar.png
app_theme_green/src/main/res/drawable-xxhdpi/password_icon.png
app_theme_green/src/main/res/drawable-xxhdpi/phone.png
app_theme_green/src/main/res/drawable-xxhdpi/record.png
app_theme_green/src/main/res/drawable-xxhdpi/search.png
app_theme_green/src/main/res/drawable-xxhdpi/send_btn.png
app_theme_green/src/main/res/drawable-xxhdpi/sync.png
app_theme_green/src/main/res/drawable-xxhdpi/tabs_contacts.png
app_theme_green/src/main/res/drawable-xxhdpi/tabs_contacts_selected.png
app_theme_green/src/main/res/drawable-xxhdpi/tabs_favorites.png
app_theme_green/src/main/res/drawable-xxhdpi/tabs_favorites_selected.png
app_theme_green/src/main/res/drawable-xxhdpi/tabs_messages.png
app_theme_green/src/main/res/drawable-xxhdpi/tabs_messages_selected.png
app_theme_green/src/main/res/drawable-xxhdpi/tabs_recent.png
app_theme_green/src/main/res/drawable-xxhdpi/tabs_recent_selected.png
app_theme_green/src/main/res/drawable-xxhdpi/top_gradient.png
app_theme_green/src/main/res/drawable-xxhdpi/user_icon.png
app_theme_green/src/main/res/drawable-xxhdpi/username_icon.png
app_theme_green/src/main/res/drawable-xxhdpi/video.png
app_theme_green/src/main/res/drawable-xxhdpi/video_button.png
app_theme_green/src/main/res/drawable-xxhdpi/video_voice_green.png
app_theme_green/src/main/res/drawable-xxhdpi/video_voice_red.png
app_theme_green/src/main/res/drawable-xxhdpi/view_button.png
app_theme_green/src/main/res/drawable-xxhdpi/view_select_button.png
app_theme_green/src/main/res/drawable-xxhdpi/voice.png
app_theme_green/src/main/res/drawable-xxhdpi/voice_button.png
app_theme_green/src/main/res/drawable-xxhdpi/voice_select_button.png
app_theme_green/src/main/res/drawable-xxxhdpi
app_theme_green/src/main/res/drawable-xxxhdpi/add.png
app_theme_green/src/main/res/drawable-xxxhdpi/back.png
app_theme_green/src/main/res/drawable-xxxhdpi/btn_edit_dwn.png
app_theme_green/src/main/res/drawable-xxxhdpi/btn_edit_up.png
app_theme_green/src/main/res/drawable-xxxhdpi/camera.png
app_theme_green/src/main/res/drawable-xxxhdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-xxxhdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-xxxhdpi/empty_avatar.png
app_theme_green/src/main/res/drawable-xxxhdpi/favorite.png
app_theme_green/src/main/res/drawable-xxxhdpi/group_avatar.png
app_theme_green/src/main/res/drawable-xxxhdpi/logo.png
app_theme_green/src/main/res/drawable-xxxhdpi/mail.png
app_theme_green/src/main/res/drawable-xxxhdpi/message.png
app_theme_green/src/main/res/drawable-xxxhdpi/missed_call.png
app_theme_green/src/main/res/drawable-xxxhdpi/password_icon.png
app_theme_green/src/main/res/drawable-xxxhdpi/phone.png
app_theme_green/src/main/res/drawable-xxxhdpi/record.png
app_theme_green/src/main/res/drawable-xxxhdpi/search.png
app_theme_green/src/main/res/drawable-xxxhdpi/send_btn.png
app_theme_green/src/main/res/drawable-xxxhdpi/sync.png
app_theme_green/src/main/res/drawable-xxxhdpi/tabs_contacts.png
app_theme_green/src/main/res/drawable-xxxhdpi/tabs_contacts_selected.png
app_theme_green/src/main/res/drawable-xxxhdpi/tabs_favorites.png
app_theme_green/src/main/res/drawable-xxxhdpi/tabs_favorites_selected.png
app_theme_green/src/main/res/drawable-xxxhdpi/tabs_messages.png
app_theme_green/src/main/res/drawable-xxxhdpi/tabs_messages_selected.png
app_theme_green/src/main/res/drawable-xxxhdpi/tabs_recent.png
app_theme_green/src/main/res/drawable-xxxhdpi/tabs_recent_selected.png
app_theme_green/src/main/res/drawable-xxxhdpi/username_icon.png
app_theme_green/src/main/res/drawable-xxxhdpi/video.png
app_theme_green/src/main/res/drawable-xxxhdpi/video_voice_green.png
app_theme_green/src/main/res/drawable-xxxhdpi/video_voice_red.png
app_theme_green/src/main/res/drawable-xxxhdpi/voice.png
app_theme_green/src/main/res/drawable/animation_red_green.xml
app_theme_green/src/main/res/drawable/btn_edit_selector.xml
app_theme_green/src/main/res/drawable/btn_minitpay_selector.xml
app_theme_green/src/main/res/drawable/btn_pressed_shape.xml
app_theme_green/src/main/res/drawable/btn_unpressed_shape.xml
app_theme_green/src/main/res/drawable/etit_bg.xml
app_theme_green/src/main/res/drawable/icon_check.png
app_theme_green/src/main/res/drawable/icon_uncheck.png
app_theme_green/src/main/res/drawable/launcher.png
app_theme_green/src/main/res/drawable/minit_logo.png
app_theme_green/src/main/res/drawable/ring_phone_200dp.xml
app_theme_green/src/main/res/drawable/ringing_phone_web.png
app_theme_green/src/main/res/drawable/tabs_contacts_selector.xml
app_theme_green/src/main/res/drawable/tabs_favorites_selector.xml
app_theme_green/src/main/res/drawable/tabs_messages_selector.xml
app_theme_green/src/main/res/drawable/tabs_recent_selector.xml
app_theme_green/src/main/res/drawable/theme_blue.png
app_theme_green/src/main/res/drawable/theme_green.png
app_theme_green/src/main/res/drawable/theme_orange.png
app_theme_green/src/main/res/drawable/theme_red.png
app_theme_green/src/main/res/drawable/video_top_gradient.xml
app_theme_green/src/main/res/layout
app_theme_green/src/main/res/layout/activity_account_recovery.xml
app_theme_green/src/main/res/layout/activity_balance.xml
app_theme_green/src/main/res/layout/activity_change_pwd.xml
app_theme_green/src/main/res/layout/activity_chat.xml
app_theme_green/src/main/res/layout/activity_contact_detail.xml
app_theme_green/src/main/res/layout/activity_edit_msg.xml
app_theme_green/src/main/res/layout/activity_eula.xml
app_theme_green/src/main/res/layout/activity_image_view.xml
app_theme_green/src/main/res/layout/activity_login.xml
app_theme_green/src/main/res/layout/activity_login_minit_pay.xml
app_theme_green/src/main/res/layout/activity_login_out.xml
app_theme_green/src/main/res/layout/activity_main.xml
app_theme_green/src/main/res/layout/activity_map.xml
app_theme_green/src/main/res/layout/activity_minit_pay.xml
app_theme_green/src/main/res/layout/activity_pay.xml
app_theme_green/src/main/res/layout/activity_phone_retrieval.xml
app_theme_green/src/main/res/layout/activity_profile.xml
app_theme_green/src/main/res/layout/activity_register.xml
app_theme_green/src/main/res/layout/activity_select_contact.xml
app_theme_green/src/main/res/layout/activity_show_code.xml
app_theme_green/src/main/res/layout/activity_sound_recorder.xml
app_theme_green/src/main/res/layout/activity_theme.xml
app_theme_green/src/main/res/layout/activity_unknown_contact.xml
app_theme_green/src/main/res/layout/activity_version.xml
app_theme_green/src/main/res/layout/activity_view_location.xml
app_theme_green/src/main/res/layout/dialog_favorites_choice.xml
app_theme_green/src/main/res/layout/dialog_input.xml
app_theme_green/src/main/res/layout/dialog_new_call_interrupt.xml
app_theme_green/src/main/res/layout/dialog_select_call.xml
app_theme_green/src/main/res/layout/fragment_answer.xml
app_theme_green/src/main/res/layout/fragment_call.xml
app_theme_green/src/main/res/layout/fragment_change_pwd.xml
app_theme_green/src/main/res/layout/fragment_change_pwd_fail.xml
app_theme_green/src/main/res/layout/fragment_change_pwd_success.xml
app_theme_green/src/main/res/layout/fragment_connecting.xml
app_theme_green/src/main/res/layout/fragment_main.xml
app_theme_green/src/main/res/layout/fragment_phone_entry.xml
app_theme_green/src/main/res/layout/fragment_pin_entry.xml
app_theme_green/src/main/res/layout/fragment_recovery_failed.xml
app_theme_green/src/main/res/layout/fragment_recovery_method.xml
app_theme_green/src/main/res/layout/fragment_recovery_success.xml
app_theme_green/src/main/res/layout/fragment_text.xml
app_theme_green/src/main/res/layout/fragment_video.xml
app_theme_green/src/main/res/layout/fragment_voice.xml
app_theme_green/src/main/res/layout/item_chat_msg_container.xml
app_theme_green/src/main/res/layout/item_chat_participant.xml
app_theme_green/src/main/res/layout/item_contact.xml
app_theme_green/src/main/res/layout/item_contact_detail.xml
app_theme_green/src/main/res/layout/item_country_spinner.xml
app_theme_green/src/main/res/layout/item_favorite.xml
app_theme_green/src/main/res/layout/item_log_meeting.xml
app_theme_green/src/main/res/layout/item_log_message.xml
app_theme_green/src/main/res/layout/item_message_location.xml
app_theme_green/src/main/res/layout/item_message_received_text.xml
app_theme_green/src/main/res/layout/item_message_sent_failed_text.xml
app_theme_green/src/main/res/layout/item_message_sent_text.xml
app_theme_green/src/main/res/layout/item_pending_media.xml
app_theme_green/src/main/res/layout/item_startchat.xml
app_theme_green/src/main/res/layout/item_tab.xml
app_theme_green/src/main/res/layout/item_theme.xml
app_theme_green/src/main/res/layout/simple_recycler_list.xml
app_theme_green/src/main/res/menu
app_theme_green/src/main/res/menu/menu_attach.xml
app_theme_green/src/main/res/menu/menu_attach_for_video.xml
app_theme_green/src/main/res/menu/menu_contact_details.xml
app_theme_green/src/main/res/menu/menu_contacts.xml
app_theme_green/src/main/res/menu/menu_fav.xml
app_theme_green/src/main/res/menu/menu_image_view.xml
app_theme_green/src/main/res/menu/menu_map.xml
app_theme_green/src/main/res/menu/menu_msg.xml
app_theme_green/src/main/res/menu/menu_msg_copy.xml
app_theme_green/src/main/res/menu/menu_msg_delete.xml
app_theme_green/src/main/res/menu/menu_msg_edit.xml
app_theme_green/src/main/res/menu/menu_msg_failed.xml
app_theme_green/src/main/res/menu/menu_msg_save.xml
app_theme_green/src/main/res/menu/menu_recent.xml
app_theme_green/src/main/res/menu/menu_select_contacts.xml
app_theme_green/src/main/res/menu/menu_start.xml
app_theme_green/src/main/res/menu/menu_text_chat_group.xml
app_theme_green/src/main/res/menu/menu_text_chat_single.xml
app_theme_green/src/main/res/mipmap-hdpi
app_theme_green/src/main/res/mipmap-hdpi/ic_launcher.png
app_theme_green/src/main/res/mipmap-hdpi/ic_launcher_round.png
app_theme_green/src/main/res/mipmap-hdpi/ic_notifications.png
app_theme_green/src/main/res/mipmap-mdpi
app_theme_green/src/main/res/mipmap-mdpi/ic_launcher.png
app_theme_green/src/main/res/mipmap-mdpi/ic_launcher_round.png
app_theme_green/src/main/res/mipmap-mdpi/ic_notifications.png
app_theme_green/src/main/res/mipmap-xhdpi
app_theme_green/src/main/res/mipmap-xhdpi/ic_launcher.png
app_theme_green/src/main/res/mipmap-xhdpi/ic_launcher_round.png
app_theme_green/src/main/res/mipmap-xhdpi/ic_notifications.png
app_theme_green/src/main/res/mipmap-xxhdpi
app_theme_green/src/main/res/mipmap-xxhdpi/ic_launcher.png
app_theme_green/src/main/res/mipmap-xxhdpi/ic_launcher_round.png
app_theme_green/src/main/res/mipmap-xxhdpi/ic_notifications.png
app_theme_green/src/main/res/mipmap-xxxhdpi
app_theme_green/src/main/res/mipmap-xxxhdpi/ic_launcher.png
app_theme_green/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png
app_theme_green/src/main/res/values
app_theme_green/src/main/res/values-v21
app_theme_green/src/main/res/values-v21/styles.xml
app_theme_green/src/main/res/values-w820dp
app_theme_green/src/main/res/values-w820dp/dimens.xml
app_theme_green/src/main/res/values/colors.xml
app_theme_green/src/main/res/values/dimens.xml
app_theme_green/src/main/res/values/strings.xml
app_theme_green/src/main/res/values/styles.xml
app_theme_green/src/main/res/xml
app_theme_green/src/main/res/xml/provider_file_paths.xml
app_theme_green/src/main/res/xml/searchable.xml
app_theme_green/src/test
app_theme_green/src/test/java
app_theme_green/src/test/java/com
app_theme_green/src/test/java/com/hsdi
app_theme_green/src/test/java/com/hsdi/app_theme_green
app_theme_green/src/test/java/com/hsdi/app_theme_green/ExampleUnitTest.java
theme/src/main/java/com/hsdi/theme/handler/BtnColorStateListThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/TxtColorStateListThemeHandler.java



Anthor�� Yi
Date��2016.08.24
Message ��modify mainActivity page skin
Modify files��
app/src/main/assets/THEME/GREEN
app/src/main/java/com/hsdi/NetMe/ui/chat/ChatActivity.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/contact_detail/ContactDetailActivity.java
app/src/main/java/com/hsdi/NetMe/ui/contact_detail/DetailHolder.java
app/src/main/res/layout/activity_contact_detail.xml
app/src/main/res/layout/fragment_text.xml
app/src/main/res/layout/item_chat_participant.xml
app_theme_green/src/main/res/drawable-hdpi/add.png
app_theme_green/src/main/res/drawable-hdpi/attach.png
app_theme_green/src/main/res/drawable-hdpi/back.png
app_theme_green/src/main/res/drawable-hdpi/message.png
app_theme_green/src/main/res/drawable-hdpi/phone.png
app_theme_green/src/main/res/drawable-hdpi/send_btn.png
app_theme_green/src/main/res/drawable-hdpi/video.png
app_theme_green/src/main/res/drawable-ldpi/message.png
app_theme_green/src/main/res/drawable-mdpi/add.png
app_theme_green/src/main/res/drawable-mdpi/attach.png
app_theme_green/src/main/res/drawable-mdpi/back.png
app_theme_green/src/main/res/drawable-mdpi/message.png
app_theme_green/src/main/res/drawable-mdpi/phone.png
app_theme_green/src/main/res/drawable-mdpi/send_btn.png
app_theme_green/src/main/res/drawable-mdpi/video.png
app_theme_green/src/main/res/drawable-xhdpi/add.png
app_theme_green/src/main/res/drawable-xhdpi/attach.png
app_theme_green/src/main/res/drawable-xhdpi/back.png
app_theme_green/src/main/res/drawable-xhdpi/close_icon.png
app_theme_green/src/main/res/drawable-xhdpi/message.png
app_theme_green/src/main/res/drawable-xhdpi/phone.png
app_theme_green/src/main/res/drawable-xhdpi/send_btn.png
app_theme_green/src/main/res/drawable-xhdpi/video.png
app_theme_green/src/main/res/drawable-xxhdpi/add.png
app_theme_green/src/main/res/drawable-xxhdpi/attach.png
app_theme_green/src/main/res/drawable-xxhdpi/back.png
app_theme_green/src/main/res/drawable-xxhdpi/message.png
app_theme_green/src/main/res/drawable-xxhdpi/phone.png
app_theme_green/src/main/res/drawable-xxhdpi/send_btn.png
app_theme_green/src/main/res/drawable-xxhdpi/video.png
app_theme_green/src/main/res/drawable-xxxhdpi/add.png
app_theme_green/src/main/res/drawable-xxxhdpi/back.png
app_theme_green/src/main/res/drawable-xxxhdpi/message.png
app_theme_green/src/main/res/drawable-xxxhdpi/phone.png
app_theme_green/src/main/res/drawable-xxxhdpi/send_btn.png
app_theme_green/src/main/res/drawable-xxxhdpi/video.png
app_theme_green/src/main/res/layout/activity_contact_detail.xml
app_theme_green/src/main/res/layout/fragment_text.xml
app_theme_green/src/main/res/layout/item_chat_participant.xml
app_theme_green/src/main/res/values/colors.xml

Add files:
app_theme_green/src/main/res/drawable-hdpi/close_icon.png
app_theme_green/src/main/res/drawable-mdpi/close_icon.png
app_theme_green/src/main/res/drawable-xxhdpi/close_icon.png
app_theme_green/src/main/res/drawable-xxxhdpi/close_icon.png



Anthor�� Yi
Date��2016.08.28
Message ��modify page skin
Modify files��
app/build.gradle

app/src/main/assets/THEME/GREEN
app/src/main/java/com/hsdi/NetMe/BaseActivity.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/helper_activities/SoundRecorderActivity.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/text_helpers/TextMsgHolder.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/contact_selection/SelectContactHolder.java
app/src/main/java/com/hsdi/NetMe/ui/contact_selection/SelectContactsActivity.java
app/src/main/java/com/hsdi/NetMe/ui/main/MainActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/LoginActivity.java
app/src/main/res/drawable-xxhdpi/chat_bubble_received.9.png
app/src/main/res/drawable-xxhdpi/chat_bubble_sent.9.png
app/src/main/res/layout/activity_chat.xml
app/src/main/res/layout/activity_login.xml
app/src/main/res/layout/activity_main.xml
app/src/main/res/layout/activity_sound_recorder.xml
app/src/main/res/layout/fragment_text.xml
app/src/main/res/layout/fragment_video.xml
app/src/main/res/layout/item_chat_msg_container.xml
app/src/main/res/layout/item_message_received_text.xml
app/src/main/res/layout/item_message_sent_failed_text.xml
app/src/main/res/layout/item_message_sent_text.xml
app/src/main/res/layout/item_startchat.xml
app/src/main/res/menu/menu_msg.xml
app/src/main/res/values/colors.xml
app/src/main/res/values/styles.xml
app_theme_green/src/main/res/drawable-hdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-hdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-hdpi/record.png
app_theme_green/src/main/res/drawable-hdpi/video.png
app_theme_green/src/main/res/drawable-mdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-mdpi/record.png
app_theme_green/src/main/res/drawable-mdpi/video.png
app_theme_green/src/main/res/drawable-xhdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-xhdpi/pause_btn.png
app_theme_green/src/main/res/drawable-xhdpi/play_btn.png
app_theme_green/src/main/res/drawable-xhdpi/record.png
app_theme_green/src/main/res/drawable-xhdpi/send.png
app_theme_green/src/main/res/drawable-xhdpi/video.png
app_theme_green/src/main/res/drawable-xxhdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-xxhdpi/chat_bubble_sent.9.png
app_theme_green/src/main/res/drawable-xxhdpi/close_chat_button.png
app_theme_green/src/main/res/drawable-xxhdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-xxhdpi/record.png
app_theme_green/src/main/res/drawable-xxhdpi/video.png
app_theme_green/src/main/res/drawable-xxxhdpi/drawer_bg.png
app_theme_green/src/main/res/drawable-xxxhdpi/record.png
app_theme_green/src/main/res/drawable-xxxhdpi/video.png
app_theme_green/src/main/res/layout/fragment_text.xml
app_theme_green/src/main/res/values/colors.xml
app_theme_green/src/main/res/values/styles.xml
theme/src/main/java/com/hsdi/theme/basic/ThemeLayoutInflaterFactory.java
app/src/main/res/drawable-hdpi/chat_bubble_sent.9.png
app/src/main/res/drawable-hdpi/checkbox_select.png
app/src/main/res/drawable-hdpi/checkbox_unselect.png
app/src/main/res/drawable-hdpi/empty_avatar_user.png
app/src/main/res/drawable-ldpi/chat_bubble_sent.9.png
app/src/main/res/drawable-ldpi/empty_avatar_user.png
app/src/main/res/drawable-mdpi/chat_bubble_sent.9.png
app/src/main/res/drawable-mdpi/checkbox_select.png
app/src/main/res/drawable-mdpi/checkbox_unselect.png
app/src/main/res/drawable-mdpi/empty_avatar_user.png
app/src/main/res/drawable-xhdpi/chat_bubble_sent.9.png
app/src/main/res/drawable-xhdpi/checkbox_select.png
app/src/main/res/drawable-xhdpi/checkbox_unselect.png
app/src/main/res/drawable-xhdpi/empty_avatar_user.png
app/src/main/res/drawable-xxhdpi/checkbox_select.png
app/src/main/res/drawable-xxhdpi/checkbox_unselect.png
app/src/main/res/drawable-xxhdpi/empty_avatar_user.png
app/src/main/res/drawable-xxxhdpi/chat_bubble_sent.9.png
app/src/main/res/drawable-xxxhdpi/checkbox_select.png
app/src/main/res/drawable-xxxhdpi/checkbox_unselect.png
app/src/main/res/drawable-xxxhdpi/empty_avatar_user.png
app/src/main/res/drawable/checkbox_selector.xml
app_theme_green/src/main/res/drawable-hdpi/chat_bubble_sent.9.png
app_theme_green/src/main/res/drawable-hdpi/checkbox_select.png
app_theme_green/src/main/res/drawable-hdpi/checkbox_unselect.png
app_theme_green/src/main/res/drawable-hdpi/close_chat_button.png
app_theme_green/src/main/res/drawable-hdpi/empty_avatar_user.png
app_theme_green/src/main/res/drawable-hdpi/pause_btn.png
app_theme_green/src/main/res/drawable-hdpi/play_btn.png
app_theme_green/src/main/res/drawable-hdpi/send.png
app_theme_green/src/main/res/drawable-mdpi/chat_bubble_sent.9.png
app_theme_green/src/main/res/drawable-mdpi/checkbox_select.png
app_theme_green/src/main/res/drawable-mdpi/checkbox_unselect.png
app_theme_green/src/main/res/drawable-mdpi/close_chat_button.png
app_theme_green/src/main/res/drawable-mdpi/empty_avatar_user.png
app_theme_green/src/main/res/drawable-mdpi/pause_btn.png
app_theme_green/src/main/res/drawable-mdpi/play_btn.png
app_theme_green/src/main/res/drawable-xhdpi/chat_bubble_sent.9.png
app_theme_green/src/main/res/drawable-xhdpi/checkbox_select.png
app_theme_green/src/main/res/drawable-xhdpi/checkbox_unselect.png
app_theme_green/src/main/res/drawable-xhdpi/close_chat_button.png
app_theme_green/src/main/res/drawable-xhdpi/empty_avatar_user.png
app_theme_green/src/main/res/drawable-xxhdpi/checkbox_select.png
app_theme_green/src/main/res/drawable-xxhdpi/checkbox_unselect.png
app_theme_green/src/main/res/drawable-xxhdpi/empty_avatar_user.png
app_theme_green/src/main/res/drawable-xxhdpi/pause_btn.png
app_theme_green/src/main/res/drawable-xxhdpi/play_btn.png
app_theme_green/src/main/res/drawable-xxhdpi/send.png
app_theme_green/src/main/res/drawable-xxxhdpi/chat_bubble_sent.9.png
app_theme_green/src/main/res/drawable-xxxhdpi/checkbox_select.png
app_theme_green/src/main/res/drawable-xxxhdpi/checkbox_unselect.png
app_theme_green/src/main/res/drawable-xxxhdpi/close_chat_button.png
app_theme_green/src/main/res/drawable-xxxhdpi/empty_avatar_user.png
app_theme_green/src/main/res/drawable-xxxhdpi/pause_btn.png
app_theme_green/src/main/res/drawable-xxxhdpi/play_btn.png
app_theme_green/src/main/res/drawable-xxxhdpi/send.png
app_theme_green/src/main/res/drawable/checkbox_selector.xml
theme/src/main/java/com/hsdi/theme/basic/BaseFragment.java


Delete files:
app/src/main/res/drawable-hdpi/chat_bubble_received.9.png
app/src/main/res/drawable-ldpi/chat_bubble_received.9.png
app/src/main/res/drawable-mdpi/chat_bubble_received.9.png
app/src/main/res/drawable-xhdpi/chat_bubble_received.9.png
app/src/main/res/drawable-xxxhdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-ldpi
app_theme_green/src/main/res/drawable-ldpi/add.png
app_theme_green/src/main/res/drawable-ldpi/attach.png
app_theme_green/src/main/res/drawable-ldpi/back.png
app_theme_green/src/main/res/drawable-ldpi/camera.png
app_theme_green/src/main/res/drawable-ldpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-ldpi/drawer_bg.png
app_theme_green/src/main/res/drawable-ldpi/empty_avatar.png
app_theme_green/src/main/res/drawable-ldpi/favorite.png
app_theme_green/src/main/res/drawable-ldpi/group_avatar.png
app_theme_green/src/main/res/drawable-ldpi/mail.png
app_theme_green/src/main/res/drawable-ldpi/message.png
app_theme_green/src/main/res/drawable-ldpi/missed_call.png
app_theme_green/src/main/res/drawable-ldpi/password_icon.png
app_theme_green/src/main/res/drawable-ldpi/phone.png
app_theme_green/src/main/res/drawable-ldpi/record.png
app_theme_green/src/main/res/drawable-ldpi/search.png
app_theme_green/src/main/res/drawable-ldpi/send_btn.png
app_theme_green/src/main/res/drawable-ldpi/sync.png
app_theme_green/src/main/res/drawable-ldpi/tabs_contacts.png
app_theme_green/src/main/res/drawable-ldpi/tabs_contacts_selected.png
app_theme_green/src/main/res/drawable-ldpi/tabs_favorites.png
app_theme_green/src/main/res/drawable-ldpi/tabs_favorites_selected.png
app_theme_green/src/main/res/drawable-ldpi/tabs_messages.png
app_theme_green/src/main/res/drawable-ldpi/tabs_messages_selected.png
app_theme_green/src/main/res/drawable-ldpi/tabs_recent.png
app_theme_green/src/main/res/drawable-ldpi/tabs_recent_selected.png
app_theme_green/src/main/res/drawable-ldpi/username_icon.png
app_theme_green/src/main/res/drawable-ldpi/video.png
app_theme_green/src/main/res/drawable-ldpi/video_voice_green.png
app_theme_green/src/main/res/drawable-ldpi/video_voice_red.png
app_theme_green/src/main/res/drawable-ldpi/voice.png
app_theme_green/src/main/res/drawable-mdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-xhdpi/chat_bubble_received.9.png
app_theme_green/src/main/res/drawable-xxxhdpi/chat_bubble_received.9.png





Anthor�� Yi
Date��2016.08.29
Message ��modify page skin
Modify files��
app/src/main/assets/THEME/GREEN
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/contacts/ContactFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/favorites/FavoritesFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/MainActivity.java
app/src/main/java/com/hsdi/NetMe/ui/main/message_logs/MessageLogFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/recent_logs/RecentFragment.java
app/src/main/res/layout/activity_main.xml
app/src/main/res/layout/fragment_text.xml
app/src/main/res/values/colors.xml
app/src/main/res/values/styles.xml
app_theme_green/src/main/res/values/colors.xml
app_theme_green/src/main/res/values/styles.xml
theme/src/main/java/com/hsdi/theme/basic/BaseThemeActivity.java
theme/src/main/java/com/hsdi/theme/basic/ThemeHandlerFactory.java
theme/src/main/java/com/hsdi/theme/basic/ThemeLayoutInflaterFactory.java
theme/src/main/java/com/hsdi/theme/handler/DrawableThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ThemeHandler.java

Add files
app/src/main/res/drawable/switch_selector.xml
theme/src/main/java/com/hsdi/theme/handler/MenuItemIconThemeHandler.java



Anthor�� Yi
Date��2016.08.30
Message ��modify page skin
Modify files��
app/src/main/assets/THEME/GREEN
app/src/main/java/com/hsdi/NetMe/BaseActivity.java
app/src/main/java/com/hsdi/NetMe/database/DatabaseHelper.java
app/src/main/java/com/hsdi/NetMe/ui/chat/ChatActivity.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/helper_activities/EditMessageActivity.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/helper_activities/ImageViewActivity.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/helper_activities/MapActivity.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/helper_activities/ViewLocationActivity.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/text_helpers/TextMsgHolder.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/contact_detail/ContactDetailActivity.java
app/src/main/java/com/hsdi/NetMe/ui/contact_selection/SelectContactsActivity.java
app/src/main/java/com/hsdi/NetMe/ui/EulaActivity.java
app/src/main/java/com/hsdi/NetMe/ui/main/contacts/ContactFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/MainActivity.java
app/src/main/java/com/hsdi/NetMe/ui/ProfileActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/AccountRecoveryActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/ChangePwdActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/phone_verification/PhoneEntryFragment.java
app/src/main/java/com/hsdi/NetMe/ui/startup/phone_verification/PinEntryFragment.java
app/src/main/java/com/hsdi/NetMe/ui/startup/PhoneRetrievalActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/RegisterActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/ThemeActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/VersionActivity.java
app/src/main/res/drawable-xxhdpi/chat_bubble_received.9.png
app/src/main/res/drawable/switch_selector.xml
app/src/main/res/layout/activity_main.xml
app/src/main/res/layout/fragment_text.xml
app/src/main/res/layout/item_theme.xml
app/src/main/res/values/colors.xml
app/src/main/res/values/styles.xml
app_theme_green/src/main/res/values/colors.xml
theme/src/main/java/com/hsdi/theme/basic/BaseThemeActivity.java
theme/src/main/java/com/hsdi/theme/basic/ThemeHandlerFactory.java
theme/src/main/java/com/hsdi/theme/basic/ThemeLayoutInflaterFactory.java
theme/src/main/java/com/hsdi/theme/handler/BackgroundColorThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/BtnColorStateListThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ColorThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ThemeHandler.java

Add files��
app_theme_green/src/main/res/drawable/switch_selector.xml
theme/src/main/java/com/hsdi/theme/handler/StatusBarThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ToolbarBackDrawableThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ToolbarOverflowDrawableThemeHandler.java

Anthor�� Yi
Date��2016.09.01
Message ��modify page skin
Modify files��
app/build.gradle
app/src/main/AndroidManifest.xml
app/src/main/assets/THEME/GREEN
app/src/main/java/com/hsdi/NetMe/BaseActivity.java
app/src/main/java/com/hsdi/NetMe/NetMeApp.java
app/src/main/java/com/hsdi/NetMe/ui/chat/text/TextFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/favorites/FavoritesFragment.java
app/src/main/java/com/hsdi/NetMe/ui/main/MainActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/change_password/ChangePwdFragment.java
app/src/main/java/com/hsdi/NetMe/ui/startup/change_password/ChangePwdSuccessFragment.java
app/src/main/java/com/hsdi/NetMe/ui/startup/phone_verification/PhoneEntryFragment.java
app/src/main/java/com/hsdi/NetMe/ui/startup/phone_verification/PinEntryFragment.java
app/src/main/java/com/hsdi/NetMe/ui/startup/recovery/RecoveryFailedFragment.java
app/src/main/java/com/hsdi/NetMe/ui/startup/recovery/RecoveryMethodFragment.java
app/src/main/java/com/hsdi/NetMe/ui/startup/recovery/RecoverySuccessFragment.java
app/src/main/java/com/hsdi/NetMe/ui/startup/ThemeActivity.java
app/src/main/java/com/hsdi/NetMe/ui/startup/VersionActivity.java
app/src/main/res/layout/activity_main.xml
app/src/main/res/layout/activity_version.xml
app/src/main/res/layout/dialog_favorites_choice.xml
app/src/main/res/layout/fragment_change_pwd.xml
app/src/main/res/layout/fragment_phone_entry.xml
app/src/main/res/layout/fragment_pin_entry.xml
app/src/main/res/layout/fragment_text.xml
app/src/main/res/values/colors.xml
app/src/main/res/values/dimens.xml
app/src/main/res/values/strings.xml
app/src/main/res/values/styles.xml
app_theme_green/src/main/res/drawable-xhdpi/fav_chat.png
app_theme_green/src/main/res/drawable-xhdpi/fav_video.png
app_theme_green/src/main/res/values/colors.xml
app_theme_green/src/main/res/values/styles.xml
theme/src/main/java/com/hsdi/theme/basic/ThemeHandlerFactory.java
theme/src/main/java/com/hsdi/theme/basic/ThemeLayoutInflaterFactory.java
theme/src/main/java/com/hsdi/theme/handler/BackgroundColorThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/DrawableLeftThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/MenuItemIconThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ThemeHandler.java

Add files:
app/src/main/java/com/hsdi/NetMe/util/PopMenu.java
app/src/main/res/layout/popmenu.xml
app/src/main/res/layout/popmenu_item.xml
app/src/main/res/mipmap-hdpi/ic_launcher2.png
app/src/main/res/mipmap-mdpi/ic_launcher2.png
app/src/main/res/mipmap-xhdpi/ic_launcher2.png
app/src/main/res/mipmap-xxhdpi/ic_launcher2.png
app/src/main/res/mipmap-xxxhdpi/ic_launcher2.png
app_theme_green/src/main/res/drawable-hdpi/fav_chat.png
app_theme_green/src/main/res/drawable/switch_selector.xml
theme/src/main/java/com/hsdi/theme/basic/BaseThemeFragment.java
theme/src/main/java/com/hsdi/theme/basic/StyleManager.java
theme/src/main/java/com/hsdi/theme/handler/StatusBarThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/StyleThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ToolbarBackDrawableThemeHandler.java
theme/src/main/java/com/hsdi/theme/handler/ToolbarOverflowDrawableThemeHandler.java





