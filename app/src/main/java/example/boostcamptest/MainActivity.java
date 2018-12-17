package example.boostcamptest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import example.boostcamptest.RecyclerView.Adapter;
import example.boostcamptest.RecyclerView.item;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "recyclerview";

    private static ArrayList<item> mArrayList;
    private static Adapter mAdapter;
    private static RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private int count = -1;

    private Button btnSearch;
    private EditText editTitle;
    //private TextView text;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static String text;
    public static int cnt = 1;
    public static int totalcnt = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new Adapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        btnSearch = (Button)findViewById(R.id.btnSearch);
        editTitle = (EditText) findViewById(R.id.editTitle);
        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //버튼누를때 구현되는것
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTitle.getWindowToken(),0);
                text = editTitle.getText().toString();
                cnt = 1;
                mArrayList = new ArrayList<>();
                new HttpAsyncTask().execute("https://openapi.naver.com/v1/search/movie.json");
                if(totalcnt==0){
                    Toast.makeText(MainActivity.this, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!mRecyclerView.canScrollVertically(1)){
                    cnt +=10;
                    if(totalcnt<cnt){
                        Toast.makeText(MainActivity.this, "마지막입니다", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new HttpAsyncTask().execute("https://openapi.naver.com/v1/search/movie.json");
                }
            }
        });
    }

    public class HttpAsyncTask extends AsyncTask<String, Void, ArrayList<item>>{
        @Override
        protected ArrayList<item> doInBackground(String... params){
            String result = null;
            String strUrl = params[0];
            try{
                //url 객체 생성
                URL url = new URL(strUrl);
                String strurl = url.toString() +"?Query="+text+"&start="+cnt;
                url = new URL(strurl);
                //URL을 연결할 객체 생성
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);
                connection.setRequestProperty("X-Naver-Client-Id", "BJbhvmykFKi53dAGLGzL");
                connection.setRequestProperty("X-Naver-Client-Secret", "kIhtpy5ICz");
                int responseCode = connection.getResponseCode();
                Map<String, List<String>> map = connection.getHeaderFields();

                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                System.out.println();
                //입력 스트림 열기
                InputStream inputStream = connection.getInputStream();
                //문자열 저장 객체
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                System.out.println("에러코드    "+responseCode);
                while ((line = reader.readLine())!=null){
                    builder.append(line+"\n");
                    //System.out.println("에러코드    "+responseCode);
                }
                //결과
                result = builder.toString();
                System.out.println("결과"+result.toString());
                JSONObject object = new JSONObject(result.toString());
                JSONArray jsonArray;
                if(cnt==1){
                    String total= new String(object.getString("total"));
                    totalcnt = Integer.parseInt(total);
                    System.out.println("갯수는?"+totalcnt);
                }
                jsonArray = new JSONArray(object.getString("items"));
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String title = jObject.getString("title");
                    String link = jObject.getString("link");
                    String Image = jObject.getString("image");
                    String subtitle = jObject.getString("subtitle");
                    String pubDate = jObject.getString("pubDate");
                    String director = jObject.getString("director");
                    String actor = jObject.getString("actor");
                    String userRating = jObject.getString("userRating");
                    item items = new item(Image,title,subtitle,pubDate,director,actor,userRating,link);
                    mArrayList.add(items);
                    System.out.println("제목은    "+ title);
                }
                System.out.println("갯수"+ mArrayList.size());

            }catch (MalformedURLException e){
                e.printStackTrace();
                System.out.println("실패1");
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("실패2");
            }catch (JSONException e){
                e.printStackTrace();
                System.out.println("실패3");
            }
            return mArrayList;
        }
        @Override
        protected void onPostExecute(ArrayList<item> items){
            super.onPostExecute(items);
            if(items!=null){
                //Log.d("HttpAsyncTask",s);
                if(cnt==1){
                    mAdapter = new Adapter(items);
                    mRecyclerView.setAdapter(mAdapter);
                }else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}

