package com.japan.blockchain.chain.tron.dto.req;

import com.japan.blockchain.chain.tron.dto.NetWorkType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tron.trident.core.ApiWrapper;

/**
 * @ClassName SmartMultiRequest
 * @Description 智能合约多签请求
 * @Version 1.0
 * @Author Jet
 * @Date 2024/4/8   17:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiRequest {

    /**
     * 客户端
     */
    private ApiWrapper apiWrapper;


    /**
     * 网络类型
     */
    private NetWorkType netWorkType;

    /**
     * 多签地址
     */
    private String ownerAddress;

    /**
     * 收款地址
     */
    private String toAddress;

    /**
     * 金额
     */
    private long amount;
}
