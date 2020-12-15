package co.edu.unal.tiendabd.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import co.edu.unal.tiendabd.R;
import co.edu.unal.tiendabd.ui.class_bd.Category;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.CategoryViewHolder>
        implements View.OnClickListener {
    Context context;
    LayoutInflater inflater;
    View v;
    private ArrayList<Category> categoryArrayList;
    public int mPosiciónMarcada = -1;
    private View.OnClickListener listener;

    public AdapterCategory(Context context, ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterCategory.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_categories, parent, false);
        CategoryViewHolder holder = new CategoryViewHolder(v);
        v.setOnClickListener(this::onClick);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Category category = categoryArrayList.get(position);
        Picasso.with(context).load(category.getImageCategory()).into(holder.imgUpload);
        holder.mName.setText(category.getName());
        holder.mId.setText(category.getId());
    }

    @Override
    public int getItemCount() {
        try {
            return categoryArrayList.size();
        } catch (Exception e) {
            return 0;
        }
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

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mName, mId, mCategory;
        ImageView imgUpload;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardviewCategory);
            mName = itemView.findViewById(R.id.txtNameValueCategory);
            mId = itemView.findViewById(R.id.txtIdValueCategory);
            imgUpload = itemView.findViewById(R.id.imgCardViewCategory);
        }
    }
}
