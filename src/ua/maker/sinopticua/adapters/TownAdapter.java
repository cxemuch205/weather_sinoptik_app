package ua.maker.sinopticua.adapters;

import java.util.List;

import ua.maker.sinopticua.R;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.models.ItemTown;
import ua.maker.sinopticua.utils.Tools;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TownAdapter extends ArrayAdapter<ItemTown>{
	
	private Context mContext;
	private List<ItemTown> data;
	private onClearItemListener clearListener;

	public TownAdapter(Context context,	List<ItemTown> data) {
		super(context, R.layout.item_town_layout, data);
		this.data = data;
		this.mContext = context;
	}
	
	@Override
	public ItemTown getItem(int position) {
		return data.get(position);
	}
	
	public void setClearItemListener(onClearItemListener listener){
		this.clearListener = listener;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		
		if(view == null){
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			view = inflater.inflate(R.layout.item_town_layout, null);
			
			holder = new ViewHolder();
			holder.tvNameTown = (TextView)view.findViewById(R.id.tv_name_town);
			holder.ivClear = (ImageView)view.findViewById(R.id.iv_clear);

            initTypefaces(holder);

            view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		ItemTown item = data.get(position);
		final int localPos = position;
		holder.tvNameTown.setText(item.getNameTown());
		holder.ivClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(clearListener != null)
					clearListener.onClearingStart(localPos);
				data.remove(localPos);
				notifyDataSetChanged();
				if(clearListener != null)
					clearListener.onClearingEnd(localPos);
			}
		});
		
		return view;
	}

    private void initTypefaces(ViewHolder holder) {
        holder.tvNameTown.setTypeface(Tools.getFont(mContext, App.MTypeface.ROBOTO_LIGHT));
    }

    static class ViewHolder{
		TextView tvNameTown;
		ImageView ivClear;
	}
	
	public interface onClearItemListener{
		public void onClearingStart(int position);
		public void onClearingEnd(int position);
	}

}
