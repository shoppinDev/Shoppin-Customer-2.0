package com.dealwala.main.dealwala.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
 * Created by Jaimin Patel on 23-Jun-16.
 */
public class MyLoyaltyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<MyLoyaltyModel> dataList = new ArrayList<>();

    RecyclerView recyclerView;

    TextView tvEmpty;

    boolean isQueryRequested = false;

    public MyLoyaltyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLoyaltyFragment newInstance(String param1, String param2) {
        MyLoyaltyFragment fragment = new MyLoyaltyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wall, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);

        new GetMyLoyaltyTask().execute();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class GetMyLoyaltyTask extends AsyncTask<Void, Void, Void> {
        boolean success;
        String responseError;
        ProgressDialog dialog;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (success) {
                if (dataList.size() > 0) {
                    RecyclerViewLoyaltyAdapter adapter = new RecyclerViewLoyaltyAdapter(getActivity(), dataList);
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
            inputArray.add(new BasicNameValuePair("webmethod", "my_loyalty_list"));
            inputArray.add(new BasicNameValuePair("custemer_id", ModuleClass.USER_ID));

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
                        getLoyaltyListFromJson(dataArray);
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

    public void getLoyaltyListFromJson(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                Log.v("Notification", "Data object " + object.toString());

                MyLoyaltyModel data = new MyLoyaltyModel(
                        object.getString("totalpins"),
                        object.getString("totalreedem"),
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
