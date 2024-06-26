package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vihaan.shaktinewconcept.R;

import java.util.ArrayList;
import java.util.List;

import activity.BeanVk.MotorParamListModel;

public class ComAdapter extends RecyclerView.Adapter<ComAdapter.ViewHolder> {
    Context mContext;
    private List<MotorParamListModel.Response> cmponentList;
    private final List<MotorParamListModel.Response> arSearch;
    TextView noDataFound;

    private ItemclickListner itemclickListner;

    public ComAdapter(Context context, List<MotorParamListModel.Response> listdata, TextView noDataFound) {
        cmponentList = listdata;
        mContext = context;
        this.arSearch = new ArrayList<>();
        this.arSearch.addAll(listdata);
        this.noDataFound = noDataFound;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.comp_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MotorParamListModel.Response response = cmponentList.get(position);
         holder.title.setText(response.getParametersName());
         holder.editTextValue.setText(String.valueOf(response.getpValue() * response.getFactor()));
         holder.getBtn.setOnClickListener(v -> itemclickListner.getBtnMethod(response,position));
         holder.setBtn.setOnClickListener(v -> itemclickListner.setBtnMethod(response,position));
    }

    @Override
    public int getItemCount() {
        return cmponentList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lvlMainItemViewID;
        Button getBtn, setBtn;
        TextView title;
        EditText editTextValue;

        public ViewHolder(View itemView) {
            super(itemView);

            lvlMainItemViewID = itemView.findViewById(R.id.lvlMainItemViewID);
            getBtn = itemView.findViewById(R.id.getBtn);
            setBtn = itemView.findViewById(R.id.setBtn);
            title = itemView.findViewById(R.id.title);
            editTextValue = itemView.findViewById(R.id.editTextValue);


        }
    }

    public void EditItemClick(ItemclickListner response) {
        try {
            itemclickListner = response;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface ItemclickListner {
        void getBtnMethod(MotorParamListModel.Response response, int pos);
        void setBtnMethod(MotorParamListModel.Response response,  int pos);

    }
}