package nlu.fit.studyappr.adapter.progress; // Your package

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nlu.fit.studyappr.R; // Ensure correct R class
import nlu.fit.studyappr.model.DailyActivityItem;
import nlu.fit.studyappr.model.DailyProgressEntry;

public class DailyDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DATE_HEADER = 0;
    private static final int VIEW_TYPE_ACTIVITY_ITEM = 1;

    private List<Object> displayableItems; // Will hold both DailyProgressEntry (as header) and DailyActivityItem
    private LayoutInflater inflater;
    private Context context;

    public DailyDetailsAdapter(Context context, List<DailyProgressEntry> dailyProgressEntries) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.displayableItems = new ArrayList<>();
        // Flatten the list for the RecyclerView
        if (dailyProgressEntries != null) {
            for (DailyProgressEntry entry : dailyProgressEntries) {
                displayableItems.add(entry); // Add date entry itself to act as a header object
                if (entry.getActivities() != null) {
                    displayableItems.addAll(entry.getActivities());
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (displayableItems.get(position) instanceof DailyProgressEntry) {
            return VIEW_TYPE_DATE_HEADER;
        } else if (displayableItems.get(position) instanceof DailyActivityItem) {
            return VIEW_TYPE_ACTIVITY_ITEM;
        }
        return super.getItemViewType(position); // Should not happen
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATE_HEADER) {
            View view = inflater.inflate(R.layout.list_item_date_header, parent, false);
            return new DateHeaderViewHolder(view);
        } else { // VIEW_TYPE_ACTIVITY_ITEM
            View view = inflater.inflate(R.layout.activity_track_list_item_activity_detail, parent, false);
            // Ensure R.layout.activity_track_list_item_activity_detail is the correct layout name
            // from your XML snippet (you used it in tools:listitem)
            return new ActivityItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_DATE_HEADER) {
            DateHeaderViewHolder dateHolder = (DateHeaderViewHolder) holder;
            DailyProgressEntry entry = (DailyProgressEntry) displayableItems.get(position);
            dateHolder.dateHeaderTextView.setText(entry.getDate());
        } else if (holder.getItemViewType() == VIEW_TYPE_ACTIVITY_ITEM) {
            ActivityItemViewHolder activityHolder = (ActivityItemViewHolder) holder;
            DailyActivityItem activity = (DailyActivityItem) displayableItems.get(position);
            activityHolder.activityTitleTextView.setText(activity.getTitle());
            activityHolder.activityDetailTextView.setText(activity.getDetails());
            // You can add more logic here based on activity.getType() if needed
        }
    }

    @Override
    public int getItemCount() {
        return displayableItems != null ? displayableItems.size() : 0;
    }

    // ViewHolder for Date Header
    static class DateHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView dateHeaderTextView;

        public DateHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            dateHeaderTextView = itemView.findViewById(R.id.dateHeaderTextView);
        }
    }

    // ViewHolder for Activity Item
    static class ActivityItemViewHolder extends RecyclerView.ViewHolder {
        TextView activityTitleTextView;
        TextView activityDetailTextView;

        public ActivityItemViewHolder(@NonNull View itemView) {
            super(itemView);
            activityTitleTextView = itemView.findViewById(R.id.activityTitleTextView);
            activityDetailTextView = itemView.findViewById(R.id.activityDetailTextView);
        }
    }

    // Method to update data if needed
    public void updateData(List<DailyProgressEntry> newDailyProgressEntries) {
        this.displayableItems.clear();
        if (newDailyProgressEntries != null) {
            for (DailyProgressEntry entry : newDailyProgressEntries) {
                displayableItems.add(entry);
                if (entry.getActivities() != null) {
                    displayableItems.addAll(entry.getActivities());
                }
            }
        }
        notifyDataSetChanged();
    }
}