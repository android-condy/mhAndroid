package com.idic.ordermoudle.util;

import com.idic.utilmoudle.MD5Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 文 件 名: AntUtil
 * 创 建 人: sineom
 * 创建日期: 2020/7/8 09:52
 * 修改时间：
 * 修改备注：
 */

public class AntUtil {
    public static String getSignToken(Map<String, Object> map) {
        String result = "";
        try {
            List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> item : infoIds) {
                if (item.getKey() == null) {
                    item.getKey();
                }
                String key = item.getKey();
                Object val = item.getValue();
                if (!val.equals("")) {
                    sb.append(key).append("=").append(val).append("&");
                }
            }
            sb.append("appSecret").append("=").append("4a5ad70aafef48d651868b520fe6fe22");
            result = sb.toString();
            //进行MD5加密
            result = MD5Utils.MD5Encode(result).toUpperCase();
        } catch (Exception e) {
            return null;
        }
        return result;
    }


}
