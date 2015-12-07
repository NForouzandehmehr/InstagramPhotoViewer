package com.example.najmehf.instagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by najmeh.f on 12/3/2015.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<instagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<instagramPhoto> objects) {
        super(context,android.R.layout.simple_list_item_1, objects);
    }
    @Override
    public View getView(int position,View convertView, ViewGroup parent){
        instagramPhoto photo=getItem(position);
        if(convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent, false);

        }
        DeviceDimensionHelper dh= new DeviceDimensionHelper();
        TextView tvCaption =(TextView) convertView.findViewById(R.id.tvCaption);
        TextView comment1= (TextView) convertView.findViewById(R.id.comment1);
        TextView comment2=(TextView) convertView.findViewById(R.id.comment2);
        ImageView ivPhoto= (ImageView)convertView.findViewById(R.id.ivPhoto);
        int ImageWidth= dh.getDisplayWidth(getContext());
        //String formattedText = "This <i>is</i> a <b>test</b> of <a href='http://foo.com'>html</a>";
        String formatted="<font color='#000080'>"+photo.username+ "</font>" + "&nbsp;"+ photo.caption;
        tvCaption.setText(Html.fromHtml(formatted));
        String usercomment1= "<font color='#000080'>"+photo.commentsUsers.get(0)+ "</font>" + "&nbsp;"+ photo.comments.get(0);
        String userComment2="<font color='#000080'>"+photo.commentsUsers.get(1)+ "</font>" + "&nbsp;"+ photo.comments.get(1);
        comment1.setText(Html.fromHtml(usercomment1));
        comment2.setText(Html.fromHtml(userComment2));
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ImageWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ivPhoto.setLayoutParams(lp);
        return convertView;
    }

}
