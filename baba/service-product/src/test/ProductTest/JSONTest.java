package ProductTest;

import com.baba.pojo.Buyer;
import com.baba.pojo.SuperPojo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

/**
 * 测试json转对象，对象转json
 * @author Jamayette
 *         Created on  2017/8/21
 */
public class JSONTest {

    @Test
    public void testJson1() throws IOException {

//        Buyer buyer = new Buyer();
//        buyer.setCity(null);
//        buyer.setRealName("hehe");

        SuperPojo superPojo = new SuperPojo();
        superPojo.setProperty("haha", "wahaha");
        superPojo.setProperty("hehe", null);

        ObjectMapper om = new ObjectMapper();
        StringWriter w = new StringWriter();
        // 设置规则 排除null（注意对map无效）
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.writeValue(w, superPojo);
        System.out.println(w.toString());

        // json转对象
        SuperPojo readValue = om.readValue(w.toString(), SuperPojo.class);

        System.out.println(readValue);
    }

}
