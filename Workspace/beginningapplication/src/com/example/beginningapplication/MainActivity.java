package com.example.beginningapplication;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.app.Activity;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;


public class MainActivity extends ActionBarActivity implements OnClickListener{
	Button btn1;
	Button btn2;
	Button btn3;
	EditText etxt1;
	int counter = 0;
	String strcounter; 
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1=(Button)findViewById(R.id.button1);
        btn2=(Button)findViewById(R.id.button2);
        btn3=(Button)findViewById(R.id.button3);
        etxt1=(EditText)findViewById(R.id.editText1);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v==btn1){
					counter+= 5;
					strcounter = Integer.toString(counter);
					etxt1.setText(strcounter);
				}
				
				if (v==btn2){
					counter-= 5;
					strcounter = Integer.toString(counter);
					etxt1.setText(strcounter);
				}
				
				
			}
		}); // HENSON:  You were missing the closing parenthesis here.


        
        class StopWatch extends Activity {
            Chronometer mChronometer;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.main);

                Button button;

                mChronometer = (Chronometer) findViewById(R.id.chronometer);

                // Watch for button clicks.
                button = (Button) findViewById(R.id.start);
                button.setOnClickListener(mStartListener);

                button = (Button) findViewById(R.id.stop);
                button.setOnClickListener(mStopListener);

                button = (Button) findViewById(R.id.reset);
                button.setOnClickListener(mResetListener);
              
            }

            View.OnClickListener mStartListener = new OnClickListener() {
                public void onClick(View v) {
                    mChronometer.start();
                }
            };

            View.OnClickListener mStopListener = new OnClickListener() {
                public void onClick(View v) {
                    mChronometer.stop();
                }
            };

            View.OnClickListener mResetListener = new OnClickListener() {
                public void onClick(View v) {
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                }
            };
        }  

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
