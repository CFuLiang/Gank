package com.fuliang.gank.sample.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuliang.gank.sample.R;
import com.fuliang.gank.sample.model.ResultsList;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.path;


public class WelfarePreview extends Fragment implements View.OnTouchListener,View.OnClickListener{

    private TextView saveBtn;
    private ImageView imageView;
    private ArrayList<ResultsList> list;
    private int position;
    private float startX;
    private float endX;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welfare_preview,container,false);
        view.setOnTouchListener(this);
        imageView = (ImageView)view.findViewById(R.id.image_view);
        saveBtn = (TextView)view.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        getImage(position);
        return view;
    }

    public void setData(ArrayList<ResultsList> listData,int position,FragmentManager manager){
        list = listData;
        fragmentManager = manager;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        saveImageToGallery(((BitmapDrawable)imageView.getDrawable()).getBitmap());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float alpha = Math.abs(event.getX() - startX)/200;
                if (alpha > 1){
                    alpha = 1;
                }
                imageView.setAlpha(1-alpha);
                break;
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                if (endX == startX){
                    removeSelf();
                }
                if (startX -endX >200){
                    if (position< list.size()-1){
                        position ++;
                        getImage(position);
                    }else {
                        Snackbar.make(getActivity().getWindow().getDecorView(),"到最前面了哦", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                }
                if (startX - endX <-200){
                    if (position > 0){
                        position --;
                        getImage(position);
                    }else {
                        Snackbar.make(getActivity().getWindow().getDecorView(),"到最后了哦", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                }
                imageView.setAlpha(1f);
                break;
        }
        return true;
    }

    private void getImage(int position){
        Picasso.with(getActivity())
                .load(list.get(position).url)
                .placeholder(R.mipmap.xx)
                .into(imageView);
    }

    private void removeSelf(){
        if(fragmentManager != null){
            fragmentManager.beginTransaction()
                    .setCustomAnimations(0,0)
                    .remove(WelfarePreview.this)
                    .commitAllowingStateLoss();
        }
    }

    private void saveImageToGallery(Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "welfare");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Snackbar.make(getActivity().getWindow().getDecorView(),"保存成功",BaseTransientBottomBar.LENGTH_SHORT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Snackbar.make(getActivity().getWindow().getDecorView(),"保存失败，请重试",BaseTransientBottomBar.LENGTH_SHORT);
        } catch (IOException e) {
            e.printStackTrace();
            Snackbar.make(getActivity().getWindow().getDecorView(),"保存失败，请重试",BaseTransientBottomBar.LENGTH_SHORT);
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }

}
