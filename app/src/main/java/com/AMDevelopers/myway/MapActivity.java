package com.AMDevelopers.myway;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.services.android.geocoder.ui.GeocoderAutoCompleteView;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.geocoding.v5.models.GeocodingFeature;

import java.util.ArrayList;

public class MapActivity extends Fragment {

	private MapView mapView;
	public static MapView Map;
	static PolylineOptions DistanceDirections, SpeedDirections;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View map = inflater.inflate(R.layout.activity_map, container, false);
		mapView = (MapView) map.findViewById(R.id.mapView);
		Map = mapView;
		mapView.setStyleUrl(Style.MAPBOX_STREETS);
		mapView.setCenterCoordinate(new LatLng(MainActivity.MyLat, MainActivity.MyLong));
		mapView.setZoomLevel(11);
		mapView.addMarker(new MarkerOptions().position(new LatLng(MainActivity.MyLat, MainActivity.MyLong)).title("ME").snippet("Iam Here"));

		mapView.setOnMapClickListener(new MapView.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				if (MainActivity.NORMAL) {
					if (MainActivity.DestLat == 1000 || MainActivity.DestLong == 1000) {
						mapView.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude())).title("DESTINATION").snippet("Want Be Here"));
						MainActivity.DestLat = point.getLatitude();
						MainActivity.DestLong = point.getLongitude();
					} else {
						Toast.makeText(MainActivity.main.getApplicationContext(), "Destination is already defined , Change It : Press It First To Remove It", Toast.LENGTH_SHORT).show();
					}
				}
				if (MainActivity.TSP) {
					mapView.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude())).title("DESTINATION").snippet("Want Be Here"));
					MainActivity.TSPDests.add(point.getLatitude() + ":" + point.getLongitude());
				}

			}
		});

		mapView.setOnMarkerClickListener(new MapView.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if (MainActivity.NORMAL) {
					if (marker.getTitle().equalsIgnoreCase("DESTINATION")) {
						mapView.removeMarker(marker);
						try {
							mapView.removeAnnotation(DistanceDirections.getPolyline());
							mapView.removeAnnotation(SpeedDirections.getPolyline());
						} catch (Exception e) {
						}
						MainActivity.DestLat = 1000;
						MainActivity.DestLong = 1000;
						MainActivity.Route = "";
						MainActivity.SRoute = "";
						return true;
					}
				}
				if (MainActivity.TSP) {
					if (marker.getTitle().equalsIgnoreCase("DESTINATION")) {
						mapView.removeMarker(marker);
						MainActivity.TSPDests.remove(marker.getPosition().getLatitude() + ":" + marker.getPosition().getLongitude());
						return true;
					}
				}
				return false;
			}
		});

		GeocoderAutoCompleteView autocomplete = (GeocoderAutoCompleteView) map.findViewById(R.id.query);
		autocomplete.setAccessToken("sk.eyJ1IjoicHJvZ3JhbW1lcjEwMTA3IiwiYSI6ImNpazhpcWVqczAwMWx2eW04MTFrYWpsdW0ifQ.xFc3lWlrCByXJ_IFKz3rmQ");
		autocomplete.setType(GeocodingCriteria.TYPE_POI);
		autocomplete.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
			@Override
			public void OnFeatureClick(GeocodingFeature feature) {
				Position position = feature.asPosition();
				mapView.addMarker(new MarkerOptions().position(new LatLng(position.getLatitude(), position.getLongitude())).title("DESTINATION").snippet("Want Be Here"));
				MainActivity.DestLat = position.getLatitude();
				MainActivity.DestLong = position.getLongitude();
				CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(position.getLatitude(), position.getLongitude())).zoom(15).build();
				mapView.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);
			}
		});

		try{
			if (MainActivity.NORMAL){
				mapView.addMarker(new MarkerOptions().position(new LatLng(MainActivity.DestLat, MainActivity.DestLong)).title("DESTINATION").snippet("Want Be Here"));
				mapView.addPolyline(DistanceDirections);
				mapView.addPolyline(SpeedDirections);
			}
			if (MainActivity.TSP){
				for (int i = 1; i < MainActivity.TSPDests.size(); i++){
					String [] LatLong = MainActivity.TSPDests.get(i).split(":");
					double Lat = Double.parseDouble(LatLong[0]);
					double Long = Double.parseDouble(LatLong[1]);
					mapView.addMarker(new MarkerOptions().position(new LatLng(Lat, Long)).title("DESTINATION").snippet("Want Be Here"));
				}
			}
		}catch (Exception ex){}

		mapView.onCreate(savedInstanceState);
		return map;
	}

	@Override
	public void onStart() {
		super.onStart();
		mapView.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		mapView.onStop();
	}

	@Override
	public void onPause()  {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	public static void doRouting(){
		if (MainActivity.Route.equalsIgnoreCase("")){
			Toast.makeText(MainActivity.main.getApplicationContext(), "No Shortest Distance Route Is Defined ... !", Toast.LENGTH_SHORT).show();
			return;
		}
		ArrayList<LatLng> Points = new ArrayList<>();
		String [] Route = MainActivity.Route.replace("[", "").replace("]", "").replace(" ", "").split(",");
		for (int i = 0; i < Route.length; i++){
			Points.add(new LatLng(Double.parseDouble(Route[i].substring(Route[i].indexOf(":") + 1)), Double.parseDouble(Route[i].substring(0, Route[i].indexOf(":")))));
		}
		LatLng [] points = Points.toArray(new LatLng[Points.size()]);
		DistanceDirections = new PolylineOptions().add(points).color(Color.parseColor("#99212F3C")).width(3);
		Map.addPolyline(DistanceDirections);
	}

	public static void doSRouting(){
		if (MainActivity.SRoute.equalsIgnoreCase("")){
			Toast.makeText(MainActivity.main.getApplicationContext(), "No Fastest Route Is Defined ... !", Toast.LENGTH_SHORT).show();
			return;
		}
		ArrayList<LatLng> Points = new ArrayList<>();
		String [] Route = MainActivity.SRoute.replace("[", "").replace("]", "").replace(" ", "").split(",");
		for (int i = 0; i < Route.length; i++){
			Points.add(new LatLng(Double.parseDouble(Route[i].substring(Route[i].indexOf(":") + 1)), Double.parseDouble(Route[i].substring(0, Route[i].indexOf(":")))));
		}
		LatLng [] points = Points.toArray(new LatLng[Points.size()]);
		SpeedDirections = new PolylineOptions().add(points).color(Color.parseColor("#99145A32")).width(3);
		Map.addPolyline(SpeedDirections);
	}
}
