package io.security.corespringsecurity.controller.api;

import com.framework.jo.utils.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.security.corespringsecurity.service.GetDataService;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
@Controller
@AllArgsConstructor
@RequestMapping("/mypage")
public class GetApiController {
	
	// 한 화면에 보여줄 리스트 갯수, 페이징 범위의 갯수
	public static final int pagePerList = 10;
	public static final int pagingCount = 10;
	
	@Autowired
	private GetDataService getDataService;
	
	
	 /**
     * 
     * 요청주소 http://apis.data.go.kr/B190017/service/GetInsuredProductService202008/getCompanyList202008
     * 서비스URL http://apis.data.go.kr/B190017/service/GetInsuredProductService202008
     * 금융권역, 금융회사명 등 예금자보호 금융상품을 취급하는 금융권역별 보호대상 금융회사 목록 조회 기능
     * Request
     *  서비스키	ServiceKey	400	필	-	공공데이터포털에서 받은 인증
     *  페이지 번호	pageNo	4	옵	1	페이지번호              
     *  한 페이지 결과 수	numOfRows	4	옵	10	한 페이지 결과 수 
     *  결과형식	resultType	4	옵	xml	결과형식(xml/json) 
     *  금융회사명	fncIstNm	100	옵	SK증권주식회사	금융회사명  
     *  금융권역	regnNm	100	옵	금융투자	금융권역           
     * 
     * Response
     *  결과코드	resultCode	2	필	00	결과코드                      
     *  결과메시지	resultMsg	50	필	OK	결과메시지                     
     *  한 페이지 결과 수	numOfRows	4	필	10	한 페이지 결과 수            
     *  페이지 번호	pageNo	4	필	1	3                             
     *  전체 결과 수	totalCount	4	필	3	전체 결과 수                   
     *  번호	num	4	필	1	일련번호                                  
     *  금융회사명	fncIstNm	200	필	SK증권주식회사	금융기관 이름           
     *  금융상품명	prdNm	200	필	비과세장기회사채형	금융상품 이름               
     *  상품판매중단일자	prdSalDscnDt	8	필	20091231	상품판매중단일자  
     *  등록일	regDate	8	필	20200225	등록일                 
     *  
     *  "numOfRows":10
     *  "pageNo":1    
     *  "totalCount":320  
     *  
     * @throws IOException 
     */
    @GetMapping("/getCompanyInfo.do")
    public ModelAndView getCompanyInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	URI urlToUri = null;
    	
    	Map<String, Object> map = null;
    	ArrayList<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("api/depositcompany");
    	
    	ResponseEntity<String> resultEntity = getDataService.getCompanyDataService();
		
		String sResult = resultEntity.getBody();
		
		Gson gson = new Gson();
		JsonObject jsonobject = new JsonObject();
		JsonObject jsondataHeader = new JsonObject();
		JsonArray jsondataItem = new JsonArray();
		JsonObject jsondataBody = new JsonObject();
		
		jsonobject  = gson.fromJson(sResult, JsonObject.class);
		
		jsondataHeader = (JsonObject) jsonobject.get("getCompanyList");
		jsondataBody = (JsonObject) jsondataHeader.get("header");
		jsondataItem = (JsonArray) jsondataHeader.get("item");
		
		//String rows 	=jsondataHeader.get("rows").getAsString();				//
		String numOfRows= jsondataHeader.get("numOfRows").getAsString();		//현재개수
		String pageNo	= jsondataHeader.get("pageNo").getAsString();			//페이지번호
		String totalCount= jsondataHeader.get("totalCount").getAsString();		//전체수
		
		for (int i = 0; i < jsondataItem.size(); i++) {
			map = new HashMap<String, Object>();
			jsondataBody = jsondataItem.get(i).getAsJsonObject();
			map.put("SEQ", jsondataBody.get("num").getAsString());
			map.put("COMPNM", jsondataBody.get("fncIstNm").getAsString());
			
			array.add(map);
		}
		
        mv.addObject("bankList", array);
        
        //페이지 처리
        mv.addObject("numOfRows", numOfRows);
        mv.addObject("pageNo", pageNo);
        mv.addObject("totalCount", totalCount);
        
        return mv;
    }
	
    /**
     * 
     * 요청주소 http://apis.data.go.kr/B190017/service/GetInsuredProductService202008/getProductList202008
     * 서비스URL http://apis.data.go.kr/B190017/service/GetInsuredProductService202008
     * 금융권역, 금융회사명, 금융상품명, 상품판매중단일 등 금융회사별 보호대상 금융상품을 조회하는 기능
     * 
     * Request
     *  서비스키	ServiceKey	400	필	-	공공데이터포털에서 받은 인증
     *  페이지 번호	pageNo	4	옵	1	페이지번호              
     *  한 페이지 결과 수	numOfRows	4	옵	10	한 페이지 결과 수 
     *  결과형식	resultType	4	옵	xml	결과형식(xml/json) 
     *  금융권역	regnNm	100	옵	금융투자	금융권역           
     *  금융회사명	fncIstNm	100	옵	SK증권주식회사	금융회사명  
     *  금융상품명	prdNm	1000	옵	비과세장기회사채형	금융상품명  
     * 
     * Response
     *  결과코드	resultCode	2	필	00	결과코드                      
     *  결과메시지	resultMsg	50	필	OK	결과메시지                     
     *  한 페이지 결과 수	numOfRows	4	필	10	한 페이지 결과 수            
     *  페이지 번호	pageNo	4	필	1	3                             
     *  전체 결과 수	totalCount	4	필	3	전체 결과 수                   
     * @throws IOException 
     *  번호	num	4	필	1	일련번호                                  
     *  금융회사명	fncIstNm	200	필	SK증권주식회사	금융기관 이름           
     *  금융상품명	prdNm	200	필	비과세장기회사채형	금융상품 이름               
     *  상품판매중단일자	prdSalDscnDt	8	필	20091231	상품판매중단일자  
     *  등록일	regDate	8	필	20200225	등록일                       
     * 
     * prdSalDscnDt
     * num
     * fncIstNm
     * regDate
     * prdNm
     * 
     * 
     */
    @PostMapping("/getDepositItem.do")
    public ModelAndView getDepositItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String bankNm = request.getParameter("FNIST_NM");
    	
    	Map<String, Object> map = null;
    	ArrayList<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
    	
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("api/deposititem");
    	
    	ResponseEntity<String> resultEntity = getDataService.getItemDataService(bankNm);
    	
    	String sResult = resultEntity.getBody();
    	
    	Gson gson = new Gson();
		JsonObject jsonobject = new JsonObject();
		JsonObject jsondataHeader = new JsonObject();
		JsonArray jsondataItem = new JsonArray();
		JsonObject jsondataBody = new JsonObject();
		
		jsonobject  = gson.fromJson(sResult, JsonObject.class);
		
		jsondataHeader = (JsonObject) jsonobject.get("getProductList");
		jsondataBody = (JsonObject) jsondataHeader.get("header");
		jsondataItem = (JsonArray) jsondataHeader.get("item");
		
		//String rows 	=jsondataHeader.get("rows").getAsString();				//
		String numOfRows= jsondataHeader.get("numOfRows").getAsString();		//현재개수
		String pageNo	= jsondataHeader.get("pageNo").getAsString();			//페이지번호
		String totalCount= jsondataHeader.get("totalCount").getAsString();		//전체수
		
		for (int i = 0; i < jsondataItem.size(); i++) {
			map = new HashMap<String, Object>();
			jsondataBody = jsondataItem.get(i).getAsJsonObject();
			map.put("PRD_HALT_DATE", jsondataBody.get("prdSalDscnDt").getAsString());
			map.put("SEQ", jsondataBody.get("num").getAsString());
			map.put("FNIST_NM", jsondataBody.get("fncIstNm").getAsString());
			map.put("REG_DATE", jsondataBody.get("regDate").getAsString());
			map.put("PRD_NM", jsondataBody.get("prdNm").getAsString());
			
			array.add(map);
		}
		
        mv.addObject("ItemList", array);
        
        //페이지 처리
        mv.addObject("numOfRows", numOfRows);
        mv.addObject("pageNo", pageNo);
        mv.addObject("totalCount", totalCount);
    	
    	
        return mv;
    }
    
    
}
