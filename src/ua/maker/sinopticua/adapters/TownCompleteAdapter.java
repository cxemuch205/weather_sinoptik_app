package ua.maker.sinopticua.adapters;

import java.util.ArrayList;
import java.util.List;

import ua.maker.sinopticua.R;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.models.ItemTown;
import ua.maker.sinopticua.utils.Tools;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class TownCompleteAdapter extends ArrayAdapter<ItemTown> implements Filterable {

    private Context context;
    private List<ItemTown> data;

    public TownCompleteAdapter(Context context, List<ItemTown> data) {
        super(context, R.layout.item_town_complite_layout, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_town_complite_layout, null);

            holder = new ViewHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_name_town);
            holder.tvDetail = (TextView) view.findViewById(R.id.tv_detail_location);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemTown item = data.get(position);
        holder.tvName.setText(item.getNameTown());
        holder.tvDetail.setText(item.getDetailLocation());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_town_complite_layout, null);

            holder = new ViewHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_name_town);
            holder.tvDetail = (TextView) view.findViewById(R.id.tv_detail_location);

            initTypefaces(holder);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemTown item = data.get(position);
        holder.tvName.setText(item.getNameTown());
        holder.tvDetail.setText(item.getDetailLocation());

        return view;
    }

    private void initTypefaces(ViewHolder holder) {
        holder.tvName.setTypeface(Tools.getFont(context, App.MTypeface.ROBOTO_MEDIUM));
        holder.tvDetail.setTypeface(Tools.getFont(context, App.MTypeface.ROBOTO_LIGHT));
    }

    @Override
    public ItemTown getItem(int position) {
        return data.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null & results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<ItemTown> results = new ArrayList<ItemTown>();
                if (constraint != null) {
                    if (data.size() > 0) {
                        for (ItemTown item : data) {
                            if (item.getNameTown().contains(constraint)) {
                                results.add(item);
                            }
                        }
                    }
                    filterResults.values = results;
                }
                return filterResults;
            }
        };
        return filter;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvDetail;
    }
}
