package me.lovexl.toutiao.model;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {
        public Map<String,Object > objs  = new HashMap<String, Object>();
        public void set(String key,Object object){
            objs.put(key, object);
        }
        public Object get(String key){
            return objs.get(key);
        }
}
