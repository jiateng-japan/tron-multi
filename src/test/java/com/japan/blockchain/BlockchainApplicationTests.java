package com.japan.blockchain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Common;
import org.tron.trident.proto.Contract;
import org.tron.trident.proto.Response;

@Disabled
@Slf4j
class BlockchainApplicationTests {


    @Test
    void tradeTrx() throws IllegalException {
        ApiWrapper apiWrapper = ApiWrapper.ofNile("b3193b64b71e15860cc33420f737bf834b6c104eedf7ca42f99ee95baeffc490");
        Response.TransactionExtention transfer = apiWrapper.transfer("TB2BtGBwVvvMQPJgqDSA1R5eLnoFY9CzNv", "TVKVgeYpB5Vw3F4DfrXmHNZN9tLQQhVgbL", 50L);
        Chain.Transaction transaction = apiWrapper.signTransaction(transfer);
        String s = apiWrapper.broadcastTransaction(transaction);
        log.warn("交易:{}", s);
    }


    @Test
    void updatePermission() throws IllegalException {
        ApiWrapper wrapper = ApiWrapper.ofNile("b3193b64b71e15860cc33420f737bf834b6c104eedf7ca42f99ee95baeffc490");
        Contract.AccountPermissionUpdateContract.Builder builder = Contract.AccountPermissionUpdateContract.newBuilder();
        Common.Permission ownerPermission = null;
        Common.Permission.Builder builderOwner = Common.Permission.newBuilder();
        builderOwner.setTypeValue(0);
        builderOwner.setPermissionName("owner");
        builderOwner.setThreshold(2);

        Common.Key.Builder keyOwner = Common.Key.newBuilder();
        keyOwner.setAddress(ApiWrapper.parseAddress("TSs2uVY5MxbMa8R4aS6d9313dvdHtsRpZm"));
        keyOwner.setWeight(1);
        builderOwner.addKeys(keyOwner);
        Common.Key.Builder keyOwner2 = Common.Key.newBuilder();
        keyOwner2.setAddress(ApiWrapper.parseAddress("TBfVhFEHzfr6Uo6a758VcyVoYxD3uPbYzn"));
        keyOwner2.setWeight(1);
        builderOwner.addKeys(keyOwner2);
        ownerPermission = builderOwner.build();

        Common.Permission activePermissions = null;
        Common.Permission.Builder builderActive = Common.Permission.newBuilder();
        builderActive.setTypeValue(2);
        builderActive.setThreshold(2);
        builderActive.setPermissionName("active0");
        builderActive.setOperations(ApiWrapper.parseAddress("7fff1fc0037e0000000000000000000000000000000000000000000000000000"));

        Common.Key.Builder keyActive = Common.Key.newBuilder();
        keyActive.setAddress(ApiWrapper.parseAddress("TSs2uVY5MxbMa8R4aS6d9313dvdHtsRpZm"));
        keyActive.setWeight(1);

        builderActive.addKeys(keyActive);

        Common.Key.Builder keyActive2 = Common.Key.newBuilder();
        keyActive2.setAddress(ApiWrapper.parseAddress("TBfVhFEHzfr6Uo6a758VcyVoYxD3uPbYzn"));
        keyActive2.setWeight(1);

        builderActive.addKeys(keyActive2);
        activePermissions = builderActive.build();

        builder.setOwner(ownerPermission);
        builder.addActives(activePermissions);
        builder.setOwnerAddress(ApiWrapper.parseAddress("TDyCdgzzq7DZWpDdh4AwZcH9FDr9ukuufC"));

        Response.TransactionExtention transaction = wrapper.accountPermissionUpdate(builder.build());
        Chain.Transaction signedTxn = wrapper.signTransaction(transaction, new KeyPair("4e885ccf568c2d382bfd15a7b05a1a7b63f4e66806cbb7e6e64169cfe639a31a"));
        signedTxn = wrapper.signTransaction(signedTxn, new KeyPair("7f22c659d1185b475f990e7356643a84b308d9ccdbfa68b14b7fc843d46af99d"));
        signedTxn = wrapper.signTransaction(signedTxn, new KeyPair("0579e6a54a60a7c221fe1f85f3d58cc8f92eaeb39a2ff76b5325e45d63d3e3a2"));
        String ret = wrapper.broadcastTransaction(signedTxn);
        log.warn("修改权限:{}", ret);
    }



}
