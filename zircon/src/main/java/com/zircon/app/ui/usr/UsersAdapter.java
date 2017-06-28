package com.zircon.app.ui.usr;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.User;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.Utils;
import com.zircon.app.utils.ui.AbsSearchListAdapter;

import java.util.ArrayList;

/**
 * Created by jikoobaruah on 30/04/17.
 */

public class UsersAdapter extends AbsSearchListAdapter<User, UsersAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setUser(getItem(position));
    }

    @Override
    protected ArrayList<User> getFilteredList(String query) {
        ArrayList<User> filteredList = new ArrayList<>();
        if (query == null || query.trim().length() == 0)
            filteredList = masterItems;
        else {
            String FirstName, LastName;
            int size = masterItems.size();
            for (int i = 0; i < size; i++) {
                FirstName = masterItems.get(i).firstname;
                LastName = (masterItems.get(i).lastname == null ? "" : masterItems.get(i).lastname);
                if ((FirstName.toLowerCase().contains(query.toLowerCase())) || (LastName.toLowerCase().contains(query.toLowerCase()))) {
                    filteredList.add(masterItems.get(i));
                }
            }
        }
        return filteredList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImageView;
        TextView nameTextView;
        TextView descTextView;
        TextView emailTextView;
        TextView phoneTextView;
        LinearLayout container;


        public ViewHolder(View itemView) {
            super(itemView);

            profileImageView = (ImageView) itemView.findViewById(R.id.iv_profile);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            descTextView = (TextView) itemView.findViewById(R.id.tv_desc);
            emailTextView = (TextView) itemView.findViewById(R.id.tv_email);
            phoneTextView = (TextView) itemView.findViewById(R.id.tv_phone);
            container = (LinearLayout) itemView.findViewById(R.id.ll_content);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationUtils.navigateToUserDetailPage((BaseActivity) v.getContext(), ((ColorDrawable) profileImageView.getBackground()).getColor(), getItem(getAdapterPosition()));
                }
            });

        }

        public void setUser(User user) {

            Picasso.with(profileImageView.getContext()).load(user.profilePic).placeholder(Utils.getTextDrawable(profileImageView.getContext(), user.firstname)).fit().into(profileImageView);

            profileImageView.setBackgroundColor(Utils.getRandomMaterialColor(profileImageView.getContext(), "300"));

            String name = user.getFullName();

            nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, nameTextView.getResources().getDimension(R.dimen.text_size_large));
            if (query != null && query.trim().length() > 0) {
                Spannable wordtoSpan = new SpannableString(name);
                int spanStartIndex = name.toLowerCase().indexOf(query.toLowerCase());
                if (spanStartIndex >= 0) {
                    wordtoSpan.setSpan(new ForegroundColorSpan(nameTextView.getResources().getColor(R.color.colorAccent)), spanStartIndex, spanStartIndex + query.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), spanStartIndex, spanStartIndex + query.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                nameTextView.setText(wordtoSpan);
            } else {
                nameTextView.setText(name);
            }

            descTextView.setText(user.description);
            emailTextView.setText(user.email);
            phoneTextView.setText(user.contactNumber);
        }

    }

}
