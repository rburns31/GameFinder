package com.gamefinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 *
 * Created by Ryan on 4/21/2016.
 */
public class ManageTvsRecyclerAdapter extends RecyclerView.Adapter<ManageTvsRecyclerAdapter.ViewHolder> {
    private Context parentContext;
    private List<TelevisionResponse> responseBody;
    private int selectedIndex;
    private List<CheckBox> allCheckBoxes = new ArrayList<>();

    public ManageTvsRecyclerAdapter() {
        super();

        // Get the televisions from the database
        Call<List<TelevisionResponse>> getTelevisionsCall
                = ApiUtils.service.getTelevisions(ApiUtils.accessToken, ApiUtils.client, ApiUtils.uid);
        try {
            responseBody = getTelevisionsCall.execute().body();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // Set the selected index from the DB
        for (int i = 0; i < responseBody.size(); i++) {
            TelevisionResponse tv = responseBody.get(i);
            if (tv.getSelected()) {
                selectedIndex = i;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        parentContext = parent.getContext();
        View view = LayoutInflater.from(parentContext).inflate(R.layout.tv_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TelevisionResponse tv = responseBody.get(position);

        //
        holder.tvName.setText(tv.getName());
        String tvBrandText = "Brand: " + tv.getBrand();
        holder.tvBrand.setText(tvBrandText);
        String tvCableText = "Cable Company: " + tv.getCable_company();
        holder.tvCable.setText(tvCableText);

        final CheckBox thisCheckBox = (CheckBox) holder.itemView.findViewById(R.id.checkBox);
        allCheckBoxes.add(thisCheckBox);

        //
        if (selectedIndex == position) {
            thisCheckBox.setChecked(true);
        } else {
            thisCheckBox.setChecked(false);
        }

        thisCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If not clicking the already selected checkbox
                if (selectedIndex != position) {
                    // Un-check the last checkbox
                    allCheckBoxes.get(selectedIndex).setChecked(false);

                    selectedIndex = position;
                } else {
                    thisCheckBox.setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (responseBody == null) {
            return 0;
        }
        return responseBody.size();
    }

    public int getSelectedTvId() {
        return responseBody.get(selectedIndex).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName;
        protected TextView tvBrand;
        protected TextView tvCable;
        protected CheckBox selectCheckbox;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvBrand = (TextView) itemView.findViewById(R.id.tvBrand);
            tvCable = (TextView) itemView.findViewById(R.id.tvCable);
            selectCheckbox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}