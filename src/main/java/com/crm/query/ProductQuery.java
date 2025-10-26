package com.crm.query;

import com.crm.common.model.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.crm.entity.Product;
/**
 * @Author: zmy
 * @Date: 2025/10/26
 * @Version: 1.0
 */
/**
 * @Date: 2025/10/26
 * @Version: 1.0
 * 商品查询参数类
 */

@Data
public class ProductQuery extends Query {
    @Schema(description = "商品名称")
    private String name;
    @Schema(description = "商品状态")
    private Integer status;
}