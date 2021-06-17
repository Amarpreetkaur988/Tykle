package com.tykle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tykle.R;

import java.util.List;

/**
 * Created by Naveen Devrani on 04-04-2018.
 */

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder>
{
    Context context;

    List<String> list;

    public HelpAdapter(Context context, List<String> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from( context ).inflate( R.layout.custom_help,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HelpAdapter.ViewHolder holder, int position)
    {
        holder.text.setText(list.get(0 ));
    }

    @Override
    public int getItemCount()
    {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text;
        public ViewHolder(View itemView)
        {
            super( itemView );
        text=itemView.findViewById( R.id.text );

        }
    }
}
