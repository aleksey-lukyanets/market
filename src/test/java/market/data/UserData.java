package market.data;

import market.dto.UserDTO;
import org.apache.commons.codec.binary.Base64;

/**
 * 
 */
public class UserData {

    public static final String USER_LOGIN = "ivan.petrov@yandex.ru";
    public static final String USER_PASSWORD = "petrov";
    public static final String USER_NAME = "Jean-Louis O'Brian";
    public static final String USER_PHONE = "+7 123 456 78 90";
    public static final String USER_ADDRESS = "ул. Итальянская, д. 7";
    public static final String USER_WRONG_PHONE = "123 456 78 90";
    public static final String CREDIT_CARD_NUMBER = "1111 2222 3333 4444";
    
    public static final String BASIC_AUTH_VALUE = "Basic "
            + new String(Base64.encodeBase64((USER_LOGIN + ":" + USER_PASSWORD).getBytes()));
    
    public static final String WRONG_BASIC_AUTH_VALUE = "Basic "
            + new String(Base64.encodeBase64((USER_LOGIN + "111").getBytes()));

    public static UserDTO getUserDTO() {
        return new UserDTO(USER_LOGIN, USER_PASSWORD, USER_NAME, USER_PHONE, USER_ADDRESS);
    }
}
