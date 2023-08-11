package com.owori.domain.member.jwt;

import com.owori.domain.member.dto.client.ApplePublicKeyDetails;
import com.owori.domain.member.dto.client.ApplePublicKeyResponse;
import com.owori.domain.member.dto.collection.AppleMap;
import com.owori.domain.member.exception.JwtProcessingException;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;

@Component
public class AppleKeyGenerator {
    private static final String ALG_HEADER_KEY = "alg";
    private static final String KID_HEADER_KEY = "kid";
    private static final int POSITIVE_SIGNUM = 1;

    public PublicKey generatePublicKey(AppleMap appleMap, ApplePublicKeyResponse applePublicKeys) {
        List<ApplePublicKeyDetails> publicKeys = applePublicKeys.getKeys();
        ApplePublicKeyDetails publicKey = publicKeys.stream()
                .filter(key -> key.getAlg().equals(appleMap.get(ALG_HEADER_KEY)))
                .filter(key -> key.getKid().equals(appleMap.get(KID_HEADER_KEY)))
                .findAny()
                .orElseThrow(JwtProcessingException::new);

        return generatePublicKeyWithApplePublicKey(publicKey);
    }

    private PublicKey generatePublicKeyWithApplePublicKey(ApplePublicKeyDetails applePublicKey) {
        byte[] n = Base64Utils.decodeFromUrlSafeString(applePublicKey.getN());
        byte[] e = Base64Utils.decodeFromUrlSafeString(applePublicKey.getE());
        RSAPublicKeySpec publicKeySpec =
                new RSAPublicKeySpec(new BigInteger(POSITIVE_SIGNUM, n), new BigInteger(POSITIVE_SIGNUM, e));

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new JwtProcessingException();
        }
    }
}
