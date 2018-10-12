package kz.onko_zko.hospital.hamburger;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class HamburgerMenuModel {
    public String CategoryName;
    public List<SubMenuModel> SubCategories;
    public int menuIconDrawable;
    public Fragment fragment;

    public HamburgerMenuModel(String categoryName, int menuIconDrawable, Fragment fragment) {
        this.CategoryName = categoryName;
        this.fragment = fragment;
        this.menuIconDrawable = menuIconDrawable;
        this.SubCategories = new ArrayList<>();
    }

    public HamburgerMenuModel(String categoryName,int menuIconDrawable, ArrayList<SubMenuModel> subMenu) {
        this.CategoryName = categoryName;
        this.SubCategories = new ArrayList<>();
        this.SubCategories.addAll(subMenu);
        this.menuIconDrawable = menuIconDrawable;
    }

    public static class SubMenuModel {
        public String subMenuTitle;
        public Fragment fragment;

        public SubMenuModel(String subMenuTitle, Fragment fragment) {
            this.subMenuTitle = subMenuTitle;
            this.fragment = fragment;
        }
    }
}
