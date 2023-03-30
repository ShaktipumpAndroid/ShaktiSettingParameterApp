package activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shakti.shaktinewconcept.R;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
  //  private List<Product> productList;

    //getting the context and product list with constructor
    public SettingAdapter(Context mCtx) {
        this.mCtx = mCtx;

    }
/*
 public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }
*/

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_settings, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        //getting the product of the specified position
       // Product product = productList.get(position);
        //binding the data with the viewholder views
       /* holder.textViewTitle.setText("Motor");
        holder.textViewShortDesc.setText("Submer....");
        holder.textViewRating.setText(String.valueOf(3));
        holder.textViewPrice.setText(String.valueOf(48000));
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.dt1));*/



        holder.txtSetAndGetID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ss = v.getTag().toString();

                if(ss.equalsIgnoreCase("Get"))
                {
                    holder.txtSetAndGetID.setText("Set");
                    holder.txtSetAndGetID.setTag("Set");
                }
                else
                {
                    holder.txtSetAndGetID.setText("Get");
                    holder.txtSetAndGetID.setTag("Get");
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return 20;
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView txtSetAndGetID;
       // ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            txtSetAndGetID = itemView.findViewById(R.id.txtSetAndGetID);

        }
    }
}
