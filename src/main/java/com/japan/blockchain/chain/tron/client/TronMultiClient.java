package com.japan.blockchain.chain.tron.client;

import com.alibaba.fastjson2.JSONObject;
import com.japan.blockchain.chain.tron.dto.MultiTrade;
import com.japan.blockchain.chain.tron.dto.req.MultiRequest;
import com.japan.blockchain.chain.tron.dto.req.SmartMultiRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tron.trident.abi.TypeReference;
import org.tron.trident.abi.datatypes.Address;
import org.tron.trident.abi.datatypes.Bool;
import org.tron.trident.abi.datatypes.Function;
import org.tron.trident.abi.datatypes.generated.Uint256;
import org.tron.trident.core.contract.Contract;
import org.tron.trident.core.contract.Trc20Contract;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.core.transaction.TransactionBuilder;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Base58Check;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @ClassName TronMultiClient
 * @Description tron 波场多签客户端
 * @Version 1.0
 * @Author Jet
 * @Date 2024/4/8   17:38
 */
@Slf4j
public class TronMultiClient {


    /**
     * @Description 智能合约多签
     * @Author Jet
     * @Date 2024/4/8   17:51
     */
    public void multiSmartTransaction(SmartMultiRequest request) {
        BigInteger amount = BigInteger.valueOf(request.getAmount()).multiply(BigInteger.valueOf(10L).pow(6));
        //获取到合约信息
        Contract contract = request.getApiWrapper().getContract(request.getContractAddress());

        Trc20Contract trc20Contract = new Trc20Contract(contract, request.getOwnerAddress(), request.getApiWrapper());
        Function transfer = new Function("transfer",
                Arrays.asList(new Address(request.getToAddress()),
                        new Uint256(amount)),
                Arrays.asList(new TypeReference<Bool>() {
                }));
        TransactionBuilder builder = trc20Contract.getWrapper().triggerCall(Base58Check.bytesToBase58(trc20Contract.getOwnerAddr().toByteArray()), Base58Check.bytesToBase58(trc20Contract.getCntrAddr().toByteArray()), transfer);
        builder.setFeeLimit(100000000L);
        Chain.Transaction signedTxn = trc20Contract.getWrapper().signTransaction(builder.build());
        log.warn("发起只能合约交易:{}", signedTxn);

    }


    /**
     * @Description 多签
     * @Author Jet
     * @Date 2024/4/8   17:51
     */
    public boolean multiTransaction(MultiRequest request) throws IllegalException {

        //金额转化，默认为10的6次方
        long amount = BigInteger.valueOf(request.getAmount()).multiply(BigInteger.valueOf(10L).pow(6)).longValue();
        //创建一笔trx的交易事件
        Response.TransactionExtention source = request.getApiWrapper().transfer(request.getOwnerAddress(), request.getToAddress(), amount);

        MultiTrade.MultiValue multiValue = new MultiTrade.MultiValue(amount, request.getOwnerAddress(), request.getToAddress());

        MultiTrade multiTrade = MultiTrade.grpcResultToHttp(request.getApiWrapper().signTransaction(source), request.getApiWrapper().keyPair.toBase58CheckAddress(), request.getNetWorkType(), source.getTxid().toByteArray(), multiValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(JSONObject.toJSONString(multiTrade), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(request.getNetWorkType().getUrl(), requestEntity, String.class);
        log.warn("广播多签签名:{}", response.getBody());
        return response.getStatusCode().is2xxSuccessful();

    }
}
