package my.com.maxmoney.scanpayment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

import my.com.maxmoney.scanpayment.R;
import my.com.maxmoney.scanpayment.common.AppData;
import my.com.maxmoney.scanpayment.model.ScanModel;

/**
 * Created by root on 17/01/2018.
 */

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ScanViewHolder> {

    private ArrayList<ScanModel> mData;

    private Context mContext;

    private OnScanViewClickListener mListener;

    public ScanAdapter(Context context, OnScanViewClickListener listener) {
        mData = new ArrayList<>();
        mContext = context;
        mListener = listener;
    }

    @Override
    public ScanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_holder_history, parent, false);
        return new ScanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScanViewHolder holder, int position) {

        ScanModel model = mData.get(position);

        holder.name.setText(model.getName());
        holder.id.setText(model.getTransactionId());
        holder.amount.setText(model.getAmount());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault());
        String date = simpleDateFormat.format(model.getTimestamp());
        holder.timestamp.setText(date);

        switch (model.getStatus()) {
            case 1:
                holder.status.setText("PAYMENT COMPLETED");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.colorStatusSuccess, null));
                break;
            case 99:
                holder.status.setText("PAYMENT INCOMPLETE");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.colorStatusFailed, null));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(ScanModel model) {
        mData.add(model);
        notifyDataSetChanged();
    }

    public ScanModel get(int pos) {
        return mData.get(pos);
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public interface OnScanViewClickListener {
        void onClick(int position);
    }

    class ScanViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView id;
        TextView timestamp;
        TextView status;
        TextView amount;

        public ScanViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            id = itemView.findViewById(R.id.tv_transId);
            timestamp = itemView.findViewById(R.id.tv_timestamp);
            status = itemView.findViewById(R.id.tv_status);
            amount = itemView.findViewById(R.id.tv_amount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
