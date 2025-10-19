package com.crm.service;

import com.crm.common.result.PageResult;
import com.crm.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.query.CustomerQuery;
import com.crm.vo.CustomerVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
@Service
public interface CustomerService extends IService<Customer> {
    /**
     * 分页查询客户列表
     *
     * @param query 分页查询条件
     * @return 分页结果对象（包含客户VO列表和总条数）
     */
    PageResult<CustomerVO> getPage(CustomerQuery query);
    /**
     * 导出客户信息
     *
     * @param query
     * @param httpResponse
     */
    void exportCustomer(CustomerQuery query, HttpServletResponse httpResponse);

    /**
     * 保存或更新客户信息
     * @param customerVO
     */
    void saveOrUpdate(CustomerVO customerVO);
}
