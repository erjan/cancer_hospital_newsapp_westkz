package kz.onko_zko.hospital.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kz.onko_zko.hospital.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FilePickerDialog dialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    File uploadFile = null;
    Map<String, String> map = new HashMap<String, String>();

    String url_base = "https://onkozko.herokuapp.com/";

    private OnFragmentInteractionListener mListener;

    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
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
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        final View v = inflater.inflate(R.layout.fragment_admin, container, false);
        dialog = new FilePickerDialog(getActivity(),properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if(files.length>0)
                {
                    Bitmap myBitmap = BitmapFactory.decodeFile(files[0]);
                    ((ImageView)v.findViewById(R.id.preview_image)).setImageBitmap(myBitmap);
                    uploadFile = new File(files[0]);
                }
            }
        });
        v.findViewById(R.id.attach_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.show();
            }
        });
        final EditText name_of_news = v.findViewById(R.id.name_of_news);
        final EditText title_news = v.findViewById(R.id.title_news);
        v.findViewById(R.id.sendToServer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable())
                    return;
                OkHttpClient client = new OkHttpClient();
                Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title_of_news", title_news.getText().toString())
                        .addFormDataPart("content_news", name_of_news.getText().toString());
                if(uploadFile != null) {
                    builder.addFormDataPart("attachments", uploadFile.getName(),
                            RequestBody.create(MediaType.parse(MimeTypeMap.getFileExtensionFromUrl(uploadFile.getAbsolutePath())), uploadFile));
                }
                Request request = new Request.Builder()
                        .url(url_base+"add-new")
                        .post(builder.build())
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        name_of_news.post(new Runnable() {
                            @Override
                            public void run() {
                                name_of_news.setText("");
                            }
                        });
                        title_news.post(new Runnable() {
                            @Override
                            public void run() {
                                title_news.setText("");
                            }
                        });
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                ((ImageView)v.findViewById(R.id.preview_image)).setImageDrawable(null);
                            }
                        });
                        Log.v("Response from server:", String.valueOf(response.body()));
                    }
                });
            }
        });
        final Spinner spinner = v.findViewById(R.id.select_deletable);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!isNetworkAvailable())
                    return;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url_base+"/get-news")
                        .build();
                Response response;
                try {
                    response = client.newCall(request).execute();
                    JSONArray arr = new JSONArray(response.body().string());
                    map.clear();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = (JSONObject) arr.get(i);
                        map.put(obj.getString("title"),obj.getString("id"));
                    }
                    spinner.post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, map.keySet().toArray(new String[0]));
                            spinner.setAdapter(adapter);
                        }
                    });
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        v.findViewById(R.id.delete_button_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("article_id", map.get(spinner.getSelectedItem().toString()));
                Request request = new Request.Builder()
                        .url(url_base+"delete-article")
                        .post(builder.build())
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(url_base+"/get-news")
                                .build();
                        Response rsp;
                        try {
                            rsp = client.newCall(request).execute();
                            JSONArray arr = new JSONArray(rsp.body().string());
                            map.clear();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = (JSONObject) arr.get(i);
                                map.put(obj.getString("title"),obj.getString("id"));
                            }
                            spinner.post(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, map.keySet().toArray(new String[0]));
                                    spinner.setAdapter(adapter);
                                }
                            });
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        v.findViewById(R.id.loginAdminButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((EditText)v.findViewById(R.id.password)).getText().toString().equals("onko_zko_1965")
                    && ((EditText)v.findViewById(R.id.login)).getText().toString().equals("hospital_admin_zko"))
                {
                    v.findViewById(R.id.credentialsPanel).setVisibility(View.GONE);
                    v.findViewById(R.id.adminControlPanel).setVisibility(View.VISIBLE);
                }
            }
        });
        return v;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(dialog!=null)
                    {
                        dialog.show();
                    }
                }
                else {
                    Toast.makeText(getActivity(),"Permission is Required for getting list of files",Toast.LENGTH_SHORT).show();
                }
            }
        }
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
