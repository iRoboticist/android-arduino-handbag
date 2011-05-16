package com.rancidbacon.Handbag;

import com.rancidbacon.Handbag.R;
import com.rancidbacon.Handbag.HandbagActivity.ConfigMsg;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseActivity extends HandbagActivity {

	private InputController mInputController;

	public BaseActivity() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mAccessory != null) {
			showControls();
		} else {
			hideControls();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Simulate");
		menu.add("Quit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle() == "Simulate") {
			showControls();
		} else if (item.getTitle() == "Quit") {
			finish();
			System.exit(0);
		}
		return true;
	}

	protected void enableControls(boolean enable) {
		if (enable) {
			showControls();
		} else {
			hideControls();
		}
	}

	protected void hideControls() {
		setContentView(R.layout.no_device);
		mInputController = null;
	}

	
	void addButton() {
				
		LinearLayout layout = (LinearLayout) findViewById(R.id.mainstage);
		
		Button buttonView = new Button(this); 
        buttonView.setText("Button");

        layout.addView(buttonView);
	}
	
	void addLabel() {
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.mainstage);
		
		TextView label = new TextView(this); 
        label.setText("Label");

        layout.addView(label);
	}
	
	protected void showControls() {
		setContentView(R.layout.main);

		mInputController = new InputController(this);
		mInputController.accessoryAttached();
	}

	protected void handleConfigMessage(ConfigMsg c) {
		switch (c.getWidgetType()) {
			case 0x00:
				addButton();
				break;
				
			case 0x01:
				addLabel();
				break;
		} 
	}	
	
	protected void handleLightMessage(LightMsg l) {
		if (mInputController != null) {
			mInputController.setLightValue(l.getLight());
		}
	}

	protected void handleSwitchMessage(SwitchMsg o) {
		if (mInputController != null) {
			byte sw = o.getSw();
			if (sw >= 0 && sw < 4) {
				mInputController.switchStateChanged(sw, o.getState() != 0);
			}
		}
	}

}