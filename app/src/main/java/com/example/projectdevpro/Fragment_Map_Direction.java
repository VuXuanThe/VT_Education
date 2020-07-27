package com.example.projectdevpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.projectdevpro.Object.Teacher;
import com.example.projectdevpro.databinding.FragmentMapDirectionBinding;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import static androidx.core.app.ActivityCompat.finishAffinity;

public class Fragment_Map_Direction extends Fragment {
    private static final String TAG = "FragmentFindTeacher";
    FragmentMapDirectionBinding binding;
    private AndroidXMapFragment mapFragment;
    private Map map;
    private PositioningManager positioningManager = null;
    private PositioningManager.OnPositionChangedListener positionListener;
    GeoCoordinate teacherPosition = null;
    private NavigationManager m_navigationManager;
    private GeoBoundingBox m_geoBoundingBox;
    private Route m_route;
    private boolean m_foregroundServiceStarted;

    public static Fragment_Map_Direction newInstance(Bundle bundle) {
        Fragment_Map_Direction fragment = new Fragment_Map_Direction();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_direction, container, false);
        Bundle bundle = getArguments();
        Teacher teacher = (Teacher) bundle.getSerializable("Teacher");
        teacherPosition = new GeoCoordinate(teacher.getLatitude(), teacher.getLongitude());
        ApplicationContext context = new ApplicationContext(getContext());
        initMapFragment(context, teacher);
        return binding.getRoot();
    }

    private void initMapFragment(ApplicationContext context, final Teacher teacher) {
        String diskCacheRoot = getActivity().getFilesDir().getPath()
                + File.separator + ".isolated-here-maps";

        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(diskCacheRoot);
        if(!success)
            Toast.makeText(getActivity().getApplicationContext(), "Unable to set isolated disk cache path.", Toast.LENGTH_SHORT).show();
        else{
            mapFragment = new AndroidXMapFragment();
            getFragmentManager().beginTransaction().add(R.id.MapFragmentDirection, mapFragment, "MAP_TAG").commit();
            mapFragment.init(context, new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(
                        final OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE){
                        map = mapFragment.getMap();
                        map.setZoomLevel(15);
                        map.setTilt((map.getMinTilt() + map.getMaxTilt())/2);
                        map.setProjectionMode(Map.Projection.MERCATOR);
                        map.getPositionIndicator().setVisible(true);
                        Image teacher_room = new Image();
                        teacher_room.setBitmap(createAvatarUser(teacher.getAvatar()));
                        map.addMapObject(new MapMarker(teacherPosition, teacher_room));
                        updateCurrentPosition();
                        GPSLocation currentPosition = new GPSLocation(getContext());
                        binding.edtCurrentLocation.setText(Adress(currentPosition.getLongitude(), currentPosition.getLatitude()));
                        createRoute(currentPosition);
                        m_navigationManager = NavigationManager.getInstance();
                    }
                    else{
                        System.out.println("ERROR: Cannot initialize Map Fragment");
                        new AlertDialog.Builder(getActivity()).setMessage(
                                "Error : " + error.name() + "\n\n" + error.getDetails())
                                .setTitle(R.string.engine_init_error)
                                .setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                finishAffinity(getActivity());
                                            }
                                        }).create().show();
                    }
                }
            });
        }
    }

    private Bitmap createAvatarUser(Bitmap bitmap) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dp(62), dp(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = getResources().getDrawable(R.drawable.boder_user_marker, null);
            drawable.setBounds(0, 0, dp(62), dp(76));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();
            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(5), dp(5), dp(52 + 5), dp(52 + 5));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {}
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }

    private void updateCurrentPosition(){
        positioningManager = PositioningManager.getInstance();
        positionListener = new PositioningManager.OnPositionChangedListener() {
            @Override
            public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
                map.setCenter(geoPosition.getCoordinate(), Map.Animation.BOW);
            }
            @Override
            public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) { }};
        try {
            positioningManager.addListener(new WeakReference<>(positionListener));
            if(!positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK)) {
                Log.e("HERE", "PositioningManager.start: Failed to start...");
            }
        } catch (Exception e) {
            Log.e("HERE", "Caught: " + e.getMessage());
        }
        map.getPositionIndicator().setVisible(true);
    }

    private String Adress(double longitude, double latitude){
        Geocoder geocoder;
        String address = null;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    private void createRoute(GPSLocation currentPosition) {
        CoreRouter coreRouter = new CoreRouter();
        RoutePlan routePlan = new RoutePlan();
        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setTransportMode(RouteOptions.TransportMode.SCOOTER);
        routeOptions.setHighwaysAllowed(false);
        routeOptions.setRouteType(RouteOptions.Type.FASTEST);
        routeOptions.setRouteCount(1);
        routePlan.setRouteOptions(routeOptions);
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(currentPosition.getLatitude(), currentPosition.getLongitude()));
        RouteWaypoint destination = new RouteWaypoint(teacherPosition);

        routePlan.addWaypoint(startPoint);
        routePlan.addWaypoint(destination);

        coreRouter.calculateRoute(routePlan,
                new Router.Listener<List<RouteResult>, RoutingError>() {
                    @Override
                    public void onProgress(int i) {
                        /* The calculation progress can be retrieved in this callback. */
                    }
                    @Override
                    public void onCalculateRouteFinished(List<RouteResult> routeResults,
                                                         RoutingError routingError) {
                        if (routingError == RoutingError.NONE) {
                            if (routeResults.get(0).getRoute() != null) {
                                m_route = routeResults.get(0).getRoute();
                                //m_route.getFirstManeuver().toString()
//                                for(int i = 0; i < m_route.getManeuvers().size(); i++)
//                                Toast.makeText(getActivity(), m_route.getManeuvers().get(i).getNextRoadName(), Toast.LENGTH_SHORT).show();

                                MapRoute mapRoute = new MapRoute(routeResults.get(0).getRoute());
                                mapRoute.setManeuverNumberVisible(true);
                                map.addMapObject(mapRoute);
                                m_geoBoundingBox = routeResults.get(0).getRoute().getBoundingBox();
                                map.zoomTo(m_geoBoundingBox, Map.Animation.NONE,
                                        Map.MOVE_PRESERVE_ORIENTATION);

                                //startNavigation();
                            } else {
                                Toast.makeText(getContext(), "Error:route results returned is not valid", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error:route calculation returned error code: " + routingError, Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void startNavigation(){
        m_navigationManager.setMap(map);
        m_navigationManager.simulate(m_route, 60);
        //m_navigationManager.startNavigation(m_route);
        startForegroundService();
        m_navigationManager.setMapUpdateMode(NavigationManager.MapUpdateMode.ROADVIEW);
    }

    private void  startForegroundService() {
        if (!m_foregroundServiceStarted) {
            m_foregroundServiceStarted = true;
            Intent startIntent = new Intent(getContext(), ForegroundService.class);
            startIntent.setAction(ForegroundService.START_ACTION);
            getActivity().getApplicationContext().startService(startIntent);
        }
    }
}
