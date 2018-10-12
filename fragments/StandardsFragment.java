package kz.onko_zko.hospital.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kz.onko_zko.hospital.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StandardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StandardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StandardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StandardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StandardsFragment newInstance(String param1, String param2) {
        StandardsFragment fragment = new StandardsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standards, container, false);
        view.findViewById(R.id.standard1).setOnClickListener(this);
        view.findViewById(R.id.standard2).setOnClickListener(this);
        view.findViewById(R.id.standard3).setOnClickListener(this);
        view.findViewById(R.id.standard4).setOnClickListener(this);
        view.findViewById(R.id.standard5).setOnClickListener(this);
        view.findViewById(R.id.standard6).setOnClickListener(this);
        view.findViewById(R.id.standard7).setOnClickListener(this);
        view.findViewById(R.id.standard8).setOnClickListener(this);
        return view;
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

    @Override
    public void onClick(View v) {
        int stdNum = 1;
        switch (v.getId()) {
            case R.id.standard1:
                stdNum = 1;
                break;
            case R.id.standard2:
                stdNum = 2;

                break;
            case R.id.standard3:
                stdNum = 3;

                break;
            case R.id.standard4:
                stdNum = 4;

                break;
            case R.id.standard5:
                stdNum = 5;

                break;
            case R.id.standard6:
                stdNum = 6;

                break;
            case R.id.standard7:
                stdNum = 7;

                break;
            case R.id.standard8:
                stdNum = 8;

                break;
        }
        Uri uri = Uri.parse("http://www.onko-zko.kz/images/files/standart/kz/"+String.valueOf(stdNum)+".docx");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
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
