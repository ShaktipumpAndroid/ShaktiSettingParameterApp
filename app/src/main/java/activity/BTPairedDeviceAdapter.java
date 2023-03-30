package activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.shakti.shaktinewconcept.R;

import java.util.List;

import webservice.WebURL;


public class BTPairedDeviceAdapter extends RecyclerView.Adapter<BTPairedDeviceAdapter.ViewHolder> {

    private Context mContext;

    private List mDeviceNameList;
    private List mDeviceMACAddressList;
    //private HomeUserNameClickListener mHomeUserNameClickListener;

    public BTPairedDeviceAdapter(Context mContext, List mDeviceNameList, List mDeviceMACAddressList) {

        this.mDeviceNameList = mDeviceNameList;
        this.mDeviceMACAddressList = mDeviceMACAddressList;
        // this.mHomeUserNameClickListener = mHomeUserNameClickListener;
        this.mContext = mContext;

    }

    public BTPairedDeviceAdapter(String SSS, Context mContext) {
        // this.galleryModelsList = galleryModelsList;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.bt_paired_device_item, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // holder.rlvHomeMainView.getLayoutParams().width = Validation.getDeviceHeightWidth(mContext, true)/2;
        //  holder.rlvHomeMainView.getLayoutParams().height = Validation.getDeviceHeightWidth(mContext, false)/3+30;

        //changeButtonVisibility(false,0.5f, holder);
        holder.txtBTNameID.setText("Name: "+mDeviceNameList.get(position).toString());
        holder.txtBTMACAddressID.setText("MAC Address: "+mDeviceMACAddressList.get(position).toString());

        holder.cardMainViewMyNotifyID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WebURL.BT_DEVICE_NAME = mDeviceNameList.get(position).toString();
                WebURL.BT_DEVICE_MAC_ADDRESS = mDeviceMACAddressList.get(position).toString();

               // Intent intent = new Intent(mContext, GetBTDATAListActivity.class);
                /*Intent intent = new Intent(mContext, BTDemoBigDataActivity.class);
                intent.putExtra("BtNameHead",mDeviceNameList.get(position).toString());
                intent.putExtra("BtMacAddressHead",mDeviceMACAddressList.get(position).toString());
                mContext.startActivity(intent);*/

                ((Activity)mContext).finish();

            /*  Constant.BT_DEVICE_NAME_CONSTANT = mDeviceNameList.get(position).toString();
              Constant.BT_DEVICE_ADDRESS_CONSTANT = mDeviceMACAddressList.get(position).toString();

                ((Activity)mContext).finish();*/

            //    mContext.startActivity(new Intent(mContext, GetBTDATAListActivity.class));

              //  changeButtonVisibility(true,1.0f, holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (mDeviceMACAddressList != null && mDeviceMACAddressList.size() > 0)
            return mDeviceMACAddressList.size();
        else
            return 0;
         // return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtBTNameID, txtBTMACAddressID;

        public CardView cardMainViewMyNotifyID;


        public ViewHolder(View v) {

            super(v);


            txtBTNameID = (TextView) v.findViewById(R.id.txtBTNameID);
            txtBTMACAddressID = (TextView) v.findViewById(R.id.txtBTMACAddressID);
            cardMainViewMyNotifyID = (CardView) v.findViewById(R.id.cardMainViewMyNotifyID);

        }
    }



   /* private void changeButtonVisibility(boolean state, float alphaRate, BTPairedDeviceAdapter.ViewHolder holder) {
        holder.txtSetBTNID.setEnabled(state);
        holder.txtSetBTNID.setAlpha(alphaRate);
    }*/
}


