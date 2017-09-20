package com.hsdi.NetMe.ui.chat.text;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.maps.model.LatLng;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.ChatTrackerManager;
import com.hsdi.NetMe.models.Chat;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.ManagedMessage;
import com.hsdi.NetMe.models.Media;
import com.hsdi.NetMe.models.Participant;
import com.hsdi.NetMe.models.Phone;
import com.hsdi.NetMe.models.events.EventMessageLog;
import com.hsdi.NetMe.models.events.EventScrollMsgList;
import com.hsdi.NetMe.models.response_models.BaseResponse;
import com.hsdi.NetMe.network.GcmBroadcastReceiver;
import com.hsdi.NetMe.network.RestServiceGen;
import com.hsdi.NetMe.ui.chat.ChatInterface;
import com.hsdi.NetMe.ui.chat.text.helper_activities.EditMessageActivity;
import com.hsdi.NetMe.ui.chat.text.helper_activities.ImageViewActivity;
import com.hsdi.NetMe.ui.chat.text.helper_activities.MapActivity;
import com.hsdi.NetMe.ui.chat.text.helper_activities.SoundRecorderActivity;
import com.hsdi.NetMe.ui.chat.text.participants_list.ParticipantAdapter;
import com.hsdi.NetMe.ui.chat.text.text_helpers.PendingMediaAdapter;
import com.hsdi.NetMe.ui.chat.text.text_helpers.TextAdapter;
import com.hsdi.NetMe.ui.chat.util.MessageManager;
import com.hsdi.NetMe.ui.chat.util.MsgManagerCallback;
import com.hsdi.NetMe.ui.chat.util.OnMessageClickListener;
import com.hsdi.NetMe.ui.contact_detail.ContactDetailActivity;
import com.hsdi.NetMe.util.Constants;
import com.hsdi.NetMe.util.DeviceUtils;
import com.hsdi.NetMe.util.DialogUtils;
import com.hsdi.NetMe.util.PopMenu;
import com.hsdi.NetMe.util.PreferenceManager;
import com.hsdi.theme.basic.BaseThemeActivity;
import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.background_color;
import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.toolbar_nav_icon;
import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.toolbar_overflow_icon;

public class TextFragment extends Fragment implements MsgManagerCallback,
        OnMessageClickListener, Toolbar.OnMenuItemClickListener, IUpdateInputtingListener {
    private final static String TAG = "textFrag";

    public static final String EXTRA_INVITED = "invited_users";
    public static final String EXTRA_CHAT_ID = "chat_id";
    public static final String EXTRA_VIDEO_MODE = "videoModeOn";

    // Some constants
    private final static int REQUEST_CODE_CHOOSE_FILE = 1;
    private final static int REQUEST_CODE_CHOOSE_LOCATION = 3;
    private final static int REQUEST_CODE_TAKE_PICTURE = 4;

    private final static int REQUEST_CODE_RECORD_VOICE = 5;
    private final static int REQUEST_CODE_PERMISSION = 44;

    private final static long NEW_CHAT_ID = -1;

    /**
     * Maximum size of the attachment to be sent (in bytes)
     */
    private final static long MAX_ATTACHMENT_SIZE = 50 * 1000 * 1000;  // 50 Mb
    private final static int MAX_VOICE_RECORDING_LENGTH = 10 * 60 * 1000;   // 10 min.
    @Bind(R.id.chat_typing)
    TextView chatTyping;

    @Bind(R.id.chat_entry_fields)
    LinearLayout chatEntryFields;
    @Bind(R.id.chat_rl_container)
    RelativeLayout chatRlContainer;
    @Bind(R.id.chat_close_drawer)
    ImageView chatCloseDrawer;
    @Bind(R.id.chat_toolbar_typing)
    ImageView chatToolbarTyping;
    @Bind(R.id.chat_send_btn)
    ImageView chatSendBtn;
    @Bind(R.id.ll_chat_send)
    LinearLayout llChatSend;

    // attachment stuff
    private File voiceFile;
    private File photoFile;

    private View mPopWindowView;
    private PopupWindow mPopWindow;

    // Views
    @Bind(R.id.chat_toolbar)
    Toolbar toolbar;
    @Bind(R.id.chat_message_list)
    RecyclerView msgList;
    @Bind(R.id.chat_media_review)
    RecyclerView pendingMediaList;
    @Bind(R.id.chat_message_entry)
    EditText msgEntryField;
    @Bind(R.id.chat_toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.chat_attach_btn)
    View attachButton;
    @Bind(R.id.chat_loading_bar)
    View progressBar;
    @Bind(R.id.chat_drawer)
    DrawerLayout drawer;
    @Bind(R.id.chat_participants)
    RecyclerView participantList;

    private ChatInterface chatInterface;
    private LinearLayoutManager textLayoutManager;
    private static TextAdapter textAdapter;

    private LinearLayoutManager pendingMediaLayoutManager;
    private PendingMediaAdapter pendingMediaAdapter;

    private long chatId = NEW_CHAT_ID;
    private String invited = null;
    private MessageManager msgManager;
    private int unloadedMsgCount = 0;
    private boolean currentlyLoading = false;
    private boolean videoModeOn = false;
    private PreferenceManager manager;
    private boolean menuLoading = false;
    private View textView;


    private Timer timer = null;
    private String participantName = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i("yuyong_TextFragment", "onCreateView: ");
        textView = inflater.inflate(R.layout.fragment_text, container, false);
        ButterKnife.bind(this, textView);

       // ((BaseThemeActivity) getActivity()).applyTheme(toolbar, R.color.primary, ThemeLayoutInflaterFactory.ThemeTypeValue.background_color);
       // ((BaseThemeActivity) getActivity()).applyTheme(llChatSend, R.color.primary, ThemeLayoutInflaterFactory.ThemeTypeValue.background_color);

        ((BaseThemeActivity) getActivity()).applyTheme(attachButton, R.drawable.attach, ThemeLayoutInflaterFactory.ThemeTypeValue.src);
        ((BaseThemeActivity) getActivity()).applyTheme(chatSendBtn, R.drawable.send_btn, ThemeLayoutInflaterFactory.ThemeTypeValue.src);

        EventBus.getDefault().register(this);

        GcmBroadcastReceiver.setInputtingListener(this);
        manager = PreferenceManager.getInstance(getActivity());
        initializeMain();

        // check if to start the tutorial
        boolean tutorialShown = NetMeApp.getInstance().getPrefManager().chatTutorialShown();
        if (!tutorialShown && !videoModeOn) startMessageTutorial();

        NetMeApp.setCurrentChat(chatId);

        msgEntryField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("TAG", "beforeTextChanged--------------->" + start + "---" + after + "---" + count);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TAG", "onTextChanged--------------->" + start + "---" + before + "---" + count);
                // msgManager.sendTextOnly("$$");
                sendTypingType(chatId);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("TAG", "afterTextChanged--------------->");
            }
        });

        return textView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("yuyong_TextFragment", "onResume: ");
        NetMeApp.setChatVisible(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("yuyong_TextFragment", "onPause: ");
        NetMeApp.setChatVisible(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("yuyong_TextFragment", "onAttach: Context");
        try {
            chatInterface = (ChatInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ChatInterface");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("yuyong_TextFragment", "onAttach: Activity");
        try {
            chatInterface = (ChatInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ChatInterface");
        }
    }

    @Override
    public void onDetach() {
        Log.i("yuyong_TextFragment", "onDetach: ");
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().post(new EventMessageLog());

        textAdapter = null;
        textLayoutManager = null;
        pendingMediaAdapter = null;
        pendingMediaLayoutManager = null;

        super.onDetach();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_text_video:
                if (chatInterface != null && invited != null) {
                    // chatInterface.switchToVideo(invited, chatId);
                    // singleChoiceDialog(getActivity()).show();
                    createChoiceDialog(getActivity());
                }
                return true;

            case R.id.menu_text_chat_name:
                showEditChatTitle();
                return true;

            case R.id.menu_text_chat_participants:
                drawer.openDrawer(GravityCompat.END);
                return true;

            default:
                return false;
        }
    }

    private String[] items = {"video", "voice"};

    public AlertDialog singleChoiceDialog(final Context context) {
        return new AlertDialog.Builder(context)
                .setTitle("Please select video or voice")
                .setIcon(R.drawable.video_voice)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context, items[which], Toast.LENGTH_SHORT).show();
                        if (items[0].equals(items[which])) {
                            chatInterface.switchToVideo(invited, chatId, Constants.MEETING_VIDEO);
                        } else {
                            chatInterface.switchToVideo(invited, chatId, Constants.MEETING_VOICE);
                        }
                        dialog.dismiss();
                    }
                })
                .create();
    }

    public void createChoiceDialog(final Context context) {
        final View dialogView = View.inflate(context, R.layout.dialog_select_call, null);
        TextView tvVoice = (TextView) dialogView.findViewById(R.id.tv_voice);
        TextView tvVideo = (TextView) dialogView.findViewById(R.id.tv_video);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.show();

        tvVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatInterface.switchToVideo(invited, chatId, Constants.MEETING_VOICE);
                dialog.dismiss();
            }
        });

        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatInterface.switchToVideo(invited, chatId, Constants.MEETING_VIDEO);
                dialog.dismiss();
            }
        });
    }
    private PopMenu popMenu;
    private void initPopmenu() {
        // 初始化弹出菜单
        popMenu = new PopMenu(getActivity());
        popMenu.addItem(getString(R.string.voice));
        popMenu.addItem(getString(R.string.camera));
        popMenu.addItem(getString(R.string.file));
        popMenu.addItem(getString(R.string.location));

        // 弹出菜单监听器
        AdapterView.OnItemClickListener popmenuItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //部分提交
                        popMenu.dismiss();
                        Toast.makeText(getActivity(), "部分提交", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //结束安装
                        popMenu.dismiss();
                        Toast.makeText(getActivity(), "结束安装", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //从本地删除
                        popMenu.dismiss();
                        Toast.makeText(getActivity(), "从本地删除", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //返回首页
                        popMenu.dismiss();
                        Toast.makeText(getActivity(), "返回首页", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        // 菜单项点击监听器
        popMenu.setOnItemClickListener(popmenuItemClickListener);
    }


    @SuppressWarnings("ResultOfMethodCallIgnored,ConstantConditions")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //everything is good so handle the result
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // send audio file just recorded
                case REQUEST_CODE_RECORD_VOICE:
//                    sendVoice();
                    if (voiceFile != null) pendingMediaAdapter.addVoiceFile(voiceFile);
                    break;


                // send picture just captured from the camera
                case REQUEST_CODE_TAKE_PICTURE:
//                    sendCameraImage();
                    if (photoFile != null) pendingMediaAdapter.addImageFile(photoFile);
                    break;


                // send file, check if image file
                case REQUEST_CODE_CHOOSE_FILE:
                    setPendingFile(data);
                    break;

                // send location
                case REQUEST_CODE_CHOOSE_LOCATION:
                    setPendingLocation(data);
                    break;

            }
        }

        //action failed or was canceled so delete any relevant data/files
        else if (resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_RECORD_VOICE:
                    voiceFile.delete();
                    break;

                case REQUEST_CODE_TAKE_PICTURE:
                    photoFile.delete();
                    break;
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollEvent(EventScrollMsgList event) {
        switch (event.getScrollType()) {
            // notify that the item was inserted in to the list
            case EventScrollMsgList.SCROLL_TO_BOTTOM:
                textLayoutManager.scrollToPosition(0);
                break;


            case EventScrollMsgList.ASK_TO_SCROLL: {
                int bottomVisPos = textLayoutManager.findFirstVisibleItemPosition();

                // if the last visible item is more then 2 items away ask the user if they want to scroll
                if (bottomVisPos > 2) {
                    Snackbar.make(msgList, R.string.chat_view_new_msg, Snackbar.LENGTH_LONG)
                            .setAction(
                                    R.string.ok,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            msgList.smoothScrollToPosition(0);
                                        }
                                    }
                            )
                            .show();
                }
                // if the last visible is within 2 items of the bottom, just scroll
                else msgList.smoothScrollToPosition(0);
                break;
            }
        }
    }

    /**
     * Returns the current chat id
     */
    public long getChatId() {
        return chatId;
    }

    /**
     * Shows a dialog which will allow the user to change the name of the chat title. the title
     * title is stored locally only.
     */
    private void showEditChatTitle() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialog_input, null, false);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        // get the current title
        final EditText editText = (EditText) promptView.findViewById(R.id.text_input_edit_text);
        String currentTitle = toolbarTitle.getText().toString();
        editText.setText(currentTitle);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(
                        R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get the new title and update it
                                String newTitle = editText.getText().toString();
                                ChatTrackerManager.storeChatName(getActivity(), chatId, newTitle);
                                toolbarTitle.setText(newTitle);
                            }
                        }
                )
                .setNegativeButton(
                        R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }
                );

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Starts a {@link TapTargetSequence}  tutorial for the message tab
     */
    private void startMessageTutorial() {
        TapTarget entryTarget = TapTarget
                .forView(msgEntryField, getString(R.string.chat_text_entry_title), getString(R.string.chat_text_entry_description))
                .outerCircleColor(R.color.primary_dark)
                .targetCircleColor(R.color.primary_accent)
                .transparentTarget(true)
                .tintTarget(false)
                .cancelable(true);

        TapTarget attachmentTarget = TapTarget
                .forView(attachButton, getString(R.string.chat_attachment_title), getString(R.string.chat_attachment_description))
                .outerCircleColor(R.color.primary_dark)
                .targetCircleColor(R.color.primary_accent)
                .transparentTarget(true)
                .tintTarget(false)
                .cancelable(true);

        TapTarget toolbarTarget = TapTarget
                .forView(toolbar, getString(R.string.chat_toolbar_title), getString(R.string.chat_toolbar_description))
                .outerCircleColor(R.color.primary_dark)
                .targetCircleColor(R.color.primary_accent)
                .transparentTarget(true)
                .tintTarget(false)
                .cancelable(true);

        new TapTargetSequence(getActivity())
                .targets(entryTarget, attachmentTarget, toolbarTarget)
                .continueOnCancel(true)
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        NetMeApp.getInstance().getPrefManager().setChatTutorialShown();
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        NetMeApp.getInstance().getPrefManager().setChatTutorialShown();
                    }
                })
                .start();
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Gets the information passed to the chat and initializes the chat activity views
     */
    private void initializeMain() {
        Log.i("yuyong_TextFragment", "initializeMain: ");
        Bundle extras = getArguments();
        List<Participant> participantList = new ArrayList<>();
        // if a chat id was provided, go ahead and load the chat
        if (extras.containsKey(EXTRA_CHAT_ID) && extras.getLong(EXTRA_CHAT_ID) > -1) {
            chatId = extras.getLong(EXTRA_CHAT_ID);

            String chatName = ChatTrackerManager.getChatName(getActivity(), chatId);
            if (!TextUtils.isEmpty(chatName)) toolbarTitle.setText(chatName);

            Log.d(TAG, "requested chat with id " + extras.getLong(EXTRA_CHAT_ID));
            progressBar.setVisibility(View.VISIBLE);
            try {
                msgManager = NetMeApp.getInstance().getMsgManagerForChat(chatId);
                intiView(participantList);
            } catch (Exception e) {
                Log.e(TAG, "Failed to get a good message manager", e);
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_unexpected, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            msgManager.loadChat(this, true);
        }
        // if invited user were provided, set up the chat for initial (text only, title)
        else if (extras.containsKey(EXTRA_INVITED)) {
            invited = extras.getString(EXTRA_INVITED);
            Log.d(TAG, "requested new chat with invited user's = " + invited);
            chatId = NEW_CHAT_ID;
            //  attachButton.setVisibility(View.GONE);

            // show group
            if (invited.contains(",")) toolbarTitle.setText(R.string.group_title);
                // show the single user
            else {
                // try to get the contact's name
                Contact contact = NetMeApp.getContactWith(invited);
                if (contact != null && TextUtils.isEmpty(contact.getName())) {
                    //  participantName = contact.getName();
                    toolbarTitle.setText(contact.getName());
                }
                // if failed to get name, just show number
                else toolbarTitle.setText(invited);
            }

            try {
                msgManager = NetMeApp.getInstance().getMsgManagerForChat(chatId);
                intiView(participantList);
            } catch (Exception e) {
                Log.e(TAG, "Failed to get a good message manager", e);
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_unexpected, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
        // if no chat id or invited users were provided, something went wrong
        else {
            onChatFailed(null);
            return;
        }

        // setup the back button to finish the activity
        toolbar.setTitle("");
        //  toolbar.setNavigationIcon(R.drawable.back);
        ((BaseThemeActivity) getActivity()).applyTheme(toolbar, R.drawable.back, toolbar_nav_icon);

        //设置overflow，需要最小版本是21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setOverflowIcon(getActivity().getDrawable(R.drawable.menu_overflow));
            ((BaseThemeActivity) getActivity()).applyTheme(toolbar, R.drawable.menu_overflow, toolbar_overflow_icon);
        }
        //设置toolbar的背景
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color));
        ((BaseThemeActivity) getActivity()).applyTheme(toolbar, R.color.toolbar_color, background_color);

        llChatSend.setBackgroundColor(getResources().getColor(R.color.toolbar_color));
        ((BaseThemeActivity) getActivity()).applyTheme(llChatSend, R.color.toolbar_color, background_color);

        // ((BaseThemeActivity) getActivity()).applyTheme(llChatSend,R.color.primary,background_color);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        // adjust for video mode if true, text mode if false
        setVideoMode(extras.getBoolean(EXTRA_VIDEO_MODE, false));
        msgList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // getting first visible item position and total item count
                int firstVisPos = textLayoutManager.findLastVisibleItemPosition();
                if (textAdapter == null) {
                    return;
                }
                int count = textAdapter.getItemCount();

                // if the first visible position with within 2 of the top (top = count -1),
                // there are more chats to load, and there wan't a load already requested.
                // Then request more chats
                if (firstVisPos > (count - 3) && unloadedMsgCount > 0 && !currentlyLoading) {
                    // setting the loading status so no more chat pages are loaded until this
                    // one is done loading
                    currentlyLoading = true;

                    // showing the message loading progressbar
                    progressBar.setVisibility(View.VISIBLE);

                    // request the nest set of messages be loaded
                    msgManager.loadChat(TextFragment.this, false);
                }
            }
        });

        // setting up the media review area above the text entry area
        pendingMediaLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        pendingMediaLayoutManager.setSmoothScrollbarEnabled(true);
        pendingMediaAdapter = new PendingMediaAdapter(getActivity());
        pendingMediaList.setLayoutManager(pendingMediaLayoutManager);
        pendingMediaList.setAdapter(pendingMediaAdapter);
        pendingMediaAdapter.notifyDataSetChanged();


    }

    private void intiView(List<Participant> participantList) {
        // setting up the chat message list view
        textLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        textLayoutManager.setSmoothScrollbarEnabled(true);
        textAdapter = new TextAdapter(getActivity(), msgManager, this, chatId, participantList);
        Log.i("yuyong_TextFragment", String.format("initializeMain: textAdapter = %d ,chatId = %d", textAdapter.hashCode(), chatId));
        msgList.setLayoutManager(textLayoutManager);
        msgList.setAdapter(textAdapter);
    }

    /**
     * Gets the information returned after loading the chat and initializes the participant drawer
     */
    private void initializeDrawer(Chat chat) {
        // if this is not a group chat, lock the drawer closed and don't load anything
        if (!chat.isGroupChat()) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            return;
        }

        // getting all the contacts
        List<Contact> contacts = new ArrayList<>();
        for (Participant participant : chat.getParticipants()) {
            String username = participant.getUsername();
            Contact contact = NetMeApp.getContactWith(username);

            if (contact != null) {
                // adjusting the phone list to only have the currently used phone number/username
                List<Phone> phones = new ArrayList<>();
                for (Phone phone : contact.getPhones()) {
                    if (phone.getPlainNumber().equals(username)) {
                        phones.add(phone);
                        break;
                    }
                }
                contact.setPhones(phones);
                Log.i("yuyong_profile", "initializeDrawer: contact != null");
            } else {
                // create a new phone object with the username
                List<Phone> phones = new ArrayList<>();
                Phone phone = new Phone(
                        participant.getUsername(),
                        0,
                        null
                );
                phones.add(phone);

                // create a new contact using the participant
                contact = new Contact(
                        -1,
                        null,
                        participant.getFullName(),
                        phones,
                        null,
                        null,
                        null,
                        false,
                        true
                );
                contact.setAvatarUrl(participant.getAvatarUrl());
                Log.i("yuyong_profile", "initializeDrawer: contact == null");
            }

            contacts.add(contact);
        }

        // initialize the textAdapter to be used by the participant list
        participantList.setLayoutManager(new LinearLayoutManager(getActivity()));
        participantList.setAdapter(new ParticipantAdapter(getActivity(), contacts));
    }

    /**
     * Click method which will close the participant drawer
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.chat_close_drawer)
    public void closeDrawer(View view) {
        drawer.closeDrawers();
    }

    /**
     * Adjusts the fragment's view for when video is also being shown.
     * The toolbar is hidden is video mode is marked as on so that the attachment
     * menu is adjusted accordingly.
     *
     * @param videoModeOn to set it for video mode ({@code TRUE}) or just text ({@code FALSE})
     */
    public void setVideoMode(boolean videoModeOn) {
        this.videoModeOn = videoModeOn;
        if (toolbar != null) toolbar.setVisibility(videoModeOn ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onAvatarClicked(final String senderUsername) {
        // opening the contact details screen
        Contact contact = NetMeApp.getContactWith(senderUsername);
        if (contact != null) {
            Intent contactIntent = new Intent(getActivity(), ContactDetailActivity.class);
            contactIntent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, contact.getId());
            startActivity(contactIntent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setTitle(R.string.add_contact);
            builder.setMessage(R.string.add_selected_contact_msg);
            builder.setNegativeButton(R.string.cancel, null);
            builder.setPositiveButton(
                    R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // try to find the name of the participant
                            String name = senderUsername;
                            for (Participant participant : textAdapter.getParticipants()) {
                                if (participant.getUsername().equals(senderUsername)) {
                                    name = participant.getFullName();
                                    break;
                                }
                            }

                            // start the add contact stuff
                            Intent addContactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                            addContactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                            addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, senderUsername);
                            addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                            startActivity(addContactIntent);
                        }
                    }
            );
            builder.show();
        }
    }

    @Override
    public void onClick(final ManagedMessage managedMessage) {
        // if the content is good, try to do some action on the content depending on what it is
        boolean statusReady = managedMessage.getOverallStatus() == ManagedMessage.STATUS_READY;
        // statusReady && managedMessage.hasDecryptedMedia() &&managedMessage.isFromContact()
        if (statusReady && managedMessage.hasDecryptedMedia()) {
            switch (managedMessage.getDecryptedMedia().get(0).getType()) {
                case Media.TYPE_IMAGE: {
                    Log.i("yuyong_image", "onClick:TYPE_IMAGE ");
                    Intent imageIntent = new Intent(getActivity(), ImageViewActivity.class);
                    imageIntent.putExtra(ImageViewActivity.EXTRA_CHAT_ID, chatId);
                    imageIntent.putExtra(ImageViewActivity.EXTRA_MESSAGE_ID, managedMessage.getMessageId());
                    startActivity(imageIntent);
                    break;
                }
                case Media.TYPE_VOICE:
                case Media.TYPE_VIDEO:
                case Media.TYPE_AUDIO:
                case Media.TYPE_FILE: {
                    Log.d("ChatActivity", "working on voice and file types");
                    try {
                        Log.i("yuyong_image", "onClick:TYPE_VOICE ");
                        msgManager.saveDecryptedMediaInternally(managedMessage, 0, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(
                                getActivity(),
                                R.string.error_file_open,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                    break;
                }

                case Media.TYPE_LOCATION: // this is handled by the actual map being displayed
                case Media.TYPE_INVALID:
                default:
                    //do nothing
            }
        } else if (managedMessage.getOverallStatus() == ManagedMessage.STATUS_FAILED) {
            // try to reload or resend the message
            switch (managedMessage.getType()) {
                case ManagedMessage.TYPE_SENT:
                    msgManager.resendMessage(managedMessage);
                    break;

                case ManagedMessage.TYPE_RECEIVED:
                case ManagedMessage.TYPE_LOAD:
                    msgManager.reloadMessage(managedMessage);
                    break;

            }
        }
    }

    @Override
    public void onCloseClicked(Media media) {
    }

    @Override
    public boolean onLongClicked(View messageView, final ManagedMessage managedMessage) {
        // get the message state variables
        int msgStatus = managedMessage.getOverallStatus();
        boolean fromContact = managedMessage.isFromContact();
        boolean hasText = managedMessage.hasDecryptedText();
        boolean hasMedia = managedMessage.hasDecryptedMedia();

        // start creating the popup menu
        Context wrapper = new ContextThemeWrapper(getActivity(), R.style.ActionMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, messageView);
        MenuInflater inflater = popupMenu.getMenuInflater();
        popupMenu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_msg_copy:
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText(
                                        getString(R.string.text_copy),
                                        managedMessage.getDecryptedText()
                                );
                                clipboard.setPrimaryClip(clip);
                                return true;

                            case R.id.menu_msg_delete:
                                msgManager.deleteMessage(managedMessage.getMessageId());
                                return true;

                            case R.id.menu_msg_edit:
                                Intent editIntent = new Intent(getActivity(), EditMessageActivity.class);
                                editIntent.putExtra(EditMessageActivity.EXTRA_CHAT_ID, chatId);
                                editIntent.putExtra(EditMessageActivity.EXTRA_MESSAGE_ID, managedMessage.getMessageId());
                                startActivity(editIntent);
                                return true;

                            case R.id.menu_msg_retry:
                                onClick(managedMessage);
                                return true;

                            case R.id.menu_msg_save:
                                // save externally
                                Log.i("yuyong", "onLongClicked: menu_msg_save");
                                msgManager.saveDecryptedMediaExternally(managedMessage, 0, TextFragment.this);
                                return true;
                        }
                        return false;
                    }
                }
        );

        // show just the re-try option
        if (msgStatus == ManagedMessage.STATUS_FAILED) {
            inflater.inflate(R.menu.menu_msg_failed, popupMenu.getMenu());
            popupMenu.show();
            return true;
        }
        // show the save option if it has media and the copy text option if it has text
        else if (msgStatus == ManagedMessage.STATUS_READY && fromContact && (hasMedia || hasText)) {
            // if there is media, add the save option
            if (hasMedia) inflater.inflate(R.menu.menu_msg_save, popupMenu.getMenu());
                // if there is text, add the copy option
            else inflater.inflate(R.menu.menu_msg_copy, popupMenu.getMenu());

            popupMenu.show();
            return true;
        }
        // show the delete option as well as the copy and edit text options if it has text
        else if (msgStatus == ManagedMessage.STATUS_READY && !fromContact) {
            inflater.inflate(R.menu.menu_msg_delete, popupMenu.getMenu());

            // if there is media, add the save option
            if (hasMedia) inflater.inflate(R.menu.menu_msg_save, popupMenu.getMenu());
                // if there is text, add the copy and edit options
            else if (hasText) {
                inflater.inflate(R.menu.menu_msg_copy, popupMenu.getMenu());
                inflater.inflate(R.menu.menu_msg_edit, popupMenu.getMenu());
            }

            popupMenu.show();
            return true;
        }

        return false;
    }


//--------------------------------------------------------------------------------------------------


    @SuppressWarnings("ConstantConditions")
    @Override
    public void messageSavingResult(final File savedMedia, final boolean successful) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Context context = getActivity();

                    // if the file was
                    if (successful && savedMedia != null) {
                        try {
                            // get file type
                            String fileName = savedMedia.getName();
                            String mimeType = URLConnection.guessContentTypeFromName(fileName);
                            Log.i("yuyong_voice", String.format("messageSavingResult:--->%s --->%s--->%s", savedMedia.getName(), savedMedia.getAbsolutePath(), context));
                            // create uri that can be read externally
                            Uri externalReadUri = FileProvider.getUriForFile(
                                    context,
                                    "com.hsdi.NetMe",
                                    savedMedia
                            );
                            // generate view intent and start
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(externalReadUri, mimeType);
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException anfe) {
                            Log.e("Messenger", "Failed to find an activity to run this", anfe);
                            Toast.makeText(context, R.string.error_file_open, Toast.LENGTH_SHORT).show();
                        }
                    } else if (successful) {
                        Toast.makeText(context, R.string.file_stored_downloads, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(context, R.string.error_save_file, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onChatLoaded(final Chat chat, final boolean isLoaded) {

        // resetting the loading status so more messages can be loaded if needed
        currentlyLoading = false;

        // set chat information
        chatId = chat.getChatId();
        Log.i("yuyong_TextFragment", "onChatLoaded: " + chatId);
        if (textAdapter == null) {
            return;
        }
        textAdapter.updateChatId(chatId);
        textAdapter.addParticipants(chat.getParticipants());
        unloadedMsgCount = chat.getTotalMessage() - msgManager.getMessageCount();

        // storing this chat as the latest for these contacts
        ChatTrackerManager.storeChat(getActivity(), chat);

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // initialize the participant list
                    initializeDrawer(chat);
                    if (!isLoaded) {
                        // hiding the message loading progress bar
                        progressBar.setVisibility(View.GONE);
                    }
                    toolbarTitle.setText(chat.getTitle(getActivity()));

                    String chatName = ChatTrackerManager.getChatName(getActivity(), chat.getChatId());
                    //String chatName = chat.getTitle()
                    // use any stored chat titles
                    if (!TextUtils.isEmpty(chatName)) toolbarTitle.setText(chatName);
                        // adjusting the toolbar for group chats
                    else if (chat.isGroupChat()) {
                        toolbarTitle.setText(R.string.group_title);
                      //  toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.group_avatar, 0, 0, 0);
                      //  ((BaseThemeActivity) getActivity()).applyTheme(toolbarTitle, R.drawable.group_avatar, drawableLeft);

                    }
                    // adjusting the toolbar for single person chat
                    else {
                        Participant firstOther = chat.getFirstOtherParticipant(getActivity());
                        if (firstOther != null && !TextUtils.isEmpty(firstOther.getUsername())) {
                            invited = firstOther.getUsername();
                        }
                    }

                    if (!menuLoading) {
                        // adjust the menu for single person chat
                        if (!chat.isGroupChat()) toolbar.inflateMenu(R.menu.menu_text_chat_single);
                            // adjust the menu for group chat
                        else toolbar.inflateMenu(R.menu.menu_text_chat_group);
                        menuLoading = true;
                    }


                    toolbar.setOnMenuItemClickListener(TextFragment.this);
                }
            });
        }
    }

    @Override
    public void onChatFailed(final String errorMessage) {
        //Log.i("yuyong_TextFragment", "onChatFailed: " + textAdapter.hashCode());
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // hiding the message loading progress bar
                    progressBar.setVisibility(View.GONE);
                    if (!DeviceUtils.hasInternetConnection(getActivity())) {
                        DialogUtils.getFinishingErrorDialog(getActivity(), R.string.error_connection).show();
                    } else if (errorMessage != null) {
                        DialogUtils.getFinishingErrorDialog(getActivity(), errorMessage).show();
                    } else {
                        Log.i("yuyong_TextFragment", "onChatFailed ERROR");
                        //   DialogUtils.getFinishingErrorDialog(getActivity(), R.string.error_unexpected).show();
                    }
                }
            });
        }
    }

    @Override
    public void onNewChatStarted(final long newChatId, final List<Participant> participants) {
        // Log.i("yuyong_TextFragment", "onNewChatStarted: " + textAdapter.hashCode());
        // updating everyone's chatIds
        chatId = newChatId;
        NetMeApp.setCurrentChat(newChatId);
        if (textAdapter == null) {
            return;
        }
        textAdapter.addParticipants(participants);
        textAdapter.updateChatId(chatId);
        textAdapter.notifyDataSetChanged();

        // storing this chat as the latest for these contacts
        ChatTrackerManager.storeChatIdWithParticipants(getActivity(), participants, chatId, -1, -1, -1);

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    attachButton.setVisibility(View.VISIBLE);
                }
            });
        }
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Shows a popup menu above the attachment button with the things the user can attach
     */
    @TargetApi(23)
    @SuppressWarnings("unused")
    @OnClick(R.id.chat_attach_btn)
    public void onAttachClicked(View view) {
        Context wrapper = new ContextThemeWrapper(getActivity(), R.style.ActionMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        if (videoModeOn) inflater.inflate(R.menu.menu_attach_for_video, popupMenu.getMenu());
        else inflater.inflate(R.menu.menu_attach, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_item_voice:
                                startVoiceRecorder();
                                return true;

                            case R.id.menu_item_camera:
                                startCamera();
                                return true;

                            case R.id.menu_item_file:
                                startFileBrowser();
                                return true;

                            case R.id.menu_item_location:
                                startMapActivity();
                                return true;
                        }
                        return false;
                    }
                }
        );
        popupMenu.show();

      //  initPopmenu();
      //  popMenu.showAsDropDown(attachButton);
    }


    /**
     * Tries to send any message items
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.chat_send_btn)
    public void onSendClicked(View view) {

        // general checks
        boolean chatExists = chatId > -1;
        boolean invitedExists = !TextUtils.isEmpty(invited);
        List<Media> pendingMediaList = pendingMediaAdapter.getItemList();
        Log.i("yuyong", String.format("onSendClicked getItemList %d", pendingMediaList.hashCode()));
        for (Media media : pendingMediaList) {
            Log.i("yuyong", String.format("onSendClicked getItemList %s-->%s", media.getFileName(), media.getFileDesc() + ""));
        }
        // post any media items
        for (Media media : pendingMediaList) {

            // media checks
            boolean fileDescExists = media.getFileDesc() != null;
            boolean fileExists = media.getFile() != null;
            switch (media.getType()) {
                case Media.TYPE_IMAGE:
                    if (chatExists && fileDescExists) {
                        Log.i("yuyong", "onSendClicked: 1");
                        msgManager.sendImageWithText(null, media.getFileName(), media.getFileDesc(), media.getUri());
                    } else if (chatExists && fileExists) {
                        Log.i("yuyong", "onSendClicked: 2");
                        msgManager.sendImageWithText(null, media.getFile());
                    } else if (fileDescExists && invitedExists) {
                        Log.i("yuyong", "onSendClicked: 3");
                        msgManager.startChatWithImage(invited, null, media.getFileName(), media.getFileDesc(), media.getUri(), this);
                    } else if (fileExists && invitedExists) {
                        Log.i("yuyong", "onSendClicked: 4");
                        msgManager.startChatWithImage(invited, null, media.getFile(), this);
                    }
                    break;

                case Media.TYPE_LOCATION:
                    if (chatExists) msgManager.sendLocationWithText(null, media.getLocation());
                    else if (invitedExists) {
                        msgManager.startChatWithLocation(invited, null, media.getLocation(), this);
                    }
                    break;

                case Media.TYPE_VOICE:
                    if (chatExists && fileExists) {
                        Log.i("yuyong", "onSendClicked: ----->sendVoiceWithText");
                        msgManager.sendVoiceWithText(null, media.getFile());
                    } else if (fileExists && invitedExists) {
                        Log.i("yuyong", "onSendClicked: ----->startChatWithVoice");
                        msgManager.startChatWithVoice(invited, null, media.getFile(), this);
                    }
                    break;

                case Media.TYPE_AUDIO:
                case Media.TYPE_VIDEO:
                case Media.TYPE_FILE:
                    if (chatExists && fileDescExists) {
                        msgManager.sendFileWithText(null, media.getFileName(), media.getFileDesc());
                    } else if (fileDescExists && invitedExists) {
                        msgManager.startChatWithImage(invited, null, media.getFileName(), media.getFileDesc(), media.getUri(), this);
                    }
                    break;

                case Media.TYPE_INVALID:
                default:
                    break;
            }
        }

        // clear the list of media items after it has been sent
        pendingMediaAdapter.clearItemList();

        // get the text and clear the field
        String text = msgEntryField.getText().toString();
        msgEntryField.setText("");

        // send the text normally when the chat exists
        if (!TextUtils.isEmpty(text) && chatExists) {
            msgManager.sendTextOnly(text);
        }
        // start a new chat otherwise
        else if (!TextUtils.isEmpty(text) && invitedExists) {
            msgManager.startChatWithTextOnly(invited, text, this);
        }
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Starts the SoundRecorderActivity to record the audio attachment
     */
    private void startVoiceRecorder() {
        String voiceFileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        File voiceDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        voiceFile = new File(voiceDir, voiceFileName + ".voice.3gp");

        Intent recordIntent = new Intent(getActivity(), SoundRecorderActivity.class);
        recordIntent.putExtra(MediaStore.EXTRA_OUTPUT, voiceFile.getPath());
        recordIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, MAX_VOICE_RECORDING_LENGTH);
        recordIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, MAX_ATTACHMENT_SIZE);
        startActivityForResult(recordIntent, REQUEST_CODE_RECORD_VOICE);
    }

    /**
     * Starts the device camera to capture an image attachment
     */
    private void startCamera() {
        try {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                File photoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile(
                        imageFileName,
                        ".jpg",
                        photoDir
                );

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
            } else {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_PERMISSION
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the device file browser to find a file to send as an attachment
     */
    private void startFileBrowser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_FILE);
    }

    /**
     * Starts the MapActivity to pick a location to send as an attachment
     */
    private void startMapActivity() {
        Intent intent = new Intent(getActivity(), MapActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_LOCATION);
    }

    /**
     * Makes sure the browser returned a file, then gets the message manager to send it.
     *
     * @param data    the data intent received from the browser activity
     */
    @SuppressWarnings("ConstantConditions")
    List<Media> list;

    private void setPendingFile(Intent data) {
        if (data != null && data.getData() != null) {
            try {
                // getting the data
                Uri uri = data.getData();

                ContentResolver resolver = getActivity().getContentResolver();
                Cursor cursor = resolver.query(uri, null, null, null, null);
                cursor.moveToFirst();
                String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                cursor.close();

                String mimeType = resolver.getType(uri);
                FileDescriptor fileDescriptor = resolver.openFileDescriptor(uri, "r").getFileDescriptor();

                if (TextUtils.isEmpty(fileName) || fileDescriptor == null) {
                    throw new FileNotFoundException();
                }

                // if image, add image to pending list
                if (mimeType != null && mimeType.contains("image")) {
                    Log.i("yuyong", "setPendingFile-->pendingMediaAdapter.addImageFile");
                    pendingMediaAdapter.addImageFile(fileName, fileDescriptor, uri);
                    list = pendingMediaAdapter.getItemList();
                }
                // not image, just add as normal file to pending list
                else pendingMediaAdapter.addFile(fileName, fileDescriptor);
            }
            // something went wrong, show user an error
            catch (FileNotFoundException | NullPointerException | CursorIndexOutOfBoundsException exception) {
                Log.e("Messenger", "Failed to get media attachment", exception);

                // show the user an error about the message not sending
                Toast.makeText(getActivity(), R.string.error_send_encrypted, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Attempts to get the location (coordinates) chosen in the MapActivity and then sends.
     *
     * @param data the intent received from the MapActivity
     */
    private void setPendingLocation(Intent data) {
        if (data != null) {
            LatLng latLng = data.getParcelableExtra(MapActivity.EXTRA_LAT_LNG);
            if (latLng != null) pendingMediaAdapter.addLocation(latLng);
        }
    }

    boolean isTyping;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.i("yuyong_menu", "onPrepareOptionsMenu: isTyping = " + isTyping);
        menu.findItem(R.id.menu_text_video).setVisible(!isTyping);
        getActivity().invalidateOptionsMenu();

    }

    @Override
    public void onShowInputting(long chatId) {
        Log.i("yuyong_message", "onShowInputting: ");
        Chat chat = ChatTrackerManager.getChatWithChatId(getActivity(), chatId);
        List<Participant> participants = chat.getParticipants();
        if (getActivity() != null && participants.size() < 3) {
            Participant participant = chat.getFirstOtherParticipant(getActivity());
            String username = participant.getUsername();
            Contact contact = NetMeApp.getContactWith(username);
            if (contact != null && TextUtils.isEmpty(contact.getName())) {
                participantName = contact.getName();
            } else {
                participantName = participant.getFirstName() + " " + participant.getLastName();
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatTyping.setVisibility(View.VISIBLE);
                    chatToolbarTyping.setVisibility(View.VISIBLE);
                    chatToolbarTyping.setImageResource(R.drawable.animation_red_green);
                    AnimationDrawable animationDrable = (AnimationDrawable) chatToolbarTyping.getDrawable();
                    animationDrable.start();
                    isTyping = true;
                    onPrepareOptionsMenu(toolbar.getMenu());
                    if (timer == null) {
                        timer = startInputTimer();
                        startClearInputTimer();
                    }
                }
            });
        }
    }

    @Override
    public void onClearInputting() {
        Log.i("yuyong_message", "onClearInputting: ");
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (chatTyping != null) {
                        chatTyping.setVisibility(View.GONE);
                    }
                    if (chatToolbarTyping != null) {
                        chatToolbarTyping.setVisibility(View.GONE);
                    }
                    isTyping = false;
                    onPrepareOptionsMenu(toolbar.getMenu());
                    Log.i("yuyong_menu", "onClearInputting:isTyping =  " + isTyping);
                }
            });
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    chatTyping.setText(participantName + " is typing...");
                    chatTyping.setTextSize(20);
                    Log.i("yuyong_menu", "handleMessage: isTyping = " + isTyping);
                    break;
                default:
                    break;
            }
        }
    };

    public Timer startInputTimer() {
        TimerTask timerInput = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        };
        Timer timer1 = new Timer();
        //  timer1.schedule(timerInput, 0, 500);
        timer1.schedule(timerInput, 0);
        return timer1;
    }

    public Timer startClearInputTimer() {
        TimerTask clearInput = new TimerTask() {
            @Override
            public void run() {
                onClearInputting();
                timer = null;
            }
        };
        Timer timer2 = new Timer();
        timer2.schedule(clearInput, 7 * 1000);
        return timer2;
    }

    private void sendTypingType(long chatId) {
        Log.i("yuyong_message", "sendTypingType: chatId = " + chatId);
        RestServiceGen.getUnCachedService()
                .sendTypingType(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        chatId,
                        String.valueOf(Constants.INPUT_TYPING)
                )
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        Log.i("yuyong_message", "sendTypingType:onResponse: ");
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.i("yuyong_message", "sendTypingType:onFailure: ");
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
