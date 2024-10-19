package com.entity.vo;

import com.entity.XinziEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 薪资
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 */
@TableName("xinzi")
public class XinziVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */

    @TableField(value = "id")
    private Integer id;


    /**
     * 员工
     */

    @TableField(value = "yuangong_id")
    private Integer yuangongId;


    /**
     * 薪资编号
     */

    @TableField(value = "xinzi_uuid_number")
    private String xinziUuidNumber;


    /**
     * 标题
     */

    @TableField(value = "xinzi_name")
    private String xinziName;


    /**
     * 月份
     */

    @TableField(value = "xinzi_month")
    private String xinziMonth;


    /**
     * 基本工资
     */

    @TableField(value = "jiben_jine")
    private Double jibenJine;


    /**
     * 奖金
     */

    @TableField(value = "jiangjin_jine")
    private Double jiangjinJine;


    /**
     * 绩效
     */

    @TableField(value = "jixiao_jine")
    private Double jixiaoJine;


    /**
     * 补贴
     */

    @TableField(value = "butie_jine")
    private Double butieJine;


    /**
     * 应发
     */

    @TableField(value = "yingfa_jine")
    private Double yingfaJine;


    /**
     * 代扣失业金
     */

    @TableField(value = "daikou_shiyejin_jine")
    private Double daikouShiyejinJine;


    /**
     * 代扣养老保险金
     */

    @TableField(value = "daikou_yanglaojin_jine")
    private Double daikouYanglaojinJine;


    /**
     * 代扣公积金
     */

    @TableField(value = "daikou_gongjijin_jine")
    private Double daikouGongjijinJine;


    /**
     * 代扣个人所得税
     */

    @TableField(value = "daikou_gerensuodeshui_jine")
    private Double daikouGerensuodeshuiJine;


    /**
     * 实发工资
     */

    @TableField(value = "shifa_jine")
    private Double shifaJine;


    /**
     * 备注
     */

    @TableField(value = "xinzi_content")
    private String xinziContent;


    /**
     * 记录时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat

    @TableField(value = "insert_time")
    private Date insertTime;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat

    @TableField(value = "create_time")
    private Date createTime;


    /**
	 * 设置：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 获取：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 设置：员工
	 */
    public Integer getYuangongId() {
        return yuangongId;
    }


    /**
	 * 获取：员工
	 */

    public void setYuangongId(Integer yuangongId) {
        this.yuangongId = yuangongId;
    }
    /**
	 * 设置：薪资编号
	 */
    public String getXinziUuidNumber() {
        return xinziUuidNumber;
    }


    /**
	 * 获取：薪资编号
	 */

    public void setXinziUuidNumber(String xinziUuidNumber) {
        this.xinziUuidNumber = xinziUuidNumber;
    }
    /**
	 * 设置：标题
	 */
    public String getXinziName() {
        return xinziName;
    }


    /**
	 * 获取：标题
	 */

    public void setXinziName(String xinziName) {
        this.xinziName = xinziName;
    }
    /**
	 * 设置：月份
	 */
    public String getXinziMonth() {
        return xinziMonth;
    }


    /**
	 * 获取：月份
	 */

    public void setXinziMonth(String xinziMonth) {
        this.xinziMonth = xinziMonth;
    }
    /**
	 * 设置：基本工资
	 */
    public Double getJibenJine() {
        return jibenJine;
    }


    /**
	 * 获取：基本工资
	 */

    public void setJibenJine(Double jibenJine) {
        this.jibenJine = jibenJine;
    }
    /**
	 * 设置：奖金
	 */
    public Double getJiangjinJine() {
        return jiangjinJine;
    }


    /**
	 * 获取：奖金
	 */

    public void setJiangjinJine(Double jiangjinJine) {
        this.jiangjinJine = jiangjinJine;
    }
    /**
	 * 设置：绩效
	 */
    public Double getJixiaoJine() {
        return jixiaoJine;
    }


    /**
	 * 获取：绩效
	 */

    public void setJixiaoJine(Double jixiaoJine) {
        this.jixiaoJine = jixiaoJine;
    }
    /**
	 * 设置：补贴
	 */
    public Double getButieJine() {
        return butieJine;
    }


    /**
	 * 获取：补贴
	 */

    public void setButieJine(Double butieJine) {
        this.butieJine = butieJine;
    }
    /**
	 * 设置：应发
	 */
    public Double getYingfaJine() {
        return yingfaJine;
    }


    /**
	 * 获取：应发
	 */

    public void setYingfaJine(Double yingfaJine) {
        this.yingfaJine = yingfaJine;
    }
    /**
	 * 设置：代扣失业金
	 */
    public Double getDaikouShiyejinJine() {
        return daikouShiyejinJine;
    }


    /**
	 * 获取：代扣失业金
	 */

    public void setDaikouShiyejinJine(Double daikouShiyejinJine) {
        this.daikouShiyejinJine = daikouShiyejinJine;
    }
    /**
	 * 设置：代扣养老保险金
	 */
    public Double getDaikouYanglaojinJine() {
        return daikouYanglaojinJine;
    }


    /**
	 * 获取：代扣养老保险金
	 */

    public void setDaikouYanglaojinJine(Double daikouYanglaojinJine) {
        this.daikouYanglaojinJine = daikouYanglaojinJine;
    }
    /**
	 * 设置：代扣公积金
	 */
    public Double getDaikouGongjijinJine() {
        return daikouGongjijinJine;
    }


    /**
	 * 获取：代扣公积金
	 */

    public void setDaikouGongjijinJine(Double daikouGongjijinJine) {
        this.daikouGongjijinJine = daikouGongjijinJine;
    }
    /**
	 * 设置：代扣个人所得税
	 */
    public Double getDaikouGerensuodeshuiJine() {
        return daikouGerensuodeshuiJine;
    }


    /**
	 * 获取：代扣个人所得税
	 */

    public void setDaikouGerensuodeshuiJine(Double daikouGerensuodeshuiJine) {
        this.daikouGerensuodeshuiJine = daikouGerensuodeshuiJine;
    }
    /**
	 * 设置：实发工资
	 */
    public Double getShifaJine() {
        return shifaJine;
    }


    /**
	 * 获取：实发工资
	 */

    public void setShifaJine(Double shifaJine) {
        this.shifaJine = shifaJine;
    }
    /**
	 * 设置：备注
	 */
    public String getXinziContent() {
        return xinziContent;
    }


    /**
	 * 获取：备注
	 */

    public void setXinziContent(String xinziContent) {
        this.xinziContent = xinziContent;
    }
    /**
	 * 设置：记录时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }


    /**
	 * 获取：记录时间
	 */

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 设置：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 获取：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
