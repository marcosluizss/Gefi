package br.com.ml2s.gefi;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by marcossantos on 05/09/2014.
 */
public class Mask {

    public static TextWatcher number(final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String x = s.toString();
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }
                x = x.replaceAll("\\D", "");
                isUpdating = true;
                ediTxt.setText(x);
                ediTxt.setSelection(x.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

}
