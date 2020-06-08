package org.fisco.bcos.temp

import org.fisco.bcos.channel.client.TransactionSucCallback
import org.fisco.bcos.temp.UserMeter
import org.fisco.bcos.web3j.abi.FunctionEncoder
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder
import org.fisco.bcos.web3j.abi.TypeReference
import org.fisco.bcos.web3j.abi.datatypes.*
import org.fisco.bcos.web3j.abi.datatypes.Function
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes16
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.protocol.Web3j
import org.fisco.bcos.web3j.protocol.core.RemoteCall
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt
import org.fisco.bcos.web3j.tuples.generated.Tuple3
import org.fisco.bcos.web3j.tuples.generated.Tuple4
import org.fisco.bcos.web3j.tx.Contract
import org.fisco.bcos.web3j.tx.TransactionManager
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder
import java.math.BigInteger
import java.util.*
import java.util.concurrent.Callable

/**
 *
 * Auto generated code.
 *
 * **Do not modify!**
 *
 * Please use the [web3j command line tools](https://docs.web3j.io/command_line.html),
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * [codegen module](https://github.com/web3j/web3j/tree/master/codegen) to update.
 *
 *
 * Generated with web3j version none.
 */
class UserMeter : Contract {
    @Deprecated("")
    protected constructor(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?) : super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit) {
    }

    protected constructor(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?) : super(BINARY, contractAddress, web3j, credentials, contractGasProvider) {}

    @Deprecated("")
    protected constructor(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?) : super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit) {
    }

    protected constructor(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?) : super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider) {}

    fun meterList(param0: BigInteger?): RemoteCall<Tuple3<ByteArray, String, String>> {
        val function = Function(FUNC_METERLIST,
                Arrays.asList<Type<*>>(Uint256(param0)),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Bytes16?>() {}, object : TypeReference<Utf8String?>() {}, object : TypeReference<Utf8String?>() {}))
        return RemoteCall(
                Callable {
                    val results = executeCallMultipleValueReturn(function)
                    Tuple3(
                            results[0].value as ByteArray,
                            results[1].value as String,
                            results[2].value as String)
                })
    }

    fun getInfo(_meterId: ByteArray?): RemoteCall<Tuple4<String, String, String, String>> {
        val function = Function(FUNC_GETINFO,
                Arrays.asList<Type<*>>(Bytes16(_meterId)),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Utf8String?>() {}, object : TypeReference<Utf8String?>() {}, object : TypeReference<Utf8String?>() {}, object : TypeReference<Address?>() {}))
        return RemoteCall(
                Callable {
                    val results = executeCallMultipleValueReturn(function)
                    Tuple4(
                            results[0].value as String,
                            results[1].value as String,
                            results[2].value as String,
                            results[3].value as String)
                })
    }

    fun verifier(): RemoteCall<String> {
        val function = Function(FUNC_VERIFIER,
                Arrays.asList(),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Address?>() {}))
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun auditor(): RemoteCall<String> {
        val function = Function(FUNC_AUDITOR,
                Arrays.asList(),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Address?>() {}))
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun manufacturer(): RemoteCall<String> {
        val function = Function(FUNC_MANUFACTURER,
                Arrays.asList(),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Utf8String?>() {}))
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun publisher(): RemoteCall<String> {
        val function = Function(FUNC_PUBLISHER,
                Arrays.asList(),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Address?>() {}))
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun verify(_meterId: ByteArray?, _verifyTime: String?, _result: String?): RemoteCall<TransactionReceipt> {
        val function = Function(
                FUNC_VERIFY,
                Arrays.asList<Type<*>>(Bytes16(_meterId),
                        Utf8String(_verifyTime),
                        Utf8String(_result)), emptyList())
        return executeRemoteCallTransaction(function)
    }

    fun verify(_meterId: ByteArray?, _verifyTime: String?, _result: String?, callback: TransactionSucCallback?) {
        val function = Function(
                FUNC_VERIFY,
                Arrays.asList<Type<*>>(Bytes16(_meterId),
                        Utf8String(_verifyTime),
                        Utf8String(_result)), emptyList())
        asyncExecuteTransaction(function, callback)
    }

    fun verifySeq(_meterId: ByteArray?, _verifyTime: String?, _result: String?): String {
        val function = Function(
                FUNC_VERIFY,
                Arrays.asList<Type<*>>(Bytes16(_meterId),
                        Utf8String(_verifyTime),
                        Utf8String(_result)), emptyList())
        return createTransactionSeq(function)
    }

    fun getVerifyInput(transactionReceipt: TransactionReceipt): Tuple3<ByteArray, String, String> {
        val data = transactionReceipt.input.substring(10)
        val function = Function(FUNC_VERIFY,
                Arrays.asList(),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Bytes16?>() {}, object : TypeReference<Utf8String?>() {}, object : TypeReference<Utf8String?>() {}))
        val results = FunctionReturnDecoder.decode(data, function.outputParameters)
        return Tuple3(
                results[0].value as ByteArray,
                results[1].value as String,
                results[2].value as String
        )
    }

    fun finished(): RemoteCall<Boolean> {
        val function = Function(FUNC_FINISHED,
                Arrays.asList(),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Bool?>() {}))
        return executeRemoteCallSingleValueReturn(function, Boolean::class.java)
    }

    fun finish(): RemoteCall<TransactionReceipt> {
        val function = Function(
                FUNC_FINISH,
                Arrays.asList(), emptyList())
        return executeRemoteCallTransaction(function)
    }

    fun finish(callback: TransactionSucCallback?) {
        val function = Function(
                FUNC_FINISH,
                Arrays.asList(), emptyList())
        asyncExecuteTransaction(function, callback)
    }

    fun finishSeq(): String {
        val function = Function(
                FUNC_FINISH,
                Arrays.asList(), emptyList())
        return createTransactionSeq(function)
    }

    companion object {
        val BINARY_ARRAY = arrayOf("608060405234801561001057600080fd5b5060405162001252380380620012528339810180604052810190808051820192919050505033600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806001908051906020019061008c929190610093565b5050610138565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100d457805160ff1916838001178555610102565b82800160010185558215610102579182015b828111156101015782518255916020019190600101906100e6565b5b50905061010f9190610113565b5090565b61013591905b80821115610131576000816000905550600101610119565b5090565b90565b61110a80620001486000396000f300608060405260043610610099576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630b929a2d1461009e5780632ae18dd8146101dd5780632b7ac3f3146103a15780633ec045a6146103f8578063747542821461044f5780638c72c54e146104df578063b25b1a1314610536578063bef4876b14610602578063d56b288914610631575b600080fd5b3480156100aa57600080fd5b506100c960048036038101908080359060200190929190505050610648565b60405180846fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561013957808201518184015260208101905061011e565b50505050905090810190601f1680156101665780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b8381101561019f578082015181840152602081019050610184565b50505050905090810190601f1680156101cc5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b3480156101e957600080fd5b5061021b60048036038101908080356fffffffffffffffffffffffffffffffff191690602001909291905050506107ce565b604051808060200180602001806020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001848103845288818151815260200191508051906020019080838360005b8381101561029557808201518184015260208101905061027a565b50505050905090810190601f1680156102c25780820380516001836020036101000a031916815260200191505b50848103835287818151815260200191508051906020019080838360005b838110156102fb5780820151818401526020810190506102e0565b50505050905090810190601f1680156103285780820380516001836020036101000a031916815260200191505b50848103825286818151815260200191508051906020019080838360005b83811015610361578082015181840152602081019050610346565b50505050905090810190601f16801561038e5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b3480156103ad57600080fd5b506103b6610a72565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561040457600080fd5b5061040d610a98565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561045b57600080fd5b50610464610abe565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104a4578082015181840152602081019050610489565b50505050905090810190601f1680156104d15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156104eb57600080fd5b506104f4610b5c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561054257600080fd5b5061060060048036038101908080356fffffffffffffffffffffffffffffffff19169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610b82565b005b34801561060e57600080fd5b50610617610df7565b604051808215151515815260200191505060405180910390f35b34801561063d57600080fd5b50610646610e0a565b005b60008181548110151561065757fe5b90600052602060002090600302016000915090508060000160009054906101000a90047001000000000000000000000000000000000290806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107265780601f106106fb57610100808354040283529160200191610726565b820191906000526020600020905b81548152906001019060200180831161070957829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107c45780601f10610799576101008083540402835291602001916107c4565b820191906000526020600020905b8154815290600101906020018083116107a757829003601f168201915b5050505050905083565b606080606060008060008091505b600080549050821015610a69576000828154811015156107f857fe5b90600052602060002090600302019050866fffffffffffffffffffffffffffffffff19168160000160009054906101000a9004700100000000000000000000000000000000026fffffffffffffffffffffffffffffffff19161415610a5c5760018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108ed5780601f106108c2576101008083540402835291602001916108ed565b820191906000526020600020905b8154815290600101906020018083116108d057829003601f168201915b50505050509550806001018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561098c5780601f106109615761010080835404028352916020019161098c565b820191906000526020600020905b81548152906001019060200180831161096f57829003601f168201915b50505050509450806002018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a2b5780601f10610a0057610100808354040283529160200191610a2b565b820191906000526020600020905b815481529060010190602001808311610a0e57829003601f168201915b50505050509350600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169250610a69565b81806001019250506107dc565b50509193509193565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b545780601f10610b2957610100808354040283529160200191610b54565b820191906000526020600020905b815481529060010190602001808311610b3757829003601f168201915b505050505081565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b3373ffffffffffffffffffffffffffffffffffffffff16600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515610c6d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260278152602001807fe58faae69c89e6a380e5ae9ae59198e58fafe4bba5e68f90e4baa4e6a380e5ae81526020017f9ae7bb93e69e9c0000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600260149054906101000a900460ff16151515610d18576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260338152602001807fe5a794e68998e58d95e5ae8ce68890e5908ee4b88de58fafe6b7bbe58aa0e6b081526020017fb4e8a1a8e79a84e6a380e5ae9ae7bb93e69e9c0000000000000000000000000081525060400191505060405180910390fd5b6000606060405190810160405280856fffffffffffffffffffffffffffffffff19168152602001848152602001838152509080600181540180825580915050906001820390600052602060002090600302016000909192909190915060008201518160000160006101000a8154816fffffffffffffffffffffffffffffffff0219169083700100000000000000000000000000000000900402179055506020820151816001019080519060200190610dd1929190611039565b506040820151816002019080519060200190610dee929190611039565b50505050505050565b600260149054906101000a900460ff1681565b3373ffffffffffffffffffffffffffffffffffffffff16600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515610ef5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001807fe58faae69c89e6a380e5ae9ae59198e58fafe4bba5e5ae8ce688", "90e5a794e68981526020017f98e58d950000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600260149054906101000a900460ff16151515610fa0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001807fe5a794e68998e58d95e5ae8ce68890e5908ee4b88de58fafe5868de6aca1e5ae81526020017f8ce688900000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6000808054905011151561101c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807fe7a9bae5a794e68998e58d95e4b88de58fafe4bba5e5ae8ce68890000000000081525060200191505060405180910390fd5b6001600260146101000a81548160ff021916908315150217905550565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061107a57805160ff19168380011785556110a8565b828001600101855582156110a8579182015b828111156110a757825182559160200191906001019061108c565b5b5090506110b591906110b9565b5090565b6110db91905b808211156110d75760008160009055506001016110bf565b5090565b905600a165627a7a72305820bd0a47e42309c4f49d09ab3fcf5c97834596372e2190da230b998c2ee613f2140029")
        val BINARY = java.lang.String.join("", *BINARY_ARRAY)
        val ABI_ARRAY = arrayOf("[{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"meterList\",\"outputs\":[{\"name\":\"meterId\",\"type\":\"bytes16\"},{\"name\":\"verifyTime\",\"type\":\"string\"},{\"name\":\"result\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_meterId\",\"type\":\"bytes16\"}],\"name\":\"getInfo\",\"outputs\":[{\"name\":\"_manufacturer\",\"type\":\"string\"},{\"name\":\"_verifyTime\",\"type\":\"string\"},{\"name\":\"_result\",\"type\":\"string\"},{\"name\":\"_verifier\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"verifier\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"auditor\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"manufacturer\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"publisher\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_meterId\",\"type\":\"bytes16\"},{\"name\":\"_verifyTime\",\"type\":\"string\"},{\"name\":\"_result\",\"type\":\"string\"}],\"name\":\"verify\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"finished\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"finish\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_manufacturer\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]")
        val ABI = java.lang.String.join("", *ABI_ARRAY)
        val transactionDecoder = TransactionDecoder(ABI, BINARY)
        const val FUNC_METERLIST = "meterList"
        const val FUNC_GETINFO = "getInfo"
        const val FUNC_VERIFIER = "verifier"
        const val FUNC_AUDITOR = "auditor"
        const val FUNC_MANUFACTURER = "manufacturer"
        const val FUNC_PUBLISHER = "publisher"
        const val FUNC_VERIFY = "verify"
        const val FUNC_FINISHED = "finished"
        const val FUNC_FINISH = "finish"

        @Deprecated("")
        fun load(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?): UserMeter {
            return UserMeter(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        @Deprecated("")
        fun load(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?): UserMeter {
            return UserMeter(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }

        fun load(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?): UserMeter {
            return UserMeter(contractAddress, web3j, credentials, contractGasProvider)
        }

        fun load(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?): UserMeter {
            return UserMeter(contractAddress, web3j, transactionManager, contractGasProvider)
        }

        fun deploy(web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?, _manufacturer: String?): RemoteCall<UserMeter> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(Utf8String(_manufacturer)))
            return deployRemoteCall(UserMeter::class.java, web3j, credentials, contractGasProvider, BINARY, encodedConstructor)
        }

        fun deploy(web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?, _manufacturer: String?): RemoteCall<UserMeter> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(Utf8String(_manufacturer)))
            return deployRemoteCall(UserMeter::class.java, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor)
        }

        @Deprecated("")
        fun deploy(web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?, _manufacturer: String?): RemoteCall<UserMeter> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(Utf8String(_manufacturer)))
            return deployRemoteCall(UserMeter::class.java, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor)
        }

        @Deprecated("")
        fun deploy(web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?, _manufacturer: String?): RemoteCall<UserMeter> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(Utf8String(_manufacturer)))
            return deployRemoteCall(UserMeter::class.java, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor)
        }
    }
}