package org.fisco.bcos.solidity;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.*;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple3;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class UserMeter extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040523480156200001157600080fd5b506040516200151a3803806200151a83398101806040528101908080518201929190602001805182019291906020018051820192919050505033600460006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508260019080519060200190620000a3929190620000df565b508160029080519060200190620000bc929190620000df565b508060039080519060200190620000d5929190620000df565b505050506200018e565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200012257805160ff191683800117855562000153565b8280016001018555821562000153579182015b828111156200015257825182559160200191906001019062000135565b5b50905062000162919062000166565b5090565b6200018b91905b80821115620001875760008160009055506001016200016d565b5090565b90565b61137c806200019e6000396000f3006080604052600436106100af576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630b929a2d146100b45780632ae18dd8146101f35780632b7ac3f3146103b75780633ec045a61461040e57806374754282146104655780638c72c54e146104f5578063a2161eba1461054c578063a81af5f7146105dc578063b25b1a131461066c578063bef4876b14610738578063d56b288914610767575b600080fd5b3480156100c057600080fd5b506100df6004803603810190808035906020019092919050505061077e565b60405180846fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561014f578082015181840152602081019050610134565b50505050905090810190601f16801561017c5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156101b557808201518184015260208101905061019a565b50505050905090810190601f1680156101e25780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b3480156101ff57600080fd5b5061023160048036038101908080356fffffffffffffffffffffffffffffffff19169060200190929190505050610904565b604051808060200180602001806020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001848103845288818151815260200191508051906020019080838360005b838110156102ab578082015181840152602081019050610290565b50505050905090810190601f1680156102d85780820380516001836020036101000a031916815260200191505b50848103835287818151815260200191508051906020019080838360005b838110156103115780820151818401526020810190506102f6565b50505050905090810190601f16801561033e5780820380516001836020036101000a031916815260200191505b50848103825286818151815260200191508051906020019080838360005b8381101561037757808201518184015260208101905061035c565b50505050905090810190601f1680156103a45780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b3480156103c357600080fd5b506103cc610ba8565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561041a57600080fd5b50610423610bce565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561047157600080fd5b5061047a610bf4565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104ba57808201518184015260208101905061049f565b50505050905090810190601f1680156104e75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561050157600080fd5b5061050a610c92565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561055857600080fd5b50610561610cb8565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105a1578082015181840152602081019050610586565b50505050905090810190601f1680156105ce5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156105e857600080fd5b506105f1610d56565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610631578082015181840152602081019050610616565b50505050905090810190601f16801561065e5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561067857600080fd5b5061073660048036038101908080356fffffffffffffffffffffffffffffffff19169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610df4565b005b34801561074457600080fd5b5061074d611069565b604051808215151515815260200191505060405180910390f35b34801561077357600080fd5b5061077c61107c565b005b60008181548110151561078d57fe5b90600052602060002090600302016000915090508060000160009054906101000a90047001000000000000000000000000000000000290806001018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561085c5780601f106108315761010080835404028352916020019161085c565b820191906000526020600020905b81548152906001019060200180831161083f57829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108fa5780601f106108cf576101008083540402835291602001916108fa565b820191906000526020600020905b8154815290600101906020018083116108dd57829003601f168201915b5050505050905083565b606080606060008060008091505b600080549050821015610b9f5760008281548110151561092e57fe5b90600052602060002090600302019050866fffffffffffffffffffffffffffffffff19168160000160009054906101000a9004700100000000000000000000000000000000026fffffffffffffffffffffffffffffffff19161415610b925760018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a235780601f106109f857610100808354040283529160200191610a23565b820191906000526020600020905b815481529060010190602001808311610a0657829003601f168201915b50505050509550806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610ac25780601f10610a9757610100808354040283529160200191610ac2565b820191906000526020600020905b815481529060010190602001808311610aa557829003601f168201915b50505050509450806002018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b615780601f10610b3657610100808354040283529160200191610b61565b820191906000526020600020905b815481529060010190602001808311610b4457829003601f168201915b50505050509350600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169250610b9f565b8180600101925050610912565b50509193509193565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c8a5780601f10610c5f57610100808354040283529160200191610c8a565b820191906000526020600020905b815481529060010190602001808311610c6d57829003601f168201915b505050505081565b600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60028054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610d4e5780601f10610d2357610100808354040283529160200191610d4e565b820191906000526020600020905b815481529060010190602001808311610d3157829003601f168201915b505050505081565b60038054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610dec5780601f10610dc157610100808354040283529160200191610dec565b820191906000526020600020905b815481529060010190602001808311610dcf57829003601f168201915b505050505081565b3373ffffffffffffffffffffffffffffffffffffffff16600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515610edf576040517f08c379a00000000000000000000000000000","000000000000000000000000000081526004018080602001828103825260278152602001807fe58faae69c89e6a380e5ae9ae59198e58fafe4bba5e68f90e4baa4e6a380e5ae81526020017f9ae7bb93e69e9c0000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600460149054906101000a900460ff16151515610f8a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260338152602001807fe5a794e68998e58d95e5ae8ce68890e5908ee4b88de58fafe6b7bbe58aa0e6b081526020017fb4e8a1a8e79a84e6a380e5ae9ae7bb93e69e9c0000000000000000000000000081525060400191505060405180910390fd5b6000606060405190810160405280856fffffffffffffffffffffffffffffffff19168152602001848152602001838152509080600181540180825580915050906001820390600052602060002090600302016000909192909190915060008201518160000160006101000a8154816fffffffffffffffffffffffffffffffff02191690837001000000000000000000000000000000009004021790555060208201518160010190805190602001906110439291906112ab565b5060408201518160020190805190602001906110609291906112ab565b50505050505050565b600460149054906101000a900460ff1681565b3373ffffffffffffffffffffffffffffffffffffffff16600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515611167576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001807fe58faae69c89e6a380e5ae9ae59198e58fafe4bba5e5ae8ce68890e5a794e68981526020017f98e58d950000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600460149054906101000a900460ff16151515611212576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001807fe5a794e68998e58d95e5ae8ce68890e5908ee4b88de58fafe5868de6aca1e5ae81526020017f8ce688900000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6000808054905011151561128e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807fe7a9bae5a794e68998e58d95e4b88de58fafe4bba5e5ae8ce68890000000000081525060200191505060405180910390fd5b6001600460146101000a81548160ff021916908315150217905550565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106112ec57805160ff191683800117855561131a565b8280016001018555821561131a579182015b828111156113195782518255916020019190600101906112fe565b5b509050611327919061132b565b5090565b61134d91905b80821115611349576000816000905550600101611331565b5090565b905600a165627a7a723058201f3f50f0212c7a0b2e0c8b6f7165cba4042041cf3689a092da76c351e712406d0029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"meterList\",\"outputs\":[{\"name\":\"meterId\",\"type\":\"bytes16\"},{\"name\":\"verifyTime\",\"type\":\"string\"},{\"name\":\"result\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_meterId\",\"type\":\"bytes16\"}],\"name\":\"getInfo\",\"outputs\":[{\"name\":\"_manufacturer\",\"type\":\"string\"},{\"name\":\"_verifyTime\",\"type\":\"string\"},{\"name\":\"_result\",\"type\":\"string\"},{\"name\":\"_verifier\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"verifier\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"auditor\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"manufacturer\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"publisher\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"verifierName\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"validDate\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_meterId\",\"type\":\"bytes16\"},{\"name\":\"_verifyTime\",\"type\":\"string\"},{\"name\":\"_result\",\"type\":\"string\"}],\"name\":\"verify\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"finished\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"finish\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_manufacturer\",\"type\":\"string\"},{\"name\":\"_verifierName\",\"type\":\"string\"},{\"name\":\"_validDate\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC_METERLIST = "meterList";

    public static final String FUNC_GETINFO = "getInfo";

    public static final String FUNC_VERIFIER = "verifier";

    public static final String FUNC_AUDITOR = "auditor";

    public static final String FUNC_MANUFACTURER = "manufacturer";

    public static final String FUNC_PUBLISHER = "publisher";

    public static final String FUNC_VERIFIERNAME = "verifierName";

    public static final String FUNC_VALIDDATE = "validDate";

    public static final String FUNC_VERIFY = "verify";

    public static final String FUNC_FINISHED = "finished";

    public static final String FUNC_FINISH = "finish";

    @Deprecated
    protected UserMeter(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected UserMeter(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected UserMeter(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected UserMeter(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<Tuple3<byte[], String, String>> meterList(BigInteger param0) {
        final Function function = new Function(FUNC_METERLIST, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes16>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple3<byte[], String, String>>(
                new Callable<Tuple3<byte[], String, String>>() {
                    @Override
                    public Tuple3<byte[], String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<byte[], String, String>(
                                (byte[]) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteCall<Tuple4<String, String, String, String>> getInfo(byte[] _meterId) {
        final Function function = new Function(FUNC_GETINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16(_meterId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
        return new RemoteCall<Tuple4<String, String, String, String>>(
                new Callable<Tuple4<String, String, String, String>>() {
                    @Override
                    public Tuple4<String, String, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<String> verifier() {
        final Function function = new Function(FUNC_VERIFIER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> auditor() {
        final Function function = new Function(FUNC_AUDITOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> manufacturer() {
        final Function function = new Function(FUNC_MANUFACTURER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> publisher() {
        final Function function = new Function(FUNC_PUBLISHER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> verifierName() {
        final Function function = new Function(FUNC_VERIFIERNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> validDate() {
        final Function function = new Function(FUNC_VALIDDATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> verify(byte[] _meterId, String _verifyTime, String _result) {
        final Function function = new Function(
                FUNC_VERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16(_meterId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifyTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_result)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void verify(byte[] _meterId, String _verifyTime, String _result, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_VERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16(_meterId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifyTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_result)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String verifySeq(byte[] _meterId, String _verifyTime, String _result) {
        final Function function = new Function(
                FUNC_VERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16(_meterId), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifyTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_result)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple3<byte[], String, String> getVerifyInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_VERIFY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes16>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple3<byte[], String, String>(

                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue()
                );
    }

    public RemoteCall<Boolean> finished() {
        final Function function = new Function(FUNC_FINISHED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> finish() {
        final Function function = new Function(
                FUNC_FINISH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void finish(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_FINISH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String finishSeq() {
        final Function function = new Function(
                FUNC_FINISH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    @Deprecated
    public static UserMeter load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new UserMeter(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static UserMeter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new UserMeter(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static UserMeter load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new UserMeter(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static UserMeter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new UserMeter(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<UserMeter> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _manufacturer, String _verifierName, String _validDate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_manufacturer), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifierName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_validDate)));
        return deployRemoteCall(UserMeter.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<UserMeter> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _manufacturer, String _verifierName, String _validDate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_manufacturer), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifierName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_validDate)));
        return deployRemoteCall(UserMeter.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<UserMeter> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _manufacturer, String _verifierName, String _validDate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_manufacturer), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifierName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_validDate)));
        return deployRemoteCall(UserMeter.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<UserMeter> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _manufacturer, String _verifierName, String _validDate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_manufacturer), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_verifierName), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_validDate)));
        return deployRemoteCall(UserMeter.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
