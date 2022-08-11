package hello.itemservice.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class MessageSourceTest {
    @Autowired
    MessageSource ms;

    @Test
    void helloMessage() {
        String result = ms.getMessage("hello", null, null);
        //locale 정보가 없으면 basename에서 설정한 기본 이름 메시지 파일을 조회
        //basename 으로 messages를 지정 했으므로 messages.properties 파일에서 데이터 조회
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode() {
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
        //메시지가 없는 경우에는 NoSuchMessageException이 발생
    }
    @Test
    void notFoundMessageCodeDefaultMessage() {
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
        //메시지가 없어도 기본 메시지(defaultMessage)를 사용하면 기본 메시지가 반환
    }

    @Test
    void argumentMessage() {
        String result = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(result).isEqualTo("안녕 Spring");
        //hello.name=안녕 {0} -> {0} 부분은 매개변수(args: Spring) 전달 ->안녕 Spring
    }


    /** locale 정보를 기반으로 "국제화 파일을 선택"
     * Locale 에 맞추어 구체적인 것이 있으면 구체적인 것을 찾고, 없으면 디폴트를 찾는다
     * */
    @Test
    void defaultLang() {
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        //locale 정보가 없으므로 messages 를 사용

        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
        //locale 정보가 있지만, message_ko 가 없으므로 messages 를 사용
    }

    @Test
    void enLang() {

        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
        // locale 정보가 Locale.ENGLISH 이므로 messages_en 을 찾아서 사용
    }
}
