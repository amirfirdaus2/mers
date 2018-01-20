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

    public ScanAdapter(Context context) {
        mData = new ArrayList<>();
        mContext = context;
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        String date = simpleDateFormat.format(model.getTimestamp());
        holder.timestamp.setText(date);

        switch (model.getStatus()) {
            case AppData.STATUS_SCAN:
                holder.status.setText(R.string.scan_status_scan);
                holder.status.setTextColor(mContext.getResources().getColor(R.color.colorStatusScan, null));
                break;
            case AppData.STATUS_PAYMENT_COMPLETE:
                holder.status.setText(R.string.scan_status_payment_complete);
                holder.status.setTextColor(mContext.getResources().getColor(R.color.colorStatusSuccess, null));
                break;
            case AppData.STATUS_PAYMENT_FAILED:
                holder.status.setText(R.string.scan_status_payment_failed);
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

    class ScanViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView id;
        TextView timestamp;
        TextView status;

        public ScanViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            id = itemView.findViewById(R.id.tv_transId);
            timestamp = itemView.findViewById(R.id.tv_timestamp);
            status = itemView.findViewById(R.id.tv_status);
        }
    }
}
