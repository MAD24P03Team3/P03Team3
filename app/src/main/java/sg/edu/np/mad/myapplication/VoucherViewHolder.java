package sg.edu.np.mad.myapplication;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VoucherViewHolder extends RecyclerView.ViewHolder{
    TextView voucherName;
    public VoucherViewHolder(@NonNull View itemView) {
        super(itemView);
        voucherName = itemView.findViewById(R.id.vouchername);

        itemView.findViewById(R.id.elevatedButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Voucher button", "Voucher button onClick: ");
            }
        });
    }


}
