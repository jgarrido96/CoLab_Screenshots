package com.example.myapplication.Fragments;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FileAdapter;
import com.example.myapplication.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class InternalFragment extends Fragment {

    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private List<File> fileList;
    private ImageView img_back;
    private TextView tv_path_holder;
    File storage;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_internal, container, false);

        tv_path_holder = view.findViewById(R.id.tv_path_holder);
        img_back = view.findViewById(R.id.img_back);



        String internal_storage = System.getenv("EXTERNAL_STORAGE");
        String more_stuff = internal_storage + "/Pictures";
        storage = new File(more_stuff);

        tv_path_holder.setText(storage.getAbsolutePath());
        runtimePermission();

        return view;

    }

    private void runtimePermission() {
        Dexter.withContext(getContext()).withPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                displayFiles();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findFiles(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File single_file : files) {
//            if(single_file.isDirectory() && !single_file.isHidden()){
                arrayList.add(single_file);
//            }
        }
//        for (File single_file : files){
//            if (single_file.getName().toLowerCase().endsWith(".jpeg") ||
//                    single_file.getName().toLowerCase().endsWith(".jpg") ||
//                    single_file.getName().toLowerCase().endsWith(".png"))
//            {
//                arrayList.add(single_file);
//            }
//        }
        return arrayList;
    }

    private void displayFiles() {
        recyclerView = view.findViewById(R.id.recycler_internal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fileList = new ArrayList<>();
        fileList.addAll(findFiles(storage));
        fileAdapter = new FileAdapter(fileList, getContext());
        recyclerView.setAdapter(fileAdapter);
    }
}