package com.nku.canteen.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nku.canteen.dto.CanteenDTO;
import com.nku.canteen.entity.Canteen;
import com.nku.canteen.service.CanteenService;
import com.nku.canteen.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 食堂控制器
 */
@RestController
@RequestMapping("/canteen")
public class CanteenController {

    @Autowired
    private CanteenService canteenService;

    /**
     * 获取食堂列表
     */
    @GetMapping("/list")
    public R<?> getCanteenList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, name = "page_size") Integer pageSize) {
        List<CanteenDTO> canteenList = canteenService.getCanteenList(page, pageSize);
        return R.success(canteenList);
    }

    /**
     * 根据校区获取食堂列表
     */
    @GetMapping("/list/campus")
    public R<?> getCanteenListByCampus(
            @RequestParam("campus") String campus,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, name = "page_size") Integer pageSize) {
        if (campus == null || campus.trim().isEmpty()) {
            return R.paramError("校区名称不能为空");
        }
        List<CanteenDTO> canteenList = canteenService.getCanteenListByCampus(campus, page, pageSize);
        return R.success(canteenList);
    }

    /**
     * 获取食堂详情
     */
    @GetMapping("/detail")
    public R<?> getCanteenDetail(@RequestParam("canteen_id") Integer canteenId) {
        CanteenDTO canteenDTO = canteenService.getCanteenDetail(canteenId);
        if (canteenDTO == null) {
            return R.notFound("食堂不存在");
        }
        return R.success(canteenDTO);
    }

    /**
     * 新增食堂（管理员）
     */
    @PostMapping("/add")
    public R<?> addCanteen(@RequestBody Canteen canteen, HttpServletRequest request) {
        // 只有管理员才能添加食堂
        if (!"admin".equals(request.getAttribute("userType"))) {
            return R.unauthorized("非管理员无权添加食堂");
        }
        
        // 参数校验
        if (canteen.getName() == null) {
            return R.paramError("食堂名称不能为空");
        }
        
        if (canteen.getCampus() == null) {
            return R.paramError("食堂校区不能为空");
        }
        
        int result = canteenService.addCanteen(canteen);
        if (result <= 0) {
            return R.error(999, "添加失败");
        }
        
        return R.success(canteen.getCanteenId(), "添加成功");
    }

    /**
     * 更新食堂（管理员）
     */
    @PostMapping("/update")
    public R<?> updateCanteen(@RequestBody Canteen canteen, HttpServletRequest request) {
        // 只有管理员才能更新食堂
        if (!"admin".equals(request.getAttribute("userType"))) {
            return R.unauthorized("非管理员无权更新食堂");
        }
        
        // 参数校验
        if (canteen.getCanteenId() == null) {
            return R.paramError("食堂ID不能为空");
        }
        
        // 检查食堂是否存在
        CanteenDTO existingCanteen = canteenService.getCanteenDetail(canteen.getCanteenId());
        if (existingCanteen == null) {
            return R.notFound("食堂不存在");
        }
        
        int result = canteenService.updateCanteen(canteen);
        if (result <= 0) {
            return R.error(999, "更新失败");
        }
        
        return R.success(null, "更新成功");
    }

    /**
     * 删除食堂（管理员）
     */
    @PostMapping("/delete")
    public R<?> deleteCanteen(@RequestParam("canteen_id") Integer canteenId, HttpServletRequest request) {
        // 只有管理员才能删除食堂
        if (!"admin".equals(request.getAttribute("userType"))) {
            return R.unauthorized("非管理员无权删除食堂");
        }
        
        // 检查食堂是否存在
        CanteenDTO existingCanteen = canteenService.getCanteenDetail(canteenId);
        if (existingCanteen == null) {
            return R.notFound("食堂不存在");
        }
        
        try {
            int result = canteenService.deleteCanteen(canteenId);
            if (result <= 0) {
                return R.error(999, "删除失败");
            }
            
            return R.success(null, "删除成功");
        } catch (Exception e) {
            // 打印异常信息到控制台
            System.err.println("删除食堂时发生异常: " + e.getMessage());
            e.printStackTrace();
            return R.error(999, "系统异常");
        }
    }
}