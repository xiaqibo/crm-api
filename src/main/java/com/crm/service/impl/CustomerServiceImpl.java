package com.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.exception.ServerException;
import com.crm.common.result.PageResult;
import com.crm.convert.CustomerConvert;
import com.crm.entity.Customer;
import com.crm.entity.SysManager;
import com.crm.mapper.CustomerMapper;
import com.crm.query.CustomerQuery;
import com.crm.security.user.SecurityUser;
import com.crm.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.utils.ExcelUtils;
import com.crm.vo.CustomerVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Override
    public PageResult<CustomerVO> getPage(CustomerQuery query) {
        Page<CustomerVO> page = new Page<>(query.getPage(), query.getLimit());
        MPJLambdaWrapper<Customer> wrapper = selection(query);
        Page<CustomerVO> result = baseMapper.selectJoinPage(page, CustomerVO.class, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    public void exportCustomer(CustomerQuery query, HttpServletResponse httpResponse) {
        MPJLambdaWrapper<Customer> wrapper = new MPJLambdaWrapper<>();
        List<Customer> customerList = baseMapper.selectJoinList(wrapper);
        ExcelUtils.writeExcel(httpResponse, customerList, "客户信息", "客户信息", CustomerVO.class);
    }

    private MPJLambdaWrapper<Customer> selection(CustomerQuery query) {
        MPJLambdaWrapper<Customer> wrapper = new MPJLambdaWrapper<>();

        // 构建查询关系
        wrapper.selectAll(Customer.class)
                .selectAs("o", SysManager::getAccount, CustomerVO::getOwnerName)
                .selectAs("c", SysManager::getAccount, CustomerVO::getCreaterName)
                .leftJoin(SysManager.class, "o", SysManager::getId, Customer::getOwnerId)
                .leftJoin(SysManager.class, "c", SysManager::getId, Customer::getCreaterId);

        // 搜索条件
        if (StringUtils.isNotBlank(query.getName())) {
            wrapper.like(Customer::getName, query.getName());
        }
        if (StringUtils.isNotBlank(query.getPhone())) {
            wrapper.like(Customer::getPhone, query.getPhone());
        }
        if (query.getLevel() != null) {
            wrapper.eq(Customer::getLevel, query.getLevel());
        }
        if (query.getSource() != null) {
            wrapper.eq(Customer::getSource, query.getSource());
        }
        if (query.getFollowStatus() != null) {
            wrapper.eq(Customer::getFollowStatus, query.getFollowStatus());
        }
        if (query.getIsPublic() != null) {
            wrapper.eq(Customer::getIsPublic, query.getIsPublic());
        }
        // 排序
        wrapper.orderByDesc(Customer::getCreateTime);
        return wrapper;
    }

    @Override
    public void saveOrUpdate(CustomerVO customerVO) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getPhone, customerVO.getPhone());

        if (customerVO.getId() == null) {
            // 新增
            Customer exist = baseMapper.selectOne(wrapper);
            if (exist != null) {
                throw new ServerException("该手机号客户已存在，请勿重复添加");
            }
            Customer entity = CustomerConvert.INSTANCE.convert(customerVO);
            Integer managerId = SecurityUser.getManagerId();
            entity.setCreaterId(managerId);
            entity.setOwnerId(managerId);
            // 补必填字段默认值
            entity.setFollowStatus(0); // 0 待跟进
            baseMapper.insert(entity);
        } else {
            // 更新
            wrapper.ne(Customer::getId, customerVO.getId());
            Customer exist = baseMapper.selectOne(wrapper);
            if (exist != null) {
                throw new ServerException("该手机号客户已存在，请勿重复添加");
            }
            Customer entity = CustomerConvert.INSTANCE.convert(customerVO);
            // 保证跟进状态不为空（若 VO 没传，可在这里补）
            if (entity.getFollowStatus() == null) {
                entity.setFollowStatus(0);
            }
            baseMapper.updateById(entity);
        }
    }
}
