package sheldon.com.android.gohomev4.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import sheldon.com.android.gohomev4.R;
import sheldon.com.android.gohomev4.activities.MainActivity;
import sheldon.com.android.gohomev4.adapters.DigitalOutputAdapter;
import sheldon.com.android.gohomev4.content.WidgetControl;
import sheldon.com.android.gohomev4.helper.ExpandableButton;

import static sheldon.com.android.gohomev4.adapters.DigitalOutputAdapter.WidgetViewHolder.widgetToggle;

public class ControlFragment extends Fragment {
    private ImageButton mExpandButtonDO;
    private RecyclerView mRecyclerViewDO;
    private static ShimmerFrameLayout digitalOutputShimmer;
    private static DigitalOutputAdapter digitalOutputAdapter;
    private static String currLabel, currColor, currValue;
    private static ArrayList<String> prevLabel, prevColor, prevValue;
    private static int positionDO = 0;
    public static boolean valueUpdated;

    private static ArrayList<WidgetControl> widgetsDO;

    public ControlFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CONTROL_FRAG", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CONTROL_FRAG", "onCreateView");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);

        mRecyclerViewDO = (RecyclerView) rootView.findViewById(R.id.rv_control);
        mExpandButtonDO = (ImageButton) rootView.findViewById(R.id.expand_button_do);
        digitalOutputShimmer = (ShimmerFrameLayout) rootView.findViewById((R.id.rv_container_do));

        widgetsDO = new ArrayList<>();
        prevLabel = new ArrayList<>();
        prevColor = new ArrayList<>();
        prevValue = new ArrayList<>();

        initiateEmptyWidgets();
        digitalOutputShimmer.startShimmerAnimation();

        digitalOutputAdapter = new DigitalOutputAdapter(widgetsDO);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        mRecyclerViewDO.setHasFixedSize(true);
        mRecyclerViewDO.setAdapter(digitalOutputAdapter);
        mRecyclerViewDO.setLayoutManager(llm);

        ExpandableButton.giveActionOnClick(mExpandButtonDO, mRecyclerViewDO);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CONTROL_FRAG", "onResume");
    }

    private static void initiateEmptyWidgets() {
        widgetsDO.add(new WidgetControl("Control", "bg-gray", "NA", MainActivity.starText));
        prevLabel.add("Control");
        prevColor.add("bg-gray");
        prevValue.add("NA");
    }

    public static void updateDataDO(JSONObject jsonObject) {
        update(widgetsDO, positionDO, jsonObject);

        digitalOutputAdapter.notifyDataSetChanged();

        positionDO++;
    }

    private static void update(ArrayList<WidgetControl> widgets, int position, JSONObject jsonObject) {
        // if widget at current position is exist, update the data
        if (!(position >= widgets.size())) {
            Log.d("CONTROL_FRAGMENT", "UPDATE");
            try {
                //if current data not equals prev data, update, then set curr data as prev data
                //else do nothing

                currLabel = jsonObject.getString("label");
                if (!currLabel.equals(prevLabel.get(position))) {
                    Log.d("CONTROL_FRAGMENT", "LABEL UPDATED");
                    widgets.get(position).setLabel(currLabel);
                    prevLabel.set(position, currLabel);
                }

                currColor = jsonObject.getString("color");
                if (!currColor.equals(prevColor.get(position))) {
                    Log.d("CONTROL_FRAGMENT", "COLOR UPDATED");
                    widgets.get(position).setColor(currColor);
                    prevColor.set(position, currColor);
                }

                currValue = jsonObject.getString("value");
                if (!currValue.equals(prevValue.get(position))) {
                    valueUpdated = true;
                    Log.d("CONTROL_FRAGMENT", "VALUE UPDATED");
                    widgets.get(position).setValue(currValue);
                    prevValue.set(position, currValue);

//                    widgets.get(position).setToggleState(currValue.equals("ON"));
                    widgetToggle.setChecked(currValue.equals("ON"));
                }

                widgets.get(position).setUpdateIndicator(MainActivity.starText);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else { // identified as new widget
            try {
                Log.d("CONTROL_FRAGMENT", "ADD");
                widgets.add(position, new WidgetControl(jsonObject.getString("label"),
                        jsonObject.getString("color"),
                        jsonObject.getString("value"),
                        MainActivity.starText));
                prevLabel.add(position, currLabel);
                prevColor.add(position, currColor);
                prevValue.add(position, currValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearDataSet() {
        prevLabel.clear();
        prevColor.clear();
        prevValue.clear();
    }

    public static void resetPosition() {
        positionDO = 0;
        digitalOutputShimmer.stopShimmerAnimation();
    }


    public static void removeUnusedD0Widgets() {
        for (int i = MainActivity.countUpdateDO; i < widgetsDO.size(); i++) {
            widgetsDO.remove(i);
        }

        digitalOutputAdapter.notifyItemRemoved(positionDO);
        digitalOutputAdapter.notifyItemRangeRemoved(positionDO, digitalOutputAdapter.getItemCount());
    }
}