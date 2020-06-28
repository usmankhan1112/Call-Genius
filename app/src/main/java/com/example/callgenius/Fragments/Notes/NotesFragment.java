package com.example.callgenius.Fragments.Notes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callgenius.R;
import com.example.callgenius.Recycler.ModelCalls;
import com.example.callgenius.Recycler.NotesRvAdapter;
import com.example.callgenius.SubActivities.NotesSub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.callgenius.MainActivity.getday;

public class NotesFragment extends Fragment implements NotesRvAdapter.NoteListener {

    protected RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public NotesRvAdapter mAdapter;
    
    //private FloatingSearchView search;
    private SearchView search;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notes, container, false);

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = getView().findViewById(R.id.view_recycler);
        //search = view.findViewById(R.id.searchView);

        //search = (FloatingSearchView) getView().findViewById(R.id.floating_search_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new NotesRvAdapter(getCallLog(), this);
        recyclerView.setAdapter(mAdapter);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    mAdapter.getFilter().filter(query);
                    return false;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange (String newText){
                if (newText != null) {
                    mAdapter.getFilter().filter(newText);
                    return false;
                }
                return true;
            }
        });
    }
*/

    public List<ModelCalls> getCallLog(){
        List<ModelCalls> list = new ArrayList<>();

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
        while(!cursor.isAfterLast()){
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

            /*
            long PastTime = System.currentTimeMillis() - date;

            long hoursPast = TimeUnit.MILLISECONDS.toHours(PastTime);
            long minsPast = TimeUnit.MILLISECONDS.toMinutes(PastTime) - (hoursPast*60);
            long secsPast = TimeUnit.MILLISECONDS.toSeconds(PastTime) - (minsPast*60);

            String TimePast;
                if (hoursPast == 0) {
                    if (minsPast == 0) {
                        TimePast = secsPast + " secs ago";
                    } else {
                        TimePast = minsPast + " mins ago";
                    }
                } else {
                    TimePast = hoursPast + " hours ago";
                }
                    */

            if(list.size() == 0 || list.size() == 1 || !list.get(list.size() - 2).getDay().equalsIgnoreCase(day)){
                list.add(new ModelCalls(cursor.getString(caller),cursor.getString(number),cursor.getInt(status),TotalDuration, day, time, true));
            }

            list.add(new ModelCalls(cursor.getString(caller),cursor.getString(number),cursor.getInt(status),TotalDuration, day, time, false));
            cursor.moveToNext();
        }

        return list;
    }

    @Override
    public void Click(int position) {
        List<ModelCalls> list2;
        list2 = getCallLog();
        Intent intent = new Intent(getContext(), NotesSub.class);
        Bundle bundle = new Bundle();
        bundle.putInt("Status",list2.get(position).getStatus());
        bundle.putString("Caller",list2.get(position).getCaller());
        bundle.putString("Number",list2.get(position).getNumber());
        bundle.putString("Duration",list2.get(position).getDuration());
        bundle.putString("Day",list2.get(position).getDay());
        bundle.putString("Time",list2.get(position).getTime());

        intent.putExtras(bundle);
        startActivity(intent);
    }
}