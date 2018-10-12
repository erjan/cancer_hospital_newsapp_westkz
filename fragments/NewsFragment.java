package kz.onko_zko.hospital.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

import kz.onko_zko.hospital.NewsAdapter;
import kz.onko_zko.hospital.R;
import kz.onko_zko.hospital.ShowDetailsFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<NewsAdapter.NewsItem> mDataSet;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<NewsAdapter.NewsItem> myDataset;
    Context ctx ;
    private OnFragmentInteractionListener mListener;
    String url_base = "https://onkozko.herokuapp.com/";
    NewsAdapter.NewsInterface newsInterface = new NewsAdapter.NewsInterface() {
        @Override
        public void clickedNews(NewsAdapter.NewsItem item) {
            if(ctx==null)
                return;
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, ShowDetailsFragment.newInstance(item.imageUrl,item.title,item.content)).commit();
        }
    };

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
    }
    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx = inflater.getContext();
        View v= inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.news_feed);
        myDataset = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(inflater.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter(new ArrayList<NewsAdapter.NewsItem>(), newsInterface);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAdapter = new NewsAdapter(getDataSet(), newsInterface);
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                    mRecyclerView.swapAdapter(mAdapter, true);
                    if(mAdapter.getItemCount()<1) {
                        getActivity().findViewById(R.id.news_feed).setVisibility(View.INVISIBLE);
                        getActivity().findViewById(R.id.no_news_text).setVisibility(View.VISIBLE);
                    } else {
                        getActivity().findViewById(R.id.no_news_text).setVisibility(View.INVISIBLE);
                        getActivity().findViewById(R.id.news_feed).setVisibility(View.VISIBLE);
                    }
                    }
                });
            }
        }).start();
        mRecyclerView.swapAdapter(mAdapter, true);
        return v;
    }

    private ArrayList<NewsAdapter.NewsItem> getDataSet() {
        if(!isNetworkAvailable())
            return null;

        mDataSet = new ArrayList<>();
        Request request = new Request.Builder()
                .url(url_base+"get-news")
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response;
        try {
            response = client.newCall(request).execute();
            JSONArray arr = new JSONArray(response.body().string());
            mDataSet.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                String img_arr = obj.getString("attachment_array");
                mDataSet.add(new NewsAdapter.NewsItem(obj.getString("title"), obj.getString("news_content"), img_arr));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return mDataSet;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private boolean isNetworkAvailable() {
        if(ctx == null)
            return false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) (ctx).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
}
