package io.security.corespringsecurity.service;

import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface GetItemDataService {
  
	//금융회사정보 
	public ResponseEntity<String> getCompanyDataService() throws UnsupportedEncodingException;
  
	//금융상푸정보
	public ResponseEntity<String> getItemDataService() throws UnsupportedEncodingException;
  
}