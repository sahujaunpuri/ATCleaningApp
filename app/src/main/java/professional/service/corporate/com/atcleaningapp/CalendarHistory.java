package professional.service.corporate.com.atcleaningapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarHistory extends android.support.v4.app.Fragment  implements OnDateSelectedListener {
    MaterialCalendarView materialCalendarView;
    int user_id;
    private SessionManager sm;
    private ArrayList<LoginDBClass> lac;
    private int current_user;
    public static OngoinWorkEmployer newInstance() {
        OngoinWorkEmployer fragment = new OngoinWorkEmployer();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calender_history, container, false);
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            user_id = Integer.parseInt(f.getUser_id());
        }
        current_user = user_id;
        materialCalendarView = (MaterialCalendarView)view.findViewById(R.id.calendarView);
        materialCalendarView.setOnDateChangedListener(this);
        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Fragment fragment = new PastBookings();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        //String str_date = date.getDate().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sdf.format(date.getDate());
        int hirer_id = current_user;
        b.putInt("hirer_id", hirer_id);
        b.putString("date", date1);
        fragment.setArguments(b);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

}
