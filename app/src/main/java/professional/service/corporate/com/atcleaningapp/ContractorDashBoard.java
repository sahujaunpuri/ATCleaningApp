package professional.service.corporate.com.atcleaningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContractorDashBoard extends android.support.v4.app.Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.contractors_view_of_job_post, container, false);
        return view;
    }
}
