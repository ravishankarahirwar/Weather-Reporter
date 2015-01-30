package weatherreporter.util;

/**
 * Created by ravi on 28/1/15.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import weatherreporter.com.weatherreporter.R;
import weatherreporter.dataclasses.ForecastData;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    public ArrayList<ForecastData> listForecastData;
    Typeface mHeaderFont;
    public ExpandableListAdapter(Context context, ArrayList<ForecastData> list) {
        this._context = context;
        mHeaderFont = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        listForecastData=list;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listForecastData.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ForecastData childText = (ForecastData) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        ImageView iconId = (ImageView) convertView
                .findViewById(R.id.iconId);
        iconId.setImageResource(childText.imageId);



        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.description);
        txtListChild.setText(childText.description.substring(0,1).toUpperCase() + childText.description.substring(1));
        txtListChild.setTypeface(mHeaderFont);


        TextView morningTemprature = (TextView) convertView
                .findViewById(R.id.morningTemprature);
        morningTemprature.setTypeface(mHeaderFont);
        morningTemprature.setText("Morning : "+childText.morn+_context.getString(R.string.unit_temperature));


        TextView dayTemprature = (TextView) convertView
                .findViewById(R.id.dayTemprature);
        dayTemprature.setText("Day : "+childText.day+_context.getString(R.string.unit_temperature));
        dayTemprature.setTypeface(mHeaderFont);

        TextView eveningdayTemprature = (TextView) convertView
                .findViewById(R.id.eveningdayTemprature);
        eveningdayTemprature.setText("Evening : "+childText.eve+_context.getString(R.string.unit_temperature));
        eveningdayTemprature.setTypeface(mHeaderFont);

        TextView nightdayTemprature = (TextView) convertView
                .findViewById(R.id.nightdayTemprature);
        nightdayTemprature.setText("Night : "+childText.night+_context.getString(R.string.unit_temperature));
        nightdayTemprature.setTypeface(mHeaderFont);





        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listForecastData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listForecastData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ForecastData headerTitle = (ForecastData) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.dt);
        lblListHeader.setTypeface(mHeaderFont);


        TextView minTemp = (TextView) convertView
                .findViewById(R.id.minTemp);
        minTemp.setTypeface(null, Typeface.BOLD);
        minTemp.setText(headerTitle.getMinimumTemperature()+_context.getString(R.string.unit_temperature));
        minTemp.setTypeface(mHeaderFont);

        TextView maxTemp = (TextView) convertView
                .findViewById(R.id.maxTemp);
        maxTemp.setTypeface(null, Typeface.BOLD);
        maxTemp.setText(headerTitle.getMaximumTemperature()+_context.getString(R.string.unit_temperature));
        maxTemp.setTypeface(mHeaderFont);



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}