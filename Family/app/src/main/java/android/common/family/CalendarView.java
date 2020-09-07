
package android.common.family;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.TableRow.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class CalendarView extends FragmentActivity implements OnClickListener {

    private Calendar rightNow;
    private GregorianCalendar gCal;
    private int iYear = 0;
    private int iMonth = 0;

    private int startDayOfweek = 0;
    private int maxDay = 0;
    private int oneday_width =0;
    private int oneday_height =0;

    ArrayList<String> daylist; //일자 목록을 가지고 있는다.
    ArrayList<String> actlist; //일자에 해당하는 활동내용

    TextView aDateTxt;

    private int dayCnt;
    private int mSelect = -1;

    protected void initialize(){
        setContentView(R.layout.activity_calendar);

        rightNow = Calendar.getInstance();
        gCal = new GregorianCalendar();
        iYear = rightNow.get(Calendar.YEAR);
        iMonth = rightNow.get(Calendar.MONTH);

        Button btnMPrev = (Button)findViewById(R.id.btn_calendar_prevmonth);
        btnMPrev.setOnClickListener(this);
        Button btnMNext = (Button)findViewById(R.id.btn_calendar_nextmonth);
        btnMNext.setOnClickListener(this);

        btnMPrev.setText("prev");
        btnMNext.setText("next");

        aDateTxt = (TextView)findViewById(R.id.CalendarMonthTxt);

        makeCalendardata(iYear, iMonth);
        makeCalendar();
    }


    //달력의 일자 표시
    private void printDate(String thisYear, String thisMonth)
    {

        if(thisMonth.length() == 1) {
            aDateTxt.setText(String.valueOf(thisYear) + "." + "0"+ thisMonth);
        }
        else{
            aDateTxt.setText(String.valueOf(thisYear) + "." + thisMonth);
        }
    }

    //달력에 표시할 일자를 배열에 넣어 구성
    private void makeCalendardata(int thisYear, int thisMonth) {
        printDate(String.valueOf(thisYear),String.valueOf(thisMonth+1));

        rightNow.set(thisYear, thisMonth, 1);
        gCal.set(thisYear, thisMonth, 1);
        startDayOfweek = rightNow.get(Calendar.DAY_OF_WEEK);


        maxDay = gCal.getActualMaximum ((Calendar.DAY_OF_MONTH));
        if(daylist==null)daylist = new ArrayList<String>();
        daylist.clear();

        if(actlist==null)actlist = new ArrayList<String>();
        actlist.clear();

        daylist.add("sun");actlist.add("");
        daylist.add("mon");actlist.add("");
        daylist.add("tue");actlist.add("");
        daylist.add("wed");actlist.add("");
        daylist.add("thu");actlist.add("");
        daylist.add("fri");actlist.add("");
        daylist.add("sat");actlist.add("");

        if(startDayOfweek != 1) {
            gCal.set(thisYear, thisMonth-1, 1);
            int prevMonthMaximumDay = (gCal.getActualMaximum((Calendar.DAY_OF_MONTH))+2);
            for(int i=startDayOfweek;i>1;i--){
                daylist.add(Integer.toString(prevMonthMaximumDay-i));
                actlist.add("p");
            }
        }

        for(int i=1;i<=maxDay;i++) //????? ??´?.
        {
            daylist.add(Integer.toString(i));
            actlist.add("");
        }


        int dayDummy = (startDayOfweek-1)+maxDay;
        if(dayDummy >35)
        {
            dayDummy = 42 - dayDummy;
        }else{
            dayDummy = 35 - dayDummy;
        }

        //.
        if(dayDummy != 0)
        {
            for(int i=1;i<=dayDummy;i++)
            {
                daylist.add(Integer.toString(i));
                actlist.add("n");
            }
        }

        makeCalendar();
    }



    private void makeCalendar()
    {
        final Oneday[] oneday = new Oneday[daylist.size()];
        final Calendar today = Calendar.getInstance();
        TableLayout tl =(TableLayout)findViewById(R.id.tl_calendar_monthly);
        tl.removeAllViews();

        dayCnt = 0;
        int maxRow = ((daylist.size() > 42)? 7:6);
        int maxColumn = 7;


        oneday_width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        oneday_height = getWindow().getWindowManager().getDefaultDisplay().getHeight();

        oneday_height = ((((oneday_height >= oneday_width)?oneday_height:oneday_width) - tl.getTop()) / (maxRow+1))-10;
        oneday_width = (oneday_width / maxColumn)+1;



        int daylistsize =daylist.size()-1;
        for(int i=1;i<=maxRow;i++ )
        {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
            for(int j=1;j<=maxColumn;j++)
            {
                //calender_oneday를 생성해 내용을 넣는다.
                oneday[dayCnt] = new android.common.family.Oneday(getApplicationContext());

                //요일별 색상 정하기
                if((dayCnt % 7) == 0){
                    oneday[dayCnt].setTextDayColor(Color.RED);
                } else if((dayCnt % 7) == 6){
                    oneday[dayCnt].setTextDayColor(Color.GRAY);
                } else {
                    oneday[dayCnt].setTextDayColor(Color.BLACK);
                }

                //요일 표시줄 설정
                if(dayCnt >= 0 && dayCnt < 7)
                {
                    oneday[dayCnt].setBgDayPaint(Color.DKGRAY); //배경색상
                    oneday[dayCnt].setTextDayTopPadding(15); //일자 표시할때 top padding
                    oneday[dayCnt].setTextDayColor(Color.WHITE); //일자의 글씨 색상
                    oneday[dayCnt].setTextDaySize(30); //일자의 글시크기
                    oneday[dayCnt].setLayoutParams(new LayoutParams(oneday_width,35)); //일자 컨트롤 크기
                    oneday[dayCnt].isToday = false;

                }else{

                    oneday[dayCnt].isToday = false;
                    oneday[dayCnt].setDayOfWeek(dayCnt%7 + 1);
                    oneday[dayCnt].setDay(Integer.valueOf(daylist.get(dayCnt)).intValue());
                    oneday[dayCnt].setTextActcntSize(15);
                    oneday[dayCnt].setTextActcntColor(Color.BLACK);
                    oneday[dayCnt].setTextActcntTopPadding(18);
                    oneday[dayCnt].setBgSelectedDayPaint(Color.rgb(0, 162, 232));
                    oneday[dayCnt].setBgTodayPaint(Color.LTGRAY);
                    oneday[dayCnt].setBgActcntPaint(Color.rgb(251, 247, 176));
                    oneday[dayCnt].setLayoutParams(new LayoutParams(oneday_width,oneday_height));

                    //이전 달 블럭 표시시
                    if(actlist.get(dayCnt).equals("p")){
                        oneday[dayCnt].setTextDaySize(18);
                        actlist.set(dayCnt, "");
                        oneday[dayCnt].setTextDayTopPadding(-4);

                        if(iMonth - 1 < Calendar.JANUARY){
                            oneday[dayCnt].setMonth(Calendar.DECEMBER);
                            oneday[dayCnt].setYear(iYear - 1);
                        }  else {
                            oneday[dayCnt].setMonth(iMonth - 1);
                            oneday[dayCnt].setYear(iYear);
                        }

                        //다음 달 블럭 표시
                    } else if(actlist.get(dayCnt).equals("n")){
                        oneday[dayCnt].setTextDaySize(18);
                        actlist.set(dayCnt, "");
                        oneday[dayCnt].setTextDayTopPadding(-4);
                        if(iMonth + 1 > Calendar.DECEMBER){
                            oneday[dayCnt].setMonth(Calendar.JANUARY);
                            oneday[dayCnt].setYear(iYear + 1);
                        }  else {
                            oneday[dayCnt].setMonth(iMonth + 1);
                            oneday[dayCnt].setYear(iYear);
                        }
                        // 현재 달 블럭 표시
                    }else{
                        oneday[dayCnt].setTextDaySize(24);
                        oneday[dayCnt].setYear(iYear);
                        oneday[dayCnt].setMonth(iMonth);

                        //오늘 표시
                        if(oneday[dayCnt].getDay() == today.get(Calendar.DAY_OF_MONTH)
                                && oneday[dayCnt].getMonth() == today.get(Calendar.MONTH)
                                && oneday[dayCnt].getYear() == today.get(Calendar.YEAR)){

                            oneday[dayCnt].isToday = true;
                            actlist.set(dayCnt,"오늘");
                            oneday[dayCnt].invalidate();
                            mSelect = dayCnt;
                        }
                    }


                    oneday[dayCnt].setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            //Toast.makeText(context, iYear+"-"+iMonth+"-"+oneday[v.getId()].getTextDay(), Toast.LENGTH_LONG).show();
                            return false;
                        }
                    });

                    oneday[dayCnt].setOnTouchListener(new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if(oneday[v.getId()].getTextDay() != "" && event.getAction() == MotionEvent.ACTION_UP)
                            {
                                if(mSelect != -1){
                                    oneday[mSelect].setSelected(false);
                                    oneday[mSelect].invalidate();
                                }
                                oneday[v.getId()].setSelected(true);
                                oneday[v.getId()].invalidate();
                                mSelect = v.getId();

                                //Log.d("hahaha", oneday[mSelect].getMonth()+"-"+ oneday[mSelect].getDay());

                                onTouched(oneday[mSelect]);
                            }
                            return false;
                        }
                    });
                }


                oneday[dayCnt].setTextDay(daylist.get(dayCnt).toString()); //????,???? ??? 
                oneday[dayCnt].setTextActCnt(actlist.get(dayCnt).toString());//??????? ??? 
                oneday[dayCnt].setId(dayCnt); //?????? ????? ??????? ??? id??? 
                oneday[dayCnt].invalidate();
                tr.addView(oneday[dayCnt]);

                if(daylistsize != dayCnt)
                {
                    dayCnt++;
                }else{
                    break;
                }
            }
            tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

        }
    }




    /**
     * ????? 2??? ????? ???, 2 -> 02
     * @param value
     * @return
     */
    protected String doubleString(int value)
    {
        String temp;

        if(value < 10){
            temp = "0"+ String.valueOf(value);

        }else {
            temp = String.valueOf(value);
        }
        return temp;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_calendar_nextmonth:
                if(iMonth == 11)
                {
                    iYear = iYear + 1;
                    iMonth = 0;
                }
                else
                {
                    iMonth = iMonth + 1;
                }
                makeCalendardata(iYear,iMonth);
                break;
            case R.id.btn_calendar_prevmonth:
                if(iMonth == 0)
                {
                    iYear = iYear - 1;
                    iMonth = 11;
                }else{
                    iMonth = iMonth - 1;
                }
                makeCalendardata(iYear,iMonth);
                break;
        }
    }

    /**
     * ???? ????????? ????????? ??? ????? ??? ??? ???
     * @param oneday
     */
    protected void onTouched(android.common.family.Oneday oneday){

    }

    /**
     * ??? ???? ?????? ???? ??? ????? ???
     * @param test ????? ???
     * @param basis ???? ???
     * @param during ??(??)
     * @return
     */
    protected boolean isInside(Oneday test, Oneday basis, int during){
        Calendar calbasis = Calendar.getInstance();
        calbasis.set(basis.getYear(), basis.getMonth(), basis.getDay());
        calbasis.add(Calendar.DAY_OF_MONTH, during);

        Calendar caltest = Calendar.getInstance();
        caltest.set(test.getYear(), test.getMonth(), test.getDay());

        if(caltest.getTimeInMillis() < calbasis.getTimeInMillis()){
            return true;
        }
        return false;
    }

    /**
     *???? ??????? ??? 
     */
    public void gotoToday(){
        final Calendar today = Calendar.getInstance();
        iYear = today.get(Calendar.YEAR);
        iMonth = today.get(Calendar.MONTH);
        makeCalendardata(today.get(Calendar.YEAR),today.get(Calendar.MONTH));
    }
}