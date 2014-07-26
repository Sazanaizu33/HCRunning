package com.example.hcrunning.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinichi on 2014/07/24.
 */
public class HCRunningArrayAdapter extends ArrayAdapter<TextView> {
  List<TimeInterval> mIntervals;
  Activity mParentActivity;

    /**
     * Constructs this adapter
     * @param _context	A context to create the Views in
     * @param _resource	The resource ID of a TextView to use when instantiating Views
     * @param _items	The actual objects we're representing
     */
  public HCRunningArrayAdapter(Context _context,  ArrayList<TextView> _items, Activity activity) {
    super(_context, R.layout.rowlayout, _items);
    mParentActivity = activity;
    mIntervals = new ArrayList<TimeInterval>();
  }

  /**
   * Called when a component using this adapter (like a ListView) needs to get a View
   * that represents an object in the ArrayList.
   *
   * @param position		The position in the ArrayList of the object we want a representation of
   * @param convertView	A view that has already been instantiated but is destined to be garbage collected.
   * 							Use this for recycling Views and faster performance.
   * @param parent		The ViewGroup that the returned view will be a child of.
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

    TextView timeTextView = (TextView) rowView.findViewById(R.id.time);
    TimeInterval time = mIntervals.get(position);
    timeTextView.setText( time.toString() );

    return rowView;
  }

  public void add( long minutes, long seconds )
  {
    this.add(new TimeInterval(minutes, seconds) );
  }

  public void add( TimeInterval interval ) {
    mIntervals.add( interval );

    TextView textView = (TextView) mParentActivity.findViewById(R.id.textView);
    textView.setText(interval.toString());
    this.add(textView);
  }


  List<TimeInterval> getTimes() {
    return mIntervals;
  }
}