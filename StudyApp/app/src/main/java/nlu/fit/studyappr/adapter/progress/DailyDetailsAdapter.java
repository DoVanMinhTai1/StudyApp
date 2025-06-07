package nlu.fit.studyappr.adapter.progress; // Your package

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nlu.fit.studyappr.R; // Ensure correct R class
import nlu.fit.studyappr.model.learningProgress.DailyActivityItem;
import nlu.fit.studyappr.model.learningProgress.DailyProgressEntry;

public class DailyDetailsAdapter extends RecyclerView.Adapter<DailyDetailsAdapter.ActivityItemViewHolder> {

    private static final int VIEW_TYPE_DATE_HEADER = 0;
    private static final int VIEW_TYPE_ACTIVITY_ITEM = 1;

    private List<DailyProgressEntry> displayableItems;
    private LayoutInflater inflater;
    private Context context;

    private String currentType;

    public DailyDetailsAdapter(Context context, List<DailyProgressEntry> dailyProgressEntries,String initial) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.displayableItems = dailyProgressEntries;
        this.currentType = initial;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (displayableItems.get(position) instanceof DailyProgressEntry) {
//            return VIEW_TYPE_DATE_HEADER;
//        } else if (displayableItems.get(position) instanceof DailyActivityItem) {
//            return VIEW_TYPE_ACTIVITY_ITEM;
//        }
//        return super.getItemViewType(position); // Should not happen
//    }

    @NonNull
    @Override
    public DailyDetailsAdapter.ActivityItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_track_list_item_activity_detail,parent,false);
        return new ActivityItemViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ActivityItemViewHolder holder, int position) {
        DailyProgressEntry entry = displayableItems.get(position);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            holder..setText(entry.getDate().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy")));
//        }

        List<DailyActivityItem> dailyProgressEntries = entry.getActivities();

        List<DailyActivityItem> filtered = dailyProgressEntries.stream().filter(activity -> currentType.equalsIgnoreCase(activity.getType())).collect(Collectors.toList());

        holder.linearLayoutActivitiesContainer.removeAllViews();

        if(filtered.isEmpty()) {
            TextView textView = new TextView(context);
            textView.setText("Không có hoạt động nào");
            holder.linearLayoutActivitiesContainer.addView(textView);
        } else {
            for (DailyActivityItem d : filtered) {
                View activityView = LayoutInflater.from(context).inflate(R.layout.activity_track_list_item_activity_detail,holder.linearLayoutActivitiesContainer,false);


                TextView textView = activityView.findViewById(R.id.activityTitleTextView);
                TextView timeSpent = activityView.findViewById(R.id.activityDetailTextView);

                textView.setText(d.getTitle());
                String timeSpentString = String.valueOf(d.getTimeSpentSecondsToday());
                timeSpent.setText(timeSpentString);


                holder.linearLayoutActivitiesContainer.addView(activityView);
            }
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
    public static class ActivityItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutActivitiesContainer;

        public ActivityItemViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutActivitiesContainer = itemView.findViewById(R.id.linearContainer);
        }
    }

    // Method to update data if needed
    public void updateData(List<DailyProgressEntry> newDailyProgressEntries, String type) {
        this.displayableItems.clear();
        this.displayableItems.addAll(newDailyProgressEntries);
        this.currentType = type;
        notifyDataSetChanged();
    }
}