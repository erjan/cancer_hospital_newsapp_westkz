package kz.onko_zko.hospital.hamburger;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import kz.onko_zko.hospital.R;

public class HamburgerMenuViewHolder extends GroupViewHolder {
    private TextView titleName;
    private ImageView arrow;
    private ImageView icon;

    public HamburgerMenuViewHolder(View itemView) {
        super(itemView);
        titleName = itemView.findViewById(R.id.menu_item_name);
        arrow = itemView.findViewById(R.id.menu_item_arrow);
        icon = itemView.findViewById(R.id.menu_item_icon);
    }

    public void setGenreTitle(Context context, ExpandableGroup title, boolean changeColor) {
        if (title instanceof HamburgerMenuItem) {
            titleName.setText(title.getTitle());
            if (((HamburgerMenuItem) title).getImageId()!= -1){
                icon.setImageResource(((HamburgerMenuItem)title).getImageId());
            }
            itemView.setBackgroundColor((changeColor)?0xFF197a9f:0xFF1c86ae);
            if(title.getItemCount() != 0) {
                arrow.setVisibility(View.VISIBLE);
            } else {
                arrow.setVisibility(View.GONE);
            }
        }
    }
}
