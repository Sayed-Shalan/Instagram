package instagram.shalan.net.sayed.instagram.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sotra on 8/6/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> FragmentList = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void  addFragment(Fragment fragment){
        FragmentList.add(fragment);

    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return FragmentList.size();
    }
}
