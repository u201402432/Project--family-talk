package android.common.family;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class CalendarActivity extends CalendarView {
    Button but_board,but_calendar,but_home,but_set,but_talk;

    private Oneday basisDay;
    private int during;
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(Window.FEATURE_NO_TITLE, Window.FEATURE_NO_TITLE);
        initialize();



        but_board = (Button)findViewById(R.id.board);
        but_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, GalleryActivity.class));
            }
        });

        but_calendar = (Button)findViewById(R.id.calendar);
        but_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, CalendarActivity.class));
            }
        });

        but_set = (Button)findViewById(R.id.setting);
        but_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, SettingActivity.class));
            }
        });

        but_talk = (Button)findViewById(R.id.talk);
        but_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this,TalkActivity.class));
            }
        });

        but_home = (Button)findViewById(R.id.home);
        but_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, MainActivity.class));
            }
        });


        basisDay = new Oneday(this);

        Intent intent = getIntent();
        int[] b = intent.getIntArrayExtra("basisDay");
        during = intent.getIntExtra("during", 0);
        if(b != null){
            basisDay.setYear(b[0]);
            basisDay.setMonth(b[1]);
            basisDay.setDay(b[2]);
        } else {
            Calendar cal = Calendar.getInstance();
            basisDay.setYear(cal.get(Calendar.YEAR));
            basisDay.setMonth(cal.get(Calendar.MONTH));
            basisDay.setDay(cal.get(Calendar.DAY_OF_MONTH));

        }
        backPressCloseSystem = new BackPressCloseSystem(this);
    }
    @Override
    public void onBackPressed() {
        backPressCloseSystem.onBackPressed();
    }
    @Override
    protected void onTouched(Oneday touchedDay){

        if(isInside(touchedDay, basisDay, during)){
            Calendar cal = Calendar.getInstance();
            cal.set(basisDay.getYear(), basisDay.getMonth(), basisDay.getDay());
            cal.add(Calendar.DAY_OF_MONTH,during);
            Toast.makeText(this, (cal.get(Calendar.MONTH) + 1) + "월" +
                    cal.get(Calendar.DAY_OF_MONTH) + "일 이후 선택 가능", Toast.LENGTH_SHORT).show();
            return;
        }

        final String year = String.valueOf(touchedDay.getYear());
        final String month = doubleString(touchedDay.getMonth() + 1);
        final String date = doubleString(touchedDay.getDay());
        //
        AlertDialog.Builder builder =  new AlertDialog.Builder(CalendarActivity.this);
        builder.setTitle("다음 날짜로 설정하시겠습니까??");
        builder.setMessage(year + "." + month + "." + date + "(" + touchedDay.getDayOfWeekKorean()+")");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Intent intent = new Intent();
                //intent.putExtra("date", year + "." + month + "." + date);
                //setResult(RESULT_OK, intent);
               // finish();
                Toast.makeText(getApplicationContext(), year + "." + month + "." + date, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("아니오", null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //����
            case R.id.menuitem_calendar_0:
                gotoToday();
                return true;
        }

        return false;
    }
}
