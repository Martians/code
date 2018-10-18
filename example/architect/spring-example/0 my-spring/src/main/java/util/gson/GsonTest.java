package util.gson;

import com.data.util.StringFormat;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * https://www.jianshu.com/p/e740196225a4
 * http://blog.csdn.net/axuanqq/article/details/51441590
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class User {

    /**
     * 从 json 转换为 class时，可以识别json中的多种别名
     */
    @SerializedName(value = "emailaddress", alternate = {"email", "email_address"})
    private String address;

    @SerializedName(value = "your_name")
    private String name;
    private Date date;

    @Expose(deserialize = true, serialize = true)
    private String status;

    /**
     * not show in json
     */
    transient private String not_to_json;
}

class SinceUntilSample {
    @Since(4)
    public String since;
    @Until(5)
    public String until;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ModifierSample {
    public String public_string;
    public Integer value;
    private String private_string;
    public static String static_string;
}

@Slf4j
public class GsonTest {

    static void attr_alias() {
        List<String> array = Arrays.asList("{\"name\":\"怪盗kidou\", \"email_address\":\"ikidou@example.com\"}",
                "{\"name\":\"怪盗kidou\", \"email\":\"ikidou@example.com\"}",
                "{\"name\":\"怪盗kidou\", \"emailaddress\":\"ikidou@example.com\"}");

        /**
         * 成功转换的到 User类，日志输出的是 User.toString
         *
         * 可以识别 "emailaddress"、"email"、"email_address
         */
        log.debug("user: {}", (new Gson()).fromJson(array.get(0), User.class));
        log.debug("user: {}", (new Gson()).fromJson(array.get(1), User.class));
        log.debug("user: {}", (new Gson()).fromJson(array.get(2), User.class));

        /**
         * 这里输出的是 User的Json格式，会 根据 SerializedName做调整
         **/
        log.debug("user json:{}", new GsonBuilder().serializeNulls().create().toJson(new User()));
    }

    static void collection() {
        String string = "[\"Android\",\"Java\",\"PHP\"]";

        /**
         * 因为 Java 是类型擦除的，所以必须指定 TypeToken 来对泛型提供支持
         *      TypeToken的构造方法是protected，这里相当于生成了一个子类型
         */
        List<String> list = new Gson().fromJson(string, new TypeToken<List<String>>() {}.getType());
        log.debug("list: {}", StringFormat.list(list));
    }

    static void outputFilter() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                /**
                 * 设置时间格式
                 */
                .setDateFormat("yyyy-MM-dd")
                /**
                 * 不对内部类，进行序列化
                 */
                .disableInnerClassSerialization()
//                .generateNonExecutableJson()
//                .disableHtmlEscaping()
                /**
                 * 设置格式化输出
                 */
                .setPrettyPrinting()
                .create();

        Date date = new Date(2017,06,06,06,06,06);
        User user = new User(null, null, date, null, null);
        log.debug("null user: {}", gson.toJson(user));
    }

    static void exposeFilter() {
        /**
         * 必须使用 GsonBuilder创建
         *
         * 只有加了 Expose 的才会输出
         */
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .disableInnerClassSerialization()
                .setPrettyPrinting()
                .create();

        User user = new User();
        log.debug("expose user: {}", gson.toJson(user));
    }

    static void versionFilter() {

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setVersion(3)
                .disableInnerClassSerialization()
                .setPrettyPrinting()
                .create();

        SinceUntilSample sample = new SinceUntilSample();
        log.debug("expose user: {}", gson.toJson(sample));
    }

    static void modifierFilter() {

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PRIVATE)
                .setPrettyPrinting()
                .create();

        ModifierSample sample = new ModifierSample();
        log.debug("modifier user: {}", gson.toJson(sample));
    }

    static void strategyFilter() {

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        //log.debug("check: {}", f.getName());
                        if ("status".equals(f.getName())) return true;

                        Expose expose = f.getAnnotation(Expose.class);
                        if (expose != null && expose.deserialize() == false) return true;
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                        //return (clazz == int.class || clazz == Integer.class);
                    }
                })
                .create();

        /**
         * 这里将不会显示 status 字段
         */
        User user = new User();
        log.debug("strategy user: {}", gson.toJson(user));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * not work ？？？？
     *
     * can't find annotation FooA
     */
    public @interface FooA {
    }

    /**
     * 是可以使用嵌套类的，除非是static
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class TagSample {
        @SerializedName(value = "your_name")
        public String a;

        @GsonTest.FooA
        public String b;
    }

    static void tagFilter() {

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                //.setExclusionStrategies(new ExclusionStrategy() {
                    public boolean shouldSkipField(FieldAttributes f) {
                        log.debug("check annotation: {}", f.getAnnotations());
                        return f.getAnnotation(FooA.class) != null;
                    }

                    public boolean shouldSkipClass(Class<?> clazz) {
                        return clazz.getAnnotation(FooA.class) != null;
                    }
                })
                .create();

        TagSample tag = new TagSample();
        log.debug("strategy user: {}", gson.toJson(tag));
    }
    /////////////////////////////////////////////////////////////////////////////////////////////

    static void strategyConverter() {

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setFieldNamingStrategy(new FieldNamingStrategy() {
                    /**
                     * 这里，只会检查没有设置 SerializedName 的字段
                     */
                    @Override
                    public String translateName(Field f) {

                        if (f.getName() == "status") {
                            log.debug("check: {}", f.getName());
                            return "new_status";
                        }
                        return f.getName();
                    }
                })
                .create();

        User user = new User();
        log.debug("strategy converter: {}", gson.toJson(user));
    }

    public static void main(String[] args) {
        if (true) {
            attr_alias();

            collection();

            outputFilter();

            exposeFilter();

            versionFilter();

            modifierFilter();

            strategyFilter();

            strategyConverter();
        }

        tagFilter();
    }
}
