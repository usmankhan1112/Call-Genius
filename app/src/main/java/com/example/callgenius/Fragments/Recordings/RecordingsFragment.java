package com.example.callgenius.Fragments.Recordings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callgenius.R;
import com.example.callgenius.Recycler.ModelRecordings;
import com.example.callgenius.Recycler.RecordingsRvAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.AUDIO_SERVICE;
import static com.example.callgenius.MainActivity.getday;
import static com.example.callgenius.Fragments.User.UserFragment.recordenabled;

public class RecordingsFragment extends Fragment {

    private RecordingsViewModel recordingsViewModel;

    protected RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter rAdapter;
    public static Boolean callEnded = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recordingsViewModel =
                ViewModelProviders.of(this).get(RecordingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recordings, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rec_recycler = getView().findViewById(R.id.recording_recycler);
        ToggleButton play = getView().findViewById(R.id.play);

        rec_recycler.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        rec_recycler.setLayoutManager(layoutManager);

        rAdapter = new RecordingsRvAdapter(getRecordLog());
        rec_recycler.setAdapter(rAdapter);
    }

    List<ModelRecordings> list = new ArrayList<>();

    public List<ModelRecordings> getRecordLog(){
        String filename = "CallGenius-" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if(recordenabled){
            AudioManager manager = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);
            if(manager.getMode()==AudioManager.MODE_IN_CALL){
                Intent intent = new Intent(getContext(),RecordingsFragment.class);
                //
                // intent.setAction(ACTION_REC);
                intent.putExtra("File", filename);
                getContext().startService(intent);
                }
            if(callEnded){
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                }

                Cursor cursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);

                int caller = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                int status = cursor.getColumnIndex(CallLog.Calls.TYPE);

                cursor.moveToFirst();
                    Date date1 = new Date(Long.valueOf(cursor.getString(date)));
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mma");
                    String time = timeFormat.format(date1);

                    String day = getday(date1);

                    long Duration = Long.valueOf(cursor.getString(duration));

                    long Hours = TimeUnit.SECONDS.toHours(Duration);
                    long Minutes = TimeUnit.SECONDS.toMinutes(Duration) - (Hours*60);
                    long Seconds = Duration - (TimeUnit.SECONDS.toMinutes(Duration)*60);

                    String TotalDuration;
                    if(Hours == 0) {
                        if (Minutes == 0) {
                            TotalDuration = Seconds + " secs";
                        }
                        else {
                            TotalDuration = Minutes + "mins " + Seconds + "secs";
                        }
                    }

                    else{
                        TotalDuration = Hours + "h " + Minutes + "m " + Seconds + "s";
                    }

                    if(list.size() == 0 || list.size() == 1 || !list.get(list.size() - 2).getDay().equalsIgnoreCase(day)){
                        list.add(new ModelRecordings(cursor.getString(caller),cursor.getString(number),cursor.getInt(status),TotalDuration, day, time, filename, true));
                    }

                    list.add(new ModelRecordings(cursor.getString(caller),cursor.getString(number),cursor.getInt(status),TotalDuration, day, time, filename,false));
                    return list;
                }
            }
            return list;
        }
    }