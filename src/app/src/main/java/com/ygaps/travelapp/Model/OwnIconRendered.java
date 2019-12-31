package com.ygaps.travelapp.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.ygaps.travelapp.R;

public class OwnIconRendered extends DefaultClusterRenderer<MyMarkerItem> {
    private Context context;
    public OwnIconRendered(Context context, GoogleMap map, ClusterManager<MyMarkerItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MyMarkerItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        BitmapDrawable bitmapdraw=(BitmapDrawable) context.getResources().getDrawable(R.drawable.library);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 60, 60, false)));
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());

    }


    @Override
    protected void onBeforeClusterRendered(Cluster<MyMarkerItem> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
        BitmapDrawable bitmapdraw=(BitmapDrawable) context.getResources().getDrawable(R.drawable.library);
        //copy.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), width, height, false)));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 60, 60, false)));

    }
}
