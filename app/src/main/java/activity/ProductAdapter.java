package activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vihaan.shaktinewconcept.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
  //  private List<Product> productList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx) {
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
        View view = inflater.inflate(R.layout.layout_products, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the product of the specified position
       // Product product = productList.get(position);

        //binding the data with the viewholder views
       /* holder.textViewTitle.setText("Motor");
        holder.textViewShortDesc.setText("Submer....");
        holder.textViewRating.setText(String.valueOf(3));
        holder.textViewPrice.setText(String.valueOf(48000));

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.dt1));*/

       holder.rlvInfoContainerID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

              // Intent mIntent = new Intent(mCtx, SettingActivity.class);
               Intent mIntent = new Intent(mCtx, DeviceSettingActivity.class);
               mCtx.startActivity(mIntent);
           }
       });

    }


    @Override
    public int getItemCount() {
        return 30;
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        RelativeLayout rlvInfoContainerID;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            rlvInfoContainerID = itemView.findViewById(R.id.rlvInfoContainerID);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
