package com.mysurgery.screen.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysurgery.R;
import com.mysurgery.custom.CustomScreen;
import com.mysurgery.utils.FontUtils;
import com.mysurgery.utils.QTSConst;
import com.mysurgery.utils.QTSRun;

/**
 * Created by Administrator on 4/13/2017.
 */

public class ContactFragment extends CustomScreen {
    TextView toolbar_title, callSwitchBoard, callSurgeryUnit, callClinic ;
    TextView  lb1, lb2, lb3 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        this.initComponents(view);
        return view;
    }

    @Override
    protected void initComponents(View container) {
        toolbar_title = (TextView) container.findViewById(R.id.toolbar_title);
        callSwitchBoard = (TextView) container.findViewById(R.id.callSwitchBoard);
        callSurgeryUnit = (TextView) container.findViewById(R.id.callSurgeryUnit);
        callClinic = (TextView) container.findViewById(R.id.callClinic);
        lb1 = (TextView) container.findViewById(R.id.lb1);
        lb2 = (TextView) container.findViewById(R.id.lb2);
        lb3 = (TextView) container.findViewById(R.id.lb3);
        FontUtils.loadFont(getActivity(), QTSConst.FONT_SANSPRO_LIGHT);
        FontUtils.setFont(toolbar_title);
        FontUtils.setFonts((LinearLayout) container.findViewById(R.id.ll_group_contact));
        if (!QTSRun.getPhoneNumber1(getActivity()).isEmpty())
            callSwitchBoard.setText(String.valueOf("(02) "+QTSRun.getPhoneNumber1(getActivity())));
        else {
            lb1.setVisibility(View.GONE);
            callSwitchBoard.setVisibility(View.GONE);
        }

        if (!QTSRun.getPhoneNumber2(getActivity()).isEmpty())
            callSurgeryUnit.setText(String.valueOf("(02) "+QTSRun.getPhoneNumber2(getActivity())));
        else {
            lb2.setVisibility(View.GONE);
            callSurgeryUnit.setVisibility(View.GONE);
        }

        if (!QTSRun.getPhoneNumber3(getActivity()).isEmpty())
            callClinic.setText(String.valueOf("(02) "+QTSRun.getPhoneNumber3(getActivity())));
        else {
            lb3.setVisibility(View.GONE);
            callClinic.setVisibility(View.GONE);
        }
        callSwitchBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "02"+QTSRun.getPhoneNumber1(getActivity());
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel: 02" + phone.replaceAll("(02)","")));
                startActivity(callIntent);
            }
        });
        callSurgeryUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "02"+QTSRun.getPhoneNumber2(getActivity());
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel: 02" + phone.replaceAll("(02)","")));
                startActivity(callIntent);
            }
        });
        callClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "02"+QTSRun.getPhoneNumber3(getActivity());
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel: 02" + phone.replaceAll("(02)","")));
                startActivity(callIntent);
            }
        });
    }
}
