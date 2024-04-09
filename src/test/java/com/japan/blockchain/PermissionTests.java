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


@Slf4j
@Disabled
class PermissionTests {


    @Test
    void createAccount() throws IllegalException {
        ApiWrapper apiWrapper = ApiWrapper.ofNile("e81e598bc65ca85149f67e9b3a59f465c0da8f7aad4333d7b17a3a97c9a7a237");
        KeyPair keyPair = KeyPair.generate();
        Response.TransactionExtention transfer = apiWrapper.createAccount("TSGrmkfFdUrjmbaFQafpzULJf3dsdR5rm8", keyPair.toBase58CheckAddress());
        log.debug("新账户密钥信息：{}", keyPair.toPrivateKey());
        Chain.Transaction transaction = apiWrapper.signTransaction(transfer);
        String s = apiWrapper.broadcastTransaction(transaction);
        log.debug("创建新账户:{}", s);
    }


    @Test
    void updatePermission() throws IllegalException {
        ApiWrapper wrapper = ApiWrapper.ofNile("e81e598bc65ca85149f67e9b3a59f465c0da8f7aad4333d7b17a3a97c9a7a237");
        Contract.AccountPermissionUpdateContract.Builder builder = Contract.AccountPermissionUpdateContract.newBuilder();
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
        Common.Permission ownerPermission = builderOwner.build();

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
        Common.Permission activePermissions = builderActive.build();

        builder.setOwner(ownerPermission);
        builder.addActives(activePermissions);
        builder.setOwnerAddress(ApiWrapper.parseAddress("TDyCdgzzq7DZWpDdh4AwZcH9FDr9ukuufC"));

        Response.TransactionExtention transaction = wrapper.accountPermissionUpdate(builder.build());
        Chain.Transaction signedTxn = wrapper.signTransaction(transaction, new KeyPair("f35b9e9e9f20f5a68dd7a24da378c38bf314fb6b7e100bf1f0b0beeb388d478e"));
        //signedTxn = wrapper.signTransaction(signedTxn, new KeyPair("4e885ccf568c2d382bfd15a7b05a1a7b63f4e66806cbb7e6e64169cfe639a31a"));
        String ret = wrapper.broadcastTransaction(signedTxn);
        log.debug("修改权限:{}", ret);
    }


}
