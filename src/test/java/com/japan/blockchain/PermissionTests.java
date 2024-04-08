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
class PermissionTests {


    @Test
    void createAccount() throws IllegalException {
        ApiWrapper apiWrapper = ApiWrapper.ofNile("e81e598bc65ca85149f67e9b3a59f465c0da8f7aad4333d7b17a3a97c9a7a237");
        KeyPair keyPair = KeyPair.generate();
        Response.TransactionExtention transfer = apiWrapper.createAccount("TSGrmkfFdUrjmbaFQafpzULJf3dsdR5rm8", keyPair.toBase58CheckAddress());
        log.warn("新账户密钥信息：{}",keyPair.toPrivateKey());
        Chain.Transaction transaction = apiWrapper.signTransaction(transfer);
        String s = apiWrapper.broadcastTransaction(transaction);
        log.warn("创建新账户:{}", s);
    }


    @Test
    void updatePermission() throws IllegalException {
        ApiWrapper wrapper = ApiWrapper.ofNile("e81e598bc65ca85149f67e9b3a59f465c0da8f7aad4333d7b17a3a97c9a7a237");
        Contract.AccountPermissionUpdateContract.Builder builder = Contract.AccountPermissionUpdateContract.newBuilder();
        Common.Permission ownerPermission = null;
        Common.Permission.Builder builderOwner = Common.Permission.newBuilder();
        builderOwner.setTypeValue(0);
        builderOwner.setPermissionName("owner");
        builderOwner.setThreshold(2);

        Common.Key.Builder keyOwner = Common.Key.newBuilder();
        keyOwner.setAddress(ApiWrapper.parseAddress("TTq3bxh4hCwCYadUcsNhxHa1TvFtxKYiSq"));
        keyOwner.setWeight(1);
        builderOwner.addKeys(keyOwner);
        Common.Key.Builder keyOwner2 = Common.Key.newBuilder();
        keyOwner2.setAddress(ApiWrapper.parseAddress("TKnd3jYtLkGv9cZLZGpmTJBZgFQPaVsofr"));
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
        keyActive.setAddress(ApiWrapper.parseAddress("TTq3bxh4hCwCYadUcsNhxHa1TvFtxKYiSq"));
        keyActive.setWeight(1);

        builderActive.addKeys(keyActive);

        Common.Key.Builder keyActive2 = Common.Key.newBuilder();
        keyActive2.setAddress(ApiWrapper.parseAddress("TKnd3jYtLkGv9cZLZGpmTJBZgFQPaVsofr"));
        keyActive2.setWeight(1);

        builderActive.addKeys(keyActive2);
        activePermissions = builderActive.build();

        builder.setOwner(ownerPermission);
        builder.addActives(activePermissions);
        builder.setOwnerAddress(ApiWrapper.parseAddress("TWQSGmWQ4Y8cctti5Y9LZHqfedMiNe7VwR"));

        Response.TransactionExtention transaction = wrapper.accountPermissionUpdate(builder.build());
        Chain.Transaction signedTxn = wrapper.signTransaction(transaction, new KeyPair("2e3f0add9e1544542984d2f6db585601010d588eff4976f4775e514699e0b3a9"));
        signedTxn = wrapper.signTransaction(signedTxn, new KeyPair("69f59bb33a04c56bc5aaa8bf0451e4be8c540cdfb32a6bc8b690885876be2945"));
        String ret = wrapper.broadcastTransaction(signedTxn);
        log.warn("修改权限:{}", ret);
    }



}
