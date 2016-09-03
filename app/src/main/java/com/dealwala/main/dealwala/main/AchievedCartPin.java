package com.dealwala.main.dealwala.main;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.model.DealDataModel;
import com.dealwala.main.dealwala.model.MyLoyaltyModel;
import com.dealwala.main.dealwala.util.JSONParser;
import com.dealwala.main.dealwala.util.ModuleClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jaimin Patel on 05-Aug-16.
 */
public class AchievedCartPin extends Fragment {

    private RecyclerView recyclerView;
    ArrayList<MyLoyaltyModel> dataList = new ArrayList<>();
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wall, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new GetAchievedCart().execute();
        return view;
    }

    class GetAchievedCart extends AsyncTask<Void, Void, Void> {
        boolean success;
        String responseError;
        ProgressDialog dialog;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (success) {
                if (dataList.size() > 0) {
                    RecyclerViewAchievedPinAdapter adapter = new RecyclerViewAchievedPinAdapter(getActivity(), dataList);
                    recyclerView.setAdapter(adapter);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getActivity(), responseError, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "list_cart_deals"));
            inputArray.add(new BasicNameValuePair("customer_id", ModuleClass.USER_ID));

            double latitude = 0.0000;
            double longitude = 0.0000;
            if(MainActivity.mCurrentLocation != null){
                inputArray.add(new BasicNameValuePair("long",""+MainActivity.mCurrentLocation.getLongitude()));
                inputArray.add(new BasicNameValuePair("lat",""+MainActivity.mCurrentLocation.getLatitude()));
            }else{
                inputArray.add(new BasicNameValuePair("lat",""+latitude));
                inputArray.add(new BasicNameValuePair("long",""+longitude));
            }

            Log.d("Deal List input ", inputArray.toString());
            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH + "index.php", "GET", inputArray);
            Log.d("Deal List ", responseJSON.toString());

            if (responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");
                    if (dataArray.length() > 0) {
                        getAchievedPinListFromJson(dataArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseError = "There is some problem in server connection";
                }

            } else {
                success = false;
                responseError = "There is some problem in server connection";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(dataList != null){
                dataList.clear();
            }

            dialog = new ProgressDialog(getActivity(), R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }

    public void getAchievedPinListFromJson(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                Log.v("Notification", "Data object " + object.toString());

                MyLoyaltyModel data = new MyLoyaltyModel(
                        object.getString("id"),
                        object.getString("date_time"),
                        object.getString("merchant_id"),
                        object.getString("merchant_name")
                );


                dataList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
