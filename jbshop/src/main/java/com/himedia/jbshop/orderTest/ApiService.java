package com.himedia.jbshop.orderTest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class ApiService {

	public Map<String, String> restApi(Map<String, String> map, String url) throws Exception {
		// Json String 데이터
		// 맵을 Json포맷으로 변경
		ObjectMapper mapper = new ObjectMapper();
		String param = mapper.writeValueAsString(map);

		// 여기는 고정값
		// 아래부터 OkHttp 사용
		// OkHttp:Rest API,Http 통신을 간편하게 사용할 수 있도록 만들어진 라이브러리
		OkHttpClient client = new OkHttpClient();
		// application/json 중요
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, param);
		Request request = new Request.Builder().url(url).post(body).addHeader("cache-control", "no-cache").build();
		// 결과값 받기
		Response response = client.newCall(request).execute();
		String result = response.body().string();
		// 여기까지 고정값

		ObjectMapper resultMapper = new ObjectMapper();
		Map<String, String> resultMap = resultMapper.readValue(result, Map.class);

		return resultMap;
	}

	// SHA-256 암호화
	public String encrypt(String text) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(text.getBytes());
		return bytesToHex(md.digest());
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
	// SHA-256 암호화
}
