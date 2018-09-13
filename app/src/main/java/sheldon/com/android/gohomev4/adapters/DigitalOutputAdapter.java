package sheldon.com.android.gohomev4.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sheldon.com.android.gohomev4.R;
import sheldon.com.android.gohomev4.activities.MainActivity;
import sheldon.com.android.gohomev4.asynctask.LoopJ;
import sheldon.com.android.gohomev4.content.WidgetControl;
import sheldon.com.android.gohomev4.fragments.ControlFragment;

import static sheldon.com.android.gohomev4.adapters.DigitalOutputAdapter.WidgetViewHolder.widgetToggle;


public class DigitalOutputAdapter extends RecyclerView.Adapter<DigitalOutputAdapter.WidgetViewHolder> {


    public static class WidgetViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView widgetLabel;
        TextView widgetValue;
        TextView widgetUpdateIndicator;
        View switchContainer;
        public static Switch widgetToggle;

        public WidgetViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_control);
            widgetLabel = (TextView) itemView.findViewById(R.id.widget_label_control);
            widgetValue = (TextView) itemView.findViewById(R.id.widget_value_control);
            widgetUpdateIndicator = (TextView) itemView.findViewById(R.id.widget_update_indicator_control);
            widgetToggle = (Switch) itemView.findViewById(R.id.widget_toggle_control);
            switchContainer = (View) itemView.findViewById(R.id.container_switch);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // item clicked
                    Toast.makeText(v.getContext(), "Position:" +
                            Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private ArrayList<WidgetControl> widgets;

    public DigitalOutputAdapter(ArrayList<WidgetControl> widgets) {
        this.widgets = widgets;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WidgetViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_control, viewGroup, false);
        WidgetViewHolder wvh = new WidgetViewHolder(v);
        return wvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final WidgetViewHolder widgetViewHolder, @SuppressLint("RecyclerView") final int position) {
        Log.d("OnBindControl", String.valueOf(position));

        // Label
        widgetViewHolder.widgetLabel.setText(widgets.get(position).getLabel());
        // Color
        widgetViewHolder.cv.setCardBackgroundColor(widgets.get(position).cvColor());
        widgetViewHolder.switchContainer.setBackgroundColor(widgets.get(position).iconColor());
        // Value
        widgetViewHolder.widgetValue.setText(widgets.get(position).getValue());
        // Update Indicator
        widgetViewHolder.widgetUpdateIndicator.setText(widgets.get(position).getUpdateIndicator());

//        if (!LoopJ.isChangingSwitchState) {
//            widgetToggle.setChecked(widgets.get(position).getToggleState());
//        }


        widgetToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int switchID = position + 1;
                int switchState = isChecked ? 1 : 0;
                Log.d("DO_ADAPTER_CHECKED", "onCheckedChanged: switchID " + switchID);
                Log.d("DO_ADAPTER_CHECKED", "onCheckedChanged: switchState " + switchState);

                LoopJ.setSwitchState(MainActivity.username, switchID, switchState);
            }
        });
    }

    @Override
    public int getItemCount() {
        return widgets.size();
    }
}