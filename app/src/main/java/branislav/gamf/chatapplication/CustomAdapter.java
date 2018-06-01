package branislav.gamf.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class CustomAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<ContactItem> mContacts;
    public CustomAdapter(Context mContext) {
        this.mContext = mContext;
        this.mContacts = new ArrayList<ContactItem>();
    }

    public void AddContact(ContactItem contact){
        mContacts.add(contact);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        Object retValue = null;
        try{
            retValue = mContacts.get(position);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return retValue;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_layout,null);
            final ViewHolder holder = new ViewHolder();
            final View bundleConvertView = convertView;
            holder.firstLetter = (TextView) convertView.findViewById(R.id.firstLetter);
            holder.fullName = (TextView) convertView.findViewById(R.id.fullName);
            holder.nextButton = (ImageView) convertView.findViewById(R.id.nextButton);
            holder.nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView text = bundleConvertView.findViewById(R.id.fullName);

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PrefsFile",MODE_PRIVATE).edit();
                    editor.putString("reciever_username",text.getText().toString());
                    editor.apply();

                    Intent intent = new Intent(v.getContext(),MessageActivity.class);
                    v.getContext().startActivity(intent);
                }
            });



            convertView.setTag(holder);

        }


        ContactItem contact = (ContactItem) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.firstLetter.setText(contact.getFirstLetter());
        holder.fullName.setText(contact.getFullName());
        holder.nextButton.setImageDrawable(contact.getImage());

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.firstLetter.setBackgroundColor(color);

        convertView.setTag(holder);

        return convertView;
    }
    private class ViewHolder{
        public TextView firstLetter = null;
        public TextView fullName = null;
        public ImageView nextButton = null;
    }



}
