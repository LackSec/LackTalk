package org.lacksec.lacktalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private String[] values;

  public MySimpleArrayAdapter(Context context, String[] values) {
    super(context, R.layout.rowlayout, values);
    this.context = context;
    this.values = values;
  }
  
  public boolean updateValues(String[] val)
  {
	  values = val;
	  
	  return true;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.label);
    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    textView.setText(values[position]);
    // Change the icon for Windows and iPhone
    String s = values[position];
    if (s.startsWith("iPhone")) {
      imageView.setImageResource(R.drawable.no);
    } else {
      imageView.setImageResource(R.drawable.yes);
    }

    return rowView;
  }
} 