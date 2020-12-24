package com.framework.jo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;


import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {
	
	public static String getHashKey(String jsonStr, String key, String charset) {
		String hash = "";
		try {
			Mac sha256HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
			sha256HMAC.init(secretKey);

			hash = Base64.encodeBase64String(sha256HMAC.doFinal(jsonStr.getBytes(charset)));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return hash;
	}
	

	//SimpleClientHttpRequestFactory - jdk에서 제공하는 HttpUrlConnection을이용한다.
	//HttpComponentsClientHttpRequestFactory - RESTTEMPLETE
	//POST https://my_url?array=your_value1&array=your_value2&name=bob 전송
	//POST 방식
	public static ResponseEntity<String> callApi(String uri, JsonObject body, HttpHeaders httpHeaders){
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom()
			        .loadTrustMaterial(null, acceptingTrustStrategy)
			        .build();
		} catch (KeyManagementException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		}
 
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
 
        CloseableHttpClient httpClient = HttpClients.custom()
                //.setSSLSocketFactory(csf)
                .build();
 
        requestFactory.setHttpClient(httpClient);

		HttpEntity<String> requestEntity = new HttpEntity<String>(body.toString(), httpHeaders);																																																																																																																																																															
		
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		ResponseEntity<String> result = null;
		try {
			//RequestHeader 직접 설정할 때 사용.
			result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);


		} catch (RestClientException e) {
			log.debug("RestClientException: {}", uri);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return result;
	}
	
	
	public static ResponseEntity<String> callExtApi(String uri, JsonObject body){
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom()
			        .loadTrustMaterial(null, acceptingTrustStrategy)
			        .build();
		} catch (KeyManagementException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		}
 
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
 
        CloseableHttpClient httpClient = HttpClients.custom()
                //.setSSLSocketFactory(csf)
                .build();
 
        requestFactory.setHttpClient(httpClient);

		HttpEntity<String> requestEntity = new HttpEntity<String>(body.toString(), new HttpHeaders());																																																																																																																																																															
		
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		ResponseEntity<String> result = null;
		try {
			//RequestHeader 직접 설정할 때 사용.
			result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);


		} catch (RestClientException e) {
			log.debug("RestClientException: {}", uri);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Spring4 까지 사용하던 RestTemplate는 HTTP(s)요청을 날리기 위해 JDK HttpURLConnection이나 Apache HttpComponents 등에 의존을 했다. 
	 * 그래서 일반적으로 insecure SSL과 관련된 처리를 할 때는 JDK에서 제공하는 기능에 기반한 HttpRequestFactory를 구현하여 이 안에서 hostname의 
	 * 검증 없이 모든 것을 신뢰하는 처리를 하고, X.509 공개키 암호화 검증을 모두 패스하게 하는 식으로 우회 로직을 구현해야한다.
	 * Spring4 에서 RestTemplate에 RequestFactory를 구현하여 insecure SSL을 구현하는 방법은 다음과 같다. 
	 * 
	 * 
	 * 
	 */
	//Get 방식
	public static ResponseEntity<String> callApiGet(URI uri, HttpHeaders httpHeaders){
		ResponseEntity<String> result = null;
		
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;	// https 인증서 유효성 검사를 비활성화
        SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom()
			        .loadTrustMaterial(null, acceptingTrustStrategy)
			        .build();
		} catch (KeyManagementException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		}
 
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
 
        CloseableHttpClient httpClient = HttpClients.custom()
                //.setSSLSocketFactory(csf)
                .build();
 
        requestFactory.setHttpClient(httpClient);
        
		HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);																																																																																																																																																															
		
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		try {

			result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

		} catch (RestClientException e) {
			log.debug("RestClientException: {}", uri);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return result;
	}
	
	
	public static String getRequestStr(HttpServletRequest httpServletRequest) throws IOException{
		StringBuffer sb = new StringBuffer();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = httpServletRequest.getReader();
			char[] charBuffer = new char[128];
			int bytesRead;
			while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
				sb.append(charBuffer, 0, bytesRead);
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		return sb.toString();
	}
	
	public static String convSTRtoNull(Object obj) {
	      if(obj==null) {
	         return "";
	      }else {
	         return (String)obj;
	      } 
	      
	   }

}
