package com.tafh.myfin_app.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class HashHelper {

    public static String sha256(String data) {
        return DigestUtils.sha256Hex(data);
    }

}
