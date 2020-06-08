package org.fisco.bcos.temp

import org.fisco.bcos.channel.client.TransactionSucCallback
import org.fisco.bcos.web3j.abi.TypeReference
import org.fisco.bcos.web3j.abi.datatypes.Function
import org.fisco.bcos.web3j.abi.datatypes.Type
import org.fisco.bcos.web3j.abi.datatypes.Utf8String
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.protocol.Web3j
import org.fisco.bcos.web3j.protocol.core.RemoteCall
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt
import org.fisco.bcos.web3j.tx.Contract
import org.fisco.bcos.web3j.tx.TransactionManager
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger
import java.util.*

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
class HelloWorld : Contract {
    @Deprecated("")
    protected constructor(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?) : super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit) {
    }

    protected constructor(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?) : super(BINARY, contractAddress, web3j, credentials, contractGasProvider) {}

    @Deprecated("")
    protected constructor(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?) : super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit) {
    }

    protected constructor(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?) : super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider) {}

    fun set(n: String?): RemoteCall<TransactionReceipt?> {
        val function = Function(
                FUNC_SET,
                Arrays.asList<Type<*>>(Utf8String(n)), emptyList())
        return executeRemoteCallTransaction(function)
    }

    operator fun set(n: String?, callback: TransactionSucCallback?) {
        val function = Function(
                FUNC_SET,
                Arrays.asList<Type<*>>(Utf8String(n)), emptyList())
        asyncExecuteTransaction(function, callback)
    }

    fun setSeq(n: String?): String {
        val function = Function(
                FUNC_SET,
                Arrays.asList<Type<*>>(Utf8String(n)), emptyList())
        return createTransactionSeq(function)
    }

    fun get(): RemoteCall<String?> {
        val function = Function(FUNC_GET,
                Arrays.asList(),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Utf8String?>() {}))
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    companion object {
        const val BINARY = "608060405234801561001057600080fd5b506040805190810160405280600d81526020017f48656c6c6f2c20576f726c6421000000000000000000000000000000000000008152506000908051906020019061005c929190610062565b50610107565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100a357805160ff19168380011785556100d1565b828001600101855582156100d1579182015b828111156100d05782518255916020019190600101906100b5565b5b5090506100de91906100e2565b5090565b61010491905b808211156101005760008160009055506001016100e8565b5090565b90565b6102d7806101166000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680634ed3885e146100515780636d4ce63c146100ba575b600080fd5b34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061014a565b005b3480156100c657600080fd5b506100cf610164565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561010f5780820151818401526020810190506100f4565b50505050905090810190601f16801561013c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b8060009080519060200190610160929190610206565b5050565b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101fc5780601f106101d1576101008083540402835291602001916101fc565b820191906000526020600020905b8154815290600101906020018083116101df57829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061024757805160ff1916838001178555610275565b82800160010185558215610275579182015b82811115610274578251825591602001919060010190610259565b5b5090506102829190610286565b5090565b6102a891905b808211156102a457600081600090555060010161028c565b5090565b905600a165627a7a72305820f4ddb4341c4a42ef27536ba77c299d0bfba52fd072e427a55c894494074291dd0029"
        const val ABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"n\",\"type\":\"string\"}],\"name\":\"set\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]"
        const val FUNC_SET = "set"
        const val FUNC_GET = "get"

        @Deprecated("")
        fun load(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?): HelloWorld {
            return HelloWorld(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        @Deprecated("")
        fun load(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?): HelloWorld {
            return HelloWorld(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }

        fun load(contractAddress: String?, web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?): HelloWorld {
            return HelloWorld(contractAddress, web3j, credentials, contractGasProvider)
        }

        fun load(contractAddress: String?, web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?): HelloWorld {
            return HelloWorld(contractAddress, web3j, transactionManager, contractGasProvider)
        }

        fun deploy(web3j: Web3j?, credentials: Credentials?, contractGasProvider: ContractGasProvider?): RemoteCall<HelloWorld> {
            return deployRemoteCall(HelloWorld::class.java, web3j, credentials, contractGasProvider, BINARY, "")
        }

        fun deploy(web3j: Web3j?, transactionManager: TransactionManager?, contractGasProvider: ContractGasProvider?): RemoteCall<HelloWorld> {
            return deployRemoteCall(HelloWorld::class.java, web3j, transactionManager, contractGasProvider, BINARY, "")
        }

        @Deprecated("")
        fun deploy(web3j: Web3j?, credentials: Credentials?, gasPrice: BigInteger?, gasLimit: BigInteger?): RemoteCall<HelloWorld> {
            return deployRemoteCall(HelloWorld::class.java, web3j, credentials, gasPrice, gasLimit, BINARY, "")
        }

        @Deprecated("")
        fun deploy(web3j: Web3j?, transactionManager: TransactionManager?, gasPrice: BigInteger?, gasLimit: BigInteger?): RemoteCall<HelloWorld> {
            return deployRemoteCall(HelloWorld::class.java, web3j, transactionManager, gasPrice, gasLimit, BINARY, "")
        }
    }
}