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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import nlu.fit.studyappr.R; // Ensure correct R class
import nlu.fit.studyappr.model.learningProgress.DailyActivityItem;
import nlu.fit.studyappr.model.learningProgress.DailyProgressEntry;

public class DailyDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_DATE_HEADER = 0;
    private static final int VIEW_TYPE_ACTIVITY_ITEM = 1;
    private LayoutInflater inflater;

    private List<Object> displayableItems = new ArrayList<>();

    // Các hằng số lọc
    private final String TOPIC_TYPE_DATE = "DATE";
    private final String TOPIC_TYPE_VOCABULARY = "VOCABULARY";
    private final String TOPIC_TYPE_GRAMMAR = "GRAMMAR";


    private Context context;

    private String currentType;

    public DailyDetailsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemViewType(int position) {
        // Kiểm tra loại object tại vị trí 'position' để trả về đúng ViewType
        if (displayableItems.get(position) instanceof String) {
            return VIEW_TYPE_DATE_HEADER;
        } else {
            return VIEW_TYPE_ACTIVITY_ITEM;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Dựa vào viewType, inflate layout tương ứng và tạo ViewHolder
        if (viewType == VIEW_TYPE_DATE_HEADER) {
            View view = inflater.inflate(R.layout.list_item_date_header, parent, false);
            return new DateHeaderViewHolder(view);
        } else { // VIEW_TYPE_ACTIVITY_ITEM
            View view = inflater.inflate(R.layout.activity_track_list_item_activity_detail, parent, false);
            return new ActivityItemViewHolder(view);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Dựa vào loại ViewHolder, lấy dữ liệu và bind vào view
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_DATE_HEADER) {
            DateHeaderViewHolder dateHolder = (DateHeaderViewHolder) holder;
            String dateString = (String) displayableItems.get(position);
            dateHolder.bind(dateString);
        } else { // VIEW_TYPE_ACTIVITY_ITEM
            ActivityItemViewHolder activityHolder = (ActivityItemViewHolder) holder;
            DailyActivityItem activityItem = (DailyActivityItem) displayableItems.get(position);
            activityHolder.bind(activityItem);
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

        void bind(String dateStr) {
            try {
                // Định dạng lại ngày tháng cho dễ đọc
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("vi", "VN"));
                    dateHeaderTextView.setText(dateTime.format(formatter));
                } else {
                    dateHeaderTextView.setText(dateStr); // Fallback cho API < 26
                }
            } catch (Exception e) {
                dateHeaderTextView.setText(dateStr); // Nếu lỗi, hiện ngày gốc
                e.printStackTrace();
            }
        }
    }


    // ViewHolder for Activity Item
    public static class ActivityItemViewHolder extends RecyclerView.ViewHolder {
        TextView activityTitleTextView;
        TextView activityDetailTextView;

        public ActivityItemViewHolder(@NonNull View itemView) {
            super(itemView);
            activityTitleTextView = itemView.findViewById(R.id.activityTitleTextView);
            activityDetailTextView = itemView.findViewById(R.id.activityDetailTextView);
        }

        void bind(DailyActivityItem item) {
            activityTitleTextView.setText(item.getTitle());
            long seconds = item.getTimeSpentSecondsToday();
            // Chuyển đổi giây sang phút và giây để hiển thị đẹp hơn (tùy chọn)
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;
            String timeSpentString = String.format(Locale.getDefault(), "Thời gian học: %d phút %d giây", minutes, remainingSeconds);
            activityDetailTextView.setText(timeSpentString);
        }
    }

    public void setData(List<DailyProgressEntry> dailyProgressEntries, String filterType) {
        displayableItems.clear(); // Xóa dữ liệu cũ

        for (DailyProgressEntry entry : dailyProgressEntries) {
            List<DailyActivityItem> activitiesForDay;

            // Lọc các hoạt động dựa trên filterType
            if (filterType.equalsIgnoreCase(TOPIC_TYPE_DATE)) {
                activitiesForDay = entry.getActivities(); // Lấy tất cả
            } else { // Lọc theo VOCABULARY hoặc GRAMMAR
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    activitiesForDay = entry.getActivities().stream()
                            .filter(activity -> filterType.equalsIgnoreCase(activity.getType()))
                            .collect(Collectors.toList());
                } else { // Fallback cho API < 24
                    activitiesForDay = new ArrayList<>();
                    for(DailyActivityItem activity : entry.getActivities()) {
                        if(filterType.equalsIgnoreCase(activity.getType())) {
                            activitiesForDay.add(activity);
                        }
                    }
                }
            }

            // Nếu có hoạt động để hiển thị cho ngày này, thêm ngày và các hoạt động vào danh sách
            if (!activitiesForDay.isEmpty()) {
                // 1. Thêm tiêu đề ngày (là một String)
                displayableItems.add(entry.getDate());
                // 2. Thêm tất cả các hoạt động đã lọc
                displayableItems.addAll(activitiesForDay);
            }
        }

        // Báo cho Adapter biết dữ liệu đã thay đổi để vẽ lại RecyclerView
        notifyDataSetChanged();
    }
}