package com.example.callgenius.Recycler;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.callgenius.R;

import java.util.ArrayList;
import java.util.List;

public class NotesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    static List<ModelCalls> callList;
    static List<ModelCalls> searchList;
    static NoteListener mnotelistener;

    public NotesRvAdapter(List<ModelCalls> list, NoteListener notelistener) {
        callList = list;
        //searchList = new ArrayList<>(callList);
        mnotelistener = notelistener;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView callstatus;
        TextView caller,calltime,callduration;
        NoteListener noteListener;

        public CallViewHolder(View itemview, NoteListener noteListener) {
            super(itemview);
            caller = itemview.findViewById(R.id.callerid);
            calltime = itemview.findViewById(R.id.call_time);
            callduration = itemview.findViewById(R.id.call_duration);
            callstatus = itemview.findViewById(R.id.call_status);
            this.noteListener = noteListener;
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            noteListener.Click(getAdapterPosition());
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
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;

        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_header, parent, false);

            holder = new HeaderViewHolder(view);
        }

        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_recycler, parent, false);

            holder = new CallViewHolder(view, mnotelistener);
    }
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (getItemViewType(position) == 0) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headertext.setText(callList.get(position).getDay());
        }

        else if(getItemViewType(position) == 1) {
            CallViewHolder callHolder = (CallViewHolder) holder;

            TextView callerid, duration, time;
            ImageView status;

            status = callHolder.callstatus;
            callerid = callHolder.caller;
            duration = callHolder.callduration;
            time = callHolder.calltime;

            if (callList.get(position).getStatus() == 1) {
                status.setImageResource(R.drawable.incoming);
            } else if (callList.get(position).getStatus() == 2) {
                status.setImageResource(R.drawable.outgoing);
            } else if (callList.get(position).getStatus() == 3) {
                status.setImageResource(R.drawable.missed);
            }
            if (callList.get(position).getCaller() == null) {
                callerid.setText(callList.get(position).getNumber());
            } else {
                callerid.setText(callList.get(position).getCaller());
            }

            duration.setText(callList.get(position).getDuration());
            time.setText(callList.get(position).getTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(callList.get(position).isHeader() == true) {
            return 0;
        }
        else
            return 1;
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return callList.size();
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelCalls> filtered = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filtered.addAll(callList);
            }

            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(ModelCalls item : searchList){
                    if(item.getCaller().contains(filterPattern)){
                        filtered.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtered;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            callList.clear();
            callList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter(){
        return searchFilter;
    }

    public interface NoteListener{
        void Click(int position);
    }
}