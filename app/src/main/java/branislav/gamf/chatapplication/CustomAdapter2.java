package branislav.gamf.chatapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

public class CustomAdapter2 extends BaseAdapter {

    private Context mContext;
    private ArrayList<MessageItem> mMessages;

    public CustomAdapter2(Context mContext) {
        this.mContext = mContext;
        mMessages = new ArrayList<MessageItem>();
    }

    public void update(ArrayList<Message> messages,String senderID){
        mMessages.clear();
        int i=0;
        if(messages != null){
            while(!messages.isEmpty()){
                MessageItem temp = new MessageItem(messages.get(i).getmMessage(), true, messages.get(i).getmMessageID());
                if(messages.get(i).getmSenderID().equals(senderID)) {
                    temp.setBackgroundColor(false);
                }
                mMessages.add(temp);
                messages.remove(messages.get(i));

            }
        }
        notifyDataSetChanged();

    }
    public void AddMessage(MessageItem message){
        mMessages.add(message);
        notifyDataSetChanged();
    }

    public void RemoveMessage(MessageItem message){
        mMessages.remove(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    public MessageItem getMsg(int i){
        return mMessages.get(i);
    }

    @Override
    public Object getItem(int position) {
        Object retValue = null;
        try{
            retValue = mMessages.get(position);
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
            convertView = inflater.inflate(R.layout.message_layout,null);
            ViewHolder holder = new ViewHolder();
            holder.messageText = (TextView) convertView.findViewById(R.id.messageText);
            convertView.setTag(holder);

        }

        MessageItem message = (MessageItem) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.messageText.setText(message.getMessage());
        //backgroundColod == true left
        //                == false right
        if(message.isBackgroundColor() == true){
            holder.messageText.setBackgroundColor(Color.parseColor("#C0C0C0"));
            holder.messageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);;
        }
        else{
            holder.messageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.messageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);;
        }

        return convertView;
    }

    private class ViewHolder{
        public TextView messageText = null;
        public boolean color = false;
    }
}
