package com.mysurgery.screen.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mysurgery.AnalyticsApplication;
import com.mysurgery.R;
import com.mysurgery.object.HospitalObject;
import com.mysurgery.utils.QTSConst;
import com.mysurgery.utils.QTSRun;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final int DATE_DIALOG_ID = 999;
    TextView lbDate, lbQuestion;
    ImageView ivTopQuset;
    Spinner spnTime;
    ListView listAnswers;
    Button btnNext, btnPre;
    AnswersListAdapter adapter;
    String[] listQuestions;
    int[] mImages = {R.drawable.ic_drive, R.drawable.ic_procedure, R.drawable.ic_procedure, R.drawable.ic_calendar};
    List<HospitalObject> listHospital = null;
    String[] answerChoose = {"", "", "", ""};

    private int posList = 0, posChoose1 = 0, posChoose2 = 0;
    private int year;
    private int month;
    private int day;
    Tracker mTracker;

    private String mContact = "", mHopital = "", mlong = "", mlat = "", mPhone1 = "", mPhone2 = "", mPhone3 = "";
    private int mPosition1 = 0, mPosition2 = 0;
    private String mTypeSurgery = "", mDateSurgery = "";
    private String mDateOfSurgery = "";
    private int mPosDate = 0, mPosSpinner = 0;
    private String mDates = "";
    private String mProceduce = "", mUrlHttp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTopQuset = (ImageView) findViewById(R.id.ivTopQuset);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnPre = (Button) findViewById(R.id.btnPrevious);
        lbDate = (TextView) findViewById(R.id.lbDate);
        spnTime = (Spinner) findViewById(R.id.spnTime);
        lbQuestion = (TextView) findViewById(R.id.lbQuestion);
        listAnswers = (ListView) findViewById(R.id.listAnswers);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        posChoose1 = QTSRun.getPositionChoose1(getApplicationContext());
        posChoose2 = QTSRun.getPositionChoose2(getApplicationContext());
        listQuestions = getResources().getStringArray(R.array.arr_questions);

        mPosDate = QTSRun.getPositionDate(getApplicationContext());
        mDates = QTSRun.getDates(getApplicationContext());
        mPosSpinner = QTSRun.getPositionSpinner(getApplicationContext());
        mHopital = QTSRun.getHospital(getApplicationContext());
        mTypeSurgery = QTSRun.getTypeSurgery(getApplicationContext());
        mProceduce = QTSRun.getProcedure(getApplicationContext());
        mDateOfSurgery = QTSRun.getDateOfSurgery(getApplicationContext());
        mDateSurgery = QTSRun.getDateSurgery(getApplicationContext());
        mContact = QTSRun.getContactHop(getApplicationContext());
        mPhone1 = QTSRun.getPhoneNumber1(getApplicationContext());
        mPhone2 = QTSRun.getPhoneNumber2(getApplicationContext());
        mPhone3 = QTSRun.getPhoneNumber3(getApplicationContext());
        mlat = QTSRun.getLatitude(getApplicationContext());
        mlong = QTSRun.getLongitude(getApplicationContext());
        mPosition1 = QTSRun.getPositionChoose1(getApplicationContext());
        mPosition2 = QTSRun.getPositionChoose2(getApplicationContext());
        mUrlHttp = QTSRun.getUrlHPT(getApplicationContext());

        QTSRun.setFontTV(getApplicationContext(), lbDate, QTSConst.FONT_LATO_REGULAR);
        QTSRun.setFontTV(getApplicationContext(), lbQuestion, QTSConst.FONT_SANSPRO_SEMIBOLD);
        QTSRun.setLayoutView(btnNext, QTSRun.GetWidthDevice(getApplicationContext()) / 4, QTSRun.GetWidthDevice(getApplicationContext()) / 4 * 72 / 186);
        QTSRun.setLayoutView(btnPre, QTSRun.GetWidthDevice(getApplicationContext()) / 4, QTSRun.GetWidthDevice(getApplicationContext()) / 4 * 72 / 186);

        listHospital = new ArrayList<HospitalObject>();
        Gson gson = new Gson();
//        Log.e("TagFragment","read file in asset folder:"+QTSRun.loadJSONFromAsset(MainActivity.this));
        try {
            JSONArray m_jArray = new JSONArray(QTSRun.loadJSONFromAsset(MainActivity.this));
            for (int i = 0; i < m_jArray.length(); i++) {
                HospitalObject itemObject = gson.fromJson(m_jArray.getJSONObject(i).toString(), HospitalObject.class);
                listHospital.add(itemObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapterSpn = new ArrayAdapter<String>(this, R.layout.simple_row_item_sp, getResources().getStringArray(R.array.arr_itemSpinner));
        spnTime.setAdapter(adapterSpn);
        getDefaultInfor();

        spnTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mDates = String.valueOf(parent.getSelectedItem());
                mPosSpinner = pos;
//                QTSRun.setDates(getApplicationContext(), String.valueOf(parent.getSelectedItem()));
//                QTSRun.setPositionSpinner(getApplicationContext(), pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnNext.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        lbDate.setOnClickListener(this);
        init(posList, posChoose1, posChoose2);
    }

    private void init(int positionQt, int pos1, int pos2) {
        if (positionQt == 0) {
            btnPre.setVisibility(View.GONE);
        } else {
            btnPre.setVisibility(View.VISIBLE);
            if (positionQt == 3) {
                if (mPosDate == 0) {
                    lbDate.setVisibility(View.VISIBLE);
                    spnTime.setVisibility(View.GONE);

                } else if (mPosDate == 1) {
                    spnTime.setVisibility(View.VISIBLE);
                    lbDate.setVisibility(View.GONE);
                } else {
                    lbDate.setVisibility(View.GONE);
                    spnTime.setVisibility(View.GONE);
                }
            } else {
                lbDate.setVisibility(View.GONE);
                spnTime.setVisibility(View.GONE);
            }
        }
        btnNext.setEnabled(false);
        lbQuestion.setText(listQuestions[positionQt]);
        ivTopQuset.setImageResource(mImages[positionQt]);
        adapter = new AnswersListAdapter(getApplicationContext(), listHospital, positionQt, pos1, pos2);
        listAnswers.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView answerStr = (TextView) view.findViewById(R.id.tvAnswer);
                TextView urlStr = (TextView) view.findViewById(R.id.tvUrl);
                answerChoose[posList] = answerStr.getText().toString();
                btnNext.setEnabled(true);
                if (posList == 0) {
                    mContact = listHospital.get(position).getContacts();
                    mHopital = listHospital.get(position).getHospital();
                    mlong = listHospital.get(position).getLongitude();
                    mlat = listHospital.get(position).getLatitude();
                    mPhone1 = listHospital.get(position).getPhone_number1();
                    mPhone2 = listHospital.get(position).getPhone_number2();
                    mPhone3 = listHospital.get(position).getPhone_number3();
                    mPosition1 = position;
//                    QTSRun.setPhoneNumber1(getApplicationContext(),listHospital.get(position).getPhone_number1());
//                    QTSRun.setPhoneNumber2(getApplicationContext(),listHospital.get(position).getPhone_number2());
//                    QTSRun.setPhoneNumber3(getApplicationContext(),listHospital.get(position).getPhone_number3());
//                    QTSRun.setContactHop(getApplicationContext(),listHospital.get(position).getContacts());
//                    QTSRun.setHospital(getApplicationContext(),listHospital.get(position).getHospital());
//                    QTSRun.setLatitude(getApplicationContext(),listHospital.get(position).getLatitude());
//                    QTSRun.setLongitude(getApplicationContext(),listHospital.get(position).getLongitude());
                    posChoose1 = position;
//                    QTSRun.setPositionChoose1(getApplicationContext(),position);
                } else if (posList == 1) {
                    posChoose2 = position;
                    mPosition2 = position;
                    mTypeSurgery = listHospital.get(posChoose1).getTypes().get(position).getType_name();
                    mDateSurgery = listHospital.get(posChoose1).getTypes().get(position).getDays_type();
//                    QTSRun.setPositionChoose2(getApplicationContext(),position);
//                    QTSRun.setTypeSurgery(getApplicationContext(),listHospital.get(posChoose1).getTypes().get(position).getType_name());
//                    QTSRun.setDateSurgery(getApplicationContext(), listHospital.get(posChoose1).getTypes().get(position).getDays_type());
                } else if (posList == 3) {
                    mDateOfSurgery = listHospital.get(position).getAppointment()[position];
                    mPosDate = position;
//                    QTSRun.setDateOfSurgery(getApplicationContext(), listHospital.get(position).getAppointment()[position]);
//                    QTSRun.setPositionDate(getApplicationContext(),position);
                    if (position == 0) {
                        lbDate.setVisibility(View.VISIBLE);
                        spnTime.setVisibility(View.GONE);
                        mDates = lbDate.getText().toString();
//                        QTSRun.setDates(getApplicationContext(), lbDate.getText().toString());
                    } else if (position == 1) {
                        spnTime.setVisibility(View.VISIBLE);
                        lbDate.setVisibility(View.GONE);
                    } else {
                        mDates = "";
//                        QTSRun.setDates(getApplicationContext(), "");
                        spnTime.setVisibility(View.GONE);
                        lbDate.setVisibility(View.GONE);
                    }
                } else {
                    mProceduce = listHospital.get(posChoose1).getTypes().get(posChoose2).getOptions().get(position).getContent();
                    mUrlHttp = listHospital.get(posChoose1).getTypes().get(posChoose2).getOptions().get(position).getUrl_option();
//                    QTSRun.setProcedure(getApplicationContext(),listHospital.get(posChoose1).getTypes().get(posChoose2).getOptions().get(position).getContent());
//                    QTSRun.setUrlHPT(getApplicationContext(),listHospital.get(posChoose1).getTypes().get(posChoose2).getOptions().get(position).getUrl_option());
                    spnTime.setVisibility(View.GONE);
                    lbDate.setVisibility(View.GONE);
                }
                adapter.setData(listHospital, posList, posChoose1, posChoose2);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnNext) {
            if (posList < 3) {
                if (posList == 1) {
                    if (mTypeSurgery.equalsIgnoreCase("I don't know")) {
                        mUrlHttp = "";
//                        QTSRun.setUrlHPT(getApplicationContext(),"");
                        posList = posList + 2;
                    } else {
                        posList++;
                    }
                } else {
                    posList++;
                }
                init(posList, posChoose1, posChoose2);
            } else if (posList == 3) {
                QTSRun.setPhoneNumber1(getApplicationContext(), mPhone1);
                QTSRun.setPhoneNumber2(getApplicationContext(), mPhone2);
                QTSRun.setPhoneNumber3(getApplicationContext(), mPhone3);
                QTSRun.setContactHop(getApplicationContext(), mContact);
                QTSRun.setHospital(getApplicationContext(), mHopital);
                QTSRun.setLatitude(getApplicationContext(), mlat);
                QTSRun.setLongitude(getApplicationContext(), mlong);
                QTSRun.setPositionChoose1(getApplicationContext(), mPosition1);
                QTSRun.setPositionChoose2(getApplicationContext(), mPosition2);
                QTSRun.setTypeSurgery(getApplicationContext(), mTypeSurgery);
                QTSRun.setDateSurgery(getApplicationContext(), mDateSurgery);
                QTSRun.setDateOfSurgery(getApplicationContext(), mDateOfSurgery);
                QTSRun.setPositionDate(getApplicationContext(), mPosDate);
                QTSRun.setDates(getApplicationContext(), mDates);
                QTSRun.setProcedure(getApplicationContext(), mProceduce);
                QTSRun.setUrlHPT(getApplicationContext(), mUrlHttp);
                QTSRun.setPositionSpinner(getApplicationContext(), mPosSpinner);
                QTSRun.setIsLogin(getApplicationContext(), true);
                Intent intent = new Intent(MainActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (v == btnPre) {
            btnNext.setEnabled(true);
            spnTime.setVisibility(View.GONE);
            lbDate.setVisibility(View.GONE);
            if (posList >= 1) {
                if (posList == 3) {
                    if (mTypeSurgery.equalsIgnoreCase("I don't know")) {
                        posList = posList - 2;
                    } else {
                        posList--;
                    }
                } else {
                    posList--;
                }
                init(posList, posChoose1, posChoose2);
            }
        } else if (v == lbDate) {
            showDialog(DATE_DIALOG_ID);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            lbDate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
            mDates = lbDate.getText().toString();
//            QTSRun.setDates(getApplicationContext(), lbDate.getText().toString());
//            QTSRun.setDateSurgery(getApplicationContext(), lbDate.getText().toString());
        }

    };

    public void getDefaultInfor() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        lbDate.setText(new StringBuilder()
                .append(year).append("-").append(month + 1).append("-").append(day).append(" "));
        if (mPosDate == 0) {
            lbDate.setText(mDates);
        } else if (mPosDate == 1) {
            spnTime.setSelection(mPosSpinner);
        }
//        if (QTSRun.getPositionDate(getApplicationContext()) == 0){
//            lbDate.setText(QTSRun.getDates(getApplicationContext()));
//        }else if (QTSRun.getPositionDate(getApplicationContext()) == 1){
//            spnTime.setSelection(QTSRun.getPositionSpinner(getApplicationContext()));
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class AnswersListAdapter extends BaseAdapter {
        Context context;
        //        ArrayList<String> arrAnswers ;
        List<HospitalObject> listObj;
        LayoutInflater inflater;
        int posi = 0;
        int pos1 = 0;
        int pos2 = 0;

//        public AnswersListAdapter(Context context, ArrayList<String> arrAnswers, int posi) {
//            this.context = context;
//            this.arrAnswers = arrAnswers;
//            this.posi = posi;
//
//            inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }

        public AnswersListAdapter(Context context, List<HospitalObject> listObj, int posi, int pos1, int pos2) {
            this.context = context;
            this.listObj = listObj;
            this.posi = posi;
            this.pos1 = pos1;
            this.pos2 = pos2;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            Log.e("MainActivity", "Item options:" + posi + "//" + pos1 + "//" + pos2);
            switch (posi) {
                case 0:
                    return listObj.size();
                case 1:
                    return listObj.get(pos1).getTypes().size();
                case 2:
                    return listObj.get(pos1).getTypes().get(pos2).getOptions().size();
                case 3:
                    return listObj.get(pos1).getAppointment().length;
                default:
                    return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return listHospital.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            TextView answer;
            TextView urlWeb;
            ImageView ivChecked;
        }

        @Override
        public View getView(final int position, View converview, ViewGroup container) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if (converview == null) {
                holder = new ViewHolder();
                converview = inflater.inflate(R.layout.row_question_hopital, container,
                        false);
                holder.answer = (TextView) converview.findViewById(R.id.tvAnswer);
                holder.urlWeb = (TextView) converview.findViewById(R.id.tvUrl);
                holder.ivChecked = (ImageView) converview.findViewById(R.id.ivChecked);
                converview.setTag(holder);
            } else {
                holder = (ViewHolder) converview.getTag();
            }
            switch (posi) {
                case 0:
                    holder.answer.setText(listObj.get(position).getHospital());
                    if (answerChoose[posi].equalsIgnoreCase(listObj.get(position).getHospital())) {
                        mPosition1 = position;
                        posChoose1 = position;
                        holder.ivChecked.setBackgroundResource(R.drawable.ic_ticked);
                        btnNext.setEnabled(true);
                    } else {
                        if (mHopital.equalsIgnoreCase(listObj.get(position).getHospital())) {
                            mPosition1 = position;
                            posChoose1 = position;
                            answerChoose[posi] = mHopital;
                            holder.ivChecked.setBackgroundResource(R.drawable.ic_ticked);
                            btnNext.setEnabled(true);
                        } else {
                            holder.ivChecked.setBackgroundResource(R.drawable.ic_unticked);
                        }
                    }
                    break;
                case 1:
                    holder.answer.setText(listObj.get(pos1).getTypes().get(position).getType_name());
                    if (answerChoose[posi].equalsIgnoreCase(listObj.get(pos1).getTypes().get(position).getType_name())) {
                        posChoose2 = position;
                        mPosition2 = position;
                        holder.ivChecked.setBackgroundResource(R.drawable.ic_ticked);
                        btnNext.setEnabled(true);
                    } else {
                        if (mTypeSurgery.equalsIgnoreCase(listObj.get(pos1).getTypes().get(position).getType_name())) {
                            posChoose2 = position;
                            mPosition2 = position;
                            answerChoose[posi] = listObj.get(pos1).getTypes().get(position).getType_name();
                            holder.ivChecked.setBackgroundResource(R.drawable.ic_ticked);
                            btnNext.setEnabled(true);
                        } else {
                            holder.ivChecked.setBackgroundResource(R.drawable.ic_unticked);
                        }
                    }
                    break;
                case 2:
                    holder.answer.setText(listObj.get(pos1).getTypes().get(pos2).getOptions().get(position).getContent());
                    holder.urlWeb.setText(listObj.get(pos1).getTypes().get(pos2).getOptions().get(position).getUrl_option());
                    if (answerChoose[posi].equalsIgnoreCase(listObj.get(pos1).getTypes().get(pos2).getOptions().get(position).getContent())) {
                        holder.ivChecked.setBackgroundResource(R.drawable.ic_ticked);
                        btnNext.setEnabled(true);
                    } else {
                        if (mProceduce.equalsIgnoreCase(listObj.get(pos1).getTypes().get(pos2).getOptions().get(position).getContent())) {
                            answerChoose[posi] = listObj.get(pos1).getTypes().get(pos2).getOptions().get(position).getContent();
                            holder.ivChecked.setBackgroundResource(R.drawable.ic_ticked);
                            btnNext.setEnabled(true);
                        } else {
                            holder.ivChecked.setBackgroundResource(R.drawable.ic_unticked);
                        }
                    }
                    break;
                case 3:
                    holder.answer.setText(listObj.get(position).getAppointment()[position]);
                    if (answerChoose[posi].equalsIgnoreCase(listObj.get(position).getAppointment()[position])) {
                        holder.ivChecked.setBackgroundResource(R.drawable.ic_ticked);
                        btnNext.setEnabled(true);
                    } else {
                        if (mDateOfSurgery.equalsIgnoreCase(listObj.get(position).getAppointment()[position])) {
                            answerChoose[posi] = listObj.get(position).getAppointment()[position];
                            holder.ivChecked.setBackgroundResource(R.drawable.ic_ticked);
                            btnNext.setEnabled(true);
//
                        } else {
                            holder.ivChecked.setBackgroundResource(R.drawable.ic_unticked);
                        }
                    }
                    break;
                default:
                    break;
            }
//            holder.answer.setText(arrAnswers.get(position));
//            if (answerChoose[posi].equalsIgnoreCase(arrAnswers.get(position))){
//                holder.ivChecked.setBackgroundResource(R.drawable.checkedbox);
//            }else {
//                holder.ivChecked.setBackgroundResource(R.drawable.empty_checkbox);
//            }
            QTSRun.setFontTV(context, holder.answer, QTSConst.FONT_LATO_REGULAR);
            return converview;
        }

        public void setData(List<HospitalObject> listObj, int posi, int pos1, int pos2) {
            this.listObj = listObj;
            this.posi = posi;
            this.pos1 = pos1;
            this.pos2 = pos2;
            Log.e("MainActivity", "Item options:" + pos1 + "//" + pos2);
            this.notifyDataSetChanged();
        }
    }
}
