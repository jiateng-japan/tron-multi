package com.japan.blockchain.chain.tron.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.tron.trident.proto.Chain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MultiTrade
 * @Description 多签交易
 * @Version 1.0
 * @Author Jet
 * @Date 2024/4/8   16:35
 */
@Data
public class MultiTrade implements Serializable {

    @Serial
    private static final long serialVersionUID = 0L;

    private String address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String functionSelector;
    private String netType;
    private Transaction transaction;


    public MultiTrade(String address, String netType) {
        this.address = address;
        this.netType = netType;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Transaction {

        private String raw_data_hex;
        private String txID;
        private boolean visible = true;
        private RawData raw_data;
        private List<String> signature;


        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class RawData {

            private Long expiration;
            private String ref_block_bytes;
            private String ref_block_hash;

            @JsonInclude(JsonInclude.Include.NON_NULL)
            private Long fee_limit;

            private Long timestamp;
            private List<Contract> contract;


            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Contract {
                private String type;
                private Parameter<Object> parameter;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Parameter<T> {
                    private String type_url;
                    private T value;
                }
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultiValue {
        private Long amount;
        private String owner_address;
        private String to_address;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SmartMultiValue {
        private String contract_address;
        private String data;
        private String owner_address;
    }

    /**
     * @Description grpcResultToHttp
     * @Author Jet
     * @Date 2024/4/8   13:26
     */
    public static <T> MultiTrade grpcResultToHttp(Chain.Transaction source, String controllerAddress, NetWorkType netType, byte[] txid, T value){
        //构建参数信息
        Transaction.RawData.Contract.Parameter parameter = new Transaction.RawData.Contract.Parameter();
        parameter.setType_url(source.getRawData().getContract(0).getParameter().getTypeUrl());
        parameter.setValue(value);

        //构建合约信息
        Transaction.RawData.Contract contract = new Transaction.RawData.Contract();
        contract.setType(source.getRawData().getContract(0).getType().name());
        contract.setParameter(parameter);

        //构建数据块信息
        Transaction.RawData rawData = new Transaction.RawData();
        rawData.setRef_block_bytes(Hex.toHexString(source.getRawData().getRefBlockBytes().toByteArray()));
        rawData.setRef_block_hash(Hex.toHexString(source.getRawData().getRefBlockHash().toByteArray()));
        rawData.setExpiration(source.getRawData().getExpiration());
        rawData.setTimestamp(source.getRawData().getTimestamp());
        ArrayList<Transaction.RawData.Contract> contractArrayList = new ArrayList<>();
        contractArrayList.add(contract);
        rawData.setContract(contractArrayList);

        //构建交易信息
        Transaction transaction = new Transaction();
        transaction.setTxID(Hex.toHexString(txid));
        transaction.setRaw_data(rawData);
        transaction.setRaw_data_hex(Hex.toHexString(source.getRawData().toByteArray()));
        List<String> signList = new ArrayList<>(1);
        signList.add(Hex.toHexString(source.getSignature(0).toByteArray()));
        transaction.setSignature(signList);
        //构建多签信息
        MultiTrade result = new MultiTrade(controllerAddress, netType.getName());
        result.setTransaction(transaction);
        return result;
    }


}
