package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class AddressListAdapter extends ArrayAdapter {

    private ArrayList<AddressClass> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView buildingNo;
        CheckBox select_checkbox;
        TextView buildingName;
        TextView street;
        TextView city;
        TextView zip;
        TextView state;
        TextView landmark;
      //  public CheckBox checkBox;
    }

    public AddressListAdapter(ArrayList data, Context context) {
        super(context, R.layout.address_list_row, data);
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public AddressClass getItem(int position) {
        return dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_list_row, parent, false);
            viewHolder.buildingNo = (TextView) convertView.findViewById(R.id.building);
            viewHolder.buildingName = (TextView) convertView.findViewById(R.id.building_name);
            viewHolder.street = (TextView) convertView.findViewById(R.id.street1);
            viewHolder.city = (TextView) convertView.findViewById(R.id.city);
            viewHolder.state = (TextView) convertView.findViewById(R.id.state);
            viewHolder.zip = (TextView) convertView.findViewById(R.id.zip);
            viewHolder.landmark = (TextView) convertView.findViewById(R.id.landmark);
           // viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.select_checkbox);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        AddressClass item = getItem(position);


        viewHolder.buildingNo.setText("Building No "+item.building);
        viewHolder.buildingName.setText("Apartment Name "+item.apartment);
        viewHolder.street.setText("Street "+item.street1);
        viewHolder.city.setText("City "+item.city);
        viewHolder.state.setText("State "+item.state);
        viewHolder.zip.setText("Zip "+item.zip);
        viewHolder.landmark.setText("Landmark "+item.landmark);
      //  viewHolder.checkBox.setChecked(item.checked);



        return result;
    }
}
