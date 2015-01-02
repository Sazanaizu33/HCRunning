package com.example.hcrunning.app;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment implements View.OnClickListener {

  //! Listener for commanding the main activity to run with the intervals provided
  private RunIntervalsCreatedListener mIntervalsCreatedListener = null;

  //! The adapter that manages the time intervals in the display for the list view
  private HCRunningArrayAdapter mAdapter;

  //! The list view that displays the intervals for creation
  private ListView mListView;

  /** Must be empty.  First method called after creating the fragment is onAttach() */
  public CreateFragment() {}

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    try {
      mIntervalsCreatedListener = (RunIntervalsCreatedListener) activity;
    } catch (ClassCastException e) {
      Log.d("Warning", "Activity does not implement listener to handle interval creation.");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mAdapter = new HCRunningArrayAdapter( getActivity().getApplicationContext(), new ArrayList<TextView>(), getActivity() );
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_create, container, false);
    mListView = (ListView) view.findViewById(R.id.listview_countdown_create);
    mListView.setAdapter(mAdapter);
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView oldValue = mAdapter.getItem(position);

        updateFromNumberPicker(oldValue, position);
      }
    });

    Button addButton = (Button) view.findViewById(R.id.add_button);
    addButton.setOnClickListener(this);

    Button runButton = (Button) view.findViewById(R.id.run_button);
    runButton.setOnClickListener(this);
    return view;
  }

  public void onAddButton(View view){
    this.showNumberPicker();
  }

  public void onRunButtonClicked(View view) {
    if( mIntervalsCreatedListener == null )
      return;

    mIntervalsCreatedListener.onRunIntervalsCreated( this.mAdapter.getTimes() );
  }

  public void showNumberPicker() {
    final Dialog dialog = createNumberPickerDialog();

    final NumberPicker numberPickerForMinutes = (NumberPicker) dialog.findViewById(R.id.numberPickerMinutes);
    final NumberPicker numberPickerForSeconds = (NumberPicker) dialog.findViewById(R.id.numberPickerSeconds);

    Button onNumberPickerSetButton = (Button) dialog.findViewById(R.id.buttonSet);
    Button onNumberPickerCancelButton = (Button) dialog.findViewById(R.id.buttonCancel);

    onNumberPickerSetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        long minutesPart = numberPickerForMinutes.getValue();
        long secondsPart = numberPickerForSeconds.getValue() * 5;

        TimeInterval interval = new TimeInterval(minutesPart, secondsPart);

        //! \todo determine if this is a feature that is still desired
        //! \note used to display the last one added
        //! mCurrentTimeTextView.setText(interval.toString());

        mAdapter.add(interval);
        dialog.dismiss();
      }
    });

    onNumberPickerCancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    dialog.show();
  }

  public void updateFromNumberPicker( final TextView textViewInterval, final int positionInAdapter ) {
    final Dialog dialog = createNumberPickerDialog();

    TimeInterval interval = HCRunningToolkit.getTimeInterval(textViewInterval);
    final NumberPicker numberPickerForMinutes = (NumberPicker) dialog.findViewById(R.id.numberPickerMinutes);
    numberPickerForMinutes.setValue((int) (interval.Minutes));

    final NumberPicker numberPickerForSeconds = (NumberPicker) dialog.findViewById(R.id.numberPickerSeconds);
    numberPickerForSeconds.setValue((int)(interval.Seconds));

    Button onNumberPickerSetButton = (Button) dialog.findViewById(R.id.buttonSet);
    Button onNumberPickerCancelButton = (Button) dialog.findViewById(R.id.buttonCancel);

    onNumberPickerSetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        long minutesPart = numberPickerForMinutes.getValue();
        long secondsPart = numberPickerForSeconds.getValue() * 5;

        TimeInterval updatedInterval = new TimeInterval(minutesPart, secondsPart);
        updateInterval(updatedInterval, positionInAdapter);
        dialog.dismiss();
      }
    });

    onNumberPickerCancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    dialog.show();
  }

  private void updateInterval( TimeInterval newInterval, int position ) {
    this.mAdapter.replace(position, newInterval);
  }

  private Dialog createNumberPickerDialog() {
    final Dialog dialog = new Dialog(getActivity());
    dialog.setTitle("Set Timer");
    dialog.setContentView(R.layout.dialog);

    final NumberPicker numberPickerForMinutes = (NumberPicker) dialog.findViewById(R.id.numberPickerMinutes);
    numberPickerForMinutes.setMaxValue(59);
    numberPickerForMinutes.setMinValue(0);
    numberPickerForMinutes.setWrapSelectorWheel(true);

    final NumberPicker numberPickerForSeconds = (NumberPicker) dialog.findViewById(R.id.numberPickerSeconds);
    final String[] seconds = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};

    //! \todo deprecrated code that needs to be reviewed to ensure if it is ok to delete
//    final String[] seconds = new String[12];
//    for (int i = 0; i < seconds.length; i++) {
//      String number = Integer.toString(i * 5);
//      //seconds[i] = number.length() < 2 ? "0" + number : number;
//    }
    numberPickerForSeconds.setDisplayedValues(seconds);
    numberPickerForSeconds.setMaxValue(seconds.length - 1);
//    numberPickerForSeconds.setMaxValue(59);
    numberPickerForSeconds.setMinValue(0);
    numberPickerForSeconds.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        //picker.setValue((newVal < oldVal) ? (oldVal - 5) : (oldVal + 5));
        //newVal = Integer.parseInt(seconds[newVal]);
      }
    });
    numberPickerForSeconds.setWrapSelectorWheel(true);

    return dialog;
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.add_button:
        onAddButton(v);
        break;
      case R.id.run_button:
        onRunButtonClicked(v);
        break;
    }
  }
}
