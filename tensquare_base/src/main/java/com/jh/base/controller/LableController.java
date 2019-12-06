package com.jh.base.controller;

import com.jh.base.pojo.Label;
import com.jh.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin   //跨域
@RequestMapping("/lable")
@RefreshScope   //在springcloud bus中自定义注解修改也起作用（默认修改spring默认注解才有效）
public class LableController {
    @Autowired
    private LabelService labelService;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
         labelService.findAll();
         return new Result(true, StatusCode.OK.getCode(),"查询成功");
    }

    @RequestMapping(value = "/{lableId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String lableId){
        labelService.findById(lableId);
        return new Result(true, StatusCode.OK.getCode(),"查询成功");
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result add( @RequestBody Label label){
        labelService.add(label);
        return new Result(true,StatusCode.OK.getCode(),"增加成功");
    }

    /*** 修改标签 * @param label * @return */
    @RequestMapping(value="/{id}" ,method = RequestMethod.PUT)
    public Result update( @RequestBody Label label,@PathVariable String id){
        label.setId(id); labelService.update(label);
        return new Result(true,StatusCode.OK.getCode(),"修改成功");
    }
    /*** 删除标签 * @param id * @return */
    @RequestMapping(value="/{id}" ,method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        labelService.deleteById(id);
        return new Result(true,StatusCode.OK.getCode(),"删除成功");
    }

    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Label label){
        List<Label> list = labelService.findSearch(label);
        return new Result(true,StatusCode.OK.getCode(),"查询成功");
    }
    @RequestMapping(value = "/seatch/{page}/{size}",method = RequestMethod.POST)
    public Result pageQuery(@RequestBody Label label,@PathVariable int page,@PathVariable int size){
        Page<Label> pageDatas = labelService.pageQuery(label,page,size);
        return new Result(true,StatusCode.OK.getCode(),"分页查询成功",new PageResult<Label>(pageDatas.getTotalElements(),pageDatas.getContent()));
    }
}
