package a.ghoedev.yoss.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import a.ghoedev.yoss.Activity.ShowItem;
import a.ghoedev.yoss.Adapter.ViewAdapter;
import a.ghoedev.yoss.BuildConfig;
import a.ghoedev.yoss.Interface.OnClick;
import a.ghoedev.yoss.R;
import a.ghoedev.yoss.Util.Constant;
import a.ghoedev.yoss.Util.Method;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class VideoFragment extends Fragment {

    private Method method;
    private OnClick onClick;
    private String root;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ViewAdapter viewAdapter;
    private MaterialTextView textView_noData;
    private LayoutAnimationController animation;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, container, false);

        onClick = new OnClick() {
            @Override
            public void position(int position, String type) {
                startActivity(new Intent(getActivity(), ShowItem.class)
                        .putExtra("position", position)
                        .putExtra("type", type));
            }
        };
        method = new Method(getActivity(), onClick);

        int resId = R.anim.layout_animation_from_bottom;
        animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);

        progressBar = view.findViewById(R.id.progressbar_fragment);
        recyclerView = view.findViewById(R.id.recyclerView_fragment);
        textView_noData = view.findViewById(R.id.textView_noData_fragment);
        textView_noData.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        new DataVideo().execute();

        return view;

    }

    @SuppressLint("StaticFieldLeak")
    public class DataVideo extends AsyncTask<String, String, String> {

        boolean isVlaue = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                switch (method.url_type()) {
                    case "w": {
                        root = Environment.getExternalStorageDirectory() + BuildConfig.url;
                        File file = new File(root);
                        Constant.videoArray.clear();
                        Constant.videoArray = getListFiles(file);
                        break;
                    }
                    case "wb": {
                        root = Environment.getExternalStorageDirectory() + BuildConfig.url_second;
                        File file = new File(root);
                        Constant.videoArray.clear();
                        Constant.videoArray = getListFiles(file);
                        break;
                    }
                    case "wball": {
                        root = Environment.getExternalStorageDirectory() + BuildConfig.url;
                        File file = new File(root);
                        Constant.videoArray.clear();
                        Constant.videoArray = getListFiles(file);
                        root = Environment.getExternalStorageDirectory() + BuildConfig.url_second;
                        File file_second = new File(root);
                        Constant.videoArray.addAll(getListFiles(file_second));
                        break;
                    }
                }
            } catch (Exception e) {
                isVlaue = true;
                Log.d("error", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (!isVlaue) {
                if (Constant.videoArray.size() == 0) {
                    textView_noData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    textView_noData.setVisibility(View.GONE);
                    viewAdapter = new ViewAdapter(getActivity(), Constant.videoArray, "video", method);
                    recyclerView.setAdapter(viewAdapter);
                    recyclerView.setLayoutAnimation(animation);
                }
            } else {
                textView_noData.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);

            super.onPostExecute(s);
        }
    }

    private List<File> getListFiles(File parentDir) {
        List<File> inFiles = new ArrayList<>();
        try {
            Queue<File> files = new LinkedList<>(Arrays.asList(parentDir.listFiles()));
            while (!files.isEmpty()) {
                File file = files.remove();
                if (file.isDirectory()) {
                    files.addAll(Arrays.asList(file.listFiles()));
                } else if (file.getName().endsWith(".mp4")) {
                    inFiles.add(file);
                }
            }
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return inFiles;
    }

}
