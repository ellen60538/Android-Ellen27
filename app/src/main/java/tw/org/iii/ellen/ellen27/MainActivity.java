package tw.org.iii.ellen.ellen27;
// 非同步任務
//不用handler也可以在背景中做
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mesg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mesg = findViewById(R.id.mesg) ;
    }

    private MyAsyncTask myAsyncTask ;
    public void test1(View view) {
        mesg.setText("") ;
        myAsyncTask = new MyAsyncTask() ;
        myAsyncTask.execute("Ellen","Andy","Sandra") ;
    }

    public void test2(View view) {
        if (myAsyncTask != null){
            Log.v("ellen","status : " + myAsyncTask.getStatus().name()) ;
            if ( !myAsyncTask.isCancelled()){
                myAsyncTask.cancel(true) ;
            }

        }
    }

    private class MyAsyncTask extends AsyncTask<String, Object, String>{
        //第一個泛型:
        //第三個泛型:做完之後要回傳的型別
        private int total, i ;

        @Override
        protected void onPreExecute() {
            //執行前
            //前置動作,可以將連線資料庫之類的動作在這裡
            super.onPreExecute();
            Log.v("ellen","onPreExecute");
            mesg.append("Start...\n") ;
        }

        @Override
        protected void onPostExecute(String result) {
            //執行後
            super.onPostExecute(result);
            Log.v("ellen","onPostExecute : " + result);
            mesg.append(result) ;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            //更新進度
            super.onProgressUpdate(values);
            Log.v("ellen","onProgressUpdate : " + values[0] + "%");
            mesg.append(values[1] + ":" + values[0] + "%\n");
        }

        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
            Log.v("ellen","onCancelled(aVoid)" + result) ;
            mesg.append(result) ;
            //企圖讓doInBackground正常return
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v("ellen","onCancelled()");
        }

        @Override
        protected String doInBackground(String... names) {
            Log.v("ellen","doInBackground");
            total = names.length ;

            for (String name : names) {
                i++ ;
                try {
                    Thread.sleep(1500) ;
                }catch (Exception e){}

                Log.v("ellen", name + "") ;
                //mesg.append(name + "\n") ; 這裡會報錯or閃退
                publishProgress((int)Math.ceil(i*100.0/total), name) ;
            }
            if (isCancelled()){
                return "Canceled";
            }else {
                return "Gama Over";
            }
        }
    }
}
