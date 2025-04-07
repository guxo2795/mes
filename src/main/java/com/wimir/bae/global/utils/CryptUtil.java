package com.wimir.bae.global.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CryptUtil {

    // 알고리즘 형식(AES, CBC..)
    @Value("${bae.security.alg}")
    private String alg;

    // 암호화/복호화에 사용하는 바이트 대칭 키
    @Value("${bae.security.key}")
    private String key;

    // CBC모드에서 초기화 벡터
    @Value("${bae.security.iv}")
    private String iv;

    // AES 암호화
    public String encrypt(String plain) {
        try {
            // 암호화에 사용할 알고리즘 지정- "AES/CBC/PKCS5Padding";
            // AES: 알고리즘(대칭키)
            // CBC: 블록 암호화 모드
            // PKCS5Padding: 패딩 방식(블록 크기 맞춤용)
            Cipher cipher = Cipher.getInstance(alg);

            // 바이트 배열로 된 key를 사용하여 AES용 대칭키 객체 생성
            // SecretKeySpec은 알고리즘 이름을 받드시 명시해야함. "AES"처럼 하드코딩
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");

            // CBC모드에서는 초기화벡터(IV)가 필요함.
            // 초기화벡터는 암호화의 출발점을 랜덤하게 만드는 값
            // 같은 평문과 키라도 IV가 다르면 암호문이 완전히 달라짐.
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

            // ENCRYPT_MODE로 Cipher 객체 초기화
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            
            // 평문 문자열을 UTF-8로 인코딩 후 AES암호화 수행, 결과는 바이트 배열로 반환
            // 암호화는 수학 연산이라서 결과가 byte[]
            // 암호문은 텍스트가 아니라, 아무 의미 없는 2진수 데이터
            byte[] encrypted = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));

            // 암호문을 바로 문자열로 변환하면 깨질 가능성이 높음 => Base64로 인코딩
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // AES 복호화
    public String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
