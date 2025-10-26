package com.crm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.query.ProductQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */

public interface ProductMapper extends BaseMapper<Product> {

}