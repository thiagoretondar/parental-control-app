package fei.tcc.parentalcontrol.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fei.tcc.parentalcontrol.R;
import fei.tcc.parentalcontrol.vo.AppVo;

/**
 * Created by thiagoretondar on 7/23/16.
 */
public class SelectableAdapter extends ArrayAdapter<AppVo> {

    private SparseBooleanArray mSelectedItemsIds;
    private LayoutInflater inflater;
    private Context mContext;
    private List<AppVo> list;

    public SelectableAdapter(Context context, int resource, List<AppVo> objects) {
        super(context, resource, objects);
        mSelectedItemsIds = new SparseBooleanArray();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.list = objects;
    }

    private static class ViewHolder {
        TextView itemName;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.app_list_row, null);
            holder.itemName = (TextView) view.findViewById(R.id.app_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // TODO change here what should be displayed?
        holder.itemName.setText(list.get(position).getName());
        return view;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }

        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public void remove(AppVo appVo) {
        list.remove(appVo);
        notifyDataSetChanged();
    }

}
