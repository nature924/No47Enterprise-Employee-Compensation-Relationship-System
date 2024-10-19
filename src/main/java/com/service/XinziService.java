package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.XinziEntity;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * 薪资 服务类
 */
public interface XinziService extends IService<XinziEntity> {

    /**
    * @param params 查询参数
    * @return 带分页的查询出来的数据
    */
     PageUtils queryPage(Map<String, Object> params);
}