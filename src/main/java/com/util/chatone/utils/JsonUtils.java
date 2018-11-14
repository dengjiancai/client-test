package com.util.chatone.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * JSON相关操作
 *
 * @author Weisq
 * @version 1.0
 * @data 2016年11月4日 下午3:46:54
 */
public class JsonUtils {


    private static ValueFilter filter = (obj, s, v) -> {
        if (v == null)
            return "";
        return v;
    };

    public static String objectToJson(Object object, boolean... prettyFormat) {
        if (null == prettyFormat || prettyFormat.length == 0)
            return JSON.toJSONString(object, true);
        else
            return JSON.toJSONString(object, prettyFormat[0]);
    }

    public static String objectToJsonWriteNullStringAsEmpty(Object object, boolean... prettyFormat) {
        if (null == prettyFormat || prettyFormat.length == 0)
            return JSON.toJSONString(object, filter, SerializerFeature.PrettyFormat);
        else
            return JSON.toJSONString(object, filter);
    }


    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

}
