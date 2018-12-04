package professional.service.corporate.com.atcleaningapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SafeDepositClass> products;
    private ProductListAdapterListener listener;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ProductListAdapter(Activity activity, List<SafeDepositClass> feedItems, ProductListAdapterListener listener) {
        this.activity = activity;
        this.products = feedItems;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int location) {
        return products.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.safe_deposite_amount1, null);


        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        TextView title = (TextView) convertView.findViewById(R.id.title_of_project);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        final EditText price = (EditText) convertView.findViewById(R.id.amt_paid_p);
        final TextView bid_amt = (TextView) convertView.findViewById(R.id.amt);
        //final TextView amt_paid = (TextView) convertView.findViewById(R.id.amt_paid);
        CircleImageView circleImageView = (CircleImageView)convertView.findViewById(R.id.circleView);



        Button btnAddToCart = (Button) convertView
                .findViewById(R.id.btnAddToCart);
        Button btnRelease = (Button) convertView
                .findViewById(R.id.release_now);
        final SafeDepositClass product = products.get(position);

        title.setText(product.getProject_name());
        name.setText(product.getDescription());

        //amt_paid.setText(product.getSafe_amt().toString());



        price.setText("Price: $" + product.getMilestone_amt().toString());

       // status.setText(product.getStatus());

        if(product.getStatus().equals("Paid")){
            btnAddToCart.setVisibility(View.GONE);
            btnRelease.setVisibility(View.VISIBLE);
        }
        if(product.getUrl()!= "null") {
            Picasso.with(AppController.getAppContext()).load(product.url).fit().into(circleImageView);
        }
        // user profile pic
        // image.setImageUrl(product.getImage(), imageLoader);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onAddToCartPressed(product);
            }
        });
        btnRelease.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onReleasedPressed(product);
            }
        });


        return convertView;
    }

    public interface ProductListAdapterListener {
        public void onAddToCartPressed(SafeDepositClass product);

        void onReleasedPressed(SafeDepositClass product);
    }

}