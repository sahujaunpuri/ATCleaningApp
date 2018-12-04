package professional.service.corporate.com.atcleaningapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
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

import java.math.BigDecimal;
import java.util.List;

public  class ProductListAdapter1 extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Product> products;
    private ProductListAdapterListener listener;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ProductListAdapter1(Activity activity, List<Product> feedItems, ProductListAdapter1.ProductListAdapterListener listener) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.safe_deposite_amount, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView title = (TextView) convertView.findViewById(R.id.title_of_project);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        final EditText price = (EditText) convertView.findViewById(R.id.amt_paid_p);
        final TextView bid_amt = (TextView) convertView.findViewById(R.id.amt);
       // final TextView amt_paid = (TextView) convertView.findViewById(R.id.amt_paid);
        CircleImageView circleImageView = (CircleImageView)convertView.findViewById(R.id.circleView);

        Button btnAddToCart = (Button) convertView
                .findViewById(R.id.btnAddToCart);

        final Product product = products.get(position);

        title.setText(product.getName());
        name.setText(product.getDescription());
        if(product.getPrice().toString()!="null") {
            bid_amt.setText(product.getPrice().toString());
        }
        //amt_paid.setText(product.getSafe_amt().toString());

        if(product.getImage()!= "null") {
            Picasso.with(AppController.getAppContext()).load(product.image).fit().into(circleImageView);
        }
        btnAddToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String priceNew = price.getText().toString();
                String _id = (String.valueOf(products.get(position).getId()));
                String description = String.valueOf(products.get(position).getDescription());
                String  sku = (String.valueOf(products.get(position).getSku()));
                String name = (String.valueOf(products.get(position).getName()));
                String payee = (String.valueOf(products.get(position).getPayee()));
                BigDecimal price = products.get(position).getPrice();
                String price1 = String.valueOf(price);
                Double paid = Double.valueOf(priceNew);
                Double due = Double.valueOf( price1) - paid;
                // calculatePrice(price, priceNew);
                listener.onAddToCartPressed(_id,description,sku,name,String.valueOf(price),paid, payee);
            }
        });


        return convertView;
    }

    private void calculatePrice(BigDecimal price, String priceNew) {
        String price1 = String.valueOf(price);
        Double paid = Double.valueOf(priceNew);
        Double due = Double.valueOf( price1) - paid;

    }

    public interface ProductListAdapterListener {
        public void onAddToCartPressed(String _id, String description, String sku, String name, String price, Double paid, String payee);

        void onAccessPressed(String payee);
    }

}