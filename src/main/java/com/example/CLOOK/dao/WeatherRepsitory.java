package com.example.CLOOK.dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.example.CLOOK.domain.GeocodingVO;
import com.example.CLOOK.domain.WeatherVO;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public interface WeatherRepsitory {

    public static List<WeatherVO> getShortWeather(GeocodingVO geocodingVO)
            throws IOException, ParseException {
        // 현재 시간
        LocalTime now = LocalTime.now();
        // 포맷 정의하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        // 포맷 적용하기
        String formatedNow = now.format(formatter);
        // 포맷 적용된 현재 시간 출력

        int formatnow = Integer.parseInt(formatedNow);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String currentdate = df.format(cal.getTime());

        cal.add(Calendar.DATE, -1);
        String oneth = df.format(cal.getTime());
        System.out.println(oneth);

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        // 홈페이지에서 받은 키
        String serviceKey = "lsreK53XwFXG2rEI3GpisRYQCjg97dt7uTl0HEZnBtYQvqdxXub024qirOptZW3z%2FEJyGQIDVoSWWrzXnUMBxQ%3D%3D";
        String pageNo = "1";
        String numOfRows = "100000";
        String baseDate = "";
        // 조회하고싶은 날짜
        if (formatnow >= 0 & formatnow <= 210) {
            baseDate = oneth;
        } else {
            baseDate = currentdate;
        }
        String baseTime = "0200"; // 조회하고싶은 시간
        String type = "JSON"; // 타입 xml, json 등등 ..
        String nx = geocodingVO.getXLat(); // 위도
        String ny = geocodingVO.getYLon();

        cal.add(Calendar.DATE, 1);
        String twoth = df.format(cal.getTime());
        System.out.println(twoth);
        cal.add(Calendar.DATE, 1);
        String threeth = df.format(cal.getTime());
        System.out.println(threeth);

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
                + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="
                + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
                + URLEncoder.encode(baseTime, "UTF-8"));

        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));
        // urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" +
        // URLEncoder.encode("UTF-8")); //경도
        // urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" +
        // URLEncoder.encode(ny, "UTF-8")); //위도

        URL url = new URL(urlBuilder.toString());
        // 어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        // System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }
        /*
         * StringBuilder sb = new StringBuilder();
         * String line;
         * while ((line = rd.readLine()) != null) {
         * sb.append(line);
         * }
         * rd.close();
         * conn.disconnect();
         * String result = sb.toString();
         * 
         * return result;
         */

        List<WeatherVO> listweatherVO = new ArrayList<WeatherVO>();

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(rd.readLine());
        JSONObject response = (JSONObject) object.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");

        String fcstDate = "";
        String fcstTime = "";

        // String status = (String) response.get("status");
        for (int i = 0; i < item.size(); i++) {
            WeatherVO weatherVO = new WeatherVO();
            object = (JSONObject) item.get(i);
            fcstDate = (String) object.get("fcstDate");
            fcstTime = (String) object.get("fcstTime");
            String category = (String) object.get("category");

            // System.out.println(fcstDate);
            // System.out.println(category);
            if (fcstDate.equals(currentdate)) {

                if (category.equals("POP")) {
                    String result = (String) object.get("fcstValue");
                    weatherVO.setPop(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);
                } else if (category.equals("PTY")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setPty(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("PCP")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setPcp(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("REH")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setReh(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("SNO")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setSno(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("SKY")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setSky(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("VEC")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setVec(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("TMN")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setSno(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("TMX")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setSno(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                }

            } else {

                if (category.equals("PTY")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setPty(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("SKY")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setSky(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("TMN")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setTmn(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                } else if (category.equals("TMX")) {

                    String result = (String) object.get("fcstValue");
                    weatherVO.setTmx(result);
                    weatherVO.setFcstTime(fcstTime);
                    weatherVO.setFcstDate(fcstDate);
                    listweatherVO.add(weatherVO);

                }

            }

        }

        return listweatherVO;

    }

    public static JSONObject getShortWeather2(GeocodingVO geocodingVO)
            throws IOException, ParseException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String currentdate = df.format(cal.getTime());

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        // 홈페이지에서 받은 키
        String serviceKey = "lsreK53XwFXG2rEI3GpisRYQCjg97dt7uTl0HEZnBtYQvqdxXub024qirOptZW3z%2FEJyGQIDVoSWWrzXnUMBxQ%3D%3D";
        String pageNo = "1";
        String numOfRows = "100000";
        String baseDate = currentdate; // 조회하고싶은 날짜
        String baseTime = "0200"; // 조회하고싶은 시간
        String type = "JSON"; // 타입 xml, json 등등 ..
        String nx = geocodingVO.getXLat(); // 위도
        String ny = geocodingVO.getYLon();

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
                + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="
                + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
                + URLEncoder.encode(baseTime, "UTF-8"));

        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));
        // urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" +
        // URLEncoder.encode("UTF-8")); //경도
        // urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" +
        // URLEncoder.encode(ny, "UTF-8")); //위도

        URL url = new URL(urlBuilder.toString());
        // 어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        // System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        JSONObject result = (JSONObject) new JSONParser().parse(sb.toString());

        rd.close();
        conn.disconnect();

        /*
         * List<WeatherVO> listweatherVO = new ArrayList<WeatherVO>();
         * 
         * JSONParser parser = new JSONParser();
         * JSONObject object = (JSONObject) parser.parse(rd.readLine());
         * 
         * 
         * JSONObject response = (JSONObject) object.get("response");
         * JSONObject body = (JSONObject) response.get("body");
         * JSONObject items = (JSONObject) body.get("items");
         * JSONArray item = (JSONArray) items.get("item");
         * 
         * String fcstDate = "";
         * String fcstTime = "";
         * 
         * // String status = (String) response.get("status");
         * for (int i = 0; i < item.size(); i++) {
         * WeatherVO weatherVO = new WeatherVO();
         * object = (JSONObject) item.get(i);
         * fcstDate = (String) object.get("fcstDate");
         * fcstTime = (String) object.get("fcstTime");
         * String category = (String) object.get("category");
         * 
         * 
         * }
         * 
         * rd.close();
         * conn.disconnect();
         */

        return result;

    }

    public static String getShortWeather3(GeocodingVO geocodingVO)
            throws IOException, ParseException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String currentdate = df.format(cal.getTime());

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        // 홈페이지에서 받은 키
        String serviceKey = "lsreK53XwFXG2rEI3GpisRYQCjg97dt7uTl0HEZnBtYQvqdxXub024qirOptZW3z%2FEJyGQIDVoSWWrzXnUMBxQ%3D%3D";
        String pageNo = "1";
        String numOfRows = "100000";
        String baseDate = currentdate; // 조회하고싶은 날짜
        String baseTime = "0200"; // 조회하고싶은 시간
        String type = "JSON"; // 타입 xml, json 등등 ..
        String nx = geocodingVO.getXLat(); // 위도
        String ny = geocodingVO.getYLon();

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
                + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="
                + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
                + URLEncoder.encode(baseTime, "UTF-8"));

        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));
        // urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" +
        // URLEncoder.encode("UTF-8")); //경도
        // urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" +
        // URLEncoder.encode(ny, "UTF-8")); //위도

        URL url = new URL(urlBuilder.toString());
        // 어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        // System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        String result = sb.toString();

        rd.close();
        conn.disconnect();

        /*
         * List<WeatherVO> listweatherVO = new ArrayList<WeatherVO>();
         * 
         * JSONParser parser = new JSONParser();
         * JSONObject object = (JSONObject) parser.parse(rd.readLine());
         * 
         * 
         * JSONObject response = (JSONObject) object.get("response");
         * JSONObject body = (JSONObject) response.get("body");
         * JSONObject items = (JSONObject) body.get("items");
         * JSONArray item = (JSONArray) items.get("item");
         * 
         * String fcstDate = "";
         * String fcstTime = "";
         * 
         * // String status = (String) response.get("status");
         * for (int i = 0; i < item.size(); i++) {
         * WeatherVO weatherVO = new WeatherVO();
         * object = (JSONObject) item.get(i);
         * fcstDate = (String) object.get("fcstDate");
         * fcstTime = (String) object.get("fcstTime");
         * String category = (String) object.get("category");
         * 
         * 
         * }
         * 
         * rd.close();
         * conn.disconnect();
         */

        return result;

    }

    public static WeatherVO getShortPartWeather1(GeocodingVO geocodingVO) throws IOException, ParseException {

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        // 현재 날짜 구하기
        LocalDate nowDate = LocalDate.now();
        // 포맷 정의
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 포맷 적용
        String formatedNowDate = nowDate.format(formatterDate);
        // 결과 출력
        System.out.println(formatedNowDate);

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        // 홈페이지에서 받은 키
        String serviceKey = "lsreK53XwFXG2rEI3GpisRYQCjg97dt7uTl0HEZnBtYQvqdxXub024qirOptZW3z%2FEJyGQIDVoSWWrzXnUMBxQ%3D%3D";
        String pageNo = "1";
        String numOfRows = "100000";
        String baseDate = formatedNowDate; // 조회하고싶은 날짜
        String baseTime = "0200"; // 조회하고싶은 시간
        String type = "JSON"; // 타입 xml, json 등등 ..
        String nx = geocodingVO.getXLat(); // 위도
        String ny = geocodingVO.getYLon();
        ; // 경도

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));

        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
                + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="
                + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
                + URLEncoder.encode(baseTime, "UTF-8"));

        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));
        // urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" +
        // URLEncoder.encode("UTF-8")); //경도
        // urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" +
        // URLEncoder.encode(ny, "UTF-8")); //위도

        /*
         * GET방식으로 전송해서 파라미터 받아오기
         */
        URL url = new URL(urlBuilder.toString());
        // 어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        // System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        /*
         * BufferedReader rd;
         * if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
         * rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),
         * "UTF-8"));
         * } else {
         * rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(),
         * "UTF-8"));
         * }
         * StringBuilder sb = new StringBuilder();
         * String line;
         * while ((line = rd.readLine()) != null) {
         * sb.append(line);
         * }
         * rd.close();
         * conn.disconnect();
         * String result = sb.toString();
         * 
         * return result;
         * /
         */

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        WeatherVO weatherVO = new WeatherVO();

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(rd.readLine());
        JSONObject response = (JSONObject) object.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");

        // String status = (String) response.get("status");
        int counttmn = 0;
        int counttmx = 0;
        for (int i = 0; i < item.size(); i++) {
            object = (JSONObject) item.get(i);
            String category = (String) object.get("category");

            // System.out.println(category);
            if (category.equals("TMN") && counttmn < 1) {
                counttmn += 1;
                String tmn = (String) object.get("fcstValue");
                System.out.println("TMN::" + tmn);
                weatherVO.setTmn(tmn);
            }
            if (category.equals("TMX") && counttmx < 1) {
                counttmx += 1;
                String tmx = (String) object.get("fcstValue");
                System.out.println("TMX::" + tmx);
                weatherVO.setTmx(tmx);
            }
        }

        /*
         * if(status.equals("NOT_FOUND"))
         * {
         * resultString="잘못 설정된 데이터값입니다. 관리자에게 문의해주시기 바랍니다";
         * 
         * }else if(status.equals("ERROR"))
         * {
         * resultString="서버 에러입니다. 관리자에게 문의해주시기 바랍니다";
         * }else{}
         * /
         */

        // System.out.println(item);

        return weatherVO;

    }

    public static WeatherVO getShortPartWeather2(GeocodingVO geocodingVO) throws IOException, ParseException {

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        // 현재 날짜 구하기
        LocalDate nowDate = LocalDate.now();
        // 포맷 정의
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 포맷 적용
        String formatedNowDate = nowDate.format(formatterDate);
        // 결과 출력
        System.out.println(formatedNowDate);
        // 현재 시간
        LocalTime nowTime = LocalTime.now();
        // 현재시간 출력
        System.out.println(nowTime);

        SimpleDateFormat datatime = new SimpleDateFormat("HH");

        String time = datatime.format(cal.getTime());

        int hh = Integer.parseInt(time);

        // 포맷변경 ( 년월일 시분초)
        SimpleDateFormat sdformat = new SimpleDateFormat("HH30");

        // 1시간 전
        cal.setTime(date);
        cal.add(Calendar.HOUR, -1);
        String shortPartTime = sdformat.format(cal.getTime());
        System.out.println("1시간 전 : " + shortPartTime);

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
        // 홈페이지에서 받은 키
        String serviceKey = "lsreK53XwFXG2rEI3GpisRYQCjg97dt7uTl0HEZnBtYQvqdxXub024qirOptZW3z%2FEJyGQIDVoSWWrzXnUMBxQ%3D%3D";
        String pageNo = "1";
        String numOfRows = "100000";
        String baseDate = formatedNowDate; // 조회하고싶은 날짜
        String baseTime = shortPartTime; // 조회하고싶은 시간
        String type = "JSON"; // 타입 xml, json 등등 ..
        String nx = geocodingVO.getXLat(); // 위도
        String ny = geocodingVO.getYLon();
        ; // 경도

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
                + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="
                + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="
                + URLEncoder.encode(baseTime, "UTF-8"));

        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));
        // urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" +
        // URLEncoder.encode("UTF-8")); //경도
        // urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" +
        // URLEncoder.encode(ny, "UTF-8")); //위도

        /*
         * GET방식으로 전송해서 파라미터 받아오기
         */
        URL url = new URL(urlBuilder.toString());
        // 어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        // System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        /*
         * BufferedReader rd;
         * if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
         * rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),
         * "UTF-8"));
         * } else {
         * rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(),
         * "UTF-8"));
         * }
         * StringBuilder sb = new StringBuilder();
         * String line;
         * while ((line = rd.readLine()) != null) {
         * sb.append(line);
         * }
         * rd.close();
         * conn.disconnect();
         * String result = sb.toString();
         * 
         * return result;
         */

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        WeatherVO weatherVO = new WeatherVO();

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(rd.readLine());
        JSONObject response = (JSONObject) object.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");

        // String status = (String) response.get("status");
        int countsky = 0;
        int countt1h = 0;
        int countpty = 0;
        for (int i = 0; i < item.size(); i++) {
            object = (JSONObject) item.get(i);
            String category = (String) object.get("category");

            // System.out.println(category);
            if (category.equals("SKY") && countsky < 1) {
                countsky += 1;
                String sky = (String) object.get("fcstValue");
                System.out.println("SKY::" + sky);
                weatherVO.setSky(sky);
            }
            if (category.equals("T1H") && countt1h < 1) {
                countt1h += 1;
                String t1h = (String) object.get("fcstValue");
                System.out.println("T1H::" + t1h);
                weatherVO.setT1h(t1h);
            }
            if (category.equals("PTY") && countpty < 1) {
                countpty += 1;
                String pty = (String) object.get("fcstValue");
                System.out.println("PTY::" + pty);
                weatherVO.setPty(pty);
            }
        }
        /*
         * String sky = weatherVO.getSky();
         * String pty = weatherVO.getPty();
         * 
         * if(pty=="1"||pty=="5")
         * {
         * 
         * }else if(pty=="2"||pty=="6"){
         * 
         * }else if(pty=="3"||pty=="7"){
         * 
         * }else{
         * 
         * if(sky=="1"){
         * if(6<hh&19>hh){
         * 
         * }else{
         * 
         * }
         * }
         * if(sky=="3"){
         * if(6<hh&19>hh){
         * 
         * }else{
         * 
         * }
         * 
         * }
         * 
         * }
         */

        /*
         * if(status.equals("NOT_FOUND"))
         * {
         * resultString="잘못 설정된 데이터값입니다. 관리자에게 문의해주시기 바랍니다";
         * 
         * }else if(status.equals("ERROR"))
         * {
         * resultString="서버 에러입니다. 관리자에게 문의해주시기 바랍니다";
         * }else{}
         */

        // System.out.println(item);

        return weatherVO;

    }

}
