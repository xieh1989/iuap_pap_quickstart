package com.yonyou.iuap.example.asval.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.iuap.base.web.BaseController;
import com.yonyou.iuap.example.asval.entity.ExampleAsVal;
import com.yonyou.iuap.example.asval.service.ExampleAsValService;
import com.yonyou.iuap.example.asval.validator.ExampleAsValValidator;
import com.yonyou.iuap.mvc.type.SearchParams;
import com.yonyou.iuap.persistence.vo.pub.util.StringUtil;
import com.yonyou.uap.wb.entity.org.Organization;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/exampleAsVal")
public class ExampleAsValController extends BaseController {

    @Autowired
    private ExampleAsValService exampleAsValService;
    @Autowired
    private ExampleAsValValidator exampleAsValValidator;
    @RequestMapping(value={"/list"},method = {RequestMethod.GET})
    public Object list(PageRequest pageRequest, SearchParams searchParams) {

        Page<ExampleAsVal> tmpdata = exampleAsValService.selectAllByPage(pageRequest,searchParams);
        return buildSuccess(tmpdata);
    }
    @RequestMapping(value={"/save"},method = {RequestMethod.POST})
    public Object insert(@RequestBody  List<ExampleAsVal> exampleAsVals){
        exampleAsValValidator.validator(exampleAsVals);
        exampleAsValService.batchAddOrUpdate(exampleAsVals);
        return buildSuccess();
    }
    @RequestMapping(value={"/delete"},method = {RequestMethod.POST})
    public Object delete(@RequestBody List<ExampleAsVal> exampleAsVals){
        exampleAsValService.batchDelete(exampleAsVals);
        return buildSuccess();
    }
    /**
     * 根据ID查找对应的结果集
     * 在数据权限分配后数据回显时会调用到该方法
     */
    @RequestMapping(value={"/getByIds"},method={RequestMethod.POST})
    public JSONObject getIds(HttpServletRequest request){
        JSONObject result = new JSONObject();
        String data = request.getParameter("data");
        if(StringUtil.isEmpty(data)){
            return result;
        }
        JSONArray arr = JSON.parseArray(data);

        List<ExampleAsVal> asValList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(arr)){
            String[] strArray = arr.toArray(new String[arr.size()]);
            asValList = this.exampleAsValService.getByIds(strArray);
        }
        result.put("data",transformTOBriefEntity(asValList));
        return result;
    }

    private List<Organization> transformTOBriefEntity(List<ExampleAsVal> asValList){
        List<Organization> results = new ArrayList<>();
        if(!CollectionUtils.isEmpty(asValList)){
            for (ExampleAsVal entity:asValList){
            	Organization organization =new Organization();
            	organization.setCode(entity.getCode());
            	organization.setName(entity.getName());
            	organization.setId(entity.getId());
                results.add(organization);
            }
        }

        return results;
    }
}
