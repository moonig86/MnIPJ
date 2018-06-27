package mn.com.tpj_mnlibtools.gps;

import java.io.Serializable;

/**
 * Created by Ingeol Moon on 2018-06-25.
 */

public class MnPlaceDTO implements Serializable {
    /**
     * mStatus 상태값
     * OK : 오류가 발생되지 않았음을 나타냄. 장소가 성공적으로 감지되었고 최소 한 개 이상의 결과가 반환
     * ZERO_RESULTS : 검색에 성공했지만 반환된 결과가 없음
     * OVER_QUERY_LIMIT : 할당량이 초과
     * REQUEST_DENIED : 요청거부
     * INVALID_REQUEST : 필수 검색 매개변수(location 또는 radius)가 누락
     * UNKNOWN_ERROR : 서버측 오류. 다시 시도할 것
     * */
    public static final String REQUEST_OK                   = "OK";
    public static final String REQUEST_ZERO_RESULTS         = "ZERO_RESULTS";
    public static final String REQUEST_OVER_QUERY_LIMIT     = "OVER_QUERY_LIMIT";
    public static final String REQUEST_REQUEST_DENIED       = "REQUEST_DENIED";
    public static final String REQUEST_INVALID_REQUEST      = "INVALID_REQUEST";
    public static final String REQUEST_UNKNOWN_ERROR        = "UNKNOWN_ERROR";

    // location 관련 변수
    public double mLoc_lat;
    public double mLoc_lng;
    public double mVp_n_lat;
    public double mVp_n_lng;
    public double mVp_s_lat;
    public double mVp_s_lng;

    // store info
    public String mIcon;
    public String mId;
    public String mName;
    public boolean isOpen_now;
    public String mWeekday;
    public int mPhoto_height;
    public int mPhoto_width;
    public String mPhoto_ref;
    public String mPhoto_html;
    public String mPlaceId;
    public float mRating;
    public String mRef;
    public String mScope;
    public String mType;
    public String mVicinity;

    // store detail info(중복정보 제외)
    public String d_address_components;
    public String d_formatted_address;
    public String d_formatted_tel;
    public String d_icon;
    public String d_opentime;
    public String d_website;               // 상점 홈페이지
    public String d_companyNum;            // 상점 사업자번호
    public String d_profile;               // 상점 소개
    public String d_msg;                   // 상점 msg(비고)
}
