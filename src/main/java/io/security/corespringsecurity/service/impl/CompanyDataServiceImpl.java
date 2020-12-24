package io.security.corespringsecurity.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.security.corespringsecurity.service.GetDataService;
import io.security.corespringsecurity.utils.CommonUtil;

@Service
public class CompanyDataServiceImpl implements GetDataService {
	
	@Override
	public ResponseEntity<String> getCompanyDataService() throws UnsupportedEncodingException {
		URI urlToUri = null;
		
    	//금융회사별 보호대상 금융상품 조회
    	String uri = "http://apis.data.go.kr/B190017/service/GetInsuredProductService202008/getCompanyList202008";
    	String serviceKey= "UO7SF8R9emt2wBa%2BEiS9QAOKrP6fMdfkjojVTq%2BxCozVgQ4nsdxHxs45zsAHNKl%2F%2Fkt%2BX9EoTnYBphZlvOPtwQ%3D%3D";
    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));
        
    	UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam(URLEncoder.encode("ServiceKey","UTF-8"), serviceKey)
                .queryParam(URLEncoder.encode("pageNo","UTF-8"), URLEncoder.encode("1", "UTF-8"))
                .queryParam(URLEncoder.encode("numOfRows","UTF-8"), URLEncoder.encode("10", "UTF-8"))
                .queryParam(URLEncoder.encode("resultType","UTF-8"), URLEncoder.encode("json", "UTF-8"))
                .queryParam(URLEncoder.encode("fncIstNm","UTF-8"), URLEncoder.encode("", "UTF-8"))
                .queryParam(URLEncoder.encode("regnNm","UTF-8"), URLEncoder.encode("", "UTF-8"))
                .build(false);
    	
		try {
			urlToUri = new URI(builder.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		ResponseEntity<String> resultEntity= CommonUtil.callApiGet(urlToUri, headers);
		
		
		return resultEntity;
	}
	
	@Override
	public ResponseEntity<String> getItemDataService(String bankNm) throws UnsupportedEncodingException {
		URI urlToUri = null;
		
		String uri = "http://apis.data.go.kr/B190017/service/GetInsuredProductService202008/getProductList202008";
    	String serviceKey= "UO7SF8R9emt2wBa%2BEiS9QAOKrP6fMdfkjojVTq%2BxCozVgQ4nsdxHxs45zsAHNKl%2F%2Fkt%2BX9EoTnYBphZlvOPtwQ%3D%3D";
    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));
        
    	UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam(URLEncoder.encode("ServiceKey","UTF-8"), serviceKey)
                .queryParam(URLEncoder.encode("pageNo","UTF-8"), URLEncoder.encode("1", "UTF-8"))
                .queryParam(URLEncoder.encode("numOfRows","UTF-8"), URLEncoder.encode("10", "UTF-8"))
                .queryParam(URLEncoder.encode("resultType","UTF-8"), URLEncoder.encode("json", "UTF-8"))
                .queryParam(URLEncoder.encode("regnNm","UTF-8"), "")
                .queryParam(URLEncoder.encode("fncIstNm","UTF-8"), URLEncoder.encode(bankNm, "UTF-8"))
                .queryParam(URLEncoder.encode("prdNm","UTF-8"), "")
                .build(false);
    	
		try {
			urlToUri = new URI(builder.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResponseEntity<String> resultEntity= CommonUtil.callApiGet(urlToUri, headers);
		
        return resultEntity;
	}
	
}
