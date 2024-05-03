package in.snm.restservice.apikey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class ApiKeyService {
    @Value("${application.security.secret-key}")
    private String secretKey;
    @Value("${application.security.api-key}")
    private String apiKey;

    public boolean isApiKeyValid(String incomingApiKey) {
        boolean result = false;
        try {
            result = decrypt(incomingApiKey).equals(decrypt(apiKey));
        } catch (Exception ignored) {}
    return result;
    }

    private String decrypt(String strToDecrypt) throws Exception {
        byte[] keyBytes = generateKey(secretKey);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
        return new String(decryptedBytes);
    }

    private byte[] generateKey(String key)  throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        return sha.digest(hexStringToByteArray(key));
    }

    private byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
