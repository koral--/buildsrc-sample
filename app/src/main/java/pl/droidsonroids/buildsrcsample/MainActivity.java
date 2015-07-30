package pl.droidsonroids.buildsrcsample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import pl.droidsonroids.domainnameutils.TldList;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Spinner spinner = (Spinner) findViewById(R.id.spinner_tld);
		spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, TldList.getTldList()));
	}
}
