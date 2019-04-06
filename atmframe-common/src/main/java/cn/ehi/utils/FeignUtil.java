package cn.ehi.utils;

import cn.ehi.api.BaseApi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Request.Options;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 33053
 * @create 2018/12/18
 * 〈feign工具类〉
 */
public class FeignUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FeignUtil.class);
    private static String baseUrl = "";

    // 保存生成的api
    private static Map<String, BaseApi> apiMap = new HashMap<>();

    public static <T extends BaseApi> T getHttpApi(Class<T> api, String url) {
        if (url == null || url.length() == 0) {
            LOG.error("请求失败，请检查配置文件，未找到请求链接");
            return null;
        }
        T base = (T) apiMap.get(api.getSimpleName());
        if (base == null) {
            base = create(api, url);
            apiMap.put(api.getSimpleName(), base);
        }
        return base;
    }

    public static <T extends BaseApi> T getHttpApi(Class<T> api) {
        return getHttpApi(api, baseUrl);
    }

    /**
     * 创建请求的client
     *
     * @param api api的类，必须继承BaseApi
     * @param url 请求的baseURl-统一
     * @return
     */
    private static <T extends BaseApi> T create(Class<T> api, String url) {

        return Feign.builder().client(new OkHttpClient())
                .encoder(new JacksonEncoder(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .configure(SerializationFeature.INDENT_OUTPUT, false)))
                .decoder(new JacksonDecoder())
                // 请求拦截器
                .requestInterceptor(new RequestInterceptor() {

                    @Override
                    public void apply(RequestTemplate template) {
                        LOG.info(template.bodyTemplate());
                        LOG.info(template.toString());
                        LOG.info(template.url());
                    }
                }).options(new Options(30_000, 30_000)).target(api, url);
    }
}
