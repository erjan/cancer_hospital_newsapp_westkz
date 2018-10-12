package kz.onko_zko.hospital;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kz.onko_zko.hospital.fragments.NewsFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PostTitle = "Title";
    private static final String PostContent = "Content";
    private static final String imageurl = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String post_title;
    private String post_content;
    private String image_url;
String url_base = "https://onkozko.herokuapp.com/";
    private OnFragmentInteractionListener mListener;

    public ShowDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowDetailsFragment newInstance(String param1, String param2) {
        ShowDetailsFragment fragment = new ShowDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ShowDetailsFragment newInstance(String urlImage, String postTitle, String contentPost) {
        ShowDetailsFragment fragment = new ShowDetailsFragment();
        Bundle args = new Bundle();
        args.putString(PostTitle, postTitle);
        args.putString(PostContent, contentPost);
        args.putString(imageurl, urlImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            post_title = getArguments().getString(PostTitle);
            post_content = getArguments().getString(PostContent);
            image_url = getArguments().getString(imageurl);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_details, container, false);
        ((TextView)view.findViewById(R.id.newsFullContent)).setText(post_content);
        ((TextView)view.findViewById(R.id.title_detailed_news)).setText(post_title);
        if(!image_url.equals("")) {
            GlideApp.with(view)
                    .load(image_url)
                    .fitCenter()
                    .into((ImageView)view.findViewById(R.id.imageDetailedNews));
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null)
            return;
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, NewsFragment.newInstance("","")).commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
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
