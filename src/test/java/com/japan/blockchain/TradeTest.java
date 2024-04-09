package com.japan.blockchain;

import com.japan.blockchain.chain.tron.client.TronMultiClient;
import com.japan.blockchain.chain.tron.dto.NetWorkType;
import com.japan.blockchain.chain.tron.dto.req.MultiRequest;
import com.japan.blockchain.chain.tron.dto.req.SmartMultiRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.exceptions.IllegalException;

/**
 * @ClassName TradeTest
 * @Description 交易测试
 * @Version 1.0
 * @Author Jet
 * @Date 2024/4/5   17:35
 */

@Slf4j
@Disabled
public class TradeTest {

    @Test
    void testMultiTrx() throws IllegalException {

        MultiRequest multiRequest = new MultiRequest();
        multiRequest.setApiWrapper(ApiWrapper.ofNile("7f22c659d1185b475f990e7356643a84b308d9ccdbfa68b14b7fc843d46af99d"));
        multiRequest.setNetWorkType(NetWorkType.MAIN_NET);
        multiRequest.setOwnerAddress("TDyCdgzzq7DZWpDdh4AwZcH9FDr9ukuufC");
        multiRequest.setToAddress("TVKVgeYpB5Vw3F4DfrXmHNZN9tLQQhVgbL");
        multiRequest.setAmount(33L);
        TronMultiClient tronMultiClient = new TronMultiClient();
        boolean b = tronMultiClient.multiTransaction(multiRequest);
        log.debug("trx 多签广播交易结果：{}", b);

    }


    @Test
    void tradeMultiUSDTTest() {
        SmartMultiRequest multiRequest = new SmartMultiRequest();
        multiRequest.setApiWrapper(ApiWrapper.ofNile("2e3f0add9e1544542984d2f6db585601010d588eff4976f4775e514699e0b3a9"));
        multiRequest.setNetWorkType(NetWorkType.MAIN_NET);
        multiRequest.setContractAddress("TXLAQ63Xg1NAzckPwKHvzw7CSEmLMEqcdj");
        multiRequest.setOwnerAddress("TWQSGmWQ4Y8cctti5Y9LZHqfedMiNe7VwR");
        multiRequest.setToAddress("TSGrmkfFdUrjmbaFQafpzULJf3dsdR5rm8");
        multiRequest.setAmount(19L);
        TronMultiClient tronMultiClient = new TronMultiClient();
        boolean b = tronMultiClient.multiSmartTransaction(multiRequest);
        log.debug("智能合约多签广播交易结果：{}", b);

    }

}
