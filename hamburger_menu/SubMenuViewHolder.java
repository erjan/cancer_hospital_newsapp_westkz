package kz.onko_zko.hospital.hamburger;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import kz.onko_zko.hospital.R;

public class SubMenuViewHolder extends ChildViewHolder {
    TextView subMenuTextView;
    View background;

    public SubMenuViewHolder(View itemView) {
        super(itemView);
        background = itemView;
        subMenuTextView = itemView.findViewById(R.id.subMenuTitle);
    }

    public void setSubMenuName(String name) {
        subMenuTextView.setText(name);
    }

    public void setBackground(boolean set) {
        background.setBackgroundColor((set)?0xFF197a9f:0xFF1c86ae);
    }
}
