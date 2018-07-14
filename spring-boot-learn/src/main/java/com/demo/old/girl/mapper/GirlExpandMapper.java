package com.demo.old.girl.mapper;

import java.util.List;
import java.util.Map;

public interface GirlExpandMapper {
    List<Map<String, Object>> selectBoth(float val);
}