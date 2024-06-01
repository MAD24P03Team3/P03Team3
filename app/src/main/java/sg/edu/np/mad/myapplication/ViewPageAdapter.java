package sg.edu.np.mad.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPageAdapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"Menu","Drinks","Praffles"};
    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){

            case 0:
                return new pratamenu();
            case 1:
                return new MenuDrinks();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
