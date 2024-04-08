package com.japan.blockchain;

import com.japan.blockchain.chain.tron.client.TronMultiClient;
import com.japan.blockchain.chain.tron.dto.NetWorkType;
import com.japan.blockchain.chain.tron.dto.req.MultiRequest;
import com.japan.blockchain.chain.tron.dto.req.SmartMultiRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.proto.Response;

/**
 * @ClassName TradeTest
 * @Description 交易测试
 * @Version 1.0
 * @Author Jet
 * @Date 2024/4/5   17:35
 */

@Slf4j
public class TradeTest {


    @Test
    void tradeTrx() {
        ApiWrapper apiWrapper = ApiWrapper.ofNile("b3193b64b71e15860cc33420f737bf834b6c104eedf7ca42f99ee95baeffc490");
        Response.Account account = apiWrapper.getAccount("TVKVgeYpB5Vw3F4DfrXmHNZN9tLQQhVgbL");
        log.warn("账户信息:{}", account);
    }


    @Test
    //@Disabled
    void testMultiTrx() throws IllegalException {

        MultiRequest multiRequest = new MultiRequest();

        multiRequest.setApiWrapper(ApiWrapper.ofNile("7f22c659d1185b475f990e7356643a84b308d9ccdbfa68b14b7fc843d46af99d"));
        multiRequest.setNetWorkType(NetWorkType.MAIN_NET);
        multiRequest.setOwnerAddress("TDyCdgzzq7DZWpDdh4AwZcH9FDr9ukuufC");
        multiRequest.setControllerAddress("TSs2uVY5MxbMa8R4aS6d9313dvdHtsRpZm");
        multiRequest.setToAddress("TVKVgeYpB5Vw3F4DfrXmHNZN9tLQQhVgbL");
        multiRequest.setAmount(23L);
        TronMultiClient tronMultiClient = new TronMultiClient();
        tronMultiClient.multiTransaction(multiRequest);

    }


    @Test
        //@Disabled
    void tradeMultiUsdtTest() throws IllegalException {


        SmartMultiRequest multiRequest = new SmartMultiRequest();

        multiRequest.setApiWrapper(ApiWrapper.ofNile("7f22c659d1185b475f990e7356643a84b308d9ccdbfa68b14b7fc843d46af99d"));
        multiRequest.setNetWorkType(NetWorkType.MAIN_NET);
        multiRequest.setContractAddress("TXLAQ63Xg1NAzckPwKHvzw7CSEmLMEqcdj");
        multiRequest.setOwnerAddress("TDyCdgzzq7DZWpDdh4AwZcH9FDr9ukuufC");
        multiRequest.setControllerAddress("TSs2uVY5MxbMa8R4aS6d9313dvdHtsRpZm");
        multiRequest.setToAddress("TVKVgeYpB5Vw3F4DfrXmHNZN9tLQQhVgbL");
        multiRequest.setAmount(24L);
        TronMultiClient tronMultiClient = new TronMultiClient();
        tronMultiClient.multiSmartTransaction(multiRequest);

    }

}
