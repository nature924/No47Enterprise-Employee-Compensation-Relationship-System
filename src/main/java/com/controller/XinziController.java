
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 薪资
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/xinzi")
public class XinziController {
    private static final Logger logger = LoggerFactory.getLogger(XinziController.class);

    @Autowired
    private XinziService xinziService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service
    @Autowired
    private YuangongService yuangongService;



    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("员工".equals(role))
            params.put("yuangongId",request.getSession().getAttribute("userId"));
        if(params.get("orderBy")==null || params.get("orderBy")==""){
            params.put("orderBy","id");
        }
        PageUtils page = xinziService.queryPage(params);

        //字典表数据转换
        List<XinziView> list =(List<XinziView>)page.getList();
        for(XinziView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        XinziEntity xinzi = xinziService.selectById(id);
        if(xinzi !=null){
            //entity转view
            XinziView view = new XinziView();
            BeanUtils.copyProperties( xinzi , view );//把实体数据重构到view中

                //级联表
                YuangongEntity yuangong = yuangongService.selectById(xinzi.getYuangongId());
                if(yuangong != null){
                    BeanUtils.copyProperties( yuangong , view ,new String[]{ "id", "createTime", "insertTime", "updateTime"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setYuangongId(yuangong.getId());
                }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody XinziEntity xinzi, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,xinzi:{}",this.getClass().getName(),xinzi.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");
        else if("员工".equals(role))
            xinzi.setYuangongId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        Wrapper<XinziEntity> queryWrapper = new EntityWrapper<XinziEntity>()
            .eq("yuangong_id", xinzi.getYuangongId())
            .eq("xinzi_month", xinzi.getXinziMonth())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        XinziEntity xinziEntity = xinziService.selectOne(queryWrapper);
        if(xinziEntity==null){

            //应发
            xinzi.setYingfaJine(xinzi.getButieJine()+xinzi.getJiangjinJine()+xinzi.getJixiaoJine()+xinzi.getJibenJine());
            daikouJine(xinzi);//代扣金额相关
            //实发
            xinzi.setShifaJine(xinzi.getYingfaJine()-xinzi.getDaikouGerensuodeshuiJine()-xinzi.getDaikouGongjijinJine()-xinzi.getDaikouShiyejinJine()-xinzi.getDaikouYanglaojinJine());


            xinzi.setInsertTime(new Date());
            xinzi.setCreateTime(new Date());
            xinziService.insert(xinzi);
            return R.ok();
        }else {
            return R.error(511,"该员工该月份已有薪资记录");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody XinziEntity xinzi, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,xinzi:{}",this.getClass().getName(),xinzi.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
//        else if("员工".equals(role))
//            xinzi.setYuangongId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        //根据字段查询是否有相同数据
        Wrapper<XinziEntity> queryWrapper = new EntityWrapper<XinziEntity>()
            .notIn("id",xinzi.getId())
            .andNew()
            .eq("yuangong_id", xinzi.getYuangongId())
            .eq("xinzi_month", xinzi.getXinziMonth())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        XinziEntity xinziEntity = xinziService.selectOne(queryWrapper);
        if(xinziEntity==null){

            //应发
            xinzi.setYingfaJine(xinzi.getButieJine()+xinzi.getJiangjinJine()+xinzi.getJixiaoJine()+xinzi.getJibenJine());
            daikouJine(xinzi);//代扣金额相关
            //实发
            xinzi.setShifaJine(xinzi.getYingfaJine()-xinzi.getDaikouGerensuodeshuiJine()-xinzi.getDaikouGongjijinJine()-xinzi.getDaikouShiyejinJine()-xinzi.getDaikouYanglaojinJine());

            xinziService.updateById(xinzi);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"该员工该月份已有薪资记录");
        }
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        xinziService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<XinziEntity> xinziList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示

                        List<YuangongEntity> yuangongEntities = yuangongService.selectList(new EntityWrapper<>());
                        if(yuangongEntities.size()==0)
                            return R.error("没有员工,没法添加薪资");

                        Map<String, Integer> map = new LinkedHashMap<>();
                        for(YuangongEntity y:yuangongEntities){
                            map.put(y.getYuangongUuidNumber(),y.getId());
                        }


                        for(List<String> data:dataList){
                            //循环
                            XinziEntity xinziEntity = new XinziEntity();
                            xinziEntity.setYuangongId(map.get(data.get(0)));   //通过员工编号获取员工id
                            xinziEntity.setXinziUuidNumber(String.valueOf(new Date().getTime())+String.valueOf(new Random().nextInt(100)));                    //薪资编号 要改的
                            xinziEntity.setXinziName(data.get(1));                    //标题 要改的
                            xinziEntity.setXinziMonth(data.get(2));                    //月份 要改的
                            xinziEntity.setJibenJine(Double.valueOf(data.get(3)));                    //基本工资 要改的
                            xinziEntity.setJiangjinJine(Double.valueOf(data.get(4)));                    //奖金 要改的
                            xinziEntity.setJixiaoJine(Double.valueOf(data.get(5)));                    //绩效 要改的
                            xinziEntity.setButieJine(Double.valueOf(data.get(6)));                    //补贴 要改的
//                            xinziEntity.setYingfaJine(data.get(0));                    //应发 要改的
//                            xinziEntity.setDaikouShiyejinJine(data.get(0));                    //代扣失业金 要改的
//                            xinziEntity.setDaikouYanglaojinJine(data.get(0));                    //代扣养老保险金 要改的
//                            xinziEntity.setDaikouGongjijinJine(data.get(0));                    //代扣公积金 要改的
//                            xinziEntity.setDaikouGerensuodeshuiJine(data.get(0));                    //代扣个人所得税 要改的
//                            xinziEntity.setShifaJine(data.get(0));                    //实发工资 要改的
                            xinziEntity.setXinziContent("批量添加工号为"+data.get(0)+"的"+xinziEntity.getXinziMonth()+"的薪资记录");//详情和图片
                            xinziEntity.setInsertTime(date);//时间
                            xinziEntity.setCreateTime(date);//时间


                            //应发
                            xinziEntity.setYingfaJine(xinziEntity.getButieJine()+xinziEntity.getJiangjinJine()+xinziEntity.getJixiaoJine()+xinziEntity.getJibenJine());
                            daikouJine(xinziEntity);//代扣金额相关
                            //实发
                            xinziEntity.setShifaJine(xinziEntity.getYingfaJine()-xinziEntity.getDaikouGerensuodeshuiJine()-xinziEntity.getDaikouGongjijinJine()-xinziEntity.getDaikouShiyejinJine()-xinziEntity.getDaikouYanglaojinJine());


                            XinziEntity xinziEntity1 = xinziService.selectOne(new EntityWrapper<XinziEntity>()
                                    .eq("yuangong_id", xinziEntity.getYuangongId())
                                    .eq("xinzi_month", xinziEntity.getXinziMonth())
                            );
                            if(xinziEntity1 != null)
                                return R.error("工号为["+data.get(0)+"]的员工在["+xinziEntity.getXinziMonth()+"]月份已有薪资记录");



                            xinziList.add(xinziEntity);
                        }

                        xinziService.insertBatch(xinziList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }


    /**
     * 薪资代扣逻辑
     * @param xinzi
     */

    public void daikouJine(XinziEntity xinzi){
        if(xinzi.getYingfaJine()>100000){//大于10万
            xinzi.setDaikouGerensuodeshuiJine(1000.0);
            xinzi.setDaikouGongjijinJine(1001.0);
            xinzi.setDaikouShiyejinJine(1002.0);
            xinzi.setDaikouYanglaojinJine(1003.0);
        }else if(xinzi.getYingfaJine()>90000){//大于9万
            xinzi.setDaikouGerensuodeshuiJine(900.0);
            xinzi.setDaikouGongjijinJine(901.0);
            xinzi.setDaikouShiyejinJine(902.0);
            xinzi.setDaikouYanglaojinJine(903.0);
        }else if(xinzi.getYingfaJine()>80000){//大于8万
            xinzi.setDaikouGerensuodeshuiJine(800.0);
            xinzi.setDaikouGongjijinJine(801.0);
            xinzi.setDaikouShiyejinJine(802.0);
            xinzi.setDaikouYanglaojinJine(803.0);
        }else if(xinzi.getYingfaJine()>70000){//大于7万
            xinzi.setDaikouGerensuodeshuiJine(700.0);
            xinzi.setDaikouGongjijinJine(701.0);
            xinzi.setDaikouShiyejinJine(702.0);
            xinzi.setDaikouYanglaojinJine(703.0);
        }else if(xinzi.getYingfaJine()>60000){//大于6万
            xinzi.setDaikouGerensuodeshuiJine(600.0);
            xinzi.setDaikouGongjijinJine(601.0);
            xinzi.setDaikouShiyejinJine(602.0);
            xinzi.setDaikouYanglaojinJine(603.0);
        }else if(xinzi.getYingfaJine()>50000){//大于5万
            xinzi.setDaikouGerensuodeshuiJine(500.0);
            xinzi.setDaikouGongjijinJine(501.0);
            xinzi.setDaikouShiyejinJine(502.0);
            xinzi.setDaikouYanglaojinJine(503.0);
        }else if(xinzi.getYingfaJine()>40000){//大于4万
            xinzi.setDaikouGerensuodeshuiJine(400.0);
            xinzi.setDaikouGongjijinJine(401.0);
            xinzi.setDaikouShiyejinJine(402.0);
            xinzi.setDaikouYanglaojinJine(403.0);
        }else if(xinzi.getYingfaJine()>30000){//大于3万
            xinzi.setDaikouGerensuodeshuiJine(300.0);
            xinzi.setDaikouGongjijinJine(301.0);
            xinzi.setDaikouShiyejinJine(302.0);
            xinzi.setDaikouYanglaojinJine(303.0);
        }else if(xinzi.getYingfaJine()>20000){//大于2万
            xinzi.setDaikouGerensuodeshuiJine(200.0);
            xinzi.setDaikouGongjijinJine(201.0);
            xinzi.setDaikouShiyejinJine(202.0);
            xinzi.setDaikouYanglaojinJine(203.0);
        }else if(xinzi.getYingfaJine()>10000){//大于1万
            xinzi.setDaikouGerensuodeshuiJine(100.0);
            xinzi.setDaikouGongjijinJine(101.0);
            xinzi.setDaikouShiyejinJine(102.0);
            xinzi.setDaikouYanglaojinJine(103.0);
        }else if(xinzi.getYingfaJine()>50000){//大于1万
            xinzi.setDaikouGerensuodeshuiJine(50.0);
            xinzi.setDaikouGongjijinJine(51.0);
            xinzi.setDaikouShiyejinJine(52.0);
            xinzi.setDaikouYanglaojinJine(53.0);
        }else if(xinzi.getYingfaJine()>50000){//大于1万
            xinzi.setDaikouGerensuodeshuiJine(10.0);
            xinzi.setDaikouGongjijinJine(11.0);
            xinzi.setDaikouShiyejinJine(12.0);
            xinzi.setDaikouYanglaojinJine(13.0);
        }
    }






}
