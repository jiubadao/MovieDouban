package mvp.wyyne.douban.moviedouban.utils;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by XXW on 2017/6/12.
 */

public class SwipeRefreshUtils {

    public static void init(SwipeRefreshLayout refreshLayout, SwipeRefreshLayout.OnRefreshListener listener) {

        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(listener);

    }
}
