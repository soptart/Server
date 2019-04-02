package org.sopt.artoo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.sopt.artoo.utils.KakaoAccessToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class KakaoService { //카카오톡 정보 반환
    public JsonNode verifyAccessToken(String accessToken){
        final String RequestUrl = "https://kapi.kakao.com/v2/user/me";

        final List<NameValuePair> postParams = new ArrayList<>();

        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", "9477313f9acbc65bfe8d30cf67cc3802"));    // REST API KEY
        postParams.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/MS/kakaologin"));    // 리다이렉트 URI
        postParams.add(new BasicNameValuePair("code", accessToken));    // 로그인 과정중 얻은 code 값

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);
        // add header
        post.addHeader("Authorization", "Bearer " + accessToken);
        JsonNode returnNode = null;
        try {

            post.setEntity(new UrlEncodedFormEntity(postParams));
            final HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();
            System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
            System.out.println("Response Code : " + responseCode);
            //JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // clear resources
        }
        return returnNode;
    }
}
