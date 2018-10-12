package kz.onko_zko.hospital.hamburger;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import kz.onko_zko.hospital.R;

public class RecyclerAdapter extends ExpandableRecyclerViewAdapter<HamburgerMenuViewHolder, SubMenuViewHolder> {
    private Context context;
    private ItemClickChild mListener;
    private String selectedItemParent = "";
    private String selectedItemChild = "";

    public RecyclerAdapter(Context context, List<? extends ExpandableGroup> groups, Activity activity) {
        super(groups);
        this.context = context;
        mListener = (ItemClickChild) activity;
    }

    @Override
    public HamburgerMenuViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.menu_item, parent, false);
        HamburgerMenuViewHolder holder = new HamburgerMenuViewHolder(view);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public SubMenuViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.submenu_item, parent, false);
        SubMenuViewHolder holder = new SubMenuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindChildViewHolder(SubMenuViewHolder holder, int flatPosition, ExpandableGroup group, final int childIndex) {
        final SubMenuItem subTitle = ((HamburgerMenuItem) group).getItems().get(childIndex);
        final HamburgerMenuItem menu = (HamburgerMenuItem)group;
        holder.setSubMenuName(subTitle.getName());
        holder.setBackground(selectedItemChild.equals(subTitle.getName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItemParent = menu.getTitle();
                selectedItemChild = subTitle.getName();
                mListener.onMenuItemClick(subTitle.getName());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(final HamburgerMenuViewHolder holder, final int flatPosition, ExpandableGroup group) {
        final HamburgerMenuItem menu = (HamburgerMenuItem) group;
        holder.setGenreTitle(context, group, selectedItemParent.equals(menu.getTitle()));
        final String name = group.getTitle();
        if(group.getItemCount() == 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItemParent = menu.getTitle();
                    selectedItemChild = "";
                    mListener.onMenuItemClick(name);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void setItemParent(String itemName) {
        this.selectedItemParent = itemName;
        this.selectedItemChild = "";
        notifyDataSetChanged();
    }

    public interface ItemClickChild{
        void onMenuItemClick(String itemString);
    }
}
