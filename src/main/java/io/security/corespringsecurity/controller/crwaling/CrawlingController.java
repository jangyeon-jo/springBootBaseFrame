package io.security.corespringsecurity.controller.crwaling;

import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
@RestController
@AllArgsConstructor
@RequestMapping("/internal")
public class CrawlingController {

    @GetMapping("/crawling.do")
    public ModelAndView crawling(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	URI urlToUri = null;
    	
    	Map<String, Object> map = null;
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("depositcompany");
    	
    	String URL = "https://weather.naver.com/rgn/cityWetrMain.nhn";
    	org.jsoup.nodes.Document doc = Jsoup.connect(URL).get();
    	
    	//org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(doc);
    	
    	String day 		= "";
    	String date 	= "";
    	String timeSlot = "";
    	String weather 	= "";
    	String temperature = "";
    	
    	ArrayList<String> arrayDay 	= null;
    	ArrayList<String> arrayDate = null;
    	ArrayList<String> arrayTimeSlot = null;
    	ArrayList<String> arrayWeather 	= null;
    	ArrayList<String> arrayTemperature = null;
    	
    	Elements data = doc.select(".day_data");
    	
    	Elements cell_day = data.select(".cell_date .day");
    	Elements cell_date = data.select(".cell_date .date");
    	Elements cell_timeSlot = data.select(".cell_weather .weather_inner .inner_left .timeslot");
    	Elements cell_weather = data.select(".cell_weather .weather_inner .inner_left .rainfall");
    	Elements cell_temperature = data.select(".cell_temperature");
    	
    	String[] strDay = cell_day.text().split(" ");
    	String[] strDate = cell_date.text().split(" ");
    	String[] strTimeSlot = cell_timeSlot.text().split(" ");
    	String[] strWeather = cell_weather.text().split(" ");
    	String[] strTemperature = cell_temperature.text().split(" ");
    	
    	for (int i = 0; i < strDay.length; i++) {
    		day = strDate[i];
    		arrayDay.add(day);
		}
    	
    	for (int i = 0; i < strDate.length; i++) {
    		date = strDate[i];
    		arrayDate.add(date);
		}
    	
    	for (int i = 0; i < strTimeSlot.length; i++) {
    		timeSlot = strTemperature[i];
    		arrayTimeSlot.add(timeSlot);
		}
    	
    	for (int i = 0; i < strWeather.length; i++) {
    		weather = strWeather[i];
    		arrayWeather.add(weather);
		}
    	
    	for (int i = 0; i < strTemperature.length; i++) {
    		temperature = strTemperature[i];
    		arrayTemperature.add(temperature);
		}
    	
		mv.addObject("arrayDay", arrayDay);
		mv.addObject("arrayDate", arrayDate);
		mv.addObject("arrayTimeSlot", arrayTimeSlot);
		mv.addObject("arrayWeather", arrayWeather);
		mv.addObject("arrayTemperature", arrayTemperature);
    	
        return mv;
    }
    
    //구굴 맵 API
    @GetMapping("/callGoogleApi.do")
    public ModelAndView callGoogleApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Map<String, Object> map = null;
    	ArrayList<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("internal/googlemap");
        
        return mv;
    }
    
    //카카오 맵 API
    @GetMapping("/callKakaoApi.do")
    public ModelAndView callKakaoApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Map<String, Object> map = null;
    	ArrayList<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("depositcompany");
        
        return mv;
    }
}
