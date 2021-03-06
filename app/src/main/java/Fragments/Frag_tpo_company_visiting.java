package Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import CustomItems.CustomListAdapter;
import CustomItems.CustomListAdapter_companies;
import CustomItems.Model;
import CustomItems.Model_companies;
import in.ac.nith.nithamirpur.R;

/**
 * Created by Ashu on 28-08-2016.
 */
public class Frag_tpo_company_visiting extends Fragment {
    private static final String url = "http://14.139.56.14/app/tpo_company.php";
    private ProgressDialog pDialog;
    private List<Model_companies> notices = new ArrayList<Model_companies>();
    private ListView listView;
    private CustomListAdapter_companies adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tpo_company_visiting, container, false);
        listView = (ListView) v.findViewById(R.id.list);
        adapter = new CustomListAdapter_companies(getActivity(), notices);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ashu", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Model_companies m = new Model_companies();
                                m.setData(obj.getString("name"));
                                notices.add(m);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        pDialog.hide();
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "No Internet Connection Detected", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });
        queue.add(req);
        return v;
    }
}
