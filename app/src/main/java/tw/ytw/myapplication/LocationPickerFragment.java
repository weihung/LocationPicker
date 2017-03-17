package tw.ytw.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by ytw on 2016/10/20.
 * init mode single 1, multiple 2.
 */

public class LocationPickerFragment extends Fragment {

    private static final String TAG = LocationPickerFragment.class.getSimpleName();
    private static final String KEY_TITLE = "title";
    private static final int SIZE_SMALL = 0;
    private static final int SIZE_MEDIUM = 1;
    private static final int SIZE_LARGE = 2;
    private static final int SIZE_EXTRA_LARGE = 3;

    private static int mGridItemSize = SIZE_LARGE;
    public List<String> mLocationPoints = new ArrayList<>();
    private GridLayout mContainer;

    private int mode;  //single 1, multiple 2.
    private int style_mode; // default 0, white 1;
    public static final int SINGLE_CHOOSE_MODE = 1;
    public static final int MULTIPLE_CHOOSE_MODE = 2;

    public static final int STYLE_MODE_DEFAULT = 0;
    public static final int STYLE_MODE_WHITE = 1;

    /**
     * @param mode single 1, multiple 2.
     */
    public void init(int mode) {
        init(mode, STYLE_MODE_DEFAULT);
    }

    public void init(int mode, int style_mode) {
        this.mode = mode;
        this.style_mode = style_mode;
    }

    /**
     * @param mode single 1, multiple 2.
     */
    public static LocationPickerFragment newInstance(int mode) {
        return newInstance(mode, STYLE_MODE_DEFAULT);
    }

    public static LocationPickerFragment newInstance(int mode, int style_mode) {
        LocationPickerFragment fragment = new LocationPickerFragment();
        fragment.init(mode, style_mode);
        return fragment;
    }

    public interface OnCompleteListener {
        void onComplete();
    }

    private OnCompleteListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.mListener = (OnCompleteListener) getActivity();
        } catch (final ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_locationpicker_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mContainer = (GridLayout) view.findViewById(R.id.GridViewContainer);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onComplete();
    }

    public int selectPlace(String name) {
        if (mode == SINGLE_CHOOSE_MODE) {
            boolean flag = false;
            boolean selected = false;
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                View view = mContainer.getChildAt(i);
                if (view.getTag().toString().compareTo(name) == 0) {
                    flag = true;
                    selected = !view.isSelected();
                    view.setSelected(selected);
                    //Toast.makeText(getActivity(), "巡邏點：" + name, Toast.LENGTH_SHORT).show();
                } else {
                    view.setSelected(false);
                }
            }
            if (flag) {
                return selected ? 1 : 0;
            } else {
                Toast.makeText(getActivity(), "找不到該地點", Toast.LENGTH_SHORT).show();
                return -1;
            }
        } else {
            View view = getView().findViewWithTag(name);
            if (view != null) {
                boolean selected = !view.isSelected();
                view.setSelected(selected);
                //Toast.makeText(getActivity(), "巡邏點：" + name, Toast.LENGTH_SHORT).show();
                return selected ? 1 : 0;
            } else {
                Toast.makeText(getActivity(), "找不到該地點", Toast.LENGTH_SHORT).show();
                return -1;
            }
        }
    }

    public void onClick(View view) {
        if (mode == SINGLE_CHOOSE_MODE) {
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                mContainer.getChildAt(i).setSelected(false);
            }
        }
        if (view != null) {
            boolean selected = view.isSelected();
            view.setSelected(!selected);
        }
    }

    public List<String> getSelectedItem() {
        List<String> list = new ArrayList<>();
        int count = mContainer.getChildCount();
        Log.d(TAG, "total item count:" + count);
        for (int i = 0; i < count; i++) {
            View child = mContainer.getChildAt(i);
            if (child.isSelected()) {
                list.add((String) child.getTag());
            }
        }
        return list;
    }

    private void updateData() {
        Log.d(TAG, "updateData:");
        if (null != mLocationPoints) {
            int count = mLocationPoints.size();
            if (count <= 2) {
                try {
                    mContainer.setColumnCount(5);
                    mContainer.setRowCount(1);
                    mGridItemSize = SIZE_EXTRA_LARGE;
                } catch (Exception e) {
                    Log.i("Ten", "error");
                }
            } else if (count > 2 && count <= 6) {
                mContainer.setColumnCount(7);
                mContainer.setRowCount(2);
                mGridItemSize = SIZE_LARGE;
            } else if (count > 6 && count <= 8) {
                mContainer.setColumnCount(9);
                mContainer.setRowCount(2);
                mGridItemSize = SIZE_MEDIUM;
            } else {
                mContainer.setColumnCount(11);
                mContainer.setRowCount(3);
                mGridItemSize = SIZE_SMALL;
            }
            Button view;
            for (String place : mLocationPoints) {
                AtomicReference<HashMap<Object, Object>> map = new AtomicReference<>(new HashMap<>());
                if (style_mode == 1) {
                    if (SIZE_EXTRA_LARGE == mGridItemSize) {
                        view = (Button) LayoutInflater.from(getActivity()).inflate(R.layout.patrol_place_list_item_extra_large_white, mContainer, false);
                    } else if (SIZE_SMALL == mGridItemSize) {
                        view = (Button) LayoutInflater.from(getActivity()).inflate(R.layout.patrol_place_list_item_small_white, mContainer, false);
                    } else {
                        view = (Button) LayoutInflater.from(getActivity()).inflate(R.layout.patrol_place_list_item_normal_white, mContainer, false);
                    }
                } else {
                    if (SIZE_EXTRA_LARGE == mGridItemSize) {
                        view = (Button) LayoutInflater.from(getActivity()).inflate(R.layout.patrol_place_list_item_extra_large, mContainer, false);
                    } else if (SIZE_SMALL == mGridItemSize) {
                        view = (Button) LayoutInflater.from(getActivity()).inflate(R.layout.patrol_place_list_item_small, mContainer, false);
                    } else {
                        view = (Button) LayoutInflater.from(getActivity()).inflate(R.layout.patrol_place_list_item_normal, mContainer, false);
                    }
                }
                view.setTag(place);
                view.setText(place);
                mContainer.addView(view);
                map.get().put(KEY_TITLE, place);

                Log.d(TAG, "add item:" + place);
            }
        } else {
            Log.d(TAG, "mPatrolPoints is null");
        }
    }

    public void updateNavigationList(String data) {
        final String[] elements = data.split(";");
        mLocationPoints.clear();
        Collections.addAll(mLocationPoints, elements);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateData();
            }
        });
    }
}


