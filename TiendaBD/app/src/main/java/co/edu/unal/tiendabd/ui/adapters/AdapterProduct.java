package co.edu.unal.tiendabd.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

import co.edu.unal.tiendabd.R;
import co.edu.unal.tiendabd.ui.class_bd.Product;


public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ProductViewHolder> implements View.OnClickListener {
    Context context;
    LayoutInflater inflater;
    private ArrayList<Product> categoryArrayList;
    int mPosiciónMarcada=-1;
    View v;
    private View.OnClickListener listener;
    public AdapterProduct(Context context,ArrayList<Product> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
        this.context=context;
        this.inflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterProduct.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_products, parent, false);
        ProductViewHolder holder = new ProductViewHolder(v);
        v.setOnClickListener(this::onClick);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product products = categoryArrayList.get(position);
        Picasso.with(context).load(products.getImageProduct()).into(holder.imgUpload);
        holder.mName.setText(products.getName());
        holder.mId.setText(products.getId());
        holder.mCategory.setText(products.getCategory());
        String av= String.valueOf(products.getAvailability());
        holder.mAvailability.setText(av);
    }

    @Override
    public int getItemCount() {
        try
        {
            return categoryArrayList.size();
        }catch(Exception e)
        {return 0;}


    }
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mName, mId, mCategory,mAvailability;
        ImageView imgUpload;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardviewProduct);
            mName = itemView.findViewById(R.id.txtNameValueProduct);
            mId =  itemView.findViewById(R.id.txtIdValueProduct);
            imgUpload = itemView.findViewById(R.id.imgCardViewProduct);
            mCategory= itemView.findViewById(R.id.txtCategoryProduct);
            mAvailability= itemView.findViewById(R.id.txtAvailabilityValueProduct);
        }
    }
}