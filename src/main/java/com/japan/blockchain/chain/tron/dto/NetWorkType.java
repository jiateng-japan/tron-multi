package com.japan.blockchain.chain.tron.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName NetWorkType
 * @Description 网络类型
 * @Version 1.0
 * @Author Jet
 * @Date 2024/4/8   16:44
 */
@Getter
@AllArgsConstructor
public enum NetWorkType {

    MAIN_NET("main_net","https://niletest.tronlink.org/api/wallet/multi/transaction?serializable=true"),;


    private final String name;
    private final String url;

}
