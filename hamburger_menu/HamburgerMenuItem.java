package kz.onko_zko.hospital.hamburger;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class HamburgerMenuItem extends ExpandableGroup<SubMenuItem> {
    private int imageId;

    public HamburgerMenuItem(String title, List<SubMenuItem> items, int imageId) {
        super(title, items);
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }
}
