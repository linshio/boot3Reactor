package cn.linshio;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

/**
 * @author linshio
 * @create 2024/6/6 14:19
 */

public class MyTest {

    @Test
    void testPassword(){
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"));
    }
}
