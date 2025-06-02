package com.nku.canteen.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nku.canteen.dto.StallDTO;
import com.nku.canteen.entity.Stall;
import com.nku.canteen.service.StallService;
import com.nku.canteen.util.R;
import com.nku.canteen.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 档口控制器
 */
@RestController
@RequestMapping("/stall")
public class StallController {

    @Autowired
    private StallService stallService;

    /**
     * 获取指定食堂下的档口列表
     */
    @GetMapping("/list")
    public R<?> getStallList(@RequestParam("canteen_id") Integer canteenId) {
        List<StallDTO> stallList = stallService.getStallListByCanteen(canteenId);
        return R.success(stallList);
    }

    /**
     * 获取档口详情
     */
    @GetMapping("/detail")
    public R<?> getStallDetail(@RequestParam("stall_id") Integer stallId) {
        StallDTO stallDTO = stallService.getStallDetail(stallId);
        if (stallDTO == null) {
            return R.notFound("档口不存在");
        }
        return R.success(stallDTO);
    }

    /**
     * 新增档口（管理员）
     */
    @PostMapping("/add")
    public R<?> addStall(@RequestBody Stall stall, HttpServletRequest request) {
        // 只有管理员才能添加档口
        if (!"admin".equals(request.getAttribute("userType"))) {
            return R.unauthorized("非管理员无权添加档口");
        }
        
        // 参数校验
        if (stall.getName() == null) {
            return R.paramError("档口名称不能为空");
        }
        if (stall.getCanteenId() == null) {
            return R.paramError("所属食堂ID不能为空");
        }
        
        int result = stallService.addStall(stall);
        if (result <= 0) {
            return R.error(999, "添加失败");
        }
        
        return R.success(stall.getStallId(), "添加成功");
    }

    /**
     * 更新档口（管理员）
     */
    @PostMapping("/update")
    public R<?> updateStall(@RequestBody Stall stall, HttpServletRequest request) {
        // 只有管理员才能更新档口
        if (!"admin".equals(request.getAttribute("userType"))) {
            return R.unauthorized("非管理员无权更新档口");
        }
        
        // 参数校验
        if (stall.getStallId() == null) {
            return R.paramError("档口ID不能为空");
        }
        
        try {
            int result = stallService.updateStall(stall);
            return R.success(null, "更新成功");
        } catch (ServiceException e) {
            return R.paramError(e.getMessage());
        } catch (Exception e) {
            return R.systemError();
        }
    }

    /**
     * 删除档口（管理员）
     */
    @PostMapping("/delete")
    public R<?> deleteStall(@RequestParam("stall_id") Integer stallId, HttpServletRequest request) {
        // 只有管理员才能删除档口
        if (!"admin".equals(request.getAttribute("userType"))) {
            return R.unauthorized("非管理员无权删除档口");
        }
        
        // 检查档口是否存在
        StallDTO existingStall = stallService.getStallDetail(stallId);
        if (existingStall == null) {
            return R.notFound("档口不存在");
        }
        
        try {
            int result = stallService.deleteStall(stallId);
            if (result <= 0) {
                return R.error(999, "删除失败");
            }
            
            return R.success(null, "删除成功");
        } catch (Exception e) {
            // 打印异常信息到控制台
            System.err.println("删除档口时发生异常: " + e.getMessage());
            e.printStackTrace();
            // 打印更详细的异常栈信息
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (int i = 0; i < Math.min(10, stackTrace.length); i++) {
                System.err.println("  at " + stackTrace[i]);
            }
            
            return R.error(999, "系统异常: " + e.getMessage());
        }
    }
} 