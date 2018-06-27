package mn.com.tpj_mnlibtools.gps;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import mn.com.ipj_mnctools.log.MnLog;

/**
 * Created by Ingeol Moon on 2018-06-22.
 */

public class MnGPS {
    public static final String TAG = MnGPS.class.getSimpleName();

    private final String GOOGLE_PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=";
    private final String GOOGLE_STOREINFO_URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";

    private Activity mActivity;
    private Context mContext;

    private String k;                       // google map apikey
    private LatLng l;                       // now place latlng data
    private int r;                          // radius(범위)
    private String st;                      // store type(검색할 상점 type), place_type.txt 참조

    private ArrayList<String> jList;        // json data list
    private ArrayList<MnPlaceDTO> pList;    // place info list


    public MnGPS(Activity activity){
        this.mActivity = activity;
        this.mContext = mActivity.getApplicationContext();
    }


    public LatLng getThisLocation(){
        LatLng nowLatLng = null;

        MnGPSInfo gpsInfo = new MnGPSInfo(mActivity);
        if(gpsInfo.isGetLocation){
            double latitude = gpsInfo.getLatitude();
            double longitude = gpsInfo.getLongitude();

            MnLog.d(TAG, "lat: " + latitude + "\nlng: " + longitude);
            nowLatLng = new LatLng(latitude, longitude);
        }
        else{
            gpsInfo.showSettingsAlert();
        }

        return nowLatLng;
    }


    public void setData_apiKey(String s){
        // s : apikey
        this.k = s;
    }


    public void setData_forPlace_locInfo(LatLng l, int i){
        // l : 위도, 경도 데이터
        // s : radius (반경 범위)
        this.l = l;
        this.r = i;
    }


    public void setData_forPlace_type(String s){
        this.st = s;
    }


    public void start_forPlace(){
        String lat = String.valueOf(l.latitude);
        String lng = String.valueOf(l.longitude);

        String url = GOOGLE_PLACES_URL+k+"&location="+lat+","+lng+"&radius="+r+"&type="+st+"&language=ko";
        MnTask_p task_p = new MnTask_p(url);
        task_p.start();
    }


    private class MnTask_p extends Thread {
        String defaultUrl = "";
        String jsonData  = "";
        int keyCnt = 0;
        boolean isPageToken = false;


        public MnTask_p(String url){
            this.defaultUrl = url;
        }


        @Override
        public void run() {
            jList = new ArrayList<>();
            String strUrl = defaultUrl;
            HttpURLConnection conn = null;

            do{
                if(keyCnt > 0){
                    try{
                        Thread.sleep(1500);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                jsonData = setHttpURLConnect(strUrl);
                jList.add(jsonData);

                try{
                    JSONObject jobj = new JSONObject(jsonData);

                    if(jobj.has("next_page_token")){
                        String pageToken = jobj.getString("next_page_token");
                        strUrl = defaultUrl+"&pagetoken="+pageToken;

                        isPageToken = true;
                    }
                    else{
                        isPageToken = false;
                    }
                    keyCnt++;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }while(isPageToken);

            pList = new ArrayList<>();

            try{
                for(int i=0; i<jList.size(); i++){
                    JSONObject jobj = new JSONObject(jList.get(i));
                    JSONArray jArr = jobj.getJSONArray("results");
                    String status = jobj.getString("status");

                    if(status.equals(MnPlaceDTO.REQUEST_OK)){
                        MnLog.d(TAG, "result length : "+jArr.length());

                        for(int j=0; j<jArr.length(); j++){
                            JSONObject jsonObject = jArr.getJSONObject(j);

                            String geometry = getJsonString(jsonObject, "geometry");
                            if(geometry.equals("")){
                                continue;
                            }

                            JSONObject jsonGeometry = new JSONObject(geometry);

                            String location = getJsonString(jsonGeometry, "location");
                            if(location.equals("")){
                                continue;
                            }

                            JSONObject jsonLocation = new JSONObject(location);
                            MnPlaceDTO placeDTO = new MnPlaceDTO();
                            placeDTO.mLoc_lat = Double.parseDouble(getJsonString(jsonLocation, "lat"));
                            placeDTO.mLoc_lng = Double.parseDouble(getJsonString(jsonLocation, "lng"));
                            MnLog.d(TAG, "mLoc_lat : "+placeDTO.mLoc_lat);
                            MnLog.d(TAG, "mLoc_lng : "+placeDTO.mLoc_lng);

                            String viewport = getJsonString(jsonGeometry, "viewport");
                            JSONObject jsonViewport = new JSONObject(viewport);
                            String northeast = getJsonString(jsonViewport, "northeast");
                            JSONObject jsonNortheast = new JSONObject(northeast);
                            placeDTO.mVp_n_lat = Double.parseDouble(getJsonString(jsonNortheast, "lat"));
                            placeDTO.mVp_n_lng = Double.parseDouble(getJsonString(jsonNortheast, "lng"));
                            MnLog.d(TAG, "mVp_n_lat : "+placeDTO.mVp_n_lat);
                            MnLog.d(TAG, "mVp_n_lng : "+placeDTO.mVp_n_lng);

                            String southwest = getJsonString(jsonViewport, "southwest");
                            JSONObject jsonSouthwest = new JSONObject(southwest);
                            placeDTO.mVp_s_lat = Double.parseDouble(getJsonString(jsonSouthwest, "lat"));
                            placeDTO.mVp_s_lng = Double.parseDouble(getJsonString(jsonSouthwest, "lng"));
                            MnLog.d(TAG, "mVp_s_lat : "+placeDTO.mVp_s_lat);
                            MnLog.d(TAG, "mVp_s_lng : "+placeDTO.mVp_s_lng);

                            placeDTO.mIcon = getJsonString(jsonObject, "icon");
                            MnLog.d(TAG, "icon : "+placeDTO.mIcon);

                            placeDTO.mId = getJsonString(jsonObject, "id");
                            MnLog.d(TAG, "id : "+placeDTO.mId);

                            placeDTO.mName = getJsonString(jsonObject, "name");
                            MnLog.d(TAG, "name : "+placeDTO.mName);

                            String openHours = getJsonString(jsonObject, "opening_hours");
                            if(!openHours.equals("")){
                                JSONObject jsonOpen = new JSONObject(openHours);
                                placeDTO.isOpen_now = Boolean.valueOf(getJsonString(jsonOpen, "open_now"));
                                MnLog.d(TAG, "open_state : "+placeDTO.isOpen_now);
                            }

                            String photos = getJsonString(jsonObject, "photos");
                            if(!photos.equals("")){
                                MnLog.d(TAG, "photos : "+photos);
                                JSONArray jsonPhotoArr = new JSONArray(photos);

                                for(int k=0; k<jsonPhotoArr.length(); k++){
                                    JSONObject jsonPhoto = jsonPhotoArr.getJSONObject(k);

                                    placeDTO.mPhoto_height = Integer.parseInt(getJsonString(jsonPhoto, "height"));
                                    MnLog.d(TAG, "mPhoto_height : "+placeDTO.mPhoto_height);

                                    placeDTO.mPhoto_ref = getJsonString(jsonPhoto, "photo_reference");
                                    MnLog.d(TAG, "mPhoto_ref : "+placeDTO.mPhoto_ref);

                                    placeDTO.mPhoto_width = Integer.parseInt(getJsonString(jsonPhoto, "width"));
                                    MnLog.d(TAG, "mPhoto_width : "+placeDTO.mPhoto_width);
                                }
                            }

                            placeDTO.mPlaceId = getJsonString(jsonObject, "place_id");
                            MnLog.d(TAG, "place_id : "+placeDTO.mPlaceId);

                            String rating = getJsonString(jsonObject, "rating");
                            if(!rating.equals("")){
                                placeDTO.mRating = Float.parseFloat(rating);
                            }
                            else{
                                placeDTO.mRating = 0;
                            }
                            MnLog.d(TAG, "mRating : "+placeDTO.mRating);

                            placeDTO.mRef = getJsonString(jsonObject, "reference");
                            MnLog.d(TAG, "ref : "+placeDTO.mRef);

                            placeDTO.mScope = getJsonString(jsonObject, "scope");
                            MnLog.d(TAG, "scope : "+placeDTO.mScope);

                            placeDTO.mType = getJsonString(jsonObject, "types");
                            MnLog.d(TAG, "types : "+placeDTO.mType);

                            placeDTO.mVicinity = getJsonString(jsonObject, "vicinity");
                            MnLog.d(TAG, "vicinity : "+placeDTO.mVicinity);

                            pList.add(placeDTO);
                        }
                    }
                    else if(status.equals(MnPlaceDTO.REQUEST_ZERO_RESULTS)){
                        Toast.makeText(mContext, "request data is null", Toast.LENGTH_SHORT).show();
                    }
                    else if(status.equals(MnPlaceDTO.REQUEST_OVER_QUERY_LIMIT)){
                        Toast.makeText(mContext, "할당량 초과", Toast.LENGTH_SHORT).show();
                    }
                    else if(status.equals(MnPlaceDTO.REQUEST_REQUEST_DENIED)){
                        Toast.makeText(mContext, "요청 거부", Toast.LENGTH_SHORT).show();
                    }
                    else if(status.equals(MnPlaceDTO.REQUEST_INVALID_REQUEST)){
                        Toast.makeText(mContext, "필수 검색 매개변수 누락", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            String placeInfoJsonData = "";

            for(int i=0; i<pList.size(); i++){
                placeInfoJsonData = setHttpURLConnect(GOOGLE_STOREINFO_URL+pList.get(i).mPlaceId+"key="+k+"&language=ko");

                try{
                    JSONObject jobj = new JSONObject(placeInfoJsonData);
                    String result = jobj.getString("result");
                    String status = jobj.getString("status");

                    if(status.equals(MnPlaceDTO.REQUEST_OK)){
                        JSONObject jsonResult = new JSONObject(result);

                        pList.get(i).d_formatted_tel = getJsonString(jsonResult, "formatted_phone_number");
                        MnLog.d(TAG, "phone_number : "+pList.get(i).d_formatted_tel);

                        String openTime = getJsonString(jsonResult, "opening_hours");
                        String webSite = getJsonString(jsonResult, "website");

                        pList.get(i).d_opentime = openTime;
                        if(!pList.get(i).d_opentime.equals("")){

                            JSONObject jsonOpenObj = new JSONObject(pList.get(i).d_opentime);
                            JSONArray jsonOpenArr = jsonOpenObj.getJSONArray("weekday_text");

                            String temp = jsonOpenArr.get(0).toString();
                            //temp = temp.replace(" ", "");
                            temp = temp.replace("월요일:", "");
                            temp = temp.trim();

                            pList.get(i).d_opentime = temp;
                            MnLog.d(TAG, "openTime : "+pList.get(i).d_opentime);
                        }

                        pList.get(i).d_website = webSite;

                        // test code(현재 임시값들이 채워준다)
                        Random random = new Random();

                        pList.get(i).d_icon = pList.get(i).mIcon;
                        // test code------------------------------------
                        pList.get(i).d_companyNum = "347-10-00780";
                        // ---------------------------------------------

                    }
                    else if(status.equals(MnPlaceDTO.REQUEST_ZERO_RESULTS)){
                        Toast.makeText(mContext, "request data is null", Toast.LENGTH_SHORT).show();
                    }
                    else if(status.equals(MnPlaceDTO.REQUEST_OVER_QUERY_LIMIT)){
                        Toast.makeText(mContext, "할당량 초과", Toast.LENGTH_SHORT).show();
                    }
                    else if(status.equals(MnPlaceDTO.REQUEST_REQUEST_DENIED)){
                        Toast.makeText(mContext, "요청 거부", Toast.LENGTH_SHORT).show();
                    }
                    else if(status.equals(MnPlaceDTO.REQUEST_INVALID_REQUEST)){
                        Toast.makeText(mContext, "필수 검색 매개변수 누락", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }


        private String setHttpURLConnect(String strurl){
            HttpURLConnection conn = null;
            String result = "";

            try{
                URL url = new URL(strurl);
                conn = (HttpURLConnection)url.openConnection();

                conn.setRequestMethod("POST");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sbb = new StringBuilder();
                String line = null;

                while((line = br.readLine()) != null){
                    sbb.append(line);
                }
                result = sbb.toString();

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                conn.disconnect();
            }

            return result;
        }


        private String getJsonString(JSONObject json, String key){
            String result = "";

            if(json.has(key)){
                try{
                    result = json.getString(key);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            return result;
        }
    }
}
