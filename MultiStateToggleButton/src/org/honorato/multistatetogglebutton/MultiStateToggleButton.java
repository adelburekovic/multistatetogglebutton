package org.honorato.multistatetogglebutton;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.multistatetogglebutton.R;

public class MultiStateToggleButton extends ToggleButton {

	private static final String TAG = "MultiStateToggleButton";
	List<Button> buttons;
	boolean mMultipleChoice = false;

	public MultiStateToggleButton(Context context) {
		super(context, null);
		if (this.isInEditMode()) {
			return;
		}
	}

	public MultiStateToggleButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		if (this.isInEditMode()) {
			return;
		}
		int[] set = {
				android.R.attr.entries
		};
		TypedArray a = context.obtainStyledAttributes(attrs, set);
		CharSequence[] texts = a.getTextArray(0);
		a.recycle();

		setElements(texts, new boolean[texts.length]);
	}

	public void enableMultipleChoice(boolean enable) {
		this.mMultipleChoice = enable;
	}

	public void setElements(CharSequence[] texts, boolean[] selected) {
		// TODO: Add an exception
		if (texts == null || texts.length < 2) {
			Log.d(TAG, "Minimum quantity: 2");
			return;
		}

		boolean enableDefaultSelection = true;
		if (selected == null || texts.length != selected.length) {
			Log.d(TAG, "Invalid selection array");
			enableDefaultSelection = false;
		}

		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout mainLayout = (LinearLayout) inflater.inflate(R.layout.view_multi_state_toggle_button, this, true);
		mainLayout.removeAllViews();

		this.buttons = new ArrayList<Button>();
		for (int i = 0; i < texts.length; i++) {
			Button b = null;
			if (i == 0) {
				b = (Button) inflater.inflate(R.layout.view_left_toggle_button, mainLayout, false);
			} else if (i == texts.length - 1) {
				b = (Button) inflater.inflate(R.layout.view_right_toggle_button, mainLayout, false);
			} else {
				b = (Button) inflater.inflate(R.layout.view_center_toggle_button, mainLayout, false);
			}
			b.setText(texts[i]);
			final int position = i;
			b.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					setValue(position);
				}

			});
			mainLayout.addView(b);
			if (enableDefaultSelection) {
				b.setSelected(selected[i]);
			}
			this.buttons.add(b);
		}
		mainLayout.setBackgroundResource(R.drawable.button_section_shape);
	}

	public void setElements(CharSequence[] elements) {
		int size = elements == null ? 0 : elements.length;
		setElements(elements, new boolean[size]);
	}

	public void setElements(List<?> elements) {
		int size = elements == null ? 0 : elements.size();
		setElements(elements, new boolean[size]);
	}

	public void setElements(List<?> elements, Object selected) {
		int size = 0;
		int index = -1;
		if (elements != null) {
			size = elements.size();
			index = elements.indexOf(selected);
		}
		boolean[] selectedArray = new boolean[size];
		if (index != -1 && index < size) {
			selectedArray[index] = true;
		}
		setElements(elements, new boolean[size]);
	}

	public void setElements(List<?> texts, boolean[] selected) {
		int size = texts == null ? 0 : texts.size();
		setElements(texts.toArray(new String[size]), selected);
	}

	public void setElements(int arrayResourceId, int selectedPosition) {
		// Get resources
		String[] elements = this.getResources().getStringArray(arrayResourceId);

		// Set selected boolean array
		int size = elements == null ? 0 : elements.length;
		boolean[] selected = new boolean[size];
		if (selectedPosition >= 0 && selectedPosition < size) {
			selected[selectedPosition] = true;
		}

		// Super
		setElements(elements, selected);
	}

	public void setElements(int arrayResourceId, boolean[] selected) {
		setElements(this.getResources().getStringArray(arrayResourceId), selected);
	}

	public void setButtonState(Button button, boolean selected) {
		if (button == null) {
			return;
		}
		button.setSelected(selected);
		if (selected) {
			button.setBackgroundResource(R.drawable.button_pressed);
			button.setTextAppearance(this.context, R.style.WhiteBoldText);
		} else {
			button.setBackgroundResource(R.drawable.button_not_pressed);
			button.setTextAppearance(this.context, R.style.BlackNormalText);
		}
	}

	public int getValue() {
		for (int i = 0; i < this.buttons.size(); i++) {
			if (buttons.get(i).isSelected()) {
				return i;
			}
		}
		return -1;
	}

	public void setValue(int position) {
		for (int i = 0; i < this.buttons.size(); i++) {
			if (mMultipleChoice) {
				if (i == position) {
					Button b = buttons.get(i);
					if (b != null) {
						setButtonState(b, !b.isSelected());
					}
					break;
				}
			} else {
				if (i == position) {
					setButtonState(buttons.get(i), true);
				} else if (!mMultipleChoice) {
					setButtonState(buttons.get(i), false);
				}
			}
			super.setValue(position);
		}
	}
}