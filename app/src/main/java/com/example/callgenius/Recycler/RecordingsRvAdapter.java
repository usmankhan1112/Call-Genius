package com.example.callgenius.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.callgenius.R;

import java.util.List;

public class RecordingsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ModelRecordings> recordList;

        public RecordingsRvAdapter(List<ModelRecordings> list) {
            recordList = list;
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class CallViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            ImageView callstatus;
            TextView caller,calltime,callduration;

            public CallViewHolder(View itemview) {
                super(itemview);
                caller = itemview.findViewById(R.id.callerid);
                calltime = itemview.findViewById(R.id.call_time);
                callduration = itemview.findViewById(R.id.call_duration);
                callstatus = itemview.findViewById(R.id.call_status);
            }
        }

        public static class HeaderViewHolder extends RecyclerView.ViewHolder {
            TextView headertext;

            public HeaderViewHolder(View itemView){
                super(itemView);
                headertext = itemView.findViewById(R.id.header);
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder holder;

            if (viewType == 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_header, parent, false);
                holder = new HeaderViewHolder(view);
            }

            else{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_recycler, parent, false);
                holder = new CallViewHolder(view);
            }
            return holder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            if (getItemViewType(position) == 0) {
                com.example.callgenius.Recycler.NotesRvAdapter.HeaderViewHolder headerHolder = (com.example.callgenius.Recycler.NotesRvAdapter.HeaderViewHolder) holder;
                headerHolder.headertext.setText(recordList.get(position).getDay());
            }

            else if(getItemViewType(position) == 1) {
                com.example.callgenius.Recycler.NotesRvAdapter.CallViewHolder callHolder = (com.example.callgenius.Recycler.NotesRvAdapter.CallViewHolder) holder;
                TextView callerid, duration, time;
                ImageView status;

                status = callHolder.callstatus;
                callerid = callHolder.caller;
                duration = callHolder.callduration;
                time = callHolder.calltime;

                if (recordList.get(position).getStatus() == 1) {
                    status.setImageResource(R.drawable.incoming);
                } else if (recordList.get(position).getStatus() == 2) {
                    status.setImageResource(R.drawable.outgoing);
                } else if (recordList.get(position).getStatus() == 3) {
                    status.setImageResource(R.drawable.missed);
                }
                if (recordList.get(position).getCaller() == null) {
                    callerid.setText(recordList.get(position).getNumber());
                } else {
                    callerid.setText(recordList.get(position).getCaller());
                }

                duration.setText(recordList.get(position).getDuration());
                time.setText(recordList.get(position).getTime());
            }
        }

        @Override
        public int getItemViewType(int position)
        {
            if(recordList.get(position).isHeader() == true) {
                return 0;
            }
            else
                return 1;
        }
        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return recordList.size();
        }
}
